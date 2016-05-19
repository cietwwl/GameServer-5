package com.mi.game.module.legion.pojo;

import java.util.HashMap;
import java.util.Map;

import com.mi.game.util.Utilities;

public class LegionGG extends LegionBase {

	// 剩余参拜次数
	private int visitNum;

	// 最大参拜次数
	private int maxVisit;
	// 参拜时间
	private String visitTime;

	public int getVisitNum() {
		return visitNum;
	}

	public void setVisitNum(int visitNum) {
		this.visitNum = visitNum;
	}

	public int getMaxVisit() {
		return maxVisit;
	}

	public void setMaxVisit(int maxVisit) {
		this.maxVisit = maxVisit;
	}

	public String getVisitTime() {
		return visitTime;
	}

	public void setVisitTime(String visitTime) {
		this.visitTime = visitTime;
	}

	public boolean isVisit() {
		String dateTime = Utilities.getDateTime();
		if (visitTime == null || visitTime.isEmpty()) {
			visitTime = dateTime;
			return false;
		}
		if (!visitTime.equals(dateTime)) {
			visitTime = dateTime;
			return false;
		}
		return true;
	}

	public Map<String, Object> responseMap() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("currentPid", getCurrentPid());
		if (!isVisit()) {
			result.put("visitNum", maxVisit);
		} else {
			result.put("visitNum", visitNum);
		}
		result.put("maxVisit", maxVisit);
		return result;
	}

}
