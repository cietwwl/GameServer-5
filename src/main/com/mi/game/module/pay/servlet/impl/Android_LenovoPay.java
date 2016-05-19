package com.mi.game.module.pay.servlet.impl;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
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
 * lenovo支付成功callback
 * 
 * 调试未通过
 */
public class Android_LenovoPay extends BasePay {

	public static final String APP_ID = ConfigUtil.getString("lenovo.AppID");

	public static final String APP_KEY = ConfigUtil.getString("lenovo.AppKey");

	public static void pay(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String resultMsg = "SUCCESS";
		try {
			// 请求参数
			Map<String, String> requestParams = getRequestMap(request);
			// 签名参数
			String signature = requestParams.get("sign");
			// transdata
			String transdata = requestParams.get("transdata");

			Logs.pay.error("lenovo平台callback数据：" + requestParams);
			Logs.pay.error("lenovo平台callback签名(signature)：" + signature);

			// 签名验证
			if (!validSign(transdata, signature, APP_KEY)) {
				Logs.pay.error("订单：签名验证错误");
				writerResult(response, "FAILURE");
				return;
			}

			JSONObject jsonData = JSON.parseObject(transdata);

			// 游戏合作商自定义参数
			String callbackInfo = jsonData.getString("cpprivate");
			// 如果是支付服务器,则分发回调请求
			if (PAYCENTER) {
				// 透传信息
				String cpUserInfo = callbackInfo;
				if (StringUtils.isEmpty(cpUserInfo)) {
					Logs.pay.error("未找到平台透传消息,分发支付回调请求失败!");
					writerResult(response, "FAILURE");
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
			String orderId = jsonData.getString("exorderno");

			// 交易结果
			String orderStatus = jsonData.getString("result");

			if (!"0".equals(orderStatus)) {
				Logs.pay.error("订单：" + orderId + " 支付结果不为0");
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
				orderEntity.setPayPlatForm(PlatFromConstants.PLATFORM_LENOVO);
				payModule.savePayOrderEntity(orderEntity);
				// 处理充值
				payModule.payGold(orderEntity.getPlayerID(), orderEntity, null, PlatFromConstants.PLATFORM_LENOVO, null);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			resultMsg = "FAILURE";
		}
		writerResult(response, resultMsg);
	}

	public static String sign(String content, String privateKey, String input_charset) {
		try {
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decode(privateKey));
			KeyFactory keyf = KeyFactory.getInstance("RSA");
			PrivateKey priKey = keyf.generatePrivate(priPKCS8);
			Signature signature = Signature.getInstance("SHA1WithRSA");
			signature.initSign(priKey);
			signature.update(content.getBytes(input_charset));
			byte[] signed = signature.sign();
			return Base64.encode(signed);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Boolean validSign(String transdata, String sign, String privateKey) {
		String tmp = sign(transdata, privateKey, "utf-8");
//		try {
//			tmp = URLDecoder.decode(tmp, "utf-8");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		if (sign.equals(tmp)) {
			return true;
		}
		return false;
	}

	private static void writerResult(HttpServletResponse response, String resultMsg) throws IOException {
		Logs.pay.error("返回平台数据：" + resultMsg);
		response.getWriter().write(resultMsg);
	}

	public static void main(String[] args) {
		String transdata = "{\"transtype\":0,\"result\":0,\"transtime\":\"2015-03-12 13:30:35\",\"count\":1,\"paytype\":5,\"money\":600,\"waresid\":389,\"appid\":\"1501210339409.app.ln\",\"exorderno\":\"1828\",\"feetype\":0,\"transid\":\"21503121330352695636\",\"cpprivate\":\"1-1828\"}";
		String sign = "Lwhn4zBzX+U8kVXsyjpEFFtDHrrXk7K1zCBqJMNsCYjn1aco0WQWrFer5pME2msedT4ryc3yIcbTN/9r9DmdYPtOH4zLhLzfbCF1YnQdp94kW/l+hZLMgNk7VE5D6rVrctyupobPPt9dH1CqVegkBxSnnIIh/zoZEjHrbEtvw6I=";
		String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALLcSs7/Zs1+zwvsLKvIWz5KWCsfobAScojwbZ5jjYVl7YfQ2VJPyEdFTInOtDLR5imu/Qg2fIvuJZZKjvuGnL8sqUrepQFCFkeAeivHZlvBg7/ETulrQ+tZgWealylb7v88N6zUp6vXm4w9e7wsRdfqIUjF75yPyRiDW0YgUR6LAgMBAAECgYEAg73fHDji79nHh3CeNqmNC4SXDKxv29C6rELrew643tkE7FTYvdgmHNjeV2L5N7WDYpCRr8ryUd9HsDuatYmhSbNdBR+cON2lNDSfouGIna1LirCWep8KyBmqVY8J8XiA4pBnp8LBUcdmc3Xjh37eQUjrReh6icJV9iYeFYZtoAECQQDhzpf8gY04t4zCiM4GjZVu8ppDdMwjwoXXbkF1I6jgklpKCih08Qvn81qxXJzKbAeIe3uprj8z0dHAVvWD35uLAkEAysa2on3yKPIGCfNHLSR3ogZezJ59DGthwEI/CxCPqFI7zpIGJJueG6bc+ARStI0w/Yo6+Xaj42RaC0nqw6DpAQJAbYZzmfqMsTH2iHUq2WrbTJFH2ehRDBdSLAhCn7rIMwVJ3hfHP9GbYMpc9yJgup5jmQOsG8V0yoG/J3gGZgyTuQJALxkUlo1WT7hXOrxszclomBOOtUwik13lqHg6mGFSXLAs///euWYDxTxumpS2eQVKWblflU2/JzPJzlFu291mAQJARyH1FcOwOVGR4f98+6rgId2KeQH4p9DfXlpGnfKKEnUWCiEe0ylFEQpXaT6bZPH6yQjGCQHPzt3NmKKVTvM+Aw==";
		System.out.println(validSign(transdata, sign, privateKey));
	}
}
