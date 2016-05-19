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
 * 每日充值活动,初始化,获取奖励模版以及已领取奖励列表
 * 
 * @author 赵鹏翔
 * @time Apr 7, 2015 6:21:57 PM
 */
@HandlerType(type = HandlerIds.EVENT_PAY_EVERY_DAY_INIT)
public class EventPayEveryDayInitHandler extends BaseHandler {

	@Override
	public void execute(IOMessage ioMessage) {
		String playerID = ioMessage.getPlayerId();
		String rewardID = (String) ioMessage.getInputParse("rewardID");
		EventModule eventModule = ModuleManager.getModule(
				ModuleNames.EventModule, EventModule.class);
		EventPayEveryDayProtocol protocol = new EventPayEveryDayProtocol();
		eventModule.payEveryDayInfoInit(playerID, protocol);
		ioMessage.setProtocol(protocol);
		ioMessage.setOutputResult(protocol);
		
	}
}
