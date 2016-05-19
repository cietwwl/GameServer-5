package com.mi.game.module.astral.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.astral.AstralModule;
import com.mi.game.module.astral.protocol.AstralProtocol;
import com.mi.game.module.base.handler.BaseHandler;
@HandlerType(type = HandlerIds.RefreshAstralList)
public class RefreshAstralListHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String playerID = ioMessage.getPlayerId();
		AstralProtocol protocol = new AstralProtocol();
		AstralModule module = ModuleManager.getModule(ModuleNames.AstralModule,AstralModule.class);
		module.refreshDrawList(playerID, ioMessage, protocol);
		ioMessage.setProtocol(protocol);
	}
}
