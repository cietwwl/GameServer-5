package com.mi.game.module.pay.servlet;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
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

import com.mi.core.util.DateTimeUtil;
import com.mi.game.module.login.cmcc.Cmcc_Login;
import com.mi.game.module.pay.define.PayConstants;
import com.mi.game.module.pay.servlet.impl.Android_AnzhiPay;
import com.mi.game.module.pay.servlet.impl.Android_BaiduPay;
import com.mi.game.module.pay.servlet.impl.Android_CmccMmPay;
import com.mi.game.module.pay.servlet.impl.Android_CmccPay;
import com.mi.game.module.pay.servlet.impl.Android_CoolPadPay;
import com.mi.game.module.pay.servlet.impl.Android_CuccPay;
import com.mi.game.module.pay.servlet.impl.Android_DownJoyPay;
import com.mi.game.module.pay.servlet.impl.Android_HuaweiPay;
import com.mi.game.module.pay.servlet.impl.Android_JinliPay;
import com.mi.game.module.pay.servlet.impl.Android_LenovoPay;
import com.mi.game.module.pay.servlet.impl.Android_MuzhiwanPay;
import com.mi.game.module.pay.servlet.impl.Android_OPPOPay;
import com.mi.game.module.pay.servlet.impl.Android_PPSPay;
import com.mi.game.module.pay.servlet.impl.Android_Qihu360Pay;
import com.mi.game.module.pay.servlet.impl.Android_TencentPay;
import com.mi.game.module.pay.servlet.impl.Android_UcPay;
import com.mi.game.module.pay.servlet.impl.Android_ViVoPay;
import com.mi.game.module.pay.servlet.impl.Android_Wan37Pay;
import com.mi.game.module.pay.servlet.impl.Android_WandoujiaPay;
import com.mi.game.module.pay.servlet.impl.Android_XiaomiPay;
import com.mi.game.module.pay.servlet.impl.Android_YingyonghuiPay;
import com.mi.game.module.pay.servlet.impl.Android_Youai;
import com.mi.game.module.pay.servlet.impl.Ios_AiSiPay;
import com.mi.game.module.pay.servlet.impl.Ios_Baidu91Pay;
import com.mi.game.module.pay.servlet.impl.Ios_HaimaPay;
import com.mi.game.module.pay.servlet.impl.Ios_IapplePay;
import com.mi.game.module.pay.servlet.impl.Ios_ItoolsPay;
import com.mi.game.module.pay.servlet.impl.Ios_KuaiYongPay;
import com.mi.game.module.pay.servlet.impl.Ios_PPToolsPay;
import com.mi.game.module.pay.servlet.impl.Ios_TongbuTuiPay;
import com.mi.game.module.pay.servlet.impl.Ios_XyPay;
import com.mi.game.util.Logs;

public class MiGamePayServlet extends HttpServlet {

	private static final long serialVersionUID = -4895729981606764834L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String appKey = request.getParameter("appKey");
		if(appKey != null){
			if(appKey.equals("dntg_and")){
				Android_Youai.pay(request, response);
				return;
			}
		}
		String platForm = request.getParameter("platForm");
		String serviceid = request.getParameter("serviceid");
		Map<String, String> map=new HashMap<String, String>();
		if(platForm==null){				
			if("validateorderid".equals(serviceid)){				
				platForm="cucccheck";
			}else{
				map=getXml(request);				
				if(map.get("orderid")!=null&&!"".equals(map.get("orderid"))){
					platForm="cucc";
				}				
			}
		}
		// 检查平台是否为空
		if (platForm == null || StringUtils.isEmpty(platForm)) {
			Logs.pay.info("platForm is null");
			String ip = request.getRemoteAddr();
			Map<String, String> requestParams = BasePay.getRequestMap(request);
			Logs.pay.error("callback数据：" + requestParams);
			Logs.pay.info(DateTimeUtil.getStringDate() + " --------------> ip : " + ip);
			return;
		}
	
		// 强制小写
		platForm = platForm.toLowerCase();
		switch (platForm) {
		case PayConstants.PLATFORM_AISI:
			Ios_AiSiPay.pay(request, response);
			break;
		case PayConstants.PLATFORM_91:
			Ios_Baidu91Pay.pay(request, response);
			break;
		case PayConstants.PLATFORM_HAIMA:
			Ios_HaimaPay.pay(request, response);
			break;
		case PayConstants.PLATFORM_IAPPLE:
			Ios_IapplePay.pay(request, response);
			break;
		case PayConstants.PLATFORM_ITOOLS:
			Ios_ItoolsPay.pay(request, response);
			break;
		case PayConstants.PLATFORM_KUAIYONG:
			Ios_KuaiYongPay.pay(request, response);
			break;
		case PayConstants.PLATFORM_PPZHUSHOU:
			Ios_PPToolsPay.pay(request, response);
			break;
		case PayConstants.PLATFORM_TONGBUTUI:
			Ios_TongbuTuiPay.pay(request, response);
			break;
		case PayConstants.PLATFORM_XY:
			Ios_XyPay.pay(request, response);
			break;

		// /android 支付回调开始
		case PayConstants.PLATFORM_BAIDU:
			Android_BaiduPay.pay(request, response);
			break;
		case PayConstants.PLATFORM_COOLPAD:
			Android_CoolPadPay.pay(request, response);
			break;
		case PayConstants.PLATFORM_DANGLE:
			Android_DownJoyPay.pay(request, response);
			break;
		case PayConstants.PLATFORM_HUAWEI:
			Android_HuaweiPay.pay(request, response);
			break;
		case PayConstants.PLATFORM_JINLI:
			Android_JinliPay.pay(request, response);
			break;
		case PayConstants.PLATFORM_LENOVO:
			Android_LenovoPay.pay(request, response);
			break;
		case PayConstants.PLATFORM_MUZHIWAN:
			Android_MuzhiwanPay.pay(request, response);
			break;
		case PayConstants.PLATFORM_OPPO:
			Android_OPPOPay.pay(request, response);
			break;
		case PayConstants.PLATFORM_PPS:
			Android_PPSPay.pay(request, response);
			break;
		case PayConstants.PLATFORM_360:
			Android_Qihu360Pay.pay(request, response);
			break;
		case PayConstants.PLATFORM_UC:
			Android_UcPay.pay(request, response);
			break;
		case PayConstants.PLATFORM_VIVO:
			Android_ViVoPay.pay(request, response);
			break;
		case PayConstants.PLATFORM_37WAN:
			Android_Wan37Pay.pay(request, response);
			break;
		case PayConstants.PLATFORM_WDJ:
			Android_WandoujiaPay.pay(request, response);
			break;
		case PayConstants.PLATFORM_XIAOMI:
			Android_XiaomiPay.pay(request, response);
			break;
		case PayConstants.PLATFORM_YYH:
			Android_YingyonghuiPay.pay(request, response);
			break;
		case PayConstants.PLATFORM_ANZHI:
			Android_AnzhiPay.pay(request, response);
			break;
		case PayConstants.PLATFORM_YOUAI:
			Android_Youai.pay(request, response);
			break;
		case PayConstants.PLATFORM_TENCENT:
			Android_TencentPay.pay(request, response);
			break;
		case PayConstants.PLATFORM_CMCC:
			Android_CmccPay.pay(request, response);
			break;
		case PayConstants.PLATFORM_CMCC_LOGIN:			
			Cmcc_Login.pay(request, response);
			break;
		case PayConstants.PLATFORM_CUCC:
			Android_CuccPay.pay(request, response,map);
			break;
		case PayConstants.PLATFORM_CUCC_CHECK:
			Android_CuccPay.payCheck(request, response);
			break;
		case PayConstants.PLATFORM_CMCC_MM:
			Android_CmccMmPay.pay(request, response);
			break;
		case PayConstants.PLATFORM_CMCC_MM_CHECK:
			Android_CmccMmPay.payCheck(request, response);
			break;
		default:
			Map<String, String> requestData = BasePay.getRequestMap(request);
			Logs.pay.info(platForm + "--->平台回调暂未处理,平台request数据:\n"
					+ requestData);
		}
	}
	
	private static Map<String, String> getXml(HttpServletRequest request) {
		// 解析对方发来的xml数据
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		Map<String, String> params = new HashMap<String, String>();
		try {
			db = dbf.newDocumentBuilder();
			ServletInputStream sis=request.getInputStream();
			
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
			NodeList nList = doc.getElementsByTagName("callbackReq");
			for (int i = 0; i < nList.getLength(); i++) {
				Element node = (Element) nList.item(i);	
				if(node.getElementsByTagName("orderid").item(0).getFirstChild()!=null){
					params.put("orderid", node.getElementsByTagName("orderid").item(0).getFirstChild().getNodeValue());
				}else{
					params.put("orderid","");
				}
				if(node.getElementsByTagName("ordertime").item(0).getFirstChild()!=null){
					params.put("ordertime", node.getElementsByTagName("ordertime").item(0).getFirstChild().getNodeValue());
				}else{
					params.put("ordertime","");
				}
				if(node.getElementsByTagName("cpid").item(0).getFirstChild()!=null){
					params.put("cpid", node.getElementsByTagName("cpid").item(0).getFirstChild().getNodeValue());
				}else{
					params.put("cpid","");
				}
				if(node.getElementsByTagName("appid").item(0).getFirstChild()!=null){
					params.put("appid", node.getElementsByTagName("appid").item(0).getFirstChild().getNodeValue());	
				}else{
					params.put("appid","");
				}
				if(node.getElementsByTagName("fid").item(0).getFirstChild()!=null){
					params.put("fid", node.getElementsByTagName("fid").item(0).getFirstChild().getNodeValue());
				}else{
					params.put("fid","");
				}
				if(node.getElementsByTagName("consumeCode").item(0).getFirstChild()!=null){
					params.put("consumeCode", node.getElementsByTagName("consumeCode").item(0).getFirstChild().getNodeValue());
				}else{
					params.put("consumeCode","");
				}
				if(node.getElementsByTagName("payfee").item(0).getFirstChild()!=null){
					params.put("payfee", node.getElementsByTagName("payfee").item(0).getFirstChild().getNodeValue());
				}else{
					params.put("payfee","");
				}
				if(node.getElementsByTagName("payType").item(0).getFirstChild()!=null){
					params.put("payType", node.getElementsByTagName("payType").item(0).getFirstChild().getNodeValue());
				}else{
					params.put("payType","");
				}
				if(node.getElementsByTagName("hRet").item(0).getFirstChild()!=null){
					params.put("hRet", node.getElementsByTagName("hRet").item(0).getFirstChild().getNodeValue());
				}else{
					params.put("hRet","");
				}
				if(node.getElementsByTagName("status").item(0).getFirstChild()!=null){
					params.put("status", node.getElementsByTagName("status").item(0).getFirstChild().getNodeValue());
				}else{
					params.put("status","");
				}
				if(node.getElementsByTagName("signMsg").item(0).getFirstChild()!=null){
					params.put("signMsg", node.getElementsByTagName("signMsg").item(0).getFirstChild().getNodeValue());
				}else{
					params.put("signMsg","");
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
