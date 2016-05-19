package com.mi.game.module.login.data;

import com.mi.core.engine.annotation.XmlTemplate;
import com.mi.core.template.BaseTemplate;
@XmlTemplate(template = {"com/mi/template/RandomNamePrototype.xml"})
public class RandomNamePrototype extends BaseTemplate{
	private String surname ;
	private String manName;
	private String womanName;
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getManName() {
		return manName;
	}
	public void setManName(String manName) {
		this.manName = manName;
	}
	public String getWomanName() {
		return womanName;
	}
	public void setWomanName(String womanName) {
		this.womanName = womanName;
	}



	
	
}
