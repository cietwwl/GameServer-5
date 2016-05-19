package com.mi.game.module.admin.handler;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.core.util.ConfigUtil;
import com.mi.core.util.XmlTemplateUtil;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.abstrac.ResponseResult;
import com.mi.game.module.admin.protocol.BaseAdminProtocol;
import com.mi.game.module.admin.userManage.AdminModule;
import com.mi.game.module.admin.userManage.pojo.AdminRoleEntity;
import com.mi.game.module.admin.userManage.pojo.AdminUserEntity;
import com.mi.game.module.base.bean.init.ConfigVersionData;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.login.LoginModule;

@HandlerType(type = HandlerIds.updateTemplateAndRebot, order = 2)
public class RemoteUpdateRebotManageHandler extends BaseHandler {

	private static final String FINAL_CDN_ADDRESS_CONFIG = "http://180.150.177.186/Resources/#/Data/Config/ConfigVersionPrototype.xml";
	private static final String FINAL_CDN_ADDRESS = "http://180.150.177.186/Resources/#/Data/Config/";
	private static final String SHPATH = ConfigUtil.getString("shPath","/data/local/server/startup.sh");
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
				if (role.getPermission().contains("U")) {
					result = ResponseResult.OK;
				}
				break;
			}
		}
		if (result == ResponseResult.OK) {
			try {
				String cdnUrl = (String) ioMessage.getInputParse("cdnUrl");
				String CDN_ADDRESS = FINAL_CDN_ADDRESS.replace("#", cdnUrl);
				String CDN_ADDRESS_CONFIG = FINAL_CDN_ADDRESS_CONFIG.replace("#", cdnUrl);

				Map<String, ConfigVersionData> configVersionData = LoginModule.configVersionData;
				Document document = new SAXReader().read(CDN_ADDRESS_CONFIG);
				List<ConfigVersionData> entityList = null;
				entityList = new ArrayList<ConfigVersionData>();
				List<JSONObject> list = XmlTemplateUtil.loadXmlTemplate(document);
				if (list != null) {
					for (int i = 0; i < list.size(); i++) {
						JSONObject bean = list.get(i);
						ConfigVersionData entity = JSON.parseObject(bean.toJSONString(), ConfigVersionData.class);
						if (entity != null) {
							entityList.add(entity);
						}
					}
				}

				boolean needRestart = false;
				for (ConfigVersionData data : entityList) {
					if (data.getServerType() == 1) {
						ConfigVersionData localVersion = configVersionData.get(data.getName());
						if (localVersion != null && data.getVersion() > localVersion.getVersion()) {
							String fileName = data.getName() + "." + data.getType();
							String localTemplateFile = TemplateManageHandler.class.getResource("/com/mi/template").getPath() + "/" + fileName;
							String remoteTemplateFile = CDN_ADDRESS + fileName;
							Document remoteFile = new SAXReader().read(remoteTemplateFile);
							// 增加判断远程文件内容不为空
							if (remoteFile.hasContent()) {
								needRestart = true;
								XMLWriter writer = new XMLWriter(new OutputStreamWriter(new FileOutputStream(localTemplateFile), "UTF-8"));
								writer.write(remoteFile);
								writer.close();
							}
						}
					}
				}
				if (needRestart) {
					Runtime.getRuntime().exec(SHPATH);
					Runtime.getRuntime().exit(0);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		protocol.put("code", 1);
		protocol.put("result", result);
		ioMessage.setOutputResult(protocol);
	}
}
