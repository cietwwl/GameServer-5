package com.mi.game.module.farm.pojo;

import java.util.HashMap;
import java.util.Map;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.utils.IndexDirection;
import com.mi.core.pojo.BaseEntity;
import com.mi.game.module.reward.data.GoodsBean;

@Entity(noClassnameStored = true)
public class FarmEntity extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 515567750846890272L;
	@Indexed(value = IndexDirection.ASC, unique = true)
	private String playerID ;
	private long endTime;
	private long farmTime;
	private Map<Integer,GoodsBean> baseList;
	private Map<Integer,GoodsBean> splList;
	private int stageID;
	private long farmedTime;
	
	@Override
	public Map<String,Object> responseMap(){
		Map<String,Object> data = new HashMap<>();
		data.put("endTime", endTime);
		data.put("farmTime", farmTime);
		data.put("stageID", stageID);
		data.put("farmedTime",farmedTime);
		if(baseList != null)
			data.put("baseList", baseList.values());
		if(splList != null)
			data.put("splList", splList.values());
		data.put("farmedTime", farmedTime);
		return data;
	}
	
	
	
	
	public long getFarmedTime() {
		return farmedTime;
	}

	public void setFarmedTime(long farmedTime) {
		this.farmedTime = farmedTime;
	}

	public long getFarmTime() {
		return farmTime;
	}

	public void setFarmTime(long farmTime) {
		this.farmTime = farmTime;
	}

	public Map<Integer, GoodsBean> getBaseList() {
		if(baseList == null)
			baseList = new HashMap<>();
		return baseList;
	}

	public void setBaseList(Map<Integer, GoodsBean> baseList) {
		this.baseList = baseList;
	}

	public Map<Integer, GoodsBean> getSplList() {
		if(splList == null)
			splList = new HashMap<>();
		return splList;
	}

	public void setSplList(Map<Integer, GoodsBean> splList) {
		this.splList = splList;
	}

	public String getPlayerID() {
		return playerID;
	}

	public void setPlayerID(String playerID) {
		this.playerID = playerID;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public int getStageID() {
		return stageID;
	}

	public void setStageID(int stageID) {
		this.stageID = stageID;
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
