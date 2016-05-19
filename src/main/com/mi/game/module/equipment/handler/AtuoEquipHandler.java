package com.mi.game.module.equipment.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.equipment.EquipmentModule;
import com.mi.game.module.equipment.protocol.EquipmentProtocol;

/**
 * @author 刘凯旋	
 * 一键装备handler
 * 2014年6月19日 下午9:22:06
 */
@HandlerType(type = HandlerIds.AtuoEquip)
public class AtuoEquipHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String playerID = ioMessage.getInputParse("playerID").toString();
		long heroID = 0;
		if(ioMessage.getInputParse("heroID") != null){
			heroID = Long.parseLong(ioMessage.getInputParse("heroID").toString());
		}
		EquipmentProtocol protocol = new EquipmentProtocol();
		EquipmentModule equipmentModule = ModuleManager.getModule(ModuleNames.EquipmentModule,EquipmentModule.class);
		equipmentModule.autoEquip(playerID, heroID, protocol,ioMessage);
		ioMessage.setProtocol(protocol);
	}
}
