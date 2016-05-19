package com.mi.game.module.achievement.protocol;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mi.core.protocol.BaseProtocol;
import com.mi.game.defines.HandlerIds;
import com.mi.game.module.achievement.pojo.AchievementEntity;
import com.mi.game.module.achievement.pojo.AchievementInfo;
import com.mi.game.module.reward.data.GoodsBean;

public class AchievementProtocol extends BaseProtocol{
	private AchievementEntity achievementEntity;
	private List<GoodsBean> showMap ;
	private Map<String,Object> itemMap;
	private AchievementInfo acInfo;
	private int heroCount;
	private int dungeonCount;
	private int equipCount;
	private int leadCount;
	@Override
	public Map<String,Object> responseMap(int y){
		Map<String,Object> data = new HashMap<String, Object>();
		switch(y){
			case HandlerIds.getAchievementInfo:
				data.put("achievementEntity", achievementEntity.responseMap());
				break;
			case HandlerIds.getAchievementReward:
				data.put("itemMap", itemMap);
				data.put("showMap",showMap);
				data.put("acInfo", acInfo);
				if(heroCount != 0){
					data.put("heroCount", heroCount);
				}
				if(dungeonCount != 0){
					data.put("dungeonCount", dungeonCount);
				}
				if(equipCount != 0){
					data.put("equipCount", equipCount);
				}
				if(leadCount != 0){
					data.put("leadCount", leadCount);
				}
				break;
		}
		return data;
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

	public AchievementInfo getAcInfo() {
		return acInfo;
	}
	public void setAcInfo(AchievementInfo acinfo) {
		this.acInfo = acinfo;
	}
	public List<GoodsBean> getShowMap() {
		return showMap;
	}

	public void setShowMap(List<GoodsBean> showMap) {
		this.showMap = showMap;
	}



	public Map<String, Object> getItemMap() {
		return itemMap;
	}



	public void setItemMap(Map<String, Object> itemMap) {
		this.itemMap = itemMap;
	}



	public AchievementEntity getAchievementEntity() {
		return achievementEntity;
	}

	public void setAchievementEntity(AchievementEntity achievementEntity) {
		this.achievementEntity = achievementEntity;
	}
	
}
