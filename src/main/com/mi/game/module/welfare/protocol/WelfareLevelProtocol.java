package com.mi.game.module.welfare.protocol;

import java.util.HashMap;
import java.util.Map;

import com.mi.core.protocol.BaseProtocol;
import com.mi.game.module.welfare.pojo.WelfareLevelEntity;

public class WelfareLevelProtocol extends BaseProtocol {

	private WelfareLevelEntity levelEntity;
	private Map<String, Object> itemMap;

	@Override
	public Map<String, Object> responseMap() {
		Map<String, Object> response = new HashMap<String, Object>();
		if (itemMap != null) {
			response.put("itemMap", itemMap);
		}
		if (levelEntity != null) {
			response.put("level", levelEntity.responseMap());
		}
		return response;
	}

	public WelfareLevelEntity getEventLevelEntity() {
		return levelEntity;
	}

	public void setEventLevelEntity(WelfareLevelEntity eventLevelEntity) {
		this.levelEntity = eventLevelEntity;
	}

	public Map<String, Object> getItemMap() {
		return itemMap;
	}

	public void setItemMap(Map<String, Object> itemMap) {
		this.itemMap = itemMap;
	}

}
