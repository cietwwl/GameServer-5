package com.mi.game.module.admin.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.admin.protocol.BaseAdminProtocol;
import com.mi.game.module.admin.userManage.AdminModule;
import com.mi.game.module.admin.userManage.pojo.AdminCategoryEntity;
import com.mi.game.module.admin.userManage.pojo.AdminRoleEntity;
import com.mi.game.module.admin.userManage.pojo.AdminUserEntity;
import com.mi.game.module.admin.userManage.pojo.VUD;
import com.mi.game.module.base.handler.BaseHandler;

@HandlerType(type = HandlerIds.adminManage, order = 2)
public class AdminUserManageHandler extends BaseHandler {

	private static List<VUD> vudList = null;

	@Override
	public void execute(IOMessage ioMessage) {
		BaseAdminProtocol protocol = new BaseAdminProtocol();
		String username = (String) ioMessage.getInputParse("username");
		String userID = (String) ioMessage.getInputParse("userID");
		String password = (String) ioMessage.getInputParse("password");
		String roles = (String) ioMessage.getInputParse("roles");
		String adminType = (String) ioMessage.getInputParse("adminType");
		AdminModule adminModule = ModuleManager.getModule(ModuleNames.AdminModule, AdminModule.class);
		int result = 0;
		switch (adminType) {
		case "all":
			List<AdminUserEntity> adminList = adminModule.getAllAdminUserEntity();
			JSONArray admins = new JSONArray();
			for (AdminUserEntity admin : adminList) {
				admin.setRoles(null);
				admins.add(JSON.toJSON(admin));
			}
			protocol.put("roles", getRoleTree());
			protocol.put("admins", admins);
			break;
		case "add":
			JSONArray roleArray = JSON.parseArray(roles);
			List<AdminRoleEntity> roleList = new ArrayList<AdminRoleEntity>();
			for (int i = 0; i < roleArray.size(); i++) {
				JSONObject role = roleArray.getJSONObject(i);
				Set<String> permission = role.keySet();
				String roleID = role.getString("id").replace("r", "");
				AdminRoleEntity roleEntity = adminModule.getRoleEntity(roleID);
				roleEntity.setPermission(permission);
				roleList.add(roleEntity);
			}
			result = adminModule.addUserEntity(username, password, roleList);
			break;
		case "update":
			JSONArray editRoleArray = JSON.parseArray(roles);
			List<AdminRoleEntity> editRoleList = new ArrayList<AdminRoleEntity>();
			for (int i = 0; i < editRoleArray.size(); i++) {
				JSONObject role = editRoleArray.getJSONObject(i);
				Set<String> permission = role.keySet();
				String roleID = role.getString("id").replace("r", "");
				AdminRoleEntity roleEntity = adminModule.getRoleEntity(roleID);
				roleEntity.setPermission(permission);
				editRoleList.add(roleEntity);
			}
			result = adminModule.updateAdminEntity(userID, username, password, editRoleList);
			break;
		case "del":
			result = adminModule.delAdmin(userID);
			break;
		case "find":
			AdminUserEntity adminEntity = adminModule.getAdminUserEntity(userID);
			protocol.put("roles", getRoleTree(adminEntity.getRoles()));
			adminEntity.setRoles(null);
			protocol.put("admin", JSON.toJSON(adminEntity));
			break;
		}
		protocol.put("result", result);
		protocol.put("code", 1);
		ioMessage.setOutputResult(protocol);
	}

	private JSONArray getRoleTree(List<AdminRoleEntity> hasRoles) {
		AdminModule adminModule = ModuleManager.getModule(ModuleNames.AdminModule, AdminModule.class);
		List<AdminCategoryEntity> categoryList = adminModule.getAllCategoryEntity();
		List<AdminRoleEntity> roleList = adminModule.getAllRoleEntity();
		JSONArray tree = new JSONArray();
		for (AdminCategoryEntity category : categoryList) {
			JSONObject cate = new JSONObject();
			cate.put("id", category.getCategoryID());
			cate.put("name", category.getCategoryName());
			tree.add(cate);
		}
		for (AdminRoleEntity role : roleList) {
			JSONObject ro = new JSONObject();
			ro.put("id", "r" + role.getRoleID());
			ro.put("pId", role.getCategoryID());
			ro.put("name", role.getRoleName());

			for (VUD vud : getVUDList()) {
				JSONObject vu = new JSONObject();
				vu.put("id", vud.getId());
				vu.put("pId", "r" + role.getRoleID());
				vu.put("name", vud.getName());
				for (AdminRoleEntity roleTemp : hasRoles) {
					if (role.getRoleID().equals(roleTemp.getRoleID())) {
						if (roleTemp.getPermission().contains(vud.getId())) {
							vu.put("checked", true);
						}
						break;
					}
				}
				tree.add(vu);
			}
			for (AdminRoleEntity roleTemp : hasRoles) {
				if (role.getRoleID().equals(roleTemp.getRoleID())) {
					ro.put("checked", true);
					break;
				}
			}

			tree.add(ro);
		}

		return tree;
	}

	private JSONArray getRoleTree() {
		AdminModule adminModule = ModuleManager.getModule(ModuleNames.AdminModule, AdminModule.class);
		List<AdminCategoryEntity> categoryList = adminModule.getAllCategoryEntity();
		List<AdminRoleEntity> roleList = adminModule.getAllRoleEntity();
		JSONArray tree = new JSONArray();
		for (AdminCategoryEntity category : categoryList) {
			JSONObject cate = new JSONObject();
			cate.put("id", category.getCategoryID());
			cate.put("name", category.getCategoryName());
			// cate.put("open", true);
			tree.add(cate);
		}
		for (AdminRoleEntity role : roleList) {
			JSONObject ro = new JSONObject();
			ro.put("id", "r" + role.getRoleID());
			ro.put("pId", role.getCategoryID());
			ro.put("name", role.getRoleName());
			tree.add(ro);
			for (VUD vud : getVUDList()) {
				JSONObject vu = new JSONObject();
				vu.put("id", vud.getId());
				vu.put("pId", "r" + role.getRoleID());
				vu.put("name", vud.getName());
				tree.add(vu);
			}
		}
		return tree;
	}

	private List<VUD> getVUDList() {
		if (vudList == null) {
			vudList = new ArrayList<VUD>();
			vudList.add(new VUD("C", "添加权限"));
			vudList.add(new VUD("R", "查看权限"));
			vudList.add(new VUD("U", "编辑权限"));
			vudList.add(new VUD("D", "删除权限"));
		}
		return vudList;
	}
}
