package com.mi.game.module.cdk.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.cdk.CDKModule;
import com.mi.game.module.cdk.protocol.CDKProtocol;

@HandlerType(type = HandlerIds.CDK_BUG)
public class getBugHandler extends BaseHandler {

	@Override
	public void execute(IOMessage ioMessage) {
		CDKModule cdkModule = ModuleManager.getModule(ModuleNames.CDKModule, CDKModule.class);
		CDKProtocol protocol = new CDKProtocol();
		cdkModule.getBug();
		ioMessage.setOutputResult(protocol);
	}

}
