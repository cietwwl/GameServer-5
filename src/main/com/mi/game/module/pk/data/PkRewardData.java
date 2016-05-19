package com.mi.game.module.pk.data;

import com.mi.core.engine.annotation.XmlTemplate;
import com.mi.core.template.BaseTemplate;

@XmlTemplate(template = { "com/mi/template/TournamentRewardPrototype.xml" })
public class PkRewardData extends BaseTemplate {
	private int pid;
	private String itemID;
	private String name;
	private int rangeLow;
	private int rangeHigh;

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public int getRangeLow() {
		return rangeLow;
	}

	public void setRangeLow(int rangeLow) {
		this.rangeLow = rangeLow;
	}

	public int getRangeHigh() {
		return rangeHigh;
	}

	public void setRangeHigh(int rangeHigh) {
		this.rangeHigh = rangeHigh;
	}

	public String getItemID() {
		return itemID;
	}

	public void setItemID(String itemID) {
		this.itemID = itemID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
