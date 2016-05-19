package com.mi.game.module.event.pojo;

import java.util.HashMap;
import java.util.Map;

import com.google.code.morphia.annotations.Indexed;
import com.mi.core.pojo.BaseEntity;
import com.mi.game.util.Utilities;

/**
 * 积分抽奖实体
 * 
 * @author Administrator
 *
 */
public class EventDrawIntegralEntity extends BaseEntity {
	private static final long serialVersionUID = -6658307895801155319L;
	@Indexed
	private String playerID;
	// 积分总数
	private int integraTotal;
	// 抽奖次数
	private int drawNum;
	// 积分上限
	private int maxIntegral = 1000;
	// 开始时间
	private long startTime;
	// 结束时间
	private long endTime;
	// 时间
	private String dateTime;

	public String getPlayerID() {
		return playerID;
	}

	public void setPlayerID(String playerID) {
		this.playerID = playerID;
	}

	public int getIntegraTotal() {
		return integraTotal;
	}

	public void setIntegraTotal(int integraTotal) {
		this.integraTotal = integraTotal;
	}

	public int getDrawNum() {
		return drawNum;
	}

	public void setDrawNum(int drawNum) {
		this.drawNum = drawNum;
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

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public void addIntegraTotal(int num) {
		this.integraTotal += num;
		if (integraTotal > maxIntegral) {
			integraTotal = maxIntegral;
		}
	}

	/**
	 * 是否已刷新
	 * 
	 * @return
	 */
	public boolean isRefresh() {
		String nowTime = Utilities.getDateTime();
		if (dateTime == null || dateTime.isEmpty()) {
			dateTime = nowTime;
			return false;
		}
		if (!nowTime.equals(dateTime)) {
			dateTime = nowTime;
			return false;
		}
		return true;
	}

	@Override
	public Map<String, Object> responseMap() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("integralTotal", integraTotal);
		result.put("drawNum", drawNum);
		result.put("maxIntegral", maxIntegral);
		result.put("startTime", startTime);
		result.put("endTime", endTime);
		return result;
	}

	@Override
	public Object getKey() {
		return playerID;
	}

	@Override
	public String getKeyName() {
		return playerID;
	}

	@Override
	public void setKey(Object key) {
		this.playerID = key.toString();
	}

}
