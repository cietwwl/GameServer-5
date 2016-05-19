package com.mi.game.module.dungeon.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.dungeon.DungeonModule;
import com.mi.game.module.dungeon.protocol.DungeonProtocol;

@HandlerType(type = HandlerIds.dungeonActiveStart)
public class DungeonActiveHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String playerID = ioMessage.getPlayerId();
		int activeID = 0;
		int damage = 0;
		int stage = 0;
		boolean win = false;
		if(ioMessage.getInputParse("activeID") != null){
			activeID = Integer.parseInt(ioMessage.getInputParse("activeID").toString());
		}
		if(ioMessage.getInputParse("damage") != null){
			damage = Integer.parseInt(ioMessage.getInputParse("damage").toString());
		}
		if(ioMessage.getInputParse("stage") != null){
			stage = Integer.parseInt(ioMessage.getInputParse("stage").toString());
		}
		if(ioMessage.getInputParse("win") != null){
			win = Boolean.parseBoolean(ioMessage.getInputParse("win").toString());
		}
		DungeonProtocol protocol = new DungeonProtocol();
		DungeonModule module  = ModuleManager.getModule(ModuleNames.DungeonModule, DungeonModule.class);
		if(win){
			try{
				module.attackActiveDungeon(playerID, activeID, damage, stage,ioMessage, protocol);
			}catch(Exception ex){
				protocol.setCode(Integer.parseInt(ex.getMessage()));
			}
		}
		ioMessage.setProtocol(protocol);
	}
}
