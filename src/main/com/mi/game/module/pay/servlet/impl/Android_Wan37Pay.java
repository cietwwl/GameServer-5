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
 * 37手游支付成功callback
 */
public class Android_Wan37Pay extends BasePay {

	public static final String PAY_KEY = ConfigUtil.getString("37wan.payKey");
	public static final String APP_KEY = ConfigUtil.getString("37wan.appKey");
	public static final String APP_ID = ConfigUtil.getString("37wan.appId");

	public static void pay(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String state = "1";
		try {
			// 请求参数
			Map<String, String> requestParams = getRequestMap(request);
			// 签名参数
			String signature = requestParams.get("sign");
			String localSignature = getSign(requestParams);
			Logs.pay.error("37手游平台callback数据：" + requestParams);
			Logs.pay.error("37手游平台callback签名(signature)：" + signature);
			Logs.pay.error("本地计算签名(localSignature)：" + localSignature);

			// 签名验证
			if (!localSignature.equals(signature)) {
				Logs.pay.error("订单：签名验证错误");
				writerResult(response, "0");
				return;
			}

			// 游戏合作商自定义参数
			String callbackInfo = requestParams.get("dext");
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
			String orderId = requestParams.get("doid");

			PayOrderEntity orderEntity = payModule.getPayOrderEntity(orderId);

			if (orderEntity == null) {
				Logs.pay.error("订单：" + orderId + "未找到");
				writerResult(response, state);
				return;
			}

			// 订单状态不是初始状态
			if (orderEntity.getState() != 0) {
				Logs.pay.error("订单：" + orderId + " 不是初始状态");
				writerResult(response, state);
				return;
			}

			// 订单初始状态
			if (orderEntity.getState() == 0) {
				Logs.pay.error("订单：" + orderId + "支付完成，开始添加玩家元宝");
				orderEntity.setState(1);
				orderEntity.setCallbackTime(System.currentTimeMillis());
				orderEntity.setPayPlatForm(PlatFromConstants.PLATFORM_37WAN);
				payModule.savePayOrderEntity(orderEntity);
				// 处理充值
				payModule.payGold(orderEntity.getPlayerID(), orderEntity, null, PlatFromConstants.PLATFORM_37WAN, null);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			state = "0";
		}
		writerResult(response, state);
	}

	private static String getSign(Map<String, String> requestParams) {
		StringBuilder sb = new StringBuilder();
		sb.append(requestParams.get("time"));
		sb.append(PAY_KEY);
		sb.append(requestParams.get("oid"));
		sb.append(requestParams.get("doid"));
		sb.append(requestParams.get("dsid"));
		sb.append(requestParams.get("uid"));
		sb.append(requestParams.get("money"));
		sb.append(requestParams.get("coin"));
		return Utilities.encrypt(sb.toString()).toLowerCase();
	}

	private static void writerResult(HttpServletResponse response, String state) throws IOException {
		JSONObject json = new JSONObject();
		json.put("state", state);
		json.put("data", "data");
		json.put("msg", "msg");
		Logs.pay.error("返回平台数据：" + json.toJSONString());
		response.getWriter().write(json.toJSONString());
	}
}
