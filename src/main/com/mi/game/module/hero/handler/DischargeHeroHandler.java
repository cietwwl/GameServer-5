package com.mi.game.module.hero.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.hero.HeroModule;
import com.mi.game.module.hero.protocol.HeroInfoProtocol;

/**
 * @author 刘凯旋	
 * 武将卸下
 * 2014年5月30日 下午3:38:33
 */
@HandlerType(type = HandlerIds.DischargeHero)
public class DischargeHeroHandler extends BaseHandler{	
	@Override
	public void execute(IOMessage ioMessage) {
		HeroModule heroModule = ModuleManager.getModule(ModuleNames.HeroModule, HeroModule.class);
		String playerID = ioMessage.getInputParse("playerID").toString();
		long heroID = 0;
		if(ioMessage.getInputParse("heroID") != null){
			heroID = Long.parseLong(ioMessage.getInputParse("heroID").toString());
		}
		HeroInfoProtocol protocol = new HeroInfoProtocol();
		heroModule.dischargeHero(playerID, heroID,protocol);
		ioMessage.setProtocol(protocol);
	}
}
