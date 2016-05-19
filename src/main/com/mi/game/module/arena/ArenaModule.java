package com.mi.game.module.arena;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.TemplateManager;
import com.mi.core.engine.annotation.Module;
import com.mi.core.util.DateTimeUtil;
import com.mi.game.defines.ActionType;
import com.mi.game.defines.BattleMsgType;
import com.mi.game.defines.ErrorIds;
import com.mi.game.defines.KindIDs;
import com.mi.game.defines.MailType;
import com.mi.game.defines.ModuleNames;
import com.mi.game.defines.RewardType;
import com.mi.game.defines.SysConstants;
import com.mi.game.defines.SystemMsgType;
import com.mi.game.module.achievement.AchievementModule;
import com.mi.game.module.arena.dao.ArenaEntityDAO;
import com.mi.game.module.arena.dao.ArenaLuckyDAO;
import com.mi.game.module.arena.dao.ArenaShopEntityDAO;
import com.mi.game.module.arena.data.AreanRobotData;
import com.mi.game.module.arena.data.ArenaRewardData;
import com.mi.game.module.arena.data.ArenaShopData;
import com.mi.game.module.arena.data.RankRewardData;
import com.mi.game.module.arena.pojo.ArenaEntity;
import com.mi.game.module.arena.pojo.ArenaShopEntity;
import com.mi.game.module.arena.pojo.ArenaShopInfo;
import com.mi.game.module.arena.pojo.LuckRankMapEntity;
import com.mi.game.module.arena.pojo.LuckyInfo;
import com.mi.game.module.arena.protocol.ArenaInfo;
import com.mi.game.module.arena.protocol.ArenaProtocol;
import com.mi.game.module.arena.protocol.PKInfo;
import com.mi.game.module.base.BaseModule;
import com.mi.game.module.battleReport.BattleReportModule;
import com.mi.game.module.dayTask.DayTaskModule;
import com.mi.game.module.drop.DropModule;
import com.mi.game.module.equipment.EquipmentModule;
import com.mi.game.module.equipment.pojo.Equipment;
import com.mi.game.module.equipment.pojo.EquipmentMapEntity;
import com.mi.game.module.event.EventModule;
import com.mi.game.module.event.pojo.EventConfigEntity;
import com.mi.game.module.event.util.EventUtils;
import com.mi.game.module.festival.FestivalModule;
import com.mi.game.module.festival.define.FestivalConstants;
import com.mi.game.module.hero.HeroModule;
import com.mi.game.module.hero.dao.HeroDAO;
import com.mi.game.module.hero.data.HeroData;
import com.mi.game.module.hero.pojo.Hero;
import com.mi.game.module.hero.pojo.HeroEntity;
import com.mi.game.module.hero.pojo.HeroSkinEntity;
import com.mi.game.module.hero.pojo.HeroTroopsEntity;
import com.mi.game.module.lead.LeadModule;
import com.mi.game.module.lead.pojo.HeroPrototype;
import com.mi.game.module.lead.pojo.LeadDesitnyEntity;
import com.mi.game.module.legion.LegionModule;
import com.mi.game.module.legion.pojo.LegionEntity;
import com.mi.game.module.login.LoginModule;
import com.mi.game.module.login.dao.PlayerEntitiyDAO;
import com.mi.game.module.login.pojo.PlayerEntity;
import com.mi.game.module.mailBox.MailBoxModule;
import com.mi.game.module.mainTask.MainTaskModule;
import com.mi.game.module.manual.ManualModule;
import com.mi.game.module.manual.pojo.HeroManual;
import com.mi.game.module.manual.pojo.HeroManualsEntity;
import com.mi.game.module.reward.RewardModule;
import com.mi.game.module.reward.data.GoodsBean;
import com.mi.game.module.talisman.TalismanModule;
import com.mi.game.module.talisman.pojo.TalismanEntity;
import com.mi.game.module.talisman.pojo.TalismanMapEntity;
import com.mi.game.module.talisman.protocol.DrawInfo;
import com.mi.game.util.Base64Coder;
import com.mi.game.util.CommonMethod;
import com.mi.game.util.GZIPUtil;
import com.mi.game.util.Logs;
import com.mi.game.util.Utilities;

@Module(name = ModuleNames.ArenaModule, clazz = ArenaModule.class)
public class ArenaModule extends BaseModule {
	private final ArenaEntityDAO arenaEntityDAO = ArenaEntityDAO.getInstance();
	private final ArenaLuckyDAO arenaLuckyDAO = ArenaLuckyDAO.getInstance();
	private final Map<Long, ArenaRewardData> arenaDataMap = new HashMap<>();
	private final ArenaShopEntityDAO arenaShopEntityDAO = ArenaShopEntityDAO.getInstance();
	private int drawDropID = 103729;
	private int valentineDropID = FestivalConstants.diamondArenaDrop;
	private int foolDayDropID = FestivalConstants.FoolDayboxArenaDrop;
	private int dayRecover = 1000116;

	@Override
	public void init() {
		initArenaData();
		initEntity();
		this.initArenaRobotList();
	}

	public void initArenaRobotList() {
		long count = arenaEntityDAO.getCount();
		if(count == 0){
			List<AreanRobotData> dataList = TemplateManager.getTemplateList(AreanRobotData.class);
			int rank = 1;
			List<ArenaEntity> entityList = new ArrayList<>();
			for (AreanRobotData data : dataList) {
				int level = data.getLevel();
				ArenaEntity arenaEntity = new ArenaEntity();
				arenaEntity.setRank(rank);
				arenaEntity.setKey("npc" + data.getPid());
				arenaEntity.setLevel(level);
				rank++;
				entityList.add(arenaEntity);
			}
			arenaEntityDAO.save(entityList);
		}
	}

	private void initEntity() {
		if (this.getArenaLuckyEntity() == null) {
			LuckRankMapEntity entity = this.initArenaLuckyEntity();
			this.saveArenaLuckyEntity(entity);
		}
	}

	private void initArenaData() {
		List<ArenaRewardData> dataList = TemplateManager.getTemplateList(ArenaRewardData.class);

		for (ArenaRewardData data : dataList) {
			arenaDataMap.put(data.getRank(), data);
		}
	}

	public void initAreanEntity() {

	}

	/**
	 * 获取自己的竞技场实体
	 * */
	public ArenaEntity getArenaEntity(String playerID) {
		ArenaEntity arenaEntity = arenaEntityDAO.getEntity(playerID);
		if (arenaEntity == null) {
			if (!Utilities.isNpc(playerID)) {
				if (PlayerEntitiyDAO.getInstance().getEntity(playerID) != null) {
					long rank = this.getCount() + 1;
					arenaEntity = new ArenaEntity();
					arenaEntity.setRank(rank);
					arenaEntity.setKey(playerID);
					this.saveArenaEntity(arenaEntity);
				} else {
					logger.error("玩家实体为空");
					throw new IllegalArgumentException(ErrorIds.NoEntity + "");
				}
			}
		}
		return arenaEntity;
	}

	/**
	 * 获取竞技场排名
	 * */
	public long getArenaRank(String playerID) {
		ArenaEntity entity = this.getArenaEntity(playerID);
		return entity.getRank();
	}

	/**
	 * 按照排名获取竞技场的实体
	 * */
	public ArenaEntity getArenaEntityByRank(long rank) {
		return arenaEntityDAO.getEntityByRank(rank);
	}

	public long getCount() {
		return arenaEntityDAO.getCount();
	}

	/***
	 * 保存竞技场实体
	 * */
	public void saveArenaEntity(ArenaEntity entity) {
		arenaEntityDAO.save(entity);
	}

	/***
	 * 初始化npc实体
	 * */
	public void initNpc() {
		// this.initNpcHeroEntity();
		// this.initNpcPlayerEntity():
	}

	public void initNpcHeroEntity() {

	}

	public void saveArenaLuckyEntity(LuckRankMapEntity entity) {
		arenaLuckyDAO.save(entity);
	}

	public LuckRankMapEntity getArenaLuckyEntity() {
		LuckRankMapEntity arenaEntity = arenaLuckyDAO.getEntity("1");
		if (arenaEntity == null) {
			arenaEntity = this.initArenaLuckyEntity();
			this.saveArenaLuckyEntity(arenaEntity);
		}
		return arenaEntity;
	}

	private LuckRankMapEntity initArenaLuckyEntity() {
		Map<Long, LuckyInfo> luckyList = this.initLuckyList();
		LuckRankMapEntity entity = new LuckRankMapEntity();
		entity.setKey("1");
		entity.setNextRankList(luckyList);
		return entity;
	}

	private boolean getStatus() {
		return EventUtils.isChickenTime(SysConstants.ARENA_TIME);
//		String startTime = "22:00";
//		String endTime = "23:00";
//		long nowTime = System.currentTimeMillis();
//		Date startDate = DateTimeUtil.getDate(startTime);
//		Date endDate = DateTimeUtil.getDate(endTime);
//		Date nowDate = new Date(nowTime);
//		if (nowDate.after(startDate) && nowDate.before(endDate)) {
//			return false;
//		}
//		return true;
	}

	public Map<Long, LuckyInfo> initLuckyList() {
		List<Long> rankList = new ArrayList<>();
		Map<Long, LuckyInfo> luckyList = new HashMap<>();
		int gold = 0;
		for (int i = 0; i < 10; i++) {
			long random = Utilities.getRandomInt(500) + 1;
			if (rankList.contains(random)) {
				i--;
			} else {
				rankList.add(random);
				if (i < 3) {
					gold = 50;
				} else {
					gold = 25;
				}
				LuckyInfo luckyInfo = new LuckyInfo();
				luckyInfo.setGold(gold);
				luckyInfo.setRank(random);
				luckyList.put(random, luckyInfo);
			}
		}
		return luckyList;
	}

	/**
	 * 获取竞技场信息
	 * */
	public void getAreanInfo(String playerID, ArenaProtocol protocol) {
		ArenaEntity entity = this.getArenaEntity(playerID);
		LegionModule legionModule = ModuleManager.getModule(ModuleNames.LegionModule, LegionModule.class);
		long rank = entity.getRank();
		protocol.setRank(rank);
		Map<String, ArenaInfo> arenaInfoMap = new HashMap<String, ArenaInfo>();
		List<Long> showIDList = this.getRankInfoList(rank);
		List<Object> searchList = new ArrayList<>();
		Map<String, Long> rankMap = new HashMap<>();
		for (long tempRank : showIDList) {
			ArenaEntity tempEntity = this.getArenaEntityByRank(tempRank);
			if (tempEntity != null) {
				searchList.add(tempEntity.getKey());
				rankMap.put(tempEntity.getKey().toString(), tempRank);
			}
		}
		for (Object temp : searchList) {
			String searchID = temp.toString();
			ArenaInfo arenaInfo = new ArenaInfo();
			arenaInfo.setPlayerID(searchID);
			this.getrankReward(rankMap.get(searchID), arenaInfo);
			arenaInfo.setRank(rankMap.get(searchID));
			List<Integer> heroTemplateList = new ArrayList<Integer>();
			if (Utilities.isNpc(searchID)) {
				int robotID = Integer.parseInt(searchID.substring(3));
				AreanRobotData areanRobotData = TemplateManager.getTemplateData(robotID, AreanRobotData.class);
				heroTemplateList = areanRobotData.getHero();
				arenaInfo.setName(areanRobotData.getName());
				arenaInfo.setLevel(areanRobotData.getLevel());
				arenaInfo.setPhotoID(areanRobotData.getPhotoID());
			} else {
				HeroEntity heroEntity = HeroDAO.getInstance().getEntity(searchID);
				List<Long> teamList = heroEntity.getTeamList();
				PlayerEntity playerEntity = PlayerEntitiyDAO.getInstance().getEntity(searchID);
				arenaInfo.setName(playerEntity.getNickName());
				arenaInfo.setVipLevel(playerEntity.getVipLevel());
				arenaInfo.setPhotoID(playerEntity.getPhotoID());
				arenaInfo.setLevel(playerEntity.getLevel());
				String groupID = playerEntity.getGroupID();
				if (!groupID.equals("0")) {
					LegionEntity legionEntity = legionModule.getLegionEntity(groupID);
					if (legionEntity != null) {
						arenaInfo.setGroupName(legionEntity.getName());
					}
				}
				for (long heroID : teamList) {
					if (heroID != 0) {
						Hero hero = heroEntity.getHeroMap().get(heroID + "");
						if (hero != null) {
							heroTemplateList.add(hero.getTemplateID());
						}
					}
				}
			}
			arenaInfo.setHeroList(heroTemplateList);
			arenaInfoMap.put(temp.toString(), arenaInfo);
		}
		protocol.setArenaInfoList(arenaInfoMap);

	}

	// private void addRobot(long rank, int level) {
	// ArenaEntity arenaEntity = new ArenaEntity();
	// arenaEntity.setRank(rank);
	// RobotModule robotModule =
	// ModuleManager.getModule(ModuleNames.RobotModule, RobotModule.class);
	// long robotID = robotModule.getRobotID();
	// arenaEntity.setLevel(level);
	// arenaEntity.setTemplateID(0);
	// arenaEntity.setKey("npcA" + robotID);
	//
	// }

	private void getrankReward(long rank, ArenaInfo arInfo) {
		EventModule eventModule = ModuleManager.getModule(ModuleNames.EventModule, EventModule.class);
		int silver = 0;
		int reputation = 0;
		if (rank <= 100) {
			ArenaRewardData data = arenaDataMap.get(rank);
			silver = data.getCoinReward();
			reputation = data.getReputationReward();
		} else if (rank > 100 && rank < 3001) {
			silver = 9500 - (int) (rank * 0.9);
			reputation = 2000 - (int) (rank * 0.4);
		} else {
			silver = 6500;
			reputation = 700;
		}
		// 获取竞技场奖励翻倍奖励倍数
		int param = eventModule.getWelfareReward(SysConstants.ARENA_ACTIVE_ID);
		silver = silver * param;
		reputation = reputation * param;

		arInfo.setSilver(silver);
		arInfo.setReputation(reputation);
	}

	// private List<PlayerEntity> getPlayerEntityInList(List<Object> list) {
	// List<PlayerEntity> playerList =
	// PlayerEntitiyDAO.getInstance().getEntityInList(list);
	// return playerList;
	// }
	//
	// private List<HeroEntity> getHeroEntityInList(List<Object> list) {
	// List<HeroEntity> heroList = HeroDAO.getInstance().getEntityInList(list);
	// return heroList;
	// }

	public List<Long> getRankInfoList(long rank) {
		List<Long> rankList = new ArrayList<>();
		if (rank <= 100) {
			long min = rank - 8;
			if (min < 1) {
				min = 1;
			}
			for (long i = min; i <= rank; i++) {
				rankList.add(i);

			}
			long max = rank + 2;
			for (long i = max; i > rank; i--) {
				rankList.add(i);
			}
		} else {
			long min = (long) (rank * 0.8);
			long max = (long) (rank * 1.2);
			for (int i = 0; i < 8; i++) {
				long random = rank - Utilities.getRandomInt((int) (rank - min));
				if (rankList.contains(random)) {
					i--;
				} else {
					rankList.add(random);
				}
			}
			for (int i = 0; i < 2; i++) {
				long random = rank + Utilities.getRandomInt((int) (max - rank));
				if (rankList.contains(random)) {
					i--;
				} else {
					rankList.add(random);
				}
			}
			rankList.add(rank);
		}

		Collections.sort(rankList);
		return rankList;
	}

	/***
	 * 获取指定角色的装备 英雄列表 宝物
	 * */
	public void getBattleInfo(String playerID, String searchID, ArenaProtocol protocol) {
		HeroModule heroModule = ModuleManager.getModule(ModuleNames.HeroModule, HeroModule.class);
		List<Hero> heroList = new ArrayList<Hero>();
		List<Equipment> equipments = new ArrayList<Equipment>();
		List<TalismanEntity> talismans = new ArrayList<>();
		List<Long> troops = new ArrayList<>();
		List<Long> teamList = new ArrayList<>();
		Map<Integer, HeroPrototype> destinyPrototype = new HashMap<>();
		List<HeroManual> manualList = new ArrayList<>();
		int fightValue = 0;
		int vipLevel = 0;
		String groupName = "";
		LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule, LoginModule.class);

		if (Utilities.isNpc(playerID)) {
			ArenaEntity arenaEntity = this.getArenaEntity(playerID);
			int randomLevel = 0;
			int robotID = Integer.parseInt(playerID.substring(3));
			if (arenaEntity != null) {
				randomLevel = arenaEntity.getLevel();
				AreanRobotData areanRobotData = TemplateManager.getTemplateData(robotID, AreanRobotData.class);
				List<Integer> list = areanRobotData.getHero();
				long heroID = 1;
				for (int temp : list) {
					if (temp != 0) {
						Hero hero = new Hero();
						if(temp == 1042201 || temp == 1042111){
							hero.setHeroID(10431);
						}else{
							heroID++;
							hero.setHeroID(heroID);
						}
						hero.setTemplateID(temp);
						heroList.add(hero);
					}
				}
			} else {
				PlayerEntity playerEntity = loginModule.getPlayerEntity(searchID);
				randomLevel = playerEntity.getLevel();
				heroList = TalismanModule.robotList.get(robotID);
			}
			for (int i = 0; i < 12; i++) {
				teamList.add(0l);
			}
			for (int i = 0; i < heroList.size(); i++) {
				Hero hero = heroList.get(i);
				hero.setLevel(randomLevel);
				troops.add(hero.getHeroID());
				teamList.set(i, hero.getHeroID());
			}
		} else {
			HeroSkinEntity heroSkinEntity = heroModule.getHeroSkin(playerID);
			protocol.setHeroSkinEntity(heroSkinEntity);
			PlayerEntity playerEntity = loginModule.getPlayerEntity(playerID);
			fightValue = playerEntity.getFightValue();
			HeroEntity heroEntity = heroModule.getHeroEntity(playerID);
			HeroTroopsEntity troopsEntity = heroModule.getHeroTroopsEntity(playerID);
			troops = troopsEntity.getTroops();
			teamList = heroEntity.getTeamList();
			Map<String, Hero> heroMap = heroEntity.getHeroMap();
			vipLevel = playerEntity.getVipLevel();
			String groupID = playerEntity.getGroupID();
			if (!groupID.equals("0")) {
				LegionModule legionModule = ModuleManager.getModule(ModuleNames.LegionModule,LegionModule.class);
				LegionEntity legionEntity = legionModule.getLegionEntity(groupID);
				if (legionEntity != null) {
					groupName = legionEntity.getName();
				}
			}
			ManualModule manualModule = ModuleManager.getModule(ModuleNames.ManualModule, ManualModule.class);
			HeroManualsEntity heroManualsEntity = manualModule.getEntity(playerID);
			Map<Integer, HeroManual> allManualList = heroManualsEntity.getHeroList();
			for (Long heroID : teamList) {
				Hero hero = heroMap.get(heroID + "");
				if (hero != null) {
					int templateID = hero.getTemplateID();
					HeroData heroData = TemplateManager.getTemplateData(templateID, HeroData.class);
					int charactorID = heroData.getCharactorID();
					HeroManual heroManual = allManualList.get(charactorID);
					if (heroManual != null) {
						manualList.add(heroManual);
					}
					heroList.add(hero);
				}
			}

			EquipmentModule equipmentModule = ModuleManager.getModule(ModuleNames.EquipmentModule, EquipmentModule.class);
			EquipmentMapEntity equipmentMapEntity = equipmentModule.getEquipmentMapEntity(playerID);
			Map<String, Equipment> equipMap = equipmentMapEntity.getEquipMap();
			for (Entry<String, Equipment> entry : equipMap.entrySet()) {
				Equipment equipment = entry.getValue();
				if (equipment.getHeroID() != 0) {
					equipments.add(equipment);
				}
			}

			TalismanModule talismanModule = ModuleManager.getModule(ModuleNames.TalismanModule, TalismanModule.class);
			TalismanMapEntity talismanEntity = talismanModule.getEntity(playerID);
			Map<String, TalismanEntity> talismanMap = talismanEntity.getTalismanMap();
			for (Entry<String, TalismanEntity> entry : talismanMap.entrySet()) {
				TalismanEntity talisman = entry.getValue();
				if (talisman.getHeroID() != 0) {
					talismans.add(talisman);
				}
			}

			LeadModule leadModule = ModuleManager.getModule(ModuleNames.LeadModule, LeadModule.class);
			LeadDesitnyEntity leadDesitnyEntity = leadModule.getDesitnyEntity(playerID);
			destinyPrototype = leadDesitnyEntity.getPrototype();

		}
		PKInfo pkInfo = new PKInfo();
		pkInfo.setGroupName(groupName);
		pkInfo.setVipLevel(vipLevel);
		pkInfo.setDestinyPrototype(destinyPrototype.values());
		pkInfo.setTeamList(teamList);
		pkInfo.setTroops(troops);
		pkInfo.setEquipmentList(equipments);
		pkInfo.setTalismanList(talismans);
		pkInfo.setHeroList(CommonMethod.getResponseListMap(heroList));
		pkInfo.setFightValue(fightValue);
		pkInfo.setManualList(manualList);
		protocol.setPkInfo(pkInfo);

	}

	/**
	 * 获取竞技场战斗结果
	 * */
	public void ArenaBattle(String playerID, String pkID, boolean win, String battleString, IOMessage ioMessage, ArenaProtocol protocol) {
		if (getStatus()) {
			logger.error("竞技场战斗时间错误");
			throw new IllegalArgumentException(ErrorIds.AreanRewardStaus + "");
		}
		ArenaEntity pkEntity = this.getArenaEntity(pkID);
		ArenaEntity playerArenaEntity = this.getArenaEntity(playerID);
		long pkRank = pkEntity.getRank();
		long myRank = playerArenaEntity.getRank();
		protocol.setRank(myRank);
		// if(pkRank - myRank > 10 && myRank < 100){
		// throw new IllegalArgumentException(ErrorIds.ArenaRankWrong +"");
		// }
		Map<String, Object> itemMap = new HashMap<>();
		RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
		HeroModule heroModule = ModuleManager.getModule(ModuleNames.HeroModule, HeroModule.class);
		Hero lead = heroModule.getLead(playerID, ioMessage);
		int level = lead.getLevel();
		int code = rewardModule.useGoods(playerID, KindIDs.ENERGY, 2, 0, true, null, itemMap, ioMessage);
		if (code != 0) {
			throw new IllegalArgumentException(code + "");
		}

		List<GoodsBean> showMap = new ArrayList<>();
		List<GoodsBean> goodsList = new ArrayList<>();
		int silver = 0;
		int exp = 0;
		int reputation = 10;
		int msgStatus = 0;
		Map<String, Object> msgParam = new HashMap<>();
		if (win) {
			msgStatus = BattleMsgType.BEATTACKFAILEDNOLOW;
			reputation = 20;
			silver = level * 25;
			exp = level * 2;
			if (myRank > pkRank) {
				msgParam.put("rank", myRank);
				msgStatus = BattleMsgType.BEATTACKFAILED;
				pkEntity.setRank(myRank);
				playerArenaEntity.setRank(pkRank);
				this.saveArenaEntity(pkEntity);
				this.saveArenaEntity(playerArenaEntity);
				protocol.setRank(pkRank);
			}
			protocol.setDrawList(this.getDrawList(goodsList));
		} else {
			msgStatus = BattleMsgType.BEATTACKWIN;
			exp = level;
		}
		MailBoxModule mailBoxModule = ModuleManager.getModule(ModuleNames.MailBoxModule, MailBoxModule.class);
		if (!Utilities.isNpc(pkID)) {
			if (!battleString.isEmpty()) {
				BattleReportModule battleReportModule = ModuleManager.getModule(ModuleNames.BattleReportModule, BattleReportModule.class);
				try {
					// 压缩
					byte[] bytes = GZIPUtil.compress(battleString);
					// 编码
					String base64 = new String(Base64Coder.encode(bytes));
					// 保存
					long reportID = battleReportModule.saveReport(base64);
					msgParam.put("reportID", reportID);
				} catch (Exception e) {
					logger.error("战斗录像压缩存储错误!");
				}
			}
			mailBoxModule.addMail(pkID, null, playerID, null, MailType.BATTLETYPE, msgStatus, msgParam);
		}
		GoodsBean reputationBean = new GoodsBean(KindIDs.REPUTATION, reputation);
		goodsList.add(reputationBean);
		showMap.add(reputationBean);
		GoodsBean silverBean = new GoodsBean(KindIDs.SILVERTYPE, silver);
		goodsList.add(silverBean);
		showMap.add(silverBean);
		GoodsBean expBean = new GoodsBean(KindIDs.LEADEXP, exp);
		goodsList.add(expBean);
		rewardModule.addGoods(playerID, goodsList, true, null, itemMap, ioMessage);
		showMap.add(expBean);
		protocol.setShowMap(showMap);
		protocol.setItemMap(itemMap);
		DayTaskModule dayTaskModule = ModuleManager.getModule(ModuleNames.DayTaskModule, DayTaskModule.class);
		dayTaskModule.addScore(playerID, ActionType.ARENA, 1);
		EventModule eventModule = ModuleManager.getModule(ModuleNames.EventModule, EventModule.class);
		eventModule.intefaceDrawIntegral(playerID, 10893);
		MainTaskModule mainTaskModule = ModuleManager.getModule(ModuleNames.MainTaskModule,MainTaskModule.class);
		mainTaskModule.updateTaskByActionType(playerID, ActionType.ARENARANK, (int)playerArenaEntity.getRank(), null);
		mainTaskModule.updateTaskByActionType(playerID, ActionType.ARENANUM, 0, ioMessage);
	}

	private List<DrawInfo> getDrawList(List<GoodsBean> goodsList) {
		List<DrawInfo> drawList = new ArrayList<DrawInfo>();
		FestivalModule festivalModule = ModuleManager.getModule(ModuleNames.FestivalModule,FestivalModule.class);
		int dropID = 0;
		if(festivalModule.isInValentine()){
			dropID = valentineDropID;
		}else if(festivalModule.isInFoolDay()){
			dropID = foolDayDropID;
		}else{
			dropID = drawDropID;
		}
		GoodsBean dropBean = DropModule.doDrop(dropID);
		DrawInfo drawInfo = new DrawInfo();
		drawInfo.setDraw(true);
		drawInfo.setPid(dropBean.getPid());
		drawInfo.setNum(dropBean.getNum());
		drawList.add(drawInfo);
		boolean isFind = false;
		for (GoodsBean goodsBean : goodsList) {
			if (goodsBean.getPid() == dropBean.getPid()) {
				goodsBean.setNum(goodsBean.getNum() + dropBean.getNum());
				isFind = true;
				break;
			}
		}
		if (!isFind) {
			goodsList.add(dropBean);
		}
		for (int i = 0; i < 2; i++) {
			DrawInfo unDrawInfo = new DrawInfo();
			GoodsBean unDropBean = DropModule.doDrop(drawDropID);
			unDrawInfo.setNum(unDropBean.getNum());
			unDrawInfo.setPid(unDropBean.getPid());
			drawList.add(unDrawInfo);
		}
		return drawList;
	}

	/**
	 * 获取竞技场前十排名
	 * */
	public void getRankTopList(ArenaProtocol protocol) {
		LegionModule legionModule = ModuleManager.getModule(ModuleNames.LegionModule, LegionModule.class);
		List<ArenaEntity> rankList = arenaEntityDAO.getTopList();
		List<Object> searchList = new ArrayList<>();
		Map<String, Long> rankMap = new HashMap<>();
		Map<String, ArenaInfo> arenaInfoMap = new HashMap<String, ArenaInfo>();
		for (ArenaEntity entity : rankList) {
			String playerID = entity.getKey().toString();
			searchList.add(playerID);
			rankMap.put(playerID, entity.getRank());
		}
		for (Object temp : searchList) {
			String searchID = temp.toString();
			ArenaInfo arenaInfo = new ArenaInfo();
			arenaInfo.setPlayerID(searchID);
			arenaInfoMap.put(temp.toString(), arenaInfo);
			this.getrankReward(rankMap.get(searchID), arenaInfo);
			arenaInfo.setRank(rankMap.get(searchID));
			List<Integer> heroTemplateList = new ArrayList<Integer>();
			if (Utilities.isNpc(searchID)) {
				int robotID = Integer.parseInt(searchID.substring(3));
				AreanRobotData areanRobotData = TemplateManager.getTemplateData(robotID, AreanRobotData.class);
				heroTemplateList = areanRobotData.getHero();
				arenaInfo.setName(areanRobotData.getName());
				arenaInfo.setLevel(areanRobotData.getLevel());
				arenaInfo.setPhotoID(areanRobotData.getPhotoID());
			} else {
				HeroEntity heroEntity = HeroDAO.getInstance().getEntity(searchID);
				List<Long> teamList = heroEntity.getTeamList();
				PlayerEntity playerEntity = PlayerEntitiyDAO.getInstance().getEntity(searchID);
				arenaInfo.setName(playerEntity.getNickName());
				arenaInfo.setVipLevel(playerEntity.getVipLevel());
				arenaInfo.setPhotoID(playerEntity.getPhotoID());
				arenaInfo.setLevel(playerEntity.getLevel());
				String groupID = playerEntity.getGroupID();
				if (!groupID.equals("0")) {
					LegionEntity legionEntity = legionModule.getLegionEntity(groupID);
					if (legionEntity != null) {
						arenaInfo.setGroupName(legionEntity.getName());
					}
				}
				for (long heroID : teamList) {
					if (heroID != 0) {
						Hero hero = heroEntity.getHeroMap().get(heroID + "");
						if (hero != null) {
							heroTemplateList.add(hero.getTemplateID());
						}
					}
				}
			}
			arenaInfo.setHeroList(heroTemplateList);
			arenaInfoMap.put(searchID, arenaInfo);
		}
		protocol.setArenaInfoList(arenaInfoMap);
	}

	public LuckRankMapEntity getLuckyRankList() {
		LuckRankMapEntity entity = this.getArenaLuckyEntity();
		return entity;
	}

	/**
	 * 保存竞技场商店实体
	 * */
	public void saveShopEntity(ArenaShopEntity entity) {
		arenaShopEntityDAO.save(entity);
	}

	/**
	 * 获取竞技场商店实体
	 * */
	public ArenaShopEntity getShopEntity(String playerID) {
		ArenaShopEntity entity = arenaShopEntityDAO.getEntity(playerID);
		if (entity == null) {
			entity = this.initShopEntity(playerID);
			this.saveShopEntity(entity);
		}
		return entity;
	}

	/**
	 * 返回竞技场兑换列表
	 * */
	public ArenaShopEntity getExchangeList(String playerID) {
		ArenaShopEntity entity = this.getShopEntity(playerID);
		long updateTime = entity.getUpdateTime();
		long nowTime = System.currentTimeMillis();
		Map<Integer, ArenaShopInfo> shopMap = entity.getShopMap();
		if (!DateTimeUtil.isSameDay(updateTime, nowTime)) {
			for (Entry<Integer, ArenaShopInfo> entry : shopMap.entrySet()) {
				int pid = entry.getKey();
				ArenaShopData data = TemplateManager.getTemplateData(pid, ArenaShopData.class);
				if(data != null){
					ArenaShopInfo shopInfo = entry.getValue();
					GoodsBean limit = data.getAmountLimit();
					if (limit.getPid() == dayRecover) {
						shopInfo.setNum(limit.getNum());
					}
				}else{
					logger.error("123");
				}
				
			}
			entity.setUpdateTime(nowTime);
			this.saveShopEntity(entity);
		}
		return entity;
	}

	/**
	 * 初始化竞技场兑换列表
	 * */
	public ArenaShopEntity initShopEntity(String playerID) {
		ArenaShopEntity entity = new ArenaShopEntity();
		entity.setKey(playerID);
		Map<Integer, ArenaShopInfo> shopMap = new HashMap<>();
		List<ArenaShopData> dataList = TemplateManager.getTemplateList(ArenaShopData.class);
		for (ArenaShopData data : dataList) {
			ArenaShopInfo shopInfo = new ArenaShopInfo();
			shopInfo.setPid(data.getPid());
			shopInfo.setNum(data.getAmountLimit().getNum());
			shopMap.put(data.getPid(), shopInfo);
		}
		entity.setShopMap(shopMap);
		entity.setUpdateTime(System.currentTimeMillis());
		return entity;
	}

	/**
	 * 兑换竞技场奖励
	 * */

	public void exchangeArenaGoods(String playerID, int pid, int num, ArenaProtocol protocol) {
		if (num < 0) {
			throw new IllegalArgumentException(ErrorIds.ParamWrong + "");
		}
		ArenaShopEntity entity = this.getExchangeList(playerID);
		Map<Integer, ArenaShopInfo> shopMap = entity.getShopMap();
		if (!shopMap.containsKey(pid)) {
			throw new IllegalArgumentException(ErrorIds.ArenaShopIDWrong + "");
		}
		ArenaShopInfo arenaShopInfo = shopMap.get(pid);
		int shopNum = arenaShopInfo.getNum();
		shopNum -= num;
		if (shopNum < 0) {
			throw new IllegalArgumentException(ErrorIds.ArenaShopItemNotEnough + "");
		}
		ArenaShopData shopData = TemplateManager.getTemplateData(pid, ArenaShopData.class);
		GoodsBean good = shopData.getItemID();
		GoodsBean addGoods = new GoodsBean(good.getPid(), good.getNum() * num);
		int price = shopData.getPrice();
		price *= num;
		RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
		Map<String, Object> itemMap = new HashMap<>();
		List<GoodsBean> showMap = new ArrayList<>();
		showMap.add(addGoods);
		int code = rewardModule.useGoods(playerID, KindIDs.REPUTATION, price, 0, true, null, itemMap, null);
		if (code != 0) {
			protocol.setCode(code);
			return;
		}
		arenaShopInfo.setNum(shopNum);
		rewardModule.addGoods(playerID, addGoods.getPid(), addGoods.getNum() , null, true, null, itemMap, null);
		this.saveShopEntity(entity);
		MainTaskModule mainTaskModule = ModuleManager.getModule(ModuleNames.MainTaskModule,MainTaskModule.class);
		mainTaskModule.updateTaskByActionType(playerID, ActionType.ARENAEXCHANGE, 0, null);
		protocol.setArenaShopInfo(arenaShopInfo);
		protocol.setItemMap(itemMap);
		protocol.setShowMap(showMap);
	}

	/**
	 * 给玩家发送pk奖励
	 * */
	public void givePKReward(String playerID, long rank) {
		ArenaInfo arenaInfo = new ArenaInfo();
		this.getrankReward(rank, arenaInfo);
		RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
		GoodsBean silverbBean = new GoodsBean(KindIDs.SILVERTYPE, arenaInfo.getSilver());
		GoodsBean repuGoodsBean = new GoodsBean(KindIDs.REPUTATION, arenaInfo.getReputation());
		List<GoodsBean> goodsList = new ArrayList<>();
		goodsList.add(silverbBean);
		goodsList.add(repuGoodsBean);
		rewardModule.addReward(playerID, goodsList, RewardType.arenaReward);
		MailBoxModule mailModule = ModuleManager.getModule(ModuleNames.MailBoxModule, MailBoxModule.class);
		Map<String, Object> msgParam = new HashMap<>();
		msgParam.put("silver", arenaInfo.getSilver());
		msgParam.put("reputation", arenaInfo.getReputation());
		msgParam.put("rank", rank);
		mailModule.addMail(playerID, null, null, null, MailType.SYSTEMTYPE, SystemMsgType.ARENAREWARD, msgParam);
		AchievementModule acModule = ModuleManager.getModule(ModuleNames.AchievementModule, AchievementModule.class);
		acModule.refreshAchievement(playerID, ActionType.ARENARANK, rank);
	}

	/**
	 * 玩家发放幸运奖励
	 * */
	public void giveLuckyReward(String playerID, long rank, int gold) {
		RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
		List<GoodsBean> goodsList = new ArrayList<>();
		GoodsBean goldBean = new GoodsBean(KindIDs.GOLDTYPE, gold);
		goodsList.add(goldBean);
		rewardModule.addReward(playerID, goodsList, RewardType.arenaLuckyReward);
		MailBoxModule mailModule = ModuleManager.getModule(ModuleNames.MailBoxModule, MailBoxModule.class);
		Map<String, Object> msgParam = new HashMap<>();
		msgParam.put("rank", rank);
		msgParam.put("gold", gold);
		mailModule.addMail(playerID, null, null, null, MailType.SYSTEMTYPE, SystemMsgType.ARENALUCKYREWARD, msgParam);
	}

	/**
	 * 查询排行榜前200名,并发送奖励
	 * 
	 * @return
	 */
	public void sendTopTwoHundredReward(EventConfigEntity eventInfo) {
		// 判断是不是活动时间
		List<ArenaEntity> rankList = arenaEntityDAO.getTopTwoHundredList();
		if (rankList == null || rankList.size() == 0) {
			return;
		}
		Logs.logger.info(eventInfo.getDesc() + "开始发放");
		long nowTime = System.currentTimeMillis();
		for (int i = 0; i < rankList.size(); i++) {
			ArenaEntity arenaEntity = rankList.get(i);
			String playerID = arenaEntity.getKey().toString();
			if (!Utilities.isNpc(playerID)) { // 不是NPC,发送奖励
				// TODO 获取奖励物品
				List<RankRewardData> rewardList = this.getRankRewardList();
				for (RankRewardData rewardData : rewardList) { // 循环
					long lowRank = rewardData.getLowRank();
					long highRank = rewardData.getHighRank();
					if (i >= (lowRank - 1) && i <= (highRank - 1)) { // 发送奖励
						sendSprotsRankReward(playerID, rewardData);
						break;
					}
				}
			}
		}
		Logs.logger.info(eventInfo.getDesc() + "奖励发放结束,耗时:"
				+ (System.currentTimeMillis() - nowTime) + "ms");
	}

	/**
	 * 获取奖励物品,并发送奖励
	 * 
	 * @param playerID
	 * @param rewardData
	 * @return
	 */
	public void sendSprotsRankReward(String playerID,
			RankRewardData rewardData) {
		RewardModule rewardModule = ModuleManager.getModule(
				ModuleNames.RewardModule, RewardModule.class);
		List<GoodsBean> goodsList = new ArrayList<GoodsBean>();
		String itemID = rewardData.getItemID();
		String[] itemArr = itemID.split(",");
		for (String itemInfo : itemArr) {
			GoodsBean goodsBean = new GoodsBean();
			String[] infoArr = itemInfo.split("=");
			int goodID = Integer.valueOf(infoArr[0]);
			int num = Integer.valueOf(infoArr[1]);
			goodsBean.setPid(goodID);
			goodsBean.setNum(num);
			goodsList.add(goodsBean);
		}

		if (goodsList != null && goodsList.size() > 0) {
			rewardModule.addReward(playerID, goodsList,
					RewardType.event_sports_rank);
		}

	}

	/**
	 * 获取竞技排行奖励列表
	 * 
	 * @return
	 */
	public List<RankRewardData> getRankRewardList() {
		List<RankRewardData> rankRewardList = TemplateManager
				.getTemplateList(RankRewardData.class);
		return rankRewardList;
	}

}
