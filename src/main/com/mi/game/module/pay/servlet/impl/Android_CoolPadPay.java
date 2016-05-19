package com.mi.game.module.pay.servlet.impl;

import java.io.IOException;
import java.util.Enumeration;
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
import com.mi.game.module.pay.define.PlatFromConstants;
import com.mi.game.module.pay.pojo.PayOrderEntity;
import com.mi.game.module.pay.servlet.BasePay;
import com.mi.game.util.Logs;
import com.mi.game.util.coolPadPay.SignHelper;

/**
 * 酷派支付成功callback
 */
public class Android_CoolPadPay extends BasePay {

	public static final String rsaPublicKey = ConfigUtil.getString("coolpad.rsaPublicKey");
	public static final String appKey = "";
	public static final String appID = "";

	public static void pay(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String resultMsg = "SUCCESS";
		try {
			// 请求参数
			Map<String, String> requestParams = getRequestMap(request);
			// 签名参数
			String signature = requestParams.get("sign");
			String transdata = requestParams.get("transdata");
			Logs.pay.error("酷派平台callback数据：" + requestParams);
			Logs.pay.error("酷派平台callback签名(signature)：" + signature);

			// 签名验证
			if (!validSign(transdata, signature, rsaPublicKey)) {
				Logs.pay.error("订单：签名验证错误");
				writerResult(response, "FAILURE");
				return;
			}

			JSONObject jsonData = JSON.parseObject(transdata);
			// 游戏合作商自定义参数
			String callbackInfo = jsonData.getString("cporderid");
			// 如果是支付服务器,则分发回调请求
			if (PAYCENTER) {
				// 透传信息
				String cpUserInfo = callbackInfo;
				if (StringUtils.isEmpty(cpUserInfo)) {
					Logs.pay.error("未找到平台透传消息,分发支付回调请求失败!");
					writerResult(response, "0");
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
			String orderId =callbackInfo.split("_")[1];

			String status = jsonData.getString("result");

			if (!"0".equals(status)) {
				Logs.pay.error("订单：" + orderId + "支付结果不为0");
				writerResult(response, resultMsg);
				return;
			}

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
				orderEntity.setPayPlatForm(PlatFromConstants.PLATFORM_COOLPAD);
				payModule.savePayOrderEntity(orderEntity);
				// 处理充值
				payModule.payGold(orderEntity.getPlayerID(), orderEntity, null, PlatFromConstants.PLATFORM_COOLPAD, null);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			resultMsg = "FAILURE";
		}
		writerResult(response, resultMsg);
	}

	/**
	 * 
	 * @param transdata
	 *            同步过来的transdata数据
	 * @param sign
	 *            同步过来的sign数据
	 * @param key
	 *            应用的密钥(商户可从商户自服务系统获取)
	 * @return 验证签名结果 true:验证通过 false:验证失败
	 */
	public static boolean validSign(String transdata, String sign, String key) {
		return SignHelper.verify(transdata, sign, key);
	}

	private static void writerResult(HttpServletResponse response, String resultMsg) throws IOException {
		Logs.pay.error("返回平台数据：" + resultMsg);
		response.getWriter().write(resultMsg);
	}

	public static void main(String[] args) {
		String key = "NjRCMzI0Mjc4QTc1MUEyQTRCRThCMzA1OTk4NTEwMDNCMTU5RkQ0MU1UWTBORFl4TmpjeE56YzVOREE0T0RVeU5ERXJNall6TWpJd01qVTROek00TVRFMU5UazJNRGs0TURjeU9EYzBORE0zTkRNek56YzRPRFF4";
		String sign = "6f0218ac1b7f3fb9bc3b7156989b2cb0 3058c630a0809a2428b42eb71652cb1d 1bb1cc0e304e49f8b96b0d4b5df7252e";
		String data = "{\"exorderno\":\"659\",\"transid\":\"05115011915363371948\",\"waresid\":1,\"appid\":\"3000866729\",\"feetype\":0,\"money\":600,\"result\":0,\"transtype\":0,\"transtime\":\"2015-01-19 15:36:48\",\"count\":1,\"cpprivate\":\"1-659-10801-kupai_13010308\",\"paytype\":401}";
		System.out.println(validSign(data, sign, key));
	}
}
