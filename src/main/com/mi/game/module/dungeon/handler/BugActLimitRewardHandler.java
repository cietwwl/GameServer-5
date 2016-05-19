package com.mi.game.module.dungeon.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.dungeon.DungeonModule;
import com.mi.game.module.dungeon.protocol.DungeonProtocol;
@HandlerType(type = HandlerIds.BuyActLimitReward)
public class BugActLimitRewardHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String playerID = ioMessage.getPlayerId();
		int actID = 0;
		int packageID = 0;
		if(ioMessage.getInputParse("actID") != null){
			actID = Integer.parseInt(ioMessage.getInputParse("actID").toString());
		}
		if(ioMessage.getInputParse("packageID") != null){
			packageID = Integer.parseInt(ioMessage.getInputParse("packageID").toString());
		}
//		int payMoney = 0;
//		if(ioMessage.getInputParse("payMoney") != null){
//			payMoney = Integer.parseInt(ioMessage.getInputParse("payMoney").toString());
//		}
		DungeonProtocol protocol = new DungeonProtocol();
		DungeonModule dungeonModule = ModuleManager.getModule(ModuleNames.DungeonModule,DungeonModule.class);
		dungeonModule.buyActLimitReward(playerID, actID,packageID,protocol,ioMessage);
		ioMessage.setProtocol(protocol);
	}
}
