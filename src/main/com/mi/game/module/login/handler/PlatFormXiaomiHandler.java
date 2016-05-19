package com.mi.game.module.login.handler;

import java.util.HashMap;
import java.util.Map;

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
import com.mi.game.module.pay.servlet.impl.Android_XiaomiPay;
import com.mi.game.util.HttpUtil;
import com.mi.game.util.Logs;

/**
 * 小米平台校验用户登录
 * 
 * @author Administrator
 *
 */
@HandlerType(type = HandlerIds.VerifyMiuiSession)
public class PlatFormXiaomiHandler extends BaseHandler {

	private static String verifySessionUrl = "http://mis.migc.xiaomi.com/api/biz/service/verifySession.do";

	@Override
	public void execute(IOMessage ioMessage) {
		LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule, LoginModule.class);

		LoginInfoProtocol protocol = new LoginInfoProtocol();
		ioMessage.setProtocol(protocol);

		String uid = "";
		String miuiSession = "";

		if (ioMessage.getInputParse("minu_uid") != null) {
			uid = ioMessage.getInputParse("minu_uid").toString();
		}
		if (ioMessage.getInputParse("minu_session") != null) {
			miuiSession = ioMessage.getInputParse("minu_session").toString();
		}

		// 如果验证session正确
		if (verify(uid, miuiSession)) {
			uid = "Xiaomi" + uid;
			uid = uid.toLowerCase();
			if (!loginModule.chechkUserPasswd(uid, uid, protocol, true)) { // 如果用户不存在
				loginModule.registerUser(uid, uid, null, true);
				// 删档测试记录小米平台用户id
				MinuEntity minuEntity = loginModule.getMinuEntity(uid);
				if (minuEntity == null) {
					minuEntity = new MinuEntity();
					minuEntity.setMinu_uid(uid);
					minuEntity.setDateTime(System.currentTimeMillis());
					loginModule.saveMinuEntity(minuEntity);
				}
			}
		} else {
			Logs.logger.info("小米登录session校验失败!");
			throw new IllegalArgumentException(ErrorIds.SERVER_ERROR + "");
		}

	}

	private static String getRequestUrl(Map<String, String> params, String baseUrl) throws Exception {
		String signString = Android_XiaomiPay.getSortQueryString(params);
		String signature = Android_XiaomiPay.getSign(params);
		return baseUrl + "?" + signString + "&signature=" + signature;
	}

	public static boolean verify(String userId, String session) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("appId", String.valueOf(Android_XiaomiPay.MIUI_APP_ID));
		params.put("uid", userId);
		params.put("session", session);
		try {
			String requestUrl = getRequestUrl(params, verifySessionUrl);
			String result = HttpUtil.doGet(requestUrl);
			JSONObject obj = JSON.parseObject(result);
			int errcode = obj.getIntValue("errcode");
			if (200 == errcode) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
