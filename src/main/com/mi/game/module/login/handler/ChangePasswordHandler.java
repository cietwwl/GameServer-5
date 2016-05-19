package com.mi.game.module.login.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.login.LoginModule;
import com.mi.game.module.login.protocol.LoginInfoProtocol;
@HandlerType(type = HandlerIds.changePassword)
public class ChangePasswordHandler extends BaseHandler {
	@Override
	public void execute(IOMessage ioMessage){
		String playerName = "";
		String oldPassword = "";
		String newPassword = "";
		if(ioMessage.getInputParse("playerName") != null){
			playerName = ioMessage.getInputParse("playerName").toString();
		}
		if(ioMessage.getInputParse("oldPassword") != null){
			oldPassword = ioMessage.getInputParse("oldPassword").toString();
		}
		if(ioMessage.getInputParse("newPassword") != null){
			newPassword = ioMessage.getInputParse("newPassword").toString();
		}
		LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule,LoginModule.class);
		loginModule.changePassword(playerName, oldPassword, newPassword);
		ioMessage.setProtocol(new LoginInfoProtocol());
	}
}
