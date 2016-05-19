package com.mi.game.module.effect.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.effect.EffectModule;
import com.mi.game.module.effect.protocol.EffectProtocol;
@HandlerType(type = HandlerIds.addRefuseBattleHandler)
public class RefuesBattleHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String playerID = ioMessage.getPlayerId();
		int payType = 0;
		if(ioMessage.getInputParse("payType") !=  null){
			payType = Integer.parseInt(ioMessage.getInputParse("payType").toString());
		}
		EffectProtocol protocol = new EffectProtocol();
		EffectModule effectModule = ModuleManager.getModule(ModuleNames.EffectModule,EffectModule.class);
		try{
			effectModule.addRefuseBattleBuff(playerID, payType,ioMessage, protocol);
		}catch(IllegalArgumentException ex){
			protocol.setCode(Integer.parseInt(ex.getMessage()));
		}
		
		ioMessage.setProtocol(protocol);
	}
}
