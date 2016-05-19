package com.mi.game.module.welfare.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.welfare.WelfareModule;
import com.mi.game.module.welfare.protocol.WelfareRescueSunProtocol;

@HandlerType(type = HandlerIds.WELFARE_RESUCESUNWUKONG)
public class RescueSunHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String playerID = ioMessage.getPlayerId();
		int rescueType = 0;
		if(ioMessage.getInputParse("rescueType") != null ){
			rescueType = Integer.parseInt(ioMessage.getInputParse("rescueType").toString());
		}
		WelfareRescueSunProtocol protocol = new WelfareRescueSunProtocol();
		WelfareModule welfareModule = ModuleManager.getModule(ModuleNames.WelfareModule,WelfareModule.class);
		welfareModule.rescueSun(playerID, rescueType, protocol, ioMessage);
		ioMessage.setProtocol(protocol);
	}
}
