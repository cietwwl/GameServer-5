package com.mi.game.module.talisman.pojo;

import java.util.List;
import java.util.Map;

import com.mi.core.pojo.BaseEntity;

public class TalismanShardPlunderEntity extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5192982305639918856L;
	private String shardID;
	private Map<Integer,List<String>> plunderList;
	
	public Map<Integer, List<String>> getPlunderList() {
		return plunderList;
	}

	public void setPlunderList(Map<Integer, List<String>> plunderList) {
		this.plunderList = plunderList;
	}

	@Override
	public Object getKey() {
		// TODO 自动生成的方法存根
		return shardID;
	}

	@Override
	public String getKeyName() {
		// TODO 自动生成的方法存根
		return "shardID";
	}

	@Override
	public void setKey(Object key) {
		// TODO 自动生成的方法存根
		shardID = key.toString();
	}
	
	
}
