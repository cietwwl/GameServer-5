package com.mi.game.module.hero.data;

import com.mi.core.engine.annotation.XmlTemplate;
import com.mi.core.template.BaseTemplate;
@XmlTemplate(template = {"com/mi/template/HeroComposePrototype.xml"})
public class HeroShardData extends BaseTemplate{
	private int composeNum;
	private int targetID;
	
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
