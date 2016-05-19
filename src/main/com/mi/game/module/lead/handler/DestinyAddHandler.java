package com.mi.game.module.lead.handler;


import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.lead.LeadModule;
import com.mi.game.module.lead.protocol.DestinyProtocol;

@HandlerType(type = HandlerIds.destinyAdd)
public class DestinyAddHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String playerID = ioMessage.getPlayerId();
		LeadModule leadModule = ModuleManager.getModule(ModuleNames.LeadModule,LeadModule.class);
		DestinyProtocol protocol = new DestinyProtocol();
		try{
			leadModule.addDestiny(playerID,protocol,ioMessage);
		}catch(Exception ex){
			protocol.setCode(Integer.parseInt(ex.getMessage()));
		}
		ioMessage.setProtocol(protocol);
	}
}
