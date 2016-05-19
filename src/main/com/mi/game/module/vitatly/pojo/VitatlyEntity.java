package com.mi.game.module.vitatly.pojo;

import java.util.HashMap;
import java.util.Map;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.utils.IndexDirection;
import com.mi.core.pojo.BaseEntity;
@Entity(noClassnameStored = true)
public class VitatlyEntity extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2419640690347005786L;
	@Indexed(value=IndexDirection.ASC, unique=true)
	private String playerID;
	private long vitatly;
	private long energy;
	private long maxEnergy;
	private long maxVitatly;
	private long lastUpdateVitatlyTime;
	private long lastUpdateEnergyTime;
	
	@Override
	public Map<String, Object> responseMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("vitatly", vitatly);
		map.put("energy", energy);
		map.put("maxVitatly", maxVitatly);
		map.put("maxEnergy", maxEnergy);
		map.put("lastUpdateEnergyTime", lastUpdateEnergyTime);
		map.put("lastUpdateVitatlyTime", lastUpdateVitatlyTime);
		return map;
	}
	
	
	
	@Override
	public Map<String, Object> responseMap(int t) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("playerID", playerID);
		map.put("vitatly", vitatly);
		map.put("energy", energy);
		map.put("maxVitatly", maxVitatly);
		map.put("maxEnergy", maxEnergy);
		map.put("lastUpdateEnergyTime", lastUpdateEnergyTime);
		map.put("lastUpdateVitatlyTime", lastUpdateVitatlyTime);
		return map;
	}



	public long getLastUpdateVitatlyTime() {
		return lastUpdateVitatlyTime;
	}
	public void setLastUpdateVitatlyTime(long lastUpdateVitatlyTime) {
		this.lastUpdateVitatlyTime = lastUpdateVitatlyTime;
	}
	public long getLastUpdateEnergyTime() {
		return lastUpdateEnergyTime;
	}
	public void setLastUpdateEnergyTime(long lastUpdateEnergyTime) {
		this.lastUpdateEnergyTime = lastUpdateEnergyTime;
	}
	public long getVitatly() {
		return vitatly;
	}
	public void setVitatly(long vitatly) {
		this.vitatly = vitatly;
	}
	public long getEnergy() {
		return energy;
	}
	public void setEnergy(long energy) {
		this.energy = energy;
	}
	public long getMaxEnergy() {
		return maxEnergy;
	}
	public void setMaxEnergy(long maxEnergy) {
		this.maxEnergy = maxEnergy;
	}
	public long getMaxVitatly() {
		return maxVitatly;
	}
	public void setMaxVitatly(long maxVitatly) {
		this.maxVitatly = maxVitatly;
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
