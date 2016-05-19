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
import com.mi.game.util.VivoSignUtils;

/**
 * VIVO手游支付成功callback
 */
public class Android_ViVoPay extends BasePay {

	private static String CPKEY = ConfigUtil.getString("vivo.CPKEY");

	public static void pay(HttpServletRequest request, HttpServletResponse response) throws IOException {

		try {
			// 请求参数
			Map<String, String> requestParams = getRequestMap(request);
			// 签名参数
			String signature = requestParams.get("signature");
			String localSignature = VivoSignUtils.getVivoSign(requestParams, CPKEY);
			Logs.pay.error("VIVO平台callback数据：" + requestParams);
			Logs.pay.error("VIVO平台callback签名(signature)：" + signature);
			Logs.pay.error("本地计算签名(localSignature)：" + localSignature);

			// 签名验证
			if (!localSignature.equals(signature)) {
				Logs.pay.error("订单：签名验证错误");
				response.setStatus(405);
				return;
			}

			// 游戏合作商自定义参数
			String callbackInfo = requestParams.get("extInfo");
			// 如果是支付服务器,则分发回调请求
			if (PAYCENTER) {
				// 透传信息
				String cpUserInfo = callbackInfo;
				if (StringUtils.isEmpty(cpUserInfo)) {
					Logs.pay.error("未找到平台透传消息,分发支付回调请求失败!");
					response.setStatus(400);
					return;
				}
				String serverID = cpUserInfo.split("_")[0];
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
			String orderId = callbackInfo.split("_")[1];

			String respCode = requestParams.get("tradeStatus");

			if (!"0000".equals(respCode)) {
				Logs.pay.error("订单：" + orderId + "支付不成功");
			}

			PayOrderEntity orderEntity = payModule.getPayOrderEntity(orderId);

			if (orderEntity == null) {
				Logs.pay.error("订单：" + orderId + "未找到");
				return;
			}

			// 订单状态不是初始状态
			if (orderEntity.getState() != 0) {
				Logs.pay.error("订单：" + orderId + " 不是初始状态");
				return;
			}

			// 订单初始状态
			if (orderEntity.getState() == 0) {
				Logs.pay.error("订单：" + orderId + "支付完成，开始添加玩家元宝");
				orderEntity.setState(1);
				orderEntity.setCallbackTime(System.currentTimeMillis());
				orderEntity.setPayPlatForm(PlatFromConstants.PLATFORM_VIVO);
				payModule.savePayOrderEntity(orderEntity);
				// 处理充值
				payModule.payGold(orderEntity.getPlayerID(), orderEntity, null, PlatFromConstants.PLATFORM_VIVO, null);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			response.setStatus(500);
			return;
		}
	}
}
