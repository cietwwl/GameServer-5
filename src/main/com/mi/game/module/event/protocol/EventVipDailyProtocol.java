package com.mi.game.module.event.protocol;

import java.util.HashMap;
import java.util.Map;

import com.mi.core.protocol.BaseProtocol;
import com.mi.game.module.event.define.EventConstans;
import com.mi.game.module.event.pojo.EventVipDailyEntity;

public class EventVipDailyProtocol extends BaseProtocol {

	private EventVipDailyEntity vipDailyEntity;
	private Map<String, Object> itemMap;

	@Override
	public Map<String, Object> responseMap() {
		Map<String, Object> response = new HashMap<String, Object>();
		if (vipDailyEntity != null) {
			response.put(EventConstans.EVENT_TYPE_VIPDAILY + "", vipDailyEntity.responseMap());
		}
		if (itemMap != null) {
			response.put("itemMap", itemMap);
		}
		return response;
	}

	public EventVipDailyEntity getVipDailyEntity() {
		return vipDailyEntity;
	}

	public void setVipDailyEntity(EventVipDailyEntity vipDailyEntity) {
		this.vipDailyEntity = vipDailyEntity;
	}

	public Map<String, Object> getItemMap() {
		return itemMap;
	}

	public void setItemMap(Map<String, Object> itemMap) {
		this.itemMap = itemMap;
	}

}
