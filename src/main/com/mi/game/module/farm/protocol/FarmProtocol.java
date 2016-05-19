package com.mi.game.module.farm.protocol;

import java.util.HashMap;
import java.util.Map;

import com.mi.core.protocol.BaseProtocol;
import com.mi.game.defines.HandlerIds;
import com.mi.game.module.farm.pojo.FarmEntity;
import com.mi.game.module.reward.data.GoodsBean;

public class FarmProtocol extends BaseProtocol{
	private Map<String,Object> itemMap;
	private Map<String,GoodsBean> showMap ;
	private FarmEntity farmEntity ;
	
	@Override
	public Map<String,Object> responseMap(int y){
		Map<String,Object> data = new HashMap<String, Object>();
		switch(y){
			case HandlerIds.startFarmHandler:
				if(farmEntity != null)
					data.put("farmEntity", farmEntity.responseMap());
				break;
			case HandlerIds.cancelFarmHandler:
				if(farmEntity != null)
					data.put("farmEntity", farmEntity.responseMap());
				break;
			case HandlerIds.getFarmReward:
				if(showMap != null)
					data.put("showMap", showMap.values());
				data.put("itemMap",itemMap);
				if(farmEntity != null)
					data.put("farmEntity", farmEntity.responseMap());
				break;
			case HandlerIds.getFarmHandler:
				data.put("farmEntity", farmEntity.responseMap());
				break;
		}
		return data;
	}
	
	public FarmEntity getFarmEntity() {
		return farmEntity;
	}
	public void setFarmEntity(FarmEntity farmEntity) {
		this.farmEntity = farmEntity;
	}
	public Map<String, Object> getItemMap() {
		return itemMap;
	}
	public void setItemMap(Map<String, Object> itemMap) {
		this.itemMap = itemMap;
	}
	public Map<String, GoodsBean> getShowMap() {
		return showMap;
	}
	public void setShowMap(Map<String, GoodsBean> showMap) {
		this.showMap = showMap;
	}
	
	
}
