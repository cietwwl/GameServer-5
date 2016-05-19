package com.mi.game.module.arena.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.arena.ArenaModule;
import com.mi.game.module.arena.protocol.ArenaProtocol;
import com.mi.game.module.base.handler.BaseHandler;
@HandlerType(type = HandlerIds.getPkList)
public class GetRankListHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String playerID  = ioMessage.getPlayerId();
		ArenaProtocol protocol = new ArenaProtocol();
		ArenaModule module = ModuleManager.getModule(ModuleNames.ArenaModule,ArenaModule.class);
		module.getAreanInfo(playerID,protocol);
		ioMessage.setProtocol(protocol);
	}
}
