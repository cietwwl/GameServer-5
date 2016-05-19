package com.mi.game.module.welfare.protocol;

import java.util.HashMap;
import java.util.Map;

import com.mi.core.protocol.BaseProtocol;
import com.mi.game.module.welfare.pojo.WelfareMonthEntity;

public class WelfareMonthProtocol extends BaseProtocol {

	private WelfareMonthEntity monthEntity;
	private Map<String, Object> itemMap;

	@Override
	public Map<String, Object> responseMap() {
		Map<String, Object> response = new HashMap<String, Object>();
		if (itemMap != null) {
			response.put("itemMap", itemMap);
		}
		if (monthEntity != null) {
			response.put("month", monthEntity.responseMap());
		}
		return response;
	}

	public WelfareMonthEntity getMonthEntity() {
		return monthEntity;
	}

	public void setMonthEntity(WelfareMonthEntity monthEntity) {
		this.monthEntity = monthEntity;
	}

	public Map<String, Object> getItemMap() {
		return itemMap;
	}

	public void setItemMap(Map<String, Object> itemMap) {
		this.itemMap = itemMap;
	}

}
