package com.mi.game.module.worldBoss.protocol;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mi.core.protocol.BaseProtocol;
import com.mi.game.defines.HandlerIds;
import com.mi.game.module.reward.data.GoodsBean;
import com.mi.game.module.worldBoss.pojo.WorldBossDamageInfo;

public class WorldBossProtocol extends BaseProtocol{
	private List<String> topNameList; 
	private String killName;
	private long startTime;
	private long endTime;
	private long damage;
	private int attackNum;
	private int inspireNum;
	private int level;
	private long bossHp;
	private int state;           //0 非战斗 1 战斗
	private long lastAttackTime;
	private long lastInspireTime;
	private Map<String,Object> itemMap;
	private boolean success;
	private int reviveNum;
	private List<TopTenInfo> topTenList;
	private long rank;
	private boolean over;
	private List<WorldBossDamageInfo> damageList;
	private List<GoodsBean> goodsList;
	private int eventID;
	
	
	@Override
	public Map<String,Object> responseMap(int y){
		Map<String,Object> data = new HashMap<String, Object>();
		switch(y){
			case HandlerIds.getBossInfo:
				//if(bossHp != 0)
					data.put("bossHp",bossHp);
				//if(damage != 0)
					data.put("damage", damage);
				//if(inspireNum != 0)
					data.put("inspireNum", inspireNum);
				//if(endTime != 0)
					data.put("endTime", endTime);
				//if(startTime != 0)
					data.put("startTime", startTime);
				//if(killName != null)
					data.put("killName", killName);
				//if(topNameList != null)
					data.put("topNameList",topNameList);
				//if(attackNum != 0)
					data.put("attackNum", attackNum);
				//if(level != 0)
					data.put("level", level);
					data.put("state", state);
					data.put("reviveNum",reviveNum);
					data.put("rank", rank);
					data.put("lastInspireTime", lastInspireTime);
					data.put("lastAttackTime", lastAttackTime);
				break;
			case HandlerIds.inspire:
				data.put("itemMap", itemMap);
				data.put("inspireNum", inspireNum);
				data.put("succeess", success);
				data.put("rank", rank);
				data.put("over",over);
				data.put("lastInspireTime", lastInspireTime);
				if(killName != null)
					data.put("killName", killName);
				if(goodsList != null)
					data.put("goodsList",goodsList);
				data.put("eventID", eventID);
				break;
			case HandlerIds.challengeBoss:
				data.put("attackNum", attackNum);
				data.put("damage", damage);
				data.put("bossHp", bossHp);
				data.put("lastAttackTime", lastAttackTime);
				data.put("rank", rank);
				data.put("over",over);
				data.put("eventID", eventID);
				if(killName != null)
					data.put("killName", killName);
				if(goodsList != null)
					data.put("goodsList",goodsList);
				break;
			case HandlerIds.worldBossRevive:
				data.put("reviveNum",reviveNum);
				data.put("itemMap", itemMap);
				data.put("lastAttackTime", lastAttackTime);
				data.put("rank", rank);
				data.put("over",over);
				if(killName != null)
					data.put("killName", killName);
				if(goodsList != null)
					data.put("goodsList",goodsList);
				data.put("eventID", eventID);
				break;
			case HandlerIds.getWorldBossTopTen:
				data.put("topTenList", topTenList);
				break;
			case HandlerIds.getWorldBossShowList:
				data.put("damageList", damageList);
				data.put("bossHp", bossHp);
				data.put("rank", rank);
				data.put("over",over);
				if(killName != null)
					data.put("killName", killName);
				if(goodsList != null)
					data.put("goodsList",goodsList);
				data.put("eventID", eventID);
				break;
		}
		return data;
	}

	
	
	public int getEventID() {
		return eventID;
	}

	public void setEventID(int eventID) {
		this.eventID = eventID;
	}

	public List<GoodsBean> getGoodsList() {
		return goodsList;
	}

	public void setGoodsList(List<GoodsBean> goodsList) {
		this.goodsList = goodsList;
	}

	public List<WorldBossDamageInfo> getDamageList() {
		return damageList;
	}

	public void setDamageList(List<WorldBossDamageInfo> damageList) {
		this.damageList = damageList;
	}

	public long getRank() {
		return rank;
	}

	public void setRank(long rank) {
		this.rank = rank;
	}

	public boolean isOver() {
		return over;
	}

	public void setOver(boolean over) {
		this.over = over;
	}

	public List<TopTenInfo> getTopTenList() {
		return topTenList;
	}

	public void setTopTenList(List<TopTenInfo> topTenList) {
		this.topTenList = topTenList;
	}

	public int getReviveNum() {
		return reviveNum;
	}

	public void setReviveNum(int reviveNum) {
		this.reviveNum = reviveNum;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Map<String, Object> getItemMap() {
		return itemMap;
	}

	public void setItemMap(Map<String, Object> itemMap) {
		this.itemMap = itemMap;
	}

	public long getLastAttackTime() {
		return lastAttackTime;
	}

	public void setLastAttackTime(long lastAttackTime) {
		this.lastAttackTime = lastAttackTime;
	}


	public long getLastInspireTime() {
		return lastInspireTime;
	}



	public void setLastInspireTime(long lastInspireTime) {
		this.lastInspireTime = lastInspireTime;
	}



	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public long getBossHp() {
		return bossHp;
	}
	public void setBossHp(long bossHp) {
		this.bossHp = bossHp;
	}
	public long getDamage() {
		return damage;
	}
	public void setDamage(long damage) {
		this.damage = damage;
	}
	public int getAttackNum() {
		return attackNum;
	}
	public void setAttackNum(int attackNum) {
		this.attackNum = attackNum;
	}
	public int getInspireNum() {
		return inspireNum;
	}
	public void setInspireNum(int inspireNum) {
		this.inspireNum = inspireNum;
	}
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	public List<String> getTopNameList() {
		return topNameList;
	}
	public void setTopNameList(List<String> topNameList) {
		this.topNameList = topNameList;
	}
	public String getKillName() {
		return killName;
	}
	public void setKillName(String killName) {
		this.killName = killName;
	}
	
	
}
