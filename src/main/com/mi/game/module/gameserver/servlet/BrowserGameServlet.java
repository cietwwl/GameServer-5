package com.mi.game.module.gameserver.servlet;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mi.core.engine.HandlerManager;
import com.mi.core.engine.IOMessage;
import com.mi.game.defines.ErrorIds;

public class BrowserGameServlet extends HttpServlet {
	private static final long serialVersionUID = 4351662379899371014L;
	protected final static Logger logger = LoggerFactory.getLogger(BrowserGameServlet.class);
	private static Map<String, Long> requestCache = new ConcurrentHashMap<String, Long>();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		long startTime = System.currentTimeMillis();
		String type = request.getParameter("type");
		if (StringUtils.isBlank(type)) {
			type = "0";
		}
		HashMap<String, Object> parseMap = this.getRequestParse(request);
		parseMap.put("ip", request.getRemoteAddr());
		// logger.error("请求参数" + parseMap);
		JSONObject jsonObject = new JSONObject();

//		long nowTime = System.currentTimeMillis();

		try {
			int handlerType = Integer.parseInt(type);
			jsonObject.put("type", type);

			IOMessage ioMessage = new IOMessage(handlerType, parseMap, request, response);
//			String playerID = (String) ioMessage.getPlayerId();
//			if (!StringUtils.isEmpty(playerID)) {
//				if (requestCache.containsKey(playerID)) {
//					long isRetryJson = requestCache.get(playerID);
//					if (isRetryJson != 0) {
//						if ((nowTime - isRetryJson) < 100) {
//							requestCache.put(playerID, nowTime);
//							return;
//						}
//					}
//				}
//			}
			
			ioMessage.setPipeline(IOMessage.JETTY_HTTP);
			
			
			
			HandlerManager.executeHandler(ioMessage);

			String json = "";
			Map<String, Object> responseMap = ioMessage.getOutputResult().responseMap(handlerType);
			// Map<String, Object> requestData = (Map<String, Object>)
			// responseMap.get("data");

			
			if (responseMap == null || responseMap.isEmpty()) {
				responseMap = ioMessage.getOutputResult().responseMap();
			}
			if (responseMap != null && !responseMap.isEmpty()) {
				Set<Entry<String, Object>> set = responseMap.entrySet();
				for (Entry<String, Object> entry : set) {
					jsonObject.put(entry.getKey(), JSON.toJSON(entry.getValue()));
				}
				jsonObject.put("code", ioMessage.getOutputResult().getCode());
				if (ioMessage.getOutputResult().getExtMap() != null) {
					jsonObject.put("extMap", ioMessage.getOutputResult().getExtMap());
				}
				json = jsonObject.toJSONString();
			} else {
				json = JSON.toJSONString(ioMessage.getOutputResult(), true);
			}
//			if (!StringUtils.isEmpty(playerID)) {
//				requestCache.put(playerID, nowTime);
//			}
			writerStringObject(json, response);
		} catch (Exception ex) {

			if (StringUtils.isNumeric(ex.getMessage())) {
				this.writerJsonObject(jsonObject, Integer.parseInt(ex.getMessage()), response);
			} else {
				logger.error("请求数据为 :" + parseMap.toString());
				logger.error(ex.getMessage(), ex);
				this.writerJsonObject(jsonObject, ErrorIds.SERVER_ERROR, response);
			}
		} finally {
			if ((System.currentTimeMillis() - startTime) > 100) {
				// logger.info("type=" + type + " time=" +
				// (System.currentTimeMillis() - startTime) + " ms");
			}
		}
	}

	public void writerStringObject(String json, HttpServletResponse response) {
		try {
			byte[] compressData = json.getBytes();
			response.getOutputStream().write(compressData);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private HashMap<String, Object> getRequestParse(HttpServletRequest request) {
		HashMap<String, Object> parseMap = new HashMap<String, Object>();

		Enumeration<?> enumeration = request.getParameterNames();

		while (enumeration.hasMoreElements()) {
			String key = (String) enumeration.nextElement();

			if (!"token".equals(key)) {
				String value = request.getParameter(key);
				if (StringUtils.isNotBlank(value)) {
					parseMap.put(key, value);
				}
			}
		}

		return parseMap;
	}

	public void writerJsonObject(JSONObject jsonObject, int result, int code, HttpServletResponse response) {
		jsonObject.put("result", result);
		jsonObject.put("code", code);
		writerJsonObject(jsonObject, response);
	}

	public void writerJsonObject(JSONObject jsonObject, int result, HttpServletResponse response) {
		jsonObject.put("result", result);
		jsonObject.put("code", 0);
		writerJsonObject(jsonObject, response);
	}

	public void writerJsonObject(JSONObject jsonObject, HttpServletResponse response) {
		try {
			response.setContentType("application/json;charset=UTF-8");

			if (jsonObject.get("result") == null) {
				jsonObject.put("result", 0);
			}
			if (jsonObject.get("code") == null) {
				jsonObject.put("code", 0);
			}
			response.getWriter().write(jsonObject.toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}