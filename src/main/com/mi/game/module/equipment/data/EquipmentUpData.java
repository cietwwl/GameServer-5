package com.mi.game.module.equipment.data;

import java.util.HashMap;
import java.util.Map;

import com.mi.core.engine.annotation.XmlTemplate;
import com.mi.core.template.BaseTemplate;
@XmlTemplate(template = {"com/mi/template/EquipLevelUpPrototype.xml"})
public class EquipmentUpData extends BaseTemplate{
	private int level ;
	private Map<Integer,Integer> cost;
	
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public Map<Integer, Integer> getCost() {
		return cost;
	}
	public void setCost(String cost) {
		if(cost == null || !cost.isEmpty()){
			this.cost = new HashMap<>();
			String[] strArr = cost.split(",");
			for(String temp : strArr){
					String[] tempArr = temp.split("=");
					this.cost.put(Integer.parseInt(tempArr[0]),Integer.parseInt(tempArr[1]));
			}
		}
		
	}
	
	
}
