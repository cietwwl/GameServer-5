package com.mi.game.module.event.protocol;

import java.util.List;
import java.util.Map;

import com.mi.core.protocol.BaseProtocol;
import com.mi.game.module.event.data.DailyPayGiftData;
import com.mi.game.module.event.pojo.EventPayEveryDayEntity;
import com.mi.game.module.reward.data.GoodsBean;

/**
 * 每日充值活动
 * 
 * @author 赵鹏翔
 * @time Apr 8, 2015 11:22:34 AM
 */
public class EventPayEveryDayProtocol extends BaseProtocol {

	private EventPayEveryDayEntity eventPayEveryDayEntity;
	private Map<String, Object> itemMap;
	private List<GoodsBean> showMap;
	private List<Integer> rewardList; // 当天已领取奖励列表
	private List<DailyPayGiftData> giftList; // 奖励模版列表
	private int payTotal; // 充值总数

	public int getPayTotal() {
		return payTotal;
	}

	public void setPayTotal(int payTotal) {
		this.payTotal = payTotal;
	}

	public List<DailyPayGiftData> getGiftList() {
		return giftList;
	}

	public void setGiftList(List<DailyPayGiftData> giftList) {
		this.giftList = giftList;
	}

	public List<Integer> getRewardList() {
		return rewardList;
	}

	public void setRewardList(List<Integer> rewardList) {
		this.rewardList = rewardList;
	}

	public EventPayEveryDayEntity getEventPayEveryDayEntity() {
		return eventPayEveryDayEntity;
	}

	public void setEventPayEveryDayEntity(
			EventPayEveryDayEntity eventPayEveryDayEntity) {
		this.eventPayEveryDayEntity = eventPayEveryDayEntity;
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
