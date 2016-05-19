package com.mi.game.module.talisman.pojo;

import java.util.HashMap;
import java.util.Map;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Index;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.annotations.Indexes;
import com.google.code.morphia.utils.IndexDirection;
import com.mi.core.pojo.BaseEntity;
@Entity(noClassnameStored = true)
@Indexes(@Index(name = "a_b", value = "playerID, shardID"))
public class TalismanShard extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5598956607739621000L;
	
	private String unique;
	
	@Indexed(value=IndexDirection.ASC)
	private String playerID;
	@Indexed()
	private int shardID;
	private int level;
	private int num;
	@Override
	public Map<String,Object> responseMap(){
		Map<String,Object> data = new HashMap<String, Object>();
		data.put("shardID", shardID);
		data.put("num", num);
		return data;
	}
	
	
	
	@Override
	public Map<String, Object> responseMap(int t) {
		Map<String,Object> data = new HashMap<String, Object>();
		switch (t) {
		case 11111:
			data.put("unique", unique);
			data.put("playerID", playerID);
			data.put("shardID", shardID);
			data.put("level", level);
			data.put("num", num);
			break;
		}
		return data;
	}



	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getPlayerID() {
		return playerID;
	}
	public void setPlayerID(String playerID) {
		this.playerID = playerID;
	}
	public int getShardID() {
		return shardID;
	}
	public void setShardID(int shardID) {
		this.shardID = shardID;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	
	@Override
	public Object getKey() {
		// TODO 自动生成的方法存根
		return unique;
	}
	@Override
	public String getKeyName() {
		// TODO 自动生成的方法存根
		return "unique";
	}
	@Override
	public void setKey(Object key) {
		// TODO 自动生成的方法存根
		unique = key.toString();
	}
	
	
	
}
