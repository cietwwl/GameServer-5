package com.mi.game.module.chat.jobs;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

import com.mi.core.engine.ModuleManager;
import com.mi.core.job.BaseJob;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.chat.ChatModule;

/**
 * job子类,主要实现间隔多久执行一件什么事.
 * 
 * @author Administrator
 */
public class InformationJob extends BaseJob {
	public static final String CURRENT_INDEX = "current_index";

	@SuppressWarnings("unchecked")
	// @Override
	protected void onExecute(JobDataMap contextData, JobExecutionContext arg0) {
		// 获取上下文参数
		List<String> messages = (List<String>) contextData.get(BaseJob.USER_PARAMETER);
		int currentIndex = contextData.getInt(CURRENT_INDEX);
		// 逻辑
		if (messages != null && messages.size() > 0) {
			String content = messages.get(currentIndex);
			currentIndex++;
			if (currentIndex >= messages.size())
				currentIndex = 0;
			// 添加聊天内容
			ChatModule module = ModuleManager.getModule(ModuleNames.ChatModule, ChatModule.class);
			if (StringUtils.isNotEmpty(content)) {
				module.sendInformationMessage(content);
			}

			// 放置上下文参数
			contextData.put(CURRENT_INDEX, currentIndex);
		}
	}

	@Override
	protected JobDataMap setParameter(JobDataMap mapData) {
		// TODO 自动生成的方法存根
		mapData.put(CURRENT_INDEX, 0);
		return mapData;
	}
}
