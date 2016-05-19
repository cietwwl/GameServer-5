package com.mi.game.module.event.pojo;

import java.util.HashMap;
import java.util.Map;

import com.mi.core.pojo.BaseEntity;

public class EventConfigEntity extends BaseEntity {

	private static final long serialVersionUID = 1L;

	// 活动key
	private String eventID;
	// 位置
	private int index;
	// 活动pid
	private int pid;
	// 活动内容pid
	private int infoPid;
	// 是否开启
	private int status;
	// 描述
	private String desc;
	// 活动版本
	private int version;
	// 开始时间
	private String startTime;
	// 结束时间
	private String endTime;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public int getInfoPid() {
		return infoPid;
	}

	public void setInfoPid(int infoPid) {
		this.infoPid = infoPid;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getEventID() {
		return eventID;
	}

	public void setEventID(String eventID) {
		this.eventID = eventID;
	}

	@Override
	public Map<String, Object> responseMap(int t) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("eventID", eventID);
		result.put("index", index);
		result.put("pid", pid);
		result.put("infoPid", infoPid);
		result.put("status", status);
		result.put("desc", desc);
		result.put("version", version);
		result.put("startTime", startTime);
		result.put("endTime", endTime);
		return result;
	}

	@Override
	public Map<String, Object> responseMap() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("eventID", eventID);
		result.put("order", index);
		result.put("pid", pid);
		result.put("infoPid", infoPid);
		result.put("status", status);
		result.put("desc", desc);
		result.put("version", version);
		result.put("startTime", startTime);
		result.put("endTime", endTime);
		return result;
	}

	@Override
	public Object getKey() {
		return eventID;
	}

	@Override
	public String getKeyName() {
		return "eventID";
	}

	@Override
	public void setKey(Object key) {
		this.eventID = key.toString();
	}

}
