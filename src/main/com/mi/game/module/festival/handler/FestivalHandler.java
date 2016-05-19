package com.mi.game.module.festival.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.festival.FestivalModule;
import com.mi.game.module.festival.protocol.FestivalProtocol;

@HandlerType(type = HandlerIds.FESTIVAL_ACTIVE)
public class FestivalHandler extends BaseHandler {

	@Override
	public void execute(IOMessage ioMessage) {
		String playerID = ioMessage.getPlayerId();
		FestivalProtocol protocol = new FestivalProtocol();
		FestivalModule festivalModule = ModuleManager.getModule(ModuleNames.FestivalModule, FestivalModule.class);
		festivalModule.festival(playerID, protocol);
		ioMessage.setOutputResult(protocol);
	}
}
