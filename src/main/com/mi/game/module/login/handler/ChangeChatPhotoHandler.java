package com.mi.game.module.login.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.login.LoginModule;
import com.mi.game.module.login.protocol.LoginInfoProtocol;
@HandlerType(type = HandlerIds.ChangeChatPhoto)
public class ChangeChatPhotoHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String playerID = ioMessage.getPlayerId();
		int photoID = 0;
		if(ioMessage.getInputParse("photoID") != null){
			photoID = Integer.parseInt(ioMessage.getInputParse("photoID").toString());
		}
		LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule,LoginModule.class);
		loginModule.changePhotoID(playerID, photoID);
		LoginInfoProtocol protocol = new LoginInfoProtocol();
		ioMessage.setProtocol(protocol);
	}
}
