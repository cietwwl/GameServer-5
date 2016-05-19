package com.mi.game.module.dungeon.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.dungeon.DungeonModule;
import com.mi.game.module.dungeon.protocol.DungeonProtocol;
@HandlerType(type = HandlerIds.ResurgenceHero)
public class ResurgenceHeroHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String playerID = ioMessage.getPlayerId();
		int num = 0;
		if(ioMessage.getInputParse("num") != null){
			num = Integer.parseInt(ioMessage.getInputParse("num").toString());
		}
		DungeonProtocol protocol = new DungeonProtocol();
		DungeonModule module = ModuleManager.getModule(ModuleNames.DungeonModule,DungeonModule.class);
		module.resurgenceHero(playerID, num, ioMessage, protocol);
		ioMessage.setProtocol(protocol);
	}
}
