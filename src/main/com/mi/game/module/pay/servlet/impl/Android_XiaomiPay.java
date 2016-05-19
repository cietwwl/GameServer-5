package com.mi.game.module.pay.servlet.impl;

import java.io.IOException;
import java.util.Arrays;
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
import com.mi.game.util.HmacSHA1Encryption;
import com.mi.game.util.Logs;

/**
 * 小米支付成功callback
 */
public class Android_XiaomiPay extends BasePay {

	// 小米
	public final static String MIUI_APP_ID = ConfigUtil.getString("miui.appId");
	public final static String MIUI_APP_KEY = ConfigUtil.getString("miui.appKey");
	public final static String MIUI_APP_SECRET = ConfigUtil.getString("miui.appSecret");

	private static String[] filter = {
			"platForm","signature"
	};

	public static void pay(HttpServletRequest request, HttpServletResponse response) throws IOException {

		int errcode = 200;
		String errMsg = "";

		try {
			// 请求参数
			Map<String, String> requestParams = getRequestMap(request);
			String cpUserInfo = requestParams.get("cpUserInfo");
			String signature = requestParams.get("signature");
			String localSignature = getSign(requestParams);
			Logs.pay.error("小米平台callback数据：" + requestParams);
			Logs.pay.error("小米平台callback签名(signature)：" + signature);
			Logs.pay.error("本地计算签名(localSignature)：" + localSignature);

			// 签名校验
			if (!localSignature.equals(signature)) {
				errcode = 1525;
				errMsg = "签名验证错误";
				writerResult(response, errcode, errMsg);
				return;
			}

			// 如果是支付服务器,则分发回调请求
			if (PAYCENTER) {
				// 开发商透传信息
				if (StringUtils.isEmpty(cpUserInfo)) {
					errMsg = "未找到平台透传消息,分发支付回调请求失败!";
					writerResult(response, 404, errMsg);
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

			// 开发商订单ID
			String cpOrderId = requestParams.get("cpOrderId");
			// 订单状态 代表成功
			String orderStatus = requestParams.get("orderStatus");

			if (!"TRADE_SUCCESS".equals(orderStatus)) {
				// 订单状态，TRADE_SUCCESS 代表成功
				Logs.pay.error("订单：" + cpOrderId + " 支付失败");
				errMsg = "支付失败";
				writerResult(response, errcode, errMsg);
				return;
			}

			PayOrderEntity orderEntity = payModule.getPayOrderEntity(cpOrderId);
			if (orderEntity == null) {
				Logs.pay.error("订单：" + cpOrderId + " 未知订单");
				errMsg = "未知订单";
				writerResult(response, errcode, errMsg);
				return;
			}

// // 订单金额
// String payFee = requestParams.get("payFee");
// int money = Integer.parseInt(payFee); // 交易金额
// if (orderEntity.getPayMoney() * 100 != money) {
// Logs.pay.error("订单：" + orderId + "充值金额不正确");
// writerResult(response, errcode, errMsg);
// return;
// }

			// 订单状态不是初始状态
			if (orderEntity.getState() != 0) {
				Logs.pay.error("订单：" + cpOrderId + " 状态不正确");
				writerResult(response, errcode, errMsg);
				return;
			}

			// 订单初始状态
			if (orderEntity.getState() == 0) {
				Logs.pay.error("订单：" + cpOrderId + " 支付完成，开始添加玩家元宝");
				orderEntity.setState(1);
				orderEntity.setCallbackTime(System.currentTimeMillis());
				orderEntity.setPayPlatForm(PlatFromConstants.PLATFORM_XIAOMI);
				payModule.savePayOrderEntity(orderEntity);
				// 处理充值
				payModule.payGold(orderEntity.getPlayerID(), orderEntity, null, PlatFromConstants.PLATFORM_XIAOMI, null);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			errcode = 9999;
			errMsg = "CP服务器内部错误";
		}
		writerResult(response, errcode, errMsg);
	}

	public static String getSign(Map<String, String> params) throws Exception {
		String signString = getSortQueryString(params);
		return HmacSHA1Encryption.hmacSHA1Encrypt(signString, MIUI_APP_SECRET);
	}

	public static String getSortQueryString(Map<String, String> params) throws Exception {
		Object[] keys = params.keySet().toArray();
		Arrays.sort(keys);
		StringBuffer sb = new StringBuffer();
		List<String> filterList = Arrays.asList(filter);
		for (Object key : keys) {
			String param = String.valueOf(key);
			if (filterList.contains(param)) {
				continue;
			}
			sb.append(String.valueOf(key)).append("=").append(params.get(String.valueOf(key))).append("&");
		}
		String text = sb.toString();
		if (text.endsWith("&")) {
			text = text.substring(0, text.length() - 1);
		}
		return text;
	}

	private static void writerResult(HttpServletResponse response, int errcode, String errMsg) throws IOException {
		JSONObject json = new JSONObject();
		json.put("errcode", errcode);
		json.put("errMsg", errMsg);
		Logs.pay.error("返回平台数据：" + json.toJSONString());
		response.getWriter().write(json.toJSONString());
	}

}
