package com.mi.game.module.pet.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.pet.PetModule;
import com.mi.game.module.pet.protocol.PetInfoProtocol;


/**
 * @author 刘凯旋	
 * 扩展背包
 * 2014年7月1日 下午12:44:43
 */
@HandlerType(type = HandlerIds.ExpandPetBag)
public class ExpandBagHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
//		String playerID = ioMessage.getPlayerId();
//		PetInfoProtocol protocol = new PetInfoProtocol();
//		PetModule petModule = ModuleManager.getModule(ModuleNames.PetModule,PetModule.class);
//		petModule.expandPetBag(playerID, protocol);
//		ioMessage.setProtocol(protocol);
	}
}
