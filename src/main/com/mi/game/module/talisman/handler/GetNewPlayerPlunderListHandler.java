package com.mi.game.module.talisman.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.talisman.TalismanModule;
import com.mi.game.module.talisman.protocol.TalismanProtocol;
@HandlerType(type = HandlerIds.NewPlayerPlunderTalismanList)
public class GetNewPlayerPlunderListHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String playerID = ioMessage.getPlayerId();
		int shardID = 0;
		if(ioMessage.getInputParse("shardID") != null){
			shardID = Integer.parseInt(ioMessage.getInputParse("shardID").toString());
		}
		TalismanProtocol protocol = new TalismanProtocol();
		TalismanModule talismanModule = ModuleManager.getModule(ModuleNames.TalismanModule,TalismanModule.class);
		try{
			talismanModule.getNewPlayerPlunderInfo(playerID, shardID, ioMessage, protocol);
		}catch(IllegalArgumentException ex){
			protocol.setCode(Integer.parseInt(ex.getMessage()));
		}
		
		ioMessage.setProtocol(protocol);
	}
}
