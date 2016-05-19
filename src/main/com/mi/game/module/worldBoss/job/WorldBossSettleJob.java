package com.mi.game.module.worldBoss.job;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

import com.mi.core.engine.ModuleManager;
import com.mi.core.job.BaseJob;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.worldBoss.WorldBossModule;

public class WorldBossSettleJob extends BaseJob{

	@Override
	protected JobDataMap setParameter(JobDataMap mapData) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	protected void onExecute(JobDataMap contextData, JobExecutionContext arg0) {
		// TODO 自动生成的方法存根
		WorldBossModule worldBossModule = ModuleManager.getModule(ModuleNames.WorldBossModule,WorldBossModule.class);
		worldBossModule.settleWorldBoss(false);
	}

}
