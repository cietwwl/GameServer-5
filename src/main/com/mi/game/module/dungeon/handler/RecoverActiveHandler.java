package com.mi.game.module.dungeon.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.dungeon.DungeonModule;
import com.mi.game.module.dungeon.protocol.DungeonProtocol;

@HandlerType(type = HandlerIds.dungeonActiveRecover)
public class RecoverActiveHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String playerID = ioMessage.getPlayerId();
		int type = 0;
		if(ioMessage.getInputParse("payType") != null){
			type = Integer.parseInt(ioMessage.getInputParse("payType").toString());
		}
		int activeID = 0;
		if(ioMessage.getInputParse("activeID") != null){
			activeID = Integer.parseInt(ioMessage.getInputParse("activeID").toString());
		}
		DungeonProtocol protocol = new DungeonProtocol();
		DungeonModule module = ModuleManager.getModule(ModuleNames.DungeonModule,DungeonModule.class);
		try{
			module.recoverActiveTimes(playerID, activeID, type, ioMessage, protocol);
		}catch(Exception ex){
			protocol.setCode(Integer.parseInt(ex.getMessage()));
		}
		ioMessage.setProtocol(protocol);
	}
	
}
