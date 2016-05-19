package com.mi.game.module.hero.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.hero.HeroModule;
import com.mi.game.module.hero.protocol.HeroInfoProtocol;

@HandlerType(type = HandlerIds.ExpandHeroBag)
public class ExpandHeroBagHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String playerID  = ioMessage.getPlayerId();
		HeroInfoProtocol protocol = new HeroInfoProtocol();
		HeroModule heroModule = ModuleManager.getModule(ModuleNames.HeroModule,HeroModule.class);
		heroModule.doExpand(playerID, protocol);
		//heroModule.calculateFightValue(playerID, ioMessage);
		ioMessage.setProtocol(protocol);
	}
}
