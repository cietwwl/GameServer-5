package com.mi.game.module.pet.data;

import com.mi.core.engine.annotation.XmlTemplate;
import com.mi.core.template.BaseTemplate;

@XmlTemplate(template = {"com/mi/template/PetTalentPrototype.xml"})
public class PetTalentData extends BaseTemplate {
	private int skillReq;
	private int addition;
	public int getSkillReq() {
		return skillReq;
	}
	public void setSkillReq(int skillReq) {
		this.skillReq = skillReq;
	}
	public int getAddition() {
		return addition;
	}
	public void setAddition(int addition) {
		this.addition = addition;
	}

}
