package com.mi.game.module.pay.servlet.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.mi.core.util.ConfigUtil;
import com.mi.game.module.base.bean.init.server.ServerInfoData;
import com.mi.game.module.pay.PayModule;
import com.mi.game.module.pay.define.PlatFromConstants;
import com.mi.game.module.pay.pojo.PayOrderEntity;
import com.mi.game.module.pay.servlet.BasePay;
import com.mi.game.util.Logs;
import com.mi.game.util.Utilities;

/**
 * pps支付成功callback
 */
public class Android_PPSPay extends HttpServlet {

	private static final long serialVersionUID = 675222321841256001L;

	private static final String PAY_KEY = ConfigUtil.getString("pps.payKey");

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		pay(req, resp);
	}

	public static void pay(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String resultCode = "0";
		try {
			// 请求参数
			Map<String, String> requestParams = BasePay.getRequestMap(request);
			// 签名参数
			String signature = requestParams.get("sign");
			String localSignature = getSign(requestParams);
			Logs.pay.error("pps平台callback数据：" + requestParams);
			Logs.pay.error("pps平台callback签名(signature)：" + signature);
			Logs.pay.error("本地计算签名(localSignature)：" + localSignature);

			// 签名验证
			if (!localSignature.equals(signature)) {
				Logs.pay.error("订单：签名验证错误");
				writerResult(response, "-1");
				return;
			}

			// 游戏合作商自定义参数
			String callbackInfo = requestParams.get("userData");
			// 如果是支付服务器,则分发回调请求
			if (BasePay.PAYCENTER) {
				// 透传信息
				String cpUserInfo = callbackInfo;
				if (StringUtils.isEmpty(cpUserInfo)) {
					Logs.pay.error("未找到平台透传消息,分发支付回调请求失败!");
					writerResult(response, "-2");
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
				String result = sendPPSRequest(address, params);
				response.getWriter().write(result);
				return;
			}

			// 充值订单号
			String orderId = callbackInfo.split("-")[1];

			PayOrderEntity orderEntity = BasePay.payModule.getPayOrderEntity(orderId);

			if (orderEntity == null) {
				Logs.pay.error("订单：" + orderId + "未找到");
				writerResult(response, resultCode);
				return;
			}

			// 订单状态不是初始状态
			if (orderEntity.getState() != 0) {
				Logs.pay.error("订单：" + orderId + " 不是初始状态");
				writerResult(response, resultCode);
				return;
			}

			// 订单初始状态
			if (orderEntity.getState() == 0) {
				Logs.pay.error("订单：" + orderId + "支付完成，开始添加玩家元宝");
				orderEntity.setState(1);
				orderEntity.setCallbackTime(System.currentTimeMillis());
				orderEntity.setPayPlatForm(PlatFromConstants.PLATFORM_PPS);
				BasePay.payModule.savePayOrderEntity(orderEntity);
				// 处理充值
				BasePay.payModule.payGold(orderEntity.getPlayerID(), orderEntity, null, PlatFromConstants.PLATFORM_PPS, null);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			resultCode = "-6";
		}
		writerResult(response, resultCode);
	}

	private static String getSign(Map<String, String> requestParams) {
		StringBuilder sb = new StringBuilder();
		sb.append(requestParams.get("user_id"));
		sb.append(requestParams.get("role_id"));
		sb.append(requestParams.get("order_id"));
		sb.append(requestParams.get("money"));
		sb.append(requestParams.get("time"));
		sb.append(PAY_KEY);
		return Utilities.encrypt(sb.toString());
	}

	private static void writerResult(HttpServletResponse response, String resultCode) throws IOException {
		JSONObject json = new JSONObject();
		json.put("result", resultCode);
		json.put("message", "msg");
		Logs.pay.error("返回平台数据：" + json.toJSONString());
		response.getWriter().write(json.toJSONString());
	}

	public static String sendPPSRequest(String address, Map<String, String> params) {
		String result = "";
		try {
			URL url;
			String requestUrl = getPPSRequestUrl(address, params);
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

	private static String getPPSRequestUrl(String address, Map<String, String> params) throws UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();
		sb.append("http://");
		sb.append(address);
		sb.append("/pay/PPSPay.action?");
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

}
