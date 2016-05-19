package com.mi.game.module.dayTask.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.utils.IndexDirection;
import com.mi.core.pojo.BaseEntity;

/**
 * @author 刘凯旋	
 *
 * 2014年8月2日 下午4:33:48
 */
@Entity(noClassnameStored = true)
public class DayTaskEntity extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4820081174321776855L;
	@Indexed(unique=true,value=IndexDirection.ASC)
	private String playerID;
	private int score;
	private long updateTime;
	private List<Integer> rewardList;
	private Map<Integer,TaskInfo> taskInfo;
	@Override
	public Map<String,Object> responseMap(){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("score", score);
		map.put("taskInfoList", taskInfo.values());
		map.put("rewardList", rewardList);
		return map;
	}
	
	
	public List<Integer> getRewardList() {
		if(rewardList == null){
			rewardList = new ArrayList<>();
		}
		return rewardList;
	}


	public void setRewardList(List<Integer> rewardList) {
		this.rewardList = rewardList;
	}


	public Map<Integer,TaskInfo> getTaskInfo() {
		return taskInfo;
	}

	public void setTaskInfo(Map<Integer,TaskInfo> taskInfo) {
		this.taskInfo = taskInfo;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
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
