package com.mi.game.module.festival.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.festival.FestivalModule;
import com.mi.game.module.festival.protocol.FestivalLaborDayPayMoreProtocol;

@HandlerType(type = HandlerIds.FESTIVAL_LABORDAY_PAYMORE)
public class FestivalLaborDayPayMoreHandler extends BaseHandler {

	@Override
	public void execute(IOMessage ioMessage) {
		String playerID = ioMessage.getPlayerId();
		String moreType = (String) ioMessage.getInputParse("moreType");
		FestivalLaborDayPayMoreProtocol protocol = new FestivalLaborDayPayMoreProtocol();
		FestivalModule festivalModule = ModuleManager.getModule(ModuleNames.FestivalModule, FestivalModule.class);
		festivalModule.laborDaypayMore(playerID, moreType, protocol);
		ioMessage.setOutputResult(protocol);
	}
}
