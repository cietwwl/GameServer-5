package com.mi.game.module.talisman.pojo;

import java.util.HashMap;
import java.util.Map;

public class TalismanEntity {
	/**装备唯一ID*/
	private long talismanID;   
	/**强化等级*/
	private int strengLevel;
	/**模板ID*/
	private int templateID;
	/**精炼等级*/
	private int refineLevel;
	/**英雄ID*/
	private long heroID;
	/**装备的属性*/
	private Map<String,Double> prototype;
	/**强化经验*/
	private int exp;
	private Map<String,Double> refinePrototype;
	

	
	public Map<String, Double> getRefinePrototype() {
		if(refinePrototype == null)
			refinePrototype = new HashMap<String, Double>();
		return refinePrototype;
	}
	public void setRefinePrototype(Map<String, Double> refinePrototype) {
		this.refinePrototype = refinePrototype;
	}
	public int getExp() {
		return exp;
	}
	public void setExp(int exp) {
		this.exp = exp;
	}
	public int getTemplateID() {
		return templateID;
	}
	public void setTemplateID(int templateID) {
		this.templateID = templateID;
	}
	public int getRefineLevel() {
		return refineLevel;
	}
	public void setRefineLevel(int refineLevel) {
		this.refineLevel = refineLevel;
	}
	public long getHeroID() {
		return heroID;
	}
	public void setHeroID(long heroID) {
		this.heroID = heroID;
	}
	public Map<String, Double> getPrototype() {
		if(prototype == null ){
			prototype = new HashMap<>();
		}
		return prototype;
	}
	public void setPrototype(Map<String, Double> prototype) {
		this.prototype = prototype;
	}
	public long getTalismanID() {
		return talismanID;
	}
	public void setTalismanID(long talismanID) {
		this.talismanID = talismanID;
	}
	public int getStrengLevel() {
		return strengLevel;
	}
	public void setStrengLevel(int strengLevel) {
		this.strengLevel = strengLevel;
	}
	
}
