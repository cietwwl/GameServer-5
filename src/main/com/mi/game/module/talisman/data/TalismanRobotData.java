package com.mi.game.module.talisman.data;

import java.util.ArrayList;
import java.util.List;

import com.mi.core.engine.annotation.XmlTemplate;
import com.mi.core.template.BaseTemplate;
@XmlTemplate(template = {"com/mi/template/TalismanRobotPrototype.xml"})
public class TalismanRobotData extends BaseTemplate{
	private List<Integer> hero;
	private String name;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Integer> getHero() {
		return hero;
	}
	public void setHero(String heroList) {
		if(heroList != null && !heroList.isEmpty()){
			this.hero = new ArrayList<>();
			String[] strArr = heroList.split(",");
			for(String str : strArr){
				this.hero.add(Integer.parseInt(str));
			}
		}
	}
	
}
