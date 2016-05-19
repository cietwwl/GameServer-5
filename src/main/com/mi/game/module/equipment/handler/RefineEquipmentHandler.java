package com.mi.game.module.equipment.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.equipment.EquipmentModule;
import com.mi.game.module.equipment.protocol.EquipmentProtocol;
@HandlerType(type = HandlerIds.EquipmentRefine)
public class RefineEquipmentHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String playerID  = ioMessage.getPlayerId();
		long equipID = 0;
		if(ioMessage.getInputParse("equipID") != null){
			equipID = Long.parseLong(ioMessage.getInputParse("equipID") .toString());
		}
		int num = 0;
		if(ioMessage.getInputParse("num") != null){
			num = Integer.parseInt(ioMessage.getInputParse("num") .toString());
		}
		int refineType = 0;
		if(ioMessage.getInputParse("refineType") != null){
			refineType = Integer.parseInt(ioMessage.getInputParse("refineType") .toString());
		}
		EquipmentProtocol protocol  = new EquipmentProtocol();
		EquipmentModule equipmentModule = ModuleManager.getModule(ModuleNames.EquipmentModule,EquipmentModule.class);
		try{
			equipmentModule.refineEquip(playerID, equipID, refineType , num, protocol, ioMessage);
		}catch(IllegalArgumentException ex){
			protocol.setCode(Integer.parseInt(ex.getMessage()));
		}

		ioMessage.setProtocol(protocol);
	}
}
