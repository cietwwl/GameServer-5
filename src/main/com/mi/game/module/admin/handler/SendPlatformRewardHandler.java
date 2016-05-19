package com.mi.game.module.admin.handler;

import java.util.List;

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
import com.mi.game.module.reward.RewardModule;

@HandlerType(type = HandlerIds.sendPlatformReward)
public class SendPlatformRewardHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
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
		protocol.put("result", result);
		protocol.put("code", 1);
		// 有C权限
		if (result == ResponseResult.OK) {
			RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule,RewardModule.class);
			String rewardKey = "";
			if(ioMessage.getInputParse("rewardKey") != null){
				rewardKey = ioMessage.getInputParse("rewardKey").toString();
			}
			try{
				String msg = "";
				String platform = "";
				String title = "";
				if(ioMessage.getInputParse("msg") != null){
					msg = ioMessage.getInputParse("msg").toString();
				}
				if(ioMessage.getInputParse("platform") != null){
					platform = ioMessage.getInputParse("platform").toString();
				}
				if(ioMessage.getInputParse("title") != null){
					title = ioMessage.getInputParse("title").toString();
				}
				rewardModule.addMsgAndReward(platform, rewardKey, msg,title);
			}catch(IllegalArgumentException ex){
				protocol.setCode(Integer.parseInt(ex.getMessage()));
				protocol.put("result", ResponseResult.ERROR);
			}
		}
		ioMessage.setOutputResult(protocol);
		
	}
}
