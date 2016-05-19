package com.mi.game.module.smelt.protocol;

import java.util.HashMap;
import java.util.Map;

import com.mi.core.protocol.BaseProtocol;
import com.mi.game.module.reward.data.GoodsBean;
import com.mi.game.util.CommonMethod;

public class SmeltProtocol extends BaseProtocol{
	private Map<String,Object> itemMap;
	private Map<Integer,GoodsBean> showMap;
	private Object rebirthItem;
	
	public Object getRebirthItem() {
		return rebirthItem;
	}
	public void setRebirthItem(Object rebirthItem) {
		this.rebirthItem = rebirthItem;
	}
	public Map<String, Object> getItemMap() {
		return itemMap;
	}
	public void setItemMap(Map<String, Object> itemMap) {
		this.itemMap = itemMap;
	}
	public Map<Integer, GoodsBean> getShowMap() {
		return showMap;
	}
	public void setShowMap(Map<Integer, GoodsBean> showMap) {
		this.showMap = showMap;
	}
	
	@Override
	public Map<String, Object> responseMap(){
		Map<String,Object> data = new HashMap<String, Object>();
		if(itemMap != null)
			data.put("itemMap", itemMap);
		if(showMap != null)
			data.put("showMap", CommonMethod.getResponseListMap(showMap.values()));
		return data;
	}
}
