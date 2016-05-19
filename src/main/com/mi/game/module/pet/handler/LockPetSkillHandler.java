package com.mi.game.module.pet.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.pet.PetModule;
import com.mi.game.module.pet.protocol.PetInfoProtocol;
@HandlerType(type = HandlerIds.LockPetSkill)
public class LockPetSkillHandler extends BaseHandler{
	
	@Override
	public void execute(IOMessage ioMessage){
		String playerID = ioMessage.getPlayerId();
		PetInfoProtocol protocol = new PetInfoProtocol();
		long petID = 0;
		int skillID = 0;
		if(ioMessage.getInputParse("petID") != null){
			petID = Long.parseLong(ioMessage.getInputParse("petID") .toString());
		}
		if(ioMessage.getInputParse("skillID") != null){
			skillID = Integer.parseInt(ioMessage.getInputParse("skillID") .toString());
		}
		PetModule petModule = ModuleManager.getModule( ModuleNames.PetModule,PetModule.class);
		petModule.lockSkill(playerID, petID, skillID, protocol); 
		ioMessage.setProtocol(protocol);
	}
}
