package com.mi.game.module.event.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.event.EventModule;
import com.mi.game.module.event.protocol.EventExploreProtocol;

@HandlerType(type = HandlerIds.EVENT_EXPLORE)
public class EventExploreHandler extends BaseHandler {

	@Override
	public void execute(IOMessage ioMessage) {
		String playerID = ioMessage.getPlayerId();
		String exploreType = (String) ioMessage.getInputParse("exploreType");
		EventModule eventModule = ModuleManager.getModule(ModuleNames.EventModule, EventModule.class);
		EventExploreProtocol protocol = new EventExploreProtocol();
		eventModule.eventExplore(playerID, exploreType, protocol);
		ioMessage.setOutputResult(protocol);
	}
}
