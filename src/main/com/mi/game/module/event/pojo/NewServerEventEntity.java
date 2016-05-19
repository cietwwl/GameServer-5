package com.mi.game.module.event.pojo;

import java.util.HashSet;
import java.util.Set;

import com.mi.core.pojo.BaseEntity;

public class NewServerEventEntity extends BaseEntity {

	private static final long serialVersionUID = 7692720462588466696L;

	private String eid;

	private String pid;

	private Set<Integer> sendDay = new HashSet<Integer>();

	public String getEid() {
		return eid;
	}

	public void setEid(String eid) {
		this.eid = eid;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public Set<Integer> getSendDay() {
		return sendDay;
	}

	public void setSendDay(Set<Integer> sendDay) {
		this.sendDay = sendDay;
	}

	/**
	 * 检查day天奖励是否发放
	 * 
	 * @param day
	 * @return
	 */
	public boolean isSend(int day) {
		boolean result = sendDay.contains(day);
		if (!result) {
			sendDay.add(day);
		}
		return result;
	}

	@Override
	public Object getKey() {
		return eid;
	}

	@Override
	public String getKeyName() {
		return "eid";
	}

	@Override
	public void setKey(Object key) {
		eid = key.toString();
	}

}
