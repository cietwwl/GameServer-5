package com.mi.game.module.arena.pojo;

import com.mi.core.pojo.BaseEntity;


/**
 * 记录竞技场状态
 * */

public class ArenaStatusEntity extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7114497760191668016L;
	private String arenaID;
	private int status;
	
	
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public Object getKey() {
		// TODO 自动生成的方法存根
		return arenaID;
	}

	@Override
	public String getKeyName() {
		// TODO 自动生成的方法存根
		return "arenaID";
	}

	@Override
	public void setKey(Object key) {
		// TODO 自动生成的方法存根
		arenaID = key.toString();
	}

}
