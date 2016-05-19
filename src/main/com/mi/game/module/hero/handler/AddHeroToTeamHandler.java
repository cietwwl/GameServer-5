package com.mi.game.module.hero.handler;

import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.hero.HeroModule;
import com.mi.game.module.hero.protocol.HeroInfoProtocol;
import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;

/**
 * @author 刘凯旋	
 * 英雄上阵handler
 * 2014年5月29日 下午2:38:18
 */
@HandlerType(type = HandlerIds.HeroAddToTeam)
public class AddHeroToTeamHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage) {
		HeroModule heroModule = ModuleManager.getModule(ModuleNames.HeroModule, HeroModule.class);
		String playerID  = ioMessage.getInputParse("playerID").toString();
		long heroID = 0;
		int postion = 0;
		if(ioMessage.getInputParse("heroID") != null){
			heroID  = Long.parseLong(ioMessage.getInputParse("heroID").toString());
		}
		if(ioMessage.getInputParse("postion") != null){
			postion  = Integer.parseInt(ioMessage.getInputParse("postion").toString());
		}
		
		HeroInfoProtocol protocol  = new HeroInfoProtocol();
		try{
			heroModule.addHeroToTeam(playerID, heroID, postion,protocol,ioMessage);
		}catch(IllegalArgumentException ex){
			protocol.setCode(Integer.parseInt(ex.getMessage()));
		}
	

		ioMessage.setProtocol(protocol);
	}
}
