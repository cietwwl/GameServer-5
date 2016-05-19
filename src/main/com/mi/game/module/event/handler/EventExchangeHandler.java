package com.mi.game.module.event.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.event.EventModule;
import com.mi.game.module.event.protocol.EventExchangeProtocol;

@HandlerType(type = HandlerIds.EVENT_EXCHANGE)
public class EventExchangeHandler extends BaseHandler {

	@Override
	public void execute(IOMessage ioMessage) {
		String playerID = ioMessage.getPlayerId();
		String exchangeID = (String) ioMessage.getInputParse("exchangeID");
		String exchangeType = (String) ioMessage.getInputParse("exchangeType");
		EventModule eventModule = ModuleManager.getModule(ModuleNames.EventModule, EventModule.class);
		EventExchangeProtocol protocol = new EventExchangeProtocol();
		eventModule.eventExchange(playerID, exchangeID, exchangeType, protocol,ioMessage);
		ioMessage.setOutputResult(protocol);
	}
}
