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
 * 装备宝物
 * 2014年6月23日 下午3:25:32
 */
@HandlerType(type = HandlerIds.EquipTalisman)
public class EquipTalismanHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String playerID = ioMessage.getPlayerId();
		TalismanProtocol protocol = new TalismanProtocol();
		long talismanID = 0;
		long heroID = 0;
		if(ioMessage.getInputParse().get("talismanID") != null){
			talismanID = Long.parseLong(ioMessage.getInputParse().get("talismanID").toString());
		}
		if(ioMessage.getInputParse().get("heroID") != null){
			heroID = Long.parseLong(ioMessage.getInputParse().get("heroID").toString());
		}
		TalismanModule talismanModule = ModuleManager.getModule(ModuleNames.TalismanModule, TalismanModule.class);
		talismanModule.doEquip(playerID, talismanID, heroID, protocol);
		ioMessage.setProtocol(protocol);
	}
}
