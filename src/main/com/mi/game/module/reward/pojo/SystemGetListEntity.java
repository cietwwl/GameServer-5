package com.mi.game.module.reward.pojo;

import java.util.ArrayList;
import java.util.List;

import com.mi.core.pojo.BaseEntity;

public class SystemGetListEntity extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = -232675784143144138L;
	private String playerID;
	private List<String> getList;

	public List<String> getGetList() {
		if(getList == null){
			getList = new ArrayList<>();
		}
		return getList;
	}

	public void setGetList(List<String> getList) {
		this.getList = getList;
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
