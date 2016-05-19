package com.mi.game.module.hero.data;

import java.util.HashMap;
import java.util.Map;

import com.mi.core.engine.annotation.XmlTemplate;
import com.mi.core.template.BaseTemplate;
@XmlTemplate(template = {"com/mi/template/TalentPrototype.xml"})
public class TalentData extends BaseTemplate{
	private Map<Integer,Double> addition;

	public Map<Integer, Double> getAddition() {
		return addition;
	}

	public void setAddition(String addition) {
		if(addition != null && !addition.isEmpty()){
			this.addition = new HashMap<Integer, Double>();
			String[] temp  = addition.split(",");
			for(String str :  temp){
				String[] list = str.split("=");
				this.addition.put(Integer.parseInt(list[0]), Double.parseDouble(list[1]));
			}
		}
	}
	
}
