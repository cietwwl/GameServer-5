package com.mi.game.module.event.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.event.EventModule;
import com.mi.game.module.event.protocol.EventExpenseProtocol;

@HandlerType(type = HandlerIds.EVENT_EXPENSE)
public class EventExpenseHandler extends BaseHandler {

	@Override
	public void execute(IOMessage ioMessage) {
		String playerID = ioMessage.getPlayerId();
		String rewardID = (String) ioMessage.getInputParse("rewardID");
		EventModule eventModule = ModuleManager.getModule(ModuleNames.EventModule, EventModule.class);
		EventExpenseProtocol protocol = new EventExpenseProtocol();
		eventModule.eventExpense(playerID, rewardID, protocol);
		ioMessage.setOutputResult(protocol);
	}
}
