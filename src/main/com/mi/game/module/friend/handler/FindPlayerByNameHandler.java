package com.mi.game.module.friend.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.friend.FriendModule;
import com.mi.game.module.friend.protocol.FriendProtocol;
@HandlerType(type = HandlerIds.findPlayerByName)
public class FindPlayerByNameHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String name = null;
		if(ioMessage.getInputParse("name") != null){
			name = ioMessage.getInputParse("name").toString();
		}
		FriendProtocol protocol = new FriendProtocol();
		FriendModule module = ModuleManager.getModule(ModuleNames.FriendMoudle,FriendModule.class);
		try{
			module.findPlayerByName(name, protocol);
		}catch(IllegalArgumentException ex){
			protocol.setCode(Integer.parseInt(ex.getMessage()));
		}
		ioMessage.setProtocol(protocol);
	}
}
