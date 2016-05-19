package com.mi.game.module.arena.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.arena.ArenaModule;
import com.mi.game.module.arena.protocol.ArenaProtocol;
import com.mi.game.module.base.handler.BaseHandler;
@HandlerType(type = HandlerIds.exchangeArenaGoods)
public class ExchangeArenaGoodsHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String playerID = ioMessage.getPlayerId();
		int pid = 0;
		int num = 1;
		if(ioMessage.getInputParse("pid") != null){
			pid = Integer.parseInt(ioMessage.getInputParse("pid").toString());
		}
		if(ioMessage.getInputParse("num") != null){
			num = Integer.parseInt(ioMessage.getInputParse("num").toString());
		}
		ArenaProtocol protocol = new ArenaProtocol();
		ArenaModule module = ModuleManager.getModule(ModuleNames.ArenaModule, ArenaModule.class);
		try{
			module.exchangeArenaGoods(playerID, pid, num,protocol);
		}catch(IllegalArgumentException ex){
			protocol.setCode(Integer.parseInt(ex.getMessage()));
		}
		ioMessage.setProtocol(protocol);
	}
}
