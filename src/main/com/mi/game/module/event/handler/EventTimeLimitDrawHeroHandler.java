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
 * 限时武将抽取武将
 * 
 * @author Administrator
 *
 */
@HandlerType(type = HandlerIds.TimeLimitDrawHero)
public class EventTimeLimitDrawHeroHandler extends BaseHandler {
	@Override
	public void execute(IOMessage ioMessage) {
		EventTimeLimitProtocol protocol = new EventTimeLimitProtocol();
		ioMessage.setProtocol(protocol);
		String playerID = ioMessage.getPlayerId();
		int drawType = 0; // 0 免费 1 付费
		if (ioMessage.getInputParse("drawType") != null) {
			drawType = Integer.parseInt(ioMessage.getInputParse("drawType").toString());
		}
		EventModule eventModule = ModuleManager.getModule(ModuleNames.EventModule, EventModule.class);
		eventModule.timeLimitDraw(playerID, drawType, protocol);
		ioMessage.setOutputResult(protocol);
	}
}
