package com.mi.game.module.event.protocol;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mi.core.protocol.BaseProtocol;
import com.mi.game.module.event.define.EventConstans;
import com.mi.game.module.event.pojo.EventChickenEntity;
import com.mi.game.module.reward.data.GoodsBean;

public class EventChickenProtocol extends BaseProtocol {

	private EventChickenEntity chickenEntity;
	private Map<String, Object> itemMap;
	private List<GoodsBean> showMap;

	@Override
	public Map<String, Object> responseMap() {
		Map<String, Object> response = new HashMap<String, Object>();
		if (itemMap != null) {
			response.put("itemMap", itemMap);
		}
		if (showMap != null) {
			response.put("showMap", showMap);
		}
		if (chickenEntity != null) {
			response.put(EventConstans.EVENT_TYPE_CHICKEN + "", chickenEntity.responseMap());
		}
		return response;
	}

	public EventChickenEntity getChickenEntity() {
		return chickenEntity;
	}

	public void setChickenEntity(EventChickenEntity chickenEntity) {
		this.chickenEntity = chickenEntity;
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
