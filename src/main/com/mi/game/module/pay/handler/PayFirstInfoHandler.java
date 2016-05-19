package com.mi.game.module.pay.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.pay.PayModule;
import com.mi.game.module.pay.protocol.PayProtocol;

@HandlerType(type = HandlerIds.PAY_INFO)
public class PayFirstInfoHandler extends BaseHandler {

	@Override
	public void execute(IOMessage ioMessage) {
		String playerID = ioMessage.getPlayerId();
		PayModule payModule = ModuleManager.getModule(ModuleNames.PayModule, PayModule.class);
		PayProtocol protocol = new PayProtocol();
		payModule.getPayFirstInfo(playerID, protocol);
		ioMessage.setOutputResult(protocol);
	}
}
