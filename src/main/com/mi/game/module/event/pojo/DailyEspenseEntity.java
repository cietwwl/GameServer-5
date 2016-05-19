package com.mi.game.module.event.pojo;

import java.util.HashMap;
import java.util.Map;

import com.mi.core.pojo.BaseEntity;

/**
 * 每日消费活动信息
 * 
 * @author 赵鹏翔
 * @time Apr 13, 2015 3:11:59 PM
 */
public class DailyEspenseEntity extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1618536117801097904L;
	private String desc;
	private long startTime;
	private long endTime;

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
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

	@Override
	public Map<String, Object> responseMap() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("startTime", startTime);
		result.put("endTime", endTime);
		result.put("desc", desc);
		return result;
	}

	@Override
	public Object getKey() {
		return null;
	}

	@Override
	public String getKeyName() {
		return null;
	}

	@Override
	public void setKey(Object key) {
		
	}

}
