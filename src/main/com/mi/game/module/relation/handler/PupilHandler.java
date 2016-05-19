package com.mi.game.module.relation.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.relation.RelationModule;

@HandlerType(type = HandlerIds.RELATION_PUPIL)
public class PupilHandler extends BaseHandler {

	@Override
	public void execute(IOMessage ioMessage) {
		String playerID = ioMessage.getPlayerId();
		// 师傅编号
		String masterID = (String) ioMessage.getInputParse("masterID");
		RelationModule relationModule = ModuleManager.getModule(ModuleNames.RelationModule, RelationModule.class);
		relationModule.apprentice(playerID, masterID, ioMessage);
	}

}
