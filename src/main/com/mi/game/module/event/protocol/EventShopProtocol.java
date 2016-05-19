package com.mi.game.module.event.protocol;

import java.util.HashMap;
import java.util.Map;

import com.mi.core.protocol.BaseProtocol;
import com.mi.game.module.event.define.EventConstans;
import com.mi.game.module.event.pojo.EventShopEntity;
import com.mi.game.module.vip.pojo.VipEntity;

public class EventShopProtocol extends BaseProtocol {

	private EventShopEntity shopEntity;
	private Map<String, Object> itemMap;
	private VipEntity vipEntity;

	@Override
	public Map<String, Object> responseMap() {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		if (shopEntity != null) {
			responseMap.put(EventConstans.EVENT_TYPE_SHOP + "", shopEntity.responseMap());
		}
		if (itemMap != null) {
			responseMap.put("itemMap", itemMap);
		}
		if (vipEntity != null) {
			responseMap.put("vipEntity", vipEntity.responseMap());
		}
		return responseMap;
	}

	public EventShopEntity getShopEntity() {
		return shopEntity;
	}

	public void setShopEntity(EventShopEntity shopEntity) {
		this.shopEntity = shopEntity;
	}

	public Map<String, Object> getItemMap() {
		return itemMap;
	}

	public void setItemMap(Map<String, Object> itemMap) {
		this.itemMap = itemMap;
	}

	public VipEntity getVipEntity() {
		return vipEntity;
	}

	public void setVipEntity(VipEntity vipEntity) {
		this.vipEntity = vipEntity;
	}

}
