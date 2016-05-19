package com.mi.game.module.admin.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.abstrac.ResponseResult;
import com.mi.game.module.admin.protocol.BaseAdminProtocol;
import com.mi.game.module.analyse.AnalyseModule;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.dailyLogin.DailyLoginModule;
import com.mi.game.module.event.EventModule;
import com.mi.game.module.notice.NoticeModule;

@HandlerType(type = HandlerIds.confdataResetCMD, order = 2)
public class RefreshCacheHandler extends BaseHandler {
	@Override
	public void execute(IOMessage ioMessage) {
		BaseAdminProtocol protocol = new BaseAdminProtocol();
		String confdata = (String) ioMessage.getInputParse("confdata");
		if (confdata.equals(ModuleNames.EventModule)) {
			EventModule eventModule = ModuleManager.getModule(confdata, EventModule.class);
			eventModule.refreshEventConfig();

			DailyLoginModule loginModule = ModuleManager.getModule(
					ModuleNames.DailyLoginModule,
					DailyLoginModule.class);
			loginModule.refreshEventConfig();

			protocol.put("result", ResponseResult.OK);
			protocol.put("code", 1);
		}
		if (confdata.equals(ModuleNames.NoticeModule)) {
			NoticeModule noticeModule = ModuleManager.getModule(confdata, NoticeModule.class);
			noticeModule.refreshNotice();
			protocol.put("result", ResponseResult.OK);
			protocol.put("code", 1);
		}
		if (confdata.equals(ModuleNames.AnalyseModule)) {
			AnalyseModule analyseModule = ModuleManager.getModule(ModuleNames.AnalyseModule, AnalyseModule.class);
			String exportType = (String) ioMessage.getInputParse("exportType");
			if ("user".equals(exportType)) {
				analyseModule.manageExportUserDB();
			}
			if ("pay".equals(exportType)) {
				analyseModule.manageExportPayDB();
			}
			protocol.put("result", ResponseResult.OK);
			protocol.put("code", 1);
		}
		ioMessage.setOutputResult(protocol);
	}

}
