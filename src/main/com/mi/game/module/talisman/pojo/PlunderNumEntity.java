package com.mi.game.module.talisman.pojo;

import com.mi.core.pojo.BaseEntity;

public class PlunderNumEntity extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6391137439797703984L;
	private String playerID;
	private int num;
	
	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	@Override
	public Object getKey() {
		// TODO 自动生成的方法存根
		return playerID;
	}

	@Override
	public String getKeyName() {
		// TODO 自动生成的方法存根
		return "playerID";
	}

	@Override
	public void setKey(Object key) {
		// TODO 自动生成的方法存根
		playerID = key.toString();
	}

}
