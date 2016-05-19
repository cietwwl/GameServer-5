package com.mi.game.module.equipment.data;

import com.mi.core.engine.annotation.XmlTemplate;
import com.mi.core.template.BaseTemplate;
@XmlTemplate(template= {"com/mi/template/EquipmentComposePrototype.xml"})
public class EquipmentShardData extends BaseTemplate{
	private int composeNum;
	private int targetID;
	private int price;
	private int canSell;
	
	public int getPrice() {
		return price;
	}
	public void setPrice(String price) {
		if(price != null){
			String[] strArr= price.split("=");
			this.price = Integer.parseInt(strArr[1]);
		}
	}
	public int getCanSell() {
		return canSell;
	}
	public void setCanSell(int canSell) {
		this.canSell = canSell;
	}
	public int getComposeNum() {
		return composeNum;
	}
	public void setComposeNum(int composeNum) {
		this.composeNum = composeNum;
	}
	public int getTargetID() {
		return targetID;
	}
	public void setTargetID(int targetID) {
		this.targetID = targetID;
	}
	
}
