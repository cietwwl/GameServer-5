package com.mi.game.module.store.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.store.StoreModule;
import com.mi.game.module.store.protocol.StoreProtocol;

@HandlerType(type = HandlerIds.STORE_INFO)
public class StoreInfoHandler extends BaseHandler {

	@Override
	public void execute(IOMessage ioMessage) {
		String playerID = ioMessage.getPlayerId();
		StoreModule storeModule = ModuleManager.getModule(ModuleNames.StoreModule, StoreModule.class);
		StoreProtocol protocol = new StoreProtocol();
		storeModule.storeInfo(playerID, protocol);
		ioMessage.setProtocol(protocol);
	}
}
