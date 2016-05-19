package com.mi.game.module.login.pojo;

import java.util.HashMap;
import java.util.Map;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.utils.IndexDirection;
import com.mi.core.pojo.BaseEntity;
@Entity(noClassnameStored = true)
public class NewPlayerEntity extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -670848308641247153L;
	@Indexed(unique=true,value=IndexDirection.ASC)
	private String playerID;
	private int properId;
	private boolean isFinality;
	private String triggerId;
	private int gameLevelID;

	@Override
	public Map<String, Object> responseMap() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("properId", properId);
		data.put("isFinality", isFinality);
		data.put("triggerId", triggerId);
		data.put("gameLevelID", gameLevelID);
		return data;
	}

	@Override
	public Map<String, Object> responseMap(int t) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("playerID", playerID);
		data.put("properId", properId);
		data.put("isFinality", isFinality);
		data.put("triggerId", triggerId);
		data.put("gameLevelID", gameLevelID);
		return data;
	}

	public int getGameLevelID() {
		return gameLevelID;
	}

	public void setGameLevelID(int gameLevelID) {
		this.gameLevelID = gameLevelID;
	}

	public int getProperId() {
		return properId;
	}

	public void setProperId(int properId) {
		this.properId = properId;
	}

	public boolean isFinality() {
		return isFinality;
	}

	public void setFinality(boolean isFinality) {
		this.isFinality = isFinality;
	}

	public String getTriggerId() {
		return triggerId;
	}

	public void setTriggerId(String triggerId) {
		this.triggerId = triggerId;
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
