package com.mi.game.module.mainTask.pojo;

import com.mi.core.engine.TemplateManager;
import com.mi.game.module.mainTask.data.MainTaskData;

public class MainTaskInfo {
	private int num;
	private int taskID;
	
	public void addNum(int point,int limit){
		num += point;
		if(num > limit){
			num = limit;
		}
	}
	
	public void setNum(int point,int limit){
		
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public int getTaskID() {
		return taskID;
	}
	public void setTaskID(int taskID) {
		this.taskID = taskID;
	}
	
	/**
	 * 获取任务对于的配置模板数据
	 * @return
	 */
	public MainTaskData taskData(){
		MainTaskData taskData = (MainTaskData)TemplateManager.getTemplateData(taskID);
		return taskData;
	}
	
}
