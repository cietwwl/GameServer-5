package com.mi.game.module.achievement.data;

import com.mi.core.engine.annotation.XmlTemplate;
import com.mi.core.template.BaseTemplate;
import com.mi.game.module.reward.data.GoodsBean;
/**
 * @author 刘凯旋	
 *
 * 2014年8月6日 下午5:00:39
 */
@XmlTemplate(template = {"com/mi/template/AchievementPrototype.xml"})
public class AchievementData extends BaseTemplate{
	private int type;
	private int nextID;
	private GoodsBean reward;
	private int actionID;
	private long num;
	private int isOrigin;
	private String achievement;
	
	public int getIsOrigin() {
		return isOrigin;
	}
	public void setIsOrigin(int isOrigin) {
		this.isOrigin = isOrigin;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getNextID() {
		return nextID;
	}
	public void setNextID(int nextID) {
		this.nextID = nextID;
	}
	public GoodsBean getReward() {
		return reward;
	}
	public void setReward(String reward) {
		if(reward != null && !reward.isEmpty()){
			this.reward = new GoodsBean();
			String[] strArr = reward.split("=");
			this.reward.setPid(Integer.parseInt(strArr[0]));
			this.reward.setNum(Integer.parseInt(strArr[1]));
		}
		 
	}
	public int getActionID() {
		return actionID;
	}
	public long getNum() {
		return num;
	}
	public void setAchievement(String achievement){
		if(achievement != null && !achievement.isEmpty()){
			String[] strArr = achievement.split("=");
			this.actionID = Integer.parseInt(strArr[0]);
			this.num = Integer.parseInt(strArr[1]);
		}
	
	}
	
	public String getAchievement(){
		return achievement;
	}
}
