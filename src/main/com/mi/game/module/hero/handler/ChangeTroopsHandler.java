package com.mi.game.module.hero.handler;

import java.util.List;

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
 * 更换队形Handler
 * 2014年5月29日 下午4:59:06
 */
@HandlerType(type = HandlerIds.ChangeTroops)
public class ChangeTroopsHandler extends BaseHandler{
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
		List<Long> troops = heroModule.changeTroops(playerID, heroID, postion);
		HeroInfoProtocol protocol = new HeroInfoProtocol();
		protocol.setTroops(troops);
		ioMessage.setProtocol(protocol);
	}
}
