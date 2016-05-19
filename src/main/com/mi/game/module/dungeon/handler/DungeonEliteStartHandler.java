package com.mi.game.module.dungeon.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.dungeon.DungeonModule;
import com.mi.game.module.dungeon.protocol.DungeonProtocol;

@HandlerType(type = HandlerIds.dungeonEliteStart)
public class DungeonEliteStartHandler  extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String playerID = ioMessage.getPlayerId();
		int eliteID = 0;
		if(ioMessage.getInputParse("eliteID") != null){
			eliteID = Integer.parseInt(ioMessage.getInputParse("eliteID") .toString());
		}
		boolean win = false;
		if(ioMessage.getInputParse("win") != null){
			win = Boolean.parseBoolean(ioMessage.getInputParse("win") .toString());
		}
		int stage = 0;
		if(ioMessage.getInputParse("stage") != null){
			stage = Integer.parseInt(ioMessage.getInputParse("stage") .toString());
		}
		DungeonProtocol protocol = new DungeonProtocol();
		DungeonModule module = ModuleManager.getModule(ModuleNames.DungeonModule,DungeonModule.class);
		try{
			module.attackEliteDungeon(playerID, eliteID, win, stage, protocol,ioMessage);
		}catch(IllegalArgumentException ex){
			protocol.setCode(Integer.parseInt(ex.getMessage()));
		}
	
		ioMessage.setProtocol(protocol);
	}
}
