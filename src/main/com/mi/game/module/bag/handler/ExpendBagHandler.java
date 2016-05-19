package com.mi.game.module.bag.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.bag.BagModule;
import com.mi.game.module.bag.protocol.BagProtocol;
import com.mi.game.module.base.handler.BaseHandler;
@HandlerType(type  = HandlerIds.ExpandBag)
public class ExpendBagHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String playerID  = ioMessage.getPlayerId();
		BagModule bagModule = ModuleManager.getModule(ModuleNames.BagModule,BagModule.class);
		BagProtocol protocol = new BagProtocol();
		bagModule.expandBagNum(playerID,protocol);
		ioMessage.setProtocol(protocol);
	}
}
