package com.mi.game.module.welfare.protocol;

import java.util.HashMap;
import java.util.Map;

import com.mi.core.protocol.BaseProtocol;
import com.mi.game.module.welfare.pojo.WelfareSignEntity;

public class WelfareSignProtocol extends BaseProtocol {

	private WelfareSignEntity signEntity;
	private Map<String, Object> itemMap;

	@Override
	public Map<String, Object> responseMap() {
		Map<String, Object> response = new HashMap<String, Object>();
		if (itemMap != null) {
			response.put("itemMap", itemMap);
		}
		if (signEntity != null) {
			response.put("sign", signEntity.responseMap());
		}
		return response;
	}

	public WelfareSignEntity getEventSignEntity() {
		return signEntity;
	}

	public void setEventSignEntity(WelfareSignEntity eventSignEntity) {
		this.signEntity = eventSignEntity;
	}

	public Map<String, Object> getItemMap() {
		return itemMap;
	}

	public void setItemMap(Map<String, Object> itemMap) {
		this.itemMap = itemMap;
	}

}
