package com.mi.game.module.arena.pojo;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.utils.IndexDirection;
import com.mi.core.pojo.BaseEntity;
@Entity(noClassnameStored = true)
public class ArenaEntity extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3303979124280471268L;
	@Indexed(value = IndexDirection.ASC, unique = true)
	private String playerID;
	@Indexed(value = IndexDirection.ASC, unique = false)
	private long rank;
	private int level;
	
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public long getRank() {
		return rank;
	}
	public void setRank(long rank) {
		this.rank = rank;
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
