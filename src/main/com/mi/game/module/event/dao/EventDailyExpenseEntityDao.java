package com.mi.game.module.event.dao;

import java.util.ArrayList;
import java.util.List;

import com.mi.core.cache.QueryInfo;
import com.mi.core.cache.QueryType;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.event.pojo.EventDailyExpenseEntity;


public class EventDailyExpenseEntityDao extends
		AbstractBaseDAO<EventDailyExpenseEntity> {

	private static EventDailyExpenseEntityDao eventDailyExpenseEntityDao = new EventDailyExpenseEntityDao();

	private EventDailyExpenseEntityDao() {
	}

	public static EventDailyExpenseEntityDao getInstance() {
		return eventDailyExpenseEntityDao;
	}

	public EventDailyExpenseEntity getDailyExpenseEntity(String playerID,
			String day) {
		EventDailyExpenseEntity entity = null;
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.addQueryCondition("playerID", QueryType.EQUAL, playerID);
		queryInfo.addQueryCondition("day", QueryType.EQUAL, day);
		entity = this.query(queryInfo);
		return entity;
	}

	public List<EventDailyExpenseEntity> getExpenseRank() {
		QueryInfo queryInfo = new QueryInfo(1, 2000, "-expenseTotal");
		return queryPage(queryInfo);
	}

	/**
	 * 获取昨天消费的玩家ID列表
	 * 
	 * @param yesterday
	 * @return
	 */
	public List<String> getYesterDayExpensePlayerID(String yesterday) {
		List<String> idList = new ArrayList<String>();
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.addQueryCondition("day", QueryType.EQUAL, yesterday);
		queryInfo.addQueryCondition("expenseTotal", QueryType.GREATERTHAN, 0);
		List<EventDailyExpenseEntity> expenseList = this.queryList(queryInfo);
		if (expenseList != null && expenseList.size() > 0) {
			for (EventDailyExpenseEntity dailyExpenseEntity : expenseList) {
				idList.add(dailyExpenseEntity.getPlayerID());
			}
		}
		return idList;
	}

}
