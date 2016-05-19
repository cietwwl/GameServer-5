package com.mi.game.module.pay.servlet.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.mi.core.util.ConfigUtil;
import com.mi.game.module.base.bean.init.server.ServerInfoData;
import com.mi.game.module.pay.PayModule;
import com.mi.game.module.pay.define.PlatFromConstants;
import com.mi.game.module.pay.pojo.PayOrderEntity;
import com.mi.game.module.pay.servlet.BasePay;
import com.mi.game.util.Base64;
import com.mi.game.util.Logs;
import com.mi.game.util.RSAEncrypt;

/**
 * 爱思支付成功callback
 */
public class Ios_AiSiPay extends BasePay {

	private static final String rsaPublicKey = ConfigUtil.getString("aisi.rsaPublicKey");

	public static void pay(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String resultMsg = "success";
		try {
			// 请求参数
			Map<String, String> requestParams = getRequestMap(request);
			Logs.pay.error("爱思callback数据：" + requestParams);
			// 签名验证
			if (!verifySignature(requestParams)) {
				Logs.pay.error("订单：签名验证错误");
				writerResult(response, "fail");
				return;
			}

			// 游戏合作商自定义参数
			String callbackInfo = requestParams.get("zone");
			// 如果是支付服务器,则分发回调请求
			if (PAYCENTER) {
				// 透传信息
				String cpUserInfo = callbackInfo;
				if (StringUtils.isEmpty(cpUserInfo)) {
					Logs.pay.error("未找到平台透传消息,分发支付回调请求失败!");
					writerResult(response, "fail");
					return;
				}
				String serverID = cpUserInfo.split("-")[0];
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

			// 充值订单号
			String orderId = requestParams.get("billno");

			// 订单状态
			String status = requestParams.get("status");

			if (!"0".equals(status)) {
				Logs.pay.error("订单：" + orderId + "已兑换过并成功返回");
				writerResult(response, resultMsg);
				return;
			}

			PayOrderEntity orderEntity = payModule.getPayOrderEntity(orderId);

			if (orderEntity == null) {
				Logs.pay.error("订单：" + orderId + "未找到");
				writerResult(response, resultMsg);
				return;
			}

			// 订单状态不是初始状态
			if (orderEntity.getState() != 0) {
				Logs.pay.error("订单：" + orderId + " 不是初始状态");
				writerResult(response, resultMsg);
				return;
			}

			// 订单初始状态
			if (orderEntity.getState() == 0) {
				Logs.pay.error("订单：" + orderId + "支付完成，开始添加玩家元宝");
				orderEntity.setState(1);
				orderEntity.setCallbackTime(System.currentTimeMillis());
				orderEntity.setPayPlatForm(PlatFromConstants.PLATFORM_AISI);
				payModule.savePayOrderEntity(orderEntity);
				// 处理充值
				payModule.payGold(orderEntity.getPlayerID(), orderEntity, null, PlatFromConstants.PLATFORM_AISI, null);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			resultMsg = "fail";
		}
		writerResult(response, resultMsg);
	}

	/**
	 * 异步通知消息验证
	 * 
	 * @param para
	 *            异步通知消息
	 * @return 验证结果
	 */
	private static boolean verifySignature(Map<String, String> para) {
		try {
			String respSignature = para.get("sign");
			// 除去数组中的空值和签名参数
			Map<String, String> filteredReq = paraFilter(para);
			Map<String, String> signature = parseSignature(respSignature);
			for (String key : filteredReq.keySet()) {
				String value = filteredReq.get(key);
				String signValue = signature.get(key);
				if (!value.equals(signValue)) {
					return false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 除去请求要素中的空值和签名参数
	 * 
	 * @param para
	 *            请求要素
	 * @return 去掉空值与签名参数后的请求要素
	 */
	private static Map<String, String> paraFilter(Map<String, String> para) {
		Map<String, String> result = new HashMap<String, String>();
		if (para == null || para.size() <= 0) {
			return result;
		}
		for (String key : para.keySet()) {
			String value = para.get(key);
			if (value == null || value.equals("") || key.equalsIgnoreCase("sign") || key.equals("platForm")) {
				continue;
			}
			result.put(key, value);
		}
		return result;
	}

	/**
	 * 解析应答字符串，生成应答要素
	 * 
	 * @param str
	 *            需要解析的字符串
	 * @return 解析的结果map
	 * @throws UnsupportedEncodingException
	 */
	private static Map<String, String> parseQString(String str) {
		Map<String, String> map = new HashMap<String, String>();
		int len = str.length();
		StringBuilder temp = new StringBuilder();
		char curChar;
		String key = null;
		boolean isKey = true;
		for (int i = 0; i < len; i++) {// 遍历整个带解析的字符串
			curChar = str.charAt(i);// 取当前字符
			if (curChar == '&') {// 如果读取到&分割符
				putKeyValueToMap(temp, isKey, key, map);
				temp.setLength(0);
				isKey = true;
			} else {
				if (isKey) {// 如果当前生成的是key
					if (curChar == '=') {// 如果读取到=分隔符
						key = temp.toString();
						temp.setLength(0);
						isKey = false;
					} else {
						temp.append(curChar);
					}
				} else {// 如果当前生成的是value
					temp.append(curChar);
				}
			}
		}
		putKeyValueToMap(temp, isKey, key, map);
		return map;
	}

	/**
	 * 生成签名
	 * 
	 * @param req
	 *            需要解析签名的要素
	 * @return 解析签名的结果map
	 * @throws Exception
	 */
	private static Map<String, String> parseSignature(String sign) throws Exception {
		byte[] dcDataStr = Base64.decode(sign);
		RSAEncrypt rsaEncrypt = new RSAEncrypt();
		rsaEncrypt.loadPublicKey(rsaPublicKey);
		byte[] plainData = rsaEncrypt.decrypt(rsaEncrypt.getPublicKey(), dcDataStr);
		String parseString = new String(plainData);
		return parseQString(parseString);
	}

	private static void putKeyValueToMap(StringBuilder temp, boolean isKey, String key, Map<String, String> map) {
		if (isKey) {
			key = temp.toString();
			if (key.length() == 0) {
				throw new RuntimeException("QString format illegal");
			}
			map.put(key, "");
		} else {
			if (key.length() == 0) {
				throw new RuntimeException("QString format illegal");
			}
			map.put(key, temp.toString());
		}
	}

	private static void writerResult(HttpServletResponse response, String resultMsg) throws IOException {
		Logs.pay.error("返回平台数据：" + resultMsg);
		response.getWriter().write(resultMsg);
	}

}
