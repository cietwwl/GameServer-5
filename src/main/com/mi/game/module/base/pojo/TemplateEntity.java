package com.mi.game.module.base.pojo;

import com.mi.core.pojo.BaseEntity;

public class TemplateEntity extends BaseEntity{
	private String pid;
	/**
	 * 
	 */
	private static final long serialVersionUID = -3109077294122994214L;

	@Override
	public Object getKey() {
		// TODO 自动生成的方法存根
		return pid;
	}

	@Override
	public String getKeyName() {
		// TODO 自动生成的方法存根
		return "pid";
	}

	@Override
	public void setKey(Object key) {
		// TODO 自动生成的方法存根
		pid = key.toString();
	}

}
