package com.mi.game.module.pay.servlet.impl;

import java.io.IOException;
import java.util.ArrayList;
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
import com.mi.game.util.Logs;
import com.mi.game.util.Utilities;

/**
 * i苹果支付成功callback
 */
public class Ios_IapplePay extends BasePay {

	public static final String gameKey = ConfigUtil.getString("iapple.gameKey");

	public static final String secretKey = ConfigUtil.getString("iapple.secretKey");

	public static void pay(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String status = "0";
		String transaction = request.getParameter("transaction");
		String payType = request.getParameter("payType");
		try {
			Map<String, String> requestParams = getRequestMap(request);

			// 签名参数
			String signature = requestParams.get("_sign");
			String localSignature = getSign(requestParams);
			Logs.pay.error("i苹果平台callback数据：" + requestParams);
			Logs.pay.error("i苹果平台callback签名(signature)：" + signature);
			Logs.pay.error("本地计算签名(localSignature)：" + localSignature);

			// 签名校验
			if (!signature.equals(localSignature)) {
				Logs.pay.error("订单:签名验证错误");
				writerResult(response, "1", transaction);
				return;
			}
			// 如果是支付服务器,则分发回调请求
			if (PAYCENTER) {
				// 开发商透传信息
				String cpUserInfo = requestParams.get("gameExtend");
				if (StringUtils.isEmpty(cpUserInfo)) {
					Logs.pay.error("未找到平台透传消息,分发支付回调请求失败!");
					writerResult(response, "1", transaction);
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

			// 订单号
			String trade_no = requestParams.get("gameExtend").split("-")[1];

			// 支付结果
			String payresult = requestParams.get("status");
			if (!"1".equals(payresult)) {
				Logs.pay.error("订单：" + trade_no + "支付失败");
				writerResult(response, status, transaction);
				return;
			}

			PayOrderEntity orderEntity = payModule.getPayOrderEntity(trade_no);
			if (orderEntity == null) {
				Logs.pay.error("订单：" + trade_no + "未知订单");
				writerResult(response, status, transaction);
				return;
			}

			// 订单状态不是初始状态
			if (orderEntity.getState() != 0) {
				Logs.pay.error("订单：" + trade_no + "状态不正确");
				writerResult(response, status, transaction);
				return;
			}

			// 订单初始状态
			if (orderEntity.getState() == 0) {
				Logs.pay.error("订单：" + trade_no + "支付完成，开始添加玩家元宝");
				orderEntity.setState(1);
				orderEntity.setCallbackTime(System.currentTimeMillis());
				orderEntity.setPayPlatForm(PlatFromConstants.PLATFORM_IAPPLE);
				payModule.savePayOrderEntity(orderEntity);
				// 处理充值
				payModule.payGold(orderEntity.getPlayerID(), orderEntity, payType, PlatFromConstants.PLATFORM_IAPPLE, null);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			status = "1";
		}
		writerResult(response, status, transaction);
	}

	public static String getSignData(Map<String, String> params) {
		StringBuffer content = new StringBuffer();
		List<String> keys = new ArrayList<String>(params.keySet());
		// 按照自然升序处理
		Collections.sort(keys);
		for (int i = 0; i < keys.size(); i++) {
			String key = (String) keys.get(i);
			if ("_sign".equals(key) || "platForm".equals(key)) {
				continue;
			}
			String value = (String) params.get(key);
			if (value != null) {
				content.append((i == 1 ? "" : "&") + key + "=" + value);
			} else {
				content.append((i == 1 ? "" : "&") + key + "=");
			}
		}
		return Utilities.encrypt(content.toString());
	}

	public static String getSign(Map<String, String> requestParams) {
		String requestData = getSignData(requestParams);
		return Utilities.encrypt(requestData + secretKey);
	}

	private static void writerResult(HttpServletResponse response, String status, String transIDO) throws IOException {
		JSONObject json = new JSONObject();
		json.put("status", status);
		json.put("transIDO", transIDO);
		Logs.pay.error("返回平台数据：" + json.toJSONString());
		response.getWriter().write(json.toJSONString());
	}

	public static void main(String[] args) {

		
		
		Map<String, String> requestMap = new HashMap<String, String>();
		requestMap.put("gameExtend", "1-3211");
		requestMap.put("platform", "GW");
		requestMap.put("cardPoint", "0");
		requestMap.put("status", "1");
		requestMap.put("transaction", "1503100007266");
		requestMap.put("platForm", "iapple");
		requestMap.put("currency", "CNY");
		requestMap.put("amount", "6.00");
		requestMap.put("serverNo", "0");
		requestMap.put("transactionTime", "1425982121");
		requestMap.put("userId", "397085");
		requestMap.put("payType", "alipay");
		requestMap.put("_sign", "14eb5952f44a5b5757f60b44d0d84555");
		requestMap.put("gameUserId", "397085");
		String sign = getSign(requestMap);
		System.out.println(sign.equals(requestMap.get("_sign")));
		
	}

}
