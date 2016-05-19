package com.mi.game.module.equipment.handler;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.equipment.EquipmentModule;
import com.mi.game.module.equipment.protocol.EquipmentProtocol;

@HandlerType(type = HandlerIds.SellEquipmentShard)
public class SellEquipShardHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String playerID =  ioMessage.getPlayerId();
		List<Object> sellList = new ArrayList<>();
		if(ioMessage.getInputParse("sellList") != null){
			sellList = JSON.parseArray(ioMessage.getInputParse("sellList").toString());
		}
		EquipmentProtocol protocol  = new EquipmentProtocol();
		EquipmentModule equipmentModule = ModuleManager.getModule(ModuleNames.EquipmentModule,EquipmentModule.class);
		equipmentModule.sellEquipmentShard(playerID, sellList, protocol);
		ioMessage.setProtocol(protocol);
	}
}
