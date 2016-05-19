package com.mi.game.module.cdk.pojo;

import java.util.HashMap;
import java.util.Map;

import com.google.code.morphia.annotations.Indexed;
import com.mi.core.pojo.BaseEntity;

public class CDKEntity extends BaseEntity {
	private static final long serialVersionUID = -290980624028741613L;

	// cid
	@Indexed
	private String cid;
	// cdk
	@Indexed
	private String cdk;
	// 批次
	private String batch;
	// 奖励物品
	private String rewardID;
	// cdk类型
	private String typeID;
	// 平台
	private String platFrom;
	// 使用次数
	private int used;
	// 开始时间
	private long startTime;
	// 过期时间
	private long endTime;
	// 使用时间
	private long useTime;
	// 创建时间
	private long createTime;

	public String getBatch() {
		return batch;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}

	public String getRewardID() {
		return rewardID;
	}

	public void setRewardID(String rewardID) {
		this.rewardID = rewardID;
	}

	public String getTypeID() {
		return typeID;
	}

	public void setTypeID(String typeID) {
		this.typeID = typeID;
	}

	public String getPlatFrom() {
		return platFrom;
	}

	public void setPlatFrom(String platFrom) {
		this.platFrom = platFrom;
	}

	public String getCdk() {
		return cdk;
	}

	public void setCdk(String cdk) {
		this.cdk = cdk;
	}

	public int isUsed() {
		return used;
	}

	public void setUsed(int used) {
		this.used = used;
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

	public long getUseTime() {
		return useTime;
	}

	public void setUseTime(long useTime) {
		this.useTime = useTime;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	@Override
	public Map<String, Object> responseMap(int t) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("cid", cid);
		result.put("cdk", cdk);
		result.put("batch", batch);
		result.put("rewardID", rewardID);
		result.put("typeID", typeID);
		result.put("platFrom", platFrom);
		result.put("used", used);
		return result;
	}

	@Override
	public Object getKey() {
		return cid;
	}

	@Override
	public String getKeyName() {
		return "cid";
	}

	@Override
	public void setKey(Object key) {
		cid = key.toString();
	}

}
