package com.mi.game.module.hero.job;

import java.util.ArrayList;
import java.util.List;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mi.core.cache.QueryInfo;
import com.mi.core.engine.ModuleManager;
import com.mi.core.job.BaseJob;
import com.mi.game.defines.ModuleNames;
import com.mi.game.defines.SysConstants;
import com.mi.game.module.event.EventModule;
import com.mi.game.module.hero.HeroModule;
import com.mi.game.module.login.dao.PlayerEntitiyDAO;
import com.mi.game.module.login.pojo.PlayerEntity;

//@QuartzJob(id = "FightValueUpdateJob", interval = (int) DateTimeUtil.ONE_HOUR_TIME_MS, count = 9999999)
public class FightValueUpdateJob extends BaseJob{
	Logger logger = LoggerFactory.getLogger(FightValueUpdateJob.class);
	@Override
	protected void onExecute(JobDataMap contextData, JobExecutionContext context) {
		long time = System.currentTimeMillis();
		int size = SysConstants.FightValueFreshNum;
		QueryInfo queryInfo = new QueryInfo(1,50,"-level"); 
		queryInfo.setTotal(size);
		queryInfo.initTotalPage();
		List<PlayerEntity> playerList = new ArrayList<>();
		PlayerEntitiyDAO playerEntitiyDAO = PlayerEntitiyDAO.getInstance();
		HeroModule heroModule = ModuleManager.getModule(ModuleNames.HeroModule,HeroModule.class);
		while(queryInfo.getPage()<=queryInfo.getTotalPage()){
			playerList = playerEntitiyDAO.queryPage(queryInfo);
			if(playerList == null || playerList.isEmpty()){
				break;
			}
			for(PlayerEntity playerEntity : playerList){
				if(playerEntity != null){
					try{
						String playerID = playerEntity.getKey().toString();
						int fightValue = heroModule.calculateFightValue(playerID, null);
						playerEntity.setFightValue(fightValue);
						playerEntitiyDAO.save(playerEntity);
					}catch (Exception ex){
						logger.error(ex.getMessage());
					}
				}
			}
		//	playerEntitiyDAO.save(playerList);
			queryInfo.setPage(queryInfo.getPage() + 1);
		}
		EventModule eventModule = ModuleManager.getModule(ModuleNames.EventModule,EventModule.class);
		if(eventModule.isInWarGod()){
			logger.error("刷新战斗力排行榜");
			eventModule.warGodRankList();
		}
		logger.debug("更新战斗力成功, 时间为"+ (System.currentTimeMillis() - time) +"毫秒");
	}
	
	@Override
	protected JobDataMap setParameter(JobDataMap mapData) {
		return mapData;
	}
}
