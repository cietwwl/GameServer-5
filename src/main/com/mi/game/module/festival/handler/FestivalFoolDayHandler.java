package com.mi.game.module.festival.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.festival.FestivalModule;
import com.mi.game.module.festival.protocol.FestivalFoolDayProtocol;

@HandlerType(type = HandlerIds.FESTIVAL_EXCHANGEFOOLDAYMAGICBOX)
public class FestivalFoolDayHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String playerID = ioMessage.getPlayerId();
		FestivalFoolDayProtocol protocol  = new FestivalFoolDayProtocol();
		FestivalModule festivalModule = ModuleManager.getModule(ModuleNames.FestivalModule,FestivalModule.class);
		try{
			festivalModule.exchangeMagicBox(playerID, protocol, ioMessage);
		}catch(IllegalArgumentException ex){
			protocol.setCode(Integer.parseInt(ex.getMessage()));
		}
		ioMessage.setProtocol(protocol);
	}
}
