package com.mi.game.module.talisman.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.talisman.TalismanModule;
import com.mi.game.module.talisman.protocol.TalismanProtocol;
@HandlerType(type = HandlerIds.ExpendTalismanBag)
public class ExpendTalismanNumHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String playerID = ioMessage.getPlayerId();
		TalismanProtocol protocol = new TalismanProtocol();
		TalismanModule module = ModuleManager.getModule(ModuleNames.TalismanModule,TalismanModule.class);
		module.expendTalismanBag(playerID, protocol);
		ioMessage.setProtocol(protocol); 
	}
}
