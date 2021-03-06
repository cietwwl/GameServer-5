package com.mi.game.module.equipment.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.equipment.EquipmentModule;
import com.mi.game.module.equipment.protocol.EquipmentProtocol;

@HandlerType(type = HandlerIds.DoEquip)
public class DoEquipHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String playerID = ioMessage.getInputParse("playerID").toString();
		long heroID = 0;
		long equipID = 0;
		if(ioMessage.getInputParse("heroID") != null){
			heroID = Long.parseLong(ioMessage.getInputParse("heroID").toString());
		}
		if(ioMessage.getInputParse("equipID") != null){
			equipID = Long.parseLong(ioMessage.getInputParse("equipID").toString());
		}
		EquipmentModule equipmentModule = ModuleManager.getModule(ModuleNames.EquipmentModule,EquipmentModule.class);
		EquipmentProtocol protocol = new EquipmentProtocol();
		equipmentModule.doEquip(playerID, heroID, equipID,protocol,ioMessage);
		ioMessage.setProtocol(protocol);
	}
}
