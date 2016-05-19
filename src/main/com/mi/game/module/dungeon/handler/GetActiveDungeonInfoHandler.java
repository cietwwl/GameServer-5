package com.mi.game.module.dungeon.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.dungeon.DungeonModule;
import com.mi.game.module.dungeon.pojo.DungeonActiveEntity;
import com.mi.game.module.dungeon.protocol.DungeonProtocol;

@HandlerType(type = HandlerIds.GetActiveDungeonInfoHandler)
public class GetActiveDungeonInfoHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String playerID = ioMessage.getPlayerId();
		DungeonModule dungeonModule = ModuleManager.getModule(ModuleNames.DungeonModule,DungeonModule.class);
		DungeonProtocol protocol = new DungeonProtocol();
		try{
			DungeonActiveEntity entity = dungeonModule.getResponseActiveEntity(playerID);
			protocol.setDungeonActiveEntity(entity);
		}catch(IllegalArgumentException ex){
			protocol.setCode(Integer.parseInt(ex.getMessage()));
		}
		ioMessage.setProtocol(protocol);
	}
}
