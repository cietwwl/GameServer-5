package com.mi.game.module.pet.data;

import java.util.HashMap;
import java.util.Map;

import com.mi.core.engine.annotation.XmlTemplate;
import com.mi.core.template.BaseTemplate;
@XmlTemplate(template = {"com/mi/template/PetSkillPrototype.xml"})
public class PetSkillData extends BaseTemplate{
	private int quality;
	private Map<Integer,Integer> property;//初始属性
	private Map<Integer,Integer> intensify;//每次升级需要增加的属性值
	public int getQuality() {
		return quality;
	}
	public void setQuality(int quality) {
		this.quality = quality;
	}
	public Map<Integer, Integer> getProperty() {
		return property;
	}
	public void setProperty(String property) {
		if(property != null){
			this.property = new HashMap<Integer, Integer>();
			String[] tempArr = property.split(",");
			for(String temp : tempArr){
				String[] costArr = temp.split("=");
				if(costArr != null){
					this.property.put(Integer.parseInt(costArr[0]),Integer.parseInt(costArr[1]));
				}
			}
		}
	}
	public Map<Integer, Integer> getIntensify() {
		return intensify;
	}
	public void setIntensify(String intensify) {
		if(intensify != null){
			this.intensify = new HashMap<Integer, Integer>();
			String[] tempArr = intensify.split(",");
			for(String temp : tempArr){
				String[] costArr = temp.split("=");
				if(costArr != null){
					this.intensify.put(Integer.parseInt(costArr[0]),Integer.parseInt(costArr[1]));
				}
			}
		}
	}
	
	
}
