package com.mi.game.module.vip.protocol;

import java.util.HashMap;
import java.util.Map;

import com.mi.core.protocol.BaseProtocol;
import com.mi.game.defines.HandlerIds;
import com.mi.game.module.vip.pojo.VipEntity;

public class VipProtocol extends BaseProtocol {
	private VipEntity vipEntity;
	private Map<String, Object> itemMap;

	@Override
	public Map<String, Object> responseMap(int y) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		switch (y) {
		case HandlerIds.VIP_GETINFO:
			if (vipEntity != null) {
				responseMap.put("vipInfo", vipEntity.responseMap());
			}
			if (itemMap != null) {
				responseMap.put("itemMap", itemMap);
			}

			break;
		case HandlerIds.VIP_PERMISSION:
			if (vipEntity != null) {
				responseMap.put("vipInfo", vipEntity.responseMap());
			}
			if (itemMap != null) {
				responseMap.put("itemMap", itemMap);
			}
			break;
		}
		return responseMap;
	}

	public VipEntity getVipEntity() {
		return vipEntity;
	}

	public void setVipEntity(VipEntity vipEntity) {
		this.vipEntity = vipEntity;
	}

	public Map<String, Object> getItemMap() {
		return itemMap;
	}

	public void setItemMap(Map<String, Object> itemMap) {
		this.itemMap = itemMap;
	}

}
