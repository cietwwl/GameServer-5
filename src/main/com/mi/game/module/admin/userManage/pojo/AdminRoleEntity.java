package com.mi.game.module.admin.userManage.pojo;

import java.util.Set;

import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.utils.IndexDirection;
import com.mi.core.pojo.BaseEntity;

public class AdminRoleEntity extends BaseEntity {
	private static final long serialVersionUID = -6460286193645900326L;
	// 权限id
	@Indexed(value = IndexDirection.ASC, unique = true)
	private String roleID;
	// 权限类型
	private String categoryID;
	// 权限名称
	private String roleName;
	// 地址
	private String roleUrl;
	// 对应实体
	private String entity;
	// 操作权限 crud
	private Set<String> permission;

	public String getRoleID() {
		return roleID;
	}

	public void setRoleID(String roleID) {
		this.roleID = roleID;
	}

	public String getRoleName() {
		return roleName;
	}

	public String getCategoryID() {
		return categoryID;
	}

	public void setCategoryID(String categoryID) {
		this.categoryID = categoryID;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleUrl() {
		return roleUrl;
	}

	public void setRoleUrl(String roleUrl) {
		this.roleUrl = roleUrl;
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public Set<String> getPermission() {
		return permission;
	}

	public void setPermission(Set<String> permission) {
		this.permission = permission;
	}

	@Override
	public Object getKey() {
		return roleID;
	}

	@Override
	public String getKeyName() {
		return "roleID";
	}

	@Override
	public void setKey(Object key) {
		roleID = key.toString();
	}

}
