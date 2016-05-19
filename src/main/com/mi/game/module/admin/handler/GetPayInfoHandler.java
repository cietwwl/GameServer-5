package com.mi.game.module.admin.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.core.protocol.BaseProtocol;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.pay.PayModule;
@HandlerType(type = HandlerIds.getPayInfoFile)
public class GetPayInfoHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		PayModule payModule = ModuleManager.getModule(ModuleNames.PayModule,PayModule.class);
		payModule.getPayInfo();
		ioMessage.setProtocol(new BaseProtocol());
	}
}
