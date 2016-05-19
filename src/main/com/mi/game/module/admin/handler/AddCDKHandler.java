package com.mi.game.module.admin.handler;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.abstrac.ResponseResult;
import com.mi.game.module.admin.protocol.BaseAdminProtocol;
import com.mi.game.module.admin.userManage.AdminModule;
import com.mi.game.module.admin.userManage.pojo.AdminRoleEntity;
import com.mi.game.module.admin.userManage.pojo.AdminUserEntity;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.cdk.CDKModule;

@HandlerType(type = HandlerIds.createCDKCMD, order = 2)
public class AddCDKHandler extends BaseHandler {
	@Override
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
			String cdkType = (String) ioMessage.getInputParse("cdkType");
			String platFrom = (String) ioMessage.getInputParse("platFrom");
			String rewardID = (String) ioMessage.getInputParse("rewardID");
			String num = (String) ioMessage.getInputParse("num");
			String startTime = (String) ioMessage.getInputParse("startTime");
			String endTime = (String) ioMessage.getInputParse("endTime");
			if (StringUtils.isEmpty(cdkType) || StringUtils.isEmpty(platFrom) || StringUtils.isEmpty(rewardID) || StringUtils.isEmpty(num)) {
				protocol.put("result", ResponseResult.OK);
				protocol.put("code", 0);
				ioMessage.setOutputResult(protocol);
				return;
			}
			CDKModule cdkModule = ModuleManager.getModule(ModuleNames.CDKModule, CDKModule.class);
			String url = cdkModule.addCDK(cdkType, platFrom, rewardID, Integer.parseInt(num), startTime, endTime);
			protocol.put("url", url);
		}
		protocol.put("result", result);
		protocol.put("code", 1);
		ioMessage.setOutputResult(protocol);
	}

}
