package com.mi.game.module.talisman.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.talisman.TalismanModule;
import com.mi.game.module.talisman.protocol.TalismanProtocol;

/**
 * @author 刘凯旋	
 * 卸载宝物handler
 * 2014年6月23日 下午3:56:44
 */
@HandlerType(type = HandlerIds.UnEquipTalisman)
public class UnEquipTalismanHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String playerID = ioMessage.getPlayerId();
		long equipID = 0;
		if(ioMessage.getInputParse("talismanID") != null){
			equipID = Long.parseLong(ioMessage.getInputParse("talismanID").toString());
		}
		TalismanModule equipmentModule = ModuleManager.getModule(ModuleNames.TalismanModule,TalismanModule.class);
		TalismanProtocol protocol = new TalismanProtocol();
		equipmentModule.unEquip(playerID, equipID, protocol);
		ioMessage.setProtocol(protocol);
	}
}
