package com.mi.game.module.pay.servlet.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
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

/**
 * Itools支付成功callback
 */
public class Ios_ItoolsPay extends BasePay {

	public static final String RSA_PUBLIC = ConfigUtil.getString("itools.rsaPublicKey");
	public static final String SIGN_ALGORITHMS = "SHA1WithRSA";

	public static void pay(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String resultMsg = "success";

		try {
			// 请求参数
			Map<String, String> requestParams = getRequestMap(request);
			Logs.pay.error("itools平台callback数据：" + requestParams);
			// 签名
			String signature = requestParams.get("sign");
			// 数据
			String notify_data = requestParams.get("notify_data");
			// RSA解密
			String notifyJson = decrypt(notify_data);
			// 签名校验
			if (!verify(notifyJson, signature)) {
				Logs.pay.error("订单:签名验证错误");
				writerResult(response, "fail");
				return;
			}
			JSONObject jsonData = JSON.parseObject(notifyJson);
			// 服务器id-订单号
			String order_id_com = jsonData.getString("order_id_com");
			// 如果是支付服务器,则分发回调请求
			if (PAYCENTER) {
				// 开发商透传信息
				String cpUserInfo = order_id_com;
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

			// 订单号
			String trade_no = order_id_com.split("-")[1];
			// 订单状态
			String result = jsonData.getString("result");

			if (!"success".equals(result)) {
				Logs.pay.error("订单：" + trade_no + "支付失败");
				writerResult(response, resultMsg);
				return;
			}

			PayOrderEntity orderEntity = payModule.getPayOrderEntity(trade_no);
			if (orderEntity == null) {
				Logs.pay.error("订单：" + trade_no + "未知订单");
				writerResult(response, resultMsg);
				return;
			}

			// 订单状态不是初始状态
			if (orderEntity.getState() != 0) {
				Logs.pay.error("订单：" + trade_no + "状态不正确");
				writerResult(response, resultMsg);
				return;
			}

			// 订单初始状态
			if (orderEntity.getState() == 0) {
				Logs.pay.error("订单：" + trade_no + "支付完成，开始添加玩家元宝");
				orderEntity.setState(1);
				orderEntity.setCallbackTime(System.currentTimeMillis());
				orderEntity.setPayPlatForm(PlatFromConstants.PLATFORM_ITOOLS);
				payModule.savePayOrderEntity(orderEntity);
				// 处理充值
				payModule.payGold(orderEntity.getPlayerID(), orderEntity, null, PlatFromConstants.PLATFORM_ITOOLS, null);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			resultMsg = "fail";
		}
		writerResult(response, resultMsg);
	}

	/**
	 * RSA 签名检查
	 */
	public static boolean verify(String content, String sign) throws Exception {
		PublicKey pubKey = getPublicKey();
		try {
			java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);
			signature.initVerify(pubKey);
			signature.update(content.getBytes("utf-8"));
			boolean result = signature.verify(Base64Coder.decode(sign));
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * RSA 公钥解密
	 */
	public static String decrypt(String content) throws Exception {
		PublicKey pubKey = getPublicKey();
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, pubKey);
		InputStream ins = new ByteArrayInputStream(Base64Coder.decode(content));
		ByteArrayOutputStream writer = new ByteArrayOutputStream();
		// rsa解密的字节大小最多是128，将需要解密的内容，按128位拆开解密
		byte[] buf = new byte[128];
		int bufl;
		while ((bufl = ins.read(buf)) != -1) {
			byte[] block = null;

			if (buf.length == bufl) {
				block = buf;
			} else {
				block = new byte[bufl];
				for (int i = 0; i < bufl; i++) {
					block[i] = buf[i];
				}
			}
			writer.write(cipher.doFinal(block));
		}
		return new String(writer.toByteArray(), "utf-8");
	}

	/**
	 * 得到公钥
	 */
	public static PublicKey getPublicKey() throws Exception {
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		byte[] encodedKey = Base64Coder.decode(RSA_PUBLIC);
		PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
		return pubKey;
	}

	private static void writerResult(HttpServletResponse response, String result) throws IOException {
		Logs.pay.error("返回平台数据：" + result);
		response.getWriter().write(result);
	}

}
