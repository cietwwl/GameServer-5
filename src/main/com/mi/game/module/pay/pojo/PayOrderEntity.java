package com.mi.game.module.pay.pojo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.google.code.morphia.annotations.Indexed;
import com.mi.core.pojo.BaseEntity;
import com.mi.core.util.DateTimeUtil;
import com.mi.game.module.event.define.EventConstans;
import com.mi.game.module.event.pojo.EventMonthCardEntity;

public class PayOrderEntity extends BaseEntity {

	private static final long serialVersionUID = -4774779738619992934L;
	// 主键
	@Indexed
	private String oid;
	// 订单id
	private String orderID;
	// 用户id
	private String playerID;
	// 充值类型
	private int type;
	// 支付价格
	private int payMoney;
	// 状态
	private int state;
	// 其他
	private String parse;
	// 创建时间
	private long createTime;
	// 通知时间
	private long callbackTime;
	// 查询时间
	private long clientTime;
	// 充值平台
	private String payPlatForm;
	// 苹果订单号
	private String appOrderID;
	// 钱包
	private Map<String, Object> itemMap = new HashMap<String, Object>();
	// 月卡信息
	private EventMonthCardEntity monthCardEntity;
	// 是否首冲
	private int firstPay;

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

	public String getPlayerID() {
		return playerID;
	}

	public void setPlayerID(String playerID) {
		this.playerID = playerID;
	}

	public int getPayType() {
		return type;
	}

	public void setPayType(int payType) {
		this.type = payType;
	}

	public int getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(int payMoney) {
		this.payMoney = payMoney;
	}

	public String getParse() {
		return parse;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getCallbackTime() {
		return callbackTime;
	}

	public void setCallbackTime(long callbackTime) {
		this.callbackTime = callbackTime;
	}

	public long getClientTime() {
		return clientTime;
	}

	public void setClientTime(long clientTime) {
		this.clientTime = clientTime;
	}

	public void setParse(String parse) {
		this.parse = parse;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getPayPlatForm() {
		return payPlatForm;
	}

	public void setPayPlatForm(String payPlatForm) {
		this.payPlatForm = payPlatForm;
	}

	public String getAppOrderID() {
		return appOrderID;
	}

	public void setAppOrderID(String appOrderID) {
		this.appOrderID = appOrderID;
	}

	public EventMonthCardEntity getMonthCardEntity() {
		return monthCardEntity;
	}

	public void setMonthCardEntity(EventMonthCardEntity monthCardEntity) {
		this.monthCardEntity = monthCardEntity;
	}

	public Map<String, Object> getItemMap() {
		Map<String, Object> data = new HashMap<String, Object>();
		Set<Entry<String, Object>> set = itemMap.entrySet();
		for (Entry<String, Object> entry : set) {
			if (entry.getValue() instanceof List) {
				data.put(entry.getKey(), JSON.toJSON(entry.getValue()));
			} else {
				data.put(entry.getKey(), entry.getValue());
			}
		}
		return data;
	}

	public void setItemMap(Map<String, Object> itemMap) {
		Map<String, Object> data = new HashMap<String, Object>();
		Set<Entry<String, Object>> set = itemMap.entrySet();
		for (Entry<String, Object> entry : set) {
			if (entry.getValue() instanceof List) {
				data.put(entry.getKey(), JSON.toJSONString(entry.getValue()));
			} else {
				data.put(entry.getKey(), entry.getValue());
			}
		}
		this.itemMap = data;
	}

	public int getFirstPay() {
		return firstPay;
	}

	public void setFirstPay(int firstPay) {
		this.firstPay = firstPay;
	}

	@Override
	public Map<String, Object> responseMap() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("orderID", orderID);
		result.put("playerID", playerID);
		result.put("type", type);
		result.put("payMoney", payMoney);
		result.put("parse", parse);
		result.put("state", state);
		result.put("firstPay", firstPay);
		return result;
	}

	@Override
	public Map<String, Object> responseMap(int t) {
		Map<String, Object> result = new HashMap<String, Object>();
		switch (t) {
		case 11111:
			result.put("oid", oid);
			result.put("clientTime", DateTimeUtil.getStringDate(clientTime, EventConstans.YMDHMS));
			result.put("callbackTime", DateTimeUtil.getStringDate(callbackTime, EventConstans.YMDHMS));
			result.put("orderID", orderID);
			result.put("playerID", playerID);
			result.put("payType", type);
			result.put("payMoney", payMoney);
			result.put("state", state);
			if (monthCardEntity != null) {
				result.put("monthCard", monthCardEntity.responseMap());
			}
			break;
		}
		return result;
	}

	@Override
	public Object getKey() {
		return oid;
	}

	@Override
	public String getKeyName() {
		return "oid";
	}

	@Override
	public void setKey(Object key) {
		oid = key.toString();
	}

}
