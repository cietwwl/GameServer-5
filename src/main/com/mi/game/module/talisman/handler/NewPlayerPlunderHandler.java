package com.mi.game.module.talisman.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.talisman.TalismanModule;
import com.mi.game.module.talisman.protocol.TalismanProtocol;
@HandlerType(type = HandlerIds.NewPlayerPlunderShard)
public class NewPlayerPlunderHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		TalismanProtocol protocol = new TalismanProtocol();
		String playerID = ioMessage.getPlayerId();
		TalismanModule talismanModule = ModuleManager.getModule(ModuleNames.TalismanModule,TalismanModule.class);
		try{
			talismanModule.newPlayerPlunderShard(playerID, ioMessage, protocol);
		}catch(Exception ex){
			protocol.setCode(Integer.parseInt(ex.getMessage()));
		}
		ioMessage.setProtocol(protocol);
	}
}
