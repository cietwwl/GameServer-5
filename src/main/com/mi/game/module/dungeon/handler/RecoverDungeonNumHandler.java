package com.mi.game.module.dungeon.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.dungeon.DungeonModule;
import com.mi.game.module.dungeon.protocol.DungeonProtocol;
@HandlerType(type = HandlerIds.recoverDungeonNum)
public class RecoverDungeonNumHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String playerID = ioMessage.getPlayerId();
		int gameLevelID = 0;
		int recoverType = 0;
		if(ioMessage.getInputParse("gameLevelID") != null){
			gameLevelID = Integer.parseInt(ioMessage.getInputParse("gameLevelID").toString());
		}
		if(ioMessage.getInputParse("recoverType") != null){
			recoverType = Integer.parseInt(ioMessage.getInputParse("recoverType").toString());
		}
		DungeonProtocol protocol = new DungeonProtocol();
		DungeonModule module = ModuleManager.getModule(ModuleNames.DungeonModule, DungeonModule.class);
		try{
			module.recoverAttackNum(playerID, gameLevelID, recoverType, ioMessage, protocol);
		}catch(Exception ex){
			protocol.setCode(Integer.parseInt(ex.getMessage()));
		}
		ioMessage.setProtocol(protocol);
	}
}
