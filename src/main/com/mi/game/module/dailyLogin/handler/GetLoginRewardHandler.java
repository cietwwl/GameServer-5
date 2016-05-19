package com.mi.game.module.dailyLogin.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.dailyLogin.DailyLoginModule;
import com.mi.game.module.dailyLogin.protocol.DailyLoginPayProtocol;

/**
 * 领取登录奖励
 * 
 * @author 赵鹏翔
 * @time Apr 7, 2015 6:21:57 PM
 */
@HandlerType(type = HandlerIds.EVENT_DAILY_LOGIN_GET_LOGINREWARD)
public class GetLoginRewardHandler extends BaseHandler {

	@Override
	public void execute(IOMessage ioMessage) {
		String playerID = ioMessage.getPlayerId();
		String pid = "";
		if (ioMessage.getInputParse("pid") != null) {
			pid = ioMessage.getInputParse("pid").toString();
		}
		DailyLoginModule loginModule = ModuleManager.getModule(
				ModuleNames.DailyLoginModule, DailyLoginModule.class);
		DailyLoginPayProtocol protocol = new DailyLoginPayProtocol();
		loginModule.getDailyLoginReward(playerID, pid, protocol, ioMessage);
		ioMessage.setProtocol(protocol);
	}
}
