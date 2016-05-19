package com.mi.game.module.login.cmcc;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.mi.game.util.Logs;

public class Cmcc_Login {

	public static void pay(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		try {
			// 请求参数
			Map<String, String> requestParams = getRequestMap(request);
			Logs.logger.error("移动平台登录callback数据：" + requestParams);
						
		} catch (Exception ex) {
			ex.printStackTrace();			
		}
		writerResult(response);
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

	private static void writerResult(HttpServletResponse response) throws IOException {		
		response.getWriter().write("0");
	}
}
