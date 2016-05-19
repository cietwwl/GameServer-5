package com.mi.game.module.mergeServer.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.core.protocol.BaseProtocol;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.mergeServer.MergeServerModule;
@HandlerType(type = HandlerIds.mergeServer)
public class MergeServerHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String mergeServerID = "";
		if(ioMessage.getInputParse("mergeServerID") != null){
			mergeServerID = ioMessage.getInputParse("mergeServerID").toString();
		}
		MergeServerModule module = ModuleManager.getModule(ModuleNames.MergeModule,MergeServerModule.class);
		module.mergeServer(mergeServerID);
		ioMessage.setProtocol(new BaseProtocol());
	}
}
