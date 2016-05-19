package com.mi.game.module.dayTask.data;

import com.mi.core.engine.annotation.XmlTemplate;
import com.mi.core.template.BaseTemplate;
@XmlTemplate(template = {"com/mi/template/DailyQuestPrototype.xml"})
public class DayTaskData extends BaseTemplate{
	private int score;
	private int actionID;
	private int actionNum;
	private String quest;
	
	public String getQuest() {
		return quest;
	}

	public void setQuest(String quest) {
		String[] strArr = quest.split("=");
		this.actionID = Integer.parseInt(strArr[0]);
		this.actionNum = Integer.parseInt(strArr[1]);
	}

	public int getActionID() {
		return actionID;
	}


	public int getActionNum() {
		return actionNum;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	
	
}
