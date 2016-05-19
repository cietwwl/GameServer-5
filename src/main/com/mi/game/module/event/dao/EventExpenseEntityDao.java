package com.mi.game.module.event.dao;

import java.util.List;

import com.mi.core.cache.QueryInfo;
import com.mi.core.cache.QueryType;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.event.pojo.EventExpenseEntity;

public class EventExpenseEntityDao extends AbstractBaseDAO<EventExpenseEntity> {

	private static EventExpenseEntityDao eventExpenseEntityDao = new EventExpenseEntityDao();

	private EventExpenseEntityDao() {

	}

	public static EventExpenseEntityDao getInstance() {
		return eventExpenseEntityDao;
	}

	public EventExpenseEntity getExpenseEntity(String playerID) {
		EventExpenseEntity entity = null;
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.addQueryCondition("playerID", QueryType.EQUAL, playerID);
		entity = this.query(queryInfo);
		return entity;
	}

	public List<EventExpenseEntity> getExpenseRank() {
		QueryInfo queryInfo = new QueryInfo(1, 2000, "-expenseTotal");
		return queryPage(queryInfo);
	}

}
