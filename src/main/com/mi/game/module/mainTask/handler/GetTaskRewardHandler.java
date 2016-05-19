package com.mi.game.module.mainTask.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.mainTask.MainTaskModule;
import com.mi.game.module.mainTask.protocol.MainTaskProtocol;
@HandlerType(type = HandlerIds.getTaskReward)
public class GetTaskRewardHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String playerID = ioMessage.getPlayerId();
		int taskID = 0;
		if(ioMessage.getInputParse("taskID") != null){
			taskID = Integer.parseInt(ioMessage.getInputParse("taskID").toString());
		}
		MainTaskProtocol protocol = new MainTaskProtocol();
		MainTaskModule mainTaskModule = ModuleManager.getModule(ModuleNames.MainTaskModule,MainTaskModule.class);
		mainTaskModule.getTaskReward(playerID, taskID, ioMessage, protocol);
		ioMessage.setProtocol(protocol);
	}
}
