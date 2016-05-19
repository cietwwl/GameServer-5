package com.mi.game.module.hero.handler;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
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
 * 武将强化
 * 2014年6月25日 上午11:11:35
 */

@HandlerType(type = HandlerIds.StrengHero)
public class StrengHeroHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String playerID = ioMessage.getPlayerId();
		String heroID = "-1";
		int strengType = 0;
		List<Object> strengList = new ArrayList<>(); 
		if(ioMessage.getInputParse("heroID") != null)
			heroID = ioMessage.getInputParse("heroID").toString();
		if(ioMessage.getInputParse("strengType") != null)
			strengType = Integer.parseInt(ioMessage.getInputParse("strengType").toString());
		if(ioMessage.getInputParse("strengList") != null)
			strengList = JSON.parseArray(ioMessage.getInputParse("strengList").toString());
		HeroInfoProtocol protocol = new HeroInfoProtocol();
		HeroModule heroModule = ModuleManager.getModule(ModuleNames.HeroModule,HeroModule.class);
		try{
			heroModule.doStreng(strengType, playerID, heroID, strengList, ioMessage, protocol);
		}catch(IllegalArgumentException ex){
			protocol.setCode(Integer.parseInt(ex.getMessage()));
		}
		ioMessage.setProtocol(protocol);
	}
}
