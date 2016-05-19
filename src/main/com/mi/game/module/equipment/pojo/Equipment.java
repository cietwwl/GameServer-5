package com.mi.game.module.equipment.pojo;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;


/**
 * @author 刘凯旋	
 *
 * 2014年6月17日 下午5:50:51
 */
public class Equipment {
	/**装备唯一ID*/
	private long equipID;          
	/**强化等级*/
	private int strengLevel;
	/**精炼属性*/
	private Map<String,Double> refine;
	/**是否装备*/
	private boolean equiped;
	/**模板ID*/
	private int templateID;
	/**装备的heroID*/
	private long heroID;
	/**装备的属性*/
	private Map<String,Double> prototype= new HashMap<String, Double>();
	/**精炼的显示属性*/
	private Map<String,Double> refineShow;
	/**强化的银币*/
	private int strengSilver;
	/**洗练的次数*/
	private int refineNum;
	/**精炼的最大属性*/
	private Map<String,Double> refineMaxProp;
	
	public Map<String, Object> responseMap() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("equipID", equipID);
		data.put("strengLevel", strengLevel);
		data.put("equiped", equiped);
		data.put("templateID", templateID);
		data.put("heroID", heroID);
		data.put("prototype", prototype);
		data.put("strengSilver", strengSilver);
		if(refine != null){
			Map<String,Integer> refineInt = new HashMap<>();
			for(Entry<String,Double> entry : refine.entrySet()){
				refineInt.put(entry.getKey(), (int)Math.floor(entry.getValue()));
			}
			data.put("refine", refineInt);
		}
		if(refineShow != null){
			Map<String,Integer> refineShowInt = new HashMap<>();
			for(Entry<String,Double> entry : refineShow.entrySet()){
				refineShowInt.put(entry.getKey(), (int)Math.floor(entry.getValue()));
			}
			data.put("refineShow", refineShowInt);
		}
		return data;
	}

	public Map<String, Double> getRefineMaxProp() {
		return refineMaxProp;
	}
	public void setRefineMaxProp(Map<String, Double> refineMaxProp) {
		this.refineMaxProp = refineMaxProp;
	}
	public int getRefineNum() {
		return refineNum;
	}
	public void setRefineNum(int refineNum) {
		this.refineNum = refineNum;
	}
	public int getStrengSilver() {
		return strengSilver;
	}
	public void setStrengSilver(int strengSilver) {
		this.strengSilver = strengSilver;
	}
	public long getHeroID() {
		return heroID;
	}
	public void setHeroID(long heroID) {
		this.heroID = heroID;
	}
	public Map<String, Double> getRefine() {
		if(refine == null)
			refine = new HashMap<String, Double>();
		return refine;
	}
	public void setRefine(Map<String, Double> refine) {
		this.refine = refine;
	}
	public Map<String, Double> getPrototype() {
		return prototype;
	}
	public void setPrototype(Map<String, Double> prototype) {
		this.prototype = prototype;
	}
	public boolean isEquiped() {
		return equiped;
	}
	public int getTemplateID() {
		return templateID;
	}
	public void setTemplateID(int templateID) {
		this.templateID = templateID;
	}
	public void setEquiped(boolean equiped) {
		this.equiped = equiped;
	}
	public long getEquipID() {
		return equipID;
	}
	public void setEquipID(long equipID) {
		this.equipID = equipID;
	}
	public int getStrengLevel() {
		return strengLevel;
	}
	public void setStrengLevel(int strengLevel) {
		this.strengLevel = strengLevel;
	}
	public Map<String, Double> getRefineShow() {
		return refineShow;
	}
	public void setRefineShow(Map<String, Double> refineShow) {
		this.refineShow = refineShow;
	}
	
	
}	
