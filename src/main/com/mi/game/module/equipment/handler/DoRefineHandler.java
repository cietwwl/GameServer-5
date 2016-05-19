package com.mi.game.module.equipment.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.equipment.EquipmentModule;
import com.mi.game.module.equipment.protocol.EquipmentProtocol;
@HandlerType(type = HandlerIds.DoRefine)
public class DoRefineHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String playerID  = ioMessage.getPlayerId();
		int handleType = 0;
		if(ioMessage.getInputParse("handleType") != null){
			handleType = Integer.parseInt(ioMessage.getInputParse("handleType").toString());
		}
		long equipID = 0;
		if(ioMessage.getInputParse("equipID") != null){
			equipID = Long.parseLong(ioMessage.getInputParse("equipID").toString());
		}
		EquipmentProtocol protocol  = new EquipmentProtocol();
		EquipmentModule equipmentModule = ModuleManager.getModule(ModuleNames.EquipmentModule,EquipmentModule.class);
		equipmentModule.doRefine(playerID, handleType, equipID, protocol);
		ioMessage.setProtocol(protocol);
	}
}
