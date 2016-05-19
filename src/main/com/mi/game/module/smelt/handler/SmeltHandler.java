package com.mi.game.module.smelt.handler;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.smelt.SmeltModule;
import com.mi.game.module.smelt.protocol.SmeltProtocol;
@HandlerType(type = HandlerIds.Smelt)
public class SmeltHandler extends BaseHandler{
	@Override
	public void execute (IOMessage ioMessage){
		String playerID = ioMessage.getPlayerId();
		SmeltProtocol protocol = new SmeltProtocol();
		int smeltType = 0;
		if(ioMessage.getInputParse("smeltType") != null){
			smeltType = Integer.parseInt(ioMessage.getInputParse("smeltType").toString());
		}
		List<Object> smeltList = new ArrayList<>();
		if(ioMessage.getInputParse("smletList") != null){
			smeltList = JSON.parseArray(ioMessage.getInputParse("smletList").toString());
		}
		SmeltModule smeltModule = ModuleManager.getModule(ModuleNames.SmeltModule,SmeltModule.class);
		smeltModule.doSmelt(playerID, smeltList, smeltType, ioMessage, protocol);
		ioMessage.setProtocol(protocol);
	}
}
