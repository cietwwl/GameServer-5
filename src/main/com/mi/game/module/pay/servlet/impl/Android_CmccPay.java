package com.mi.game.module.pay.servlet.impl;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.alibaba.fastjson.JSONObject;
import com.mi.game.module.base.bean.init.server.ServerInfoData;
import com.mi.game.module.pay.PayModule;
import com.mi.game.module.pay.define.PlatFromConstants;
import com.mi.game.module.pay.pojo.PayOrderEntity;
import com.mi.game.module.pay.servlet.BasePay;
import com.mi.game.util.Logs;

/**
 * 移动支付成功callback 宋雷
 */
public class Android_CmccPay extends BasePay {

	private static JSONObject json = new JSONObject();

	public static void pay(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		Map<String, String> requestParams = new HashMap<String, String>();
		try {
			// 请求参数
			if (PAYCENTER) {
				requestParams = getXml(request);
			} else {
				requestParams = getRequestMap(request);
			}

			Logs.pay.error("移动平台callback数据：" + requestParams);
			// 签名参数
			String hRet = requestParams.get("hRet");
			if (!"0".equals(hRet)) {
				Logs.pay.error("移动返回结果充值失败!");
				responseContent(response, 1, "failure");
				return;
			}

			// 游戏合作商自定义参数
			String callbackInfo = requestParams.get("cpparam");
			// 如果是支付服务器,则分发回调请求
			if (PAYCENTER) {
				String platForm = request.getParameter("platForm");
				// 透传信息
				String cpUserInfo = callbackInfo;
				if (StringUtils.isEmpty(cpUserInfo)) {
					Logs.pay.error("未找到平台透传消息,分发支付回调请求失败!");
					responseContent(response, 1, "failure");
					return;
				}
				String serverID = cpUserInfo.split("-")[0];
				ServerInfoData serverInfo = PayModule.serverListMap
						.get(serverID);
				String address = serverInfo.getUrl();
				requestParams.put("platForm", platForm);
				String result = sendRequest(address, requestParams);
				response.getWriter().write(result);
				return;
			}

			// 充值订单号
			String orderId = callbackInfo.split("-")[1];

			PayOrderEntity orderEntity = payModule.getPayOrderEntity(orderId);

			if (orderEntity == null) {
				Logs.pay.error("订单：" + orderId + "未找到");
				responseContent(response, 1, "failure");
				return;
			}

			// 订单状态不是初始状态
			if (orderEntity.getState() != 0) {
				Logs.pay.error("订单：" + orderId + " 不是初始状态");
				responseContent(response, 1, "failure");
				return;
			}

			// 订单初始状态
			if (orderEntity.getState() == 0) {
				Logs.pay.error("订单：" + orderId + "支付完成，开始添加玩家元宝");
				orderEntity.setState(1);
				orderEntity.setCallbackTime(System.currentTimeMillis());
				orderEntity.setPayPlatForm(PlatFromConstants.PLATFORM_CMCC);
				payModule.savePayOrderEntity(orderEntity);
				// 处理充值
				payModule.payGold(orderEntity.getPlayerID(), orderEntity, null,
						PlatFromConstants.PLATFORM_CMCC, null);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			responseContent(response, 1, "failure");
			return;
		}
		responseContent(response, 0, "successful");
	}	
	
	private static void responseContent(HttpServletResponse response,int hRet,String message) {                     
        String content=createXml(hRet,message);  
        try {
        	response.setContentType("text/xml; charset=UTF-8");
			// 把xml字符串写入响应
			byte[] xmlData = content.getBytes();
			response.setContentLength(xmlData.length);
			ServletOutputStream os = response.getOutputStream();
			os.write(xmlData);
			os.flush();
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
	private static String createXml(int hRet,String message){
		StringBuilder sb = new StringBuilder();  
        sb.append("<?xml version='1.0' encoding='UTF-8'?>");  
        sb.append("<response>");  
        sb.append(" <hRet>"+hRet+"</hRet>");                             
        sb.append(" <message>"+message+"</message>");    
        sb.append("</response>");
        return sb.toString();
	}

	private static Map<String, String> getXml(HttpServletRequest request) {
		// 解析对方发来的xml数据
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		Map<String, String> params = new HashMap<String, String>();
		try {
			db = dbf.newDocumentBuilder();
			Document doc = db.parse(request.getInputStream());
			NodeList nList = doc.getElementsByTagName("request");
			for (int i = 0; i < nList.getLength(); i++) {
				Element node = (Element) nList.item(i);
				params.put("hRet", node.getElementsByTagName("hRet").item(0).getFirstChild().getNodeValue());
				params.put("cpparam", node.getElementsByTagName("cpparam").item(0).getFirstChild().getNodeValue());	
				params.put("userId", node.getElementsByTagName("userId").item(0).getFirstChild().getNodeValue());
				params.put("consumeCode", node.getElementsByTagName("consumeCode").item(0).getFirstChild().getNodeValue());
				params.put("cpid", node.getElementsByTagName("cpid").item(0).getFirstChild().getNodeValue());
				params.put("status", node.getElementsByTagName("status").item(0).getFirstChild().getNodeValue());
				params.put("versionId", node.getElementsByTagName("versionId").item(0).getFirstChild().getNodeValue());
				params.put("cpparam", node.getElementsByTagName("cpparam").item(0).getFirstChild().getNodeValue());												
			}
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return params;
	}

}
