package com.mi.game.module.pay.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.mi.core.engine.ModuleManager;
import com.mi.core.util.ConfigUtil;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.pay.PayModule;
import com.mi.game.util.Logs;

public class BasePay {

	public final static boolean PAYCENTER = ConfigUtil.getBoolean("isPayCenter", false);

	public static PayModule payModule = ModuleManager.getModule(ModuleNames.PayModule, PayModule.class);

	public static String sendRequest(String address, Map<String, String> params) {
		String result = "";
		try {
			URL url;
			String requestUrl = getRequestUrl(address, params);
			url = new URL(requestUrl);
			URLConnection conn = url.openConnection();
			// 超时设置500毫秒
			conn.setConnectTimeout(500);
			conn.setReadTimeout(5000);
			conn.connect();
			InputStream is = conn.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
			String line;
			while ((line = br.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return result;
		}
		return result;
	}

	public static Map<String, String> getRequestMap(HttpServletRequest request) throws UnsupportedEncodingException {
		Map<String, String> params = new HashMap<String, String>();
		Enumeration<?> names = request.getParameterNames();
		while (names.hasMoreElements()) {
			String key = (String) names.nextElement();
			String value = request.getParameter(key);
			if (value.indexOf("%") != -1) {
				params.put(key, URLDecoder.decode(value, "UTF-8"));
			} else {
				params.put(key, value);
			}
		}
		return params;
	}

	private static String getRequestUrl(String address, Map<String, String> params) throws UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();
		sb.append("http://");
		sb.append(address);
		sb.append("/pay/MiGamePay.do?");
		Set<Entry<String, String>> set = params.entrySet();
		int i = 0;
		for (Entry<String, String> entry : set) {
			if (i != 0) {
				sb.append("&");
			}
			sb.append(entry.getKey());
			sb.append("=").append(URLEncoder.encode(entry.getValue(), "UTF-8"));
			i++;
		}
		Logs.pay.info("send request:" + sb.toString());
		return sb.toString();
	}

	public static String sendPlatFormRequest(String address, Map<String, String> params) {
		String result = "";
		try {
			URL url;
			String requestUrl = getPlatFormRequestUrl(address, params);
			url = new URL(requestUrl);
			URLConnection conn = url.openConnection();
			// 超时设置500毫秒
			conn.setConnectTimeout(500);
			conn.setReadTimeout(5000);
			conn.connect();
			InputStream is = conn.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
			String line;
			while ((line = br.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return result;
		}
		return result;
	}

	/**
	 * 发送post 请求
	 * 
	 * @param url
	 * @param param
	 * @return
	 * @throws IOException
	 */
	public static String sendPost(String url, String param) {
		HttpURLConnection conn;
		String result = "";
		try {
			URL requestUrl = new URL(url);
			conn = (HttpURLConnection) requestUrl.openConnection();
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			PrintWriter writer = new PrintWriter(conn.getOutputStream());
			writer.print(param);
			writer.flush();
			writer.close();
			String line;
			BufferedReader bufferedReader;
			StringBuilder sb = new StringBuilder();
			InputStreamReader streamReader = null;
			streamReader = new InputStreamReader(conn.getInputStream(), "UTF-8");
			if (streamReader != null) {
				bufferedReader = new BufferedReader(streamReader);
				sb = new StringBuilder();
				while ((line = bufferedReader.readLine()) != null) {
					sb.append(line);
				}
			}
			result = sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String getPlatFormRequestUrl(String address, Map<String, String> params) throws UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();
		sb.append(address);
		Set<Entry<String, String>> set = params.entrySet();
		int i = 0;
		for (Entry<String, String> entry : set) {
			if (i != 0) {
				sb.append("&");
			}
			sb.append(entry.getKey());
			sb.append("=").append(URLEncoder.encode(entry.getValue(), "UTF-8"));
			i++;
		}
		return sb.toString();
	}

}
