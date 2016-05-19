package com.mi.game.module.astral.pojo;

import java.util.List;
import java.util.Map;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.utils.IndexDirection;
import com.mi.core.pojo.BaseEntity;
@Entity(noClassnameStored = true)
public class AstralEntity extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5914486248968480473L;
	@Indexed(unique=true,value=IndexDirection.ASC)
	private String playerID;
	private int maxNum;          // 最大次数
	private int nowNum;         // 当前次数
	private int freeNum;         //  免费次数
    private List<AstralNeedInfo> needList;  //需要的列表	
	private List<Integer> drawList; //随机的列表
	private long crateTime;   //创建时间
	private Map<Integer, AstralReward> rewardList; //奖励列表
	private int point ;          //获得的点数
	private int addPoint;              //当前点数加成
	private int pointTimes;  //加成次数
	private int refreshRewardNum; //刷新次数
	private int maxRefreshRewardNum; //最大刷新次数
	
	public int getMaxRefreshRewardNum() {
		return maxRefreshRewardNum;
	}

	public void setMaxRefreshRewardNum(int maxRefreshRewardNum) {
		this.maxRefreshRewardNum = maxRefreshRewardNum;
	}

	public int getRefreshRewardNum() {
		return refreshRewardNum;
	}

	public void setRefreshRewardNum(int refreshRewardNum) {
		this.refreshRewardNum = refreshRewardNum;
	}

	public int getPointTimes() {
		return pointTimes;
	}

	public void setPointTimes(int pointTimes) {
		this.pointTimes = pointTimes;
	}

	public int getAddPoint() {
		return addPoint;
	}

	public void setAddPoint(int addPoint) {
		this.addPoint = addPoint;
	}

	public int getMaxNum() {
		return maxNum;
	}

	public void setMaxNum(int maxNum) {
		this.maxNum = maxNum;
	}

	public int getNowNum() {
		return nowNum;
	}

	public void setNowNum(int nowNum) {
		this.nowNum = nowNum;
	}

	public int getFreeNum() {
		return freeNum;
	}

	public void setFreeNum(int freeNum) {
		this.freeNum = freeNum;
	}

	public List<AstralNeedInfo> getNeedList() {
		return needList;
	}

	public void setNeedList(List<AstralNeedInfo> needList) {
		this.needList = needList;
	}

	public List<Integer> getDrawList() {
		return drawList;
	}

	public void setDrawList(List<Integer> drawList) {
		this.drawList = drawList;
	}

	public long getCrateTime() {
		return crateTime;
	}

	public void setCrateTime(long crateTime) {
		this.crateTime = crateTime;
	}

	public Map<Integer, AstralReward> getRewardList() {
		return rewardList;
	}

	public void setRewardList(Map<Integer, AstralReward> rewardList) {
		this.rewardList = rewardList;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
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
