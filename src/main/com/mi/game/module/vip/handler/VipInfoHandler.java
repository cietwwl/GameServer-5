package com.mi.game.module.vip.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.vip.VipModule;
import com.mi.game.module.vip.protocol.VipProtocol;

@HandlerType(type = HandlerIds.VIP_GETINFO)
public class VipInfoHandler extends BaseHandler {

	@Override
	public void execute(IOMessage ioMessage) {
		String playerID = ioMessage.getPlayerId();
		VipModule vipModule = ModuleManager.getModule(ModuleNames.VipModule, VipModule.class);
		VipProtocol protocol = new VipProtocol();
		vipModule.vipInfo(playerID, protocol);
		ioMessage.setProtocol(protocol);
	}
}
