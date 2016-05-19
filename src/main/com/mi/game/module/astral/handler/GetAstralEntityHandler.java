package com.mi.game.module.astral.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.astral.AstralModule;
import com.mi.game.module.astral.pojo.AstralEntity;
import com.mi.game.module.astral.protocol.AstralProtocol;
import com.mi.game.module.base.handler.BaseHandler;
@HandlerType(type = HandlerIds.getAstralInfo)
public class GetAstralEntityHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String playerID = ioMessage.getPlayerId();
		AstralModule astralModule = ModuleManager.getModule(ModuleNames.AstralModule,AstralModule.class);
		AstralEntity astralEntity = astralModule.getEntity(playerID);
		AstralProtocol astralProtocol = new AstralProtocol();
		astralProtocol.setAstralEntity(astralEntity);
		ioMessage.setProtocol(astralProtocol);
	}
}
