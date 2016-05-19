package com.mi.game.module.astral.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.astral.AstralModule;
import com.mi.game.module.astral.protocol.AstralProtocol;
import com.mi.game.module.base.handler.BaseHandler;
@HandlerType(type = HandlerIds.DoAstral)
public class DoAstralHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String playerID = ioMessage.getPlayerId();
		int starID = 0;
		if(ioMessage.getInputParse("starID") != null){
			starID = Integer.parseInt(ioMessage.getInputParse("starID").toString());
		}
		AstralProtocol protocol = new AstralProtocol();
		AstralModule module  = ModuleManager.getModule(ModuleNames.AstralModule, AstralModule.class);
		module.doAstral(playerID, starID, protocol, ioMessage);
		ioMessage.setProtocol(protocol);
	}
}
