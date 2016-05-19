package com.mi.game.module.store.protocol;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mi.core.protocol.BaseProtocol;
import com.mi.game.defines.HandlerIds;
import com.mi.game.module.reward.data.GoodsBean;
import com.mi.game.module.store.pojo.StoreEntity;

public class StoreProtocol extends BaseProtocol {

	private StoreEntity storeEntity;
	private Map<String, Object> itemMap;
	private List<GoodsBean> showMap;

	@Override
	public Map<String, Object> responseMap(int y) {
		Map<String, Object> responeMap = new HashMap<String, Object>();
		switch (y) {
		case HandlerIds.STORE_BUY:
			if (storeEntity != null) {
				responeMap.put("storeInfo", storeEntity.responseMap());
			}
			if (itemMap != null) {
				responeMap.put("itemMap", itemMap);
			}
			if (showMap != null) {
				responeMap.put("showMap", showMap);
			}
			break;
		case HandlerIds.STORE_INFO:
			if (storeEntity != null) {
				responeMap.put("storeInfo", storeEntity.responseMap());
			}
			break;
		}
		return responeMap;
	}

	public StoreEntity getStoreEntity() {
		return storeEntity;
	}

	public void setStoreEntity(StoreEntity storeEntity) {
		this.storeEntity = storeEntity;
	}

	public Map<String, Object> getItemMap() {
		return itemMap;
	}

	public void setItemMap(Map<String, Object> itemMap) {
		this.itemMap = itemMap;
	}

	public List<GoodsBean> getShowMap() {
		return showMap;
	}

	public void setShowMap(List<GoodsBean> showMap) {
		this.showMap = showMap;
	}

}
