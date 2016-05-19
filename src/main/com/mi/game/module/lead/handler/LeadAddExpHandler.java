package com.mi.game.module.lead.handler;

import java.util.HashMap;
import java.util.Map;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.lead.LeadModule;
import com.mi.game.module.lead.protocol.DestinyProtocol;
import com.mi.game.module.lead.protocol.ExpResponse;

@HandlerType(type = HandlerIds.AddHeroExp)
public class LeadAddExpHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String playerID = ioMessage.getPlayerId();
//		int exp = 0;
//		if(ioMessage.getInputParse("exp") != null){
//			exp = Integer.parseInt(ioMessage.getInputParse("exp").toString());
//		}
		LeadModule leadModule = ModuleManager.getModule(ModuleNames.LeadModule,LeadModule.class);
		Map<String,Object> itemMap = new HashMap<>();
		ExpResponse response = leadModule.addLevel(playerID,itemMap,ioMessage);
		DestinyProtocol protocol = new DestinyProtocol();
		protocol.setExpResponse(response);
		protocol.setItemMap(itemMap);
		ioMessage.setProtocol(protocol);
	}
}
