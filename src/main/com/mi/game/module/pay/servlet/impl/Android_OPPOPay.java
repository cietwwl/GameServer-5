package com.mi.game.module.pay.servlet.impl;

import java.io.IOException;
import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
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
import com.mi.game.util.Base64;
import com.mi.game.util.Logs;

/**
 * OPPO手游支付成功callback
 */
public class Android_OPPOPay extends BasePay {

	private static final String RESULT_STR = "result=%s&resultMsg=%s";
	public final static String rsaPublicKey = ConfigUtil.getString("oppo.rsaPublicKey");

	public static void pay(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String resultMsg = "OK";
		try {
			// 请求参数
			Map<String, String> requestParams = getRequestMap(request);
			// 签名参数
			String signature = requestParams.get("sign");
			String baseString = getBaseString(requestParams);
			Logs.pay.error("OPPO平台callback数据：" + requestParams);
			Logs.pay.error("OPPO平台callback签名(signature)：" + signature);

			// 签名验证
			if (!doCheck(baseString, signature, rsaPublicKey)) {
				Logs.pay.error("订单：签名验证错误");
				writerResult(response, "FAIL", "签名错误");
				return;
			}

			// 游戏合作商自定义参数
			String callbackInfo = requestParams.get("attach");
			// 如果是支付服务器,则分发回调请求
			if (PAYCENTER) {
				// 透传信息
				String cpUserInfo = callbackInfo;
				if (StringUtils.isEmpty(cpUserInfo)) {
					Logs.pay.error("未找到平台透传消息,分发支付回调请求失败!");
					writerResult(response, "FAIL", "未找到平台透传消息");
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
			String orderId = requestParams.get("partnerOrder").split("-")[1];

			PayOrderEntity orderEntity = payModule.getPayOrderEntity(orderId);

			if (orderEntity == null) {
				Logs.pay.error("订单：" + orderId + "未找到");
				writerResult(response, resultMsg, "回调成功");
				return;
			}

			// 订单状态不是初始状态
			if (orderEntity.getState() != 0) {
				Logs.pay.error("订单：" + orderId + " 不是初始状态");
				writerResult(response, resultMsg, "回调成功");
				return;
			}

			// 订单初始状态
			if (orderEntity.getState() == 0) {
				Logs.pay.error("订单：" + orderId + "支付完成，开始添加玩家元宝");
				orderEntity.setState(1);
				orderEntity.setCallbackTime(System.currentTimeMillis());
				orderEntity.setPayPlatForm(PlatFromConstants.PLATFORM_OPPO);
				payModule.savePayOrderEntity(orderEntity);
				// 处理充值
				payModule.payGold(orderEntity.getPlayerID(), orderEntity, null, PlatFromConstants.PLATFORM_OPPO, null);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			resultMsg = "FAIL";
		}
		writerResult(response, resultMsg, "回调成功");
	}

	private static String getBaseString(Map<String, String> param) {
		StringBuilder sb = new StringBuilder();
		sb.append("notifyId=").append(param.get("notifyId"));
		sb.append("&partnerOrder=").append(param.get("partnerOrder"));
		sb.append("&productName=").append(param.get("productName"));
		sb.append("&productDesc=").append(param.get("productDesc"));
		sb.append("&price=").append(param.get("price"));
		sb.append("&count=").append(param.get("count"));
		sb.append("&attach=").append(param.get("attach"));
		return sb.toString();
	}

	public static boolean doCheck(String content, String sign, String publicKey) throws Exception {
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		byte[] encodedKey = Base64.decode(publicKey);
		PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
		java.security.Signature signature = java.security.Signature.getInstance("SHA1WithRSA");
		signature.initVerify(pubKey);
		signature.update(content.getBytes("UTF-8"));
		boolean bverify = signature.verify(Base64.decode(sign));
		return bverify;
	}

	private static void writerResult(HttpServletResponse response, String result, String resultMsg) throws IOException {
		Logs.pay.error("返回平台数据：" + result);
		response.getWriter().write(String.format(RESULT_STR, result, URLEncoder.encode(resultMsg, "UTF-8")));
	}
}
