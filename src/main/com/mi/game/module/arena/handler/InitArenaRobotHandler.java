package com.mi.game.module.arena.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.core.protocol.BaseProtocol;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.arena.ArenaModule;
import com.mi.game.module.base.handler.BaseHandler;
@HandlerType(type = HandlerIds.initArenaRobot)
public class InitArenaRobotHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		ArenaModule arenaModule = ModuleManager.getModule(ModuleNames.ArenaModule,ArenaModule.class);
		arenaModule.initArenaRobotList();
		ioMessage.setProtocol(new BaseProtocol());
	}
}
