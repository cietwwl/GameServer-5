package com.mi.game.module.worldBoss.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.worldBoss.WorldBossModule;
import com.mi.game.module.worldBoss.protocol.WorldBossProtocol;
@HandlerType(type = HandlerIds.worldBossRevive)
public class WorldBossReviveHandler  extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String playerID = ioMessage.getPlayerId();
		WorldBossProtocol protocol = new WorldBossProtocol();
		WorldBossModule module = ModuleManager.getModule(ModuleNames.WorldBossModule, WorldBossModule.class);
		try{
			module.revive(playerID, protocol,ioMessage);
		}catch(IllegalArgumentException ex){
			protocol.setCode(Integer.parseInt(ex.getMessage()));
		}
		ioMessage.setProtocol(protocol);
	}
}
