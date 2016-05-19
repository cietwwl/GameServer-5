package com.mi.game.module.lead.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.utils.IndexDirection;
import com.mi.core.pojo.BaseEntity;
@Entity(noClassnameStored = true)
public class LeadDesitnyEntity extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4808776870088911166L;
	@Indexed(value = IndexDirection.ASC, unique = true)	
	private String playerID;
	private int destinyID;
	private Map<Integer, HeroPrototype> prototype;
	private int point;
	private int destinyNum;

	private int nextSilver;
	private List<HeroPrototype> nextPrototype;
	private int nextPoint;

	@Override
	public Map<String, Object> responseMap() {
		Map<String, Object> data = new HashMap<>();
		data.put("destinyID", destinyID);
		if (prototype != null) {
			data.put("prototype", prototype.values());
		}
		data.put("point", point);
		data.put("nextSilver", nextSilver);
		data.put("nextPrototype", nextPrototype);
		data.put("nextPoint", nextPoint);
		data.put("destinyNum", destinyNum);
		return data;
	}

	@Override
	public Map<String, Object> responseMap(int t) {
		Map<String, Object> data = new HashMap<>();
		switch (t) {
		case 11111:
			data.put("playerID", playerID);
			data.put("destinyID", destinyID);
			data.put("point", point);
			data.put("nextSilver", nextSilver);
			data.put("nextPoint", nextPoint);
			data.put("destinyNum", destinyNum);
			break;
		}
		return data;
	}

	public int getDestinyNum() {
		return destinyNum;
	}

	public void setDestinyNum(int destinyNum) {
		this.destinyNum = destinyNum;
	}

	public int getNextSilver() {
		return nextSilver;
	}

	public void setNextSilver(int nextSilver) {
		this.nextSilver = nextSilver;
	}

	public int getNextPoint() {
		return nextPoint;
	}

	public void setNextPoint(int nextPoint) {
		this.nextPoint = nextPoint;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public int getDestinyID() {
		return destinyID;
	}

	public void setDestinyID(int destinyID) {
		this.destinyID = destinyID;
	}

	public String getPlayerID() {
		return playerID;
	}

	public void setPlayerID(String playerID) {
		this.playerID = playerID;
	}

	public Map<Integer, HeroPrototype> getPrototype() {
		if (prototype == null) {
			prototype = new HashMap<>();
		}
		return prototype;
	}

	public void setPrototype(Map<Integer, HeroPrototype> prototype) {
		this.prototype = prototype;
	}

	public List<HeroPrototype> getNextPrototype() {
		if (nextPrototype == null) {
			nextPrototype = new ArrayList<>();
		}
		return nextPrototype;
	}

	public void setNextPrototype(List<HeroPrototype> nextPrototype) {
		this.nextPrototype = nextPrototype;
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
		playerID = key.toString();
	}

}
