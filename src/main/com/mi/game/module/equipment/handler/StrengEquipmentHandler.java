package com.mi.game.module.equipment.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.equipment.EquipmentModule;
import com.mi.game.module.equipment.protocol.EquipmentProtocol;
@HandlerType(type = HandlerIds.StrengEquip)
public class StrengEquipmentHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String playerID = ioMessage.getInputParse("playerID").toString();
		long equipID = 0;
		int strengType = 0;
		if(ioMessage.getInputParse("equipID") != null)
			equipID = Long.parseLong(ioMessage.getInputParse("equipID").toString());
		if(ioMessage.getInputParse("strengType") != null)
			strengType = Integer.parseInt(ioMessage.getInputParse("strengType").toString());
		EquipmentProtocol protocol = new EquipmentProtocol();
		EquipmentModule equipModule = ModuleManager.getModule(ModuleNames.EquipmentModule,EquipmentModule.class);
		if(strengType == 1){
			equipModule.autoStreng(playerID,equipID,protocol,ioMessage);
		}else{
			equipModule.strengEquipment(playerID, equipID, protocol,ioMessage);
		}
		ioMessage.setProtocol(protocol);
	}
}
