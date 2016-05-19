package com.mi.game.module.admin.userManage.pojo;

import java.util.ArrayList;
import java.util.List;

import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.utils.IndexDirection;
import com.mi.core.pojo.BaseEntity;

public class AdminUserEntity extends BaseEntity {

	private static final long serialVersionUID = 1189198087458085229L;
	// id
	@Indexed(value = IndexDirection.ASC, unique = true)
	private String userID;
	// 用户名
	private String username;
	// 密码
	private String password;
	// 权限
	private List<AdminRoleEntity> roles = new ArrayList<AdminRoleEntity>();
	// 最后登陆时间
	private long lastTime;
	// 创建时间
	private long createTime;

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<AdminRoleEntity> getRoles() {
		return roles;
	}

	public void setRoles(List<AdminRoleEntity> roles) {
		this.roles = roles;
	}

	public long getLastTime() {
		return lastTime;
	}

	public void setLastTime(long lastTime) {
		this.lastTime = lastTime;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	@Override
	public Object getKey() {
		return userID;
	}

	@Override
	public String getKeyName() {
		return "userID";
	}

	@Override
	public void setKey(Object key) {
		userID = key.toString();
	}

}
