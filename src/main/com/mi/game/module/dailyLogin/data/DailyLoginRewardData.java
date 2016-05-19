package com.mi.game.module.dailyLogin.data;

import com.mi.core.engine.annotation.XmlTemplate;
import com.mi.core.template.BaseTemplate;

@XmlTemplate(template = { "com/mi/template/ActiveLoginRewardPrototype.xml" })
public class DailyLoginRewardData extends BaseTemplate {
	private int pid;

	private String itemID;

	private String chargeItemID;

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public String getItemID() {
		return itemID;
	}

	public void setItemID(String itemID) {
		this.itemID = itemID;
	}

	public String getChargeItemID() {
		return chargeItemID;
	}

	public void setChargeItemID(String chargeItemID) {
		this.chargeItemID = chargeItemID;
	}

}
