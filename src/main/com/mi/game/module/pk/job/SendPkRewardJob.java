package com.mi.game.module.pk.job;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

import com.mi.core.engine.ModuleManager;
import com.mi.core.job.BaseJob;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.pk.PkModule;
import com.mi.game.util.Utilities;

/**
 * 发送比武奖励定时任务
 * 
 * @author 赵鹏翔
 * @time Apr 2, 2015 4:15:58 PM
 */
//@QuartzJob(id = "SendPkRewardJob", interval = (int) DateTimeUtil.ONE_MINUTE_TIME_S * 15, count = 9999999)
public class SendPkRewardJob extends BaseJob {

	@Override
	protected void onExecute(JobDataMap contextData, JobExecutionContext context) {
		boolean flag = Utilities.todayIsSaturday();
		if (flag) { // 是周六
			PkModule pkModule = ModuleManager.getModule(ModuleNames.PkModule,
					PkModule.class);
			pkModule.sendPkRewardNew();
		}
	}

	@Override
	protected JobDataMap setParameter(JobDataMap mapData) {
		return mapData;
	}

}
