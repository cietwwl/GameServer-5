package com.mi.game.module.welfare.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.welfare.WelfareModule;
import com.mi.game.module.welfare.protocol.WelfareAllProtocol;

@HandlerType(type = HandlerIds.WELFARE_ALL)
public class WelfareAllHandler extends BaseHandler {

	@Override
	public void execute(IOMessage ioMessage) {
		String playerID = ioMessage.getPlayerId();
		String welfareType = (String) ioMessage.getInputParse("welfareType");
		WelfareModule welfareModule = ModuleManager.getModule(ModuleNames.WelfareModule, WelfareModule.class);
		WelfareAllProtocol protocol = new WelfareAllProtocol();
		welfareModule.welfareAll(playerID, welfareType, protocol);
		ioMessage.setOutputResult(protocol);
	}
}
