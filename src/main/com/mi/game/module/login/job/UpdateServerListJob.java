package com.mi.game.module.login.job;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mi.core.engine.ModuleManager;
import com.mi.core.job.BaseJob;
import com.mi.core.job.annotation.QuartzJob;
import com.mi.core.util.ConfigUtil;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.login.LoginModule;

@QuartzJob(id="ServerListJob",count=99999999,interval=300)
public class UpdateServerListJob extends BaseJob{
	Logger logger =  LoggerFactory.getLogger(UpdateServerListJob.class);
	public final static boolean LOGINSERVER = ConfigUtil.getBoolean("isLoginServer", false);
	@Override
	protected JobDataMap setParameter(JobDataMap mapData) {
		// TODO 自动生成的方法存根
		return mapData;
	}

	@Override
	protected void onExecute(JobDataMap contextData, JobExecutionContext arg0) {
		// TODO 自动生成的方法存根
		if(LOGINSERVER){
			LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule,LoginModule.class);
			String md5 = loginModule.getServerMd5String();
			if(!md5.equals(LoginModule.getServerMd5())){
				logger.debug("服务器文件有更新");
				try{
					loginModule.changeServerInfo();
					LoginModule.setServerMd5(md5);
				}catch(Exception ex){
				logger.error("更新服务器错误");
					ex.printStackTrace();
				}
			}
		}
	}

}
