package com.mi.game.module.mailBox.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.mailBox.MailBoxModule;
import com.mi.game.module.mailBox.pojo.MailBoxEntity;
import com.mi.game.module.mailBox.protocol.MailBoxProtocol;
@HandlerType(type = HandlerIds.getMailBox)
public class GetMessageHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String playerID = ioMessage.getPlayerId();
		MailBoxProtocol boxProtocol = new MailBoxProtocol();
		MailBoxModule module = ModuleManager.getModule(ModuleNames.MailBoxModule, MailBoxModule.class);
		try{
			MailBoxEntity entity = module.getMailBoxEntity(playerID);
			boxProtocol.setEntity(entity);
		}catch(IllegalArgumentException ex){
			boxProtocol.setCode(Integer.parseInt(ex.getMessage()));
		}
		ioMessage.setProtocol(boxProtocol);
	}
}
