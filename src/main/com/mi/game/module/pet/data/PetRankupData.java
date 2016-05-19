package com.mi.game.module.pet.data;

import com.mi.core.engine.annotation.XmlTemplate;
import com.mi.core.template.BaseTemplate;

@XmlTemplate(template = {"com/mi/template/PetRankupPrototype.xml"})
public class PetRankupData extends BaseTemplate {
	private int level;
	private int exp;
	private int nextID;
	private int earnSkill;
	
	public int getEarnSkill() {
		return earnSkill;
	}
	public void setEarnSkill(int earnSkill) {
		this.earnSkill = earnSkill;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getExp() {
		return exp;
	}
	public void setExp(int exp) {
		this.exp = exp;
	}
	public int getNextID() {
		return nextID;
	}
	public void setNextID(int nextID) {
		this.nextID = nextID;
	}

}
