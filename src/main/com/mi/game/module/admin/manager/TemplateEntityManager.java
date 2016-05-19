package com.mi.game.module.admin.manager;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import com.mi.core.cache.QueryInfo;
import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.pojo.BaseEntity;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.abstrac.BaseEntityManager;
import com.mi.game.module.abstrac.ResponseResult;
import com.mi.game.module.admin.handler.TemplateManageHandler;
import com.mi.game.module.admin.protocol.BaseAdminProtocol;
import com.mi.game.module.base.pojo.TemplateEntity;
import com.mi.game.module.login.LoginModule;

public class TemplateEntityManager extends BaseEntityManager<TemplateEntity>{

	@Override
	public List<? extends BaseEntity> doQueryList(QueryInfo queryInfo,
			IOMessage ioMessage) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public void updateEntity(IOMessage ioMessage) {
		BaseAdminProtocol protocol = new BaseAdminProtocol();
		// TODO 自动生成的方法存根
		String content = ioMessage.getInputParse("content").toString();
		String fileName = ioMessage.getInputParse("fileName").toString();
		String path = TemplateManageHandler.class.getResource("/com/mi/template/").getPath();
		String filePath = path + fileName;
		System.out.println(filePath);
		try {
			// 写入文件
			File file = new File(filePath);
			FileOutputStream outFile = new FileOutputStream(file);
			byte[] b = content.getBytes();
			outFile.write(b);
			outFile.close();
		} catch (Exception e) {
			System.out.println("update.properties文件写入失败!");
		}
		if(fileName.equals("serverInfo.xml")){
			LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule,LoginModule.class);
			try{
				loginModule.changeServerInfo();
			}catch(Exception ex){
				System.out.println("更新服务器错误");
				ex.printStackTrace();
				protocol.put("result", ResponseResult.ERROR);
				ioMessage.setOutputResult(protocol);
			}
		}
		protocol.put("result", ResponseResult.OK);
		protocol.put("code", 1);
		ioMessage.setOutputResult(protocol);
	
	}

}
