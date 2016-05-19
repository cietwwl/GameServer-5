package com.mi.game.module.dungeon.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.dungeon.DungeonModule;
import com.mi.game.module.dungeon.pojo.DungeonActMapEntity;
import com.mi.game.module.dungeon.pojo.DungeonEliteEntity;
import com.mi.game.module.dungeon.pojo.DungeonMapEntity;
import com.mi.game.module.dungeon.protocol.DungeonProtocol;
@HandlerType(type = HandlerIds.getNormalDungeonInfo)
public class GetDungeonHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String playerID = ioMessage.getPlayerId();
		DungeonProtocol protocol = new DungeonProtocol();
		DungeonModule module= ModuleManager.getModule(ModuleNames.DungeonModule,DungeonModule.class);
		DungeonActMapEntity dungeonActMapEntity = module.getDungeonActEntity(playerID);
		DungeonMapEntity entity = module.getResponseDungeonMapEntity(playerID);
		DungeonEliteEntity dungeonEliteEntity = module.getResponseEilteEntity(playerID);
		protocol.setDungeonEliteEntity(dungeonEliteEntity);
		protocol.setDungeonMapEntity(entity);
		protocol.setDungeonActMapEntity(dungeonActMapEntity);
		ioMessage.setProtocol(protocol);
	}
}
