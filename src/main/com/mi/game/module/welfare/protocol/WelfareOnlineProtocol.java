package com.mi.game.module.welfare.protocol;

import java.util.HashMap;
import java.util.Map;

import com.mi.core.protocol.BaseProtocol;
import com.mi.game.module.welfare.pojo.WelfareOnlineEntity;

public class WelfareOnlineProtocol extends BaseProtocol {

	private WelfareOnlineEntity onlineEntity;
	private Map<String, Object> itemMap;

	@Override
	public Map<String, Object> responseMap() {
		Map<String, Object> response = new HashMap<String, Object>();
		if (itemMap != null) {
			response.put("itemMap", itemMap);
		}
		if (onlineEntity != null) {
			response.put("online", onlineEntity.responseMap());
		}
		return response;
	}

	public WelfareOnlineEntity getEventOnlineEntity() {
		return onlineEntity;
	}

	public void setEventOnlineEntity(WelfareOnlineEntity eventOnlineEntity) {
		this.onlineEntity = eventOnlineEntity;
	}

	public Map<String, Object> getItemMap() {
		return itemMap;
	}

	public void setItemMap(Map<String, Object> itemMap) {
		this.itemMap = itemMap;
	}

}
