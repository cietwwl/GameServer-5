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
import com.mi.game.util.RSAEncrypt;

/**
 * pp助手支付成功callback
 */
public class Ios_PPToolsPay extends BasePay {

	// pp助手
	public final static String PPZHUSHOU_APP_ID = ConfigUtil.getString("ppzhushou.appId");

	private static final String rsaPublicKey = ConfigUtil.getString("ppzhushou.rsaPublicKey");

	public static void pay(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String status = "success";
		try {
			// 请求参数
			Map<String, String> requestParams = getRequestMap(request);
			Logs.pay.error("pp助手callback数据：" + requestParams);

			// 签名校验
			if (!checkSign(requestParams)) {
				Logs.pay.error("订单:签名验证错误");
				writerResult(response, "error");
				return;
			}

			// 如果是支付服务器,则分发回调请求
			if (PAYCENTER) {
				// 开发商透传信息
				String cpUserInfo = requestParams.get("zone");
				if (StringUtils.isEmpty(cpUserInfo)) {
					Logs.pay.error("未找到平台透传消息,分发支付回调请求失败!");
					writerResult(response, status);
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

			String trade_no = requestParams.get("billno");
			PayOrderEntity orderEntity = payModule.getPayOrderEntity(trade_no);

			// 支付状态
			String orderStatus = requestParams.get("status");

			// 订单状态
			if (orderStatus == null || !"0".equals(orderStatus)) { // 检验状态
				Logs.pay.error("订单：" + trade_no + " 交易结果不为0");
				writerResult(response, status);
				return;
			}

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
				orderEntity.setPayPlatForm(PlatFromConstants.PLATFORM_PPZHUSHOU);
				payModule.savePayOrderEntity(orderEntity);
				// 处理充值
				payModule.payGold(orderEntity.getPlayerID(), orderEntity, null, PlatFromConstants.PLATFORM_PPZHUSHOU, null);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			status = "error";
		}
		writerResult(response, status);
	}

	// 校验订单信息,sign中的数据与request中的数据不一致返回false
	private static boolean checkSign(Map<String, String> params) throws Exception {
		String billno = params.get("billno");
		String amount = params.get("amount");
		String zone = params.get("zone");
		String roleid = params.get("roleid");

		String sign = params.get("sign");
		RSAEncrypt rsaEncrypt = new RSAEncrypt();
		rsaEncrypt.loadPublicKey(rsaPublicKey);
		byte[] plainData = rsaEncrypt.decrypt(rsaEncrypt.getPublicKey(), Base64Coder.decode(sign));
		String parseString = new String(plainData);

		JSONObject json = JSON.parseObject(parseString);

		if (!billno.equals(json.getString("billno"))) {
			return false;
		}
		if (!amount.equals(json.getString("amount"))) {
			return false;
		}
		if (!zone.equals(json.getString("zone"))) {
			return false;
		}
		if (!roleid.equals(json.getString("roleid"))) {
			return false;
		}
		return true;
	}

	private static void writerResult(HttpServletResponse response, String status) throws IOException {
		JSONObject json = new JSONObject();
		json.put("status", status);
		Logs.pay.error("返回平台数据：" + json.toJSONString());
		response.getWriter().write(json.toJSONString());
	}

}
