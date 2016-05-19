package com.mi.game.module.playerservice.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.core.protocol.BaseProtocol;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.playerservice.PlayerServiceModule;
@HandlerType(type = HandlerIds.postSuggestHandler)
public class PostSuggestHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String playerID  = ioMessage.getPlayerId();
		String message = "";
		int suggestType = 0;
		if(ioMessage.getInputParse("message") != null){
			message = ioMessage.getInputParse("message").toString();
		}
		if(ioMessage.getInputParse("suggestType") != null){
			suggestType = Integer.parseInt(ioMessage.getInputParse("suggestType").toString());
		}
		PlayerServiceModule playerServiceModule = ModuleManager.getModule(ModuleNames.PlayerServiceModule,PlayerServiceModule.class);
		BaseProtocol protocol = new BaseProtocol();
		try{
			playerServiceModule.saveSuggestInfo(playerID, message, suggestType);
		}catch(IllegalArgumentException ex){
			protocol.setCode(Integer.parseInt(ex.getMessage()));
		}
		ioMessage.setProtocol(protocol);
	}
}
