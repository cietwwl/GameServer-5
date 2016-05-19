package com.mi.game.module.analyse.handler;

import com.alibaba.fastjson.JSONObject;
import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.analyse.AnalyseModule;
import com.mi.game.module.analyse.protocol.AnalyProtocol;
import com.mi.game.module.base.handler.BaseHandler;

@HandlerType(type = HandlerIds.CLIENT_ANALY_INFO)
public class ClientAnalyHandler extends BaseHandler {

	@Override
	public void execute(IOMessage ioMessage) {
		String playerID = ioMessage.getPlayerId();
		String clientType = (String) ioMessage.getInputParse("clientType");
		JSONObject params = (JSONObject) ioMessage.getInputParse("params");
		AnalyseModule analyseModule = ModuleManager.getModule(ModuleNames.AnalyseModule, AnalyseModule.class);
		AnalyProtocol protocol = new AnalyProtocol();
		analyseModule.clientAnaly(playerID, clientType, params, protocol);
		ioMessage.setOutputResult(protocol);
	}

}
