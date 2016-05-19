package com.mi.game.module.login.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.login.LoginModule;
import com.mi.game.module.login.pojo.PlayerEntity;
import com.mi.game.module.login.protocol.LoginInfoProtocol;

@HandlerType(type = HandlerIds.getPlayerStatus)
public class GetPlayerStatusHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String playerID = ioMessage.getPlayerId();
		LoginInfoProtocol protocol = new LoginInfoProtocol();
		LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule,LoginModule.class);
		try{
			int newPlayer = 0;
			PlayerEntity playerEntity = loginModule.getPlayerEntityNoError(playerID);
			if(playerEntity == null){
				newPlayer = 1;
			}
			protocol.setNewPlayer(newPlayer);
		}catch(IllegalArgumentException ex){
			int code = Integer.parseInt(ex.getMessage());
			protocol.setCode(code);
		}
		ioMessage.setProtocol(protocol);
	}
}
