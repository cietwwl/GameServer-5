package com.mi.game.module.lead.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.lead.LeadModule;
import com.mi.game.module.lead.protocol.DestinyProtocol;

@HandlerType(type  = HandlerIds.getDestinyNum)
public class GetDestinyNumHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String playerID = ioMessage.getPlayerId();
		LeadModule leadModule = ModuleManager.getModule(ModuleNames.LeadModule,LeadModule.class);
		int num = leadModule.getDestinyNum(playerID);
		DestinyProtocol protocol = new DestinyProtocol();
		protocol.setNum(num);
		ioMessage.setProtocol(protocol);
	}
}
