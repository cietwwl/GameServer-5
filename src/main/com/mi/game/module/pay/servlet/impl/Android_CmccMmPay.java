package com.mi.game.module.pay.servlet.impl;


import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletInputStream;
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
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.mi.core.util.ConfigUtil;
import com.mi.game.module.base.bean.init.server.ServerInfoData;
import com.mi.game.module.pay.PayModule;
import com.mi.game.module.pay.define.PlatFromConstants;
import com.mi.game.module.pay.pojo.PayOrderEntity;
import com.mi.game.module.pay.servlet.BasePay;
import com.mi.game.util.Logs;
import com.mi.game.util.MD5FileUtil;

/**
 * 移动mm支付成功callback 宋雷
 */
public class Android_CmccMmPay extends BasePay {
	
	public static String CMCC_APPKEY = ConfigUtil.getString("cmccmm.appkey");
	public static void pay(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		Map<String, String> requestParams = new HashMap<String, String>();
		// 请求参数
		if (PAYCENTER) {
			requestParams = getXml(request);
		} else {
			requestParams = getRequestMap(request);
		}
		
		Logs.pay.error("移动mm平台callback数据：" + requestParams);	
		
		// 签名参数
		String orderID=requestParams.get("OrderID");
		String channelID=requestParams.get("ChannelID");
		String payCode=requestParams.get("PayCode");
		String md5Sign=requestParams.get("MD5Sign");
		String transactionID=requestParams.get("TransactionID");
		String msgType=requestParams.get("MsgType");
		String version=requestParams.get("Version");
		String orderType=requestParams.get("OrderType");
		String appID=requestParams.get("AppID");
		try {
						
			// 签名校验
			if (!doCheck(orderID,channelID,payCode,md5Sign)) {
				Logs.pay.error("订单: 校验签名验证错误");				
				responseContent(response, 1,transactionID,msgType,version);
				return;
			}
					
			// 游戏合作商自定义参数
			String callbackInfo = requestParams.get("ExData");
			// 如果是支付服务器,则分发回调请求
			if (PAYCENTER) {
				String platForm = request.getParameter("platForm");
				// 透传信息
				String cpUserInfo = callbackInfo;
				if (StringUtils.isEmpty(cpUserInfo)) {
					Logs.pay.error("未找到平台透传消息,分发支付回调请求失败!");
					responseContent(response, 1,transactionID,msgType,version);
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
				responseContent(response, 1,transactionID,msgType,version);
				return;
			}

			// 订单状态不是初始状态
			if (orderEntity.getState() != 0) {
				Logs.pay.error("订单：" + orderId + " 不是初始状态");
				responseContent(response, 9015,transactionID,msgType,version);
				return;
			}
			
			responseContent(response, 0,transactionID,msgType,version);			
									
			requestParams.put("platForm", "cmccmmcheck");
			requestParams.put("msgType",msgType);
			requestParams.put("version",version);
			requestParams.put("orderID",orderID);
			requestParams.put("appID",appID);
			requestParams.put("orderType",orderType);
			String result = sendRequest("pay-android.millergame.net", requestParams);									
			Map<String, String> resultMap=getRespXml(result);
			Logs.pay.error("移动mm订单查询数据：" + result);	
			if("0".equals(resultMap.get("ReturnCode"))){
				// 订单初始状态
				if (orderEntity.getState() == 0) {
					Logs.pay.error("订单：" + orderId + "支付完成，开始添加玩家元宝");
					orderEntity.setState(1);
					orderEntity.setCallbackTime(System.currentTimeMillis());
					orderEntity.setPayPlatForm(PlatFromConstants.PLATFORM_CMCCMM);
					payModule.savePayOrderEntity(orderEntity);
					// 处理充值
					payModule.payGold(orderEntity.getPlayerID(), orderEntity, null,
							PlatFromConstants.PLATFORM_CMCCMM, null);
				}
			}			
		} catch (Exception ex) {
			ex.printStackTrace();
			responseContent(response, 1,transactionID,msgType,version);
			return;
		}
		
	}	
	
	public static void payCheck(HttpServletRequest request,HttpServletResponse response) throws IOException {
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams = getRequestMap(request);		
		String msgType=requestParams.get("msgType");
		String version=requestParams.get("version");
		String orderID=requestParams.get("orderID");
		String appID=requestParams.get("appID");
		String orderType=requestParams.get("orderType");
		String params=createReqXml(msgType,version,orderID,appID,orderType);
		String result = BasePay.sendPost("http://ospd.mmarket.com:8089/trust", params);
		try {
        	response.setContentType("text/xml; charset=UTF-8");
			// 把xml字符串写入响应
			byte[] xmlData = result.getBytes();
			response.setContentLength(xmlData.length);
			ServletOutputStream os = response.getOutputStream();
			os.write(xmlData);
			os.flush();
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}						
	}
	
	private static boolean doCheck(String OrderID,String ChannelID,String PayCode,String signMsg){
		boolean flag=false;
		String md5Content=MD5FileUtil.getMD5String(OrderID+"#"+ChannelID+"#"+PayCode+"#"+CMCC_APPKEY);//MD5 加密	
		if(md5Content.toUpperCase().equals(signMsg)){
			flag=true;
		}
		return flag;
	}		
	
	private static void responseContent(HttpServletResponse response,int hRet,String transactionID,String msgType,String version) {                     
        String content=createXml(hRet,transactionID,msgType,version);  
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
	
	
	
	private static String createXml(int hRet,String transactionID,String msgType,String version){
		StringBuilder sb = new StringBuilder();  
        sb.append("<?xml version='1.0' encoding='UTF-8'?>");  
        sb.append("<SyncAppOrderResp xmlns='http://www.monternet.com/dsmp/schemas/'>");  
        sb.append(" <TransactionID>"+transactionID+"</TransactionID>");
        sb.append(" <MsgType>"+msgType+"</MsgType>");
        sb.append(" <Version>"+version+"</Version>");
        sb.append(" <hRet>"+hRet+"</hRet>");                                        
        sb.append("</SyncAppOrderResp>");
        return sb.toString();
	}
	
	private static String createReqXml(String msgType,String version,String orderID,String appID,String orderType){
		StringBuilder sb = new StringBuilder();  
        sb.append("<?xml version='1.0' standalone='yes'?>");  
        sb.append("<Trusted2ServQueryReq>");  
        sb.append(" <MsgType>Trusted2ServQueryReq</MsgType>");
        sb.append(" <Version>1.0.0</Version>");
        sb.append(" <OrderID>"+orderID+"</OrderID>");
        sb.append(" <AppID>"+appID+"</AppID>"); 
        sb.append(" <OrderType>"+orderType+"</OrderType>");
        sb.append("</Trusted2ServQueryReq>");
        return sb.toString();
	}

	private static synchronized Map<String, String>  getXml(HttpServletRequest request) {
		// 解析对方发来的xml数据
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		Map<String, String> params = new HashMap<String, String>();		
		try {
			db = dbf.newDocumentBuilder();
			
			ServletInputStream sis =request.getInputStream();
			int size = request.getContentLength();
			String xmlData = null;
			byte[] buffer = new byte[size];
			byte[] xmldataByte = new byte[size];
			int count = 0;  
	        int rbyte = 0;  
            // 循环读取  
            while (count < size) {   
                // 每次实际读取长度存于rbyte中              	
                rbyte = sis.read(buffer);   
                for(int i=0;i<rbyte;i++) {  
                    xmldataByte[count + i] = buffer[i];  
                }  
                count += rbyte;  
            }
            xmlData = new String(xmldataByte, "UTF-8");
            StringReader sr = new StringReader(xmlData); 
    		InputSource is = new InputSource(sr);
    		Document doc = db.parse(is);   		  		
			NodeList nList = doc.getElementsByTagName("SyncAppOrderReq");
			for (int i = 0; i < nList.getLength(); i++) {
				Element node = (Element) nList.item(i);
				if(node.getElementsByTagName("Version").item(0).getFirstChild()!=null){
					params.put("Version", node.getElementsByTagName("Version").item(0).getFirstChild().getNodeValue());
				}
				if(node.getElementsByTagName("OrderID").item(0).getFirstChild()!=null){
					params.put("OrderID", node.getElementsByTagName("OrderID").item(0).getFirstChild().getNodeValue());					
				}
				if(node.getElementsByTagName("ActionTime").item(0).getFirstChild()!=null){
					params.put("ActionTime", node.getElementsByTagName("ActionTime").item(0).getFirstChild().getNodeValue());
				}
				if(node.getElementsByTagName("ActionID").item(0).getFirstChild()!=null){
					params.put("ActionID", node.getElementsByTagName("ActionID").item(0).getFirstChild().getNodeValue());
				}
				if(node.getElementsByTagName("AppID").item(0).getFirstChild()!=null){
					params.put("AppID", node.getElementsByTagName("AppID").item(0).getFirstChild().getNodeValue());
				}
				if(node.getElementsByTagName("PayCode").item(0).getFirstChild()!=null){
					params.put("PayCode", node.getElementsByTagName("PayCode").item(0).getFirstChild().getNodeValue());
				}
				if(node.getElementsByTagName("OrderPayment").item(0).getFirstChild()!=null){
					params.put("OrderPayment", node.getElementsByTagName("OrderPayment").item(0).getFirstChild().getNodeValue());
				}
				if(node.getElementsByTagName("MD5Sign").item(0).getFirstChild()!=null){
					params.put("MD5Sign", node.getElementsByTagName("MD5Sign").item(0).getFirstChild().getNodeValue());	
				}
				if(node.getElementsByTagName("ExData").item(0).getFirstChild()!=null){
					params.put("ExData", node.getElementsByTagName("ExData").item(0).getFirstChild().getNodeValue());
				}
				if(node.getElementsByTagName("ChannelID").item(0).getFirstChild()!=null){
					params.put("ChannelID", node.getElementsByTagName("ChannelID").item(0).getFirstChild().getNodeValue());
				}
				if(node.getElementsByTagName("MsgType").item(0).getFirstChild()!=null){
					params.put("MsgType", node.getElementsByTagName("MsgType").item(0).getFirstChild().getNodeValue());
				}
				if(node.getElementsByTagName("OrderType").item(0).getFirstChild()!=null){
					params.put("OrderType", node.getElementsByTagName("OrderType").item(0).getFirstChild().getNodeValue());
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return params;
	}
	
	private static Map<String, String> getRespXml(String request) {
		StringReader sr = new StringReader(request); 
		InputSource is = new InputSource(sr);
		// 解析对方发来的xml数据
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		Map<String, String> params = new HashMap<String, String>();
		try {
			db = dbf.newDocumentBuilder();
			Document doc = db.parse(is);
			NodeList nList = doc.getElementsByTagName("Trusted2ServQueryResp");
			for (int i = 0; i < nList.getLength(); i++) {
				Element node = (Element) nList.item(i);
				if(node.getElementsByTagName("ReturnCode").item(0).getFirstChild()!=null){
					params.put("ReturnCode", node.getElementsByTagName("ReturnCode").item(0).getFirstChild().getNodeValue());
				}								
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
