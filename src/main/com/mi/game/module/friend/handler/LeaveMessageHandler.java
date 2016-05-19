package com.mi.game.module.friend.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.friend.FriendModule;
import com.mi.game.module.friend.protocol.FriendProtocol;
@HandlerType(type = HandlerIds.leaveFriendMessage)
public class LeaveMessageHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String playerID = ioMessage.getPlayerId();
		String friendID = null;
		String msg = null;
		if(ioMessage.getInputParse("friendID") != null){
			friendID = ioMessage.getInputParse("friendID").toString();
		}
		if(ioMessage.getInputParse("msg") != null){
			msg = ioMessage.getInputParse("msg").toString();
		}
		FriendModule friendModule = ModuleManager.getModule(ModuleNames.FriendMoudle,FriendModule.class);
		FriendProtocol protocol  = new FriendProtocol();
		
		try{
			friendModule.leaveMessage(playerID, friendID, msg);
		}catch(IllegalArgumentException ex){
			protocol.setCode(Integer.parseInt(ex.getMessage()));
		}
		ioMessage.setProtocol(protocol);
	}
}
