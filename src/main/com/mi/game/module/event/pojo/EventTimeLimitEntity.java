package com.mi.game.module.event.pojo;

import java.util.HashMap;
import java.util.Map;

import com.google.code.morphia.annotations.Indexed;
import com.mi.core.pojo.BaseEntity;

/**
 * 限时武将实体
 * 
 * @author Administrator
 *
 */
public class EventTimeLimitEntity extends BaseEntity {
	private static final long serialVersionUID = -7519140974452494189L;

	// 玩家id
	@Indexed
	private String playerID;
	// 开启神将的pid
	private int pid;
	// 下次免费抽取时间
	private long nextFreeTime;
	// 活动开始时间
	private long startTime;
	// 活动结束时间
	private long endTime;
	// 积分
	private int score;
	// 元宝购买次数
	private int buyNums;
	// 排名
	private int rank;
	// 付费抽奖赠送抽取次数
	private int giftNums;
	// 购买时间
	private long buyTime;

	public String getPlayerID() {
		return playerID;
	}

	public void setPlayerID(String playerID) {
		this.playerID = playerID;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public long getNextFreeTime() {
		return nextFreeTime;
	}

	public void setNextFreeTime(long nextFreeTime) {
		this.nextFreeTime = nextFreeTime;
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

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getBuyNums() {
		return buyNums;
	}

	public void setBuyNums(int buyNums) {
		this.buyNums = buyNums;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getGiftNums() {
		return giftNums;
	}

	public void setGiftNums(int giftNums) {
		this.giftNums = giftNums;
	}

	public long getBuyTime() {
		return buyTime;
	}

	public void setBuyTime(long buyTime) {
		this.buyTime = buyTime;
	}

	// 判断本次能否免费抽
	public boolean canGiftDraw() {
		if (buyNums % 15 == 0) {
			int num = buyNums / 15;
			if (num > giftNums) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Map<String, Object> responseMap() {
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("startTime", startTime);
		response.put("endTime", endTime);
		response.put("score", score);
		response.put("rank", rank);
		response.put("buyNums", buyNums);
		response.put("nextFreeTime", nextFreeTime);
		if (System.currentTimeMillis() >= nextFreeTime) { // 可以免费抽
			response.put("free", 1);
		} else {
			response.put("free", 0);
		}
		if (this.canGiftDraw()) { // 可以免费抽
			response.put("buyfree", 1);
		} else {
			response.put("buyfree", 0);
		}
		response.put("pid", pid);
		return response;
	}

	@Override
	public Object getKey() {
		return playerID;
	}

	@Override
	public String getKeyName() {
		return "playerID";
	}

	@Override
	public void setKey(Object key) {
		this.playerID = key.toString();
	}

}
