package com.mi.game.module.pay.servlet.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mi.core.util.ConfigUtil;
import com.mi.game.module.base.bean.init.server.ServerInfoData;
import com.mi.game.module.pay.PayModule;
import com.mi.game.module.pay.define.PayConstants;
import com.mi.game.module.pay.define.PlatFromConstants;
import com.mi.game.module.pay.pojo.PayOrderEntity;
import com.mi.game.module.pay.servlet.BasePay;
import com.mi.game.util.Logs;
import com.mi.game.util.Utilities;

/**
 * uc支付成功callback
 */
public class Android_UcPay extends BasePay {

	// uc
	public final static String UC_APIKEY = ConfigUtil.getString("uc.apiKey");
	public final static String UC_CPID = ConfigUtil.getString("uc.cpId");
	public final static String UC_GAMEID = ConfigUtil.getString("uc.gameId");

	public static void pay(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String ErrorDesc = "SUCCESS";
		try {
			// 请求参数
			JSONObject jsonData = getRequestData(request);

			if (jsonData == null) {
				String requestData = request.getParameter("requestData");
				jsonData = JSON.parseObject(requestData);
			}

			// 支付结果数据
			String data = jsonData.getString("data");
			// 签名参数
			String signature = jsonData.getString("sign");

			Logs.pay.error("uc平台callback数据：" + jsonData);
			Logs.pay.error("uc平台callback签名(signature)：" + signature);

			JSONObject json = JSON.parseObject(data);

			String localSignature = getSign(json);
			Logs.pay.error("本地计算签名(localSignature)：" + localSignature);

			// 签名验证
			if (!localSignature.equals(signature)) {
				Logs.pay.error("订单：签名验证错误");
				writerResult(response, "FAILURE");
				return;
			}

			// 游戏合作商自定义参数
			String callbackInfo = json.getString("callbackInfo");
			// 如果是支付服务器,则分发回调请求
			if (PAYCENTER) {
				// 透传信息
				String cpUserInfo = callbackInfo;
				if (StringUtils.isEmpty(cpUserInfo)) {
					Logs.pay.error("未找到平台透传消息,分发支付回调请求失败!");
					writerResult(response, "FAILURE");
					return;
				}
				String serverID = cpUserInfo.split("-")[0];
				ServerInfoData serverInfo = PayModule.serverListMap.get(serverID);
				String address = serverInfo.getUrl();
				Map<String, String> params = new HashMap<String, String>();
				params.put("requestData", jsonData.toJSONString());
				params.put("platForm", PayConstants.PLATFORM_UC);
				String result = sendRequest(address, params);
				response.getWriter().write(result);
				return;
			}

			// 充值订单号
			String orderId = callbackInfo.split("-")[1];
			// 订单状态
			String orderStatus = json.getString("orderStatus");

			// 订单状态
			if (orderStatus == null || !"S".equals(orderStatus)) { // 检验状态
				Logs.pay.error("订单：" + orderId + " 状态不为S");
				writerResult(response, ErrorDesc);
				return;
			}

			PayOrderEntity orderEntity = payModule.getPayOrderEntity(orderId);

			if (orderEntity == null) {
				Logs.pay.error("订单：" + orderId + "未找到");
				writerResult(response, ErrorDesc);
				return;
			}

// // 支付金额
// String amount = json.getString("amount");
// if (orderEntity.getPayMoney() != Integer.parseInt(amount)) {
// Logs.pay.error("订单：" + orderId + "充值金额不正确");
// writerResult(response, ErrorDesc);
// return;
// }

			// 订单状态不是初始状态
			if (orderEntity.getState() != 0) {
				Logs.pay.error("订单：" + orderId + " 不是初始状态");
				writerResult(response, ErrorDesc);
				return;
			}

			// 订单初始状态
			if (orderEntity.getState() == 0) {
				Logs.pay.error("订单：" + orderId + "支付完成，开始添加玩家元宝");
				orderEntity.setState(1);
				orderEntity.setCallbackTime(System.currentTimeMillis());
				orderEntity.setPayPlatForm(PlatFromConstants.PLATFORM_UC);
				payModule.savePayOrderEntity(orderEntity);
				// 处理充值
				payModule.payGold(orderEntity.getPlayerID(), orderEntity, null, PlatFromConstants.PLATFORM_UC, null);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			ErrorDesc = "FAILURE";
		}
		writerResult(response, ErrorDesc);
	}

	private static JSONObject getRequestData(HttpServletRequest request) {
		StringBuffer stringBuffer = new StringBuffer();
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(request.getInputStream(), "utf-8"));
			String ln;

			while ((ln = in.readLine()) != null) {
				stringBuffer.append(ln);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		String data = stringBuffer.toString();
		if (StringUtils.isNotEmpty(data)) {
			return JSON.parseObject(data);
		}
		return null;
	}

	private static String getSign(JSONObject json) {
		StringBuilder sb = new StringBuilder();
		sb.append(UC_CPID);
		sb.append("amount=").append(json.getString("amount"));
		sb.append("callbackInfo=").append(json.getString("callbackInfo"));
		sb.append("failedDesc=").append(json.getString("failedDesc"));
		sb.append("gameId=").append(json.getString("gameId"));
		sb.append("orderId=").append(json.getString("orderId"));
		sb.append("orderStatus=").append(json.getString("orderStatus"));
		sb.append("payWay=").append(json.getString("payWay"));
		sb.append("serverId=").append(json.getString("serverId"));
		sb.append("ucid=").append(json.getString("ucid"));
		sb.append(UC_APIKEY);
		return Utilities.encrypt(sb.toString());
	}

	private static void writerResult(HttpServletResponse response, String ErrorDesc) throws IOException {
		Logs.pay.error("返回平台数据：" + ErrorDesc);
		response.getWriter().write(ErrorDesc);
	}

}
