package com.mi.game.module.event.dao;

import java.util.List;

import com.mi.core.cache.QueryInfo;
import com.mi.core.cache.QueryType;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.event.pojo.EventConfigEntity;

public class EventConfigDao extends AbstractBaseDAO<EventConfigEntity> {

	private static EventConfigDao configDao = new EventConfigDao();

	private EventConfigDao() {

	}

	public static EventConfigDao getInstance() {
		return configDao;
	}

	public EventConfigEntity getEventConfigEntityByPid(int pid) {
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.addQueryCondition("pid", QueryType.EQUAL, pid);
		return this.query(queryInfo);
	}

	// 获取状态为0的活动
	public List<EventConfigEntity> getAllEventConfigEntity(boolean sign) {
		QueryInfo queryInfo = new QueryInfo("index");
		if (sign) {
			queryInfo.addQueryCondition("status", 0);
		}
		List<EventConfigEntity> list = this.cache.queryList(queryInfo, EventConfigEntity.class);
		return list;
	}
}
