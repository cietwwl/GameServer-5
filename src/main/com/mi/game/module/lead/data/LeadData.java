package com.mi.game.module.lead.data;

import com.mi.core.engine.annotation.XmlTemplate;
import com.mi.game.module.hero.data.HeroData;
@XmlTemplate(template = {"com/mi/template/MajorHeroPrototype.xml"})
public class LeadData extends HeroData{
	private int nextID;

	public int getNextID() {
		return nextID;
	}

	public void setNextID(int nextID) {
		this.nextID = nextID;
	}
	
}
