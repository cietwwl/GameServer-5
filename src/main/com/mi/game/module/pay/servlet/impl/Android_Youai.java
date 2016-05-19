package com.mi.game.module.pay.servlet.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.mi.core.util.ConfigUtil;
import com.mi.core.util.MD5;
import com.mi.game.module.base.bean.init.server.ServerInfoData;
import com.mi.game.module.pay.PayModule;
import com.mi.game.module.pay.define.PlatFromConstants;
import com.mi.game.module.pay.pojo.PayOrderEntity;
import com.mi.game.module.pay.servlet.BasePay;
import com.mi.game.util.Logs;

public class Android_Youai extends BasePay {
	private static String[] filter = {
			"platForm","signature"
	};

	public static void pay(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String msg = "success";
		String appSecret = ConfigUtil.getString("youai.secretKey");
		try {
			Map<String, String> requestParams = getRequestMap(request);
			String sign = requestParams.get("signature");
			Logs.pay.error("友爱平台callback数据：" + requestParams);
			if(!sign(requestParams,sign,appSecret)){
				Logs.pay.error("订单: 签名验证错误");
				writerResult(response, "fail");
				return;
			}
			String cpInfo = requestParams.get("extraInfo").toString();
			if (PAYCENTER) {
				// 开发商透传信息
				if (StringUtils.isEmpty(cpInfo)) {
					msg = "未找到平台透传消息,分发支付回调请求失败!";
					writerResult(response, msg);
					return;
				}
				String serverID = cpInfo.split("-")[0];
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
			// 开发商订单ID
			String cpOrderId = cpInfo.split("-")[1];
			// // 订单状态 代表成功
			String orderStatus = requestParams.get("status").toString();

			if (!"success".equals(orderStatus)) {
				// 订单状态，TRADE_SUCCESS 代表成功
				Logs.pay.error("订单：" + cpOrderId + " 支付失败");
				msg = "支付失败";
				writerResult(response, msg);
				return;
			}

			PayOrderEntity orderEntity = payModule.getPayOrderEntity(cpOrderId);
			if (orderEntity == null) {
				Logs.pay.error("订单：" + cpOrderId + " 未知订单");
				msg = "未知订单";
				writerResult(response, msg);
				return;
			}

			// 订单状态不是初始状态
			if (orderEntity.getState() != 0) {
				Logs.pay.error("订单：" + cpOrderId + " 状态不正确");
				msg = "订单：" + cpOrderId + " 状态不正确";
				writerResult(response, msg);
				return;
			}

			// 订单初始状态
			if (orderEntity.getState() == 0) {
				Logs.pay.error("订单：" + cpOrderId + " 支付完成，开始添加玩家元宝");
				orderEntity.setState(1);
				orderEntity.setCallbackTime(System.currentTimeMillis());
				orderEntity.setPayPlatForm(PlatFromConstants.PLATFORM_XIAOMI);
				payModule.savePayOrderEntity(orderEntity);
				// 处理充值
				payModule.payGold(orderEntity.getPlayerID(), orderEntity, null, PlatFromConstants.PLATFORM_YOUAI, null);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			msg = "CP服务器内部错误";
		}
		writerResult(response, msg);
	}

	private static boolean sign(Map<String, String> requestParams, String sign, String appSecret) {
		Set<Entry<String, String>> entrySet = requestParams.entrySet();
		Map<String, String> sortMap = new HashMap<String, String>();
		for (Entry<String, String> entry : entrySet) {
			String key = entry.getKey();
			if (key.equals("_c")) {
				continue;
			}
			if (key.equals("_a")) {
				continue;
			}
			String value = entry.getValue();
			sortMap.put(key, value);
		}
		String signString = "";
		try {
			signString = getSortQueryString(sortMap);
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		signString += appSecret;
		String nowString = MD5.md5(signString);
		if (nowString.equals(sign)) {
			return true;
		}
		return false;
	}

	public static String getSign(Map<String, String> params) throws Exception {
		String signString = getSortQueryString(params);
		return signString;
	}

	public static String getSortQueryString(Map<String, String> params) throws Exception {
		Object[] keys = params.keySet().toArray();
		Arrays.sort(keys);
		StringBuffer sb = new StringBuffer();
		List<String> filterList = Arrays.asList(filter);
		for (Object key : keys) {
			String param = String.valueOf(key);
			if (filterList.contains(param)) {
				continue;
			}
			//sb.append(UrlEncoded.encodeString(String.valueOf(key))).append("=").append(UrlEncoded.encodeString(params.get(String.valueOf(key)))).append("&");
			sb.append(String.valueOf(key)).append("=").append(params.get(String.valueOf(key))).append("&");
		}
		String text = sb.toString();
		if (text.endsWith("&")) {
			text = text.substring(0, text.length() - 1);
		}
		return text;
	}

	private static void writerResult(HttpServletResponse response, String msg) throws IOException {
		response.getWriter().write(msg);
	}

	public static void main(String[] args) {
// String data =
// "{status=success, youaiId=ya_5678478, extraInfo=1-1903, platForm=youai?totalFee=6.0, txnStamp=1426320901, signature=501c7f4cc3c5dc908a2fc006a3778b40, orderId=1903, txnId=ALPIBABA_20150314161430880_000000000021538, appKey=dntg_and}";
		Map<String, String> map = new HashMap<>();
		map.put("status", "success");
		map.put("youaiId", "ya_5678478");
		map.put("extraInfo", "1-1903");
		map.put("totalFee", "6.0");
		map.put("txnStamp", "1426320901");
		map.put("signature", "501c7f4cc3c5dc908a2fc006a3778b40");
		map.put("orderId", "1903");
		map.put("txnId", "ALPIBABA_20150314161430880_000000000021538");
		map.put("appKey", "dntg_and");
		System.out.println(sign(map, "501c7f4cc3c5dc908a2fc006a3778b40", "dntgandlqozpencoqzqoecmqacei"));
	}
}
