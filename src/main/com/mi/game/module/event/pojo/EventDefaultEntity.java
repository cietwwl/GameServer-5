package com.mi.game.module.event.pojo;

import java.util.HashMap;
import java.util.Map;

import com.mi.core.pojo.BaseEntity;

public class EventDefaultEntity extends BaseEntity {

	private static final long serialVersionUID = -7991598487024852895L;

	@Override
	public Map<String, Object> responseMap() {
		return new HashMap<String, Object>();
	}

	@Override
	public Object getKey() {
		return null;
	}

	@Override
	public String getKeyName() {
		return null;
	}

	@Override
	public void setKey(Object key) {

	}

}
