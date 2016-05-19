package com.mi.game.module.event.job;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

import com.mi.core.engine.ModuleManager;
import com.mi.core.job.BaseJob;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.event.EventModule;

/**
 * 发送每日充值昨天未领取的奖励
 * 
 * @author 赵鹏翔
 * @time Apr 2, 2015 4:15:58 PM
 */
public class SendEveryDayPayRewardJob extends BaseJob {

	@Override
	protected void onExecute(JobDataMap contextData, JobExecutionContext context) {
		EventModule eventModule = ModuleManager.getModule(
				ModuleNames.EventModule, EventModule.class);
		// 发送每日充值未领取奖励
		eventModule.sendPayEveryDayRewardForAll();
	}

	@Override
	protected JobDataMap setParameter(JobDataMap mapData) {
		return mapData;
	}

}
