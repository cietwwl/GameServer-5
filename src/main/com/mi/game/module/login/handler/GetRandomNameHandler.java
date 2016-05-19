package com.mi.game.module.login.handler;

import java.util.List;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.login.LoginModule;
import com.mi.game.module.login.protocol.LoginInfoProtocol;

@HandlerType(type = HandlerIds.getRandomNameList)
public class GetRandomNameHandler extends BaseHandler {
	@Override
	public void execute(IOMessage ioMessage){
		LoginModule module = ModuleManager.getModule(ModuleNames.LoginModule,LoginModule.class);
		LoginInfoProtocol protocol = new LoginInfoProtocol();
		
		List<String> femaleNameList = module.getRandomNameList(1);
		List<String> maleNameList = module.getRandomNameList(0);
		protocol.setMaleNameList(maleNameList);
		protocol.setFemaleNameList(femaleNameList);
		ioMessage.setProtocol(protocol);
	}
}
