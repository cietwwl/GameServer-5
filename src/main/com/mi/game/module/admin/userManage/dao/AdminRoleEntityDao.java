package com.mi.game.module.admin.userManage.dao;

import java.util.List;

import com.mi.core.cache.QueryInfo;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.admin.userManage.pojo.AdminRoleEntity;

public class AdminRoleEntityDao extends AbstractBaseDAO<AdminRoleEntity> {

	private static AdminRoleEntityDao roleEntityDao = new AdminRoleEntityDao();

	public static AdminRoleEntityDao getInstance() {
		return roleEntityDao;
	}

	public AdminRoleEntity getRoleEntity(String roleID) {
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.addQueryCondition("roleID", roleID);
		return this.query(queryInfo);
	}

	public AdminRoleEntity getRoleEntityByName(String roleName) {
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.addQueryCondition("roleName", roleName);
		return this.query(queryInfo);
	}

	public List<AdminRoleEntity> getAllRoleEntity() {
		QueryInfo queryInfo = new QueryInfo("roleID");
		return this.cache.queryList(queryInfo, AdminRoleEntity.class);
	}

}
