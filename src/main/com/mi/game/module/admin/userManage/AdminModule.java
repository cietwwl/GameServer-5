package com.mi.game.module.admin.userManage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mi.core.dao.KeyGeneratorDAO;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.Module;
import com.mi.game.defines.ModuleNames;
import com.mi.game.defines.SysConstants;
import com.mi.game.module.abstrac.ResponseResult;
import com.mi.game.module.admin.protocol.BaseAdminProtocol;
import com.mi.game.module.admin.userManage.dao.AdminCategoryEntityDao;
import com.mi.game.module.admin.userManage.dao.AdminRoleEntityDao;
import com.mi.game.module.admin.userManage.dao.AdminUserEntityDao;
import com.mi.game.module.admin.userManage.pojo.AdminCategoryEntity;
import com.mi.game.module.admin.userManage.pojo.AdminRoleEntity;
import com.mi.game.module.admin.userManage.pojo.AdminUserEntity;
import com.mi.game.module.base.BaseModule;

@Module(name = ModuleNames.AdminModule, clazz = AdminModule.class)
public class AdminModule extends BaseModule {

	private final KeyGeneratorDAO keyGeneratorDAO = KeyGeneratorDAO.getInstance();
	private static AdminUserEntityDao adminUserEntityDao = AdminUserEntityDao.getInstance();
	private static AdminRoleEntityDao roleEntityDao = AdminRoleEntityDao.getInstance();
	private static AdminCategoryEntityDao categoryEntityDao = AdminCategoryEntityDao.getInstance();

	@Override
	public void init() {
		initCategory();
		initRole();
		initAdmin();
	}

	public long getCategoryID() {
		String clsName = SysConstants.AdminCategoryIDEntity;
		long categoryID = keyGeneratorDAO.updateInc(clsName);
		return categoryID;
	}

	public long getAdminUserID() {
		String clsName = SysConstants.AdminUserIDEntity;
		long adminUserID = keyGeneratorDAO.updateInc(clsName);
		return adminUserID;
	}

	public long getRoleID() {
		String clsName = SysConstants.AdminRoleIDEntity;
		long roleID = keyGeneratorDAO.updateInc(clsName);
		return roleID;
	}

	private void initCategory() {
		// 初始化大类
		if (getAllCategoryEntity().size() == 0) {
			String[] categorys = RoleConstans.categorys();
			for (String category : categorys) {
				AdminCategoryEntity entity = new AdminCategoryEntity();
				entity.setCategoryID(getCategoryID() + "");
				entity.setCategoryName(category);
				entity.setCategoryDesc(category);
				saveCategoryEntity(entity);
			}
		}
	}

	private void initRole() {
		// 初始化小类
		if (getAllRoleEntity().size() == 0) {
			// 系统管理
			String[] systemConfig = RoleConstans.systemConfigs();
			for (String system : systemConfig) {
				AdminRoleEntity entity = new AdminRoleEntity();
				String[] temp = system.split("#");
				AdminCategoryEntity categoryEntity = getCategoryEntityByName("系统管理");
				entity.setCategoryID(categoryEntity.getCategoryID());
				entity.setRoleID(getRoleID() + "");
				entity.setRoleName(temp[0]);
				entity.setRoleUrl(temp[1]);
				saveRoleEntity(entity);
			}
			// cdk
			String[] cdkManage = RoleConstans.cdkManages();
			for (String cdk : cdkManage) {
				AdminRoleEntity entity = new AdminRoleEntity();
				String[] temp = cdk.split("#");
				AdminCategoryEntity categoryEntity = getCategoryEntityByName("cdk管理");
				entity.setCategoryID(categoryEntity.getCategoryID());
				entity.setRoleID(getRoleID() + "");
				entity.setRoleName(temp[0]);
				entity.setRoleUrl(temp[1]);
				saveRoleEntity(entity);
			}
			// player
			String[] playerManage = RoleConstans.playerManages();
			for (String player : playerManage) {
				AdminRoleEntity entity = new AdminRoleEntity();
				String[] temp = player.split("#");
				AdminCategoryEntity categoryEntity = getCategoryEntityByName("玩家管理");
				entity.setCategoryID(categoryEntity.getCategoryID());
				entity.setRoleID(getRoleID() + "");
				entity.setRoleName(temp[0]);
				entity.setRoleUrl(temp[1]);
				saveRoleEntity(entity);
			}
			// 财务
			String[] caiwuManage = RoleConstans.caiwuManages();
			for (String caiwu : caiwuManage) {
				AdminRoleEntity entity = new AdminRoleEntity();
				String[] temp = caiwu.split("#");
				AdminCategoryEntity categoryEntity = getCategoryEntityByName("财务管理");
				entity.setCategoryID(categoryEntity.getCategoryID());
				entity.setRoleID(getRoleID() + "");
				entity.setRoleName(temp[0]);
				entity.setRoleUrl(temp[1]);
				saveRoleEntity(entity);
			}
			// 权限
			String[] roleManage = RoleConstans.roleManages();
			for (String role : roleManage) {
				AdminRoleEntity entity = new AdminRoleEntity();
				String[] temp = role.split("#");
				AdminCategoryEntity categoryEntity = getCategoryEntityByName("权限管理");
				entity.setCategoryID(categoryEntity.getCategoryID());
				entity.setRoleID(getRoleID() + "");
				entity.setRoleName(temp[0]);
				entity.setRoleUrl(temp[1]);
				saveRoleEntity(entity);
			}
			// 游戏服信息
			String[] serverInfoManage = RoleConstans.serverInfoManages();
			for (String serverInfo : serverInfoManage) {
				AdminRoleEntity entity = new AdminRoleEntity();
				String[] temp = serverInfo.split("#");
				AdminCategoryEntity categoryEntity = getCategoryEntityByName("游戏服信息");
				entity.setCategoryID(categoryEntity.getCategoryID());
				entity.setRoleID(getRoleID() + "");
				entity.setRoleName(temp[0]);
				entity.setRoleUrl(temp[1]);
				saveRoleEntity(entity);
			}
			// 统计
			String[] analyManage = RoleConstans.analyManages();
			for (String analy : analyManage) {
				AdminRoleEntity entity = new AdminRoleEntity();
				String[] temp = analy.split("#");
				AdminCategoryEntity categoryEntity = getCategoryEntityByName("统计分析");
				entity.setCategoryID(categoryEntity.getCategoryID());
				entity.setRoleID(getRoleID() + "");
				entity.setRoleName(temp[0]);
				entity.setRoleUrl(temp[1]);
				saveRoleEntity(entity);
			}			
		}
	}

	public void sessionManage(String username, String password, String permission, BaseAdminProtocol protocol) {
		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password) || StringUtils.isEmpty(permission)) {
			protocol.put("result", ResponseResult.TIMEOUT);
			protocol.put("code", 1);
			return;
		}

		AdminUserEntity adminEntity = getAdminUserEntityByName(username);
		if (adminEntity == null) {
			protocol.put("result", ResponseResult.USERNULL);
			protocol.put("code", 1);
			return;
		}

		// 首页不做验证
		if ("index.html".equals(permission)) {
			protocol.put("result", ResponseResult.OK);
			protocol.put("roles", getRoleTree(adminEntity.getRoles()));
			protocol.put("code", 1);
			return;
		}

		int result = ResponseResult.PERMISSION;
		List<AdminRoleEntity> roleList = adminEntity.getRoles();
		for (AdminRoleEntity role : roleList) {
			String roleUrl = role.getRoleUrl();
			if (StringUtils.endsWithIgnoreCase(roleUrl, permission)) {
				Set<String> set = role.getPermission();
				if (set != null && set.contains("R")) {
					result = ResponseResult.OK;
				}
				break;
			}
		}

		if (result == ResponseResult.OK) {
			protocol.put("result", result);
			protocol.put("roles", getRoleTree(adminEntity.getRoles()));
			protocol.put("code", 1);
			return;
		} else {
			protocol.put("result", result);
			protocol.put("code", 1);
			return;
		}
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
			cate.put("open", true);
			tree.add(cate);
		}
		for (AdminRoleEntity role : roleList) {
			for (AdminRoleEntity roleTemp : hasRoles) {
				if (role.getRoleID().equals(roleTemp.getRoleID())) {
					JSONObject ro = new JSONObject();
					ro.put("id", "r" + role.getRoleID());
					ro.put("pId", role.getCategoryID());
					ro.put("name", role.getRoleName());
					ro.put("url", role.getRoleUrl());
					ro.put("target", "mainManager");
					tree.add(ro);
					break;
				}
			}
		}
		return tree;
	}

	public int addUserEntity(String username, String password, List<AdminRoleEntity> roles) {
		AdminUserEntity userEntity = getAdminUserEntityByName(username);
		if (userEntity != null) {
			// 已存在
			return 0;
		}
		userEntity = new AdminUserEntity();
		userEntity.setUserID(getAdminUserID() + "");
		userEntity.setUsername(username);
		userEntity.setPassword(password);
		userEntity.setRoles(roles);
		userEntity.setCreateTime(System.currentTimeMillis());
		saveUserEntity(userEntity);
		// 添加成功
		return 1;
	}

	public List<AdminUserEntity> getAllAdminUserEntity() {
		return adminUserEntityDao.getUserList();
	}

	public int updateRoleEntity(String roleID, String roleCategoryID, String roleName, String roleUrl) {
		AdminRoleEntity roleEntity = roleEntityDao.getEntity(roleID);
		roleEntity.setCategoryID(roleCategoryID);
		roleEntity.setRoleName(roleName);
		roleEntity.setRoleUrl(roleUrl);
		roleEntityDao.save(roleEntity);
		return 1;
	}

	public int updateAdminEntity(String userID, String username, String password, List<AdminRoleEntity> roles) {
		AdminUserEntity userEntity = getAdminUserEntity(userID);
		if (userEntity == null) {
			// 不存在
			return 0;
		}
		AdminUserEntity userEntityTemp = getAdminUserEntityByName(username);
		if (userEntityTemp == null || userEntityTemp.getUserID().equals(userID)) {
			userEntity.setUsername(username);
		} else {
			// 用户名已存在
			return 0;
		}
		userEntity.setPassword(password);
		userEntity.setRoles(roles);
		saveUserEntity(userEntity);
		return 1;
	}

	public int updateCategoryEntity(String categoryID, String categoryName, String categoryDesc) {
		AdminCategoryEntity categoryEntity = categoryEntityDao.getEntity(categoryID);
		categoryEntity.setCategoryName(categoryName);
		categoryEntity.setCategoryDesc(categoryDesc);
		categoryEntityDao.save(categoryEntity);
		return 1;
	}

	public int addRoleEntity(String roleCategoryID, String roleName, String roleUrl) {
		AdminRoleEntity roleEntity = getRoleEntityByName(roleName);
		if (roleEntity != null) {
			return 0;
		}
		roleEntity = new AdminRoleEntity();
		roleEntity.setRoleID(getRoleID() + "");
		roleEntity.setCategoryID(roleCategoryID);
		roleEntity.setRoleName(roleName);
		roleEntity.setRoleUrl(roleUrl);
		saveRoleEntity(roleEntity);
		return 1;
	}

	public int addCategoryEntity(String categoryID, String categoryName, String categoryDesc) {
		AdminCategoryEntity entity = getCategoryEntityByName(categoryName);
		if (entity != null) {
			return 0;
		}
		entity = new AdminCategoryEntity();
		entity.setCategoryID(getCategoryID() + "");
		entity.setCategoryName(categoryName);
		entity.setCategoryDesc(categoryDesc);
		saveCategoryEntity(entity);
		return 1;
	}

	public int delRole(String roleID) {
		delRoleEntity(roleID);
		return 1;
	}

	public int delCategory(String categoryID) {
		delCategoryEntity(categoryID);
		return 1;
	}

	public int delAdmin(String userID) {
		adminUserEntityDao.delEntity(userID);
		return 1;
	}

	public AdminUserEntity getAdminUserEntityByName(String username) {
		return adminUserEntityDao.getUserEntity(username);
	}

	public void saveUserEntity(AdminUserEntity userEntity) {
		adminUserEntityDao.save(userEntity);
	}

	public AdminRoleEntity getRoleEntity(String roleID) {
		return roleEntityDao.getRoleEntity(roleID);
	}

	public AdminUserEntity getAdminUserEntity(String userID) {
		return adminUserEntityDao.getEntity(userID);
	}

	public void delRoleEntity(String roleID) {
		roleEntityDao.delEntity(roleID);
	}

	public AdminRoleEntity getRoleEntityByName(String roleName) {
		return roleEntityDao.getRoleEntityByName(roleName);
	}

	public void saveRoleEntity(AdminRoleEntity roleEntity) {
		roleEntityDao.save(roleEntity);
	}

	public List<AdminRoleEntity> getAllRoleEntity() {
		return roleEntityDao.getAllRoleEntity();
	}

	public AdminCategoryEntity getCategoryEntityByName(String categoryName) {
		return categoryEntityDao.getCategoryEntityByName(categoryName);
	}

	public void delCategoryEntity(String categoryID) {
		categoryEntityDao.delEntity(categoryID);
	}

	public AdminCategoryEntity getCategoryEntity(String categoryID) {
		return categoryEntityDao.getEntity(categoryID);
	}

	public void saveCategoryEntity(AdminCategoryEntity entity) {
		categoryEntityDao.save(entity);
	}

	public List<AdminCategoryEntity> getAllCategoryEntity() {
		return categoryEntityDao.getAllCategoryEntity();
	}

	private void initAdmin() {
		if (adminUserEntityDao.getUserList().size() == 0) {
			AdminUserEntity userEntity = new AdminUserEntity();
			userEntity.setUsername("mile");
			userEntity.setPassword("mile2014.");
			List<AdminRoleEntity> roleList = getAllRoleEntity();
			Set<String> set = new HashSet<String>();
			set.add("C");
			set.add("R");
			set.add("U");
			set.add("D");
			for (AdminRoleEntity role : roleList) {
				role.setPermission(set);
			}
			userEntity.setUserID(getAdminUserID() + "");
			userEntity.setRoles(roleList);
			saveUserEntity(userEntity);
		}
	}
}
