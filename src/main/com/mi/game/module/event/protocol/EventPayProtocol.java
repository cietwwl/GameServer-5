package com.mi.game.module.event.protocol;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mi.core.protocol.BaseProtocol;
import com.mi.game.module.event.data.ChargeGiftData;
import com.mi.game.module.event.define.EventConstans;
import com.mi.game.module.event.pojo.EventPayEntity;
import com.mi.game.module.reward.data.GoodsBean;

public class EventPayProtocol extends BaseProtocol {

	private EventPayEntity eventPayEntity;
	private Map<String, Object> itemMap;
	private List<GoodsBean> showMap;
	private List<ChargeGiftData> chargeGiftList;		

	@Override
	public Map<String, Object> responseMap() {
		Map<String, Object> response = new HashMap<String, Object>();
		if (eventPayEntity != null) {
			response.put(EventConstans.EVENT_TYPE_PAY + "", eventPayEntity.responseMap());
		}
		if (itemMap != null) {
			response.put("itemMap", itemMap);
		}
		if (showMap != null) {
			response.put("showMap", showMap);
		}
		return response;
	}

	public EventPayEntity getEventPayEntity() {
		return eventPayEntity;
	}

	public void setEventPayEntity(EventPayEntity eventPayEntity) {
		this.eventPayEntity = eventPayEntity;
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
	
	public List<ChargeGiftData> getChargeGiftList() {
		return chargeGiftList;
	}

	public void setChargeGiftList(List<ChargeGiftData> chargeGiftList) {
		this.chargeGiftList = chargeGiftList;
	}

}
