package com.mi.game.module.notice.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.notice.NoticeModule;
import com.mi.game.module.notice.protocol.NoticeProtocol;

@HandlerType(type = HandlerIds.NOTICE_INFO)
public class NoticeHandler extends BaseHandler {

	@Override
	public void execute(IOMessage ioMessage) {
		NoticeModule noticeModule = ModuleManager.getModule(ModuleNames.NoticeModule, NoticeModule.class);
		NoticeProtocol protocol = new NoticeProtocol();
		noticeModule.noticeInfo(protocol);
		ioMessage.setOutputResult(protocol);
	}

}
