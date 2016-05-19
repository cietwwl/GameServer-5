package com.mi.game.module.pet.data;

import com.mi.core.engine.annotation.XmlTemplate;
import com.mi.core.template.BaseTemplate;
@XmlTemplate(template = {"com/mi/template/PetComposePrototype.xml"})
public class PetShardData extends BaseTemplate{
	private int composeNum;        //合成需要的数量
	private int targetID;          //合成的宠物ID
	
	public int getComposeNum() {
		return composeNum;
	}
	public void setComposeNum(int composeNum) {
		this.composeNum = composeNum;
	}
	public int getTargetID() {
		return targetID;
	}
	public void setTargetID(int targetID) {
		this.targetID = targetID;
	}
	
	
}
