package com.mi.game.module.chat.jobs;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

import com.mi.core.engine.ModuleManager;
import com.mi.core.job.BaseJob;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.chat.ChatModule;
import com.mi.game.module.chat.define.EnumChannelType;
import com.mi.game.module.chat.define.EnumMessageType;

public class HideJob extends BaseJob {

	@Override
	protected void onExecute(JobDataMap contextData, JobExecutionContext arg0) {
		@SuppressWarnings("unchecked")
		// 获取上下文参数
		List<String> messages = (List<String>) contextData.get(BaseJob.USER_PARAMETER);

		// 逻辑
		String content = messages.get(0);
		// Logs.logger.error("job execute content : " + content);

		// 添加聊天内容
		ChatModule module = ModuleManager.getModule(ModuleNames.ChatModule, ChatModule.class);
		if (StringUtils.isNotEmpty(content)) {
			module.sendInformationMessage(EnumChannelType.Hide, EnumMessageType.Infomation, content);
		}

	}

	@Override
	protected JobDataMap setParameter(JobDataMap mapData) {

		return mapData;
	}
}
