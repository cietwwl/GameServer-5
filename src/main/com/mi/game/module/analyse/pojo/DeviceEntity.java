package com.mi.game.module.analyse.pojo;

import com.google.code.morphia.annotations.Indexed;
import com.mi.core.pojo.BaseEntity;

public class DeviceEntity extends BaseEntity {

	private static final long serialVersionUID = -4679669003093125921L;

	private String did;
	// 设备id
	@Indexed
	private String device_id;
	// 设备类型
	private String phonetype;
	// 平台id
	private String platform;
	// 创建时间
	private long createTime;
	// 更新时间
	private long updateTime;

	public String getDid() {
		return did;
	}

	public void setDid(String did) {
		this.did = did;
	}

	public String getPhonetype() {
		return phonetype;
	}

	public void setPhonetype(String phonetype) {
		this.phonetype = phonetype;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public String getDevice_id() {
		return device_id;
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	@Override
	public Object getKey() {
		return did;
	}

	@Override
	public String getKeyName() {
		return "did";
	}

	@Override
	public void setKey(Object key) {
		did = key.toString();
	}

}
