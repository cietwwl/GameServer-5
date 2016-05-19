package com.mi.game.module.festival.protocol;

import java.util.HashMap;
import java.util.Map;

import com.mi.core.pojo.BaseEntity;
import com.mi.core.protocol.BaseProtocol;

public class FestivalProtocol extends BaseProtocol {
	private Map<String, Object> entityMap = new HashMap<String, Object>();
	private String festivalID;

	@Override
	public Map<String, Object> responseMap() {
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("festivals", entityMap);
		response.put("festivalID", festivalID);
		return response;
	}

	public void addEntity(String name, BaseEntity entity) {
		if (entity != null) {
			entityMap.put(name, entity.responseMap());
		}
	}

	public Map<String, Object> getEntityMap() {
		return entityMap;
	}

	public void setEntityMap(Map<String, Object> entityMap) {
		this.entityMap = entityMap;
	}

	public String getFestivalID() {
		return festivalID;
	}

	public void setFestivalID(String festivalID) {
		this.festivalID = festivalID;
	}

}
