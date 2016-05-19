package com.mi.game.module.arena.pojo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.utils.IndexDirection;
import com.mi.core.pojo.BaseEntity;
@Entity(noClassnameStored = true)
public class LuckRankMapEntity extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5739479470463795337L;
	@Indexed(unique=true,value=IndexDirection.ASC)
	private String playerID;
	private List<LuckyInfo> lastRankList;
	private Map<Long,LuckyInfo> nextRankList;
	
	@Override
	public Map<String,Object> responseMap(){
		Map<String,Object> data = new HashMap<String, Object>();
		data.put("lastRankList", lastRankList);
		data.put("nextRankList", nextRankList.values());
		return data;
	}
	
	public Map<Long, LuckyInfo> getNextRankList() {
		return nextRankList;
	}

	public void setNextRankList(Map<Long, LuckyInfo> nextRankList) {
		this.nextRankList = nextRankList;
	}

	public List<LuckyInfo> getLastRankList() {
		return lastRankList;
	}

	public void setLastRankList(List<LuckyInfo> lastRankList) {
		this.lastRankList = lastRankList;
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
