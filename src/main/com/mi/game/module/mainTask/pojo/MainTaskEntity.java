package com.mi.game.module.mainTask.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.utils.IndexDirection;
import com.mi.core.pojo.BaseEntity;
@Entity(noClassnameStored = true)
public class MainTaskEntity extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2684902410221791236L;
	@Indexed(value = IndexDirection.ASC, unique = true)	
	private String playerID;
	private List<MainTaskInfo> mainTask;
	private List<Integer> completeList;
//	public static void main(String[] args) {
//		Map<Integer,List<MainTaskInfo>> map = new HashMap<>();
//		for(int i  = 1; i < 3; i++){
//			List<MainTaskInfo> list = new ArrayList<>();
//			for(int j = i; j < i+2 ;j ++){
//				MainTaskInfo mainTaskInfo = new MainTaskInfo();
//				mainTaskInfo.setTaskID(i+j);
//				list.add(mainTaskInfo);
//			}
//			map.put(i, list);
//		}
//		Collection<List<MainTaskInfo>> allList = map.values();
//		List<MainTaskInfo> mainList = new ArrayList<>();
//		for(List<MainTaskInfo> temp : allList){
//			for(MainTaskInfo main : temp){
//				mainList.add(main);
//			}
//		}
//		for(MainTaskInfo mainTaskInfo : mainList){
//			System.out.println(mainTaskInfo.getTaskID());
//		}
//		
//	}
	@Override
	public Map<String,Object> responseMap(int y){
		return this.responseMap();
	}
	
	@Override
	public Map<String,Object> responseMap(){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("taskList", mainTask);
		map.put("completeList", completeList);
		return map;
	}
	
	public void addTaskList(List<MainTaskInfo> addList){
		if(mainTask == null){
			mainTask = new ArrayList<>();
		}
		for(MainTaskInfo temp : addList){
			mainTask.add(temp);
		}
	}
	
	public List<Integer> getCompleteList() {
		if(completeList == null){
			completeList = new ArrayList<>();
		}
		return completeList;
	}

	public void setCompleteList(List<Integer> completeList) {
		this.completeList = completeList;
	}

	public List<MainTaskInfo> getMainTask() {
		return mainTask;
	}

	public void setMainTask(List<MainTaskInfo> mainTask) {
		this.mainTask = mainTask;
	}

	@Override
	public Object getKey() {
		// TODO 自动生成的方法存根
		return playerID;
	}

	@Override
	public String getKeyName() {
		// TODO 自动生成的方法存根
		return "playerID";
	}

	@Override
	public void setKey(Object key) {
		// TODO 自动生成的方法存根
		playerID = key.toString();
	}

}
