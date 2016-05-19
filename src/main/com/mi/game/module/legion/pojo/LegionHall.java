package com.mi.game.module.legion.pojo;

import java.util.HashMap;
import java.util.Map;

public class LegionHall extends LegionBase {

	public Map<String, Object> responseMap() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("currentPid", getCurrentPid());
		return result;
	}

}
