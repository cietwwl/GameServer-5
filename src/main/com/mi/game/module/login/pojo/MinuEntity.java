package com.mi.game.module.login.pojo;

import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.utils.IndexDirection;
import com.mi.core.pojo.BaseEntity;

/**
 * 记录小米测试用户id
 *
 */
public class MinuEntity extends BaseEntity {

	private static final long serialVersionUID = -5870731726838251406L;
	@Indexed(value = IndexDirection.ASC, unique = true)
	private String minu_uid;

	private long dateTime;

	public String getMinu_uid() {
		return minu_uid;
	}

	public void setMinu_uid(String minu_uid) {
		this.minu_uid = minu_uid;
	}

	public long getDateTime() {
		return dateTime;
	}

	public void setDateTime(long dateTime) {
		this.dateTime = dateTime;
	}

	@Override
	public Object getKey() {
		return minu_uid;
	}

	@Override
	public String getKeyName() {
		return "minu_uid";
	}

	@Override
	public void setKey(Object key) {
		minu_uid = key.toString();
	}

}
