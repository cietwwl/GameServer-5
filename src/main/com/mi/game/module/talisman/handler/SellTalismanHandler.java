package com.mi.game.module.talisman.handler;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.talisman.TalismanModule;
import com.mi.game.module.talisman.protocol.TalismanProtocol;
@HandlerType(type = HandlerIds.SellTalisman)
public class SellTalismanHandler extends BaseHandler{
	@Override
	public void execute (IOMessage ioMessage){
			String playerID = ioMessage.getPlayerId();
			List<Object> sellList =  new ArrayList<>();
			if(ioMessage.getInputParse("sellList") != null){
				sellList = JSON.parseArray(ioMessage.getInputParse("sellList").toString());
			}
			TalismanProtocol protocol = new TalismanProtocol();
			TalismanModule module = ModuleManager.getModule(ModuleNames.TalismanModule,TalismanModule.class);
			module.sellTalisman(playerID, sellList, protocol,ioMessage);
			ioMessage.setProtocol(protocol);
	}
}
