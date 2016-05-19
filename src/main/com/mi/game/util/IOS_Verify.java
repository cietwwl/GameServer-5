package com.mi.game.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Locale;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class IOS_Verify {

	private static class TrustAnyTrustManager implements X509TrustManager {

		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}

		public X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[]{};
		}
	}

	private static class TrustAnyHostnameVerifier implements HostnameVerifier {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	}

	private static final String url_sandbox = "https://sandbox.itunes.apple.com/verifyReceipt";
	private static final String url_verify = "https://buy.itunes.apple.com/verifyReceipt";

	/**
	 * 苹果服务器验证
	 */
	public static String buyAppVerify(String receipt, String verifyState) {
		String url = url_verify;
		if (verifyState != null && verifyState.equals("Sandbox")) {
			url = url_sandbox;
		}
		String buyCode = Base64Coder.encodeString(receipt);
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, new TrustManager[]{
				new TrustAnyTrustManager()
			}, new java.security.SecureRandom());
			URL console = new URL(url);
			HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
			conn.setSSLSocketFactory(sc.getSocketFactory());
			conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("content-type", "text/json");
			conn.setRequestProperty("Proxy-Connection", "Keep-Alive");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			BufferedOutputStream hurlBufOus = new BufferedOutputStream(conn.getOutputStream());
			String str = String.format(Locale.CHINA, "{\"receipt-data\":\"" + buyCode + "\"}");
			hurlBufOus.write(str.getBytes());
			hurlBufOus.flush();
			InputStream is = conn.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			String line = null;
			StringBuffer sb = new StringBuffer();
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			return sb.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据原始收据返回苹果的验证地址: 沙箱 https://sandbox.itunes.apple.com/verifyReceipt 真正的地址
	 * https://buy.itunes.apple.com/verifyReceipt
	 * 
	 * @param receipt
	 * @return Sandbox 测试单 Real 正式单
	 */
	public static String getEnvironment(String receipt) {
		try {
			receipt = receipt.replaceAll(";", ",");
			receipt = receipt.replaceAll(" = ", ":");
			JSONObject job = JSON.parseObject(receipt);
			if (job.containsKey("environment")) {
				String evvironment = job.getString("environment");
				return evvironment;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "Real";
	}

	// 获取appstore订单号
	public static String getTransactionId(String receipt) {
		String transaction_id = "";
		receipt = receipt.replaceAll(";", ",");
		receipt = receipt.replaceAll(" = ", ":");
		JSONObject json = JSON.parseObject(receipt);
		String purchase_info = json.getString("purchase-info");
		purchase_info = Base64Coder.decodeString(purchase_info);
		purchase_info = purchase_info.replaceAll(";", ",");
		purchase_info = purchase_info.replaceAll(" = ", ":");
		json = JSON.parseObject(purchase_info);
		if (json.containsKey("transaction-id")) {
			transaction_id = json.getString("transaction-id");
			return transaction_id;
		}
		return transaction_id;
	}
}