package com.mi.game.module.admin.userManage.dao;

import java.util.List;

import com.mi.core.cache.QueryInfo;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.admin.userManage.pojo.AdminUserEntity;

public class AdminUserEntityDao extends AbstractBaseDAO<AdminUserEntity> {

	private static AdminUserEntityDao userEntityDao = new AdminUserEntityDao();

	public static AdminUserEntityDao getInstance() {
		return userEntityDao;
	}

	public AdminUserEntity getUserEntity(String username) {
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.addQueryCondition("username", username);
		return this.query(queryInfo);
	}

	public List<AdminUserEntity> getUserList() {
		return this.cache.queryList(new QueryInfo(), AdminUserEntity.class);
	}

}
