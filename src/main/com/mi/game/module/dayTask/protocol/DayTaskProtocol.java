package com.mi.game.module.dayTask.protocol;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mi.core.protocol.BaseProtocol;
import com.mi.game.defines.HandlerIds;
import com.mi.game.module.dayTask.pojo.DayTaskEntity;
import com.mi.game.module.dayTask.pojo.TaskInfo;
import com.mi.game.module.reward.data.GoodsBean;

/**
 * @author 刘凯旋	
 *
 * 2014年8月4日 下午4:52:48
 */
public class DayTaskProtocol extends BaseProtocol{
	private DayTaskEntity entity;
	private Map<String,Object> itemMap;
	private List<Integer> rewardList;
	private List<GoodsBean> showMap;
	private Map<Integer,TaskInfo> initDayTaskInfo; 
	
	public Map<String,Object> responseMap(int y){
		Map<String,Object> data = new HashMap<String, Object>();
		switch(y){
			case HandlerIds.getDayTaskInfo:
				data.put("entity", entity.responseMap());
				break;
			case HandlerIds.getDayTaskReward : 
				data.put("itemMap", itemMap);
				data.put("rewardList", rewardList);
				data.put("showMap",showMap);
				break;
			case HandlerIds.getDayTaskInfoHandler:
				data.put("initDayTaskInfo",initDayTaskInfo.values());
				break;
		}
		return data;
	}
	
	
	public Map<Integer, TaskInfo> getInitDayTaskInfo() {
		return initDayTaskInfo;
	}


	public void setInitDayTaskInfo(Map<Integer, TaskInfo> initDayTaskInfo) {
		this.initDayTaskInfo = initDayTaskInfo;
	}


	public List<GoodsBean> getShowMap() {
		return showMap;
	}

	public void setShowMap(List<GoodsBean> showMap) {
		this.showMap = showMap;
	}

	public List<Integer> getRewardList() {
		return rewardList;
	}

	public void setRewardList(List<Integer> rewardList) {
		this.rewardList = rewardList;
	}

	public Map<String, Object> getItemMap() {
		return itemMap;
	}

	public void setItemMap(Map<String, Object> itemMap) {
		this.itemMap = itemMap;
	}

	public DayTaskEntity getEntity() {
		return entity;
	}

	public void setEntity(DayTaskEntity entity) {
		this.entity = entity;
	}
	
}
