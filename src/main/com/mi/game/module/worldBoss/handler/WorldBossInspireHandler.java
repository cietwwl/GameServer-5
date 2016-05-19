package com.mi.game.module.worldBoss.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.worldBoss.WorldBossModule;
import com.mi.game.module.worldBoss.protocol.WorldBossProtocol;
@HandlerType(type = HandlerIds.inspire)
public class WorldBossInspireHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String playerID = ioMessage.getPlayerId();
		int inspireType = 0;
		if(ioMessage.getInputParse("inspireType") != null){
			inspireType = Integer.parseInt(ioMessage.getInputParse("inspireType").toString());
		}
		WorldBossModule module = ModuleManager.getModule(ModuleNames.WorldBossModule,WorldBossModule.class);
		WorldBossProtocol protocol = new WorldBossProtocol();
		try{
			module.inspire(playerID, inspireType, protocol,ioMessage);
		}catch(IllegalArgumentException ex){
			protocol.setCode(Integer.parseInt(ex.getMessage()));
		}
		ioMessage.setProtocol(protocol);
	}
}
