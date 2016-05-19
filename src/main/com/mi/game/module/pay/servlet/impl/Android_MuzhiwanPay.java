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
 * 拇指玩支付成功callback
 * 
 */
public class Android_MuzhiwanPay extends BasePay {

	public static final String PAY_KEY = ConfigUtil.getString("muzhiwan.payKey");
	public static final String API_KEY = ConfigUtil.getString("muzhiwan.apiKey");

	public static void pay(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String resultMsg = "SUCCESS";
		try {
			// 请求参数
			Map<String, String> requestParams = getRequestMap(request);
			// 签名参数
			String signature = requestParams.get("sign");
			String localSignature = getSign(requestParams);
			Logs.pay.error("拇指玩平台callback数据：" + requestParams);
			Logs.pay.error("拇指玩平台callback签名(signature)：" + signature);
			Logs.pay.error("本地计算签名(localSignature)：" + localSignature);

			// 签名验证
			if (!localSignature.equals(signature)) {
				Logs.pay.error("订单：签名验证错误");
				writerResult(response, "FAIL");
				return;
			}

			// 游戏合作商自定义参数
			String callbackInfo = requestParams.get("extern");
			// 如果是支付服务器,则分发回调请求
			if (PAYCENTER) {
				// 透传信息
				String cpUserInfo = callbackInfo;
				if (StringUtils.isEmpty(cpUserInfo)) {
					Logs.pay.error("未找到平台透传消息,分发支付回调请求失败!");
					writerResult(response, "0");
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
			String orderId = callbackInfo.split("-")[1];

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
				orderEntity.setPayPlatForm(PlatFromConstants.PLATFORM_MUZHIWAN);
				payModule.savePayOrderEntity(orderEntity);
				// 处理充值
				payModule.payGold(orderEntity.getPlayerID(), orderEntity, null, PlatFromConstants.PLATFORM_MUZHIWAN, null);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			resultMsg = "FAIL";
		}
		writerResult(response, resultMsg);
	}

	public static String getSign(Map<String, String> requestMap) {
		StringBuilder builder = new StringBuilder();
		builder.append(requestMap.get("appkey"));
		builder.append(requestMap.get("orderID"));
		builder.append(requestMap.get("productName"));
		builder.append(requestMap.get("productDesc"));
		builder.append(requestMap.get("productID"));
		builder.append(requestMap.get("money"));
		builder.append(requestMap.get("uid"));
		builder.append(requestMap.get("extern"));
		builder.append(PAY_KEY);
		System.out.println(builder.toString());
		return Utilities.encrypt(builder.toString());
	}

	private static void writerResult(HttpServletResponse response, String resultMsg) throws IOException {
		Logs.pay.error("返回平台数据：" + resultMsg);
		response.getWriter().write(resultMsg);
	}

	public static void main(String[] args) {
		String uid = "7492454";
		String sign = "db1bf687b13001ca4fea9e769e46e73c";
		String username = "xiangsoft";
		String orderID = "88ccfc3efc654b18a4f3df530e5245e6";
		String extern = "1-2";
		String appkey = "fbed0038f329967b83ac41175fe3dfad";
		String money = "1";
		String productDesc = "60元宝";
		String productName = "60元宝";
		String productID = "10087";

		Map<String, String> requestMap = new HashMap<String, String>();
		requestMap.put("uid", uid);
		requestMap.put("sign", sign);
		requestMap.put("username", username);
		requestMap.put("orderID", orderID);
		requestMap.put("extern", extern);
		requestMap.put("appkey", appkey);
		requestMap.put("money", money);
		requestMap.put("productName", productName);
		requestMap.put("productDesc", productDesc);
		requestMap.put("productID", productID);

		System.out.println(getSign(requestMap).equals(sign));

	}
}
