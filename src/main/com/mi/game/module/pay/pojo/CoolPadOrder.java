package com.mi.game.module.pay.pojo;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.mi.game.util.coolPadPay.SignHelper;

public class CoolPadOrder {
	// 应用编号
	private String appid;
	// 商品编号
	private int waresid;

	private String waresname;

	// 商户订单号
	private String cporderid;

	private float price;
	// 货币类型
	private String currency = "RMB";
	// 玩家ID
	private String appuserid;
	// 获取到的流水号
	private String outOrderNo;

	private String cpprivateinfo;

	private String notifyurl;

	public CoolPadOrder(String appid, int waresid, String cporderid, String appuserid,float price, String appKey) {
		this.appid = appid;
		this.waresid = waresid;
		this.cporderid = cporderid;
		this.appuserid = appuserid;
		this.price = price;
	}

	public static int getCpWaresid(int waresid) {
		int cpWaresid = 0;
		switch (waresid) {
		case 10801:
			cpWaresid = 1;
			break;
		case 10802:
			cpWaresid = 2;
			break;
		case 10803:
			cpWaresid = 3;
			break;
		case 10804:
			cpWaresid = 4;
			break;
		case 10805:
			cpWaresid = 5;
			break;
		case 10806:
			cpWaresid = 6;
			break;
		case 10807:
			cpWaresid = 7;
			break;
		case 10808:
			cpWaresid = 8;
			break;
		case 108036:
			cpWaresid = 9;
			break;
		case 108037:
			cpWaresid = 10;
			break;
		}
		return cpWaresid;
	}

	public static String getAppUserID(String userID) {
		String playerID = userID;
		String[] list = userID.split("_");
		if (list.length > 1) {
			playerID = list[1] + "#" + list[0];
		}
		return playerID;
	}

	public Map<String, Object> responseMap() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("orderNo", outOrderNo);
		result.put("orderID", cporderid);
		return result;
	}

	public static Map<String, String> wrapCreateOrder(CoolPadOrder coolPadOrder, String priKey) {
		Map<String, String> content = new HashMap<>();
		JSONObject dataReq = new JSONObject();
		dataReq.put("appid", coolPadOrder.getAppid());
		dataReq.put("waresid", getCpWaresid(coolPadOrder.getWaresid()));
		dataReq.put("waresname", coolPadOrder.getWaresname());
		dataReq.put("cporderid", coolPadOrder.getCporderid());
		dataReq.put("price", coolPadOrder.getPrice());
		dataReq.put("currency", coolPadOrder.getCurrency());
		dataReq.put("appuserid", coolPadOrder.getAppuserid());
		dataReq.put("cpprivateinfo", coolPadOrder.getCpprivateinfo());
		dataReq.put("notifyurl", coolPadOrder.getNotifyurl());

		String transData = dataReq.toJSONString();
		String sign = CoolPadOrder.getSign(transData, priKey);
		String signType = "RSA";
		content.put("transdata", transData);
		content.put("sign", sign);
		content.put("signtype", signType);
		return content;
	}

	public static String getSign(String transData, String priKey) {
		String sign = SignHelper.sign(transData, priKey);
		return sign;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public int getWaresid() {
		return waresid;
	}

	public void setWaresid(int waresid) {
		this.waresid = waresid;
	}

	public String getWaresname() {
		return waresname;
	}

	public void setWaresname(String waresname) {
		this.waresname = waresname;
	}

	public String getCporderid() {
		return cporderid;
	}

	public void setCporderid(String cporderid) {
		this.cporderid = cporderid;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getAppuserid() {
		return appuserid;
	}

	public void setAppuserid(String appuserid) {
		this.appuserid = appuserid;
	}

	public String getOutOrderNo() {
		return outOrderNo;
	}

	public void setOutOrderNo(String outOrderNo) {
		this.outOrderNo = outOrderNo;
	}

	public String getCpprivateinfo() {
		return cpprivateinfo;
	}

	public void setCpprivateinfo(String cpprivateinfo) {
		this.cpprivateinfo = cpprivateinfo;
	}

	public String getNotifyurl() {
		return notifyurl;
	}

	public void setNotifyurl(String notifyurl) {
		this.notifyurl = notifyurl;
	}

}
