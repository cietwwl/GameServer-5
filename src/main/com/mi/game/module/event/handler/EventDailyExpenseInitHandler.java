package com.mi.game.module.event.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.event.EventModule;
import com.mi.game.module.event.protocol.EventDailyExpenseProtocol;

/**
 * 每日消费,查询奖励模版列表以及已领取奖励
 * 
 * @author 赵鹏翔
 * @time Apr 10, 2015 4:50:00 PM
 */
@HandlerType(type = HandlerIds.EVENT_EXPENSE_DAILY_INIT)
public class EventDailyExpenseInitHandler extends BaseHandler {

	@Override
	public void execute(IOMessage ioMessage) {
		String playerID = ioMessage.getPlayerId();
		EventModule eventModule = ModuleManager.getModule(ModuleNames.EventModule, EventModule.class);
		EventDailyExpenseProtocol protocol = new EventDailyExpenseProtocol();
		eventModule.dailyExpenseRewardInit(playerID, protocol);
		ioMessage.setProtocol(protocol);
	}
}
