package com.mi.game.module.reward.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.reward.RewardModule;
import com.mi.game.module.reward.pojo.RewardCenterEntity;
import com.mi.game.module.reward.protocol.RewardProtocol;
@HandlerType(type = HandlerIds.getRewardCenterInfo)
public class GetRwardCenterInfoHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String playerID = ioMessage.getPlayerId();
		RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule,RewardModule.class);
		RewardProtocol protocol = new RewardProtocol();
		try{
			RewardCenterEntity entity = rewardModule.getRewardCenterEntity(playerID);
			protocol.setEntity(entity);
		}catch(Exception ex){
			protocol.setCode(Integer.parseInt(ex.getMessage()));
		}	
		ioMessage.setProtocol(protocol);
	}
}
