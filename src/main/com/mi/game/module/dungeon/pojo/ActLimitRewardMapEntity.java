package com.mi.game.module.dungeon.pojo;

import java.util.HashMap;
import java.util.Map;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.utils.IndexDirection;
import com.mi.core.pojo.BaseEntity;
@Entity(noClassnameStored = true)
public class ActLimitRewardMapEntity extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8610945839990877545L;
	@Indexed(unique=true,value=IndexDirection.ASC)
	private String playerID;
	private Map<Integer,ActLimitReward> limitList ;
	
	@Override
	public Map<String,Object> responseMap(){
		Map<String,Object> data = new HashMap<String, Object>();
		if(limitList != null)
		data.put("actLimitList", limitList.values());
		return data;
	}
		
	public Map<Integer, ActLimitReward> getLimitList() {
		if(limitList == null){
			limitList = new HashMap<>();
		}
		return limitList;
	}

	public void setLimitList(Map<Integer, ActLimitReward> limitList) {
		this.limitList = limitList;
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
		playerID  = key.toString();
	}
	
}
