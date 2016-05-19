package com.mi.game.module.event.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.event.EventModule;
import com.mi.game.module.event.protocol.EventPayEveryDayProtocol;

/**
 * 每日充值活动,领取奖励
 * 
 * @author 赵鹏翔
 * @time Apr 7, 2015 6:21:57 PM
 */
@HandlerType(type = HandlerIds.EVENT_PAY_EVERY_DAY_GET_REWARD)
public class EventPayEveryDayGetRewardHandler extends BaseHandler {

	@Override
	public void execute(IOMessage ioMessage) {
		String playerID = ioMessage.getPlayerId();
		String pid = "";
		if (ioMessage.getInputParse("pid") != null) {
			pid = ioMessage.getInputParse("pid").toString();
		}
		EventModule eventModule = ModuleManager.getModule(
				ModuleNames.EventModule, EventModule.class);
		// eventModule.intefaceEventPayEveryDay(playerID, 100); //充值接口测试
		EventPayEveryDayProtocol protocol = new EventPayEveryDayProtocol();
		eventModule.eventPayEveryDay(playerID, pid, protocol, ioMessage);
	}
}
