package com.mi.game.module.event.job;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

import com.mi.core.engine.ModuleManager;
import com.mi.core.job.BaseJob;
import com.mi.core.job.annotation.QuartzJob;
import com.mi.core.util.DateTimeUtil;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.event.EventModule;

@QuartzJob(id = "EventRewardSendJob", interval = (int) DateTimeUtil.ONE_MINUTE_TIME_S * 5, count = 9999999)
public class EventRewardSendJob extends BaseJob {

	@Override
	protected void onExecute(JobDataMap contextData, JobExecutionContext context) {
		EventModule eventModule = ModuleManager.getModule(ModuleNames.EventModule, EventModule.class);
		eventModule.sendLegionKingReward();
		eventModule.sendWarGodReward();
		eventModule.sendUpKingReward();
		// eventModule.sendLevelUpReward();
		eventModule.sendTimeLimitRankReward();
		// 定时刷新活动列表
		eventModule.refreshEventConfigTime();
	}

	@Override
	protected JobDataMap setParameter(JobDataMap mapData) {
		return mapData;
	}

}
