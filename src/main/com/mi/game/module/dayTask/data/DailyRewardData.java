package com.mi.game.module.dayTask.data;

import java.util.ArrayList;
import java.util.List;

import com.mi.core.engine.annotation.XmlTemplate;
import com.mi.core.template.BaseTemplate;
import com.mi.game.module.reward.data.GoodsBean;
@XmlTemplate(template = {"com/mi/template/DailyRewardPrototype.xml"})
public class DailyRewardData extends BaseTemplate{
	private int score;
	private List<GoodsBean> reward;
	
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public List<GoodsBean> getReward() {
		return reward;
	}
	public void setReward(String reward) {
		if(reward != null && !reward.isEmpty()){
			this.reward = new ArrayList<GoodsBean>();
			String[] strArr = reward.split(",");
			for(String str : strArr){
				String[] rewardArr = str.split("=");
				GoodsBean goodsBean = new GoodsBean(Integer.parseInt(rewardArr[0]),Integer.parseInt(rewardArr[1]));		
				this.reward.add(goodsBean);
			}
		}
	}
	
	
	
}
