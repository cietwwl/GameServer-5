package com.mi.game.module.event.job;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

import com.mi.core.engine.ModuleManager;
import com.mi.core.job.BaseJob;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.event.EventModule;

public class SendLevelUpRewardJob extends BaseJob {

	@Override
	protected void onExecute(JobDataMap contextData, JobExecutionContext context) {
		EventModule eventModule = ModuleManager.getModule(ModuleNames.EventModule, EventModule.class);
		eventModule.sendLevelUpRewardNew();
	}

	@Override
	protected JobDataMap setParameter(JobDataMap mapData) {
		return mapData;
	}

}
