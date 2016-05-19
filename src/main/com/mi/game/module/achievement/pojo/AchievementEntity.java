package com.mi.game.module.achievement.pojo;

import java.util.HashMap;
import java.util.Map;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.utils.IndexDirection;
import com.mi.core.pojo.BaseEntity;
@Entity(noClassnameStored = true)
public class AchievementEntity extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -747773983753853023L;
	@Indexed(unique=true,value=IndexDirection.ASC)
	private String playerID;
	private Map<Integer,AchievementInfo> achievementList;
	private int heroCount;
	private int dungeonCount;
	private int equipCount;
	private int leadCount;
	private long arenaRank;
	@Override
	public Map<String,Object> responseMap(){
		Map<String,Object> data = new HashMap<String, Object>();
		if(achievementList != null){
			data.put("achievementList", achievementList.values());
			data.put("heroCount", heroCount);
			data.put("dungeonCount", dungeonCount);
			data.put("leadCount", leadCount);
			data.put("equipCount",equipCount);
		}
		return data;
	}
	
	public long getArenaRank() {
		return arenaRank;
	}

	public void setArenaRank(long arenaRank) {
		this.arenaRank = arenaRank;
	}

	public Map<Integer, AchievementInfo> getAchievementList() {
		return achievementList;
	}

	public void setAchievementList(Map<Integer, AchievementInfo> achievementList) {
		this.achievementList = achievementList;
	}
	
	public int getHeroCount() {
		return heroCount;
	}

	public void setHeroCount(int heroCount) {
		this.heroCount = heroCount;
	}

	public int getDungeonCount() {
		return dungeonCount;
	}

	public void setDungeonCount(int dungeonCount) {
		this.dungeonCount = dungeonCount;
	}

	public int getEquipCount() {
		return equipCount;
	}

	public void setEquipCount(int equipCount) {
		this.equipCount = equipCount;
	}

	public int getLeadCount() {
		return leadCount;
	}

	public void setLeadCount(int leadCount) {
		this.leadCount = leadCount;
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
