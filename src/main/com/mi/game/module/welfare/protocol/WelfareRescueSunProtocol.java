package com.mi.game.module.welfare.protocol;

import java.util.HashMap;
import java.util.Map;

import com.mi.core.protocol.BaseProtocol;
import com.mi.game.module.reward.data.GoodsBean;
import com.mi.game.module.welfare.pojo.WelfareRescueSunEntity;

public class WelfareRescueSunProtocol extends BaseProtocol{
	private Map<String,Object> itemMap;
	private Map<String,GoodsBean> showMap ;
	private WelfareRescueSunEntity rescueSunEntity;
	
	@Override
	public Map<String,Object> responseMap(){
		Map<String,Object> data = new HashMap<String, Object>();
		if(showMap != null)
			data.put("showMap", showMap.values());
		if(itemMap != null)
			data.put("itemMap", itemMap);
		if(rescueSunEntity != null)
			data.put("rescueSunEntity", rescueSunEntity.responseMap());
		return data;
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
	public WelfareRescueSunEntity getRescueSunEntity() {
		return rescueSunEntity;
	}
	public void setRescueSunEntity(WelfareRescueSunEntity rescueSunEntity) {
		this.rescueSunEntity = rescueSunEntity;
	}
	
	
}
