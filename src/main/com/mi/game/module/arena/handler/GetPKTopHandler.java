package com.mi.game.module.arena.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.arena.ArenaModule;
import com.mi.game.module.arena.protocol.ArenaProtocol;
import com.mi.game.module.base.handler.BaseHandler;
@HandlerType(type = HandlerIds.pkTopList)
public class GetPKTopHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		ArenaProtocol protocol = new ArenaProtocol();
		ArenaModule module = ModuleManager.getModule(ModuleNames.ArenaModule,ArenaModule.class);
		module.getRankTopList(protocol);
		ioMessage.setProtocol(protocol);
	}
}
