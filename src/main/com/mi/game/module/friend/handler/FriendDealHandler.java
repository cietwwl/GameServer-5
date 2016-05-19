package com.mi.game.module.friend.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.friend.FriendModule;
import com.mi.game.module.friend.protocol.FriendProtocol;

@HandlerType(type = HandlerIds.friendHandler)
public class FriendDealHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String playerID = ioMessage.getPlayerId();
		String friendID = null;
		int index = 0;
		int handlerType = 0;
		if(ioMessage.getInputParse("friendID") != null){
			friendID = ioMessage.getInputParse("friendID").toString();
		}
		if(ioMessage.getInputParse("index") != null){
			index = Integer.parseInt(ioMessage.getInputParse("index").toString());
		}
		if(ioMessage.getInputParse("handlerType") != null){
			handlerType = Integer.parseInt(ioMessage.getInputParse("handlerType").toString());
		}
		FriendProtocol protocol = new FriendProtocol();
		FriendModule module = ModuleManager.getModule(ModuleNames.FriendMoudle,FriendModule.class);
		try{
			module.friendMsgHandler(playerID, friendID, handlerType, index, protocol);
		}catch(IllegalArgumentException ex){
			protocol.setCode(Integer.parseInt(ex.getMessage()));
		}
		ioMessage.setProtocol(protocol);
	}
}
