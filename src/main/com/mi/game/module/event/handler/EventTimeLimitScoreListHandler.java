package com.mi.game.module.event.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.event.EventModule;
import com.mi.game.module.event.protocol.EventTimeLimitProtocol;

/**
 * 限时武将排行榜
 * 
 * @author Administrator
 *
 */
@HandlerType(type = HandlerIds.TimeLimitScoreList)
public class EventTimeLimitScoreListHandler extends BaseHandler {
	@Override
	public void execute(IOMessage ioMessage) {
		String playerID = ioMessage.getPlayerId();
		EventModule eventModule = ModuleManager.getModule(ModuleNames.EventModule, EventModule.class);
		EventTimeLimitProtocol protocol = new EventTimeLimitProtocol();
		eventModule.getEventTimeLimitRank(playerID, protocol);
		ioMessage.setOutputResult(protocol);
	}
}
