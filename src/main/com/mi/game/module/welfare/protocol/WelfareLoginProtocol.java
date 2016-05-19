package com.mi.game.module.welfare.protocol;

import java.util.HashMap;
import java.util.Map;

import com.mi.core.protocol.BaseProtocol;
import com.mi.game.module.welfare.pojo.WelfareLoginEntity;

public class WelfareLoginProtocol extends BaseProtocol {

	private WelfareLoginEntity loginEntity;
	private Map<String, Object> itemMap;

	@Override
	public Map<String, Object> responseMap() {
		Map<String, Object> response = new HashMap<String, Object>();
		if (itemMap != null) {
			response.put("itemMap", itemMap);
		}
		if (loginEntity != null) {
			response.put("login", loginEntity.responseMap());
		}
		return response;
	}

	public WelfareLoginEntity getEventLoginEntity() {
		return loginEntity;
	}

	public void setEventLoginEntity(WelfareLoginEntity eventLoginEntity) {
		this.loginEntity = eventLoginEntity;
	}

	public Map<String, Object> getItemMap() {
		return itemMap;
	}

	public void setItemMap(Map<String, Object> itemMap) {
		this.itemMap = itemMap;
	}

}
