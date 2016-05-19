package com.mi.game.module.login.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.login.LoginModule;
import com.mi.game.module.login.protocol.LoginInfoProtocol;
@HandlerType(type = HandlerIds.PlayerInfoInit)
public class PlayerInfoInitHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String playerID = ioMessage.getPlayerId();
		int sex = 0;
		String nickName = "";
		String platform = "";
		if(ioMessage.getInputParse("sex") != null){
			sex = Integer.parseInt(ioMessage.getInputParse("sex").toString());
		}
		if(ioMessage.getInputParse("nickName") != null){
			nickName = ioMessage.getInputParse("nickName").toString();
		}
		if(ioMessage.getInputParse("platform") != null){
			platform = ioMessage.getInputParse("platform").toString();
		}
		LoginInfoProtocol protocol = new LoginInfoProtocol();
		LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule,LoginModule.class);
		try{
			loginModule.initPlayerEntity(playerID, sex, nickName,platform,ioMessage);
		}catch(IllegalArgumentException ex){
			protocol.setCode(Integer.parseInt(ex.getMessage()));
		}
		ioMessage.setProtocol(protocol);
	}
}
