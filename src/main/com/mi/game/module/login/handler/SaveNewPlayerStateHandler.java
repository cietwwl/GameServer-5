package com.mi.game.module.login.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.login.LoginModule;
import com.mi.game.module.login.protocol.LoginInfoProtocol;
@HandlerType(type = HandlerIds.saveNewPlayerState)
public class SaveNewPlayerStateHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String playerID = ioMessage.getPlayerId();
		int properId = 0;
		boolean isFinality = false;
	    String triggerId = "";
		 if(ioMessage.getInputParse("properId") != null){
			 properId = Integer.parseInt(ioMessage.getInputParse("properId").toString());
		 }
		 if(ioMessage.getInputParse("isFinality") != null){
			 isFinality = Boolean.parseBoolean(ioMessage.getInputParse("isFinality").toString());
		 }
		 if(ioMessage.getInputParse("triggerId") != null){
			 triggerId = ioMessage.getInputParse("triggerId").toString();
		 }
		LoginInfoProtocol protocol = new LoginInfoProtocol();
		LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule,LoginModule.class);
		loginModule.saveNewPlayerState(playerID, properId, isFinality, triggerId);
		ioMessage.setProtocol(protocol);
	}
}
