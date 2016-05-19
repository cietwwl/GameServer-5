package com.mi.game.module.festival.protocol;

import java.util.HashMap;
import java.util.Map;

import com.mi.core.protocol.BaseProtocol;
import com.mi.game.module.festival.pojo.NewYearPayMoreEntity;

public class FestivalPayMoreProtocol extends BaseProtocol {
	private Map<String, Object> itemMap;
	private NewYearPayMoreEntity payMoreEntity;

	@Override
	public Map<String, Object> responseMap() {
		Map<String, Object> response = new HashMap<String, Object>();
		if (itemMap != null) {
			response.put("itemMap", itemMap);
		}
		if (payMoreEntity != null) {
			response.put("1106002", payMoreEntity.responseMap());
		}
		return response;
	}

	public Map<String, Object> getItemMap() {
		return itemMap;
	}

	public void setItemMap(Map<String, Object> itemMap) {
		this.itemMap = itemMap;
	}

	public NewYearPayMoreEntity getPayMoreEntity() {
		return payMoreEntity;
	}

	public void setPayMoreEntity(NewYearPayMoreEntity payMoreEntity) {
		this.payMoreEntity = payMoreEntity;
	}

}
