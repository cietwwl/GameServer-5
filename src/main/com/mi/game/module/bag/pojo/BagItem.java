package com.mi.game.module.bag.pojo;

import java.util.Map;

import com.mi.core.engine.IResponseMap;

public class BagItem implements IResponseMap{
	private int templateID;
	private int num;
	
	public int getTemplateID() {
		return templateID;
	}

	public void setTemplateID(int templateID) {
		this.templateID = templateID;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	@Override
	public Map<String, Object> responseMap() {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public Map<String, Object> responseMap(int t) {
		// TODO 自动生成的方法存根
		return null;
	}
	
}
