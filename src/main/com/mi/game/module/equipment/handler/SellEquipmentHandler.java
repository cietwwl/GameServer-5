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

@HandlerType(type = HandlerIds.SellEquip)
public class SellEquipmentHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String playerID = ioMessage.getInputParse("playerID").toString();
		List<Object> equipIDList = new ArrayList<Object>();
		if(ioMessage.getInputParse("equipIDList") != null){
			equipIDList = JSON.parseArray(ioMessage.getInputParse("equipIDList").toString());
		}
		EquipmentModule equipmentModule = ModuleManager.getModule(ModuleNames.EquipmentModule,EquipmentModule.class);
		EquipmentProtocol protocol = new EquipmentProtocol();
		
		equipmentModule.sellEquipment(playerID,equipIDList, protocol);
		ioMessage.setProtocol(protocol);
	}
}
