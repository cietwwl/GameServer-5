package com.mi.game.module.dayTask.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.dayTask.DayTaskModule;
import com.mi.game.module.dayTask.pojo.DayTaskEntity;
import com.mi.game.module.dayTask.protocol.DayTaskProtocol;
@HandlerType(type = HandlerIds.getDayTaskInfo)
public class getDayTaskHandler extends BaseHandler{
	
	public void execute(IOMessage ioMessage){
		String playerID = ioMessage.getPlayerId();
		DayTaskModule dayTaskModule = ModuleManager.getModule(ModuleNames.DayTaskModule, DayTaskModule.class);
		DayTaskProtocol protocol = new DayTaskProtocol();
		try{
			DayTaskEntity entity = dayTaskModule.getUpdateEntity(playerID);
			protocol.setEntity(entity);
		}catch(IllegalArgumentException ex){
			protocol.setCode(Integer.parseInt(ex.getMessage()));
		}
		ioMessage.setProtocol(protocol);
	}
}
