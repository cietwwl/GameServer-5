package com.mi.game.module.lead.data;

import java.util.HashMap;
import java.util.Map;

import com.mi.core.engine.annotation.XmlTemplate;
import com.mi.core.template.BaseTemplate;
@XmlTemplate(template={"com/mi/template/DestinyPrototype.xml"})
public class LeadDestinyData extends BaseTemplate{
	private Map<Integer,Double> addition;
	private int nextID;
	private int backID;

	public int getBackID() {
		return backID;
	}
	public void setBackID(int backID) {
		this.backID = backID;
	}
	public Map<Integer, Double> getAddition() {
		return addition;
	}
	public void setAddition(String addition) {
		if(addition != null && !addition.isEmpty()){
			this.addition = new HashMap<Integer, Double>();
			String[] arr = addition.split(",");
			for(String str : arr){
				String[] addInfo = str.split("=");
				this.addition.put(Integer.parseInt(addInfo[0]), Double.parseDouble(addInfo[1]));
			}
		}
		
	}
	public int getNextID() {
		return nextID;
	}
	public void setNextID(int nextID) {
		this.nextID = nextID;
	}
	
	
	
}
