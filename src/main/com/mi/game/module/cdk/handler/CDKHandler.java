package com.mi.game.module.cdk.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.cdk.CDKModule;
import com.mi.game.module.cdk.protocol.CDKProtocol;

@HandlerType(type = HandlerIds.CDK_USE)
public class CDKHandler extends BaseHandler {

	@Override
	public void execute(IOMessage ioMessage) {
		String playerID = ioMessage.getPlayerId();
		String cdk = (String) ioMessage.getInputParse("cdk");
		CDKModule cdkModule = ModuleManager.getModule(ModuleNames.CDKModule, CDKModule.class);
		CDKProtocol protocol = new CDKProtocol();
		cdkModule.useCDK(playerID, cdk, protocol);
		ioMessage.setOutputResult(protocol);
	}

}
