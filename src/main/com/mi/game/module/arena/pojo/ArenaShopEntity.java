package com.mi.game.module.arena.pojo;

import java.util.HashMap;
import java.util.Map;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.utils.IndexDirection;
import com.mi.core.pojo.BaseEntity;
@Entity(noClassnameStored = true)
public class ArenaShopEntity extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6712707870204831622L;
	@Indexed(unique=true,value=IndexDirection.ASC)
	private String playerID;
	private Map<Integer,ArenaShopInfo> shopMap;
	private long updateTime;
	
	@Override
	public Map<String,Object> responseMap(){
		Map<String,Object> data = new HashMap<String, Object>();
		if(shopMap != null)
			data.put("shopList",shopMap.values());
		return data;
	}
	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public Map<Integer, ArenaShopInfo> getShopMap() {
		return shopMap;
	}

	public void setShopMap(Map<Integer, ArenaShopInfo> shopMap) {
		this.shopMap = shopMap;
	}

	@Override
	public Object getKey() {
		// TODO 自动生成的方法存根
		return playerID;
	}

	@Override
	public String getKeyName() {
		// TODO 自动生成的方法存根
		return "playerID";
	}

	@Override
	public void setKey(Object key) {
		// TODO 自动生成的方法存根
		playerID = key.toString();
	}
	
}
