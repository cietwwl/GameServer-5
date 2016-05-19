package com.mi.game.module.worldBoss.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.worldBoss.WorldBossModule;
import com.mi.game.module.worldBoss.protocol.WorldBossProtocol;
@HandlerType(type = HandlerIds.getWorldBossShowList)
public class GetWorldBossShowListHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String playerID = ioMessage.getPlayerId();
		long startTime = 0;
		if(ioMessage.getInputParse("startTime") != null){
			startTime = Long.parseLong(ioMessage.getInputParse("startTime").toString());
		}
		WorldBossProtocol protocol = new WorldBossProtocol();
		WorldBossModule worldBossModule = ModuleManager.getModule(ModuleNames.WorldBossModule,WorldBossModule.class);
		try{
			worldBossModule.getAttackShowList(playerID,startTime, protocol,ioMessage);
		}catch(IllegalArgumentException ex){
			protocol.setCode(Integer.parseInt(ex.getMessage()));
		}
		ioMessage.setProtocol(protocol);
	}
}
