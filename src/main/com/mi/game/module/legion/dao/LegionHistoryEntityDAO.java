package com.mi.game.module.legion.dao;

import java.util.List;

import com.mi.core.cache.QueryInfo;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.legion.pojo.LegionHistoryEntity;

public class LegionHistoryEntityDAO extends AbstractBaseDAO<LegionHistoryEntity> {

	private static final LegionHistoryEntityDAO historyEntityDAO = new LegionHistoryEntityDAO();

	private LegionHistoryEntityDAO() {
	}

	public static LegionHistoryEntityDAO getInstance() {
		return historyEntityDAO;
	}

	/**
	 * 查询军团历史操作
	 * 
	 * @param legionName
	 * @return
	 */
	public List<LegionHistoryEntity> getLegionHistoryList(String legionID) {
		QueryInfo queryInfo = new QueryInfo(1, 50, "-createTime");
		queryInfo.addQueryCondition("legionID", legionID);
		List<LegionHistoryEntity> historyList = cache.queryPage(queryInfo, LegionHistoryEntity.class);
		return historyList;
	}

	/**
	 * 查询军团创建操作
	 * 
	 * @param legionName
	 * @return
	 */
	public List<LegionHistoryEntity> getLegionCreateHistoryList() {
		QueryInfo queryInfo = new QueryInfo(1, 50, "-createTime");
		queryInfo.addQueryCondition("type", "create");
		List<LegionHistoryEntity> historyList = cache.queryList(queryInfo, LegionHistoryEntity.class);
		return historyList;
	}

	/**
	 * 查询用户最后一次 type操作
	 * 
	 * @param playerID
	 * @return
	 */
	public LegionHistoryEntity getLegionHistoryEntityByPlayerID(String playerID, String type) {
		QueryInfo queryInfo = new QueryInfo("-createTime");
		queryInfo.addQueryCondition("playerID", playerID);
		queryInfo.addQueryCondition("type", type);
		return this.query(queryInfo);
	}

}
