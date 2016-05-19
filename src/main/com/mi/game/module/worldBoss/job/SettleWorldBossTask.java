package com.mi.game.module.worldBoss.job;

import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mi.core.engine.ModuleManager;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.worldBoss.WorldBossModule;



public class SettleWorldBossTask extends TimerTask{
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Override
	public void run() {
		try {
			WorldBossModule worldBossModule = ModuleManager.getModule(ModuleNames.WorldBossModule,WorldBossModule.class);
			worldBossModule.settleWorldBoss(true);
		}catch(Exception ex ){
			ex.printStackTrace();
			logger.error("世界boss发送奖励失败");
		}
	}
}
