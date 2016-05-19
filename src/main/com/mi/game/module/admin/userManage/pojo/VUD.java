package com.mi.game.module.admin.userManage.pojo;

public class VUD {

	// id
	private String id;
	// 名称
	private String name;

	public String getId() {
		return id;
	}

	public VUD(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
