package com.mi.game.module.mainTask.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.mainTask.MainTaskModule;
import com.mi.game.module.mainTask.pojo.MainTaskEntity;
import com.mi.game.module.mainTask.protocol.MainTaskProtocol;
@HandlerType(type = HandlerIds.getTaskEntity)
public class GetTaskEntiyHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String playerID = ioMessage.getPlayerId();
		MainTaskModule mainTaskModule = ModuleManager.getModule(ModuleNames.MainTaskModule,MainTaskModule.class);
		MainTaskEntity entity = mainTaskModule.getTaskEntity(playerID);
		MainTaskProtocol protocol = new MainTaskProtocol();
		protocol.setMainTaskEntity(entity);
		ioMessage.setProtocol(protocol);
	}
}
