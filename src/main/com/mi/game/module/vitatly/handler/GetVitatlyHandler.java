package com.mi.game.module.vitatly.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.login.protocol.LoginInfoProtocol;
import com.mi.game.module.vitatly.VitatlyModule;
import com.mi.game.module.vitatly.pojo.VitatlyEntity;
@HandlerType(type = HandlerIds.getVitatlyEntity)
public class GetVitatlyHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String playerID = ioMessage.getPlayerId();
		VitatlyModule vitatlyModule = ModuleManager.getModule(ModuleNames.VitatlyModule,VitatlyModule.class);
		VitatlyEntity vitatlyEntity = vitatlyModule.getVitatlyEntity(playerID);
		LoginInfoProtocol protocol = new LoginInfoProtocol();
		protocol.setVitatlyEntity(vitatlyEntity);
		ioMessage.setProtocol(protocol);
	}
}
