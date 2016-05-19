package com.mi.game.module.admin.handler;

import java.io.File;
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

@HandlerType(type = HandlerIds.confdataListCMD, order = 2)
public class TemplateManageHandler extends BaseHandler {
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
				if (role.getPermission().contains("R")) {
					result = ResponseResult.OK;
				}
				break;
			}
		}
		if(result == ResponseResult.OK){
			String confdata = (String) ioMessage.getInputParse("confdata");
			String path = TemplateManageHandler.class.getResource("/com/mi/template").getPath();
			File confDir = new File(path);
			File[] fileArray = confDir.listFiles();
			List<Map<String, String>> fileNames = new ArrayList<Map<String, String>>();
			for (File file : fileArray) {
				String fileName = file.getName().toLowerCase();
				if (StringUtils.isNotBlank(confdata)) {
					confdata = confdata.toLowerCase();
					if (file.isDirectory()) {
						File[] fileArray2 = file.listFiles();
						for (File file2 : fileArray2) {
							fileName = file2.getName().toLowerCase();
							if (fileName.indexOf(confdata) != -1) {
								Map<String, String> map = new HashMap<String, String>();
								map.put("fileName", file2.getName());
								fileNames.add(map);
							}
						}
					} else {
						if (fileName.indexOf(confdata) != -1) {
							Map<String, String> map = new HashMap<String, String>();
							map.put("fileName", file.getName());
							fileNames.add(map);
						}
					}
				} else {
					if (file.isDirectory()) {
						File[] fileArray2 = file.listFiles();
						for (File file2 : fileArray2) {
							Map<String, String> map = new HashMap<String, String>();
							map.put("fileName", file2.getName());
							fileNames.add(map);
						}
					} else {
						Map<String, String> map = new HashMap<String, String>();
						map.put("fileName", file.getName());
						fileNames.add(map);
					}
				}
			}
			protocol.put("confList", fileNames);
		}
		protocol.put("code", 1);
		protocol.put("result", result);
		ioMessage.setOutputResult(protocol);
	}

}
