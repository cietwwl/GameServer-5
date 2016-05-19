package com.mi.game.module.login.handler;


import com.mi.game.defines.ErrorIds;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.login.LoginModule;
import com.mi.game.module.login.protocol.LoginInfoProtocol;
import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;


/**
 * 用户名登录
 * 
 * */

@HandlerType(type = HandlerIds.UserLogin)
public class UserLoginHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage) {
		LoginInfoProtocol protocol = new LoginInfoProtocol();
		String passwd = (String) ioMessage.getInputParse("password");
		String playerName = (String) ioMessage.getInputParse("playerName");
		LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule,LoginModule.class);
		playerName = playerName.toLowerCase();
		try{
			loginModule.chechkUserPasswd(playerName,passwd,protocol);
		}catch(IllegalArgumentException ex){
			int code = Integer.parseInt(ex.getMessage());
			protocol.setCode(code);
		}
		ioMessage.setProtocol(protocol);
	}
}
