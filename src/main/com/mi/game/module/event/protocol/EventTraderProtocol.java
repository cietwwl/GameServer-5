package com.mi.game.module.event.protocol;

import java.util.HashMap;
import java.util.Map;

import com.mi.core.protocol.BaseProtocol;
import com.mi.game.module.event.define.EventConstans;
import com.mi.game.module.event.pojo.EventTraderEntity;
import com.mi.game.module.vip.pojo.VipEntity;

public class EventTraderProtocol extends BaseProtocol {

	private EventTraderEntity traderEntity;
	private VipEntity vipEntity;
	private Map<String, Object> itemMap;

	@Override
	public Map<String, Object> responseMap() {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		if (traderEntity != null) {
			if (traderEntity.getExistTime() > 0) {
				responseMap.put(EventConstans.EVENT_TYPE_TRADER + "", traderEntity.responseMap());
			}
		}
		if (itemMap != null) {
			responseMap.put("itemMap", itemMap);
		}
		if (vipEntity != null) {
			responseMap.put("vipInfo", vipEntity.responseMap());
		}
		return responseMap;
	}

	public EventTraderEntity getTraderEntity() {
		return traderEntity;
	}

	public void setTraderEntity(EventTraderEntity traderEntity) {
		this.traderEntity = traderEntity;
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
