package com.mi.game.module.dungeon.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.utils.IndexDirection;
import com.mi.core.pojo.BaseEntity;
@Entity(noClassnameStored = true)
public class DungeonEliteEntity extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8930639984586356739L;
	@Indexed(value = IndexDirection.ASC, unique = true)
	private String playerID;                         
	private long lastUpdateTime;               //最后更新时间
	private int attackNum;                            //攻打次数
	private int maxPayNum;                        //最大付费次数
	private int payNum;                                //付费次数
	private List<Integer> eliteList;          //通过的精英关列表
	private int maxEliteID;
	public Map<String,Object> responseMap(){
		Map<String,Object> data = new HashMap<String, Object>();
		data.put("attackNum", attackNum);
		data.put("maxPayNum", maxPayNum);
		data.put("payNum", payNum);
		data.put("eliteList",eliteList);
		data.put("maxEliteID",maxEliteID);
		return data;
	}
	
	public int getMaxEliteID() {
		return maxEliteID;
	}

	public void setMaxEliteID(int maxEliteID) {
		this.maxEliteID = maxEliteID;
	}

	public List<Integer> getEliteList() {
		if(eliteList == null){
			eliteList = new ArrayList<>();
		}
		return eliteList;
	}

	public void setEliteList(List<Integer> eliteList) {
		this.eliteList = eliteList;
	}

	public long getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public int getAttackNum() {
		return attackNum;
	}

	public void setAttackNum(int attackNum) {
		this.attackNum = attackNum;
	}

	public int getMaxPayNum() {
		return maxPayNum;
	}

	public void setMaxPayNum(int maxPayNum) {
		this.maxPayNum = maxPayNum;
	}


	public int getPayNum() {
		return payNum;
	}

	public void setPayNum(int payNum) {
		this.payNum = payNum;
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
