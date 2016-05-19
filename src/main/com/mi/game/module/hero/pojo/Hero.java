package com.mi.game.module.hero.pojo;

import java.util.HashMap;
import java.util.Map;

import com.mi.core.engine.IResponseMap;

public class Hero implements IResponseMap {
	private long heroID;                      //英雄ID
	private int level = 1;                     //英雄等级
 	private int templateID;               //模板ID
 	private int exp;                              //当前经验值
 	private int classLevel;                //进阶等级
 	private Map<String, Long> equipMap;       //装备列表
 	private Map<Integer,Double> heroAdvanceProp;
 	private Map<Integer,Double> heroFriendShipProp;
 	private Map<Integer,Double> equipFriendShipProp;
 	private Map<Integer,Double> talismanFriendShipProp;
 	private Map<Integer,Integer> equipmentSetProp; //
 	
 	@Override
 	public Map<String, Object> responseMap() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("heroID", heroID);
		data.put("level", level);
		data.put("templateID", templateID);
		data.put("exp", exp);
		data.put("classLevel", classLevel);
		data.put("equipMap", this.getEquipMap());
		return data;
	}
 	
	public Map<Integer, Double> getEquipFriendShipProp() {
		if(equipFriendShipProp == null)
			equipFriendShipProp = new HashMap<>();
		return equipFriendShipProp;
	}
	public void setEquipFriendShipProp(Map<Integer, Double> equipFriendShipProp) {
		this.equipFriendShipProp = equipFriendShipProp;
	}
	public Map<Integer, Double> getTalismanFriendShipProp() {
		if(talismanFriendShipProp == null)
			talismanFriendShipProp = new HashMap<>();
		return talismanFriendShipProp;
	}
	public void setTalismanFriendShipProp(
			Map<Integer, Double> talismanFriendShipProp) {
		this.talismanFriendShipProp = talismanFriendShipProp;
	}
	public Map<Integer, Double> getHeroFriendShipProp() {
		if(heroFriendShipProp == null)
			heroFriendShipProp = new HashMap<>();
		return heroFriendShipProp;
	}
	public void setHeroFriendShipProp(Map<Integer, Double> heroFriendShipProp) {
		this.heroFriendShipProp = heroFriendShipProp;
	}
	public Map<Integer, Double> getHeroAdvanceProp() {
		if(heroAdvanceProp == null)
			heroAdvanceProp = new HashMap<>();
		return heroAdvanceProp;
	}
	public void setHeroAdvanceProp(Map<Integer, Double> heroAdvanceProp) {
		this.heroAdvanceProp = heroAdvanceProp;
	}
	public Map<String, Long> getEquipMap() {
		if(equipMap == null){
			equipMap = new HashMap<String, Long>();
		}
		return equipMap;
	}
	public void setEquipMap(Map<String, Long> equipMap) {
		this.equipMap = equipMap;
	}
	public long getHeroID() {
		return heroID;
	}
	public void setHeroID(long heroID) {
		this.heroID = heroID;
	}
	public int getExp() {
		return exp;
	}
	public void setExp(int exp) {
		this.exp = exp;
	}
	public int getClassLevel() {
		return classLevel;
	}
	public void setClassLevel(int classLevel) {
		this.classLevel = classLevel;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getTemplateID() {
		return templateID;
	}
	public void setTemplateID(int templateID) {
		this.templateID = templateID;
	}
	public Map<Integer, Integer> getEquipmentSetProp() {
		if(equipmentSetProp == null){
			equipmentSetProp = new HashMap<>();
		}
		return equipmentSetProp;
	}
	public void setEquipmentSetProp(Map<Integer, Integer> equipmentSetProp) {
		this.equipmentSetProp = equipmentSetProp;
	}
	@Override
	public Map<String, Object> responseMap(int t) {
		return this.responseMap();
	}
	
}
