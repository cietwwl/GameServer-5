package com.mi.game.module.legion.dao;

import java.util.List;

import com.mi.core.cache.QueryInfo;
import com.mi.core.cache.QueryType;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.legion.pojo.LegionEntity;

public class LegionEntityDAO extends AbstractBaseDAO<LegionEntity> {

	private static final LegionEntityDAO legionDAO = new LegionEntityDAO();

	private LegionEntityDAO() {
	}

	public static LegionEntityDAO getInstance() {
		return legionDAO;
	}

	/**
	 * 根据军团名称查询军团信息
	 * 
	 * @param legionName
	 * @return
	 */
	public LegionEntity getLegionByName(String name) {
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.addQueryCondition("name", QueryType.EQUAL, name);
		queryInfo.addQueryCondition("isKill", QueryType.NOT_EQUAL, true);
		LegionEntity entity = this.query(queryInfo);
		return entity;
	}

	/**
	 * 根据等级排序,查出10条记录
	 * 
	 * @param page
	 * @return
	 */
	public List<LegionEntity> getLegionList(int page, String name) {
		QueryInfo queryInfo = new QueryInfo(page, 10, "-level");
		queryInfo.addQueryCondition("isKill", QueryType.NOT_EQUAL, true);
		if (null != name && !name.isEmpty()) {
			queryInfo.addQueryCondition("name", QueryType.LIKE, name);
		}
		return cache.queryPage(queryInfo, LegionEntity.class);
	}

	/**
	 * 根据军团等级获取军团列表
	 * 
	 * @param level
	 * @return
	 */
	public List<LegionEntity> getLegionLevelList(int level) {
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.addQueryCondition("isKill", QueryType.NOT_EQUAL, true);
		queryInfo.addQueryCondition("level", level);
		return cache.queryList(queryInfo, LegionEntity.class);
	}

}
