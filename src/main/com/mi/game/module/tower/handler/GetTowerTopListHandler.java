package com.mi.game.module.tower.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.tower.TowerModule;
import com.mi.game.module.tower.protocol.TowerProtocol;
@HandlerType(type = HandlerIds.getTowerTopList)
public class GetTowerTopListHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		TowerModule module = ModuleManager.getModule(ModuleNames.TowerModule,TowerModule.class);
		TowerProtocol protocol = new TowerProtocol();
		module.getTopList(protocol);
		ioMessage.setProtocol(protocol);
	}
}
