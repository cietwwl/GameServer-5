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
public class DungeonActMapEntity extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8785340207462769098L;
	@Indexed(unique=true,value=IndexDirection.ASC)
	private String playerID;
	private Map<Integer, DungeonAct> dungeMap;

	public Map<Integer, DungeonAct> getDungeMap() {
		if (dungeMap == null) {
			dungeMap = new HashMap<Integer, DungeonAct>();
		}
		return dungeMap;
	}

	public void setDungeMap(Map<Integer, DungeonAct> dungeMap) {
		this.dungeMap = dungeMap;
	}

	@Override
	public Map<String, Object> responseMap() {
		Map<String, Object> data = new HashMap<String, Object>();
		if (dungeMap != null) {
			Collection<Object> collections = new ArrayList<>();
			for (DungeonAct dungeonAct : dungeMap.values()) {
				Map<String, Object> returnMap = new HashMap<>();
				returnMap.put("templateID", dungeonAct.getTemplateID());
				returnMap.put("rewardState", dungeonAct.getRewardlist().values());
				returnMap.put("actPoint", dungeonAct.getActPoint());
				collections.add(returnMap);
			}
			data.put("dungeActList", collections);
		}
		return data;
	}

	@Override
	public Map<String, Object> responseMap(int t) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("playerID", playerID);
		Collection<Object> collections = new ArrayList<>();
		if (dungeMap != null) {
			for (DungeonAct dungeonAct : dungeMap.values()) {
				Map<String, Object> returnMap = new HashMap<>();
				returnMap.put("templateID", dungeonAct.getTemplateID());
				returnMap.put("rewardState", dungeonAct.getRewardlist().values());
				returnMap.put("actPoint", dungeonAct.getActPoint());
				collections.add(returnMap);
			}
		}
		data.put("dungeAct", collections.size());
		return data;
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
		this.playerID = key.toString();
	}

}
