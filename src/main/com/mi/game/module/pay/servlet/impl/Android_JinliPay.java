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
import com.mi.game.util.RSAEncrypt;

/**
 * 金立支付callback
 */
public class Android_JinliPay extends BasePay {

	public final static String rsaPublicKey = ConfigUtil.getString("jinli.rsaPublicKey");

	public static void pay(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String resultMsg = "success";
		try {
			// 请求参数
			Map<String, String> requestParams = getRequestMap(request);
			String signature = requestParams.get("sign");
			Logs.pay.error("金立支付callback数据：" + requestParams);
			Logs.pay.error("金立支付callback签名(signature)：" + signature);

			// 签名校验
			if (!doCheck(requestParams, signature, rsaPublicKey)) {
				Logs.pay.error("订单: 签名验证错误");
				writerResult(response, "fail");
				return;
			}

			// 游戏合作商自定义参数
			String callbackInfo = requestParams.get("out_order_no");

			// 如果是支付服务器,则分发回调请求
			if (PAYCENTER) {
				String cpUserInfo = callbackInfo;
				if (StringUtils.isEmpty(cpUserInfo)) {
					Logs.pay.error("未找到平台透传消息,分发支付回调请求失败!");
					writerResult(response, "fail");
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
				orderEntity.setPayPlatForm(PlatFromConstants.PLATFORM_JINLI);
				payModule.savePayOrderEntity(orderEntity);
				// 处理充值
				payModule.payGold(orderEntity.getPlayerID(), orderEntity, null, PlatFromConstants.PLATFORM_JINLI, null);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			resultMsg = "fail";
		}
		writerResult(response, resultMsg);
	}

	private static boolean doCheck(Map<String, String> params, String sign, String rsaPublicKey) {
		StringBuilder contentBuffer = new StringBuilder();
		Object[] signParamArray = params.keySet().toArray();
		Arrays.sort(signParamArray);
		for (Object key : signParamArray) {
			String value = params.get(key);
			// sign和msg不参与签名
			if (!"sign".equals(key) && !"msg".equals(key) && !"platForm".equals(key)) {
				contentBuffer.append(key + "=" + value + "&");
			}
		}
		String content = StringUtils.removeEnd(contentBuffer.toString(), "&");
		boolean result = false;
		try {
			result = RSAEncrypt.doCheck(content, sign, rsaPublicKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private static void writerResult(HttpServletResponse response, String resultMsg) throws IOException {
		Logs.pay.error("返回平台数据：" + resultMsg);
		response.getWriter().write(resultMsg);
	}
}
