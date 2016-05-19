package com.mi.game.module.admin.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.admin.protocol.BaseAdminProtocol;
import com.mi.game.module.admin.userManage.AdminModule;
import com.mi.game.module.admin.userManage.pojo.AdminUserEntity;
import com.mi.game.module.base.handler.BaseHandler;

@HandlerType(type = HandlerIds.adminLogin, order = 2)
public class AdminLoginHandler extends BaseHandler {

	@Override
	public void execute(IOMessage ioMessage) {
		BaseAdminProtocol protocol = new BaseAdminProtocol();
		String username = (String) ioMessage.getInputParse("username");
		String loginType = (String) ioMessage.getInputParse("loginType");
		String password = (String) ioMessage.getInputParse("password");
		AdminModule adminModule = ModuleManager.getModule(ModuleNames.AdminModule, AdminModule.class);
		if ("login".equals(loginType)) {
			AdminUserEntity adminEntity = adminModule.getAdminUserEntityByName(username);
			if (adminEntity != null && password.equals(adminEntity.getPassword())) {
				adminEntity.setLastTime(System.currentTimeMillis());
				adminModule.saveUserEntity(adminEntity);
				protocol.put("result", 1);
			} else {
				protocol.put("result", 0);
			}
		} else if ("logout".equals(loginType)) {
			protocol.put("result", 1);
		}
		protocol.put("code", 1);
		ioMessage.setOutputResult(protocol);
	}
}
