package com.mi.game.module.event.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.event.EventModule;
import com.mi.game.module.event.protocol.EventTraderProtocol;

@HandlerType(type = HandlerIds.EVENT_TRADER)
public class EventTraderHandler extends BaseHandler {

	@Override
	public void execute(IOMessage ioMessage) {
		String playerID = ioMessage.getPlayerId();
		String traderType = (String) ioMessage.getInputParse("traderType");
		String itemID = (String) ioMessage.getInputParse("itemID");
		EventModule eventModule = ModuleManager.getModule(ModuleNames.EventModule, EventModule.class);
		EventTraderProtocol protocol = new EventTraderProtocol();
		eventModule.eventTrader(playerID, traderType, itemID, protocol);
		ioMessage.setOutputResult(protocol);
	}
}
