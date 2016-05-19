package com.mi.game.module.cdk.pojo;

import java.util.HashMap;
import java.util.Map;

import com.google.code.morphia.annotations.Indexed;
import com.mi.core.pojo.BaseEntity;

public class CDKTypeEntity extends BaseEntity {

	private static final long serialVersionUID = 183266897494048548L;

	@Indexed
	private String typeID;
	// 可用总数量
	private int num;
	// 每个帐号可用数量
	private int pnum;
	// 描述
	private String desc;

	public String getTypeID() {
		return typeID;
	}

	public void setTypeID(String typeID) {
		this.typeID = typeID;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getPnum() {
		return pnum;
	}

	public void setPnum(int pnum) {
		this.pnum = pnum;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public Object getKey() {
		return typeID;
	}

	@Override
	public String getKeyName() {
		return "typeID";
	}

	@Override
	public void setKey(Object key) {
		typeID = key.toString();
	}

	@Override
	public Map<String, Object> responseMap(int t) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("typeID", typeID);
		result.put("num", num);
		result.put("pnum", pnum);
		result.put("desc", desc);
		return result;
	}

}
