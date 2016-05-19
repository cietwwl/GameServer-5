package com.mi.game.module.login.pojo;

import com.google.code.morphia.annotations.Indexed;
import com.mi.core.pojo.BaseEntity;

public class TestUserRewardEntity extends BaseEntity {

	private static final long serialVersionUID = -1080572623242861177L;
	@Indexed
	private String uid;

	private long dateTime;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public long getDateTime() {
		return dateTime;
	}

	public void setDateTime(long dateTime) {
		this.dateTime = dateTime;
	}

	@Override
	public Object getKey() {
		return uid;
	}

	@Override
	public String getKeyName() {
		return "uid";
	}

	@Override
	public void setKey(Object key) {
		uid = key.toString();
	}

}
