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

@HandlerType(type = HandlerIds.sendSystemReward, order = 2)
public class SendRewardHandler extends BaseHandler{
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
			int sendType = 0;
			if(ioMessage.getInputParse("sendType") != null){
				sendType  = Integer.parseInt(ioMessage.getInputParse("sendType").toString());		
			}
			String rewardKey = "";
			if(ioMessage.getInputParse("rewardKey") != null){
				rewardKey = ioMessage.getInputParse("rewardKey").toString();
			}
			try{
				if(sendType == 1){
					String playerID = "";
					if(ioMessage.getInputParse("playerID") != null){
						playerID = ioMessage.getInputParse("playerID").toString();
					}
					if(!playerID.isEmpty()){
						rewardModule.giveReward(rewardKey, playerID);
					}
				}else
				if(sendType == 2){
					rewardModule.giveAllPlayerReward(rewardKey);
				}
			}catch(IllegalArgumentException ex){
				protocol.setCode(Integer.parseInt(ex.getMessage()));
				protocol.put("result", ResponseResult.ERROR);
			}
		}
		
		ioMessage.setOutputResult(protocol);
	}
}
