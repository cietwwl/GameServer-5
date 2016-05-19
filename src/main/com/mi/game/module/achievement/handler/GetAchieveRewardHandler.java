package com.mi.game.module.achievement.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.achievement.AchievementModule;
import com.mi.game.module.achievement.protocol.AchievementProtocol;
import com.mi.game.module.base.handler.BaseHandler;
@HandlerType(type = HandlerIds.getAchievementReward)
public class GetAchieveRewardHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String playerID = ioMessage.getPlayerId();
		int pid = 0;
		if(ioMessage.getInputParse("pid") != null){
			pid = Integer.parseInt(ioMessage.getInputParse("pid").toString());
		}
		AchievementModule achievementModule = ModuleManager.getModule(ModuleNames.AchievementModule,AchievementModule.class);
		AchievementProtocol protocol = new AchievementProtocol();
		achievementModule.getAchievementReward(playerID, pid,protocol);
		ioMessage.setProtocol(protocol);
	}
}
