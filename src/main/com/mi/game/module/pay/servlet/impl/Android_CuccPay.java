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
import com.mi.game.module.analyse.dao.AnalyEntityDao;
import com.mi.game.module.analyse.pojo.AnalyEntity;
import com.mi.game.module.base.bean.init.server.ServerInfoData;
import com.mi.game.module.pay.PayModule;
import com.mi.game.module.pay.define.PlatFromConstants;
import com.mi.game.module.pay.pojo.PayOrderEntity;
import com.mi.game.module.pay.servlet.BasePay;
import com.mi.game.util.Logs;
import com.mi.game.util.MD5FileUtil;

/**
 * 联通支付成功callback 宋雷
 */
public class Android_CuccPay extends BasePay {
	
	public static String CUCC_ID = ConfigUtil.getString("cucc.cpid");
	public static String CUCC_CODE = ConfigUtil.getString("cucc.cpcode");
	public static String CUCC_CLIENT_ID = ConfigUtil.getString("cucc.client_id");
	public static String CUCC_CLIENT_SECRET = ConfigUtil.getString("cucc.client_secret");
	public static String CUCC_SECRET = ConfigUtil.getString("cucc.secretkey");			
    private static AnalyEntityDao analyEntityDao=AnalyEntityDao.getInstance();
	/**
	 * 支付校验
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public static void payCheck(HttpServletRequest request,HttpServletResponse response) throws IOException {
		Map<String, String> requestParams = new HashMap<String, String>();
						
		try {
			// 请求参数
			if (PAYCENTER) {
				requestParams = getCheckXml(request);
			} else {
				requestParams = getRequestMap(request);
			}

			Logs.pay.error("联通平台callback校验数据：" + requestParams);
			// 签名参数
			String orderid=requestParams.get("orderid");
			String signMsg=requestParams.get("signMsg");
			// 签名校验
			if (!doCheck(orderid,signMsg)) {
				Logs.pay.error("订单: 校验签名验证错误");				
				responseContent(response, createCheckXml(1,null,null,0));
				return;
			}
			
			// 如果是支付服务器,则分发回调请求
			if (PAYCENTER) {
				String platForm = request.getParameter("serviceid");
				if("validateorderid".equals(platForm)){
					platForm="cucccheck";
				}else{
					platForm="cucc";
				}
				// 透传信息
				String cpUserInfo = orderid;
				if (StringUtils.isEmpty(cpUserInfo)) {
					Logs.pay.error("未找到平台透传消息,分发支付回调请求失败!");
					responseContent(response,createCheckXml(1,null,null,0));
					return;
				}
				String serverID = cpUserInfo.split("-")[1];
				ServerInfoData serverInfo = PayModule.serverListMap
						.get(serverID);
				String address = serverInfo.getUrl();
				requestParams.put("platForm", platForm);
				String result = sendRequest(address, requestParams);
				response.getWriter().write(result);
				return;
			}

			// 充值订单号
			String orderId = orderid.split("-")[2];

			PayOrderEntity orderEntity = payModule.getPayOrderEntity(orderId);

			if (orderEntity == null) {
				Logs.pay.error("订单：" + orderId + "未找到");
				responseContent(response,createCheckXml(1,null,null,0));
				return;
			}

			// 订单状态不是初始状态
			if (orderEntity.getState() != 0) {
				Logs.pay.error("订单：" + orderId + " 不是初始状态");
				
				responseContent(response,createCheckXml(1,orderEntity.getPlayerID(),getServiceid(orderEntity.getPayType()),orderEntity.getCreateTime()));
				return;
			}			
			responseContent(response,createCheckXml(0,orderEntity.getPlayerID(),getServiceid(orderEntity.getPayType()),orderEntity.getCreateTime()));
		} catch (Exception ex) {
			ex.printStackTrace();
			responseContent(response,createCheckXml(1,null,null,0));
			return;
		}
		
	}
	
	public static void pay(HttpServletRequest request,HttpServletResponse response,Map<String, String> requestParams) throws IOException {		
						
		try {
			// 请求参数
			if (!PAYCENTER) {
				requestParams = getRequestMap(request);
			}

			Logs.pay.error("联通平台callback数据：" + requestParams);
			// 签名参数
			String signMsg=requestParams.get("signMsg");
			String orderid=requestParams.get("orderid");
			String ordertime=requestParams.get("ordertime");			
			String appid=requestParams.get("appid");
			String fid=requestParams.get("fid");
			String consumeCode=requestParams.get("consumeCode");
			String payfee=requestParams.get("payfee");
			String payType=requestParams.get("payType");
			String hRet=requestParams.get("hRet");
			String status=requestParams.get("status");			
			// 签名校验
			if (!doPayCheck(signMsg,orderid, ordertime,appid,fid,consumeCode,payfee,payType,hRet,status)) {
				Logs.pay.error("订单: 签名验证错误");				
				responseContent(response,createXml(0));
				return;
			}
			
			if(!"0".equals(hRet)){
				Logs.pay.error("订单: 联通平台支付失败");				
				responseContent(response,createXml(0));
				return;
			}
			
			// 如果是支付服务器,则分发回调请求
			if (PAYCENTER) {
				String platForm = request.getParameter("serviceid");
				if("validateorderid".equals(platForm)){
					platForm="cucccheck";
				}else{
					platForm="cucc";
				}
				// 透传信息
				String cpUserInfo = orderid;
				if (StringUtils.isEmpty(cpUserInfo)) {
					Logs.pay.error("未找到平台透传消息,分发支付回调请求失败!");
					responseContent(response,createXml(0));
					return;
				}
				String serverID = cpUserInfo.split("-")[1];
				ServerInfoData serverInfo = PayModule.serverListMap
						.get(serverID);
				String address = serverInfo.getUrl();
				requestParams.put("platForm", platForm);
				String result = sendRequest(address, requestParams);
				response.getWriter().write(result);
				return;
			}

			// 充值订单号
			String orderId = orderid.split("-")[2];

			PayOrderEntity orderEntity = payModule.getPayOrderEntity(orderId);

			if (orderEntity == null) {
				Logs.pay.error("订单：" + orderId + "未找到");
				responseContent(response,createXml(0));
				return;
			}

			// 订单状态不是初始状态
			if (orderEntity.getState() != 0) {
				Logs.pay.error("订单：" + orderId + " 不是初始状态");
				responseContent(response,createXml(0));
				return;
			}

			// 订单初始状态
			if (orderEntity.getState() == 0) {
				Logs.pay.error("订单：" + orderId + "支付完成，开始添加玩家元宝");
				orderEntity.setState(1);
				orderEntity.setCallbackTime(System.currentTimeMillis());
				orderEntity.setPayPlatForm(PlatFromConstants.PLATFORM_CUCC);
				payModule.savePayOrderEntity(orderEntity);
				// 处理充值
				payModule.payGold(orderEntity.getPlayerID(), orderEntity, null,
						PlatFromConstants.PLATFORM_CUCC, null);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			responseContent(response,createXml(0));
			return;
		}
		responseContent(response,createXml(1));
	}	
	
	private static void responseContent(HttpServletResponse response,String content) {                               
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
	
	private static boolean doCheck(String orderid,String signMsg){
		boolean flag=false;
		String md5Content=MD5FileUtil.getMD5String("orderid="+orderid+"&Key="+CUCC_SECRET);//MD5 加密
//		String str = RSAEncrypt.byteArrayToString(md5Content.getBytes());//16进制编码转换成字符串str		
		if(md5Content.equals(signMsg)){
			flag=true;
		}
		return flag;
	}		
	
	public static boolean doPayCheck(String signMsg,String orderid,String ordertime,String appid,String fid,String consumeCode,String payfee,String payType,String hRet,String status){
		boolean flag=false;
		String str=MD5FileUtil.getMD5String("orderid="+orderid+"&ordertime="+ordertime+"&cpid="+CUCC_ID+"&appid="+appid+"&fid="+fid+"&consumeCode="+consumeCode+"&payfee="+payfee+"&payType="+payType+"&hRet="+hRet+"&status="+status+"&Key="+CUCC_SECRET);//MD5 加密
//		String str = RSAEncrypt.byteArrayToString(md5Content.getBytes());//16进制编码转换成字符串str				
		if(str.equals(signMsg)){			
			flag=true;
		}
		return flag;
	}		
		
	private static String createCheckXml(int checkOrderIdRsp,String playerID,String type,long time){
		StringBuilder sb = new StringBuilder();  
        sb.append("<?xml version='1.0' encoding='UTF-8'?>");  
        sb.append("<paymessages>");
        sb.append(" <checkOrderIdRsp>"+checkOrderIdRsp+"</checkOrderIdRsp>");
		if(playerID!=null){
			AnalyEntity analyEntity=analyEntityDao.getEntity(playerID);	
			if(analyEntity!=null){
				
				if(analyEntity.getUid()!=null&&!"".equals(analyEntity.getUid())){
					sb.append(" <gameaccount>"+analyEntity.getUid()+"</gameaccount>");
				}else{
					sb.append(" <gameaccount>"+1+"</gameaccount>");
				}
                if(analyEntity.getStore()!=null&&!"".equals(analyEntity.getStore())){
                	sb.append(" <channelid>"+analyEntity.getStore()+"</channelid>");	
				}else{
					sb.append(" <channelid>"+1+"</channelid>");
				}
				if(analyEntity.getImei()!=null&&!"".equals(analyEntity.getImei())){
					sb.append(" <imei>"+analyEntity.getImei()+"</imei>");
				}else{
					sb.append(" <imei>"+1+"</imei>");
				}
		                	        
		        
			}else{
				sb.append(" <gameaccount>"+1+"</gameaccount>");  	        
		        sb.append(" <channelid>"+1+"</channelid>");	        	        
		        sb.append(" <imei>"+1+"</imei>");
			}	        	        
		}else{
			sb.append(" <gameaccount>"+1+"</gameaccount>");  	        
	        sb.append(" <channelid>"+1+"</channelid>");	        	        
	        sb.append(" <imei>"+1+"</imei>");
		}
		sb.append(" <ordertime>"+time+"</ordertime>");
		sb.append(" <cpid>"+CUCC_ID+"</cpid>");
		sb.append(" <serviceid>"+type+"</serviceid>");
		sb.append(" <appversion>1.1.0.2</appversion>");
		sb.append("</paymessages>");
		  
               
        System.out.println(sb.toString());
        return sb.toString();
	}
	
	private static String createXml(int checkOrderIdRsp){		
		StringBuilder sb = new StringBuilder();  
        sb.append("<?xml version='1.0' encoding='UTF-8'?>");  
        sb.append("<paymessages>"+checkOrderIdRsp+"</paymessages>");                         
        return sb.toString();
	}

	private static Map<String, String> getCheckXml(HttpServletRequest request) {
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
			NodeList nList = doc.getElementsByTagName("checkOrderIdReq");
			for (int i = 0; i < nList.getLength(); i++) {
				Element node = (Element) nList.item(i);
				if(node.getElementsByTagName("orderid").item(0).getFirstChild()!=null){
					params.put("orderid", node.getElementsByTagName("orderid").item(0).getFirstChild().getNodeValue());
				}
				if(node.getElementsByTagName("signMsg").item(0).getFirstChild()!=null){
					params.put("signMsg", node.getElementsByTagName("signMsg").item(0).getFirstChild().getNodeValue());
				}
				if(node.getElementsByTagName("usercode").item(0).getFirstChild()!=null){
					params.put("usercode", node.getElementsByTagName("usercode").item(0).getFirstChild().getNodeValue());
				}
				if(node.getElementsByTagName("provinceid").item(0).getFirstChild()!=null){
					params.put("provinceid", node.getElementsByTagName("provinceid").item(0).getFirstChild().getNodeValue());
				}
				if(node.getElementsByTagName("cityid").item(0).getFirstChild()!=null){
					params.put("cityid", node.getElementsByTagName("cityid").item(0).getFirstChild().getNodeValue());
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
	
	private static String getServiceid(int type){
		String id="";
		switch (type) {
			case 10808:
				id="001";
				break;
			case 10809:
				id="002";
				break;
			case 108010:
				id="003";
				break;
			case 108011:
				id="004";
				break;
			case 108012:
				id="005";
				break;
			case 108013:
				id="006";
				break;
			case 108014:
				id="007";
				break;
			case 108015:
				id="008";
				break;
			case 108016:
				id="009";
				break;
			case 108017:
				id="010";
				break;
			case 108018:
				id="011";
				break;
			case 108019:
				id="012";
				break;						
		} 
		return id;
	}
	

}
