package com.mi.game.module.dungeon.handler;

import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.dungeon.DungeonModule;
import com.mi.game.module.dungeon.protocol.DungeonProtocol;
@HandlerType(type = HandlerIds.ContinuousFight)
public class ContinuousFightHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		int gameLevelID = 0;
		int starLevel = 0;
		if(ioMessage.getInputParse("gameLevelID") != null){
			gameLevelID = Integer.parseInt(ioMessage.getInputParse("gameLevelID").toString());
		}
		if(ioMessage.getInputParse("starLevel") != null){
			starLevel = Integer.parseInt(ioMessage.getInputParse("starLevel").toString());
		}
		DungeonProtocol protocol = new DungeonProtocol();
		String playerID = ioMessage.getPlayerId();
		DungeonModule dungeonModule = ModuleManager.getModule(ModuleNames.DungeonModule,DungeonModule.class);
		try{
			dungeonModule.ContinuousFight(playerID, gameLevelID, starLevel, ioMessage, protocol);
		}catch(IllegalArgumentException ex){
			protocol.setCode(Integer.parseInt(ex.getMessage()));
		}
		
		ioMessage.setProtocol(protocol);
	}
}
