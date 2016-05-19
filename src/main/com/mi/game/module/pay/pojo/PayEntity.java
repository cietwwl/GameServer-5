package com.mi.game.module.pay.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.code.morphia.annotations.Indexed;
import com.mi.core.pojo.BaseEntity;

public class PayEntity extends BaseEntity {

	private static final long serialVersionUID = -6003711308518027889L;
	@Indexed
	private String playerID;
	// 充值总数
	private int payTotal;
	// 首冲列表
	private List<Integer> firstPayList = new ArrayList<Integer>();
	// 时间
	private String dateTime;

	public String getPlayerID() {
		return playerID;
	}

	public void setPlayerID(String playerID) {
		this.playerID = playerID;
	}

	public int getPayTotal() {
		return payTotal;
	}

	public void setPayTotal(int payTotal) {
		this.payTotal = payTotal;
	}

	public List<Integer> getFirstPayList() {
		return firstPayList;
	}

	public void setFirstPayList(List<Integer> firstPayList) {
		this.firstPayList = firstPayList;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	/**
	 * 检查是否已经首冲
	 * 
	 * @param pid
	 * @return
	 */
	public boolean isFirstPay(int pid) {
		return firstPayList.contains(pid);
	}

	/**
	 * 增加首冲记录
	 * 
	 * @param pid
	 */
	public void addFirstPay(int pid) {
		if (!firstPayList.contains(pid)) {
			firstPayList.add(pid);
		}
	}

	/**
	 * 增加充值总数
	 * 
	 * @param gold
	 */
	public void addPayTotal(int gold) {
		if (gold < 0) {
			return;
		}
		payTotal += gold;
	}

	@Override
	public Map<String, Object> responseMap() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("payTotal", payTotal);
		result.put("firstPayList", firstPayList);
		return result;
	}

	@Override
	public Object getKey() {
		return playerID;
	}

	@Override
	public String getKeyName() {
		return playerID;
	}

	@Override
	public void setKey(Object key) {
		this.playerID = key.toString();
	}

}
