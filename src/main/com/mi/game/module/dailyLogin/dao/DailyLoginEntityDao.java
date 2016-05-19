package com.mi.game.module.dailyLogin.dao;

import java.util.List;

import com.mi.core.cache.QueryInfo;
import com.mi.core.cache.QueryType;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.dailyLogin.pojo.DailyLoginEntity;

public class DailyLoginEntityDao extends AbstractBaseDAO<DailyLoginEntity> {

	private static DailyLoginEntityDao dailyLoginEntityDao = new DailyLoginEntityDao();

	private DailyLoginEntityDao() {

	}

	public static DailyLoginEntityDao getInstance() {
		return dailyLoginEntityDao;
	}

	public DailyLoginEntity getLoginEntity(String playerID, String day) {
		DailyLoginEntity loginEntity = null;
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.addQueryCondition("playerID", QueryType.EQUAL, playerID);
		queryInfo.addQueryCondition("day", QueryType.EQUAL, day);
		loginEntity = this.query(queryInfo);
		return loginEntity;
	}
	
	/**
	 * 根据日期查询已领取奖励
	 * 
	 * @param playerID
	 * @param day
	 * @return
	 */
	public List<Integer> queryLoginRewardList(String playerID, String day) {
		List list = null;
		DailyLoginEntity loginEntity = null;
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.addQueryCondition("playerID", QueryType.EQUAL, playerID);
		queryInfo.addQueryCondition("day", QueryType.EQUAL, day);
		loginEntity = this.query(queryInfo);
		if(loginEntity != null){
			list = loginEntity.getRewardList();
		}
		return list;
	}
}
