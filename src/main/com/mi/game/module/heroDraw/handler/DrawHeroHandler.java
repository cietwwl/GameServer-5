package com.mi.game.module.heroDraw.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;import com.mi.game.defines.ModuleNames;
import com.mi.game.defines.HandlerIds;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.hero.protocol.HeroInfoProtocol;
import com.mi.game.module.heroDraw.HeroDrawModule;

@HandlerType(type = HandlerIds.DrawHero)
public class DrawHeroHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String playerID = ioMessage.getPlayerId();
		int drawType = 0;
		if(ioMessage.getInputParse("drawType") != null){
			drawType = Integer.parseInt(ioMessage.getInputParse("drawType").toString());
		}
		HeroInfoProtocol protocol = new HeroInfoProtocol();
		HeroDrawModule drawModule = ModuleManager.getModule(ModuleNames.HeroDrawModule,HeroDrawModule.class);
		try{
			drawModule.drawHero(playerID, drawType, protocol,ioMessage);
		}catch(IllegalArgumentException ex){
			protocol.setCode(Integer.parseInt(ex.getMessage()));
		}
		ioMessage.setProtocol(protocol);
	}
	
	
}
