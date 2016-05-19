package com.mi.game.module.festival.data;

import com.mi.core.engine.annotation.XmlTemplate;
import com.mi.core.template.BaseTemplate;

@XmlTemplate(template = {"com/mi/template/FestivalDailyRewardPrototype.xml"})
public class FestivalDailyRewardData extends BaseTemplate {
	// 活动时间
	private String dateTime;

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	
}
