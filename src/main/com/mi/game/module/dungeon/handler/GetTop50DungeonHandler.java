package com.mi.game.module.dungeon.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.dungeon.DungeonModule;
import com.mi.game.module.dungeon.protocol.DungeonProtocol;
@HandlerType(type = HandlerIds.getDungeonTopList)
public class GetTop50DungeonHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		DungeonProtocol protocol = new DungeonProtocol();
		DungeonModule dungeonModule = ModuleManager.getModule(ModuleNames.DungeonModule,DungeonModule.class);
		try{
			dungeonModule.getTop50List(protocol);
		}catch(IllegalArgumentException ex){
			protocol.setCode(Integer.parseInt(ex.getMessage()));
		}
		ioMessage.setProtocol(protocol);
	}
	
}
