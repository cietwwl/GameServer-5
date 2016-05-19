package com.mi.game.module.hero.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mi.core.engine.annotation.XmlTemplate;
import com.mi.core.template.BaseTemplate;

@XmlTemplate(template = {"com/mi/template/FriendShipPrototype.xml"})
public class FriendShipData extends BaseTemplate{
	private int activeType;
	private List<Integer> activeLimit;
	private Map<Integer,Double> property;
	
	public int getActiveType() {
		return activeType;
	}
	public void setActiveType(int activeType) {
		this.activeType = activeType;
	}
	public List<Integer> getActiveLimit() {
		if(activeLimit == null){
			activeLimit = new ArrayList<>();
		}
		return activeLimit;
	}
	public void setActiveLimit(String activeLimit) {
		if(activeLimit != null && !activeLimit.isEmpty()){
			this.activeLimit = new ArrayList<>();
			String[] list = activeLimit.split(",");
			for(String str : list){
				this.activeLimit.add(Integer.parseInt(str));
			}
		}
	}
	public Map<Integer, Double> getProperty() {
		if(property == null){
			property = new HashMap<Integer, Double>();
		}
		return property;
	}
	public void setProperty(String property) {
		if(property != null && !property.isEmpty()){
			this.property = new HashMap<>();
			String[] list = property.split(",");
			for(String str : list){
				String[] propertyList = str.split("=");
				this.property.put(Integer.parseInt(propertyList[0]), Double.parseDouble(propertyList[1]));
			}
		}
	}
	
}
