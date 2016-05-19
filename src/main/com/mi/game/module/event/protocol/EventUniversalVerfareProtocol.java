package com.mi.game.module.event.protocol;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mi.core.protocol.BaseProtocol;
import com.mi.game.module.event.data.ActiveDailyRewardData;
import com.mi.game.module.event.define.EventConstans;
import com.mi.game.module.event.pojo.EventUniversalVerfareEntity;
import com.mi.game.module.reward.data.GoodsBean;

public class EventUniversalVerfareProtocol extends BaseProtocol {

	private EventUniversalVerfareEntity eventUniversalVerfareEntity;
	private Map<String, Object> itemMap;
	private List<GoodsBean> showMap;
	private ActiveDailyRewardData dailyRewardData;	
	// 普通用户已领取列表
	private List<Integer> rewardList;	

	//VIP用户已领取列表
	private List<Integer> rewardVipList;


	@Override
	public Map<String, Object> responseMap() {
		Map<String, Object> response = new HashMap<String, Object>();
		if (eventUniversalVerfareEntity != null) {
			response.put(EventConstans.EVENT_TYPE_UNIVERSAL_VERFARE + "", eventUniversalVerfareEntity.responseMap());
		}
		if (itemMap != null) {
			response.put("itemMap", itemMap);
		}
		if (showMap != null) {
			response.put("showMap", showMap);
		}
		if(rewardList!=null&&!rewardList.isEmpty()){
			response.put("rewardList", rewardList);
		}
		if(rewardVipList!=null&&!rewardVipList.isEmpty()){
			response.put("rewardVipList", rewardVipList);
		}
		return response;
	}
	

	public EventUniversalVerfareEntity getEventUniversalVerfareEntity() {
		return eventUniversalVerfareEntity;
	}
	public void setEventUniversalVerfareEntity(
			EventUniversalVerfareEntity eventUniversalVerfareEntity) {
		this.eventUniversalVerfareEntity = eventUniversalVerfareEntity;
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
	
	public ActiveDailyRewardData getDailyRewardData() {
		return dailyRewardData;
	}

	public void setDailyRewardData(ActiveDailyRewardData dailyRewardData) {
		this.dailyRewardData = dailyRewardData;
	}
	
	public List<Integer> getRewardList() {
		return rewardList;
	}

	public void setRewardList(List<Integer> rewardList) {
		this.rewardList = rewardList;
	}

	public List<Integer> getRewardVipList() {
		return rewardVipList;
	}

	public void setRewardVipList(List<Integer> rewardVipList) {
		this.rewardVipList = rewardVipList;
	}

}
