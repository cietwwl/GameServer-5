package com.mi.game.module.festival.protocol;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mi.core.protocol.BaseProtocol;
import com.mi.game.module.festival.pojo.ValentineEntity;
import com.mi.game.module.reward.data.GoodsBean;

public class FestivalValentineProtocol extends BaseProtocol{
	private ValentineEntity valentineEntity;
	private Map<String,Object> itemMap;
	private List<GoodsBean> showMap;
	
	@Override
	public Map<String,Object> responseMap(){
		Map<String,Object> data = new HashMap<String, Object>();
		data.put("valentineEntity", valentineEntity.responseMap());
		data.put("itemMap",itemMap);
		data.put("showMap", showMap);
		return data;
	}
	
	public List<GoodsBean> getShowMap() {
		return showMap;
	}
	public void setShowMap(List<GoodsBean> showMap) {
		this.showMap = showMap;
	}
	public ValentineEntity getValentineEntity() {
		return valentineEntity;
	}
	public void setValentineEntity(ValentineEntity valentineEntity) {
		this.valentineEntity = valentineEntity;
	}
	public Map<String, Object> getItemMap() {
		return itemMap;
	}
	public void setItemMap(Map<String, Object> itemMap) {
		this.itemMap = itemMap;
	}
	
	
}
