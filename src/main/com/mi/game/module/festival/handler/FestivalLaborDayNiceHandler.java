package com.mi.game.module.festival.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.festival.FestivalModule;
import com.mi.game.module.festival.protocol.FestivalLaborDayNiceProtocol;

@HandlerType(type = HandlerIds.FESTIVAL_LABORDAY_NICE)
public class FestivalLaborDayNiceHandler extends BaseHandler {

	@Override
	public void execute(IOMessage ioMessage) {
		String playerID = ioMessage.getPlayerId();
		// copper为铜板,gold为元宝
		// copper10为铜板10次,gold10 为元宝10次
		String niceType = (String) ioMessage.getInputParse("niceType");
		FestivalLaborDayNiceProtocol protocol = new FestivalLaborDayNiceProtocol();
		FestivalModule festivalModule = ModuleManager.getModule(ModuleNames.FestivalModule, FestivalModule.class);
		festivalModule.laborDayNice(playerID, niceType, protocol, ioMessage);
		ioMessage.setOutputResult(protocol);
	}
}
