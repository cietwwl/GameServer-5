package com.mi.game.module.pet.pojo;

import java.util.HashMap;
import java.util.Map;

import com.mi.core.pojo.BaseEntity;

public class PetEntity extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1457060602128388468L;
	private String playerID;                                                // 玩家ID
	private Map<Long,Pet> petMap;                              // 宠物集合
	private Map<Integer,PetShard> shardMap;        // 碎片集合
	//private int maxSellNum;                                             // 最大背包个数
	private int maxFieldNum = 6;                                  // 宠物最大栏位
	
	 	

	@Override
	public Map<String,Object> responseMap(){
		Map<String,Object> data = new HashMap<String, Object>();
		if(petMap!=null){
			data.put("petMap", petMap.values());
		}	
		data.put("shardMap", shardMap.values());
		//data.put("maxSellNum", maxSellNum);
		data.put("maxFieldNum", maxFieldNum);
		return data;
	}
	
//	public int getMaxSellNum() {
//		return maxSellNum;
//	}
//
//	public void setMaxSellNum(int maxSellNum) {
//		this.maxSellNum = maxSellNum;
//	}

	public Map<Long, Pet> getPetMap() {
		if(petMap == null)
			petMap = new HashMap<Long,Pet>();
		return petMap;
	}

	public void setPetMap(Map<Long, Pet> petMap) {
		this.petMap = petMap;
	}

	public Map<Integer, PetShard> getShardMap() {
		if(shardMap == null){
			shardMap = new HashMap<Integer, PetShard>();
		}
		return shardMap;
	}

	public void setShardMap(Map<Integer, PetShard> shardMap) {
		this.shardMap = shardMap;
	}

	public int getMaxFieldNum() {
		return maxFieldNum;
	}

	public void setMaxFieldNum(int maxFieldNum) {
		this.maxFieldNum = maxFieldNum;
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
