package com.mi.game.module.login.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.core.protocol.BaseProtocol;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.login.LoginModule;
@HandlerType(type = HandlerIds.ChangeServersInfo)
public class ChangeServerInfoHandler extends BaseHandler{
	
	public void execute(IOMessage ioMessage){
		LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule,LoginModule.class);
		try {
			loginModule.changeServerInfo();
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		BaseProtocol protocol = new BaseProtocol();
		ioMessage.setProtocol(protocol);
	}

}
