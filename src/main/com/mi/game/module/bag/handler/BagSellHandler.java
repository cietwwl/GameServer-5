package com.mi.game.module.bag.handler;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.bag.BagModule;
import com.mi.game.module.bag.protocol.BagProtocol;
import com.mi.game.module.base.handler.BaseHandler;
@HandlerType(type = HandlerIds.ItemSell)
public class BagSellHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String playerID = ioMessage.getPlayerId();
		List<Object> sellList = new ArrayList<Object>();
		if(ioMessage.getInputParse("sellList") != null){
			sellList = JSON.parseArray(ioMessage.getInputParse("sellList").toString());
		}
		BagProtocol protocol = new BagProtocol();
		BagModule bagModule = ModuleManager.getModule(ModuleNames.BagModule,BagModule.class);
		bagModule.sellItem(playerID, sellList, protocol);
		ioMessage.setProtocol(protocol);
	}
}
