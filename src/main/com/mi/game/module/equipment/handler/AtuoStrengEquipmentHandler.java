package com.mi.game.module.equipment.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.equipment.EquipmentModule;
import com.mi.game.module.equipment.protocol.EquipmentProtocol;
@HandlerType(type = HandlerIds.AutoStrengEquipment)
public class AtuoStrengEquipmentHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String playerID = ioMessage.getPlayerId();
		long heroID = 0;
		if(ioMessage.getInputParse("heroID") != null)
			heroID = Long.parseLong(ioMessage.getInputParse("heroID").toString());
		EquipmentProtocol protocol = new EquipmentProtocol();
		EquipmentModule equipmentModule = ModuleManager.getModule(ModuleNames.EquipmentModule,EquipmentModule.class);
		equipmentModule.autoStrengAllEquipment(playerID, heroID, ioMessage, protocol);
		ioMessage.setProtocol(protocol);
	}
}
