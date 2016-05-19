package com.mi.game.module.pay.servlet.impl;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
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
 * 91支付成功callback
 */
public class Ios_Baidu91Pay extends BasePay {
	

	// 91
	public final static String BD_91_APP_ID = ConfigUtil.getString("baidu.91.appId");
	public final static String BD_91_APP_KEY = ConfigUtil.getString("baidu.91.appKey");

	public static void pay(HttpServletRequest request, HttpServletResponse response) throws IOException {

		int ErrorCode = 1;
		String ErrorDesc = "接收成功";

		try {
			// 请求参数
			Map<String, String> requestParams = getRequestMap(request);
			String signature = requestParams.get("Sign");
			String localSignature = getSign(requestParams);
			Logs.pay.error("91平台callback数据：" + requestParams);
			Logs.pay.error("91平台callback签名(signature)：" + signature);
			Logs.pay.error("本地计算签名(localSignature)：" + localSignature);

			if (!localSignature.equals(signature)) {
				Logs.pay.error("订单：签名验证错误");
				ErrorDesc = "签名验证错误";
				ErrorCode = 5;
				writerResult(response, ErrorCode, ErrorDesc);
				return;
			}

			// 如果是支付服务器,则分发回调请求
			if (PAYCENTER) {
				// 开发商透传信息,充值类型
				String cpUserInfo = requestParams.get("Note");
				if (StringUtils.isEmpty(cpUserInfo)) {
					ErrorDesc = "未找到平台透传消息,分发支付回调请求失败!";
					writerResult(response, 0, ErrorDesc);
					return;
				}
				ServerInfoData serverInfo = PayModule.serverListMap.get(cpUserInfo);
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

			// 订单状态
			String payStatus = request.getParameter("PayStatus");
			// 订单id
			String goodsId = request.getParameter("GoodsId");

			// 订单状态
			if (!"1".equals(payStatus)) {
				// 支付状态：0=失败，1=成功
				Logs.pay.error("订单：" + goodsId + " 状态不为1");
				ErrorDesc = "支付失败订单";
				writerResult(response, ErrorCode, ErrorDesc);
				return;
			}

			PayOrderEntity orderEntity = payModule.getPayOrderEntity(goodsId);
			if (orderEntity == null) {
				Logs.pay.error("订单：" + goodsId + "未找到");
				ErrorDesc = "未知订单";
				writerResult(response, ErrorCode, ErrorDesc);
				return;
			}

// 卡类支付可能造成,订单金额不一致,故去掉订单金额验证
// //订单金额
// String originalMoney = request.getParameter("OriginalMoney");
// Double dOrderMoney = Double.parseDouble(originalMoney);
// if (orderEntity.getPayMoney() != dOrderMoney.intValue()) {
// Logs.pay.error("订单：" + goodsId + "充值金额不正确");
// ErrorDesc = "充值金额不正确";
// writerResult(response, ErrorCode, ErrorDesc);
// return;
// }

			// 订单状态不是初始状态
			if (orderEntity.getState() != 0) {
				Logs.pay.error("订单：" + goodsId + " 不是初始状态");
				ErrorDesc = "订单状态不正确";
				writerResult(response, ErrorCode, ErrorDesc);
				return;
			}

			// 订单初始状态
			if (orderEntity.getState() == 0) {
				Logs.pay.error("订单：" + goodsId + "支付完成，开始添加玩家元宝");
				orderEntity.setState(1);
				orderEntity.setCallbackTime(System.currentTimeMillis());
				orderEntity.setPayPlatForm(PlatFromConstants.PLATFORM_91);
				payModule.savePayOrderEntity(orderEntity);
				// 处理充值
				payModule.payGold(orderEntity.getPlayerID(), orderEntity, null, PlatFromConstants.PLATFORM_91, null);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			ErrorCode = 9999;
			ErrorDesc = "CP服务器内部错误";
		}
		writerResult(response, ErrorCode, ErrorDesc);
	}

	private static String getSign(Map<String, String> params) {
		StringBuilder sb = new StringBuilder();
		sb.append(BD_91_APP_ID);
		sb.append(params.get("Act"));
		sb.append(params.get("ProductName"));
		sb.append(params.get("ConsumeStreamId"));
		sb.append(params.get("CooOrderSerial"));
		sb.append(params.get("Uin"));
		sb.append(params.get("GoodsId"));
		sb.append(params.get("GoodsInfo"));
		sb.append(params.get("GoodsCount"));
		sb.append(params.get("OriginalMoney"));
		sb.append(params.get("OrderMoney"));
		sb.append(params.get("Note"));
		sb.append(params.get("PayStatus"));
		sb.append(params.get("CreateTime"));
		sb.append(BD_91_APP_KEY);
		return Utilities.encrypt(sb.toString());
	}

	private static void writerResult(HttpServletResponse response, int ErrorCode, String ErrorDesc) throws IOException {
		JSONObject json = new JSONObject();
		json.put("AppID", BD_91_APP_ID);
		json.put("ErrorCode", ErrorCode);
		json.put("ErrorDesc", ErrorDesc);
		Logs.pay.error("返回平台数据：" + json.toJSONString());
		response.getWriter().write(json.toJSONString());
	}

}
