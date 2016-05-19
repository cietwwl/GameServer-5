package com.mi.game.module.gameserver.servlet;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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
import com.mi.core.protocol.BaseProtocol;
import com.mi.core.util.ConfigUtil;
import com.mi.game.defines.ErrorIds;
import com.mi.game.module.analyse.AnalyseModule;
import com.mi.game.util.GZIPUtil;
import com.mi.game.util.Utilities;

public class GameServlet extends HttpServlet {

	private static final long serialVersionUID = 375671569307192565L;
	public static final int BUFFER = 1024;
	private static Map<String, String> requestCache = new ConcurrentHashMap<String, String>();
	private static Logger logger = LoggerFactory.getLogger(GameServlet.class);
	private static String secret = ConfigUtil.getString("msg.secret", "com.miller.game");
	// 消息加密方式是否开启
	private static boolean encrypt = ConfigUtil.getBoolean("msg.encrypt", false);
	// 消息缓存是否开启
	private static boolean cache = ConfigUtil.getBoolean("request.cache", false);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		long startTime = System.currentTimeMillis();
		Map<String, Object> parseMap = this.getRequestParse(request);
		JSONObject jsonObject = new JSONObject();
		if (parseMap == null || parseMap.isEmpty()) {
			// 如果解析不出任何数据,返回请求数据异常
			// this.writerJsonObject(jsonObject, ErrorIds.RequestException,
			// response);
			return;
		}
		String type =  parseMap.get("type") + "";
		if (StringUtils.isBlank(type)) {
			type = "0";
		}

		if (logger.isDebugEnabled()) {
			//logger.debug("请求参数：" + parseMap);
		}
		int handlerType = Integer.parseInt(type);
		// 如果消息使用了MD5加密
		if (encrypt) {
			String signature = null;
			String originalData = parseMap.get("original____data").toString();

			if (parseMap.containsKey("signature")) {
				signature = (String) parseMap.get("signature");
			}

			if (StringUtils.isBlank(signature)) { // 签名为空
				this.writerJsonObject(jsonObject, 9997, response);
				return;
			}
			String code = Utilities.makeCode("" + handlerType + originalData + secret);

			if (logger.isDebugEnabled()) {
				//logger.debug("加密数据：" + (handlerType + originalData + secret) + "请求数据签名：" + signature + "，服务器生成签名：" + code);
			}

			if (StringUtils.isBlank(code) || !code.equals(signature)) { // 数据签名验证错误
				this.writerJsonObject(jsonObject, 9996, response);
				return;
			}
		}

		IOMessage ioMessage = new IOMessage(handlerType, (Map<String, Object>) parseMap.get("data"), request, response);
		ioMessage.setPipeline(IOMessage.JETTY_HTTP);

		Map<String, Object> requestData = null;
		String playerID = null;
		try {
			if (cache) {
				// 重试请求
				requestData = (Map<String, Object>) parseMap.get("data");
				playerID = (String) requestData.get("playerID");
				if (requestData.containsKey("isRetry") && requestCache.containsKey(playerID + "@" + type)) {
					String isRetryJson = requestCache.get(playerID + "@" + type);
					this.writerStringObject(isRetryJson, response);
					return;
				}
			}

			HandlerManager.executeHandler(ioMessage);

			String json = "";
			if (ioMessage.getOutputResult().getCode() == 0) {
				Map<String, Object> responseMap = ioMessage.getOutputResult().responseMap(handlerType);
				if (responseMap == null || responseMap.isEmpty()) {
					responseMap = ioMessage.getOutputResult().responseMap();
				}
				if (responseMap != null && !responseMap.isEmpty()) {
					jsonObject.putAll(responseMap);
					jsonObject.put("code", ioMessage.getOutputResult().getCode());
					if (ioMessage.getOutputResult().getExtMap() != null) {
						jsonObject.put("extMap", ioMessage.getOutputResult().getExtMap());
					}
					json = jsonObject.toJSONString();
				} else {
					json = JSON.toJSONString(ioMessage.getOutputResult(), true);
				}
			} else {
				json = JSON.toJSONString(ioMessage.getOutputResult(), true);
			}

			if (cache) {
				if (requestData.containsKey("cache")) {
					if (!StringUtils.isEmpty(playerID)) {
						requestCache.put(playerID + "@" + type, json);
					}
				}
			}
			this.writerStringObject(json, response);
		} catch (Exception ex) {
			if (StringUtils.isNumeric(ex.getMessage())) {
				int code = Integer.parseInt(ex.getMessage());
				ioMessage.setProtocol(new BaseProtocol(code));
				AnalyseModule.interfaceAnalyse(ioMessage);
				logger.error("请求参数：" + parseMap);
				this.writerJsonObject(jsonObject, code, response);
			} else {
				logger.error("请求数据为 :" + parseMap.toString());
				logger.error(ex.getMessage(), ex);
				this.writerJsonObject(jsonObject, ErrorIds.SERVER_ERROR, response);
			}
		} finally {
			if ((System.currentTimeMillis() - startTime) > 100) {
				requestData = (Map<String, Object>) parseMap.get("data");
				playerID = (String) requestData.get("playerID");
				logger.info("type=" + type + " time=" + (System.currentTimeMillis() - startTime) + " ms, " + playerID);
			}
		}
	}

	/**
	 * 获取请求参数集合
	 * 
	 * @param encrypt
	 * 
	 * @return
	 * @throws IOException
	 */
	private Map<String, Object> getRequestParse(HttpServletRequest request) throws IOException {
		Map<String, Object> parseMap = new HashMap<String, Object>();
		int totalbytes = 0;
		try {
			totalbytes = request.getContentLength();
			byte[] dataOrigin = new byte[totalbytes];
			DataInputStream in = new DataInputStream(request.getInputStream());
			in.readFully(dataOrigin);
			in.close();
			String content = new String(dataOrigin);
			// 请求数据解压缩
			if (encrypt) {
				content = GZIPUtil.uncompress(dataOrigin);
			}
			parseMap = getRequestParse(content);
		} catch (Exception ex) {
			logger.error("totalbytes:" + totalbytes);
			logger.error(ex.getMessage() + ",ip:" + request.getRemoteHost(), ex);
		}
		return parseMap;
	}

	private Map<String, Object> getRequestParse(String requestJson) {
		Map<String,Object> json = JSON.parseObject(requestJson);
		Map<String, Object> parseMap = new HashMap<String, Object>();
		parseMap.putAll(json);
		if (json.get("data") != null) {
			parseMap.put("data", JSON.parseObject(json.get("data").toString()));
			parseMap.put("original____data", json.get("data"));
		}
		return parseMap;
//		HashMap<String, Object> parseMap = new HashMap<String, Object>();
//		String[] strArray = null;
//		// 处理聊天内容中出现&字符
//		if (encrypt) {
//			strArray = new String[3];
//			int index = requestJson.indexOf("&");
//			int lastIndex = requestJson.lastIndexOf("&");
//			strArray[0] = requestJson.substring(0, index);
//			strArray[1] = requestJson.substring(index + 1, lastIndex);
//			strArray[2] = requestJson.substring(lastIndex + 1, requestJson.length());
//		} else {
//			strArray = new String[2];
//			int index = requestJson.indexOf("&");
//			strArray[0] = requestJson.substring(0, index);
//			strArray[1] = requestJson.substring(index + 1, requestJson.length());
//		}
//		// /////////////////
//		if (!strArray[0].equals("") && strArray != null) {
//			if (strArray.length != 0) {
//				for (int i = 0; i < strArray.length; i++) {
//					String[] perArray = strArray[i].split("=");
//					String key = (String) perArray[0];
//					if (key.equals("data")) {
//						parseMap.put("original____data", perArray[1]);
//						Map<String, Object> map = JSON.parseObject(perArray[1]);
//						parseMap.put((String) (perArray[0]), map);
//					} else {
//						parseMap.put((String) (perArray[0]), perArray[1]);
//					}
//				}
//			}
//		}
//		return parseMap;
	}

	/**
	 * 输出json对象
	 */
	public void writerJsonObject(JSONObject jsonObject, int code, HttpServletResponse response) {
		BaseProtocol protocol = new BaseProtocol();
		protocol.setCode(code);
		String json = JSON.toJSONString(protocol, true);
		this.writerStringObject(json, response);
	}

	/**
	 * 输出json对象
	 */
	public void writerJsonObject(JSONObject jsonObject, HttpServletResponse response) {
		this.writerStringObject(jsonObject.toString(), response);
	}

	public void writerStringObject(String json, HttpServletResponse response) {
		try {
			byte[] compressData = json.getBytes();
			if (encrypt) {
				compressData = GZIPUtil.compress(json);
			}
			response.getOutputStream().write(compressData);
			response.getOutputStream().flush();
			response.getOutputStream().close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
