package com.mi.game.module.event.dao;

import java.util.ArrayList;
import java.util.List;

import com.mi.core.cache.QueryInfo;
import com.mi.core.cache.QueryType;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.event.pojo.EventPayEveryDayEntity;


public class EventPayEveryDayEntityDao extends
		AbstractBaseDAO<EventPayEveryDayEntity> {

	private static EventPayEveryDayEntityDao eventPayEveryDayEntityDao = new EventPayEveryDayEntityDao();

	private EventPayEveryDayEntityDao() {

	}

	public static EventPayEveryDayEntityDao getInstance() {
		return eventPayEveryDayEntityDao;
	}

	public EventPayEveryDayEntity getEventPayEveryDayEntity(String playerID,
			String day) {
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.addQueryCondition("playerID", QueryType.EQUAL, playerID);
		queryInfo.addQueryCondition("day", QueryType.EQUAL, day);
		return this.query(queryInfo);
	}

	public List<EventPayEveryDayEntity> getPayRank() {
		QueryInfo queryInfo = new QueryInfo(1, 1000, "-payTotal");
		return queryPage(queryInfo);
	}

	/**
	 * 获取昨天充值的玩家ID列表
	 * 
	 * @param yesterday
	 * @return
	 */
	public List<String> getYesterDayPayPlayerID(String yesterday) {
		List<String> idList = new ArrayList<String>();
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.addQueryCondition("day", QueryType.EQUAL, yesterday);
		queryInfo.addQueryCondition("payTotal", QueryType.GREATERTHAN, 0);
		List<EventPayEveryDayEntity> expenseList = this.queryList(queryInfo);
		if (expenseList != null && expenseList.size() > 0) {
			for (EventPayEveryDayEntity payEveryDayEntity : expenseList) {
				idList.add(payEveryDayEntity.getPlayerID());
			}
		}
		return idList;
	}
}
