package com.mi.game.module.pay.servlet.impl;

import java.io.IOException;
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
import com.mi.game.util.Logs;
import com.mi.game.util.Utilities;

/**
 * xy助手支付成功callback
 */
public class Ios_XyPay extends BasePay {

	private static final String PAY_KEY = ConfigUtil.getString("xy.payKey");

	private static final String APP_KEY = ConfigUtil.getString("xy.appkey");

	public static void pay(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String resultMsg = "success";
		try {
			// 请求参数
			Map<String, String> requestParams = getRequestMap(request);
			// 签名参数
			String signature = requestParams.get("sign");
			// 签名参数2
			String signature2 = requestParams.get("sig");

			Logs.pay.error("xy助手callback数据：" + requestParams);
			Logs.pay.error("xy助手callback签名(signature)：" + signature);
			Logs.pay.error("xy助手callback签名(signature)2：" + signature2);

			// xy 平台有可能发送两个签名.先验证sign 在验证sig

			if (StringUtils.isNotEmpty(signature)) {
				String localSignature = getSign(requestParams, APP_KEY);
				Logs.pay.error("本地计算签名(localSignature)1：" + localSignature);
				// 签名验证
				if (!localSignature.equals(signature)) {
					Logs.pay.error("订单：签名验证错误");
					writerResult(response, "fail");
					return;
				}
			}

			if (StringUtils.isNotEmpty(signature2)) {
				String localSignature = getSign(requestParams, PAY_KEY);
				Logs.pay.error("本地计算签名(localSignature)2：" + localSignature);
				// 签名验证
				if (!localSignature.equals(signature2)) {
					Logs.pay.error("订单：签名验证错误");
					writerResult(response, "fail");
					return;
				}
			}

			// 游戏合作商自定义参数
			String callbackInfo = requestParams.get("serverid");
			// 如果是支付服务器,则分发回调请求
			if (PAYCENTER) {
				// 透传信息
				String cpUserInfo = callbackInfo;
				if (StringUtils.isEmpty(cpUserInfo)) {
					Logs.pay.error("未找到平台透传消息,分发支付回调请求失败!");
					writerResult(response, "-2");
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
			String orderId = requestParams.get("extra");

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
				orderEntity.setPayPlatForm(PlatFromConstants.PLATFORM_XY);
				payModule.savePayOrderEntity(orderEntity);
				// 处理充值
				payModule.payGold(orderEntity.getPlayerID(), orderEntity, null, PlatFromConstants.PLATFORM_XY, null);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			resultMsg = "fail";
		}
		writerResult(response, resultMsg);
	}

	private static String getSign(Map<String, String> requestParams, String key) {
		StringBuilder sb = new StringBuilder();
		sb.append(key);
		sb.append("amount=").append(requestParams.get("amount")).append("&");
		sb.append("extra=").append(requestParams.get("extra")).append("&");
		sb.append("orderid=").append(requestParams.get("orderid")).append("&");
		sb.append("serverid=").append(requestParams.get("serverid")).append("&");
		sb.append("ts=").append(requestParams.get("ts")).append("&");
		sb.append("uid=").append(requestParams.get("uid"));
		return Utilities.encrypt(sb.toString());
	}

	private static void writerResult(HttpServletResponse response, String resultMsg) throws IOException {
		Logs.pay.error("返回平台数据：" + resultMsg);
		response.getWriter().write(resultMsg);
	}

}
