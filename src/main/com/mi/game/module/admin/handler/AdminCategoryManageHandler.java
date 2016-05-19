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
import com.mi.game.module.admin.userManage.pojo.AdminCategoryEntity;
import com.mi.game.module.base.handler.BaseHandler;

@HandlerType(type = HandlerIds.adminCategoryManage, order = 2)
public class AdminCategoryManageHandler extends BaseHandler {

	@Override
	public void execute(IOMessage ioMessage) {
		BaseAdminProtocol protocol = new BaseAdminProtocol();
		String categoryID = (String) ioMessage.getInputParse("categoryID");
		String categoryType = (String) ioMessage.getInputParse("categoryType");
		String categoryName = (String) ioMessage.getInputParse("categoryName");
		String categoryDesc = (String) ioMessage.getInputParse("categoryDesc");
		AdminModule adminModule = ModuleManager.getModule(ModuleNames.AdminModule, AdminModule.class);
		int result = 0;
		switch (categoryType) {
		case "all":
			List<AdminCategoryEntity> categoryList = adminModule.getAllCategoryEntity();
			JSONArray categorys = new JSONArray();
			for (AdminCategoryEntity role : categoryList) {
				categorys.add(JSON.toJSON(role));
			}
			protocol.put("categorys", categorys);
			break;
		case "add":
			result = adminModule.addCategoryEntity(categoryID, categoryName, categoryDesc);
			break;
		case "update":
			result = adminModule.updateCategoryEntity(categoryID, categoryName, categoryDesc);
			break;
		case "del":
			result = adminModule.delCategory(categoryID);
			break;
		case "find":
			AdminCategoryEntity category = adminModule.getCategoryEntity(categoryID);
			protocol.put("category", JSON.toJSON(category));
			break;
		}
		protocol.put("result", result);
		protocol.put("code", 1);
		ioMessage.setOutputResult(protocol);
	}
}
