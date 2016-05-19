package com.mi.game.module.hero.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.hero.HeroModule;
import com.mi.game.module.hero.protocol.HeroInfoProtocol;
@HandlerType(type = HandlerIds.CompoundHero)
public class CompoundHeroHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String playerID = ioMessage.getPlayerId();
		String shardID = "-1";
		if(ioMessage.getInputParse("shardID") != null){
			shardID = ioMessage.getInputParse("shardID").toString();
		}
		HeroInfoProtocol protocol = new HeroInfoProtocol();
		HeroModule heroModule = ModuleManager.getModule(ModuleNames.HeroModule,HeroModule.class);
		try{
			heroModule.compoundHero(playerID, shardID, protocol, ioMessage);
		}catch(IllegalArgumentException ex){
			protocol.setCode(Integer.parseInt(ex.getMessage()));
		}
		ioMessage.setProtocol(protocol);
	}
}
