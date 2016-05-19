package com.mi.game.module.event.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.event.EventModule;
import com.mi.game.module.event.protocol.EventDrawIntegralProtocol;

@HandlerType(type = HandlerIds.EVENT_DRAW_INTEGRAL)
public class EventDrawIntegralHandler extends BaseHandler {

	@Override
	public void execute(IOMessage ioMessage) {
		String playerID = ioMessage.getPlayerId();
		EventModule eventModule = ModuleManager.getModule(ModuleNames.EventModule, EventModule.class);
		EventDrawIntegralProtocol protocol = new EventDrawIntegralProtocol();
		eventModule.eventDrawIntegral(playerID, protocol);
		ioMessage.setOutputResult(protocol);
	}
}
