package com.mi.game.module.event.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.event.EventModule;
import com.mi.game.module.event.pojo.WarGodRankEntity;
import com.mi.game.module.event.protocol.EventWarGodProtocol;
@HandlerType(type = HandlerIds.EVENT_WORGODRANK)
public class GetWarGodRankHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		EventModule eventModule = ModuleManager.getModule(ModuleNames.EventModule,EventModule.class);
		WarGodRankEntity warGodRankEntity =  eventModule.getWarGodRankEntity();
		EventWarGodProtocol protocol = new EventWarGodProtocol();
		protocol.setWarGodRankEntity(warGodRankEntity);
		ioMessage.setProtocol(protocol);
	}
}
