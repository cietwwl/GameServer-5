package com.mi.game.module.admin.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.mi.game.module.reward.RewardModule;
import com.mi.game.module.reward.data.GoodsBean;

@HandlerType(type = HandlerIds.addItemCMD, order = 2)
public class AddItemHandler extends BaseHandler {
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
			String playerID = (String) ioMessage.getInputParse("playerID");
			String itemParam = (String) ioMessage.getInputParse("itemParam");
			RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
			Map<String, Object> itemMap = new HashMap<String, Object>();
			List<GoodsBean> goodsList = new ArrayList<GoodsBean>();
			if (StringUtils.isNotBlank(itemParam)) {
				String[] items = itemParam.trim().split(",");
				for (String item : items) {
					String[] temp = item.split(":");
					goodsList.add(new GoodsBean(Integer.parseInt(temp[0]), Integer.parseInt(temp[1])));
				}
			}
			if (StringUtils.isNotBlank(playerID)) {
				rewardModule.addGoods(playerID, goodsList, true, null, itemMap, null);
			}
		}

		protocol.put("result", result);
		protocol.put("code", 1);
		ioMessage.setOutputResult(protocol);
	}

}
