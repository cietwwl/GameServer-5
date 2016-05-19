package com.mi.game.module.abstrac;

import java.util.Map;

import com.mi.core.pojo.BaseEntity;

public abstract class AbstractBaseEntity extends BaseEntity {

	private static final long serialVersionUID = 1L;

	public abstract Map<String, Object> getResponseMap();

	/**
	 * 按指定类型组装数据
	 * 
	 * @param type
	 * @return
	 */
	public Map<String, Object> getResponseMap(int type) {
		return null;
	}

	@Override
	public String toString() {
		Map<String, Object> data = getResponseMap();
		if (data != null) {
			return data.toString();
		}
		return super.toString();
	}

}
