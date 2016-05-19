package com.mi.game.module.smelt.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.smelt.SmeltModule;
import com.mi.game.module.smelt.protocol.SmeltProtocol;
@HandlerType(type = HandlerIds.Rebirth)
public class RebirthHandler extends BaseHandler {
	@Override
	public void execute(IOMessage ioMessage){
		String playerID   = ioMessage.getPlayerId();
		long targetID = 0;
		if(ioMessage.getInputParse("targetID") != null){
			targetID = Long.parseLong(ioMessage.getInputParse("targetID").toString());
		}
		int type = 0;
		if(ioMessage.getInputParse("rebirthType") != null){
			type = Integer.parseInt(ioMessage.getInputParse("rebirthType").toString());
		}
		SmeltProtocol protocol = new SmeltProtocol();
		SmeltModule module = ModuleManager.getModule(ModuleNames.SmeltModule,SmeltModule.class);
		module.doRebirth(playerID, targetID, type, ioMessage, protocol);
		ioMessage.setProtocol(protocol);
	}
}
