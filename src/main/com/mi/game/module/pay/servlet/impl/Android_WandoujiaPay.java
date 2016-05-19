package com.mi.game.module.pay.servlet.impl;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
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
import com.mi.game.util.Base64;
import com.mi.game.util.Logs;

/**
 * 豌豆荚支付成功callback
 * 
 */
public class Android_WandoujiaPay extends BasePay {

	public final static String rsaPublicKey = ConfigUtil.getString("wandoujia.rsaPublicKey");

	public static void pay(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String resultMsg = "success";
		try {
			// 请求参数
			Map<String, String> requestParams = getRequestMap(request);

			// 签名参数
			String signature = requestParams.get("sign");
			// 回调内容
			String content = requestParams.get("content");

			Logs.pay.error("豌豆荚平台callback数据：" + requestParams);
			Logs.pay.error("豌豆荚平台callback签名(signature)：" + signature);

			// 签名验证
			if (!doCheck(content, signature)) {
				Logs.pay.error("订单：签名验证错误");
				writerResult(response, "fail");
				return;
			}

			JSONObject jsonData = JSON.parseObject(content);

			// 游戏合作商自定义参数
			String callbackInfo = jsonData.getString("out_trade_no");
			// 如果是支付服务器,则分发回调请求
			if (PAYCENTER) {
				// 透传信息
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
				orderEntity.setPayPlatForm(PlatFromConstants.PLATFORM_WDJ);
				payModule.savePayOrderEntity(orderEntity);
				// 处理充值
				payModule.payGold(orderEntity.getPlayerID(), orderEntity, null, PlatFromConstants.PLATFORM_WDJ, null);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			resultMsg = "fail";
		}
		writerResult(response, resultMsg);
	}

	public static boolean doCheck(String content, String sign) {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			byte[] encodedKey = Base64.decode(rsaPublicKey);
			PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
			java.security.Signature signature = java.security.Signature.getInstance("SHA1WithRSA");
			signature.initVerify(pubKey);
			signature.update(content.getBytes("utf-8"));
			boolean bverify = signature.verify(Base64.decode(sign));
			return bverify;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private static void writerResult(HttpServletResponse response, String resultMsg) throws IOException {
		Logs.pay.error("返回平台数据：" + resultMsg);
		response.getWriter().write(resultMsg);
	}
}
