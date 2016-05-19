package com.mi.game.module.analyse.dao;

import com.mi.core.cache.QueryInfo;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.analyse.pojo.DeviceEntity;

public class DeviceEntityDao extends AbstractBaseDAO<DeviceEntity> {

	private static DeviceEntityDao analyDao = new DeviceEntityDao();

	private DeviceEntityDao() {

	}

	public static DeviceEntityDao getInstance() {
		return analyDao;
	}

	public DeviceEntity getDeviceEntity(String device_id) {
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.addQueryCondition("device_id", device_id);
		DeviceEntity entity = this.query(queryInfo);
		return entity;
	}

}
