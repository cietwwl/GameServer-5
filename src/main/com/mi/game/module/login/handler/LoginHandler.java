package com.mi.game.module.login.handler;

import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.analyse.AnalyseModule;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.login.LoginModule;
import com.mi.game.module.login.protocol.LoginInfoProtocol;
import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;

/**
 * @author 刘凯旋 用户服务器登陆handler 2014年5月29日 下午2:39:04
 */
@HandlerType(type = HandlerIds.ServerLogin, order = 2)
public class LoginHandler extends BaseHandler {
	@Override
	public void execute(IOMessage ioMessage) {
		LoginModule loginModule = (LoginModule) ModuleManager.getModule(ModuleNames.LoginModule);
		String playerID = ioMessage.getPlayerId();
		LoginInfoProtocol protocol = new LoginInfoProtocol();
		protocol.setPlayerID(playerID);
		loginModule.serverLogin(playerID, protocol, ioMessage);
		
		
		///////////////
		AnalyseModule analyseModule = ModuleManager.getModule(ModuleNames.AnalyseModule, AnalyseModule.class);
		// 记录统计日志
		analyseModule.analyLog(playerID, "LOGIN_GAME", null, null, null, null);
		ioMessage.setProtocol(protocol);
	}
}
