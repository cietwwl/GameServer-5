package com.mi.game.module.event.pojo;

import com.mi.core.pojo.BaseEntity;

/**
 * 多买多送活动,暂时没有
 * 
 * @author Administrator
 *
 */
public class EventBuyMoreEntity extends BaseEntity {

	private static final long serialVersionUID = -5911027371640727750L;
	private String playerID;

	@Override
	public Object getKey() {
		return playerID;
	}

	@Override
	public String getKeyName() {
		return playerID;
	}

	@Override
	public void setKey(Object key) {
		this.playerID = key.toString();
	}

}
