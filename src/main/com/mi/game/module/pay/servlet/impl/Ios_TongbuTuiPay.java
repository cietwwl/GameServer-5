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
 * 同步推支付成功callback
 */
public class Ios_TongbuTuiPay extends BasePay {
	
	// 同步推
	public final static String TONGBU_APP_ID = ConfigUtil.getString("tongbutui.appId");
	public final static String TONGBU_APP_KEY = ConfigUtil.getString("tongbutui.appKey");

	public static void pay(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String status = "success";
		try {
			// 请求参数
			Map<String, String> requestParams = getRequestMap(request);
			String signature = requestParams.get("sign");
			String localSignature = getSign(requestParams);
			Logs.pay.error("同步推通知数据：" + requestParams);
			Logs.pay.error("同步推通知签名（signature）：" + signature);
			Logs.pay.error("本地计算签名（localSignature）：" + localSignature);

			// 签名校验
			if (!localSignature.equals(signature)) {
				Logs.pay.error("订单:签名验证错误");
				writerResult(response, "error");
				return;
			}

			// 如果是支付服务器,则分发回调请求
			if (PAYCENTER) {
				// 开发商透传信息
				String cpUserInfo = requestParams.get("paydes");
				if (StringUtils.isEmpty(cpUserInfo)) {
					Logs.pay.error("未找到平台透传消息,分发支付回调请求失败!");
					writerResult(response, "error");
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

			String trade_no = requestParams.get("trade_no");
			PayOrderEntity orderEntity = payModule.getPayOrderEntity(trade_no);

			if (orderEntity == null) {
				Logs.pay.error("订单：" + trade_no + "未知订单");
				writerResult(response, status);
				return;
			}

			// 订单状态不是初始状态
			if (orderEntity.getState() != 0) {
				Logs.pay.error("订单：" + trade_no + "状态不正确");
				writerResult(response, status);
				return;
			}

			// 订单初始状态
			if (orderEntity.getState() == 0) {
				Logs.pay.error("订单：" + trade_no + "支付完成，开始添加玩家元宝");
				orderEntity.setState(1);
				orderEntity.setCallbackTime(System.currentTimeMillis());
				orderEntity.setPayPlatForm(PlatFromConstants.PLATFORM_TONGBUTUI);
				payModule.savePayOrderEntity(orderEntity);
				// 处理充值
				payModule.payGold(orderEntity.getPlayerID(), orderEntity, null, PlatFromConstants.PLATFORM_TONGBUTUI, null);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			status = "error";
		}
		writerResult(response, status);
	}

	public static String getSign(Map<String, String> params) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("source=").append(params.get("source"));
		sb.append("&trade_no=").append(params.get("trade_no"));
		sb.append("&amount=").append(params.get("amount"));
		// partner
		sb.append("&partner=").append(TONGBU_APP_ID);
		sb.append("&paydes=").append(params.get("paydes"));

		sb.append("&debug=").append(params.get("debug"));
		sb.append("&tborder=").append(params.get("tborder"));
		// key
		sb.append("&key=").append(TONGBU_APP_KEY);
		return Utilities.encrypt(sb.toString());
	}

	private static void writerResult(HttpServletResponse response, String status) throws IOException {
		JSONObject json = new JSONObject();
		json.put("status", status);
		Logs.pay.error("返回平台数据：" + json.toJSONString());
		response.getWriter().write(json.toJSONString());
	}

}
