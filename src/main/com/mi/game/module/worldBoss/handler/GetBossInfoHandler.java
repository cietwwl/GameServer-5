package com.mi.game.module.worldBoss.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.worldBoss.WorldBossModule;
import com.mi.game.module.worldBoss.protocol.WorldBossProtocol;
@HandlerType(type = HandlerIds.getBossInfo)
public class GetBossInfoHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String playerID = ioMessage.getPlayerId();
		WorldBossProtocol protocol = new WorldBossProtocol();
		WorldBossModule worldBossModule = ModuleManager.getModule(ModuleNames.WorldBossModule,WorldBossModule.class);
		worldBossModule.getBossInfo(playerID, protocol);
		ioMessage.setProtocol(protocol);
	}
}
