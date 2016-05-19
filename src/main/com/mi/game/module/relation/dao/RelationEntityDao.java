package com.mi.game.module.relation.dao;

import com.mi.core.cache.QueryInfo;
import com.mi.core.cache.QueryType;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.relation.pojo.RelationEntity;

public class RelationEntityDao extends AbstractBaseDAO<RelationEntity> {

	private static final RelationEntityDao relationDao = new RelationEntityDao();

	private RelationEntityDao() {
	}

	public static RelationEntityDao getInstance() {
		return relationDao;
	}

	public RelationEntity getRelationEntity(String masterID, String pupilID) {
		RelationEntity entity = null;
		QueryInfo queryInfo = new QueryInfo();
		if (masterID != null && !masterID.isEmpty()) {
			queryInfo.addQueryCondition("masterID", QueryType.EQUAL, masterID);
		}
		if (pupilID != null && !pupilID.isEmpty()) {
			queryInfo.addQueryCondition("pupilID", QueryType.EQUAL, pupilID);
		}
		entity = this.query(queryInfo);
		return entity;
	}

}
