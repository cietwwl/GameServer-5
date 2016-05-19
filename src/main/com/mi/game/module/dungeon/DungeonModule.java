package com.mi.game.module.dungeon;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.TemplateManager;
import com.mi.core.engine.annotation.Module;
import com.mi.core.util.DateTimeUtil;
import com.mi.game.defines.ActionType;
import com.mi.game.defines.ErrorIds;
import com.mi.game.defines.KindIDs;
import com.mi.game.defines.ModuleNames;
import com.mi.game.defines.SysConstants;
import com.mi.game.module.achievement.AchievementModule;
import com.mi.game.module.analyse.AnalyseModule;
import com.mi.game.module.bag.BagModule;
import com.mi.game.module.bag.pojo.BagEntity;
import com.mi.game.module.bag.pojo.BagItem;
import com.mi.game.module.base.BaseModule;
import com.mi.game.module.dayTask.DayTaskModule;
import com.mi.game.module.drop.DropModule;
import com.mi.game.module.dungeon.dao.ActLimitRewardMapDAO;
import com.mi.game.module.dungeon.dao.DungeonActMapEntityDAO;
import com.mi.game.module.dungeon.dao.DungeonActiveDAO;
import com.mi.game.module.dungeon.dao.DungeonEliteEntityDAO;
import com.mi.game.module.dungeon.dao.DungeonMapEntityDAO;
import com.mi.game.module.dungeon.data.ActData;
import com.mi.game.module.dungeon.data.ActiveTreeRewardData;
import com.mi.game.module.dungeon.data.DungeonActiveData;
import com.mi.game.module.dungeon.data.DungeonData;
import com.mi.game.module.dungeon.data.EliteDungeonData;
import com.mi.game.module.dungeon.pojo.ActLimitReward;
import com.mi.game.module.dungeon.pojo.ActLimitRewardMapEntity;
import com.mi.game.module.dungeon.pojo.ActReward;
import com.mi.game.module.dungeon.pojo.Dungeon;
import com.mi.game.module.dungeon.pojo.DungeonAct;
import com.mi.game.module.dungeon.pojo.DungeonActMapEntity;
import com.mi.game.module.dungeon.pojo.DungeonActive;
import com.mi.game.module.dungeon.pojo.DungeonActiveEntity;
import com.mi.game.module.dungeon.pojo.DungeonEliteEntity;
import com.mi.game.module.dungeon.pojo.DungeonMapEntity;
import com.mi.game.module.dungeon.protocol.DungeonProtocol;
import com.mi.game.module.dungeon.protocol.DungeonTopInfo;
import com.mi.game.module.event.EventModule;
import com.mi.game.module.event.define.EventConstans;
import com.mi.game.module.hero.HeroModule;
import com.mi.game.module.hero.pojo.Hero;
import com.mi.game.module.lead.LeadModule;
import com.mi.game.module.login.LoginModule;
import com.mi.game.module.login.pojo.NewPlayerEntity;
import com.mi.game.module.login.pojo.PlayerEntity;
import com.mi.game.module.mainTask.MainTaskModule;
import com.mi.game.module.pay.data.PayTimeRewardData;
import com.mi.game.module.reward.RewardModule;
import com.mi.game.module.reward.data.GoodsBean;
import com.mi.game.util.Utilities;

/**
 * @author 刘凯旋 副本模块 2014年6月3日 上午11:03:50
 */

@Module(name = ModuleNames.DungeonModule, clazz = DungeonModule.class)
public class DungeonModule extends BaseModule {

	private DungeonMapEntityDAO dungeonMapEntityDAO = DungeonMapEntityDAO.getInstance();
	private DungeonActMapEntityDAO dungeonActMapEntityDAO = DungeonActMapEntityDAO.getInstance();
	private final DungeonActiveDAO dungeonActiveDAO = DungeonActiveDAO.getInstance();
	private DungeonEliteEntityDAO dungeonEliteEntityDAO = DungeonEliteEntityDAO.getInstance();
	private ActLimitRewardMapDAO actLimitRewardMapDAO = ActLimitRewardMapDAO.getInstance();
	private final int addBattleNumItem = SysConstants.addDungeonItem;
	private final int goldRecoverType = 1;
	private final int itemRecoverType = 0;
	private final int goldRecoverNum = 25;
	private final int dungeonActStartID = SysConstants.dungeonActStartID;
	private final int eliteMaxPayNum = SysConstants.elitePayNum;
	private final int eliteMaxFreeNum = SysConstants.eliteFreeNum;
	private final int eliteRecoveGold = 10;
	private final int expActive = 10511;
	private final int talismanActive = 10512;
	private final int silverAcitve = 10513;
	private final int resurgenceBaseSilver = SysConstants.resurgenceBaseSilver;
	private Map<Integer, Integer> treeReward = new HashMap<>();
	private Map<Integer,PayTimeRewardData> LimitActDataMap = new HashMap<>(); 
	@Override
	public void init() {
		initTreeMap();
		initLimitActData();
	}
	
	private void initLimitActData(){
		List<PayTimeRewardData> dataList = TemplateManager.getTemplateList(PayTimeRewardData.class);
		for(PayTimeRewardData data : dataList ){
			LimitActDataMap.put(data.getDungeonID(), data);
		}
	}

	/**
	 * 初始化玩家副本
	 * 
	 * @param playerID
	 *            String 玩家ID
	 * */
	public DungeonMapEntity initDungeonMapEntity(String playerID) {
		int initID = SysConstants.dungeonStartID;
		Map<String, Dungeon> dungeonMap = new HashMap<String, Dungeon>();
		Dungeon dungeon = openNewDungeon(initID);
		dungeon.setStarLevel(1);
		dungeonMap.put(initID + "", dungeon);
		int initID2 = initID + 1;
		Dungeon dungeon2 = openNewDungeon(initID2);
		dungeonMap.put(initID2 + "", dungeon2);
		DungeonMapEntity entity = new DungeonMapEntity();
		entity.setDungeonMap(dungeonMap);
		entity.setKey(playerID);
		entity.setStarNum(1);
		return entity;
	}

	/**
	 * 初始化大关数据
	 * 
	 * @param playerID
	 *            String 玩家ID
	 * 
	 * */
	public DungeonActMapEntity initDungeonAct(String playerID) {
		int actID = dungeonActStartID;
		DungeonActMapEntity actEntity = new DungeonActMapEntity();
		Map<Integer, DungeonAct> actMap = new HashMap<Integer, DungeonAct>();
		DungeonAct dungeonAct = this.openNewDungeonAct(actID);
		dungeonAct.setActPoint(1);
		actMap.put(actID, dungeonAct);
		actEntity.setKey(playerID);
		actEntity.setDungeMap(actMap);

		return actEntity;
	}

	/**
	 * 初始化精英副本数据
	 * */
	public DungeonEliteEntity initDungeonEliteEntity(String playerID) {
		int eliteID = SysConstants.dungeonEliteStartID;
		DungeonEliteEntity entity = new DungeonEliteEntity();
		entity.setKey(playerID);
		entity.setMaxPayNum(eliteMaxPayNum);

		// 开服活动,精英副本次数翻倍
		EventModule eventModule = ModuleManager.getModule(ModuleNames.EventModule, EventModule.class);
		entity.setAttackNum(eliteMaxFreeNum * eventModule.getNewServerEventReward(EventConstans.EVENT_TYPE_PKELITE));

		List<Integer> eliteList = new ArrayList<>();
		eliteList.add(eliteID);
		entity.setEliteList(eliteList);
		return entity;
	}

	/**
	 * 初始化活动副本实体
	 * */
	public DungeonActiveEntity initActiveEntity(String playerID) {
		DungeonActiveEntity entity = new DungeonActiveEntity();
		entity.setLastUpdateTime(System.currentTimeMillis());
		Map<Integer, DungeonActive> activeList = this.initDungeonAcviteMap();
		entity.setDungeonList(activeList);
		entity.setKey(playerID);
		return entity;
	}

	/**
	 * 初始化活动副本集合
	 * */
	private Map<Integer, DungeonActive> initDungeonAcviteMap() {
		// 活动Module
		EventModule eventModule = ModuleManager.getModule(ModuleNames.EventModule, EventModule.class);

		Map<Integer, DungeonActive> activeList = new HashMap<Integer, DungeonActive>();
		List<DungeonActiveData> dataList = TemplateManager.getTemplateList(DungeonActiveData.class);
		for (DungeonActiveData data : dataList) {
			int pid = data.getPid();
			DungeonActive active = new DungeonActive();

			// 根据当前开启福利活动增加对应活动副本免费次数
			int freeNum = data.getFreeNum();
			if (data.getPid() == SysConstants.MONEY_TREE_ID) {
				freeNum = freeNum * eventModule.getWelfareReward(SysConstants.MONEY_TREE_ACTIVE_ID);
			}
			if (data.getPid() == SysConstants.BOOK_HORSE_ID) {
				freeNum = freeNum * eventModule.getWelfareReward(SysConstants.BOOK_HORSE_ACTIVE_ID);
			}
			active.setPayNum(0);
			active.setAttackNum(freeNum);
			active.setPid(pid);
			activeList.put(pid, active);
		}
		return activeList;
	}

	/**
	 * 初始化活动副本集合
	 * */
	private void initDungeonAcviteMap(DungeonActiveEntity entity) {
		EventModule eventModule = ModuleManager.getModule(ModuleNames.EventModule, EventModule.class);
		Map<Integer, DungeonActive> activeList = entity.getDungeonList();
		for(Entry<Integer,DungeonActive> entry : activeList.entrySet()){
			DungeonActive active = entry.getValue();
			int pid = active.getPid();
			DungeonActiveData data = TemplateManager.getTemplateData(pid,DungeonActiveData.class);
			active.setPayNum(0);
			int freeNum = data.getFreeNum();
			if (data.getPid() == SysConstants.MONEY_TREE_ID) {
				freeNum = freeNum * eventModule.getWelfareReward(SysConstants.MONEY_TREE_ACTIVE_ID);
			}
			if (data.getPid() == SysConstants.BOOK_HORSE_ID) {
				freeNum = freeNum * eventModule.getWelfareReward(SysConstants.BOOK_HORSE_ACTIVE_ID);
			}
			active.setAttackNum(freeNum);
		}
	}
	
	private void initTreeMap() {
		List<ActiveTreeRewardData> list = TemplateManager.getTemplateList(ActiveTreeRewardData.class);
		for (ActiveTreeRewardData data : list) {
			int range = data.getDamageRange();
			int total = data.getRewardTotal();
			treeReward.put(range, total);
		}
	}

	public void saveDungeonActiveEntity(DungeonActiveEntity entity) {
		dungeonActiveDAO.save(entity);
	}

	/**
	 * 获取普通关卡副本数据
	 * */
	public DungeonMapEntity getDungeonMapEntity(String playerID) {
		DungeonMapEntity dungeonMapEntity = dungeonMapEntityDAO.getEntity(playerID);
		if (dungeonMapEntity == null) {
			logger.error("普通副本实体为空");
			throw new NullPointerException(ErrorIds.NoEntity + "");
		}
		return dungeonMapEntity;
	}

	/**
	 * 获取副本数据(缓存)
	 * */
	public DungeonMapEntity getDungeonMapEntity(String playerID, IOMessage ioMessage) {
		DungeonMapEntity entity = null;
		if (ioMessage != null) {
			entity = (DungeonMapEntity) ioMessage.getInputParse().get(DungeonMapEntity.class.getName());
			if (entity == null) {
				entity = this.getDungeonMapEntity(playerID);
				ioMessage.getInputParse().put(DungeonMapEntity.class.getName(), entity);
			}
		} else {
			entity = this.getDungeonMapEntity(playerID);
		}
		return entity;
	}

	/**
	 * 获取前端的普通副本数据
	 * */
	public DungeonMapEntity getResponseDungeonMapEntity(String playerID) {
		DungeonMapEntity entity = this.getDungeonMapEntity(playerID);
		long nowTime = System.currentTimeMillis();
		long lastUpdateTime = entity.getLastUpdatTime();
		if (!DateTimeUtil.isSameDay(nowTime, lastUpdateTime)) {
			Map<String, Dungeon> map = entity.getDungeonMap();
			for (Entry<String, Dungeon> entry : map.entrySet()) {
				Dungeon dungeon = entry.getValue();
				dungeon.setAttackNum(0);
			}
			entity.setLastUpdatTime(nowTime);
			entity.setContinuousPayNum(0);
			this.saveDungeonMapEntity(entity);
		}
		return entity;
	}

	/**
	 * 保存关卡副本数据
	 * */
	public void saveDungeonMapEntity(DungeonMapEntity entity) {
		dungeonMapEntityDAO.save(entity);
	}

	/**
	 * 保存大关数据
	 * */
	public void saveDungeonActEntity(DungeonActMapEntity entity) {
		dungeonActMapEntityDAO.save(entity);
	}

	/**
	 * 保存精英关卡数据
	 * */
	public void saveEliteDungeonEntity(DungeonEliteEntity entity) {
		dungeonEliteEntityDAO.save(entity);
	}

	/** 获取精英关卡数据 */
	public DungeonEliteEntity getDungeonEliteEntity(String playerID) {
		DungeonEliteEntity entity = dungeonEliteEntityDAO.getEntity(playerID);
		if (entity == null) {
			throw new NullPointerException(ErrorIds.DungeonActNull + "");
		}
		return entity;
	}

	/** 获取前端的精英关卡数据 */
	public DungeonEliteEntity getResponseEilteEntity(String playerID) {
		DungeonEliteEntity entity = this.getDungeonEliteEntity(playerID);
		long lastUpdateTime = entity.getLastUpdateTime();
		long nowTime = System.currentTimeMillis();
		if (!DateTimeUtil.isSameDay(lastUpdateTime, nowTime)) {
			// 开服活动,精英副本次数翻倍
			EventModule eventModule = ModuleManager.getModule(ModuleNames.EventModule, EventModule.class);
			entity.setAttackNum(eliteMaxFreeNum * eventModule.getNewServerEventReward(EventConstans.EVENT_TYPE_PKELITE));
			entity.setPayNum(0);
			entity.setLastUpdateTime(nowTime);
			this.saveEliteDungeonEntity(entity);
		}
		return entity;
	}

	/**
	 * 获取大关数据
	 * */
	public DungeonActMapEntity getDungeonActEntity(String playerID) {
		DungeonActMapEntity entity = dungeonActMapEntityDAO.getEntity(playerID);
		if (entity == null) {
			throw new NullPointerException(ErrorIds.DungeonActNull + "");
		}
		return entity;
	}

	/**
	 * 获取大关数据
	 * */
	public DungeonActMapEntity getDungeonActEntity(String playerID, IOMessage ioMessage) {
		DungeonActMapEntity entity;
		if (ioMessage != null) {
			entity = (DungeonActMapEntity) ioMessage.getInputParse().get(DungeonActMapEntity.class.getName());
			if (entity == null) {
				entity = this.getDungeonActEntity(playerID);
				ioMessage.getInputParse().put(DungeonActMapEntity.class.getName(), entity);
			}
		} else {
			entity = this.getDungeonActEntity(playerID);
		}
		return entity;
	}

	/**
	 * 获取活动副本数据
	 * */
	public DungeonActiveEntity getDungeonActiveEntity(String playerID) {
		DungeonActiveEntity entity = dungeonActiveDAO.getEntity(playerID);
		if (entity == null) {
			logger.error("活动副本实体为空");
			throw new IllegalArgumentException(ErrorIds.NoEntity + "");
		}
		return entity;
	}

	/**
	 * 获取活动数据(缓存)
	 * */

	public DungeonActiveEntity getDungeonActiveEntity(String playerID, IOMessage ioMessage) {
		DungeonActiveEntity entity = null;
		if (ioMessage != null) {
			entity = (DungeonActiveEntity) ioMessage.getInputParse().get(DungeonActiveEntity.class.getName());
			if (entity == null) {
				entity = this.getDungeonActiveEntity(playerID);
			}
			ioMessage.getInputParse().put(DungeonActiveEntity.class.getName(), entity);
		} else {
			entity = this.getDungeonActiveEntity(playerID);
		}
		return entity;
	}

	/**
	 * 获取前端需要的活动副本实体
	 * */
	public DungeonActiveEntity getResponseActiveEntity(String playerID) {
		DungeonActiveEntity entity = this.getDungeonActiveEntity(playerID);
		long nowTime = System.currentTimeMillis();
		long lastUpdateTime = entity.getLastUpdateTime();
		if (!DateTimeUtil.isSameDay(nowTime, lastUpdateTime)) {
			this.initDungeonAcviteMap(entity);
			entity.setLastUpdateTime(nowTime);
			this.saveDungeonActiveEntity(entity);
		}
		return entity;
	}

	/**
	 * 获取单关信息
	 * */
	public Dungeon getDungeon(String playerID, int gameLevelID, IOMessage ioMessage) {
		DungeonMapEntity entity = this.getDungeonMapEntity(playerID, ioMessage);
		Map<String, Dungeon> map = entity.getDungeonMap();
		Dungeon dungeon = map.get(gameLevelID + "");
		if (dungeon == null) {
			throw new IllegalArgumentException(ErrorIds.DungeonNotFound + "");
		}
		return dungeon;
	}

	/**
	 * 获取单个活动副本的数据
	 * */
	private DungeonActive getDungeonActive(String playerID, int activeID, IOMessage ioMessage) {
		DungeonActiveEntity entity = this.getDungeonActiveEntity(playerID, ioMessage);
		Map<Integer, DungeonActive> map = entity.getDungeonList();
		DungeonActive active = map.get(activeID);
		if (active == null) {
			logger.error("活动副本实体为空");
			throw new NullPointerException(ErrorIds.NoEntity + "");
		}
		return active;
	}

	/**
	 * 获取关卡保存数据
	 * */

	/**
	 * 普通副本战斗
	 * 
	 * @param playerID
	 *            String 玩家ID
	 * @param gameLevelID
	 *            int 关卡ID
	 * @param starLevel
	 *            int 挑战级别
	 * @param win
	 *            boolean 是否胜利 缺少等级限制
	 * */

	public void DungeonStart(String playerID, int gameLevelID, int starLevel, int stage, boolean win, DungeonProtocol protocol, IOMessage ioMessage) {
		DungeonData dungeonData = TemplateManager.getTemplateData(gameLevelID, DungeonData.class);
		LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule, LoginModule.class);
		int playerLevel = loginModule.getPlayerLevel(playerID);

		if (dungeonData == null) {
			protocol.setCode(ErrorIds.DungeonNotFound);
			return;
		}
		if (playerLevel < dungeonData.getOpenLevel()) {
			throw new IllegalArgumentException(ErrorIds.DungeonLocked + "");
		}
		DungeonMapEntity entity = dungeonMapEntityDAO.getEntity(playerID);
		Map<String, Dungeon> map = entity.getDungeonMap();
		Dungeon dungeon = map.get(gameLevelID + "");
		if (dungeon == null) {
			protocol.setCode(ErrorIds.DungeonNotFound);
			return;
		}
		int maxStage = dungeonData.getMaxStage();
		int vitatlty = dungeonData.getVitality();
		Map<String, Object> itemMap = new HashMap<>();

		int canAttack = checkCanAttack(dungeon, starLevel, dungeonData);
		if (canAttack != 0) {
			protocol.setCode(canAttack);
			return;
		}
		NewPlayerEntity newPlayerEntity = loginModule.getNewPlayerEntity(playerID,ioMessage);
		protocol.setMaxGameLevelID(newPlayerEntity.getGameLevelID());
		if (win && stage == maxStage) {
			RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
			int code = rewardModule.useGoods(playerID, KindIDs.VITALITY, vitatlty, 0, true, null, itemMap, ioMessage);
			if (code != 0) {
				protocol.setCode(code);
				return;
			}
			Map<Integer, GoodsBean> dropList = new HashMap<>();
			Map<Integer, GoodsBean> specialDropList = new HashMap<>();

			dungeon.setAttackNum(dungeon.getAttackNum() + 1);
			Map<Integer, Integer> dropMap = dungeonData.getDropList();
			Map<Integer, Integer> specilDrop = dungeonData.getSpecialDrop();
			Map<Integer, GoodsBean> showMap = new HashMap<>();

			int nextID = dungeonData.getNextOpenID();
			if (map.get(nextID + "") == null && nextID != 0) {
				Dungeon newDungeon = openNewDungeon(nextID);
				map.put(nextID + "", newDungeon);
				protocol.setNextDungeon(newDungeon);
				this.updateActReward(playerID, nextID, dungeonData.getActID(),protocol, ioMessage);
			}
	
			MainTaskModule mainTaskModule = ModuleManager.getModule(ModuleNames.MainTaskModule,MainTaskModule.class);
			mainTaskModule.updateTaskByActionType(playerID,ActionType.PASSDUNGEON,gameLevelID,ioMessage); 
			
			if (starLevel > dungeon.getStarLevel()) {
				this.addDestinyPoint(playerID);
				entity.addStarNum();
				dungeon.setStarLevel(starLevel);
				DungeonActMapEntity dungeonActMapEntity = this.getDungeonActEntity(playerID, ioMessage);
				Map<Integer, DungeonAct> actMap = dungeonActMapEntity.getDungeMap();
				int actID = dungeonData.getActID();
				DungeonAct dungeonAct = actMap.get(actID);
				dungeonAct.setActPoint(dungeonAct.getActPoint() + 1);
				actMap.put(actID, dungeonAct);
				this.saveDungeonActEntity(dungeonActMapEntity);
				protocol.setNowDungeonAct(dungeonAct);
				AchievementModule acModule = ModuleManager.getModule(ModuleNames.AchievementModule, AchievementModule.class);
				acModule.refreshAchievement(playerID, ActionType.FIRSTDOWNDUNGEONNORMAL, gameLevelID);
				acModule.refreshAchievement(playerID, ActionType.DUGEONALLSTAR, entity.getStarNum());
				mainTaskModule.updateTaskByActionType(playerID,ActionType.DUNGEONREACHSTARLEVEL,0,ioMessage); 
			}
			newPlayerEntity = loginModule.saveNewPlayerGameLevel(playerID, gameLevelID,ioMessage);
			protocol.setMaxGameLevelID(newPlayerEntity.getGameLevelID());

			if (dropMap != null && !dropMap.isEmpty()) {
				this.doDrop(dropMap, dropList, showMap);
			}
			if (specilDrop != null && !specilDrop.isEmpty()) {
				this.doDrop(specilDrop, dropList, specialDropList);
			}

			HeroModule heroModule = ModuleManager.getModule(ModuleNames.HeroModule, HeroModule.class);
			Hero lead = heroModule.getLead(playerID, ioMessage);
			int exp = lead.getLevel() * 10;
			dropList.put(KindIDs.LEADEXP, new GoodsBean(KindIDs.LEADEXP, exp));
			int coinReward =  dungeonData.getCoinReward();
			int soulReward = dungeonData.getSoulReward();
			if(starLevel > 1){
				coinReward = (int) (coinReward * 1.5);
				soulReward = (int) (soulReward * 1.5);
			}
			if (dropList.containsKey(KindIDs.SILVERTYPE)) {
				GoodsBean silverbBean = dropList.get(KindIDs.SILVERTYPE);
				silverbBean.setNum(silverbBean.getNum() + coinReward);
				dropList.put(KindIDs.SILVERTYPE, silverbBean);
			}else {
				dropList.put(KindIDs.SILVERTYPE, new GoodsBean(KindIDs.SILVERTYPE,  coinReward));
			}
			if (dropList.containsKey(KindIDs.HEROSOUL)) {
				GoodsBean heroSoulBean = dropList.get(KindIDs.HEROSOUL);
				heroSoulBean.setNum(heroSoulBean.getNum() + soulReward);
				dropList.put(KindIDs.HEROSOUL, heroSoulBean);
			}else {
				dropList.put(KindIDs.HEROSOUL, new GoodsBean(KindIDs.HEROSOUL,soulReward));
			}
			rewardModule.addGoods(playerID, Utilities.getGoodList(dropList), true, null, itemMap, ioMessage);
			protocol.setShowMap(showMap);
			protocol.setSpecialDropList(specialDropList);
			protocol.setItemMap(itemMap);
			protocol.setNowDungeon(dungeon);
			protocol.setStarNum(entity.getStarNum());
			this.saveDungeonMapEntity(entity);
			boolean mystical = false;
			if (lead.getLevel() >= 16) {
				mystical = this.mysticalMerchant();
				if (mystical) {
					EventModule eventModule = ModuleManager.getModule(ModuleNames.EventModule, EventModule.class);
					eventModule.eventTrader(playerID, EventConstans.MYSTERY_TRADER_EXISTTIME);
				}
			}
			protocol.setMystery(mystical);
			DayTaskModule dayTaskModule = ModuleManager.getModule(ModuleNames.DayTaskModule, DayTaskModule.class);
			dayTaskModule.addScore(playerID, ActionType.DUNGEONNORMAL, 1);
			EventModule eventModule = ModuleManager.getModule(ModuleNames.EventModule, EventModule.class);
			eventModule.intefaceDrawIntegral(playerID, 10895);
			mainTaskModule.updateTaskByActionType(playerID,ActionType.DUNGEONCOMPLETENUM,0,ioMessage); 
		}
	}

	public void doDrop(Map<Integer, Integer> dropMap, Map<Integer, GoodsBean> dropList, Map<Integer, GoodsBean> showMap) {
		Map<Integer, GoodsBean> tempMap = new HashMap<>();
		for (Entry<Integer, Integer> entry : dropMap.entrySet()) {
			int key = entry.getKey();
			int value = entry.getValue();
			for (int i = 0; i < value; i++) {
				GoodsBean dropBean = DropModule.doDrop(key);
				int pid = dropBean.getPid();
				if (pid != SysConstants.emptyPid) {
					GoodsBean tempBean = tempMap.get(pid);
					if (tempBean != null) {
						tempBean.setNum(tempBean.getNum() + dropBean.getNum());
					} else {
						tempMap.put(pid, dropBean);
					}
				}
			}
		}
		for (GoodsBean bean : tempMap.values()) {
			int pid = bean.getPid();
			GoodsBean tempBean = showMap.get(pid);
			if (tempBean != null) {
				tempBean.setNum(tempBean.getNum() + bean.getNum());
			} else {
				showMap.put(pid, bean);
			}
		}
		for (GoodsBean bean : tempMap.values()) {
			int pid = bean.getPid();
			GoodsBean tempBean = dropList.get(pid);
			if (tempBean != null) {
				tempBean.setNum(tempBean.getNum() + bean.getNum());
			} else {
				dropList.put(pid, bean);
			}
		}
	}

	/**
	 * 检查是否可以攻打
	 * 
	 * @param gameLevelID
	 *            int 关卡ID
	 * @param starLevel
	 *            int 挑战级别
	 * @param dungeonData
	 *            DungeonData 当前关卡的模板
	 * @return 0 正常
	 * */
	private int checkCanAttack(Dungeon dungeon, int starLevel, DungeonData dungeonData) {
		if (dungeon == null) {
			return ErrorIds.DungeonLocked;
		}
		if (starLevel <= 0) {
			return ErrorIds.StarLevelError;
		}
		if (dungeon.getStarLevel() + 1 < starLevel) {
			return ErrorIds.DungeonLocked;
		}
		if (starLevel - dungeon.getStarLevel() > 1) {
			return ErrorIds.FirstDungeonNotFinish;
		}
		if (starLevel > dungeonData.getMaxStarLevel()) {
			return ErrorIds.OverDungeonDifficulty;
		}
		if (dungeon.getAttackNum() + 1 > dungeonData.getMaxAttackNum()) {
			return ErrorIds.OverMaxDungeonAttackNum;
		}
		return 0;
	}

	private void updateActReward(String playerID, int dungeonID, int actID,DungeonProtocol protocol, IOMessage ioMessage) {
		DungeonActMapEntity entity = this.getDungeonActEntity(playerID, ioMessage);
		Map<Integer, DungeonAct> actMap = entity.getDungeMap();
		DungeonData dungeonData = TemplateManager.getTemplateData(dungeonID, DungeonData.class);
		int nextActID = dungeonData.getActID();
		DungeonAct nextDungeonAct = actMap.get(nextActID);
		if (nextDungeonAct == null) {
			nextDungeonAct = this.openNewDungeonAct(nextActID);
			actMap.put(nextActID, nextDungeonAct);
			protocol.setNextDungeonAct(nextDungeonAct);
			this.addActLimitReward(playerID, actID,protocol);
		}
	}

	/**
	 * 开启新关卡
	 * */
	private Dungeon openNewDungeon(int dungeonID) {
		Dungeon newDungeon = new Dungeon();
		newDungeon.setTemplateID(dungeonID);
		return newDungeon;
	}

	/**
	 * 开启新大关
	 * */

	private DungeonAct openNewDungeonAct(int actID) {
		ActData actData = TemplateManager.getTemplateData(actID, ActData.class);
		DungeonAct dungeonAct = new DungeonAct();
		Map<Integer, ActReward> actMap = dungeonAct.getRewardlist();
		dungeonAct.setTemplateID(actID);
		Map<Integer, List<GoodsBean>> rewardMap = actData.getReward();
		for (Entry<Integer, List<GoodsBean>> entry : rewardMap.entrySet()) {
			int key = entry.getKey();
			ActReward actReward = new ActReward();
			actReward.setPoint(key);
			actMap.put(key, actReward);
		}
		dungeonAct.setRewardlist(actMap);
		return dungeonAct;
	}

	/**
	 * 领取大关奖励
	 * 
	 * @param playerID
	 *            String 玩家ID
	 * @param actID
	 *            int 大关ID
	 * @param level
	 *            int 领取的点数
	 * */
	public void getActReward(String playerID, int actID, int point, DungeonProtocol protocol,IOMessage ioMessage) {
		DungeonActMapEntity entity = this.getDungeonActEntity(playerID,ioMessage);
		Map<Integer, DungeonAct> actMap = entity.getDungeMap();
		DungeonAct act = actMap.get(actID);
		if (act == null) {
			throw new NullPointerException(ErrorIds.DungeonActNull + "");
		}
		int actPoint = act.getActPoint();
		if (actPoint < point) {
			throw new IllegalArgumentException(ErrorIds.ActPointNotEnough + "");
		}
		ActReward actReward = act.getRewardlist().get(point);
		if (actReward == null) {
			throw new IllegalArgumentException(ErrorIds.ActRewardNotFound + "");
		}
		if (actReward.getState() == 1) {
			throw new IllegalArgumentException(ErrorIds.ActRewardGeted + "");
		}
		ActData actData = TemplateManager.getTemplateData(actID, ActData.class);
		List<GoodsBean> goodsList = actData.getReward().get(point);
		Map<String, Object> itemMap = new HashMap<>();
		RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
		rewardModule.addGoods(playerID, goodsList, true, null, itemMap, null);
		actReward.setState(1);
		protocol.setItemMap(itemMap);
		protocol.setNowDungeonAct(act);
		this.saveDungeonActEntity(entity);
		MainTaskModule mainTaskModule = ModuleManager.getModule(ModuleNames.MainTaskModule,MainTaskModule.class);
		mainTaskModule.updateTaskByActionType(playerID, ActionType.GETACTBOX, 0, ioMessage);
	}

	/**
	 * 恢复攻打次数
	 * */
	public void recoverAttackNum(String playerID, int gameLevelID, int type, IOMessage ioMessage, DungeonProtocol protocol) {
		Dungeon dungeon = this.getDungeon(playerID, gameLevelID, ioMessage);
		int attackNum = dungeon.getAttackNum();
		int templateID = dungeon.getTemplateID();
		DungeonData data = TemplateManager.getTemplateData(templateID, DungeonData.class);
		int maxAttackNum = data.getMaxAttackNum();
		if (attackNum < maxAttackNum) {
			throw new IllegalArgumentException(ErrorIds.DungeonAttackNumRemain + "");
		}
		int pid = 0;
		int num = 0;
		switch (type) {
		case itemRecoverType:
			pid = addBattleNumItem;
			num = 1;
			break;
		case goldRecoverType:
			pid = KindIDs.GOLDTYPE;
			num = goldRecoverNum;
			break;
		default:
			throw new IllegalArgumentException(ErrorIds.UnKnowType + "");
		}
		Map<String, Object> itemMap = new HashMap<>();
		dungeon.setAttackNum(0);
		RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
		int code = rewardModule.useGoods(playerID, pid, num, 0, true, null, itemMap, ioMessage);
		if (code != 0) {
			protocol.setCode(code);
			return;
		}
		this.saveDungeonMapEntity((DungeonMapEntity) ioMessage.getInputParse().get(DungeonMapEntity.class.getName()));
		protocol.setNowDungeon(dungeon);
		protocol.setItemMap(itemMap);

		// ////
		// // 元宝消耗记录
		// ///
		if (pid == KindIDs.GOLDTYPE) {
			AnalyseModule analyseModule = ModuleManager.getModule(ModuleNames.AnalyseModule, AnalyseModule.class);
			analyseModule.goldCostLog(playerID, num, 1, num, "recoverAttackNum", "dungeon");
		}
	}

	/**
	 * 攻打精英副本
	 * */
	public void attackEliteDungeon(String playerID, int eliteID, boolean win, int stage, DungeonProtocol protocol,IOMessage ioMessage) {
		DungeonEliteEntity entity = this.getDungeonEliteEntity(playerID);
		int attackNum = entity.getAttackNum();
		if (attackNum < 1) {
			throw new IllegalArgumentException(ErrorIds.EliteNumNotEnough + "");
		}
		List<Integer> dungeonList = entity.getEliteList();
		if (!dungeonList.contains(eliteID)) {
			throw new IllegalArgumentException(ErrorIds.EliteNotFound + "");
		}
		EliteDungeonData data = TemplateManager.getTemplateData(eliteID, EliteDungeonData.class);
		protocol.setMaxEliteID(entity.getMaxEliteID());
		if (data.getMaxStage() == stage && win) {
			Map<Integer, Integer> dropMap = data.getDropList();
			int coin = data.getCoinReward();
			int soul = data.getSoulReward();
			Map<Integer, GoodsBean> dropList = new HashMap<>();
			Map<Integer, GoodsBean> showMap = new HashMap<>();
			dropList.put(KindIDs.SILVERTYPE, new GoodsBean(KindIDs.SILVERTYPE,coin));
			dropList.put(KindIDs.HEROSOUL, new GoodsBean(KindIDs.HEROSOUL,soul));
			this.doDrop(dropMap, dropList, showMap);
			RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
			Map<String, Object> itemMap = new HashMap<>();
			rewardModule.addGoods(playerID, Utilities.getGoodList(dropList), true, null, itemMap, ioMessage);
			int nextEliteID = data.getNextOpenID();
			if(nextEliteID != 0){
				int openEliteID = this.openNewDungeonElite(playerID, eliteID,nextEliteID, entity);
				protocol.setOpenEliteID(openEliteID);
			}else{
				entity.setMaxEliteID(eliteID);
			}
			entity.setAttackNum(attackNum - 1);
			protocol.setItemMap(itemMap);
			protocol.setAttackNum(entity.getAttackNum());
			protocol.setShowMap(showMap);
			protocol.setMaxEliteID(entity.getMaxEliteID());
			this.saveEliteDungeonEntity(entity);
		
			DayTaskModule dayTaskModule = ModuleManager.getModule(ModuleNames.DayTaskModule, DayTaskModule.class);
			dayTaskModule.addScore(playerID, ActionType.DUNGEONELITE, 1);
			EventModule eventModule = ModuleManager.getModule(ModuleNames.EventModule, EventModule.class);
			eventModule.intefaceDrawIntegral(playerID, 10896);
			MainTaskModule mainTaskModule = ModuleManager.getModule(ModuleNames.MainTaskModule,MainTaskModule.class);
			mainTaskModule.updateTaskByActionType(playerID, ActionType.DUNGEONELITECOMPLETENUM, 0, null);
		} else {
			protocol.setAttackNum(attackNum);
		}
	}

	/** 开启新精英副本 */
	private int openNewDungeonElite(String playerID,  int eliteID, int openEliteID,DungeonEliteEntity entity) {
		// EliteDungeonData data =
		// TemplateManager.getTemplateData(eliteID,EliteDungeonData.class);
		// int needActID = data.getOpenAct();
		List<Integer> eliteList = entity.getEliteList();
		
		if (!eliteList.contains(openEliteID)) {
			entity.setMaxEliteID(eliteID);
			AchievementModule acModule = ModuleManager.getModule(ModuleNames.AchievementModule, AchievementModule.class);
			acModule.refreshAchievement(playerID, ActionType.FIRSTDOWNDUNGEONELITE, openEliteID);
			// DungeonActMapEntity actEntity =
			// this.getDungeonActEntity(playerID);
			// Map<Integer, DungeonAct> actMap = actEntity.getDungeMap();
			// if(actMap.containsKey(needActID)){
			// DungeonAct act = actMap.get(needActID);
			// if(act.isPass()){
			// eliteList.add(eliteID);
			// return eliteID;
			// }
			// }
			eliteList.add(openEliteID);
			return openEliteID;
		}

		return 0;
	}

	/**
	 * 购买精英副本次数
	 * */
	public void buyElitePayNum(String playerID, DungeonProtocol protocol) {
		DungeonEliteEntity entity = this.getDungeonEliteEntity(playerID);
		int attackNum = entity.getAttackNum();
		int payNum = entity.getPayNum();
		int maxPayNum = entity.getMaxPayNum();
		if (attackNum != 0) {
			throw new IllegalArgumentException(ErrorIds.DungeonAttackNumRemain + "");
		}
		if (payNum >= maxPayNum) {
			throw new IllegalArgumentException(ErrorIds.ElitePayNumEnough + "");
		}
		entity.setAttackNum(1);
		entity.setPayNum(payNum + 1);
		int gold = entity.getPayNum() * eliteRecoveGold;
		Map<String, Object> itemMap = new HashMap<>();
		RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
		int code=rewardModule.useGoods(playerID, KindIDs.GOLDTYPE, gold, 0, true, null, itemMap, null);
		if(code!=0){
			protocol.setCode(code);
			return;
		}
		this.saveEliteDungeonEntity(entity);
		protocol.setItemMap(itemMap);
		protocol.setPayNum(entity.getPayNum());
		protocol.setAttackNum(1);

		// ////
		// // 元宝消耗记录
		// ///
		AnalyseModule analyseModule = ModuleManager.getModule(ModuleNames.AnalyseModule, AnalyseModule.class);
		analyseModule.goldCostLog(playerID, gold, entity.getPayNum(), eliteRecoveGold, "buyElitePayNum", "dungeon");

	}

	/**
	 * 攻打活动副本
	 * */
	public void attackActiveDungeon(String playerID, int activeID, int damage, int stage, IOMessage ioMessage, DungeonProtocol protocol) {
		DungeonActive active = this.getDungeonActive(playerID, activeID, ioMessage);
		int attackNum = active.getAttackNum();
		Map<String, Object> itemMap = new HashMap<>();
		if (attackNum < 1) {
			if(activeID == silverAcitve){
				BagModule bagModule = ModuleManager.getModule(ModuleNames.BagModule,BagModule.class);
				BagEntity bagEntity = bagModule.getBagEntity(playerID);
				Map<Integer,BagItem> bagList = bagEntity.getBagList();
				BagItem bagItem = bagList.get(SysConstants.TreeItem);
				if(bagItem != null){
					int itemNum = bagItem.getNum();
					if(itemNum > 0 ){
						bagItem.setNum(itemNum - 1);
						List<BagItem> list = new ArrayList<BagItem>();
						BagItem tempItem = new BagItem();
						tempItem.setTemplateID(bagItem.getTemplateID());
						tempItem.setNum(bagItem.getNum());
						list.add(tempItem);
						itemMap.put("bagItem", list);
						if(bagItem.getNum() == 0){
							bagList.remove(SysConstants.TreeItem);
						}
						bagModule.saveBagEntity(bagEntity);
						attackNum = 1;
					}else{
						throw new IllegalArgumentException(ErrorIds.ActiveDungeonNumNotEnough + "");
					}
				}else{
					throw new IllegalArgumentException(ErrorIds.ActiveDungeonNumNotEnough + "");
				}
			}else{
				throw new IllegalArgumentException(ErrorIds.ActiveDungeonNumNotEnough + "");
			}
		}
		DungeonActiveData data = TemplateManager.getTemplateData(activeID, DungeonActiveData.class);
		if (data == null) {
			throw new IllegalArgumentException(ErrorIds.ActiveDungeonNotFound + "");
		}
		this.checkTime(activeID);
		MainTaskModule mainTaskModule = ModuleManager.getModule(ModuleNames.MainTaskModule,MainTaskModule.class);
		if (stage == data.getMaxStage()) {
			Map<Integer, GoodsBean> goodsList = new HashMap<>();
			Map<Integer, GoodsBean> showMap = new HashMap<>();
			switch (activeID) {
			case expActive:
				this.expActive(activeID, goodsList, showMap);
				break;
			case talismanActive:
				this.talismanActive(activeID, goodsList, showMap);
				mainTaskModule.updateTaskByActionType(playerID, ActionType.ATTACKTALISMAN, 0, null);
				break;
			case silverAcitve:
				this.silverActive(damage, goodsList, showMap);
				AchievementModule acModule = ModuleManager.getModule(ModuleNames.AchievementModule, AchievementModule.class);
				acModule.refreshAchievement(playerID, ActionType.TREEDAMAGE, damage);
				mainTaskModule.updateTaskByActionType(playerID, ActionType.ATTACKTREE, 0, null);
				break;
			default:
				throw new IllegalArgumentException(ErrorIds.ActiveDungeonNotFound + "");
			}
			RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
		
			rewardModule.addGoods(playerID, Utilities.getGoodList(goodsList), true, null, itemMap, ioMessage);
			protocol.setItemMap(itemMap);
			active.setAttackNum(attackNum - 1);
			protocol.setActive(active);
			protocol.setShowMap(showMap);
			DungeonActiveEntity entity = (DungeonActiveEntity) ioMessage.getInputParse().get(DungeonActiveEntity.class.getName());
			this.saveDungeonActiveEntity(entity);
			DayTaskModule dayTaskModule = ModuleManager.getModule(ModuleNames.DayTaskModule, DayTaskModule.class);
			dayTaskModule.addScore(playerID, ActionType.DUNGEONACTIVE, 1);
			EventModule eventModule = ModuleManager.getModule(ModuleNames.EventModule, EventModule.class);
			eventModule.intefaceDrawIntegral(playerID, 10897);
		}

	}

	private void expActive(int pid, Map<Integer, GoodsBean> goodsList, Map<Integer, GoodsBean> showMap) {
		DungeonActiveData data = TemplateManager.getTemplateData(pid, DungeonActiveData.class);
		Map<Integer, Integer> dropList = data.getDropList();
		this.doDrop(dropList, goodsList, showMap);
	}

	private void talismanActive(int pid, Map<Integer, GoodsBean> goodsList, Map<Integer, GoodsBean> showMap) {
		DungeonActiveData data = TemplateManager.getTemplateData(pid, DungeonActiveData.class);
		Map<Integer, Integer> dropList = data.getDropList();
		this.doDrop(dropList, goodsList, showMap);
	}

	private void silverActive(int damage, Map<Integer, GoodsBean> goodsList, Map<Integer, GoodsBean> showMap) {
		int silver = this.getTreeSilver(damage);
		GoodsBean silverbBean = new GoodsBean(KindIDs.SILVERTYPE, silver);

		GoodsBean bean = goodsList.get(KindIDs.SILVERTYPE);
		if (bean != null) {
			bean.setNum(bean.getNum() + silverbBean.getNum());
		} else {
			goodsList.put(KindIDs.SILVERTYPE, silverbBean);
		}
		bean = showMap.get(KindIDs.SILVERTYPE);
		if (bean != null) {
			bean.setNum(bean.getNum() + silverbBean.getNum());
		} else {
			showMap.put(KindIDs.SILVERTYPE, silverbBean);
		}
	}

	private int getTreeSilver(int damage) {
		int silver = 0;
		if (damage >= 1000000) {
			silver = treeReward.get(1000000) + 100000;
		} else {
			int temp = 0;
			for (Entry<Integer, Integer> entry : treeReward.entrySet()) {
				int key = entry.getKey();
				if (damage >= key && key > temp) {
					temp = key;
				}
			}
			if (treeReward.get(temp) != null) {
				silver = treeReward.get(temp) + damage / 10;
			} else {
				silver = damage / 10;
			}
		}
		return silver;
	}

	private void checkTime(int pid) {
		DungeonActiveData data = TemplateManager.getTemplateData(pid, DungeonActiveData.class);
		long nowTime = System.currentTimeMillis();
		Date nowDate = new Date(nowTime);
		String afterTime = data.getOpenTime();
		String beforeTime = data.getEndTime();
		Date afterDate = DateTimeUtil.getDate(afterTime);
		Date beforeDate = DateTimeUtil.getDate(beforeTime);
		if (nowDate.before(afterDate) || nowDate.after(beforeDate)) {
			throw new IllegalArgumentException(ErrorIds.ActiveDungeonTimeWrong + "");
		}
		List<Integer> openDate = data.getOpenDate();
		int weekDay = DateTimeUtil.getWeekDay(nowTime);
		if (!openDate.contains(weekDay)) {
			throw new IllegalArgumentException(ErrorIds.ActiveDungeonTimeWrong + "");
		}
	}

	/**
	 * 恢复活动副本次数
	 * */
	public void recoverActiveTimes(String playerID, int activeID, int type, IOMessage ioMessage, DungeonProtocol protocol) {
		DungeonActive active = this.getDungeonActive(playerID, activeID, ioMessage);
		if (active.getAttackNum() > 0) {
			throw new IllegalArgumentException(ErrorIds.ActiveDungeonNotReset + "");
		}
		DungeonActiveData data = TemplateManager.getTemplateData(activeID, DungeonActiveData.class);

		int payNum = active.getPayNum();
		int maxNum = active.getMaxPayNum();
		if (payNum >= maxNum) {
			throw new IllegalArgumentException(ErrorIds.ActiveDungonMax + "");
		}
		active.setPayNum(payNum + 1);
		active.setAttackNum(1);
		int pid = 0;
		int num = 0;
		if (type == 0) {
			pid = KindIDs.GOLDTYPE;
			num = data.getPayGold();
		} else {
			Map<Integer, Integer> payItem = data.getPayItem();
			for (Entry<Integer, Integer> entry : payItem.entrySet()) {
				pid = entry.getKey();
				num = entry.getValue();
			}
		}
		Map<String, Object> itemMap = new HashMap<>();
		RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
		rewardModule.useGoods(playerID, pid, num, 0, true, null, itemMap, ioMessage);
		protocol.setItemMap(itemMap);
		protocol.setActive(active);

		DungeonActiveEntity entity = (DungeonActiveEntity) ioMessage.getInputParse().get(DungeonActiveEntity.class.getName());
		this.saveDungeonActiveEntity(entity);

		// ////
		// // 元宝消耗记录
		// ///
		if (pid == KindIDs.GOLDTYPE) {
			AnalyseModule analyseModule = ModuleManager.getModule(ModuleNames.AnalyseModule, AnalyseModule.class);
			analyseModule.goldCostLog(playerID, num, 1, num, "recoverActiveTimes", "dungeon");
		}
	}

	// /**
	// * 增加英雄经验
	// * */
	// public boolean addLeadExp(String playerID, List<GoodsBean> goodslList,
	// int num, IOMessage ioMessage, DungeonProtocol protocol) {
	// HeroModule heroModule = ModuleManager.getModule(ModuleNames.HeroModule,
	// HeroModule.class);
	// Hero lead = heroModule.getLead(playerID, ioMessage);
	// int exp = lead.getLevel() * 10 * num;
	// LeadModule leadModule = ModuleManager.getModule(ModuleNames.LeadModule,
	// LeadModule.class);
	// ExpResponse expResponse = leadModule.addExp(playerID, lead, true, exp,
	// ioMessage);
	// if (expResponse.isLevelUp()) {
	// GoodsBean goldBean = new GoodsBean(KindIDs.GOLDTYPE, 10);
	// GoodsBean energyBean = new GoodsBean(KindIDs.ENERGY, 20);
	// goodslList.add(goldBean);
	// goodslList.add(energyBean);
	// }
	// protocol.setLead(lead);
	// return expResponse.isLevelUp();
	// }

	/**
	 * 增加天命点数
	 * */
	public void addDestinyPoint(String playerID) {
		LeadModule leadModule = ModuleManager.getModule(ModuleNames.LeadModule, LeadModule.class);
		leadModule.heroAddDestinyPoint(playerID);
	}

	/**
	 * 连续攻打
	 * */
	public void ContinuousFight(String playerID, int gameLevelID, int starLevel, IOMessage ioMessage, DungeonProtocol protocol) {
		LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule, LoginModule.class);
		PlayerEntity playerEntity = loginModule.getPlayerEntity(playerID);
		if (playerEntity.getLevel() < 15) {
			throw new IllegalArgumentException(ErrorIds.ContinuousFightLevel + "");
		}
		Map<Integer, GoodsBean> dropList = new HashMap<>();
		Map<Integer, GoodsBean> showMap = new HashMap<>();
		Map<Integer, GoodsBean> specialDropList = new HashMap<>();
		Dungeon dungeon = this.getDungeon(playerID, gameLevelID, ioMessage);
		if (starLevel > dungeon.getStarLevel() || starLevel == 0) {
			throw new IllegalArgumentException(ErrorIds.OverDungeonDifficulty + "");
		}
		DungeonData dungeonData = TemplateManager.getTemplateData(gameLevelID, DungeonData.class);
		int attackNum = dungeon.getAttackNum();
		int maxAttackNum = dungeonData.getMaxAttackNum();
		if (attackNum >= maxAttackNum) {
			throw new IllegalArgumentException(ErrorIds.OverMaxDungeonAttackNum + "");
		}
		attackNum = maxAttackNum - attackNum;
		if (attackNum > 10) {
			attackNum = 10;
		}
		DungeonMapEntity dungeonMapEntity = (DungeonMapEntity) ioMessage.getInputParse().get(DungeonMapEntity.class.getName());
		long lastTime = dungeonMapEntity.getLastContinuousTime();
		long nowTime = System.currentTimeMillis();
		if (nowTime < lastTime) {
			throw new IllegalArgumentException(ErrorIds.ContinueBattleColldown + "");
		}

		int vitality = dungeonData.getVitality() * attackNum;
		RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
		Map<String, Object> itemMap = new HashMap<>();
		int code = rewardModule.useGoods(playerID, KindIDs.VITALITY, vitality, 0, true, null, itemMap, ioMessage);
		if (code != 0) {
			protocol.setCode(code);
			return;
		}

		Map<Integer, Integer> dropMap = dungeonData.getDropList();
		Map<Integer, Integer> specilDrop = dungeonData.getSpecialDrop();
		int coinReward = dungeonData.getCoinReward();
		int soulReward = dungeonData.getSoulReward();
		if(starLevel > 1){
			coinReward = (int) (coinReward * 1.5);
			soulReward = (int) (soulReward * 1.5);
		}
		if (dropList.containsKey(KindIDs.SILVERTYPE)) {
			GoodsBean silverbBean = dropList.get(KindIDs.SILVERTYPE);
			silverbBean.setNum(silverbBean.getNum() + coinReward);
			dropList.put(KindIDs.SILVERTYPE, silverbBean);
		} else {
				dropList.put(KindIDs.SILVERTYPE, new GoodsBean(KindIDs.SILVERTYPE,  coinReward));
		}
		if (dropList.containsKey(KindIDs.HEROSOUL)) {
			GoodsBean heroSoulBean = dropList.get(KindIDs.HEROSOUL);
			heroSoulBean.setNum(heroSoulBean.getNum() + soulReward);
			dropList.put(KindIDs.HEROSOUL, heroSoulBean);
		} else {
			dropList.put(KindIDs.HEROSOUL, new GoodsBean(KindIDs.HEROSOUL,soulReward));
		}
		
		EventModule eventModule = ModuleManager.getModule(ModuleNames.EventModule, EventModule.class);
		boolean isMystical = false;
		for (int i = 0; i < attackNum; i++) {
			this.doDrop(dropMap, dropList, showMap);
			this.doDrop(specilDrop, dropList, specialDropList);
			boolean mystical = this.mysticalMerchant();
			if (mystical) {
				isMystical = true;
				eventModule.eventTrader(playerID, EventConstans.MYSTERY_TRADER_EXISTTIME);
			}
		}
		int exp = playerEntity.getLevel() * 10 * attackNum;
		dropList.put(KindIDs.LEADEXP, new GoodsBean(KindIDs.LEADEXP, exp));
		List<GoodsBean> goodsList = Utilities.getGoodList(dropList);
		code = rewardModule.addGoods(playerID, goodsList, true, null, itemMap, ioMessage);
		if (code != 0) {
			protocol.setCode(code);
			return;
		}
		dungeonMapEntity.setLastContinuousTime(nowTime + 60 * DateTimeUtil.ONE_MINUTE_TIME_MS);
		dungeon.setAttackNum(dungeon.getAttackNum() + attackNum);
		DayTaskModule dayTaskModule = ModuleManager.getModule(ModuleNames.DayTaskModule, DayTaskModule.class);
		dayTaskModule.addScore(playerID, ActionType.DUNGEONNORMAL, attackNum);
		this.saveDungeonMapEntity(dungeonMapEntity);
		protocol.setMystery(isMystical);
		protocol.setShowMap(showMap);
		protocol.setItemMap(itemMap);
		protocol.setSpecialDropList(specialDropList);
		protocol.setNowDungeon(dungeon);
		protocol.setLastContinuousTime(dungeonMapEntity.getLastContinuousTime());
	}

	private boolean mysticalMerchant() {
		int random = Utilities.getRandomInt(100);
		if (random < 2) {
			return true;
		}
		return false;
	}

	/**
	 * 获取副本排行榜
	 * */
	public List<DungeonTopInfo> getTop50List(DungeonProtocol protocol) {
		List<DungeonMapEntity> list = dungeonMapEntityDAO.getTop50List();
		LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule, LoginModule.class);
		List<DungeonTopInfo> topList = new ArrayList<>();
		for (DungeonMapEntity entity : list) {
			String playerID = entity.getKey().toString();
			DungeonTopInfo topInfo = new DungeonTopInfo();
			topInfo.setStarNum(entity.getStarNum());
			PlayerEntity playerEntity = loginModule.getPlayerEntity(playerID);
			topInfo.setName(playerEntity.getNickName());
			topInfo.setPlayerID(playerID);
			topInfo.setPhotoID(playerEntity.getPhotoID());
			topInfo.setLevel(playerEntity.getLevel());
			topInfo.setPlayerID(playerID);
			topInfo.setVipLevel(playerEntity.getVipLevel());
			topInfo.setFightValue(playerEntity.getFightValue());
			DungeonActMapEntity actMapEntity = this.getDungeonActEntity(playerID);
			Map<Integer, DungeonAct> actMap = actMapEntity.getDungeMap();
			Set<Integer> setMap = actMap.keySet();
			int actID = 0;
			for (Integer temp : setMap) {
				if (temp > actID) {
					actID = temp;
				}
			}
			topInfo.setActID(dungeonActStartID);
			topList.add(topInfo);
		}
		protocol.setTopList(topList);
		return topList;
	}

	/**
	 * 消除连战冷却时间
	 * */
	public void clearContinuousFightCooldown(String playerID, DungeonProtocol protocol) {
		DungeonMapEntity dungeonMapEntity = this.getDungeonMapEntity(playerID);
		int payNum = dungeonMapEntity.addContinuousPayNum();
		Map<String, Object> itemMap = new HashMap<>();
		RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
		int code = rewardModule.useGoods(playerID, KindIDs.GOLDTYPE, payNum * 10, 0, true, null, itemMap, null);
		if (code != 0) {
			protocol.setCode(code);
			return;
		}
		dungeonMapEntity.setLastContinuousTime(0);
		this.saveDungeonMapEntity(dungeonMapEntity);
		protocol.setContinuousPayNum(payNum);
		protocol.setLastContinuousTime(0);
		protocol.setItemMap(itemMap);

		// ////
		// // 元宝消耗记录
		// ///
		AnalyseModule analyseModule = ModuleManager.getModule(ModuleNames.AnalyseModule, AnalyseModule.class);
		analyseModule.goldCostLog(playerID, payNum * 10, payNum, 10, "clearContinuousFightCooldown", "dungeon");
	}
	
	
	/***
	 * 复活英雄的银币消耗
	 * */
	public void resurgenceHero(String playerID, int num,IOMessage ioMessage,DungeonProtocol protocol){
		int base = resurgenceBaseSilver;
		if(num < 1 || num > 5){
			throw new IllegalArgumentException(ErrorIds.ResurgenceNumWrong + "");
		}
		int silver = base * (int)Math.pow(2, num);
		Map<String,Object> itemMap = new HashMap<>();
		RewardModule  rewardModule = ModuleManager.getModule(ModuleNames.RewardModule,RewardModule.class);
		int code = rewardModule.useGoods(playerID, KindIDs.SILVERTYPE, silver, 0, true, null, itemMap, ioMessage);
		if(code != 0){
			protocol.setCode(code);
			return ;
		}
		protocol.setItemMap(itemMap);
	}
	
	public void saveActLimitReward(ActLimitRewardMapEntity entity){
		actLimitRewardMapDAO.save(entity);
	}
	
	public ActLimitRewardMapEntity getActLimitReward(String playerID){
		ActLimitRewardMapEntity actLimitRewardMapEntity = actLimitRewardMapDAO.getEntity(playerID);
		if(actLimitRewardMapEntity == null){
			actLimitRewardMapEntity = new ActLimitRewardMapEntity();
			actLimitRewardMapEntity.setKey(playerID);
			this.saveActLimitReward(actLimitRewardMapEntity);
		}
		return actLimitRewardMapEntity;
	}
	
	public ActLimitRewardMapEntity getUpdateActLimitEntity(String playerID){
		ActLimitRewardMapEntity actLimitRewardMapEntity = this.getActLimitReward(playerID);
//		Map<Integer,ActLimitReward> map = actLimitRewardMapEntity.getLimitList();
//		long nowTime = System.currentTimeMillis();
//		boolean isSave = false;
//		Iterator<Entry<Integer,ActLimitReward>> it = map.entrySet().iterator();  
//        while(it.hasNext()){  
//            Map.Entry<Integer,ActLimitReward> entry=it.next();  
//            ActLimitReward actLimitReward = entry.getValue();
//            if(actLimitReward.getOverTime() < nowTime){  
//                it.remove();   
//                isSave = true;
//            }  
//        } 
//        if(isSave){
//        	this.saveActLimitReward(actLimitRewardMapEntity);
//        }
		return actLimitRewardMapEntity;
	}
	
	/***
	 * 增加大关的限时奖励
	 * */
	private void addActLimitReward(String playerID, int actID,DungeonProtocol protocol){
		PayTimeRewardData data = LimitActDataMap.get(actID);
		if(data != null){
			ActLimitRewardMapEntity actLimitRewardMapEntity = this.getUpdateActLimitEntity(playerID);
			Map<Integer,ActLimitReward> map =  actLimitRewardMapEntity.getLimitList();
			ActLimitReward actLimitReward = new ActLimitReward();
			actLimitReward.setActID(actID);
			map.put(actID, actLimitReward);
			actLimitRewardMapEntity.setLimitList(map);
			this.saveActLimitReward(actLimitRewardMapEntity);
			protocol.setActLimitRewardMapEntity(actLimitRewardMapEntity);
		}
	}
	
	/**
	 * 移除限时购买
	 * */
	public void removeActLimitReward(String playerID, int actID){
		ActLimitRewardMapEntity actLimitRewardMapEntity = this.getUpdateActLimitEntity(playerID);
		Map<Integer,ActLimitReward> map =  actLimitRewardMapEntity.getLimitList();
		if(map.containsKey(actID)){
			map.remove(actID);
			this.saveActLimitReward(actLimitRewardMapEntity);
		}
	}
	
	/**
	 * 购买限时奖励
	 * */
	public void  buyActLimitReward(String playerID, int actID,int packageID,DungeonProtocol protocol,IOMessage ioMessage){
		ActLimitRewardMapEntity actLimitRewardMapEntity = this.getUpdateActLimitEntity(playerID);
		Map<Integer,ActLimitReward> map =  actLimitRewardMapEntity.getLimitList();
		LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule,LoginModule.class);	
		PlayerEntity playerEntity = loginModule.getPlayerEntity(playerID);
		int vipLevel = playerEntity.getVipLevel();
		if(vipLevel < packageID){
			logger.error("vip等级不足");
			throw new IllegalArgumentException(ErrorIds.REWARD_VIPLEVEL_ERROR + "");
		}
		if(map.containsKey(actID)){
			ActLimitReward actLimitReward = map.get(actID);
			List<Integer> buyList = actLimitReward.getBuyPackage();
			if(buyList.contains(packageID)){
				logger.error("已购买限时礼包");
				throw new IllegalArgumentException(ErrorIds.ALREADYBUYPACKAGE + "");
			}
			PayTimeRewardData data = LimitActDataMap.get(actID);
			Map<String,Object> itemMap = new HashMap<>();
			RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule,RewardModule.class);
			Map<Integer,Integer> priceMap = data.getPrice();
			int price = priceMap.get(packageID);
			int code = rewardModule.useGoods(playerID, KindIDs.GOLDTYPE, price, 0, true, null, itemMap, ioMessage);
			if(code != 0){
				protocol.setCode(code);
				return ;
			}
			AnalyseModule analyseModule = ModuleManager.getModule(ModuleNames.AnalyseModule, AnalyseModule.class);
			analyseModule.goldCostLog(playerID, price, 1, price, actID + "" + packageID, "actLimitReward");
			List<GoodsBean> goodsList = new ArrayList<GoodsBean>();
			Map<Integer, Integer> dataMap  = data.getReward().get(packageID);
			if(dataMap != null){
				for (Entry<Integer, Integer> entry : dataMap.entrySet()) {
					goodsList.add(new GoodsBean(entry.getKey(), entry.getValue()));
				}
			}
			rewardModule.addGoods(playerID, goodsList, true, null, itemMap, ioMessage);
			buyList.add(packageID);
			Set<Integer> keySet  = priceMap.keySet();
			boolean isRemove  = false;
			if(buyList.size() == keySet.size()){
				isRemove = true;
			}
			if(isRemove){
				map.remove(actID);
			}else{
				actLimitReward.setBuyPackage(buyList);
			}
			this.saveActLimitReward(actLimitRewardMapEntity);
			protocol.setItemMap(itemMap);
			protocol.setActLimitRewardMapEntity(actLimitRewardMapEntity);
		}else{
			throw new IllegalArgumentException(ErrorIds.NoChangeLimitReward + "");
		}
	}
	
public static void main(String[] args) {
	Map<Integer,Integer> map = new HashMap<>();
	map.put(1, 1);
	map.put(2, 2);
	map.put(3, 3);
	List<Integer> arrayList = new ArrayList<>();
	arrayList.add(1);
	arrayList.add(2);
	arrayList.add(3);
	Set<Integer> sets = map.keySet();
	for(Integer i : sets){
		if(!arrayList.contains(i)){
			System.out.println(false);
		}
	}
	
	
}
	
}
