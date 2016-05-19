package com.mi.game.module.dungeon.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.dungeon.DungeonModule;
import com.mi.game.module.dungeon.protocol.DungeonProtocol;

@HandlerType(type = HandlerIds.dungeonStart)
public class DungeonStartHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage) {
		DungeonModule dungeonModule = ModuleManager.getModule(ModuleNames.DungeonModule, DungeonModule.class);
		String playerID = (String) ioMessage.getInputParse("playerID");
		int gameLevelID = 0;
		int starLevel  = 0;
		boolean win = false;
		int stage = 1;
		if(ioMessage.getInputParse("gameLevelID") != null){
			gameLevelID = Integer.parseInt(ioMessage.getInputParse("gameLevelID").toString());
		}
		if(ioMessage.getInputParse("starLevel") != null){
			starLevel = Integer.parseInt(ioMessage.getInputParse("starLevel").toString());
		}
		if(ioMessage.getInputParse("win") != null){
			win = Boolean.parseBoolean(ioMessage.getInputParse("win").toString());
		}
		if(ioMessage.getInputParse("stage") != null){
			stage = Integer.parseInt(ioMessage.getInputParse("stage").toString());
		}
		DungeonProtocol protocol = new DungeonProtocol();
		try{
			dungeonModule.DungeonStart(playerID, gameLevelID, starLevel, stage,win, protocol,ioMessage);
		}catch(IllegalArgumentException ex){
			protocol.setCode(Integer.parseInt(ex.getMessage()));
		}
		ioMessage.setProtocol(protocol);
	}
}
