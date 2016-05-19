package com.mi.game.module.event.pojo;

import java.util.HashMap;
import java.util.Map;

import com.mi.core.pojo.BaseEntity;

/**
 * 竞技排行活动信息
 * 
 * @author 赵鹏翔
 * @time Apr 13, 2015 3:16:19 PM
 */
public class SportsRankEntity extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8440289582010930387L;
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
