package com.mi.game.module.event.job;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

import com.mi.core.engine.ModuleManager;
import com.mi.core.job.BaseJob;
import com.mi.core.job.annotation.QuartzJob;
import com.mi.core.util.DateTimeUtil;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.event.EventModule;

@QuartzJob(id="EventDayJob",count=99999999,interval=(int)DateTimeUtil.ONE_DAY_TIME_S,startTime="00:00:00")
public class EventDayJob extends BaseJob {

	@Override
	protected JobDataMap setParameter(JobDataMap mapData) {
		// TODO Auto-generated method stub
		return mapData;
	}

	@Override
	protected void onExecute(JobDataMap contextData, JobExecutionContext arg0) {
		//定时刷新任务
		EventModule eventModule = ModuleManager.getModule(ModuleNames.EventModule, EventModule.class);
		eventModule.jobFortunaGiftsEntity();
		eventModule.jobLuckyDrawEntity();
		eventModule.jobUniversalVerfareEntity();
	}

}
