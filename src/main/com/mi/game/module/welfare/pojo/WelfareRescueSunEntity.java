package com.mi.game.module.welfare.pojo;

import java.util.HashMap;
import java.util.Map;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.utils.IndexDirection;
import com.mi.core.pojo.BaseEntity;
@Entity(noClassnameStored = true)
public class WelfareRescueSunEntity extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3696570499494480928L;
	@Indexed(value = IndexDirection.ASC, unique = true)
	private String playerID;
	private long overTime;
	private boolean get;
	private boolean getCloud;
	
	@Override
	public Map<String,Object> responseMap(){
		Map<String,Object> data = new HashMap<String, Object>(); 
		data.put("overTime", overTime);
		data.put("get", get);
		data.put("getCloud", getCloud);
		return data;
	}
	
	
	
	
	public boolean isGetCloud() {
		return getCloud;
	}

	public void setGetCloud(boolean getCloud) {
		this.getCloud = getCloud;
	}

	public boolean isGet() {
		return get;
	}

	public void setGet(boolean get) {
		this.get = get;
	}

	public long getOverTime() {
		return overTime;
	}

	public void setOverTime(long overTime) {
		this.overTime = overTime;
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
