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
 * 每日登录,充值,查询活动奖励以及已领取奖励列表
 * 
 * @author 赵鹏翔
 * @time Apr 7, 2015 6:21:57 PM
 */
@HandlerType(type = HandlerIds.EVENT_DAILY_LOGIN_PAY_INIT)
public class DailyLoginPayInitHandler extends BaseHandler {

	@Override
	public void execute(IOMessage ioMessage) {
		String playerID = ioMessage.getPlayerId();
		String rewardID = (String) ioMessage.getInputParse("rewardID");
		DailyLoginModule loginModule = ModuleManager.getModule(
				ModuleNames.DailyLoginModule, DailyLoginModule.class);
		DailyLoginPayProtocol protocol = new DailyLoginPayProtocol();
		loginModule.activeLoginPayInit(playerID, protocol);
		ioMessage.setProtocol(protocol);
	}
}
