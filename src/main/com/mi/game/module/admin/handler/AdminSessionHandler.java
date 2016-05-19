package com.mi.game.module.admin.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.admin.protocol.BaseAdminProtocol;
import com.mi.game.module.admin.userManage.AdminModule;
import com.mi.game.module.base.handler.BaseHandler;

@HandlerType(type = HandlerIds.adminSession, order = 2)
public class AdminSessionHandler extends BaseHandler {

	@Override
	public void execute(IOMessage ioMessage) {
		BaseAdminProtocol protocol = new BaseAdminProtocol();
		String username = (String) ioMessage.getInputParse("username");
		String password = (String) ioMessage.getInputParse("password");
		String permission = (String) ioMessage.getInputParse("permission");
		AdminModule adminModule = ModuleManager.getModule(ModuleNames.AdminModule, AdminModule.class);
		adminModule.sessionManage(username, password, permission, protocol);
		ioMessage.setOutputResult(protocol);
	}
}
