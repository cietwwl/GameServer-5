package com.mi.game.module.tower.data;

import java.util.HashMap;
import java.util.Map;

import com.mi.core.engine.annotation.XmlTemplate;
import com.mi.core.template.BaseTemplate;
@XmlTemplate(template = {"com/mi/template/HideTowerPrototype.xml"})
public class HideTowerData extends BaseTemplate{
	private Map<Integer,Integer> dropList;

	public Map<Integer, Integer> getDropList() {
		if(dropList == null){
			dropList = new HashMap<Integer, Integer>();
		}
		return dropList;
	}

	public void setDropList(String dropList) {
		if(dropList != null && !dropList.isEmpty() ){
			this.dropList =  new HashMap<Integer, Integer>();
			String[] dropArr = dropList.split(",");
			for(String str : dropArr){
				String[] dropInfo = str.split("=");
				this.dropList.put(Integer.parseInt(dropInfo[0]), Integer.parseInt(dropInfo[1]));
			}
		}
	}
	
	
	
}
