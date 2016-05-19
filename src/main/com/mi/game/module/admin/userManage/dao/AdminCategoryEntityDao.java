package com.mi.game.module.admin.userManage.dao;

import java.util.List;

import com.mi.core.cache.QueryInfo;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.admin.userManage.pojo.AdminCategoryEntity;

public class AdminCategoryEntityDao extends AbstractBaseDAO<AdminCategoryEntity> {

	private static AdminCategoryEntityDao roleEntityDao = new AdminCategoryEntityDao();

	public static AdminCategoryEntityDao getInstance() {
		return roleEntityDao;
	}

	public AdminCategoryEntity getCategoryEntityByName(String categoryName) {
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.addQueryCondition("categoryName", categoryName);
		return this.query(queryInfo);
	}

	public List<AdminCategoryEntity> getAllCategoryEntity() {
		QueryInfo queryInfo = new QueryInfo();
		return this.cache.queryList(queryInfo, AdminCategoryEntity.class);
	}

}
