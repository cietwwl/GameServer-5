package com.mi.game.module.event.protocol;

import java.util.List;
import java.util.Map;

import com.mi.core.protocol.BaseProtocol;
import com.mi.game.module.event.data.ActiveConsumeData;
import com.mi.game.module.reward.data.GoodsBean;

public class EventDailyExpenseProtocol extends BaseProtocol {

	private Map<String, Object> itemMap;
	private List<GoodsBean> showMap;
	private List<Integer> rewardList; // 购买历史
	private List<ActiveConsumeData> giftList; // 奖励模版列表
	private int expenseTotal; // 消费总数

	public int getExpenseTotal() {
		return expenseTotal;
	}

	public void setExpenseTotal(int expenseTotal) {
		this.expenseTotal = expenseTotal;
	}

	public List<ActiveConsumeData> getGiftList() {
		return giftList;
	}

	public void setGiftList(List<ActiveConsumeData> giftList) {
		this.giftList = giftList;
	}

	public List<Integer> getRewardList() {
		return rewardList;
	}

	public void setRewardList(List<Integer> rewardList) {
		this.rewardList = rewardList;
	}

	public Map<String, Object> getItemMap() {
		return itemMap;
	}

	public void setItemMap(Map<String, Object> itemMap) {
		this.itemMap = itemMap;
	}

	public List<GoodsBean> getShowMap() {
		return showMap;
	}

	public void setShowMap(List<GoodsBean> showMap) {
		this.showMap = showMap;
	}
}
