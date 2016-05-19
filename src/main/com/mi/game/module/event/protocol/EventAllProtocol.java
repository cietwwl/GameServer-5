package com.mi.game.module.event.protocol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mi.core.pojo.BaseEntity;
import com.mi.core.protocol.BaseProtocol;

public class EventAllProtocol extends BaseProtocol {
	private Map<String, Object> entityMap = new HashMap<String, Object>();
	private List<String> order = new ArrayList<String>();;

	@Override
	public Map<String, Object> responseMap() {
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("events", entityMap);
		response.put("order", order);
		return response;
	}

	public void addEntity(String pid, BaseEntity entity) {
		if (entity != null) {
			entityMap.put(pid, entity.responseMap());
		}
	}

	public Map<String, Object> getEntityMap() {
		return entityMap;
	}

	public void setEntityMap(Map<String, Object> entityMap) {
		this.entityMap = entityMap;
	}

	public void addOrder(String pid) {
		if (entityMap.containsKey(pid)) {
			if (!order.contains(pid)) {
				order.add(pid);
			}
		}
	}

	public List<String> getOrder() {
		return order;
	}

}
