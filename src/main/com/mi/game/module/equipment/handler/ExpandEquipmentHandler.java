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
 * 扩展装备背包
 * 2014年6月20日 下午4:00:15
 */
@HandlerType(type = HandlerIds.ExpandEquipBag)
public class ExpandEquipmentHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String playerID = ioMessage.getPlayerId();
		EquipmentModule equipmentModule = ModuleManager.getModule(ModuleNames.EquipmentModule,EquipmentModule.class);
		EquipmentProtocol protocol = new EquipmentProtocol();
		equipmentModule.expandEquipmentBag(playerID, ioMessage, protocol);
		ioMessage.setProtocol(protocol);
	}
}
