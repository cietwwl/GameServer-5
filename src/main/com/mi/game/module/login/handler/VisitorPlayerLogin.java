package com.mi.game.module.login.handler;

import java.util.List;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.login.LoginModule;
import com.mi.game.module.login.pojo.LoginInfoEntity;
import com.mi.game.module.login.pojo.ServerInfoEntity;
import com.mi.game.module.login.protocol.LoginInfoProtocol;
@HandlerType(type = HandlerIds.visitorLogin)
public class VisitorPlayerLogin extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String phoneID = "";
		if(ioMessage.getInputParse("phoneID") != null){
			phoneID = ioMessage.getInputParse("phoneID").toString();
		}
		LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule,LoginModule.class);
		LoginInfoEntity loginInfoEntity = loginModule.visitorLogin(phoneID);
		List<ServerInfoEntity> serverList = loginModule.getServerList();
		LoginInfoProtocol protocol = new LoginInfoProtocol();
		protocol.setServerList(serverList);
		protocol.setPlayerID(loginInfoEntity.getKey());
		protocol.setVisitorID(loginInfoEntity.getVisitorID());
		protocol.setBind(loginInfoEntity.isBind());
		protocol.setPlayerName(loginInfoEntity.getPlayerName());
		ioMessage.setProtocol(protocol);
	}
}
