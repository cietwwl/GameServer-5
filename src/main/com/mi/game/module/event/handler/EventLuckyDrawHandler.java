package com.mi.game.module.event.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.event.EventModule;
import com.mi.game.module.event.protocol.EventLuckyDrawProtocol;

@HandlerType(type = HandlerIds.EVENT_LUCKY_DRAW)
public class EventLuckyDrawHandler extends BaseHandler {

	@Override
	public void execute(IOMessage ioMessage) {
		String playerID = ioMessage.getPlayerId();				
		int type=Integer.valueOf(ioMessage.getInputParse("useType").toString());
		EventModule eventModule = ModuleManager.getModule(ModuleNames.EventModule, EventModule.class);
		EventLuckyDrawProtocol protocol = new EventLuckyDrawProtocol();
		eventModule.eventLuckyDraw(playerID, type, protocol);
		ioMessage.setOutputResult(protocol);
	}
}
