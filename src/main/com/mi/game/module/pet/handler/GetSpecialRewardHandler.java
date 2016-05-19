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
 * 获得特殊技能道具
 * 2014年7月1日 下午2:58:26
 */
@HandlerType(type = HandlerIds.GetSpecialSkillItem)
public class GetSpecialRewardHandler extends BaseHandler{
		@Override
		public void execute(IOMessage ioMessage){
			String playerID = ioMessage.getPlayerId();
			long petID = 0l;
			if(ioMessage.getInputParse("petID") != null){
				petID = Long.parseLong(ioMessage.getInputParse("petID").toString());
			}
			PetInfoProtocol protocol = new PetInfoProtocol();
			PetModule petModule = ModuleManager.getModule(ModuleNames.PetModule,PetModule.class);
			petModule.getSpecialReward(playerID, petID, protocol,ioMessage);
			ioMessage.setProtocol(protocol);
		}
}
