package com.mi.game.module.talisman.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.talisman.TalismanModule;
import com.mi.game.module.talisman.protocol.TalismanProtocol;
@HandlerType(type = HandlerIds.checkCanPlunder)
public class CheckPlunderHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String playerID = ioMessage.getPlayerId();
		int shardID = 0;
		String plunderID = "";
		if(ioMessage.getInputParse("shardID") != null){
			shardID = Integer.parseInt(ioMessage.getInputParse("shardID").toString());
		}
		if(ioMessage.getInputParse("plunderID") != null){
			plunderID = ioMessage.getInputParse("plunderID").toString();
		}
		TalismanProtocol protocol = new TalismanProtocol();
		TalismanModule talismanModule = ModuleManager.getModule(ModuleNames.TalismanModule,TalismanModule.class);
		try{
			talismanModule.checkPlunder(playerID, shardID, plunderID);
		}catch(IllegalArgumentException ex){
			int code = Integer.parseInt(ex.getMessage());
			protocol.setCode(code);
		}
		ioMessage.setProtocol(protocol);
	}
}
