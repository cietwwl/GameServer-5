package com.mi.game.module.dungeon.pojo;

import java.util.HashMap;
import java.util.Map;

import com.mi.core.engine.IResponseMap;

public class Dungeon implements IResponseMap{
	private int templateID;
	private int starLevel;
	private int attackNum;

	@Override
	public Map<String, Object> responseMap() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("templateID", templateID);
		data.put("starLevel", starLevel);
		data.put("attackNum", attackNum);
		return data;
	}
	@Override
	public Map<String, Object> responseMap(int t) {
		// TODO 自动生成的方法存根
		return this.responseMap();
	}
	public int getAttackNum() {
		return attackNum;
	}
	public void setAttackNum(int attackNum) {
		this.attackNum = attackNum;
	}
	public int getTemplateID() {
		return templateID;
	}
	public void setTemplateID(int templateID) {
		this.templateID = templateID;
	}
	public int getStarLevel() {
		return starLevel;
	}
	public void setStarLevel(int starLevel) {
		this.starLevel = starLevel;
	}

	
	
}
