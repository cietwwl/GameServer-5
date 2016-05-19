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
import com.mi.game.util.Des3Util;
import com.mi.game.util.Logs;

public class Android_AnzhiPay extends BasePay {
	public static void pay(HttpServletRequest request, HttpServletResponse response) throws IOException {
		//String appID = ConfigUtil.getString("anzhi.appID");
		String appSecret  = ConfigUtil.getString("anzhi.secretKey");
		String errMsg = "success";
		try {
			// 请求参数
			Map<String, String> requestParams = getRequestMap(request);
			String data = requestParams.get("data");
			String decrypt = getDecrypt(data, appSecret);
			Logs.pay.error("安智平台callback数据：" + requestParams);

			JSONObject dataMap = JSON.parseObject(decrypt);
			// 如果是支付服务器,则分发回调请求
			String cpInfo = dataMap.get("cpInfo").toString();
			if (PAYCENTER) {
				// 开发商透传信息
				if (StringUtils.isEmpty(cpInfo)) {
					errMsg = "未找到平台透传消息,分发支付回调请求失败!";
					writerResult(response, errMsg);
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

// // 开发商订单ID
			String cpOrderId = cpInfo.split("-")[1];
// // 订单状态 代表成功
			String orderStatus = dataMap.get("code").toString();

			if (!"1".equals(orderStatus)) {
				// 订单状态，TRADE_SUCCESS 代表成功
				Logs.pay.error("订单：" + cpOrderId + " 支付失败");
				errMsg = "支付失败";
				writerResult(response, errMsg);
				return;
			}

			PayOrderEntity orderEntity = payModule.getPayOrderEntity(cpOrderId);
			if (orderEntity == null) {
				Logs.pay.error("订单：" + cpOrderId + " 未知订单");
				errMsg = "未知订单";
				writerResult(response, errMsg);
				return;
			}

			// 订单状态不是初始状态
			if (orderEntity.getState() != 0) {
				Logs.pay.error("订单：" + cpOrderId + " 状态不正确");
				writerResult(response, errMsg);
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
				payModule.payGold(orderEntity.getPlayerID(), orderEntity, null, PlatFromConstants.PLATFORM_ANZHI, null);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			errMsg = "CP服务器内部错误";
		}
		writerResult(response, errMsg);
	}

	public static String getDecrypt(String args, String appKey) {
		return Des3Util.decrypt(args, appKey);
	}

	private static void writerResult(HttpServletResponse response, String msg) throws IOException {
		response.getWriter().write(msg);
	}

	public static void main(String[] args) {
	//	String data = "PUCJO3fzQZAurLW/3phf5mHgfx68lECDyJe3/ljmfLsxCZc5gX8dtEw0eRd+oe0GLMarY+TjN5mwFY4S8aZ5GFPxgbxj50LXZDuQ7sQIF+y50Qg2wVpUYJWCJ3U7mkGwlvbht0vOdhIDZGzEsgJuc8U4zrGbqy1v96YX8f0kLh7PVbAnmDIniAVFinLsHcyTHF+iT5+3798SoGwdsCGnoduJ12iTcHQ0IkA0gFDh1i763XAIO+vWcs4KXfA5WJT3C/nOItAQIoq7a4qigKedGdN9fxJeblzfnkVZe0TtMnEIqyjx1Er0xTLFO4KTs=";
		String data = "gnVKZb7C71UlAXTEj2Qbjy6nFrxPbM4cVjRIj9xYbIKO853EnDCtv1K4teH5VJetcgSoZnKDI4Yzf0qMkaG+3KdLaxo43hfVdcW3jYMjetZVxfPM4Bl68KR2/NoCdUkDRscOk6hqeVIHcISAlG4f0x+M5IdxtQ49AV/E+mNksjA70ApEokCPfuU+ale2ed+ofhMc9EFjzJkDjtvy8Ku1sNVDrA/TqQsAXobnyhoid5Hxzl7CPM4OroUTTFaD5dPU/DGSlzqZkuxVuPU7WGtUeXD6hYacjc+gZZC0JR9Qwpz646+B42R8A15ebWRnvNiSpzHxYTNlp2JR0S4gm55XdA==";
		String appSecret = "8eeuN8hzBI2obx3TjV9mm5np";
		String decrypt = getDecrypt(data, appSecret);
		System.out.println(decrypt);
		JSONObject dataMap = JSON.parseObject(decrypt);
		String orderStatus = dataMap.get("code").toString();
		System.out.println(orderStatus);
	}
}
