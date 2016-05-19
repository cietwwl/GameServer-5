package com.mi.game.module.festival.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mi.core.pojo.BaseEntity;

public class NewYearPayMoreEntity extends BaseEntity {
	private static final long serialVersionUID = -3853792997475110968L;

	private String playerID;
	// 活动期间充值总数
	private long payTotal;
	// 已领取列表
	private List<Integer> moreList = new ArrayList<Integer>();

	public String getPlayerID() {
		return playerID;
	}

	public void setPlayerID(String playerID) {
		this.playerID = playerID;
	}

	public long getPayTotal() {
		return payTotal;
	}

	public void setPayTotal(long payTotal) {
		this.payTotal = payTotal;
	}

	/**
	 * 增加多买多送充值数
	 * 
	 * @param payValue
	 */
	public void addPayTotal(int payValue) {
		payTotal += payValue;
	}

	public List<Integer> getMoreList() {
		return moreList;
	}

	public void setMoreList(List<Integer> moreList) {
		this.moreList = moreList;
	}

	/**
	 * 增加已领取奖励id
	 * 
	 * @param payType
	 */
	public void addMoreType(int payType) {
		if (!moreList.contains(payType)) {
			moreList.add(payType);
		}
	}

	@Override
	public Map<String, Object> responseMap() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("moreList", moreList);
		result.put("payTotal", payTotal);
		return result;
	}

	@Override
	public Object getKey() {
		return playerID;
	}

	@Override
	public String getKeyName() {
		return "playerID";
	}

	@Override
	public void setKey(Object key) {
		playerID = key.toString();
	}

}
