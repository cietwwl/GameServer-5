package com.mi.game.module.tower.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.tower.TowerModule;
import com.mi.game.module.tower.protocol.TowerProtocol;
@HandlerType(type = HandlerIds.challenegeHideTower)
public class ChallengeHideTowerHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String playerID = ioMessage.getPlayerId();
		boolean win = false;
		long hideID  = 0;
		TowerProtocol protocol = new TowerProtocol();
		if(ioMessage.getInputParse("win") != null){
			win = Boolean.parseBoolean(ioMessage.getInputParse("win").toString());
		}
		if(ioMessage.getInputParse("hideID") != null){
			hideID = Long.parseLong(ioMessage.getInputParse("hideID").toString());
		}
		TowerModule towerModule = ModuleManager.getModule(ModuleNames.TowerModule,TowerModule.class);
		try{
			towerModule.attackHideTower(playerID, hideID, win, protocol,ioMessage);
		}catch(IllegalArgumentException ex){
			protocol.setCode(Integer.parseInt(ex.getMessage()));
		}
		
		ioMessage.setProtocol(protocol);
	}
}
