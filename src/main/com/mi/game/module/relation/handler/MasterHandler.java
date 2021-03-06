package com.mi.game.module.relation.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.relation.RelationModule;

@HandlerType(type = HandlerIds.RELATION_MASTER)
public class MasterHandler extends BaseHandler {

	@Override
	public void execute(IOMessage ioMessage) {
		String playerID = ioMessage.getPlayerId();
		// 徒弟编号
		String pupilID = (String) ioMessage.getInputParse("pupilID");
		RelationModule relationModule = ModuleManager.getModule(ModuleNames.RelationModule, RelationModule.class);
		relationModule.recruit(playerID, pupilID, ioMessage);
	}
}
