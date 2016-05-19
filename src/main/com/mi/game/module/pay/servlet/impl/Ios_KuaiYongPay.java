package com.mi.game.module.pay.servlet.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
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
import com.mi.game.util.Base64Coder;
import com.mi.game.util.Logs;
import com.mi.game.util.RSAEncrypt;

/**
 * 快用支付成功callback
 */
public class Ios_KuaiYongPay extends BasePay {

	// RSA公钥
	public static final String rsaPublicKey = ConfigUtil.getString("kuaiyong.rsaPublicKey");
	// 快用
	public final static String KUAIYONG_APP_KEY = ConfigUtil.getString("kuaiyong.appKey");
	// 加密方式
	public static final String SIGN_ALGORITHMS = "SHA1WithRSA";

	// sign计算所需参数
	private static String[] filter = {
			"notify_data","orderid","dealseq","uid","subject","v"
	};

	private static List<String> filterList = Arrays.asList(filter);

	public static void pay(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String status = "success";
		try {
			Map<String, String> requestParams = getRequestMap(request);
			Logs.pay.error("快用平台callback数据：" + requestParams);
			// 签名
			String sign = requestParams.get("sign");
			// 透传消息
			String dealseq = requestParams.get("dealseq");
			String signData = getSignData(requestParams);
			// 签名校验
			if (!doCheck(signData, sign, "utf-8")) {
				Logs.pay.error("订单:签名验证错误");
				writerResult(response, "failed");
				return;
			}
			// 如果是支付服务器,则分发回调请求
			if (PAYCENTER) {
				// 开发商透传信息
				String cpUserInfo = dealseq;
				if (StringUtils.isEmpty(cpUserInfo)) {
					Logs.pay.error("未找到平台透传消息,分发支付回调请求失败!");
					writerResult(response, "failed");
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

			RSAEncrypt rsaEncrypt = new RSAEncrypt();
			rsaEncrypt.loadPublicKey(rsaPublicKey);
			String notify_data = requestParams.get("notify_data");
			// 公钥解密通告加密数据
			byte[] dcDataStr = Base64Coder.decode(notify_data);
			byte[] plainData = rsaEncrypt.decrypt(rsaEncrypt.getPublicKey(), dcDataStr);
			// 获取到加密通告信息
			String notifyData = new String(plainData, "UTF-8");

			// 关键数据
			Map<String, String> notifyMap = getNotifyData(notifyData);

			// 订单号
			String trade_no = notifyMap.get("dealseq").split("_")[1];

			// 支付结果
			String payresult = notifyMap.get("payresult");
			if (!"0".equals(payresult)) {
				Logs.pay.error("订单：" + trade_no + "支付失败");
				writerResult(response, status);
				return;
			}

			PayOrderEntity orderEntity = payModule.getPayOrderEntity(trade_no);
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
				orderEntity.setPayPlatForm(PlatFromConstants.PLATFORM_KUAIYONG);
				payModule.savePayOrderEntity(orderEntity);
				// 处理充值
				payModule.payGold(orderEntity.getPlayerID(), orderEntity, null, PlatFromConstants.PLATFORM_KUAIYONG, null);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			status = "failed";
		}
		writerResult(response, status);
	}

	public static String getSignData(Map<String, String> params) {
		StringBuffer content = new StringBuffer();
		List<String> keys = new ArrayList<String>(params.keySet());
		// 按照自然升序处理
		Collections.sort(keys);
		for (int i = 0; i < keys.size(); i++) {
			String key = (String) keys.get(i);
			if (!filterList.contains(key)) {
				continue;
			}
			String value = (String) params.get(key);
			if (value != null) {
				content.append((i == 0 ? "" : "&") + key + "=" + value);
			} else {
				content.append((i == 0 ? "" : "&") + key + "=");
			}
		}
		return content.toString();
	}

	public static boolean doCheck(String content, String sign, String encode) {
		try {
			RSAEncrypt encrypt = new RSAEncrypt();
			encrypt.loadPublicKey(rsaPublicKey);
			java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);
			signature.initVerify(encrypt.getPublicKey());
			signature.update(content.getBytes(encode));
			boolean bverify = signature.verify(Base64Coder.decode(sign));
			return bverify;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static Map<String, String> getNotifyData(String requestString) throws UnsupportedEncodingException {
		Map<String, String> result = new HashMap<String, String>();
		String[] params = requestString.split("&");
		for (String param : params) {
			String[] tmp = param.split("=");
			String key = tmp[0];
			String value = tmp[1];
			if (value.indexOf("%") != -1) {
				result.put(key, URLDecoder.decode(tmp[1], "UTF-8"));
			} else {
				result.put(key, value);
			}
		}
		return result;
	}

	private static void writerResult(HttpServletResponse response, String status) throws IOException {
		Logs.pay.error("返回平台数据：" + status);
		response.getWriter().write(status);
	}

}
