package com.mi.game.module.admin.handler;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.admin.protocol.BaseAdminProtocol;
import com.mi.game.module.admin.userManage.AdminModule;
import com.mi.game.module.admin.userManage.pojo.AdminRoleEntity;
import com.mi.game.module.base.handler.BaseHandler;

@HandlerType(type = HandlerIds.adminRoleManage, order = 2)
public class AdminRoleManageHandler extends BaseHandler {

	@Override
	public void execute(IOMessage ioMessage) {
		BaseAdminProtocol protocol = new BaseAdminProtocol();
		String roleID = (String) ioMessage.getInputParse("roleID");
		String roleType = (String) ioMessage.getInputParse("roleType");
		String roleCategory = (String) ioMessage.getInputParse("roleCategory");
		String roleName = (String) ioMessage.getInputParse("roleName");
		String roleUrl = (String) ioMessage.getInputParse("roleUrl");
		AdminModule adminModule = ModuleManager.getModule(ModuleNames.AdminModule, AdminModule.class);
		int result = 0;
		switch (roleType) {
		case "all":
			List<AdminRoleEntity> roleList = adminModule.getAllRoleEntity();
			JSONArray roles = new JSONArray();
			for (AdminRoleEntity role : roleList) {
				roles.add(JSON.toJSON(role));
			}
			protocol.put("roles", roles);
			break;
		case "add":
			result = adminModule.addRoleEntity(roleCategory, roleName, roleUrl);
			break;
		case "update":
			result = adminModule.updateRoleEntity(roleID, roleCategory, roleName, roleUrl);
			break;
		case "del":
			result = adminModule.delRole(roleID);
			break;
		case "find":
			AdminRoleEntity role = adminModule.getRoleEntity(roleID);
			protocol.put("role", JSON.toJSON(role));
			break;
		}
		protocol.put("result", result);
		protocol.put("code", 1);
		ioMessage.setOutputResult(protocol);
	}
}
