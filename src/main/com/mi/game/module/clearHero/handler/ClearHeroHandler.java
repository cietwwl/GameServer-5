package com.mi.game.module.clearHero.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.core.protocol.BaseProtocol;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.clearHero.ClearHeroModule;
@HandlerType(type = HandlerIds.clearHero)
public class ClearHeroHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		ClearHeroModule clearHeroModule = ModuleManager.getModule(ModuleNames.ClearHeroModule, ClearHeroModule.class);
		BaseProtocol baseProtocol = new BaseProtocol();
		try {
			clearHeroModule.clearHero();
			clearHeroModule.searchItemNum();
		} catch (IllegalArgumentException e) {
			baseProtocol.setCode(Integer.parseInt(e.getMessage()));
		}
		ioMessage.setProtocol(baseProtocol);
	}
}
