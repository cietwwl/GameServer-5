package com.mi.game.module.mainTask.data;

import java.util.ArrayList;
import java.util.List;

import com.mi.core.engine.annotation.XmlTemplate;
import com.mi.core.template.BaseTemplate;
import com.mi.game.module.reward.data.GoodsBean;

@XmlTemplate(template = {"com/mi/template/QuestPrototype.xml"})
public class MainTaskData extends BaseTemplate{
	private int actionType;
	private int backID;
	private int nextID;
	private int openLevel;
	private int[] parse;
	private String quest;
	private List<GoodsBean> reward;
	public static void main(String[] args) {
		String a = "1#2";
		String[] temp = a.split("#");
		List<Integer> list = new ArrayList<>();
		int c = 1;
		int d = 2;
		list.add(c);
		list.add(d);
		int[] parse = new int[]{list.get(0),d}; 
	}

	public List<GoodsBean> getReward() {
		return reward;
	}
	public void setReward(String reward) {
		if(reward != null && !reward.isEmpty()){
			this.reward = new ArrayList<>();
			String[] strArr = reward.split(",");
			for(String temp : strArr){
				String[] tempArr = temp.split("=");
				GoodsBean goodsBean = new GoodsBean(Integer.parseInt(tempArr[0]), Integer.parseInt(tempArr[1]));
				this.reward.add(goodsBean);
			}
		}
	}
	public int[] getParse() {
		return parse;
	}
	public void setParse(int[] parse) {
		this.parse = parse;
	}
	public int getOpenLevel() {
		return openLevel;
	}
	public void setOpenLevel(int openLevel) {
		this.openLevel = openLevel;
	}
	public int getNextID() {
		return nextID;
	}
	public void setNextID(int nextID) {
		this.nextID = nextID;
	}
	public int getActionType() {
		return actionType;
	}
	public void setActionType(int actionType) {
		this.actionType = actionType;
	}
	public int getBackID() {
		return backID;
	}
	public void setBackID(int backID) {
		this.backID = backID;
	}
	public String getQuest() {
		return quest;
	}
	public void setQuest(String quest) {
		if(quest != null && !quest.isEmpty()){
			String[] arr = quest.split("=");
			this.actionType = Integer.parseInt(arr[0]);
			String temp = arr[1];
			String[] tempArr = temp.split("#");
			if(tempArr.length>1){
				this.parse = new int[]{Integer.parseInt(tempArr[0]),Integer.parseInt(tempArr[1])};
			}else{
				this.parse = new int[]{Integer.parseInt(tempArr[0])};
			}
			
		}
	}
}
