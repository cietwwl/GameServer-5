package com.mi.game.module.pet.data;

import java.util.HashMap;
import java.util.Map;
import com.mi.core.engine.annotation.XmlTemplate;
import com.mi.core.template.BaseTemplate;

@XmlTemplate(template = {"com/mi/template/PetLockSkillPrototype.xml"})
public class PetLockSkillData extends BaseTemplate{
	private Map<Integer,Integer> price;	
	private int locks;
	public Map<Integer, Integer> getPrice() {
		return price;
	}
	public void setPrice(String price) {
		if(price != null){
			this.price = new HashMap<Integer, Integer>();
			String[] tempArr = price.split(",");
			for(String temp : tempArr){
				String[] costArr = temp.split("=");
				if(costArr != null){
					this.price.put(Integer.parseInt(costArr[0]),Integer.parseInt(costArr[1]));
				}
			}
		}
	}
	public int getLocks() {
		return locks;
	}
	public void setLocks(int locks) {
		this.locks = locks;
	}				
	
}
