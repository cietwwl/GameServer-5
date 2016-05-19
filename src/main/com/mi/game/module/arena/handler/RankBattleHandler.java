package com.mi.game.module.arena.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.ErrorIds;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.arena.ArenaModule;
import com.mi.game.module.arena.protocol.ArenaProtocol;
import com.mi.game.module.base.handler.BaseHandler;
@HandlerType(type = HandlerIds.pkBattle)
public class RankBattleHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String playerID = ioMessage.getPlayerId();
		String pkID = "";
		boolean win =  false;
		String battleString = "";
		if(ioMessage.getInputParse("pkID") != null){
			pkID = ioMessage.getInputParse("pkID").toString();
		}
		if(ioMessage.getInputParse("win") != null){
			win = Boolean.parseBoolean(ioMessage.getInputParse("win").toString());
		}
		if(ioMessage.getInputParse("battleString") != null){
			battleString = ioMessage.getInputParse("battleString").toString();
		}
		ArenaProtocol protocol = new ArenaProtocol();
		if(pkID.equals(playerID)){
			protocol.setCode(ErrorIds.PlayerNotPkSelf);
			ioMessage.setProtocol(protocol);
			return ;
		}
		
		ArenaModule module = ModuleManager.getModule(ModuleNames.ArenaModule,ArenaModule.class);
		try{
			module.ArenaBattle(playerID, pkID, win, battleString ,ioMessage, protocol);
		}catch(IllegalArgumentException ex){
			protocol.setCode(Integer.parseInt(ex.getMessage()));
		}
		ioMessage.setProtocol(protocol);
	}
}
