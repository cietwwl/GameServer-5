package com.mi.game.module.pk.pojo;


import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.utils.IndexDirection;
import com.mi.core.pojo.BaseEntity;


/**
 * 比武兑换历史
 * 
 * @author 赵鹏翔
 * @time Mar 27, 2015 12:35:03 PM
 */
@Entity(noClassnameStored = true)
public class PkExchangeHistoryEntity extends BaseEntity {

	public String getHistoryId() {
		return historyId;
	}

	public void setHistoryId(String historyId) {
		this.historyId = historyId;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -8418762205022482269L;
	@Indexed(value = IndexDirection.ASC, unique = true)
	private String historyId;
	@Indexed(value = IndexDirection.ASC, unique = false)
	private String playerID; // 玩家ID
	@Indexed(value = IndexDirection.ASC, unique = false)
	private String day; // 兑换日期 格式:yyyyMMdd
	@Indexed(value = IndexDirection.ASC, unique = false)
	private String week; // 周(一年中的第几周)
	@Indexed(value = IndexDirection.ASC, unique = false)
	private String year; // 年
	@Indexed(value = IndexDirection.ASC, unique = false)
	private int goodId; // 物品模版ID
	@Indexed(value = IndexDirection.ASC, unique = false)
	private int num; // 兑换数量
	private int useReward; // 消耗积分
	private int limitType; // 兑换次数限制类型 1：week ，2：day，3：only(总共)
	private long updateTime; // 兑换时间
	public int getLimitType() {
		return limitType;
	}

	public void setLimitType(int limitType) {
		this.limitType = limitType;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public int getGoodId() {
		return goodId;
	}

	public void setGoodId(int goodId) {
		this.goodId = goodId;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getUseReward() {
		return useReward;
	}

	public void setUseReward(int useReward) {
		this.useReward = useReward;
	}

	@Override
	public Object getKey() {
		return historyId;
	}

	public String getPlayerID() {
		return playerID;
	}

	public void setPlayerID(String playerID) {
		this.playerID = playerID;
	}

	@Override
	public String getKeyName() {
		return "historyId";
	}

	@Override
	public void setKey(Object key) {
		// TODO Auto-generated method stub
		historyId = key.toString();
	}

}
