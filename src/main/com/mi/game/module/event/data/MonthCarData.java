package com.mi.game.module.event.data;

import java.util.List;

import com.mi.core.engine.annotation.XmlTemplate;
import com.mi.game.module.reward.data.GoodsBean;
import com.mi.game.util.CommonMethod;

@XmlTemplate(template = { "com/mi/template/MonthCardPrototype.xml" })
public class MonthCarData extends EventBaseData {	
	private List<GoodsBean> dailyReward;

	

	public List<GoodsBean> getDailyReward() {
		return dailyReward;
	}



	public void setDailyReward(String str) {
		this.dailyReward = CommonMethod.stringToGoodsBeanList(str);
	}
	
	
}
