package com.mi.game.module.dailyLogin.dao;

import java.util.ArrayList;
import java.util.List;

import com.mi.core.cache.QueryInfo;
import com.mi.core.cache.QueryType;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.dailyLogin.pojo.DailyLoginPayEntity;


public class DailyLoginPayEntityDao extends
		AbstractBaseDAO<DailyLoginPayEntity> {

	private static DailyLoginPayEntityDao dailyLoginPayEntityDao = new DailyLoginPayEntityDao();

	private DailyLoginPayEntityDao() {

	}

	public static DailyLoginPayEntityDao getInstance() {
		return dailyLoginPayEntityDao;
	}

	public DailyLoginPayEntity getEventPayEveryDayEntity(String playerID,
			String day) {
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.addQueryCondition("playerID", QueryType.EQUAL, playerID);
		queryInfo.addQueryCondition("day", QueryType.EQUAL, day);
		return this.query(queryInfo);
	}

	public List<DailyLoginPayEntity> getPayRank() {
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
		List<DailyLoginPayEntity> expenseList = this.queryList(queryInfo);
		if (expenseList != null && expenseList.size() > 0) {
			for (DailyLoginPayEntity loginPayEntity : expenseList) {
				idList.add(loginPayEntity.getPlayerID());
			}
		}
		return idList;
	}

	/**
	 * 查询已领取奖励
	 * 
	 * @param playerID
	 * @return
	 */
	public List<Integer> queryLoginRewardList(String playerID, String day) {
		List list = null;
		DailyLoginPayEntity payEntity = null;
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.addQueryCondition("playerID", QueryType.EQUAL, playerID);
		queryInfo.addQueryCondition("day", QueryType.EQUAL, day);
		payEntity = this.query(queryInfo);
		if (payEntity != null) {
			list = payEntity.getRewardList();
		}
		return list;
	}

}
