package com.mi.game.module.worldBoss.pojo;

import java.util.List;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.utils.IndexDirection;
import com.mi.core.pojo.BaseEntity;
import com.mi.game.module.worldBoss.protocol.TopTenInfo;
@Entity(noClassnameStored = true)
public class TopTenListEntity extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1665011460113577679L;
	@Indexed(value=IndexDirection.ASC, unique=true)
	private String playerID;
	private List<TopTenInfo> topTenList;
	
	public List<TopTenInfo> getTopTenList() {
		return topTenList;
	}

	public void setTopTenList(List<TopTenInfo> topTenList) {
		this.topTenList = topTenList;
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
