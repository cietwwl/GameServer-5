package com.mi.game.module.pay.servlet.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.mi.core.util.ConfigUtil;
import com.mi.game.module.base.bean.init.server.ServerInfoData;
import com.mi.game.module.pay.PayModule;
import com.mi.game.module.pay.define.PlatFromConstants;
import com.mi.game.module.pay.pojo.PayOrderEntity;
import com.mi.game.module.pay.servlet.BasePay;
import com.mi.game.util.Base64;
import com.mi.game.util.Base64Coder;
import com.mi.game.util.Digest;
import com.mi.game.util.HmacSHA1Encryption;
import com.mi.game.util.Logs;

/**
 * 腾讯支付callback
 */
public class Android_TencentPay extends BasePay {

	// baidu
	public final static String TENCENT_APP_ID = ConfigUtil
			.getString("tencent.appid");
	public final static String TENCENT_APP_KEY = ConfigUtil
			.getString("tencent.appkey");
	public final static String CALL_BACK_URL = "/pay/MiGamePay.do";

	// sig计算需要过滤掉的参数,即不参与签名
	private static String[] filter = { "sig" };

	private static List<String> filterList = Arrays.asList(filter);

	private static JSONObject json = new JSONObject();

	public static void pay(HttpServletRequest request, HttpServletResponse response) throws IOException {

		int ResultCode = 0;
		String ResultMsg = "OK";
		// 请求参数
		Map<String, String> requestParams = getRequestMap(request);
		try {

			// 得到交易token
			String token = requestParams.get("token");

			String methodType = request.getMethod().toUpperCase(); // 得到方法类型
			String signature = requestParams.get("sig"); // 需要进行解码
			if (signature != null) {
				signature = URLDecoder.decode(signature, "UTF-8");
			}
			String localSig = getSig(requestParams, methodType);
			Logs.pay.error("腾讯平台callback数据：" + requestParams);
			Logs.pay.error("腾讯平台callback签名(sig)：" + signature);
			Logs.pay.error("本地计算签名(localSig)：" + localSig);

			// 签名校验
			if (!doCheck(localSig, signature)) {
				Logs.pay.error("订单: 签名验证错误");
				ResultMsg = "签名验证错误";
				ResultCode = 5;
				writerResult(response, ResultCode, ResultMsg);
				return;
			}

			// 订单信息
			String appmeta = requestParams.get("appmeta");
			// 如果是支付服务器,则分发回调请求
			if (PAYCENTER) {
				// TODO 自定义参数
				if (StringUtils.isEmpty(appmeta)) {
					ResultMsg = "未找到平台透传消息,分发支付回调请求失败!";
					writerResult(response, 1, ResultMsg);
					return;
				}
				String serverID = appmeta.split("-")[0];
				ServerInfoData serverInfo = PayModule.serverListMap.get(serverID);
				String address = serverInfo.getUrl();
				Map<String, String> params = new HashMap<String, String>();
				Enumeration<?> names = request.getParameterNames();
				while (names.hasMoreElements()) {
					String name = (String) names.nextElement();
					params.put(name, request.getParameter(name));
				}
				String result = sendRequest(address, params);
				response.getWriter().write(result);
				return;
			}
			// CP订单号
			String tencentOrderID = appmeta.split("-")[1];
			// 订单状态
			// int OrderStatus = contentJson.getInteger("OrderStatus");
			//
			// if (OrderStatus != 1) {
			// // 订单状态 0:失败 1:成功
			// Logs.pay.error("订单：" + CooperatorOrderSerial + " 状态不为1");
			// ResultMsg = "失败订单";
			// writerResult(response, ResultCode, ResultMsg);
			// return;
			// }

			PayOrderEntity orderEntity = payModule
					.getPayOrderEntity(tencentOrderID);

			if (orderEntity == null) {
				Logs.pay.error("订单：" + tencentOrderID + "未找到");
				ResultMsg = "未知订单";
				ResultCode = 2;
				writerResult(response, ResultCode, ResultMsg);
				return;
			}

			// 卡类支付可能造成,订单金额不一致,故去掉订单金额验证
			// // 订单金额
			// Double OrderMoney = contentJson.getDouble("OrderMoney");
			// if (orderEntity.getPayMoney() != OrderMoney.intValue()) {
			// Logs.pay.error("订单：" + CooperatorOrderSerial + "充值金额不正确");
			// ResultMsg = "充值金额不正确";
			// String resultSign = Utilities.encrypt(BD_APP_ID + ResultCode +
			// BD_APP_SECRET);
			// writerResult(response, ResultCode, ResultMsg, resultSign);
			// return;
			// }

			// 订单状态不是初始状态
			if (orderEntity.getState() != 0) {
				Logs.pay.error("订单：" + tencentOrderID + " 不是初始状态");
				ResultMsg = "订单状态不正确";
				ResultCode = 3;
				writerResult(response, ResultCode, ResultMsg);
				return;
			}

			// 订单初始状态
			if (orderEntity.getState() == 0) {
				Logs.pay.error("订单：" + tencentOrderID + "支付完成，开始添加玩家元宝");
				orderEntity.setState(1);
				orderEntity.setCallbackTime(System.currentTimeMillis());
				orderEntity.setPayPlatForm(PlatFromConstants.PLATFORM_BAIDU);
				payModule.savePayOrderEntity(orderEntity);
				// 处理充值
				payModule.payGold(orderEntity.getPlayerID(), orderEntity, null,
						PlatFromConstants.PLATFORM_BAIDU, null);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			ResultCode = 9999;
			ResultMsg = "CP服务器内部错误";
		}
		json.put("token", requestParams.get("token"));
		writerResult(response, ResultCode, ResultMsg);
	}

	/**
	 * 得到sig内容(未解码前的)
	 * 
	 * @param params
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private static String getSig(Map<String, String> params, String methodType)
			throws UnsupportedEncodingException {
		StringBuffer content = new StringBuffer();
		// 对回调地址进行编码
		List<String> keys = new ArrayList<String>(params.keySet());
		StringBuffer sb = new StringBuffer(""); // 拼接参数
		// 按照自然升序处理
		Collections.sort(keys);
		for (int i = 0; i < keys.size(); i++) {
			String key = (String) keys.get(i);
			if (filterList.contains(key)) {
				continue;
			}
			String value = (String) params.get(key);
			if (value != null) {
				sb.append((i == 0 ? "" : "&") + key + "=" + value);
			} else {
				sb.append((i == 0 ? "" : "&") + key + "=");
			}
		}
		// 将sb中的内容进行转码
		content.append(sb.toString());
		String result = methodType
				+ "&"
				+ URLEncoder.encode(CALL_BACK_URL, "UTF-8")
				+ "&"
				+ URLEncoder.encode(content.toString(), "UTF-8").replaceAll(
						"\\*", "%2A"); // *号不会被转码,手动进行替换
		return result;
	}

	/**
	 * 验证本地生成的sig和腾讯支付服务器传回来的sig是否一致, 编码格式UTF-8
	 * 
	 * @param content
	 *            本地生成的sig
	 * @param sign
	 *            服务器传回来的sig
	 * @return
	 */
	public static boolean doCheck(String content, String sign) {
		try {
			// 1.得到密钥
			String encryptKey = TENCENT_APP_KEY + "&";
			// 2. 使用HMAC-SHA1加密算法，使用密钥对content加密。
			String result = HmacSHA1Encryption.hmacSHA1Encrypt(content, sign);
			String lastSig = Base64.encode(result.getBytes());
			if (lastSig.equals(sign)) {
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}

	private static void writerResult(HttpServletResponse response, int ret,
			String msg) throws IOException {
		json.put("ret", ret);
		json.put("msg", msg);
		Logs.pay.error("返回平台数据：" + json.toJSONString());
		response.getWriter().write(json.toJSONString());
	}
	
	public static void main(String[] args) throws Exception {
		// appid=123456&format=json&openid=11111111111111111&openkey=2222222222222222&pf=qzone&userip=112.90.139.30
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("appid", "123456");
		paramsMap.put("format", "json");
		paramsMap.put("openid", "11111111111111111");
		paramsMap.put("openkey", "2222222222222222");
		paramsMap.put("pf", "qzone");
		paramsMap.put("userip", "112.90.139.30");
		paramsMap.put("sig", "orap8I6F5MV%2BIywhnrY0XcX1SD0%3D");
		String qqSign = paramsMap.get("sig");
		String localSig = getSig(paramsMap, "GET");
		doCheck(localSig, qqSign);

		String yuan = "GET&%2Fv3%2Fuser%2Fget_info&appid%3D12345%26format%3Djson%26openid%3D12345%26openkey%3D12345%26pf%3Dqzone%26userip%3D10.0.0.1";
		String key = "123&";

		String jiami = Digest.hmacSHASign(yuan, key, "UTF-8");

		System.out.println(Base64Coder.encodeString(jiami));

	}
	
}