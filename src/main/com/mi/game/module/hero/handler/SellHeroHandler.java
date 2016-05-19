package com.mi.game.module.hero.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.hero.HeroModule;
import com.mi.game.module.hero.protocol.HeroInfoProtocol;
import com.mi.game.module.reward.data.GoodsBean;

/**
 * @author 刘凯旋	
 * 出售武将
 * 2014年6月26日 下午3:59:26
 */
@HandlerType(type = HandlerIds.SellHero)
public class SellHeroHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String playerID = ioMessage.getPlayerId();
		List<Object> sellList = new ArrayList<>();
		if(ioMessage.getInputParse("sellList") != null){
			sellList = JSON.parseArray(ioMessage.getInputParse("sellList").toString());
		}
		Map<String, Object> itemMap = new HashMap<>();
		Map<String,GoodsBean> showMap = new HashMap<>();
		HeroModule heroModule = ModuleManager.getModule(ModuleNames.HeroModule,HeroModule.class);
		int code = heroModule.sellHeros(playerID, sellList,itemMap, showMap,ioMessage);
		HeroInfoProtocol protocol = new HeroInfoProtocol();
		protocol.setCode(code);
		if(code == 0){
			itemMap.put("removeHeroList",sellList);
			protocol.setItemMap(itemMap);
			protocol.setShowMap(showMap);
		}
		ioMessage.setProtocol(protocol);
	}
}
