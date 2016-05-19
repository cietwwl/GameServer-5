package com.mi.game.module.arena.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.arena.ArenaModule;
import com.mi.game.module.arena.protocol.ArenaProtocol;
import com.mi.game.module.base.handler.BaseHandler;
@HandlerType(type = HandlerIds.getPkInfo)
public class GetRankInfoHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String playerID = ioMessage.getPlayerId();
		String pkID = "";
		if(ioMessage.getInputParse("pkID") != null){
			pkID = ioMessage.getInputParse("pkID").toString();
		}
		ArenaModule module = ModuleManager.getModule(ModuleNames.ArenaModule,ArenaModule.class);
		ArenaProtocol protocol = new ArenaProtocol();
		try{
			module.getBattleInfo(pkID,playerID, protocol);
		}catch(IllegalArgumentException ex){
			protocol.setCode(Integer.parseInt(ex.getMessage()));
		}
		ioMessage.setProtocol(protocol);
	}
	
}
