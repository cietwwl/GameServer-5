package com.mi.game.module.festival.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.festival.FestivalModule;
import com.mi.game.module.festival.protocol.FestivalFirecrackerProtocol;

@HandlerType(type = HandlerIds.FESTIVAL_FIRECRACKER)
public class FestivalFirecrackerHandler extends BaseHandler {

	@Override
	public void execute(IOMessage ioMessage) {
		String playerID = ioMessage.getPlayerId();
		// copper为铜板放鞭炮,gold为元宝放鞭炮
		// copper10为铜板10连放,gold10 为元宝10连放
		String fireType = (String) ioMessage.getInputParse("fireType");
		FestivalFirecrackerProtocol protocol = new FestivalFirecrackerProtocol();
		FestivalModule festivalModule = ModuleManager.getModule(ModuleNames.FestivalModule, FestivalModule.class);
		festivalModule.firecracker(playerID, fireType, protocol,ioMessage);
		ioMessage.setOutputResult(protocol);
	}
}
