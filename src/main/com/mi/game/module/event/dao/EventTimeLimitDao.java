package com.mi.game.module.event.dao;

import java.util.List;

import com.mi.core.cache.QueryInfo;
import com.mi.core.cache.QueryType;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.event.pojo.EventTimeLimitEntity;

public class EventTimeLimitDao extends AbstractBaseDAO<EventTimeLimitEntity> {

	private static EventTimeLimitDao EventTimeLimitDAO = new EventTimeLimitDao();

	private EventTimeLimitDao() {

	}

	public static EventTimeLimitDao getInstance() {
		return EventTimeLimitDAO;
	}

	public EventTimeLimitEntity getTimeLimitEntity(String playerID, long startTime, long endTime) {
		EventTimeLimitEntity entity = null;
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.addQueryCondition("playerID", playerID);
		queryInfo.addQueryCondition("startTime", startTime);
		queryInfo.addQueryCondition("endTime", endTime);
		entity = this.query(queryInfo);
		return entity;
	}

	public int getRank(EventTimeLimitEntity timeLimitEntity) {
		QueryInfo queryInfo = new QueryInfo("-score,buyTime,playerID");
		queryInfo.addQueryCondition("startTime", timeLimitEntity.getStartTime());
		queryInfo.addQueryCondition("endTime", timeLimitEntity.getEndTime());
		queryInfo.addQueryCondition("score", QueryType.GREATERTHAN_OR_EQUAL, timeLimitEntity.getScore());
		List<EventTimeLimitEntity> list = this.cache.queryList(queryInfo, EventTimeLimitEntity.class);

		String playerID = timeLimitEntity.getPlayerID();
		if (list == null || list.size() == 0) {
			return 0;
		}
		int i = 0;
		for (EventTimeLimitEntity limitEntity : list) {
			i++;
			String key = limitEntity.getPlayerID();
			if (playerID.equals(key)) {
				return i;
			} else {
				continue;
			}
		}

		return 0;
	}

	public List<EventTimeLimitEntity> getRanks(long startTime, long endTime, int size) {
		QueryInfo queryInfo = new QueryInfo(1, size, "-score,buyTime,playerID");
		queryInfo.addQueryCondition("startTime", startTime);
		queryInfo.addQueryCondition("endTime", endTime);
		queryInfo.addQueryCondition("score", QueryType.GREATERTHAN, 0);
		return this.cache.queryPage(queryInfo, EventTimeLimitEntity.class);
	}
}
