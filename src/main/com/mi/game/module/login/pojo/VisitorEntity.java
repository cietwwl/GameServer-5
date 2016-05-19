package com.mi.game.module.login.pojo;

import com.mi.core.pojo.BaseEntity;

public class VisitorEntity extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4714413794172192157L;
	private String visitorID;
	private long visitorNum;
	
	
	public long getVisitorNum() {
		return visitorNum;
	}

	public void setVisitorNum(long visitorNum) {
		this.visitorNum = visitorNum;
	}

	@Override
	public Object getKey() {
		// TODO 自动生成的方法存根
		return visitorID;
	}

	@Override
	public String getKeyName() {
		// TODO 自动生成的方法存根
		return "visitorID";
	}

	@Override
	public void setKey(Object key) {
		// TODO 自动生成的方法存根
		visitorID = key.toString();
	}

}
