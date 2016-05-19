package com.mi.game.module.dayTask.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.dayTask.DayTaskModule;
import com.mi.game.module.dayTask.protocol.DayTaskProtocol;
@HandlerType(type = HandlerIds.getDayTaskReward)
public class getDayTaskRewardHandler  extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String playerID = ioMessage.getPlayerId();
		int pid = 0;
		if(ioMessage.getInputParse("pid") != null){
			pid = Integer.parseInt(ioMessage.getInputParse("pid").toString());
		}
		DayTaskProtocol protocol = new DayTaskProtocol();
		DayTaskModule dayTaskModule = ModuleManager.getModule(ModuleNames.DayTaskModule, DayTaskModule.class);
		try{
			dayTaskModule.getTaskReward(playerID, pid, protocol);
		}catch(IllegalArgumentException ex){
			protocol.setCode(Integer.parseInt(ex.getMessage()));
		}
		ioMessage.setProtocol(protocol);
	}
}
