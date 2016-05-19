package com.mi.game.module.cdk.pojo;

import java.util.HashMap;
import java.util.Map;

import com.google.code.morphia.annotations.Indexed;
import com.mi.core.pojo.BaseEntity;

public class PlayerCDKEntity extends BaseEntity {

	private static final long serialVersionUID = 1567279263354158405L;

	@Indexed
	private String playerID;

	// 已使用cdk类型,数量
	private Map<String, Integer> useCDK = new HashMap<String, Integer>();;

	public String getPlayerID() {
		return playerID;
	}

	public void setPlayerID(String playerID) {
		this.playerID = playerID;
	}

	public Map<String, Integer> getUseCDK() {
		return useCDK;
	}

	public void setUseCDK(Map<String, Integer> useCDK) {
		this.useCDK = useCDK;
	}

	@Override
	public Object getKey() {
		return playerID;
	}

	@Override
	public String getKeyName() {
		return "playerID";
	}

	@Override
	public void setKey(Object key) {
		this.playerID = key.toString();
	}

	@Override
	public Map<String, Object> responseMap() {
		return super.responseMap();
	}

	@Override
	public Map<String, Object> responseMap(int t) {
		return super.responseMap(t);
	}

}
