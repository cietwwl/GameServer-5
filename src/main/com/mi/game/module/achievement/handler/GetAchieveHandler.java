package com.mi.game.module.achievement.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.achievement.AchievementModule;
import com.mi.game.module.achievement.pojo.AchievementEntity;
import com.mi.game.module.achievement.protocol.AchievementProtocol;
import com.mi.game.module.base.handler.BaseHandler;
@HandlerType(type = HandlerIds.getAchievementInfo)
public class GetAchieveHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String playerID = ioMessage.getPlayerId();
		AchievementModule module = ModuleManager.getModule(ModuleNames.AchievementModule,AchievementModule.class);
		AchievementEntity entity = module.getEntity(playerID);
		AchievementProtocol protocol = new AchievementProtocol();
		protocol.setAchievementEntity(entity);
		ioMessage.setProtocol(protocol);
	}
}
