package com.mi.game.module.admin.protocol;

import java.util.HashMap;
import java.util.Map;

import com.mi.core.protocol.BaseProtocol;

public class BaseAdminProtocol extends BaseProtocol {
	
	private Map<String, Object> parse = new HashMap<String, Object>();

	public void put(String key, Object value) {
		parse.put(key, value);
	}

	@Override
	public Map<String, Object> responseMap() {
		return parse;
	}

	@Override
	public Map<String, Object> responseMap(int y) {
		return parse;
	}

}
