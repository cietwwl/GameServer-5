package com.mi.game.module.dungeon.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.dungeon.DungeonModule;
import com.mi.game.module.dungeon.protocol.DungeonProtocol;
@HandlerType(type = HandlerIds.getActReward)
public class GetActRewardHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String playerID = ioMessage.getPlayerId();
		int actID = 0;
		int point = 0;
		if(ioMessage.getInputParse("actID") != null){
			actID = Integer.parseInt(ioMessage.getInputParse("actID").toString());
		}
		if(ioMessage.getInputParse("point") != null){
			point = Integer.parseInt(ioMessage.getInputParse("point").toString());
		}
		DungeonProtocol protocol = new DungeonProtocol();
		DungeonModule module = ModuleManager.getModule(ModuleNames.DungeonModule,DungeonModule.class);
		module.getActReward(playerID, actID, point, protocol,ioMessage);
		ioMessage.setProtocol(protocol);
	}
}
