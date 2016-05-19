package com.mi.game.module.festival.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.festival.FestivalModule;
import com.mi.game.module.festival.protocol.FestivalPayMoreProtocol;

@HandlerType(type = HandlerIds.FESTIVAL_PAYMORE)
public class FestivalPayMoreHandler extends BaseHandler {

	@Override
	public void execute(IOMessage ioMessage) {
		String playerID = ioMessage.getPlayerId();
		String moreType = (String) ioMessage.getInputParse("moreType");
		FestivalPayMoreProtocol protocol = new FestivalPayMoreProtocol();
		FestivalModule festivalModule = ModuleManager.getModule(ModuleNames.FestivalModule, FestivalModule.class);
		festivalModule.payMore(playerID,moreType, protocol);
		ioMessage.setOutputResult(protocol);
	}
}
