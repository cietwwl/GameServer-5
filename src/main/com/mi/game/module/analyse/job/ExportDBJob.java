package com.mi.game.module.analyse.job;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

import com.mi.core.engine.ModuleManager;
import com.mi.core.job.BaseJob;
import com.mi.core.job.annotation.QuartzJob;
import com.mi.core.util.ConfigUtil;
import com.mi.core.util.DateTimeUtil;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.analyse.AnalyseModule;

@QuartzJob(id = "ExportDBJob", interval = (int) DateTimeUtil.ONE_MINUTE_TIME_S * 15, count = 9999999)
public class ExportDBJob extends BaseJob {

	// 15分钟运行一次
	@Override
	protected void onExecute(JobDataMap contextData, JobExecutionContext context) {
		boolean isExport = ConfigUtil.getBoolean("analyse.export.open", false);
		if (isExport) {
			AnalyseModule analyseModule = ModuleManager.getModule(ModuleNames.AnalyseModule, AnalyseModule.class);
			analyseModule.exportUserDB();
			analyseModule.exportPayDB();
		}
	}

	@Override
	protected JobDataMap setParameter(JobDataMap mapData) {
		return mapData;
	}

}
