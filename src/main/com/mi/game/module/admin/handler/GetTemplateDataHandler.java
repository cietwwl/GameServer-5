package com.mi.game.module.admin.handler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
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

@HandlerType(type = HandlerIds.confdataShowCMD)
public class GetTemplateDataHandler extends BaseHandler {
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
				if (role.getPermission().contains("R")) {
					result = ResponseResult.OK;
				}
				break;
			}
		}
		
		if(result == ResponseResult.OK){
			String fileName = ioMessage.getInputParse("fileName").toString();
			String path = TemplateManageHandler.class.getResource("/com/mi/template/").getPath();
			String filePath = path + fileName;
			StringBuffer stringBuffer = new StringBuffer();
			try {
	          String encoding="UTF-8";
	          File file=new File(filePath);
	          if(file.isFile() && file.exists()){ //判断文件是否存在
	              InputStreamReader read = new InputStreamReader(
	              new FileInputStream(file),encoding);//考虑到编码格式
	              BufferedReader bufferedReader = new BufferedReader(read);
	              String lineTxt = null;
	              while((lineTxt = bufferedReader.readLine()) != null){
	            	  stringBuffer.append(lineTxt + "\r");
	              }
	              read.close();
	      }else{
	          System.out.println("找不到指定的文件");
	      }
	      } catch (Exception e) {
	          System.out.println("读取文件内容出错");
	          e.printStackTrace();
	      }
			protocol.put("content",stringBuffer.toString());
			protocol.put("fileName",fileName);
		}
		protocol.put("result", result);
		protocol.put("code", 1);
		ioMessage.setProtocol(protocol);
	}
}
