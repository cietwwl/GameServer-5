package com.mi.game.module.friend.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.friend.FriendModule;
import com.mi.game.module.friend.protocol.FriendProtocol;
@HandlerType(type = HandlerIds.getFriendEnergy)
public class GetFriendEnergyHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String playerID = ioMessage.getPlayerId();
		long index = 0;
		if(ioMessage.getInputParse("index") != null){
			index = Long.parseLong(ioMessage.getInputParse("index").toString());
		}
		FriendProtocol protocol = new FriendProtocol();
		FriendModule friendModule = ModuleManager.getModule(ModuleNames.FriendMoudle, FriendModule.class);
		try{
			friendModule.getEnergy(playerID, index, protocol);
		}catch(IllegalArgumentException ex){
			protocol.setCode(Integer.parseInt(ex.getMessage()));
		}
		ioMessage.setProtocol(protocol);
	}
}
