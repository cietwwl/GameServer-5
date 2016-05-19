package com.mi.game.module.tower.pojo;

import com.mi.core.util.DateTimeUtil;

public class HideInfo {
	private long hideID;
	private long overTime;
	private int templateID;
	public HideInfo(){}
	
	public HideInfo(int templateID,long hideID){
		this.overTime = System.currentTimeMillis() + 2* DateTimeUtil.ONE_HOUR_TIME_MS;
		this.hideID = hideID;
		this.templateID = templateID;
	}

	public int getTemplateID() {
		return templateID;
	}

	public void setTemplateID(int templateID) {
		this.templateID = templateID;
	}

	public long getHideID() {
		return hideID;
	}

	public void setHideID(long hideID) {
		this.hideID = hideID;
	}

	public long getOverTime() {
		return overTime;
	}

	public void setOverTime(long overTime) {
		this.overTime = overTime;
	}

	
}
