package com.mi.game.module.chat.jobs;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mi.core.engine.ModuleManager;
import com.mi.core.job.BaseJob;
import com.mi.core.util.DateTimeUtil;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.chat.ChatModule;

public class ClearMessageJob extends BaseJob{
	private Logger logger = LoggerFactory.getLogger(ClearMessageJob.class);
	
	@Override
	protected JobDataMap setParameter(JobDataMap mapData) {
		return mapData;
	}

	@Override
	protected void onExecute(JobDataMap contextData, JobExecutionContext arg0) {
		logger.debug("start Clear");
		ChatModule module = ModuleManager.getModule(ModuleNames.ChatModule,ChatModule.class);
		module.clearMessage(DateTimeUtil.getMillTime());
		logger.debug("end Clear");
	}

}
