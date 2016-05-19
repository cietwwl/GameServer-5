package com.mi.game.module.equipment.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mi.core.engine.annotation.XmlTemplate;
import com.mi.core.template.BaseTemplate;

@XmlTemplate(template = {"com/mi/template/SetPrototype.xml"})
public class EquipmentSetData extends BaseTemplate{
	private List<Integer> equipID;
	private Map<Integer,Integer> twoPart;
	private Map<Integer,Integer> threePart;
	private Map<Integer,Integer> fourPart;
	public List<Integer> getEquipID() {
		return equipID;
	}
	public void setEquipID(String equipID) {
		if(equipID != null && !equipID.isEmpty()){
			this.equipID = new ArrayList<>();
			String[] strList = equipID.split(",");
			for(String str : strList){
				int temp = Integer.parseInt(str);
				this.equipID.add(temp);
			}
		}
	}
	public Map<Integer, Integer> getTwoPart() {
		return twoPart;
	}
	public void setTwoPart(String twoPart) {
		if(twoPart != null && !twoPart.isEmpty()){
			this.twoPart = new HashMap<Integer, Integer>();
			String[] strList = twoPart.split(",");
			for(String str : strList){
				String[] temp = str.split("=");
				this.twoPart.put(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]));
			}
		}
	}
	public Map<Integer, Integer> getThreePart() {
		return threePart;
	}
	public void setThreePart(String threePart) {
		if(threePart != null && !threePart.isEmpty()){
			this.threePart = new HashMap<Integer, Integer>();
			String[] strList = threePart.split(",");
			for(String str : strList){
				String[] temp = str.split("=");
				this.threePart.put(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]));
			}
		}
	}
	public Map<Integer, Integer> getFourPart() {
		return fourPart;
	}
	public void setFourPart(String fourPart) {
		if(fourPart != null && !fourPart.isEmpty()){
			this.fourPart = new HashMap<Integer, Integer>();
			String[] strList = fourPart.split(",");
			for(String str : strList){
				String[] temp = str.split("=");
				this.fourPart.put(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]));
			}
		}
	}
	
	
	
}
