package com.mi.game.module.admin.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.admin.protocol.BaseAdminProtocol;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.login.LoginModule;

@HandlerType(type = HandlerIds.changeServerStatus)
public class ChangeServerStatusHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage) {
		BaseAdminProtocol protocol = new BaseAdminProtocol();
		int status = 0;
		String serverMessage = "";
		if(ioMessage.getInputParse("status") != null){
			status = Integer.parseInt(ioMessage.getInputParse("status").toString());
		}
		if(ioMessage.getInputParse("serverMessage") != null){
			serverMessage = ioMessage.getInputParse("serverMessage").toString();
		}
		LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule,LoginModule.class);
		loginModule.setStatus(status);
		loginModule.setStopServerMessage(serverMessage);
		ioMessage.setProtocol(protocol);
	}
}
