package com.mi.game.module.event.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.event.EventModule;
import com.mi.game.module.event.protocol.EventVipGrowProtocol;

@HandlerType(type = HandlerIds.EVENT_GROW)
public class EventVipGrowHandler extends BaseHandler {
	@Override
	public void execute(IOMessage ioMessage) {
		String playerID = ioMessage.getPlayerId();
		String growType = (String) ioMessage.getInputParse("growType");
		String rewardID = (String) ioMessage.getInputParse("rewardID");
		EventModule eventModule = ModuleManager.getModule(ModuleNames.EventModule, EventModule.class);
		EventVipGrowProtocol protocol = new EventVipGrowProtocol();
		eventModule.eventVipGrow(playerID, growType, rewardID, protocol);
		ioMessage.setOutputResult(protocol);
	}
}
