package com.mi.game.module.festival.protocol;

import java.util.HashMap;
import java.util.Map;

import com.mi.core.protocol.BaseProtocol;
import com.mi.game.module.festival.pojo.LaborDayPayMoreEntity;

public class FestivalLaborDayPayMoreProtocol extends BaseProtocol {
	private Map<String, Object> itemMap;
	private LaborDayPayMoreEntity payMoreEntity;

	@Override
	public Map<String, Object> responseMap() {
		Map<String, Object> response = new HashMap<String, Object>();
		if (itemMap != null) {
			response.put("itemMap", itemMap);
		}
		if (payMoreEntity != null) {
			response.put("1106009", payMoreEntity.responseMap());
		}
		return response;
	}

	public Map<String, Object> getItemMap() {
		return itemMap;
	}

	public void setItemMap(Map<String, Object> itemMap) {
		this.itemMap = itemMap;
	}

	public LaborDayPayMoreEntity getPayMoreEntity() {
		return payMoreEntity;
	}

	public void setPayMoreEntity(LaborDayPayMoreEntity payMoreEntity) {
		this.payMoreEntity = payMoreEntity;
	}

}
