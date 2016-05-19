package com.mi.game.module.dungeon.pojo;

import java.util.HashMap;
import java.util.Map;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.utils.IndexDirection;
import com.mi.core.pojo.BaseEntity;
import com.mi.game.util.CommonMethod;

/**
 * @author 刘凯旋 副本实体 2014年6月3日 上午11:35:05
 */
@Entity(noClassnameStored = true)
public class DungeonMapEntity extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1327021311611183043L;
	@Indexed(value = IndexDirection.ASC, unique = true)
	private String playerID;
	private Map<String, Dungeon> dungeonMap; // 副本集合
	private long lastUpdatTime;
	private int starNum;
	private long lastContinuousTime;
	private long lastAddStarlNumTime;
	private int continuousPayNum;
	private long dungeonNum;

	public long getDungeonNum() {
		return dungeonNum;
	}

	public void setDungeonNum(long dungeonNum) {
		this.dungeonNum = dungeonNum;
	}

	public int addContinuousPayNum() {
		continuousPayNum++;
		return continuousPayNum;
	}

	public int getContinuousPayNum() {
		return continuousPayNum;
	}

	public void setContinuousPayNum(int continuousPayNum) {
		this.continuousPayNum = continuousPayNum;
	}

	public long getLastAddStarlNumTime() {
		return lastAddStarlNumTime;
	}

	public void setLastAddStarlNumTime(long lastAddStarlNumTime) {
		this.lastAddStarlNumTime = lastAddStarlNumTime;
	}

	public void addStarNum() {
		this.starNum += 1;
		this.lastAddStarlNumTime = System.currentTimeMillis();
	}

	public int getStarNum() {
		return starNum;
	}

	public void setStarNum(int starNum) {
		this.starNum = starNum;
	}

	public long getLastUpdatTime() {
		return lastUpdatTime;
	}

	public void setLastUpdatTime(long lastUpdatTime) {
		this.lastUpdatTime = lastUpdatTime;
	}

	public Map<String, Dungeon> getDungeonMap() {
		if (dungeonMap == null) {
			dungeonMap = new HashMap<>();
		}
		return dungeonMap;
	}

	public void setDungeonMap(Map<String, Dungeon> dungeonMap) {
		this.dungeonMap = dungeonMap;
	}

	public long getLastContinuousTime() {
		return lastContinuousTime;
	}

	public void setLastContinuousTime(long lastContinuousTime) {
		this.lastContinuousTime = lastContinuousTime;
	}

	@Override
	public Map<String, Object> responseMap() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("dungeonMap", CommonMethod.getResponseMap(dungeonMap));
		data.put("starNum", starNum);
		data.put("lastContinuousTime", lastContinuousTime);
		data.put("continuousPayNum", continuousPayNum);
		return data;
	}

	@Override
	public Map<String, Object> responseMap(int t) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("playerID", playerID);
		data.put("dungeonMapSize", CommonMethod.getResponseMap(dungeonMap).size());
		data.put("starNum", starNum);
		return data;
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
