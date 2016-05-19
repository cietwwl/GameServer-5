package com.mi.game.module.manual.data;

import java.util.ArrayList;
import java.util.List;

import com.mi.core.engine.annotation.XmlTemplate;
import com.mi.core.template.BaseTemplate;
@XmlTemplate(template = {"com/mi/template/LikeabilityPrototype.xml"})
public class ManualPrototypeData extends BaseTemplate{
	private  List<Integer> property;

	public List<Integer> getProperty() {
		return property;
	}

	public void setProperty(String property) {
		if(property != null && !property.isEmpty()){
			this.property = new ArrayList<>();
			String[] propertyList = property.split(",");
			for(String temp : propertyList){
				this.property.add(Integer.parseInt(temp));
			}
		}
	}


}


