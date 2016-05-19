package com.mi.game.module.event.protocol;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mi.core.protocol.BaseProtocol;
import com.mi.game.module.event.data.ActiveLuckyDrawData;
import com.mi.game.module.event.define.EventConstans;
import com.mi.game.module.event.pojo.EventLuckyDrawEntity;
import com.mi.game.module.reward.data.GoodsBean;

public class EventLuckyDrawProtocol extends BaseProtocol {

	private EventLuckyDrawEntity eventLuckyDrawEntity;	
	private Map<String, Object> itemMap;
	private List<GoodsBean> showMap;	
	private ActiveLuckyDrawData luckyDrawData;
	
	@Override
	public Map<String, Object> responseMap() {
		Map<String, Object> response = new HashMap<String, Object>();
		if (eventLuckyDrawEntity != null) {
			response.put(EventConstans.EVENT_TYPE_LUCKY_DRAW + "", eventLuckyDrawEntity.responseMap());
		}
		if (itemMap != null) {
			response.put("itemMap", itemMap);
		}
		if (showMap != null) {
			response.put("showMap", showMap);
		}
		if(luckyDrawData != null){
			response.put("luckyDrawData", luckyDrawData);
		}
		return response;
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
	
	public ActiveLuckyDrawData getLuckyDrawData() {
		return luckyDrawData;
	}

	public void setLuckyDrawData(ActiveLuckyDrawData luckyDrawData) {
		this.luckyDrawData = luckyDrawData;
	}
	public EventLuckyDrawEntity getEventLuckyDrawEntity() {
		return eventLuckyDrawEntity;
	}

	public void setEventLuckyDrawEntity(EventLuckyDrawEntity eventLuckyDrawEntity) {
		this.eventLuckyDrawEntity = eventLuckyDrawEntity;
	}
}
