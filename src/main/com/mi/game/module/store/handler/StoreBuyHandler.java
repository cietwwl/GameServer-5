package com.mi.game.module.store.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.store.StoreModule;
import com.mi.game.module.store.protocol.StoreProtocol;

@HandlerType(type = HandlerIds.STORE_BUY)
public class StoreBuyHandler extends BaseHandler {

	@Override
	public void execute(IOMessage ioMessage) {
		String playerID = ioMessage.getPlayerId();
		String storeType = (String) ioMessage.getInputParse("storeType");
		String itemID = (String) ioMessage.getInputParse("itemID");
		String itemNum = (String) ioMessage.getInputParse("itemNum");
		StoreModule storeModule = ModuleManager.getModule(ModuleNames.StoreModule, StoreModule.class);
		StoreProtocol protocol = new StoreProtocol();
		storeModule.storeBuy(playerID, storeType, itemID, itemNum, protocol);
		ioMessage.setProtocol(protocol);
	}
}
