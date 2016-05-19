package com.mi.game.module.event.pojo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mi.core.pojo.BaseEntity;

public class WarGodRankEntity extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4420541320660005656L;
	private String warGodId;
	private List<WarGodRankInfo> rankList;
	
	
	public List<WarGodRankInfo> getRankList() {
		return rankList;
	}

	public void setRankList(List<WarGodRankInfo> rankList) {
		this.rankList = rankList;
	}
	
	@Override
	public Map<String,Object> responseMap(){
		Map<String,Object> data = new HashMap<String, Object>();
		data.put("rankList", rankList);
		return data;
	}
	@Override
	public Object getKey() {
		// TODO 自动生成的方法存根
		return warGodId;
	}

	@Override
	public String getKeyName() {
		// TODO 自动生成的方法存根
		return "warGodId";
	}

	@Override
	public void setKey(Object key) {
		// TODO 自动生成的方法存根
		warGodId = key.toString();
	}
	
}
