package com.mi.game.module.dungeon.pojo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.utils.IndexDirection;
import com.mi.core.pojo.BaseEntity;
@Entity(noClassnameStored = true)
public class DungeonActiveEntity extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4947970795211487059L;
	@Indexed(unique=true,value=IndexDirection.ASC)
	private String playerID;
	private Map<Integer,DungeonActive> dungeonList;
	private long lastUpdateTime;
	
	public Map<Integer, DungeonActive> getDungeonList() {
		if(dungeonList == null){
			dungeonList = new HashMap<Integer, DungeonActive>();
		}
		return dungeonList;
	}

	public void setDungeonList(Map<Integer, DungeonActive> dungeonList) {
		this.dungeonList = dungeonList;
	}

	@Override
	public Object getKey() {
		// TODO Auto-generated method stub
		return playerID;
	}

	@Override
	public String getKeyName() {
		// TODO Auto-generated method stub
		return "playerID";
	}

	@Override
	public void setKey(Object arg0) {
		// TODO Auto-generated method stub
		playerID = arg0.toString();
	}
	
	
	public long getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	
	@Override
	public Map<String,Object> responseMap(){
		Map<String,Object> map = new HashMap<String, Object>();
		Collection<DungeonActive> list = new ArrayList<DungeonActive>();
		if(this.dungeonList != null){
			for(DungeonActive active : dungeonList.values()){
				list.add(active);
			}
		}
		map.put("dungeonList", list);
		return map;
	}
	
}
