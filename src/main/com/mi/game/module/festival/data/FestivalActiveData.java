package com.mi.game.module.festival.data;

import com.mi.core.engine.annotation.XmlTemplate;
import com.mi.core.template.BaseTemplate;

@XmlTemplate(template = {
	"com/mi/template/FestivalActivePrototype.xml"
})
public class FestivalActiveData extends BaseTemplate {
	// 活动开始时间
	private String startTime;
	// 活动结束时间
	private String endTime;

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

}
