package com.mi.game.module.festival.job;

import java.util.List;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.TemplateManager;
import com.mi.core.job.BaseJob;
import com.mi.core.job.annotation.QuartzJob;
import com.mi.core.util.DateTimeUtil;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.festival.data.FestivalDailyRewardData;
import com.mi.game.module.reward.RewardModule;
import com.mi.game.util.Utilities;

@QuartzJob(id="AllPlayerRewardJob",count=99999999,interval=(int)DateTimeUtil.ONE_DAY_TIME_S,startTime="10:00:00")
public class AllPlayerRewardJob extends BaseJob {

	@Override
	protected JobDataMap setParameter(JobDataMap mapData) {
		// TODO Auto-generated method stub
		return mapData;
	}

	@Override
	protected void onExecute(JobDataMap contextData, JobExecutionContext arg0) {
		//定时刷新任务
		RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule,RewardModule.class);
		List<FestivalDailyRewardData> festivalDailyRewardList=TemplateManager.getTemplateList(FestivalDailyRewardData.class);
		if(festivalDailyRewardList!=null&&!festivalDailyRewardList.isEmpty()){
			String nowTime = Utilities.getDateTime();
			for(FestivalDailyRewardData festivalDailyRewardData:festivalDailyRewardList){
				if(nowTime.equals(festivalDailyRewardData.getDateTime())){
					rewardModule.giveAllPlayerReward(String.valueOf(festivalDailyRewardData.getPid()));					
				}
			}
		}			
	}

}
