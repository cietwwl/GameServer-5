package com.mi.game.module.pay.servlet.impl;

import java.io.IOException;
import java.util.Arrays;
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
 * 360支付成功callback
 */
public class Android_Qihu360Pay extends BasePay {

	// 360
	public final static String QH360_APP_ID = ConfigUtil.getString("qh360.appId");
	public final static String QH360_APP_KEY = ConfigUtil.getString("qh360.appKey");
	public final static String QH360_SECRET_KEY = ConfigUtil.getString("qh360.secretKey");

	public static void pay(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String errorMsg = "ok";
		try {
			// 请求参数
			Map<String, String> requestParams = getRequestMap(request);
			// 签名参数
			String signature = requestParams.get("sign");
			String localSignature = getSign(requestParams);
			Logs.pay.error("360平台callback数据：" + requestParams);
			Logs.pay.error("360平台callback签名(signature)：" + signature);
			Logs.pay.error("本地计算签名(localSignature)：" + localSignature);

			// 签名验证
			if (!localSignature.equals(signature)) {
				Logs.pay.error("订单：签名验证错误");
				writerResult(response, "error");
				return;
			}
			// 游戏合作商自定义参数
			String callbackInfo = requestParams.get("app_ext1");

			// 如果是支付服务器,则分发回调请求
			if (PAYCENTER) {
				// 透传信息
				String cpUserInfo = callbackInfo;
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

			// 充值订单号
			String orderId = requestParams.get("app_order_id");
			// 订单状态
			String orderStatus = requestParams.get("gateway_flag");

			// 检验订单状态
			if (orderStatus == null || !"success".equals(orderStatus)) {
				Logs.pay.error("订单：" + orderId + " 状态不为success");
				writerResult(response, errorMsg);
				return;
			}

			PayOrderEntity orderEntity = payModule.getPayOrderEntity(orderId);

			if (orderEntity == null) {
				Logs.pay.error("订单：" + orderId + "未找到");
				writerResult(response, errorMsg);
				return;
			}

			// 订单状态不是初始状态
			if (orderEntity.getState() != 0) {
				Logs.pay.error("订单：" + orderId + " 不是初始状态");
				writerResult(response, errorMsg);
				return;
			}

			// 订单初始状态
			if (orderEntity.getState() == 0) {
				Logs.pay.error("订单：" + orderId + "支付完成，开始添加玩家元宝");
				orderEntity.setState(1);
				orderEntity.setCallbackTime(System.currentTimeMillis());
				orderEntity.setPayPlatForm(PlatFromConstants.PLATFORM_360);
				payModule.savePayOrderEntity(orderEntity);
				// 处理充值
				payModule.payGold(orderEntity.getPlayerID(), orderEntity, null, PlatFromConstants.PLATFORM_360, null);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			errorMsg = "error";
		}
		writerResult(response, errorMsg);
	}

	/**
	 * 签名计算
	 * 
	 * @param requestParams
	 * @param appSecret
	 * @return
	 */
	public static String getSign(Map<String, String> requestParams) {
		Object[] keys = requestParams.keySet().toArray();
		Arrays.sort(keys);
		String k, v;
		String str = "";
		for (int i = 0; i < keys.length; i++) {
			k = (String) keys[i];
			if (k.equals("sign") || k.equals("sign_return") || k.equals("platForm")) {
				continue;
			}
			if (requestParams.get(k) == null) {
				continue;
			}
			v = (String) requestParams.get(k);

			if (v.equals("0") || v.equals("")) {
				continue;
			}
			str += v + "#";
		}
		return Utilities.encrypt(str + QH360_SECRET_KEY);
	}

	private static void writerResult(HttpServletResponse response, String errorMsg) throws IOException {
		Logs.pay.error("返回平台数据：" + errorMsg);
		response.getWriter().write(errorMsg);
	}

}
