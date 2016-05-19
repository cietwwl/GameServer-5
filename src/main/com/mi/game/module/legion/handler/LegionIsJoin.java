package com.mi.game.module.legion.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.legion.LegionModule;
import com.mi.game.module.legion.protocol.LegionProtocol;

@HandlerType(type = HandlerIds.LEGION_ISJOIN)
public class LegionIsJoin extends BaseHandler {

	@Override
	public void execute(IOMessage ioMessage) {
		String playerID = ioMessage.getPlayerId();
		LegionModule legionModule = ModuleManager.getModule(ModuleNames.LegionModule, LegionModule.class);
		LegionProtocol protocol = new LegionProtocol();
		protocol.setPlayerID(ioMessage.getPlayerId());
		legionModule.isJoinLegion(playerID, protocol);
		ioMessage.setOutputResult(protocol);
	}

}
