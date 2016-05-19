package com.mi.game.module.login.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.login.LoginModule;
import com.mi.game.module.login.protocol.LoginInfoProtocol;
@HandlerType(type = HandlerIds.visitorRegister)
public class VisitorRegisterHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String phoneID = "";
		String playerName = "";
		String password = "";
		String email = "";
		if(ioMessage.getInputParse("phoneID") != null){
			phoneID = ioMessage.getInputParse("phoneID").toString();			
		}
		if(ioMessage.getInputParse("playerName") != null){
			playerName = ioMessage.getInputParse("playerName").toString();			
		}
		if(ioMessage.getInputParse("password") != null){
			password = ioMessage.getInputParse("password").toString();			
		}
		if(ioMessage.getInputParse("email") != null){
			email = ioMessage.getInputParse("email").toString();
		}
		LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule,LoginModule.class);
		LoginInfoProtocol protocol = new LoginInfoProtocol();
		try{
			loginModule.visitorBind(phoneID, playerName, password,email);
		}catch(IllegalArgumentException ex){
			protocol.setCode(Integer.parseInt(ex.getMessage()));
		}
		ioMessage.setProtocol(protocol);
	}
	
}
