package com.mi.game.module.worldBoss;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import com.mi.core.cache.QueryInfo;
import com.mi.core.cache.QueryType;
import com.mi.core.cache.bean.QueryBean;
import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.TemplateManager;
import com.mi.core.engine.annotation.Module;
import com.mi.core.job.BaseJob;
import com.mi.core.job.QuartzJobService;
import com.mi.core.util.DateTimeUtil;
import com.mi.game.defines.ActionType;
import com.mi.game.defines.ErrorIds;
import com.mi.game.defines.KindIDs;
import com.mi.game.defines.ModuleNames;
import com.mi.game.defines.RewardType;
import com.mi.game.defines.SysConstants;
import com.mi.game.module.analyse.AnalyseModule;
import com.mi.game.module.base.BaseModule;
import com.mi.game.module.drop.DropModule;
import com.mi.game.module.event.EventModule;
import com.mi.game.module.event.define.EventConstans;
import com.mi.game.module.event.pojo.EventConfigEntity;
import com.mi.game.module.event.util.EventUtils;
import com.mi.game.module.festival.FestivalModule;
import com.mi.game.module.festival.define.FestivalConstants;
import com.mi.game.module.legion.LegionModule;
import com.mi.game.module.legion.pojo.LegionEntity;
import com.mi.game.module.login.LoginModule;
import com.mi.game.module.login.pojo.PlayerEntity;
import com.mi.game.module.mainTask.MainTaskModule;
import com.mi.game.module.reward.RewardModule;
import com.mi.game.module.reward.data.GoodsBean;
import com.mi.game.module.worldBoss.dao.PlayerBossEntityDAO;
import com.mi.game.module.worldBoss.dao.TopTenListEntityDAO;
import com.mi.game.module.worldBoss.dao.WorldBossEntityDAO;
import com.mi.game.module.worldBoss.data.WorldBossData;
import com.mi.game.module.worldBoss.data.WorldBossJobData;
import com.mi.game.module.worldBoss.job.SettleWorldBossTask;
import com.mi.game.module.worldBoss.pojo.PlayerBossEntity;
import com.mi.game.module.worldBoss.pojo.TopTenListEntity;
import com.mi.game.module.worldBoss.pojo.WorldBossDamageInfo;
import com.mi.game.module.worldBoss.pojo.WorldBossEntity;
import com.mi.game.module.worldBoss.protocol.TopTenInfo;
import com.mi.game.module.worldBoss.protocol.WorldBossProtocol;
import com.mi.game.util.Utilities;

@Module(name = ModuleNames.WorldBossModule, clazz = WorldBossModule.class)
public class WorldBossModule extends BaseModule {
	private final WorldBossEntityDAO worldBossEntityDAO = WorldBossEntityDAO.getInstance();
	private final PlayerBossEntityDAO playerBossEntityDAO = PlayerBossEntityDAO.getInstance();
	private final TopTenListEntityDAO tenListEntityDAO = TopTenListEntityDAO.getInstance();
	private final int bossID = 10761;
	private final int bossNoFightState = 0;
	private final int bossFightState = 1;
	private final int maxInspireNum = 20;
	private final long attackInterval = 45 * DateTimeUtil.ONE_SECOND_TIME_MS;
	private final long inspireInterval = 30 * DateTimeUtil.ONE_SECOND_TIME_MS;
	private List<WorldBossDamageInfo> damageList;
	private final int silverType = 0;
	private final int goldType = 1;
	private final static int settleJobID = 9008001;

	@Override
	public void init() {
		initBossEntity();
		damageList = new ArrayList<>();
		initTopTenEntity();
		initScheduler();
	}

	@SuppressWarnings("unchecked")
	private void initScheduler() {
		WorldBossJobData jobData = TemplateManager.getTemplateData(settleJobID, WorldBossJobData.class);
		if (jobData == null) {
			if (logger.isErrorEnabled()) {
				throw new IllegalArgumentException(ErrorIds.WorldBossJobDataWrong + "");
			}
		}
		QuartzJobService quartz = QuartzJobService.getInstance();
		String startTime = jobData.getStartTime();
		Date date = DateTimeUtil.getDate(startTime);
		Date nowDate = DateTimeUtil.getDate();
		if (date.before(nowDate)) {
			date.setTime(date.getTime() + DateTimeUtil.ONE_DAY_TIME_MS);
		}
		// Date date = new Date(System.currentTimeMillis() + 20 *
		// DateTimeUtil.ONE_MINUTE_TIME_MS);
		String timeString = DateTimeUtil.getStringDate(date);
		Class<? extends BaseJob> classz = null;
		try {
			classz = (Class<? extends BaseJob>) Class.forName(jobData.getCls());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		if (classz == null) {
			throw new IllegalArgumentException(ErrorIds.WorldBossJobDataWrong + "");
		}
		quartz.createJob(jobData.getCount(), jobData.getInterval(), timeString, classz);
	}

	public void saveBossEntity(WorldBossEntity entity) {
		worldBossEntityDAO.save(entity);
	}

	public WorldBossEntity getWorldBossEntity(int bossID) {
		WorldBossEntity bossEntity = worldBossEntityDAO.getEntity(bossID + "");
		if (bossEntity == null) {
			logger.error("世界boss实体为空");
			throw new IllegalArgumentException(ErrorIds.NoEntity + "");
		}
		return bossEntity;
	}

	public WorldBossEntity getWorldBossEntity(int bossID, IOMessage ioMessage) {
		WorldBossEntity worldBossEntity = null;
		if (ioMessage != null) {
			String key = WorldBossEntity.class.getName();
			worldBossEntity = (WorldBossEntity) ioMessage.getInputParse().get(key);
			if (worldBossEntity == null) {
				worldBossEntity = this.getWorldBossEntity(bossID);
				ioMessage.getInputParse().put(key, worldBossEntity);
			}
		} else {
			worldBossEntity = this.getWorldBossEntity(bossID);
		}
		return worldBossEntity;
	}

	public void initBossEntity() {
		WorldBossEntity bossEntity = worldBossEntityDAO.getEntity(bossID + "");
		if (bossEntity == null) {
			WorldBossData data = TemplateManager.getTemplateData(bossID, WorldBossData.class);
			long basehp = data.getBaseHP();
			String startTime = data.getStartTime();
			Date startDate = DateTimeUtil.getDate(startTime);
			// long nowTime = System.currentTimeMillis();
			// Date startDate = new Date(nowTime + 20 *
			// DateTimeUtil.ONE_MINUTE_TIME_MS);
			Date nowDate = new Date();
			if (nowDate.after(startDate)) {
				startDate.setTime(startDate.getTime() + DateTimeUtil.ONE_DAY_TIME_MS);
			}
			WorldBossEntity entity = new WorldBossEntity();
			entity.setKey(bossID);
			entity.setBossHp(basehp);
			entity.setStartTime(startDate.getTime());
			entity.setEndTime(entity.getStartTime() + data.getActiveTime());
			this.saveBossEntity(entity);
		}
	}

	private void initTopTenEntity() {
		TopTenListEntity entity = this.getTopTenEntity();
		if (entity == null) {
			entity = new TopTenListEntity();
			entity.setKey("1");
			this.saveTopTenEntity(entity);
		}
	}

	private void saveTopTenEntity(TopTenListEntity entity) {
		tenListEntityDAO.save(entity);
	}

	private TopTenListEntity getTopTenEntity() {
		return tenListEntityDAO.getEntity("1");
	}

	public void savePlayerBossEntity(PlayerBossEntity entity) {
		playerBossEntityDAO.save(entity);
	}

	public PlayerBossEntity getPlayerBossEntity(String playerID) {
		PlayerBossEntity playerBossEntity = playerBossEntityDAO.getEntity(playerID);
		if (playerBossEntity == null) {
			playerBossEntity = this.initPlayerBossEntity(playerID);
			this.savePlayerBossEntity(playerBossEntity);
		}
		return playerBossEntity;
	}

	public PlayerBossEntity getPlayerBossEntity(String playerID, IOMessage ioMessage) {
		PlayerBossEntity playerBossEntity = null;
		if (ioMessage != null) {
			String entityName = PlayerBossEntity.class.getName();
			playerBossEntity = (PlayerBossEntity) ioMessage.getInputParse().get(entityName);
			if (playerBossEntity == null) {
				playerBossEntity = this.getPlayerBossEntity(playerID);
				ioMessage.getInputParse().put(entityName, playerBossEntity);
			}
		} else {
			playerBossEntity = this.getPlayerBossEntity(playerID);
		}
		return playerBossEntity;
	}

	public PlayerBossEntity initPlayerBossEntity(String playerID) {
		PlayerBossEntity entity = new PlayerBossEntity();
		entity.setKey(playerID);
		return entity;
	}

	/**
	 * 获取boss信息
	 * */
	public void getBossInfo(String playerID, WorldBossProtocol protocol) {
		WorldBossEntity entity = this.getWorldBossEntity(bossID);
		long startTime = entity.getStartTime();
		long endTime = entity.getEndTime();
		long nowTime = System.currentTimeMillis();
		int state = bossNoFightState;
		long hp = entity.getBossHp();
		if (nowTime > startTime && nowTime < endTime) {
			state = bossFightState;
		}
		if (state == bossFightState && hp > 0) {
			PlayerBossEntity playerBossEntity = this.getPlayerBossEntity(playerID);
			int attackNum = playerBossEntity.getAttackNum();
			long damage = playerBossEntity.getDamage();
			int inspireNum = playerBossEntity.getInspireNum();
			int reviveNum = playerBossEntity.getReviveNum();
			protocol.setAttackNum(attackNum);
			protocol.setDamage(damage);
			protocol.setInspireNum(inspireNum);
			protocol.setReviveNum(reviveNum);
			protocol.setBossHp(hp);
			protocol.setEndTime(endTime);
			protocol.setState(state);
			protocol.setLevel(entity.getLevel());
			protocol.setLastAttackTime(playerBossEntity.getLastAttackTime());
			protocol.setLastInspireTime(playerBossEntity.getLastInspireTime());
			if (damage != 0) {
				protocol.setRank(this.getMyRank(playerID));
			}
		} else {
			List<String> topNameList = entity.getTopNameList();
			String killName = entity.getKillName();
			protocol.setTopNameList(topNameList);
			protocol.setKillName(killName);
			protocol.setStartTime(startTime);
		}
	}

	/**
	 * 攻击boss
	 * */
	public synchronized void attackBoss(String playerID, long damage, WorldBossProtocol protocol, IOMessage ioMessage) {
		if (damage < 0) {
			throw new IllegalArgumentException(ErrorIds.ParamWrong + "");
		}
		PlayerBossEntity playerBossEntity = this.getPlayerBossEntity(playerID, ioMessage);
		long nowTime = System.currentTimeMillis();
		if (playerBossEntity.getLastAttackTime() > nowTime) {
			throw new IllegalArgumentException(ErrorIds.BossAttackTimeNoReach + "");
		}

		WorldBossEntity bossEntity = this.getWorldBossEntity(bossID, ioMessage);
		long hp = bossEntity.getBossHp();
		long maxHp = this.getBossMaxHp(bossEntity.getLevel());
		if (hp <= 0 || nowTime < bossEntity.getStartTime() || nowTime > bossEntity.getEndTime()) {
			protocol.setOver(true);
			protocol.setKillName(bossEntity.getKillName());
			if (playerBossEntity.getDamage() > 0) {
				long rank = this.getMyRank(playerID);
				protocol.setRank(rank);
				protocol.setDamage(playerBossEntity.getDamage());
				protocol.setGoodsList(this.getReward(rank));
				int eventID = this.getEventInfo(playerID, playerBossEntity.getDamage(), maxHp);
				if (eventID != 0) {
					protocol.setEventID(eventID);
				}
			}
			return;
		}
		playerBossEntity.addAttackNum();
		playerBossEntity.addDamage(damage);
		playerBossEntity.setLastAttackTime(nowTime + attackInterval);
		hp -= damage;
		LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule, LoginModule.class);
		PlayerEntity playerEntity = loginModule.getPlayerEntity(playerID);
		String name = playerEntity.getNickName();
		if (hp <= 0) {
			// 击杀世界boss,添加一次奖励
			GoodsBean killDropBean = DropModule.doDrop(1037726);
			this.savePlayerBossEntity(playerBossEntity);
			if (playerBossEntity.getDamage() > 0) {
				long rank = this.getMyRank(playerID);
				protocol.setRank(rank);
				protocol.setGoodsList(this.getReward(rank));
				if (killDropBean.getPid() != SysConstants.emptyPid) {
					protocol.getGoodsList().add(killDropBean);
				}
				int eventID = this.getEventInfo(playerID, playerBossEntity.getDamage(), maxHp);
				if (eventID != 0) {
					protocol.setEventID(eventID);
				}
			}

			bossEntity.setKillName(playerEntity.getNickName());
			bossEntity.setKillID(playerID);
			bossEntity.setSettle(true);

			RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
			List<GoodsBean> goodsList = this.getReward(1);

			if (killDropBean.getPid() != SysConstants.emptyPid) {
				goodsList.add(killDropBean);
			}
			rewardModule.addReward(playerID, goodsList, RewardType.worldBossKillReward);
			hp = 0;
			Timer timer = new Timer();
			SettleWorldBossTask task = new SettleWorldBossTask();
			timer.schedule(task,60000);
			bossEntity.setBossHp(0);
			this.saveBossEntity(bossEntity);
			protocol.setOver(true);
			protocol.setKillName(bossEntity.getKillName());
		} else {
			bossEntity.setBossHp(hp);
			this.saveBossEntity(bossEntity);
		}
		WorldBossDamageInfo damageInfo = new WorldBossDamageInfo(name, damage);
		this.damageList.add(damageInfo);
		this.savePlayerBossEntity(playerBossEntity);
		protocol.setBossHp(hp);
		protocol.setAttackNum(playerBossEntity.getAttackNum());
		protocol.setDamage(playerBossEntity.getDamage());
		protocol.setLastAttackTime(playerBossEntity.getLastAttackTime());
		protocol.setRank(this.getMyRank(playerID));
		EventModule eventModule = ModuleManager.getModule(ModuleNames.EventModule, EventModule.class);
		eventModule.intefaceDrawIntegral(playerID, 10898);
		MainTaskModule mainTaskModule = ModuleManager.getModule(ModuleNames.MainTaskModule,MainTaskModule.class);
		mainTaskModule.updateTaskByActionType(playerID, ActionType.ATTACKWORLDBOSS, 0, null);
	}

	/**
	 * 鼓舞
	 * */
	public void inspire(String playerID, int type, WorldBossProtocol protocol, IOMessage ioMessage) {
		this.checkTime(playerID, protocol, ioMessage);
		PlayerBossEntity playerBossEntity = this.getPlayerBossEntity(playerID, ioMessage);
		int inspireNum = playerBossEntity.getInspireNum();
		if (inspireNum >= maxInspireNum) {
			throw new IllegalArgumentException(ErrorIds.MaxInspireNum + "");
		}

		int pid = 0;
		int num = 0;
		boolean success = false;
		switch (type) {
		case silverType:
			pid = KindIDs.SILVERTYPE;
			num = 1000;
			int random = Utilities.getRandomInt(100);
			int percent = 100 - inspireNum * 5;
			if (random < percent) {
				success = true;
			}
			long nowTime = System.currentTimeMillis();
			if (nowTime < playerBossEntity.getLastInspireTime()) {
				throw new IllegalArgumentException(ErrorIds.InspireCoolDown + "");
			}
			playerBossEntity.setLastInspireTime(nowTime + inspireInterval);
			break;
		case goldType:
			pid = KindIDs.GOLDTYPE;
			num = 10;
			success = true;
			break;
		default:
			throw new IllegalArgumentException(ErrorIds.WrongInspireType + "");
		}
		if (success) {
			inspireNum++;
			playerBossEntity.setInspireNum(inspireNum);

		}
		this.savePlayerBossEntity(playerBossEntity);
		RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
		Map<String, Object> itemMap = new HashMap<>();
		int code = rewardModule.useGoods(playerID, pid, num, 0, true, null, itemMap, null);
		if (code != 0) {
			protocol.setCode(code);
			return;
		}

		// ////
		// // 元宝消耗记录
		// ///
		if (pid == KindIDs.GOLDTYPE) {
			AnalyseModule analyseModule = ModuleManager.getModule(ModuleNames.AnalyseModule, AnalyseModule.class);
			analyseModule.goldCostLog(playerID, num, 1, num, "inspire", "worldBoss");
		}
		protocol.setItemMap(itemMap);
		protocol.setInspireNum(inspireNum);
		protocol.setSuccess(success);
		protocol.setLastInspireTime(playerBossEntity.getLastInspireTime());
		protocol.setRank(this.getMyRank(playerID));
	}

	private void checkTime(String playerID, WorldBossProtocol protocol, IOMessage ioMessage) {
		PlayerBossEntity playerBossEntity = this.getPlayerBossEntity(playerID, ioMessage);
		WorldBossEntity bossEntity = this.getWorldBossEntity(bossID);
		long maxHp = this.getBossMaxHp(bossEntity.getLevel());
		long nowTime = System.currentTimeMillis();
		long hp = bossEntity.getBossHp();
		if (hp <= 0 || nowTime < bossEntity.getStartTime() || nowTime > bossEntity.getEndTime()) {
			protocol.setOver(true);
			protocol.setKillName(bossEntity.getKillName());
			if (playerBossEntity.getDamage() > 0) {
				long rank = this.getMyRank(playerID);
				protocol.setRank(rank);
				protocol.setGoodsList(this.getReward(rank));
				int eventID = this.getEventInfo(playerID, playerBossEntity.getDamage(), maxHp);
				if (eventID != 0) {
					protocol.setEventID(eventID);
				}
			}
			return;
		}
	}

	/**
	 * 保存前十列表
	 * */
	public void saveTopTenList() {
		List<TopTenInfo> topTenList = new ArrayList<>();
		List<PlayerBossEntity> list = worldBossEntityDAO.getTopTenList();
		LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule, LoginModule.class);
		LegionModule legionModule = ModuleManager.getModule(ModuleNames.LegionModule, LegionModule.class);
		int i = 0;
		List<String> topThreeList = new ArrayList<>();
		for (PlayerBossEntity playerBossEntity : list) {
			String playerID = playerBossEntity.getKey().toString();
			long damage = playerBossEntity.getDamage();
			TopTenInfo topTenInfo = new TopTenInfo();
			topTenInfo.setDamage(damage);
			PlayerEntity playerEntity = loginModule.getPlayerEntity(playerID);
			topTenInfo.setLevel(playerEntity.getLevel());
			topTenInfo.setName(playerEntity.getNickName());
			topTenInfo.setPlayerID(playerID);
			topTenInfo.setPhotoID(playerEntity.getPhotoID());
			topTenList.add(topTenInfo);
			if (i < 3) {
				topThreeList.add(playerEntity.getNickName());
				i++;
			}
			String groupID = playerEntity.getGroupID();
			if (!groupID.equals("0")) {
				LegionEntity legionEntity = legionModule.getLegionEntity(groupID);
				if (legionEntity != null) {
					topTenInfo.setGroupName(legionEntity.getName());
				}
			}
		}
		TopTenListEntity tenListEntity = this.getTopTenEntity();
		tenListEntity.setTopTenList(topTenList);
		this.saveTopTenEntity(tenListEntity);
		WorldBossEntity worldBossEntity = this.getWorldBossEntity(bossID);
		worldBossEntity.setTopNameList(topThreeList);
		this.saveBossEntity(worldBossEntity);
	}

	/**
	 * 更新boss的属性
	 * */
	private WorldBossEntity changeBossPrototype(boolean win) {
		WorldBossEntity worldBossEntity = this.getWorldBossEntity(bossID);
		WorldBossData data = TemplateManager.getTemplateData(bossID, WorldBossData.class);
		int level = worldBossEntity.getLevel();
		long baseHp = data.getBaseHP();
		long levelHp = data.getLevelHP();
		if (win) {
			level++;
		} else {
			level--;
			if (level < 1) {
				level = 1;
			}
		}
		long bossHp = baseHp + level * levelHp;
		if(!win){
			worldBossEntity.setKillName(null);
		}
		worldBossEntity.setBossHp(bossHp);
		worldBossEntity.setLevel(level);

		Date startDate = DateTimeUtil.getDate(data.getStartTime());
		Date nowDate = new Date();
		if (nowDate.after(startDate)) {
			startDate.setTime(startDate.getTime() + DateTimeUtil.ONE_DAY_TIME_MS);
		}
		// Date startDate = new Date(System.currentTimeMillis() + 20 *
		// DateTimeUtil.ONE_MINUTE_TIME_MS);
		logger.error("世界boss的开启时间未" + DateTimeUtil.getStringDate(startDate));
		worldBossEntity.setStartTime(startDate.getTime());
		worldBossEntity.setEndTime(worldBossEntity.getStartTime() + data.getActiveTime());

		worldBossEntity.setSettle(false);
		worldBossEntity.setSettleTime(System.currentTimeMillis());
		this.saveBossEntity(worldBossEntity);
		return worldBossEntity;
	}

	private int getEventInfo(String playerID, long damage, long maxHp) {
		int evenetID = this.getGrade(damage, maxHp);
		EventModule eventModule = ModuleManager.getModule(ModuleNames.EventModule, EventModule.class);
		EventConfigEntity eventInfo = eventModule.getEventInfo(EventConstans.EVENT_TYPE_KILLMONSTER);
		if (eventInfo == null) {
			return 0;
		}
		if (EventUtils.isEventTime(eventInfo, EventConstans.YMDHMS) && eventInfo.getStatus() == 0) {
			return evenetID;
		} else {
			return -1;
		}

	}

	/**
	 * 发送奖励
	 * */
	public void sendReward(long maxHp) {
		logger.error("开始发放世界Boss排行奖励" + System.currentTimeMillis());
		long total = worldBossEntityDAO.getWorldBossPlayerTotal();
		QueryInfo queryInfo = new QueryInfo(1, 100, "-damage");
		queryInfo.setTotal(total);
		queryInfo.initTotalPage();
		QueryBean queryBean = new QueryBean("damage", QueryType.GREATERTHAN, 0);
		queryInfo.addQueryBean(queryBean);
		List<PlayerBossEntity> playerBossList = null;
		RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);

		// 分页查询数据
		while (queryInfo.getPage() <= queryInfo.getTotalPage()) {
			playerBossList = playerBossEntityDAO.queryPage(queryInfo);
			if (playerBossList == null || playerBossList.isEmpty()) {
				break;
			}
			for (int i = 0; i < playerBossList.size(); i++) {
				int rank = (queryInfo.getPage() - 1) * queryInfo.getSize() + (i + 1);
				List<GoodsBean> goodsList = this.getReward(rank);
				// 增加一次掉落,排名前五十：1037725;
				if (rank <= 50) {
					GoodsBean addRankBean = DropModule.doDrop(1037725);
					if (addRankBean.getPid() != SysConstants.emptyPid) {
						goodsList.add(addRankBean);
					}
				}
				PlayerBossEntity playerBossEntity = playerBossList.get(i);
				String playerID = playerBossEntity.getKey().toString();
				int evenetID = this.getGrade(playerBossEntity.getDamage(), maxHp);
				playerBossEntity.setDamage(0);
				playerBossEntity.setAttackNum(0);
				playerBossEntity.setInspireNum(0);
				playerBossEntity.setReviveNum(0);
				logger.error("奖励接收人ID:" + playerID + ",排行rank=" + rank);
				rewardModule.addReward(playerID, goodsList, RewardType.worldBossReward);
				if (evenetID != 0) {
					EventModule eventModule = ModuleManager.getModule(ModuleNames.EventModule, EventModule.class);
					eventModule.sendWorldBossReward(playerID, evenetID);
				}

			}
			playerBossEntityDAO.save(playerBossList);
			queryInfo.setPage(queryInfo.getPage() + 1);
		}
		logger.error("发放世界Boss排行奖励结束");
	}

	private long getBossMaxHp(int level) {
		WorldBossData data = TemplateManager.getTemplateData(bossID, WorldBossData.class);
		long baseHp = data.getBaseHP();
		long maxHp = baseHp + level * data.getLevelHP();
		return maxHp;
	}

	private int getGrade(long damage, long hp) {
		int grade = 0;
		long percent = damage * 100 / hp;
		if (percent >= 8) {
			grade = SysConstants.worldBossThirdGrade;
		} else if (percent >= 4 && percent < 8) {
			grade = SysConstants.worldBossSecondGrade;
		} else if (percent >= 2 && percent < 4) {
			grade = SysConstants.worldBossFristGrade;
		}
		return grade;
	}

	private List<GoodsBean> getReward(long rank) {
		List<GoodsBean> goodsList = new ArrayList<>();
		int silver = 0;
		int reputation = 0;
		if (rank == 1) {
			silver = 100000;
			reputation = 800;
		} else if (rank == 2) {
			silver = 95000;
			reputation = 800;
		} else if (rank == 3) {
			silver = 90000;
			reputation = 800;
		} else if (rank > 3 && rank < 11) {
			silver = 80000;
			reputation = 750;
		} else if (rank > 10 && rank < 31) {
			silver = 70000;
			reputation = 700;
		} else if (rank > 30 && rank < 51) {
			silver = 65000;
			reputation = 650;
		} else if (rank > 51 && rank < 101) {
			silver = 60000;
			reputation = 600;
		} else if (rank > 100 && rank < 201) {
			silver = 55000;
			reputation = 550;
		} else if (rank > 200 && rank < 301) {
			silver = 50000;
			reputation = 500;
		} else if (rank > 300 && rank < 401) {
			silver = 45000;
			reputation = 450;
		} else if (rank > 400 && rank < 501) {
			silver = 40000;
			reputation = 400;
		} else if (rank > 500 && rank < 601) {
			silver = 35000;
			reputation = 350;
		} else if (rank > 600 && rank < 701) {
			silver = 30000;
			reputation = 300;
		} else if (rank > 700 && rank < 801) {
			silver = 25000;
			reputation = 250;
		} else if (rank > 800 && rank < 901) {
			silver = 20000;
			reputation = 200;
		} else if (rank > 900 && rank < 1001) {
			silver = 15000;
			reputation = 150;
		} else {
			silver = 10000;
			reputation = 100;
		}

		GoodsBean silverBean = new GoodsBean(KindIDs.SILVERTYPE, silver);
		GoodsBean reputationBean = new GoodsBean(KindIDs.REPUTATION, reputation);

		// ///////////////////////////////////
		// 春节活动 世界boss奖励翻倍
		// /////////////
		FestivalModule festivalModule = ModuleManager.getModule(ModuleNames.FestivalModule, FestivalModule.class);
		if (festivalModule.nowFestivalActive() == FestivalConstants.NEWYEAR) {
			silverBean.setNum(silverBean.getNum() * 2);
			reputationBean.setNum(reputationBean.getNum() * 2);
		}
		goodsList.add(silverBean);
		goodsList.add(reputationBean);
		return goodsList;
	}

	/**
	 * 复活
	 * */
	public void revive(String playerID, WorldBossProtocol protocol, IOMessage ioMessage) {
		this.checkTime(playerID, protocol, ioMessage);
		PlayerBossEntity playerBossEntity = this.getPlayerBossEntity(playerID, ioMessage);
		int reviveNum = playerBossEntity.getReviveNum();
		int gold = (reviveNum + 1) * 10;
		Map<String, Object> itemMap = new HashMap<>();
		RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
		int code = rewardModule.useGoods(playerID, KindIDs.GOLDTYPE, gold, 0, true, null, itemMap, null);
		if (code != 0) {
			protocol.setCode(code);
		}

		// ////
		// // 元宝消耗记录
		// ///
		AnalyseModule analyseModule = ModuleManager.getModule(ModuleNames.AnalyseModule, AnalyseModule.class);
		analyseModule.goldCostLog(playerID, gold, 1, gold, "revive", "worldBoss");

		playerBossEntity.addReviveNum();
		protocol.setReviveNum(playerBossEntity.getReviveNum());
		protocol.setItemMap(itemMap);
		protocol.setLastAttackTime(0);
		protocol.setRank(this.getMyRank(playerID));
		playerBossEntity.setLastAttackTime(0);
		this.savePlayerBossEntity(playerBossEntity);
	}

	/**
	 * 获取前十名
	 * */
	public void getTopTen(WorldBossProtocol protocol) {
		WorldBossEntity bossEntity = this.getWorldBossEntity(bossID);
		long hp = bossEntity.getBossHp();
		long nowTime = System.currentTimeMillis();
		List<TopTenInfo> topTenList = new ArrayList<>();
		if (hp < 0 || nowTime < bossEntity.getStartTime() || nowTime > bossEntity.getEndTime()) {
			TopTenListEntity topTenListEntity = this.getTopTenEntity();
			topTenList = topTenListEntity.getTopTenList();
		} else {
			List<PlayerBossEntity> list = worldBossEntityDAO.getTopTenList();
			LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule, LoginModule.class);
			LegionModule legionModule = ModuleManager.getModule(ModuleNames.LegionModule, LegionModule.class);
			for (PlayerBossEntity playerBossEntity : list) {
				String playerID = playerBossEntity.getKey().toString();
				long damage = playerBossEntity.getDamage();
				TopTenInfo topTenInfo = new TopTenInfo();
				topTenInfo.setDamage(damage);
				PlayerEntity playerEntity = loginModule.getPlayerEntity(playerID);
				String groupID = playerEntity.getGroupID();
				if (!groupID.equals("0")) {
					LegionEntity legionEntity = legionModule.getLegionEntity(groupID + "");
					if (legionEntity != null) {
						topTenInfo.setGroupName(legionEntity.getName());
					}
				}
				topTenInfo.setLevel(playerEntity.getLevel());
				topTenInfo.setName(playerEntity.getNickName());
				topTenInfo.setPlayerID(playerID);
				topTenInfo.setPhotoID(playerEntity.getPhotoID());
				topTenList.add(topTenInfo);
			}
		}
		protocol.setTopTenList(topTenList);
	}

	/**
	 * 获取我的排名
	 * */
	private long getMyRank(String playerID) {
		PlayerBossEntity playerWBEntity = this.getPlayerBossEntity(playerID);
		long damage = playerWBEntity.getDamage();
		QueryInfo countQuery = new QueryInfo("-damage,playerID");
		countQuery.addQueryCondition("playerID", QueryType.NOT_EQUAL, playerID);
		QueryBean qb1 = new QueryBean("damage", QueryType.GREATERTHAN, damage);

		QueryBean qb2 = new QueryBean("damage", QueryType.EQUAL, damage);
		qb2.addAndQueryBean(new QueryBean("playerID", QueryType.LESSTHAN, playerID));

		countQuery.addOrQueryBean(qb1);
		countQuery.addOrQueryBean(qb2);
		return playerBossEntityDAO.queryCount(countQuery) + 1;
	}

	/**
	 * 获取世界段内的攻击显示
	 * */

	public void getAttackShowList(String playerID, long startTime, WorldBossProtocol protocol, IOMessage ioMessage) {
		WorldBossEntity bossEntity = this.getWorldBossEntity(bossID);
		this.checkTime(playerID, protocol, ioMessage);
		List<WorldBossDamageInfo> showList = new ArrayList<>();
		long endTime = startTime - 3 * DateTimeUtil.ONE_SECOND_TIME_MS;
		for (WorldBossDamageInfo damageInfo : damageList) {
			long attackTime = damageInfo.getAttackTime();
			if (attackTime < startTime && attackTime > endTime) {
				showList.add(damageInfo);
			}
		}
		protocol.setBossHp(bossEntity.getBossHp());
		protocol.setDamageList(showList);
		protocol.setRank(getMyRank(playerID));
	}

	/**
	 * 世界boss结算
	 * */
	public void settleWorldBoss(boolean isKill) {
		WorldBossEntity worldBossEntity = this.getWorldBossEntity(bossID);
		long maxHp = this.getBossMaxHp(worldBossEntity.getLevel());
		if (!isKill && !worldBossEntity.isSettle()) {
			long settleTime = worldBossEntity.getSettleTime();
			if (!DateTimeUtil.isToday(new Date(settleTime))) {
				this.saveTopTenList();
				this.sendReward(maxHp);
				this.changeBossPrototype(false);
			}
		}
		if (isKill) {
			this.saveTopTenList();
			this.sendReward(maxHp);
			this.changeBossPrototype(true);
		}
	}

}
