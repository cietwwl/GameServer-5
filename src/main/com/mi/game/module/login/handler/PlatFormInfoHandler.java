package com.mi.game.module.login.handler;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.ErrorIds;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.login.LoginModule;
import com.mi.game.module.login.pojo.MinuEntity;
import com.mi.game.module.login.protocol.LoginInfoProtocol;
import com.mi.game.module.pay.servlet.BasePay;
import com.mi.game.module.pay.servlet.impl.Android_CuccPay;
import com.mi.game.module.pay.servlet.impl.Android_LenovoPay;
import com.mi.game.module.pay.servlet.impl.Android_MuzhiwanPay;
import com.mi.game.module.pay.servlet.impl.Android_Qihu360Pay;
import com.mi.game.module.pay.servlet.impl.Ios_KuaiYongPay;
import com.mi.game.module.pay.servlet.impl.Ios_PPToolsPay;
import com.mi.game.util.Logs;
import com.mi.game.util.Utilities;

/**
 * 获取平台uid
 *
 */
@HandlerType(type = HandlerIds.getPlatFormInfo)
public class PlatFormInfoHandler extends BaseHandler {
	@Override
	public void execute(IOMessage ioMessage) {
		LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule, LoginModule.class);

		LoginInfoProtocol protocol = new LoginInfoProtocol();
		ioMessage.setProtocol(protocol);

		String code = (String) ioMessage.getInputParse("code");
		String platform = (String) ioMessage.getInputParse("platform");

		if (StringUtils.isBlank(code) || StringUtils.isBlank(platform)) {
			Logs.logger.error("code=" + code + ",platform=" + platform);
			protocol.setCode(ErrorIds.ParamWrong);
			return;
		}
		platform = platform.toLowerCase();

		String uid = "";

		switch (platform) {
		case "360":
			String accessToken360 = get360AccessToken(code);
			uid = get360Uid(accessToken360);
			break;
		case "kuaiyong":
			uid = getKuaiyongUid(code);
			break;
		case "ppzhushou":
			uid = getPPzhushouUid(code);
			break;
		case "aisi":
			uid = getAisiUid(code);
			break;
		case "muzhiwan":
			uid = getMuzhiwanUid(code);
			break;
		case "lenovo":
			uid = getLenovoUid(code);
			break;
		case "cucc":
			uid = getCuccUid(code);
			break;
		}

		if (StringUtils.isBlank(uid)) {
			logger.error("获取用户id错误! code=" + code + ",platform=" + platform);
			throw new IllegalArgumentException(ErrorIds.SERVER_ERROR + "");
		}

		// 返回uid给客户端
		protocol.setUid(uid);

		// 防uid重复 处理
		uid = platform + uid;
		uid = uid.toLowerCase();
		// 用户注册
		if (!loginModule.chechkUserPasswd(uid, uid, protocol,true)) { // 如果用户不存在
			loginModule.registerUser(uid, uid, null, true);
			// 删档测试记录第三方平台用户id
			MinuEntity minuEntity = loginModule.getMinuEntity(uid);
			if (minuEntity == null) {
				minuEntity = new MinuEntity();
				minuEntity.setMinu_uid(uid);
				minuEntity.setDateTime(System.currentTimeMillis());
				loginModule.saveMinuEntity(minuEntity);
			}
		}
	}

	// 联想获取uid
	private String getLenovoUid(String code) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("lpsust", code);
		params.put("realm", Android_LenovoPay.APP_ID);
		String result = BasePay.sendPlatFormRequest("http://passport.lenovo.com/interserver/authen/1.2/getaccountid?", params);
		String uid = "";
		String begin = "<AccountID>";
		String end = "</AccountID>";
		if (StringUtils.isNotEmpty(result) && result.indexOf(begin) != 1) {
			uid = result.substring(result.indexOf(begin) + begin.length(), result.indexOf(end));
		}
		return uid;
	}

	// 拇指玩获取uid
	private String getMuzhiwanUid(String code) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("token", code);
		params.put("appkey", Android_MuzhiwanPay.API_KEY);
		String result = BasePay.sendPlatFormRequest("http://sdk.muzhiwan.com/oauth2/getuser.php?", params);
		JSONObject json = JSON.parseObject(result);
		String uid = "";
		if ("1".equals(json.getString("code"))) {
			JSONObject userInfo = json.getJSONObject("user");
			uid = userInfo.getString("uid");
		}
		return uid;
	}

//	// 酷派获取uid
//	private String getCoolPadUid(String code) {
//		Map<String, String> params = new HashMap<String, String>();
//		params.put("grant_type", "authorization_code");
//		params.put("client_id", Android_CoolPadPay.appID);
//		params.put("client_secret", Android_CoolPadPay.appKey);
//		params.put("code", code);
//		params.put("redirect_uri", Android_CoolPadPay.appKey);	
//		String result = BasePay.sendPlatFormRequest("https://openapi.coolyun.com/oauth2/token", params);
//		JSONObject json = JSON.parseObject(result);
//		String uid = "";
//		uid = json.getString("access_token");
//		return uid;
//		}
	
	// aisi助手获取uid
	private static String getAisiUid(String code) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("token", code);
		String result = BasePay.sendPlatFormRequest("https://pay.i4.cn/member_third.action?", params);
		String uid = "";
		if(StringUtils.isEmpty(result)){
			return uid;
		}
		JSONObject json = JSON.parseObject(result);
		uid = json.getString("userid");
		return uid;
	}

	// pp助手获取uid
	private static String getPPzhushouUid(String code) {
		JSONObject requestData = new JSONObject();
		requestData.put("id", System.currentTimeMillis() / 1000);
		requestData.put("service", "account.verifySession");
		JSONObject data = new JSONObject();
		data.put("sid", code);
		requestData.put("data", data);
		requestData.put("sign", Utilities.encrypt(code + Ios_PPToolsPay.PPZHUSHOU_APP_ID).toLowerCase());
		String result = BasePay.sendPost("http://passport_i.25pp.com:8080/account?tunnel-command=2852126760", requestData.toJSONString());
		JSONObject json = JSON.parseObject(result);
		JSONObject state = json.getJSONObject("state");
		String uid = "";
		if (state.getInteger("code") == 1) {
			uid = json.getJSONObject("data").getString("accountId");
		}
		return uid;
	}

	// 快用获取uid
	private static String getKuaiyongUid(String code) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("tokenKey", code);
		String sign = Utilities.encrypt(Ios_KuaiYongPay.KUAIYONG_APP_KEY + code);
		params.put("sign", sign);
		String result = BasePay.sendPlatFormRequest("http://f_signin.bppstore.com/loginCheck.php?", params);
		JSONObject json = JSON.parseObject(result);
		int resultCode = json.getIntValue("code");
		String uid = "";
		if (resultCode == 0) {
			uid = json.getJSONObject("data").getString("guid");
		}
		return uid;
	}
	
	// 联通获取uid
	private static String getCuccUid(String code) {
		JSONObject params = new JSONObject();		
		params.put("access_token", code);
		params.put("client_id", Android_CuccPay.CUCC_CLIENT_ID);
		params.put("client_secret", Android_CuccPay.CUCC_CLIENT_SECRET);
		String result = BasePay.sendPost("https://open.wostore.cn/oauth2/auth/validate_oauth2_cp", params.toJSONString());		
		String uid = "";
		if(StringUtils.isEmpty(result)){
			return uid;
		}
		JSONObject json = JSON.parseObject(result);
		uid = json.getString("user_id");
		return uid;
	}

	private static String get360AccessToken(String code) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("grant_type", "authorization_code");
		params.put("code", code);
		params.put("client_id", Android_Qihu360Pay.QH360_APP_ID);
		params.put("client_secret", Android_Qihu360Pay.QH360_APP_KEY);
		params.put("redirect_uri", "oob");
		String result = BasePay.sendPlatFormRequest("https://openapi.360.cn/oauth2/access_token", params);
		JSONObject json = JSON.parseObject(result);
		String accessToken = json.getString("access_token");
		return accessToken;
	}

	private static String get360Uid(String accessToken) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", accessToken);
		params.put("fields", "id");
		String result = BasePay.sendPlatFormRequest("https://openapi.360.cn/user/me", params);
		JSONObject json = JSON.parseObject(result);
		String uid = json.getString("id");
		return uid;
	}

}
