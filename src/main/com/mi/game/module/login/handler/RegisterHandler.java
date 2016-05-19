package com.mi.game.module.login.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.login.LoginModule;
import com.mi.game.module.login.protocol.LoginInfoProtocol;

/**
 * 注册用户handler
 * 
 * **/
@HandlerType(type = HandlerIds.RegisterUser)
public class RegisterHandler extends BaseHandler {
	@Override
	public void execute(IOMessage ioMessage) {
		String playerName = "";
		String passwd = "";
		String email = "";
		if(ioMessage.getInputParse("password") != null){
			passwd = (String) ioMessage.getInputParse("password");
		}
		if(ioMessage.getInputParse("playerName") != null){
			playerName = (String) ioMessage.getInputParse("playerName");
		}
		if(ioMessage.getInputParse("email") != null ){
			email = (String) ioMessage.getInputParse("email");		
		}
		playerName = playerName.toLowerCase();
		LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule, LoginModule.class);
		int code = loginModule.registerUser(playerName, passwd, email, false);
		LoginInfoProtocol protocol = new LoginInfoProtocol();
		protocol.setCode(code);
		ioMessage.setOutputResult(protocol);
	}

}
