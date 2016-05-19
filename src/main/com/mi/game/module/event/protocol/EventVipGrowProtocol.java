package com.mi.game.module.event.protocol;

import java.util.HashMap;
import java.util.Map;

import com.mi.core.protocol.BaseProtocol;
import com.mi.game.module.event.define.EventConstans;
import com.mi.game.module.event.pojo.EventVipGrowEntity;

public class EventVipGrowProtocol extends BaseProtocol {

	private EventVipGrowEntity vipGrowEntity;
	private Map<String, Object> itemMap;

	@Override
	public Map<String, Object> responseMap() {
		Map<String, Object> response = new HashMap<String, Object>();
		if (vipGrowEntity != null) {
			response.put(EventConstans.EVENT_TYPE_VIPGROW + "", vipGrowEntity.responseMap());
		}
		if (itemMap != null) {
			response.put("itemMap", itemMap);
		}
		return response;
	}

	public EventVipGrowEntity getVipGrowEntity() {
		return vipGrowEntity;
	}

	public void setVipGrowEntity(EventVipGrowEntity vipGrowEntity) {
		this.vipGrowEntity = vipGrowEntity;
	}

	public Map<String, Object> getItemMap() {
		return itemMap;
	}

	public void setItemMap(Map<String, Object> itemMap) {
		this.itemMap = itemMap;
	}

}
