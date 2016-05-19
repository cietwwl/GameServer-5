package com.mi.game.module.dayTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.TemplateManager;
import com.mi.core.engine.annotation.Module;
import com.mi.core.util.DateTimeUtil;
import com.mi.game.defines.ErrorIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.defines.RewardType;
import com.mi.game.module.base.BaseModule;
import com.mi.game.module.dayTask.dao.DayTaskEntityDAO;
import com.mi.game.module.dayTask.data.DailyRewardData;
import com.mi.game.module.dayTask.data.DayTaskData;
import com.mi.game.module.dayTask.pojo.DayTaskEntity;
import com.mi.game.module.dayTask.pojo.TaskInfo;
import com.mi.game.module.dayTask.protocol.DayTaskProtocol;
import com.mi.game.module.login.LoginModule;
import com.mi.game.module.login.pojo.PlayerEntity;
import com.mi.game.module.reward.RewardModule;
import com.mi.game.module.reward.data.GoodsBean;
import com.mi.game.util.Utilities;
@Module(name = ModuleNames.DayTaskModule,clazz = DayTaskModule.class)
public class DayTaskModule extends BaseModule{
	private final DayTaskEntityDAO dayTaskEntityDAO = DayTaskEntityDAO.getInstance();
	private final int[] rewardList = new int[]{10641,10642,10643};
	
	/**
	 * 获取实体
	 * */
	public DayTaskEntity getEntity(String playerID){
		DayTaskEntity dayTaskEntity = dayTaskEntityDAO.getEntity(playerID);
		if(dayTaskEntity == null){
			dayTaskEntity = this.initDayTaskEntity(playerID);
			this.saveDayTaskEntity(dayTaskEntity);
		}
		return dayTaskEntity;
	}
	
	/**
	 * 获取更新后的实体
	 * */
	
	public DayTaskEntity getUpdateEntity(String playerID){
		DayTaskEntity entity = this.getEntity(playerID);
		long nowTime = System.currentTimeMillis();
		if(!DateTimeUtil.isSameDay(entity.getUpdateTime(), nowTime)){
			getOldTaskReward(playerID,entity);
			entity = this.initDayTaskEntity(playerID);
			this.saveDayTaskEntity(entity);
		}
		return entity;
	}
	
	public boolean checkDayTask(DayTaskEntity entity){
		for(int rewardID :rewardList){
			List<Integer> getList = entity.getRewardList();
			int score = entity.getScore();
			if(!getList.contains(rewardID)){
				DailyRewardData data = TemplateManager.getTemplateData(rewardID,DailyRewardData.class);
				int needScore = data.getScore();
				if(needScore < score){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 获取未领取的任务奖励
	 * */
	public void getOldTaskReward(String playerID,DayTaskEntity entity){
		
		List<Integer> getList = entity.getRewardList();
		int score = entity.getScore();
		Map<Integer,GoodsBean> goodsList = new HashMap<>();
		for(int rewardID : rewardList){
			if(!getList.contains(rewardID)){
				DailyRewardData data = TemplateManager.getTemplateData(rewardID,DailyRewardData.class);
				int needScore = data.getScore();
				if(needScore < score){
					List<GoodsBean> rewads = data.getReward();
					for(GoodsBean goodsBean : rewads){
						if(goodsList.get(goodsBean.getPid()) != null){
							GoodsBean tempBean = goodsList.get(goodsBean.getPid());
							tempBean.setNum(tempBean.getNum() + goodsBean.getNum());
							goodsList.put(goodsBean.getPid(), tempBean);
						}else{
							goodsList.put(goodsBean.getPid(), new GoodsBean(goodsBean.getPid(),goodsBean.getNum()));
						}
					}
				}
			}
		}
		if(!goodsList.isEmpty()){
			RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule,RewardModule.class);
			rewardModule.addReward(playerID, Utilities.getGoodList(goodsList), RewardType.dayTaskReward);
		}
		
	}
	
	/**
	 * 保存实体
	 * */
	public void saveDayTaskEntity(DayTaskEntity entity){
		dayTaskEntityDAO.save(entity);
	}
	
	/**
	 * 初始化实体
	 * */
	public DayTaskEntity initDayTaskEntity(String playerID){
		long nowTime = System.currentTimeMillis();
		DayTaskEntity entity = new DayTaskEntity();
		entity.setKey(playerID);
		entity.setUpdateTime(nowTime);
		Map<Integer,TaskInfo> map = new HashMap<>();
		List<DayTaskData> dataList = TemplateManager.getTemplateList(DayTaskData.class);
		for(DayTaskData data : dataList){
			TaskInfo taskInfo = new TaskInfo();
			taskInfo.setTaskID(data.getPid());
			map.put(data.getActionID(), taskInfo);
		}
		entity.setTaskInfo(map);
		return entity;
	}
	
	/**
	 * 增加积分
	 * */
	public void addScore(String playerID,int actionType,int addNum){
		LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule,LoginModule.class);
		PlayerEntity playerEntity = loginModule.getPlayerEntity(playerID);
		int level = playerEntity.getLevel();
		if(level < 31){
			return ;
		}
		DayTaskEntity entity = this.getUpdateEntity(playerID);
		Map<Integer,TaskInfo> taskMap  = entity.getTaskInfo();
		TaskInfo taskInfo  = taskMap.get(actionType);
		if(taskInfo != null){
			int num = taskInfo.getNum();
			int pid = taskInfo.getTaskID();
			DayTaskData data = TemplateManager.getTemplateData(pid, DayTaskData.class);
			int maxNum = data.getActionNum();
			if(num < maxNum){
				num += addNum ;
				if(num >= maxNum){
					int score = data.getScore();
					entity.setScore(entity.getScore() + score);
					if(this.checkDayTask(entity)){
						loginModule.changePlayerTaskEntity(playerID, true);
					}
				}
				if(num > maxNum){
					num = maxNum;
				}
				taskInfo.setNum(num);
				this.saveDayTaskEntity(entity);
			}
		}
	}

	/**
	 * 领取奖励
	 * */
	public void getTaskReward(String playerID,int pid,DayTaskProtocol protocol){
		DayTaskEntity entity = this.getUpdateEntity(playerID);
		List<Integer> rewardList = entity.getRewardList();
		if(rewardList.contains(pid)){
			throw new IllegalArgumentException(ErrorIds.AlreadyGetQuestReward + "");
		}
		DailyRewardData data = TemplateManager.getTemplateData(pid, DailyRewardData.class);
		if(data == null){
			throw new IllegalArgumentException(ErrorIds.WrongQuestRewardID + "");
		}
		int needScore = data.getScore();
		int score = entity.getScore();
		if(score < needScore){
			throw new IllegalArgumentException(ErrorIds.QuestScoreNotEnough + "");
		}
		rewardList.add(pid);
		if(!this.checkDayTask(entity)){
			LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule,LoginModule.class);
			loginModule.changePlayerTaskEntity(playerID, false);
		}
		Map<String,Object> itemMap = new HashMap<>();
		RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
		rewardModule.addGoods(playerID, data.getReward(), true, null, itemMap, null);
		protocol.setItemMap(itemMap);
		protocol.setRewardList(rewardList);
		protocol.setShowMap(data.getReward());
		this.saveDayTaskEntity(entity);
	}
	
}
