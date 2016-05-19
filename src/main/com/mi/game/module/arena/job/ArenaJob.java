package com.mi.game.module.arena.job;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

import com.mi.core.cache.QueryInfo;
import com.mi.core.engine.ModuleManager;
import com.mi.core.job.BaseJob;
import com.mi.core.job.annotation.QuartzJob;
import com.mi.core.util.DateTimeUtil;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.arena.ArenaModule;
import com.mi.game.module.arena.dao.ArenaEntityDAO;
import com.mi.game.module.arena.pojo.ArenaEntity;
import com.mi.game.module.arena.pojo.LuckRankMapEntity;
import com.mi.game.module.arena.pojo.LuckyInfo;
import com.mi.game.module.login.LoginModule;
import com.mi.game.module.login.pojo.PlayerEntity;
import com.mi.game.util.Utilities;

@QuartzJob(id="ArenaJob",count=99999999,interval=(int)DateTimeUtil.ONE_DAY_TIME_S,startTime="22:00:00")
public class ArenaJob extends BaseJob{

	@Override
	protected JobDataMap setParameter(JobDataMap mapData) {
		// TODO 自动生成的方法存根
		return mapData;
	}


	@Override
	protected void onExecute(JobDataMap contextData, JobExecutionContext arg0) {
		QueryInfo query = new QueryInfo();
		long count = ArenaEntityDAO.getInstance().queryCount(query);
		QueryInfo queryInfo = new QueryInfo(1,500,"rank"); 
		queryInfo.setTotal(count);
		queryInfo.initTotalPage();
		List<ArenaEntity> arenaList = new ArrayList<>();
		ArenaModule arenaModule = ModuleManager.getModule(ModuleNames.ArenaModule,ArenaModule.class);
		LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule,LoginModule.class);
		LuckRankMapEntity luckyEntity = arenaModule.getLuckyRankList();
		Map<Long,LuckyInfo> luckList = luckyEntity.getNextRankList();
		List<LuckyInfo> oldList = new ArrayList<>();
		while(queryInfo.getPage()<=queryInfo.getTotalPage()){
			arenaList = ArenaEntityDAO.getInstance().queryPage( queryInfo);
			if(arenaList == null || arenaList.isEmpty()){
				break;
			}
			for(int i = 0;i<arenaList.size();i++){
				ArenaEntity arenaEntity = arenaList.get(i);
				String playerID = arenaEntity.getKey().toString();
				if(Utilities.isNpc(playerID)){
					continue;
				}
				long rank = arenaEntity.getRank();
				if(luckList.containsKey(rank)){
					LuckyInfo nowLuckInfo = luckList.get(rank);
					int gold = nowLuckInfo.getGold();
					arenaModule.giveLuckyReward(playerID, rank, gold);
					PlayerEntity playerEntity = loginModule.getPlayerEntity(playerID);
					
					LuckyInfo newLuckyInfo = new LuckyInfo();
					newLuckyInfo.setGold(gold);
					newLuckyInfo.setName(playerEntity.getNickName());
					newLuckyInfo.setRank(rank);
					oldList.add(newLuckyInfo);
				}
				arenaModule.givePKReward(playerID,rank);
			}
			ArenaEntityDAO.getInstance().save(arenaList);
			queryInfo.setPage(queryInfo.getPage()+1);
		}
		luckyEntity.setLastRankList(oldList);
		luckyEntity.setNextRankList(arenaModule.initLuckyList());
		arenaModule.saveArenaLuckyEntity(luckyEntity);
	}
}
