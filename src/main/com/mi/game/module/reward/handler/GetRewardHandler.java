package com.mi.game.module.reward.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.reward.RewardModule;
import com.mi.game.module.reward.protocol.RewardProtocol;
@HandlerType (type = HandlerIds.getRewardCenterRewrard)
public class GetRewardHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		int getRewardType = 0;
		String playerID = ioMessage.getPlayerId();
		RewardProtocol protocol = new RewardProtocol();
		if(ioMessage.getInputParse("getRewardType") != null){
			getRewardType = Integer.parseInt(ioMessage.getInputParse("getRewardType").toString());
		}
		RewardModule module = ModuleManager.getModule(ModuleNames.RewardModule,RewardModule.class);
		if(getRewardType != 0){
				long rewardID = 0;
				if(ioMessage.getInputParse("rewardID") != null){
					rewardID = Long.parseLong(ioMessage.getInputParse("rewardID").toString());
				}
				module.getReward(playerID, rewardID, protocol);
		}else{
			module.getAllReward(playerID, protocol);
		}
		ioMessage.setProtocol(protocol);
	}
}
