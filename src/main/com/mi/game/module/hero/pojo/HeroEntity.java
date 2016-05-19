package com.mi.game.module.hero.pojo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.utils.IndexDirection;
import com.mi.core.pojo.BaseEntity;
import com.mi.game.util.CommonMethod;

@Entity(noClassnameStored = true)
public class HeroEntity extends BaseEntity {
	private static final long serialVersionUID = 3156788438110424097L;
	@Indexed(value = IndexDirection.ASC, unique = true)
	private String playerID;
	private Map<String, Hero> heroMap = new HashMap<String, Hero>();;
	private Map<String, HeroShard> shardMap = new HashMap<String, HeroShard>();
	// 上阵英雄
	private List<Long> teamList;
	private int maxHeroNum;
	private long leadID;

	public long getLeadID() {
		return leadID;
	}

	public void setLeadID(long leadID) {
		this.leadID = leadID;
	}

	public int getMaxHeroNum() {
		return maxHeroNum;
	}

	public void setMaxHeroNum(int maxHeroNum) {
		this.maxHeroNum = maxHeroNum;
	}

	public List<Long> getTeamList() {
		return teamList;
	}

	public void setTeamList(List<Long> teamList) {
		this.teamList = teamList;
	}

	public Map<String, Hero> getHeroMap() {
		return heroMap;
	}

	public void setHeroMap(Map<String, Hero> heroMap) {
		this.heroMap = heroMap;
	}

	public Map<String, HeroShard> getShardMap() {
		return shardMap;
	}

	public void setShardMap(Map<String, HeroShard> shardMap) {
		this.shardMap = shardMap;
	}

	@Override
	public Map<String, Object> responseMap() {
		Map<String, Object> data = new HashMap<String, Object>();
		if (heroMap != null) {
			// Map<String,Map<String,Long>> equipList = new HashMap<>();
			//
			// for(Entry<String,Hero> entry : heroMap.entrySet() ){
			// Hero hero = entry.getValue();
			// String key = entry.getKey();
			// Map<String,Long> equipMap = hero.getEquipMap();
			// equipList.put(key, equipMap);
			// hero.setEquipMap(null);
			// }
			// data.put("equipList", equipList);
			data.put("heroList", CommonMethod.getResponseMap(heroMap));
		}
		if (shardMap != null) {
			data.put("shardMap", shardMap.values());
		}
		data.put("maxHeroNum", maxHeroNum);
		data.put("teamList", teamList);
		return data;
	}

	@Override
	public Map<String, Object> responseMap(int t) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("playerID", playerID);
		data.put("heroMapSize", heroMap.size());
		data.put("shardMapSize", shardMap.size());
		data.put("teamListSize", teamListSize());
		data.put("maxHeroNum", maxHeroNum);
		return data;
	}

	private int teamListSize() {
		int i = 0;
		for (Long index : teamList) {
			if (index != 0) {
				i++;
			}
		}
		return i;
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
		this.playerID = key.toString();
	}

}
