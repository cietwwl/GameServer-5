package com.mi.game.module.pk.dao;

import java.util.ArrayList;
import java.util.List;

import com.mi.core.cache.QueryInfo;
import com.mi.core.cache.QueryType;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.pk.pojo.PkEntity;


public class PkEntityDao extends AbstractBaseDAO<PkEntity> {
	private static PkEntityDao entityDao = new PkEntityDao();

	private PkEntityDao() {

	}

	public static PkEntityDao getInstance() {
		return entityDao;
	}

	/**
	 * 根据积分查询排行
	 * 
	 * @param points
	 * @return
	 */
	public long getPointsRank(int points, String playerID) {
		QueryInfo queryInfo = new QueryInfo();
		if(points == 0){
			points = 1;
		}
		queryInfo.addQueryCondition("points", QueryType.GREATERTHAN_OR_EQUAL,
				points);
		queryInfo.setOrder("-points,+playerID");
		List<PkEntity> list = queryList(queryInfo);
		if (list == null || list.size() == 0) {
			return 0;
		}
		int i = 0;
		for (PkEntity pkEntity : list) {
			i++;
			String key = pkEntity.getKey().toString();
			if (playerID.equals(key)) {
				return i;
			} else {
				continue;
			}
		}
		return 0;
	}

	/**
	 * 查询积分排行前两位
	 * 
	 * @return
	 */
	public List<PkEntity> getListLimtTwo() {
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.setOrder("-points,+updateTime");
		return queryList(queryInfo);
	}

	public List<Object> getPkPlayerIDList() {
		QueryInfo queryInfo = new QueryInfo();
		List<PkEntity> list = queryList(queryInfo);
		List<Object> idList = null;
		if (list != null && list.size() > 0) {
			idList = new ArrayList<Object>();
			for (PkEntity pkEntity : list) {
				idList.add((Object) pkEntity.getPlayerID());
			}
		}
		return idList;
	}

}
