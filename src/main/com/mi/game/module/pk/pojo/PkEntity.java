package com.mi.game.module.pk.pojo;


import java.util.List;
import java.util.Map;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.utils.IndexDirection;
import com.mi.core.pojo.BaseEntity;


/**
 * 
 * @author 赵鹏翔
 *
 */
@Entity(noClassnameStored = true)
public class PkEntity extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7136055249297235673L;
	@Indexed(value = IndexDirection.ASC, unique = true)
	private String playerID; // 玩家ID
	@Indexed(value = IndexDirection.DESC, unique = false)
	private int points; // 当前积分
	@Indexed(value = IndexDirection.ASC, unique = false)
	private long updateTime; // 更新时间
	private List<String> lostTo; // 比武输给的玩家列表
	private Map<String, Pk> items; // 本周比武次数 key为当前为第几周

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public Object getKey() {
		return playerID;
	}

	public String getPlayerID() {
		return playerID;
	}

	public void setPlayerID(String playerID) {
		this.playerID = playerID;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public List<String> getLostTo() {
		return lostTo;
	}

	public void setLostTo(List<String> lostTo) {
		this.lostTo = lostTo;
	}

	public Map<String, Pk> getItems() {
		return items;
	}

	public void setItems(Map<String, Pk> items) {
		this.items = items;
	}

	@Override
	public String getKeyName() {
		return "playerID";
	}

	@Override
	public void setKey(Object key) {
		playerID = key.toString();
	}

}
