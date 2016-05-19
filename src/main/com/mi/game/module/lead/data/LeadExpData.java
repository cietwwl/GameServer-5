package com.mi.game.module.lead.data;

import com.mi.core.engine.annotation.XmlTemplate;
import com.mi.core.template.BaseTemplate;

@XmlTemplate(template = "com/mi/template/MajorExpPrototype.xml")
public class LeadExpData extends BaseTemplate {
	private int level;
	private int exp;
	
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
	
	
}
