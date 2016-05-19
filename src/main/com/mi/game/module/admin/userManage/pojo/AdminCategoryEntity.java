package com.mi.game.module.admin.userManage.pojo;

import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.utils.IndexDirection;
import com.mi.core.pojo.BaseEntity;

public class AdminCategoryEntity extends BaseEntity {
	private static final long serialVersionUID = 5411798936691718104L;
	// 类型id
	@Indexed(value = IndexDirection.ASC, unique = true)
	private String categoryID;
	// 类型名称
	private String categoryName;
	// 描述
	private String categoryDesc;

	public String getCategoryID() {
		return categoryID;
	}

	public void setCategoryID(String categoryID) {
		this.categoryID = categoryID;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCategoryDesc() {
		return categoryDesc;
	}

	public void setCategoryDesc(String categoryDesc) {
		this.categoryDesc = categoryDesc;
	}

	@Override
	public Object getKey() {
		return categoryID;
	}

	@Override
	public String getKeyName() {
		return "categoryID";
	}

	@Override
	public void setKey(Object key) {
		categoryID = key.toString();
	}

}
