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
import com.mi.game.util.Base64Coder;
import com.mi.game.util.Logs;
import com.mi.game.util.Utilities;

/**
 * 百度支付callback
 */
public class Android_BaiduPay extends BasePay {

	// baidu
	public final static String BD_APP_ID = ConfigUtil.getString("baidu.appId");
	public final static String BD_APP_KEY = ConfigUtil.getString("baidu.appKey");
	public final static String BD_APP_SECRET = ConfigUtil.getString("baidu.appSecret");

	public static void pay(HttpServletRequest request, HttpServletResponse response) throws IOException {

		int ResultCode = 1;
		String ResultMsg = "";
		try {
			// 请求参数
			Map<String, String> requestParams = getRequestMap(request);
			String signature = requestParams.get("Sign");
			String localSignature = getSign(requestParams);
			Logs.pay.error("百度平台callback数据：" + requestParams);
			Logs.pay.error("百度平台callback签名(signature)：" + signature);
			Logs.pay.error("本地计算签名(localSignature)：" + localSignature);

			// 签名校验
			if (!localSignature.equals(signature)) {
				Logs.pay.error("订单: 签名验证错误");
				ResultMsg = "签名验证错误";
				ResultCode = 5;
				writerResult(response, ResultCode, ResultMsg);
				return;
			}

			// 订单信息
			String Content = requestParams.get("Content");
			// base64 decode
			Content = Base64Coder.decodeString(Content);
			// json 订单信息
			JSONObject contentJson = JSON.parseObject(Content);

			// 如果是支付服务器,则分发回调请求
			if (PAYCENTER) {
				String cpUserInfo = contentJson.getString("ExtInfo");
				if (StringUtils.isEmpty(cpUserInfo)) {
					ResultMsg = "未找到平台透传消息,分发支付回调请求失败!";
					writerResult(response, 0, ResultMsg);
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
			// CP订单号
			String CooperatorOrderSerial = request.getParameter("CooperatorOrderSerial");

			// 订单状态
			int OrderStatus = contentJson.getInteger("OrderStatus");

			if (OrderStatus != 1) {
				// 订单状态 0:失败 1:成功
				Logs.pay.error("订单：" + CooperatorOrderSerial + " 状态不为1");
				ResultMsg = "失败订单";
				writerResult(response, ResultCode, ResultMsg);
				return;
			}

			PayOrderEntity orderEntity = payModule.getPayOrderEntity(CooperatorOrderSerial);

			if (orderEntity == null) {
				Logs.pay.error("订单：" + CooperatorOrderSerial + "未找到");
				ResultMsg = "未知订单";
				writerResult(response, ResultCode, ResultMsg);
				return;
			}

// 卡类支付可能造成,订单金额不一致,故去掉订单金额验证
// // 订单金额
// Double OrderMoney = contentJson.getDouble("OrderMoney");
// if (orderEntity.getPayMoney() != OrderMoney.intValue()) {
// Logs.pay.error("订单：" + CooperatorOrderSerial + "充值金额不正确");
// ResultMsg = "充值金额不正确";
// String resultSign = Utilities.encrypt(BD_APP_ID + ResultCode +
// BD_APP_SECRET);
// writerResult(response, ResultCode, ResultMsg, resultSign);
// return;
// }

			// 订单状态不是初始状态
			if (orderEntity.getState() != 0) {
				Logs.pay.error("订单：" + CooperatorOrderSerial + " 不是初始状态");
				ResultMsg = "订单状态不正确";
				writerResult(response, ResultCode, ResultMsg);
				return;
			}

			// 订单初始状态
			if (orderEntity.getState() == 0) {
				Logs.pay.error("订单：" + CooperatorOrderSerial + "支付完成，开始添加玩家元宝");
				orderEntity.setState(1);
				orderEntity.setCallbackTime(System.currentTimeMillis());
				orderEntity.setPayPlatForm(PlatFromConstants.PLATFORM_BAIDU);
				payModule.savePayOrderEntity(orderEntity);
				// 处理充值
				payModule.payGold(orderEntity.getPlayerID(), orderEntity, null, PlatFromConstants.PLATFORM_BAIDU, null);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			ResultCode = 9999;
			ResultMsg = "CP服务器内部错误";
		}
		writerResult(response, ResultCode, ResultMsg);
	}

	private static String getSign(Map<String, String> params) {
		StringBuilder sb = new StringBuilder();
		sb.append(BD_APP_ID);
		sb.append(params.get("OrderSerial"));
		sb.append(params.get("CooperatorOrderSerial"));
		sb.append(params.get("Content"));
		sb.append(BD_APP_SECRET);
		return Utilities.encrypt(sb.toString());
	}

	private static void writerResult(HttpServletResponse response, int ResultCode, String ResultMsg) throws IOException {
		JSONObject json = new JSONObject();
		json.put("AppID", BD_APP_ID);
		json.put("ResultCode", ResultCode);
		json.put("ResultMsg", ResultMsg);
		json.put("Sign", Utilities.encrypt(BD_APP_ID + ResultCode + BD_APP_SECRET));
		Logs.pay.error("返回平台数据：" + json.toJSONString());
		response.getWriter().write(json.toJSONString());
	}
}
