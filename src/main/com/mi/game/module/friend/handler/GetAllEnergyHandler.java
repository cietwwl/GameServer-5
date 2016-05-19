package com.mi.game.module.friend.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.friend.FriendModule;
import com.mi.game.module.friend.protocol.FriendProtocol;
@HandlerType(type = HandlerIds.getAllFriendEnergy)
public class GetAllEnergyHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String playerID = ioMessage.getPlayerId();
		FriendProtocol protocol = new FriendProtocol();
		FriendModule friendModule = ModuleManager.getModule(ModuleNames.FriendMoudle,FriendModule.class);
		friendModule.getAllEnergy(playerID, protocol);
		ioMessage.setProtocol(protocol); 
	}
}
