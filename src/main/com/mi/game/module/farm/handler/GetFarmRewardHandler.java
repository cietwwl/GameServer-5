package com.mi.game.module.farm.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.farm.FarmModule;
import com.mi.game.module.farm.protocol.FarmProtocol;
@HandlerType(type = HandlerIds.getFarmReward)
public class GetFarmRewardHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String playerID = ioMessage.getPlayerId();
		int getType = 0;
		if(ioMessage.getInputParse("getType") != null){
			getType = Integer.parseInt(ioMessage.getInputParse("getType").toString());
		}
		FarmProtocol protocol = new FarmProtocol();
		FarmModule farmModule = ModuleManager.getModule(ModuleNames.FarmModule,FarmModule.class);
		farmModule.getFarmRewad(playerID,getType, ioMessage, protocol);
		ioMessage.setProtocol(protocol);
	}
}
