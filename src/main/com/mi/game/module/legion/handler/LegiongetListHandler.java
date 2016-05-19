package com.mi.game.module.legion.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.legion.LegionModule;
import com.mi.game.module.legion.protocol.LegionProtocol;

@HandlerType(type = HandlerIds.LEGION_GETLIST)
public class LegiongetListHandler extends BaseHandler {

	@Override
	public void execute(IOMessage ioMessage) {
		String playerID = ioMessage.getPlayerId();
		String page = (String) ioMessage.getInputParse("page");
		String name = (String) ioMessage.getInputParse("name");
		LegionModule legionModule = ModuleManager.getModule(ModuleNames.LegionModule, LegionModule.class);
		LegionProtocol protocol = new LegionProtocol();
		protocol.setPlayerID(ioMessage.getPlayerId());
		legionModule.getListLegion(playerID, page, name, protocol);
		ioMessage.setOutputResult(protocol);
	}
}
