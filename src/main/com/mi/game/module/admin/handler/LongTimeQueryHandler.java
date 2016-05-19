package com.mi.game.module.admin.handler;

import java.sql.Date;
import java.util.List;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.core.util.DateTimeUtil;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.abstrac.ResponseResult;
import com.mi.game.module.admin.protocol.BaseAdminProtocol;
import com.mi.game.module.admin.userManage.AdminModule;
import com.mi.game.module.admin.userManage.pojo.AdminRoleEntity;
import com.mi.game.module.admin.userManage.pojo.AdminUserEntity;
import com.mi.game.module.base.handler.BaseHandler;

@HandlerType(type = HandlerIds.longTimeQuery, order = 2)
public class LongTimeQueryHandler extends BaseHandler {
	public void execute(IOMessage ioMessage) {
		
		BaseAdminProtocol protocol = new BaseAdminProtocol();
		String permission = (String) ioMessage.getInputParse("permission");
		String username = (String) ioMessage.getInputParse("username");

		AdminModule adminModule = ModuleManager.getModule(ModuleNames.AdminModule, AdminModule.class);
		AdminUserEntity userEntity = adminModule.getAdminUserEntityByName(username);

		int result = ResponseResult.PERMISSION;
		List<AdminRoleEntity> roleList = userEntity.getRoles();
		for (AdminRoleEntity role : roleList) {
			String roleUrl = role.getRoleUrl().toLowerCase();
			if (permission.equalsIgnoreCase(roleUrl)) {
				if (role.getPermission().contains("C")) {
					result = ResponseResult.OK;
				}
				break;
			}
		}
		// 有C权限
		if (result == ResponseResult.OK) {
			String time1 = (String) ioMessage.getInputParse("time1");
			String time2 = (String) ioMessage.getInputParse("time2");
			protocol.put("result", result);
			protocol.put("time1Long",  DateTimeUtil.getDate(time1).getTime());
			protocol.put("time2Long",  DateTimeUtil.getStringDate(new Date(Long.parseLong(time2))));
		}

		protocol.put("result", result);
		protocol.put("code", 1);
		ioMessage.setOutputResult(protocol);
	}

}
