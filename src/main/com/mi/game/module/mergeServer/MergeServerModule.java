package com.mi.game.module.mergeServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mi.core.cache.QueryInfo;
import com.mi.core.cache.QueryType;
import com.mi.core.cache.bean.QueryBean;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.Module;
import com.mi.core.pojo.BaseEntity;
import com.mi.core.util.ConfigUtil;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.arena.ArenaModule;
import com.mi.game.module.arena.dao.ArenaEntityDAO;
import com.mi.game.module.arena.dao.ArenaLuckyDAO;
import com.mi.game.module.arena.dao.ArenaShopEntityDAO;
import com.mi.game.module.bag.dao.BagEntityDAO;
import com.mi.game.module.base.BaseModule;
import com.mi.game.module.battleReport.dao.BattleReportEntityDAO;
import com.mi.game.module.dayTask.dao.DayTaskEntityDAO;
import com.mi.game.module.dungeon.dao.ActLimitRewardMapDAO;
import com.mi.game.module.dungeon.dao.DungeonActMapEntityDAO;
import com.mi.game.module.dungeon.dao.DungeonActiveDAO;
import com.mi.game.module.dungeon.dao.DungeonEliteEntityDAO;
import com.mi.game.module.dungeon.dao.DungeonMapEntityDAO;
import com.mi.game.module.effect.dao.PlayerEffectDAO;
import com.mi.game.module.equipment.dao.EquipmentEntityDAO;
import com.mi.game.module.event.dao.EventChickenEntityDao;
import com.mi.game.module.event.dao.EventDrawIntegralEntityDao;
import com.mi.game.module.event.dao.EventDrawPayEntityDao;
import com.mi.game.module.event.dao.EventExchangeEntityDao;
import com.mi.game.module.event.dao.EventExpenseEntityDao;
import com.mi.game.module.event.dao.EventExploreEntityDao;
import com.mi.game.module.event.dao.EventMonthCardDao;
import com.mi.game.module.event.dao.EventPayEntityDao;
import com.mi.game.module.event.dao.EventShopEntityDao;
import com.mi.game.module.event.dao.EventTimeLimitDao;
import com.mi.game.module.event.dao.EventTraderEntityDao;
import com.mi.game.module.farm.dao.FarmEntityDAO;
import com.mi.game.module.friend.FriendModule;
import com.mi.game.module.friend.dao.FriendEntityDAO;
import com.mi.game.module.friend.pojo.FriendEntity;
import com.mi.game.module.friend.pojo.FriendState;
import com.mi.game.module.hero.dao.HeroDAO;
import com.mi.game.module.hero.dao.HeroSkinEntityDAO;
import com.mi.game.module.hero.dao.HeroTroopsDAO;
import com.mi.game.module.heroDraw.dao.HeroDrawDAO;
import com.mi.game.module.lead.dao.DestinyEntityDAO;
import com.mi.game.module.legion.LegionModule;
import com.mi.game.module.legion.dao.LegionEntityDAO;
import com.mi.game.module.legion.dao.LegionHistoryEntityDAO;
import com.mi.game.module.legion.dao.LegionMemberEntityDAO;
import com.mi.game.module.legion.pojo.LegionEntity;
import com.mi.game.module.legion.pojo.LegionMemberEntity;
import com.mi.game.module.login.LoginModule;
import com.mi.game.module.login.dao.NewPlayerEntityDAO;
import com.mi.game.module.login.dao.PlayerEntitiyDAO;
import com.mi.game.module.login.dao.PlayerStatuesEntityDAO;
import com.mi.game.module.login.pojo.PlayerEntity;
import com.mi.game.module.login.pojo.PlayerStatusEntity;
import com.mi.game.module.mailBox.dao.MailBoxEntityDAO;
import com.mi.game.module.mainTask.dao.MainTaskEntityDAO;
import com.mi.game.module.manual.dao.HeroManualDAO;
import com.mi.game.module.mergeServer.define.MergeServerDefine;
import com.mi.game.module.pay.PayModule;
import com.mi.game.module.pay.dao.PayEntityDao;
import com.mi.game.module.pay.pojo.PayEntity;
import com.mi.game.module.pet.dao.PetDAO;
import com.mi.game.module.pk.dao.PkEntityDao;
import com.mi.game.module.pk.dao.PkExchangeHistoryEntityDao;
import com.mi.game.module.pk.dao.PkRewardEntityDao;
import com.mi.game.module.pk.pojo.PkEntity;
import com.mi.game.module.reward.dao.RewardDAO;
import com.mi.game.module.talisman.dao.TalismanMapDAO;
import com.mi.game.module.talisman.dao.TalismanShardDAO;
import com.mi.game.module.tower.dao.TowerEntityDAO;
import com.mi.game.module.vip.dao.VipEnitiyDao;
import com.mi.game.module.vitatly.dao.VitatlyDAO;
import com.mi.game.module.wallet.dao.WalletDAO;
import com.mi.game.module.welfare.dao.WelfareEntityDao;
import com.mi.game.module.welfare.dao.WelfareLevelEntityDao;
import com.mi.game.module.welfare.dao.WelfareLoginEntityDao;
import com.mi.game.module.welfare.dao.WelfareMonthEntityDao;
import com.mi.game.module.welfare.dao.WelfareOnlineEntityDao;
import com.mi.game.module.welfare.dao.WelfareRescueSunEntityDao;
import com.mi.game.module.welfare.dao.WelfareSignEntityDao;
import com.mi.game.module.worldBoss.dao.PlayerBossEntityDAO;
import com.mi.game.module.worldBoss.dao.TopTenListEntityDAO;
import com.mi.game.module.worldBoss.dao.WorldBossEntityDAO;
import com.mi.game.module.worldBoss.pojo.WorldBossEntity;

/***
 * 合并服务器模块 添加合并服务器的信息在game-server.properties 需要合并的服务器信息：
 * cache.mongo.mergeServerList = 0:10.10.61.32:root:U#N0q9CCuSA5V0 每个数据用":"分隔
 * 第一个为服务器ID, 第二个为ip地址, 第三个为用户名, 第四个为密码 ********注意：这次最新的实体为MainTaskEntity
 * 新加入的实体需要添加****** 需要清除的数据库: ArenaEntity MailBoxEntity PlayerBossEntity
 * TopTenListEntity WorldBossEntity LegionHistoryEntity battleReportEntity
 * arenaLuckyDAO 需要修改的数据库 playerEntity LegionEntity friendEntity
 * 
 * 
 * */

@Module(name = ModuleNames.MergeModule, clazz = MergeServerModule.class)
public class MergeServerModule extends BaseModule{
	private final ActLimitRewardMapDAO actLimitRewardMapDAO ;
	private final ArenaEntityDAO arenaEntityDAO ;
	private final ArenaShopEntityDAO arenaShopEntityDAO  ;
	private final BagEntityDAO bagEntityDAO;
	private final BattleReportEntityDAO battleReportEntityDAO ;
	private final DayTaskEntityDAO dayTaskEntityDAO ;
	private final DungeonActiveDAO dungeonActiveDAO ;
	private final DungeonActMapEntityDAO dungeonActMapEntityDAO;
	private final DungeonEliteEntityDAO dungeonEliteEntityDAO;
	private final DungeonMapEntityDAO dungeonMapEntityDAO;
	private final EquipmentEntityDAO equipmentEntityDAO;
	private final EventChickenEntityDao chickenEntityDao;
	private final EventDrawIntegralEntityDao drawIntegralEntityDao;
	private final EventDrawPayEntityDao drawPayEntityDao;
	private final EventExchangeEntityDao exchangeEntityDao;
	private final EventExpenseEntityDao expenseEntityDao;
	private final EventExploreEntityDao exploreEntityDao;
	private final EventPayEntityDao eventPayEntityDao;
	private final EventShopEntityDao eventShopEntityDao;
	private final EventTraderEntityDao eventTraderEntityDao;
	private final EventTimeLimitDao eventTimeLimitDao;
	private final FarmEntityDAO farmEntityDAO;
	private final FriendEntityDAO friendEntityDAO;
	private final HeroDrawDAO heroDrawDAO;
	private final HeroDAO heroDAO;
	private final HeroManualDAO heroManualDAO ;
	private final HeroSkinEntityDAO heroSkinEntityDAO;
	private final HeroTroopsDAO heroTroopsDAO;
	private final DestinyEntityDAO destinyEntityDAO;
	private final LegionEntityDAO legionEntityDAO;
	private final LegionHistoryEntityDAO legionHistoryEntityDAO;
	private final LegionMemberEntityDAO legionMemberEntityDAO;
	private final ArenaLuckyDAO arenaLuckyDAO;
	private final MailBoxEntityDAO mailBoxEntityDAO;
	private final NewPlayerEntityDAO newPlayerEntityDAO;
	private final PayEntityDao payEntityDao;
	private final PlayerBossEntityDAO playerBossEntityDAO;
	private final PlayerEffectDAO playerEffectDAO;
	private final PlayerEntitiyDAO playerEntitiyDAO;
	private final PlayerStatuesEntityDAO playerStatuesEntityDAO ;
	private final RewardDAO rewardDAO;
	private final TalismanMapDAO talismanMapDAO;
	private final TalismanShardDAO talismanShardDAO;
	private final TowerEntityDAO towerEntityDAO;
	private final TopTenListEntityDAO tenListEntityDAO;
	private final VipEnitiyDao vipEnitiyDao;
	private final VitatlyDAO vitatlyDAO;
	private final WalletDAO walletDAO;
	private final WorldBossEntityDAO worldBossEntityDAO;
	private final WelfareEntityDao welfareEntityDao;
	private final WelfareLevelEntityDao welfareLevelEntityDao;
	private final WelfareLoginEntityDao welfareLoginEntityDao;
	private final WelfareMonthEntityDao welfareMonthEntityDao;
	private final WelfareOnlineEntityDao welfareOnlineEntityDao;
	private final WelfareRescueSunEntityDao welfareRescueSunEntityDao;
	private final WelfareSignEntityDao welfareSignEntityDao;
	private final MainTaskEntityDAO mainTaskEntityDAO;
	private final PkRewardEntityDao pkRewardEntityDao;
	private final PkEntityDao pkEntityDao;
	private final PkExchangeHistoryEntityDao pkExchangeHistoryEntityDao;
	private final PetDAO petEntityDao;
	private final EventMonthCardDao eventMonthCardDao;
//	private final LegionModule legionModule;
//	private final FriendModule friendModule;
//	private final LoginModule loginModule;
//	private final ArenaModule arenaModule;
	private  LegionModule legionModule ;
	private  FriendModule friendModule;
	private  LoginModule loginModule;
	private  ArenaModule arenaModule;

	public void init(){
		legionModule = ModuleManager.getModule(ModuleNames.LegionModule,LegionModule.class);
		friendModule = ModuleManager.getModule(ModuleNames.FriendMoudle,FriendModule.class);
		 loginModule = ModuleManager.getModule(ModuleNames.LoginModule,LoginModule.class);
		 arenaModule = ModuleManager.getModule(ModuleNames.ArenaModule,ArenaModule.class);
	}
	
	public MergeServerModule(){
		arenaEntityDAO = ArenaEntityDAO.getInstance();
		arenaShopEntityDAO = ArenaShopEntityDAO.getInstance();
		bagEntityDAO = BagEntityDAO.getInstance();
		battleReportEntityDAO  = BattleReportEntityDAO.getInstance();
		dayTaskEntityDAO = DayTaskEntityDAO.getInstance();
		dungeonActiveDAO = DungeonActiveDAO.getInstance();
		dungeonActMapEntityDAO = DungeonActMapEntityDAO.getInstance();
		dungeonEliteEntityDAO = DungeonEliteEntityDAO.getInstance();
	    dungeonMapEntityDAO = DungeonMapEntityDAO.getInstance();
		playerEffectDAO = PlayerEffectDAO.getInstance();
		equipmentEntityDAO = EquipmentEntityDAO.getInstance();
		friendEntityDAO = FriendEntityDAO.getInstance();
		heroDAO = HeroDAO.getInstance();
	    heroTroopsDAO = HeroTroopsDAO.getInstance();
		heroDrawDAO = HeroDrawDAO.getInstance();
		destinyEntityDAO = DestinyEntityDAO.getInstance();
	    playerEntitiyDAO = PlayerEntitiyDAO.getInstance();
		newPlayerEntityDAO = NewPlayerEntityDAO.getInstance();
		playerStatuesEntityDAO = PlayerStatuesEntityDAO.getInstance();
		heroManualDAO = HeroManualDAO.getInstance();
		rewardDAO = RewardDAO.getInstance();
		talismanMapDAO = TalismanMapDAO.getInstance();
		talismanShardDAO = TalismanShardDAO.getInstance();
		towerEntityDAO = TowerEntityDAO.getInstance();
		vitatlyDAO = VitatlyDAO.getInstance();
		walletDAO = WalletDAO.getInstance();
		heroSkinEntityDAO = HeroSkinEntityDAO.getInstance();
		actLimitRewardMapDAO = ActLimitRewardMapDAO.getInstance();
		legionEntityDAO = LegionEntityDAO.getInstance();
		chickenEntityDao = EventChickenEntityDao.getInstance();
		drawIntegralEntityDao = EventDrawIntegralEntityDao.getInstance();
		drawPayEntityDao = EventDrawPayEntityDao.getInstance();
		exchangeEntityDao = EventExchangeEntityDao.getInstance();
		expenseEntityDao = EventExpenseEntityDao.getInstance();
		exploreEntityDao = EventExploreEntityDao.getInstance();
		eventPayEntityDao = EventPayEntityDao.getInstance();
		eventShopEntityDao = EventShopEntityDao.getInstance();
		eventTraderEntityDao = EventTraderEntityDao.getInstance();
		eventTimeLimitDao = EventTimeLimitDao.getInstance();
		vipEnitiyDao = VipEnitiyDao.getInstance();
		legionMemberEntityDAO = LegionMemberEntityDAO.getInstance();
		farmEntityDAO = FarmEntityDAO.getInstance();
		mailBoxEntityDAO = MailBoxEntityDAO.getInstance();
		playerBossEntityDAO = PlayerBossEntityDAO.getInstance();
		legionHistoryEntityDAO = LegionHistoryEntityDAO.getInstance();
		arenaLuckyDAO = ArenaLuckyDAO.getInstance();
		tenListEntityDAO = TopTenListEntityDAO.getInstance();
		worldBossEntityDAO = WorldBossEntityDAO.getInstance();
		welfareEntityDao = WelfareEntityDao.getInstance();
		welfareLevelEntityDao = WelfareLevelEntityDao.getInstance();
		welfareLoginEntityDao = WelfareLoginEntityDao.getInstance();
		welfareMonthEntityDao = WelfareMonthEntityDao.getInstance();
		welfareOnlineEntityDao = WelfareOnlineEntityDao.getInstance();
		welfareRescueSunEntityDao = WelfareRescueSunEntityDao.getInstance();
		welfareSignEntityDao = WelfareSignEntityDao.getInstance();
		payEntityDao = PayEntityDao.getInstance();
		mainTaskEntityDAO = MainTaskEntityDAO.getInstance();
		pkRewardEntityDao = PkRewardEntityDao.getInstance();
		pkEntityDao = PkEntityDao.getInstance();
		pkExchangeHistoryEntityDao = PkExchangeHistoryEntityDao.getInstance();
		petEntityDao=PetDAO.getInstance();
		eventMonthCardDao = EventMonthCardDao.getInstance();
	}
	
	private final static String serverID = ConfigUtil.getString("server.id");
	public void mergeServer(String mergeServerID){
		logger.error("开始合并数据");
		/**** 清理旧账号 ***/
		delOldAccount(serverID);
		delOldAccount(mergeServerID);
		/*** 清除旧信息 */
		delTables(serverID);
		delTables(mergeServerID);
		/** 清除好友赠送耐力 */
		this.delFriendPresentRecord(serverID);
		this.delFriendPresentRecord(mergeServerID);
		/** 清除玩家状态 */
		this.changePlayerStatus(serverID);
		this.changePlayerStatus(mergeServerID);
		/** 清除公会申请列表 */
		this.delLegionApply(serverID);
		this.delLegionApply(mergeServerID);
		/** 清除成员申请公会列表 */
		this.delMemberApply(serverID);
		this.delMemberApply(mergeServerID);
		/** 清除比武仇人记录,重置比武积分 */
		this.delPkLostList(serverID);
		this.delPkLostList(mergeServerID);
		/** 初始化机器人 */
		arenaModule.initArenaRobotList();
		/*** 合并数据 */
		this.mergePlayerEntity(mergeServerID);
		this.mergeDayTaskEntity(mergeServerID);
		this.mergeArenaShopEntity(mergeServerID);
		this.mergeBagEntity(mergeServerID);
		this.mergeHeroTroopsEntity(mergeServerID);
		this.mergeDungeonActMapEntity(mergeServerID);
		this.mergeDungeonActiveEntity(mergeServerID);
		this.mergeDungeonMapEntity(mergeServerID);
		this.mergeDungeonEliteEntity(mergeServerID);
		this.mergePlayerEffect(mergeServerID);
		this.mergeEquipmentEntity(mergeServerID);
		this.mergeFriendEntity(mergeServerID);
		this.mergeHeroEntity(mergeServerID);
		this.mergeHeroDrawEntity(mergeServerID);
		this.mergeDestinyEntity(mergeServerID);
		this.mergeNewPlayerEntity(mergeServerID);
		this.mergePlayerStatus(mergeServerID);
		this.mergeHeroManualEntity(mergeServerID);
		this.mergeRewardCenterEntity(mergeServerID);
		this.mergeTalisman(mergeServerID);
		this.mergeTalismanShard(mergeServerID);
		this.mergeTowerEntity(mergeServerID);
		this.mergeVitatly(mergeServerID);
		this.mergeWalletEntity(mergeServerID);
		this.mergeHeroSkinEntity(mergeServerID);
		this.mergeActLimitRewardMapEntity(mergeServerID);
		this.mergeVipEntity(mergeServerID);
		this.mergeFarmEntity(mergeServerID);
		this.mergeLegionMemberEntity(mergeServerID);
		this.mergeLegionEntity(mergeServerID);
		this.mergeWelfareEntity(mergeServerID);
		this.mergeWelfareLevelEntity(mergeServerID);
		this.mergeWelfareLoginEntity(mergeServerID);
		this.mergeWelfareMonthEntity(mergeServerID);
		this.mergeWelfareOnlineEntity(mergeServerID);
		this.mergeWelfareRescueSunEntity(mergeServerID);
		this.mergeWelfareSignEntity(mergeServerID);
		this.mergeWorldBossEntity(mergeServerID);
		this.mergePayEntity(mergeServerID);
		this.mergeMainTask(mergeServerID);
		this.mergePkReward(mergeServerID); // 比武荣誉值
		this.mergePet(mergeServerID); // 宠物
		this.mergePkEntity(mergeServerID);
		this.mergePkExchange(mergeServerID);
		this.mergeEventMonthCardEntity(mergeServerID);
		// 恢复2区军团数据
		// this.legionBackup(mergeServerID);
	}
	
	/**
	 * 恢复2区军团数据
	 * */
	private void legionBackup(String mergeServerID) {
		Map<String, List<String>> infoMap = new HashMap<String, List<String>>();
		long time = System.currentTimeMillis();
		long total = legionEntityDAO.queryCount(new QueryInfo(), mergeServerID);
		QueryInfo queryInfo = new QueryInfo(1, 100, "playerID");
		queryInfo.setTotal(total);
		queryInfo.initTotalPage();
		List<LegionEntity> legionList = null;
		while (queryInfo.getPage() <= queryInfo.getTotalPage()) {
			legionList = legionEntityDAO.queryPage(queryInfo, mergeServerID);
			if (legionList == null || legionList.isEmpty()) {
				break;
			}
			for (int i = 0; i < legionList.size(); i++) {
				LegionEntity legionEntity = legionList.get(i);
					int num = 10001 + i;
					legionEntity.setLegionID("2_" + num);
					String name = legionEntity.getName();
					LegionEntity searchEntity = legionEntityDAO
							.getLegionByName(name);
					if (searchEntity != null) {
						name += ".s" + mergeServerID;
						legionEntity.setName(name);
					}
				// 查询本工会所有会员id,放到map中
					List<String> memberList = legionEntity.getMembers();
				// 循环所有成员,从1区查询成员信息,修改军团id
					if (memberList != null && memberList.size() > 0) {
						for (String playerID : memberList) {
							PlayerEntity playerEntity = playerEntitiyDAO
									.getEntity(playerID, serverID);
							if (playerEntity != null) {
								playerEntity.setGroupID(legionEntity
										.getLegionID());
							playerEntitiyDAO.save(playerEntity, serverID);
							logger.debug("恢复公会数据成功,玩家+" + playerID + "工会ID改为 :"
									+ legionEntity.getLegionID() + "工会名称为："
									+ legionEntity.getName());
							}
						}
					}
			}
			queryInfo.setPage(queryInfo.getPage() + 1);
			legionEntityDAO.save(legionList, serverID);

		}
		logger.debug("恢复2区公会数据成功, 用时:" + (System.currentTimeMillis() - time)
				+ "毫秒");
	}

	/***
	 * 合并玩家活动购买月卡
	 * */
	private void mergeEventMonthCardEntity(String mergeServerID) {
		this.mergeFunction(mergeServerID, eventMonthCardDao, "活动月卡");
	}

	private void changePlayerStatus(String serverID){
		//long time = System.currentTimeMillis();
		long total = playerStatuesEntityDAO.queryCount(new QueryInfo(), serverID);
		QueryInfo queryInfo = new QueryInfo(1,100,"playerID");
		queryInfo.setTotal(total);
		queryInfo.initTotalPage();
		List<PlayerStatusEntity> statusList = null;
		while(queryInfo.getPage() <= queryInfo.getTotalPage()){
			statusList = playerStatuesEntityDAO.queryPage(queryInfo, serverID);
			if(statusList == null || statusList.isEmpty()){
				break;
			}
			for(int i = 0; i< statusList.size();i++){
				PlayerStatusEntity playerStatusEntity = statusList.get(i);
				playerStatusEntity.setFriend(0);
				playerStatusEntity.setInvite(0);
				playerStatusEntity.setNews(0);
			}
			queryInfo.setPage(queryInfo.getPage() + 1);
			playerStatuesEntityDAO.save(statusList,serverID);
		}
	}
	
	private void delLegionApply(String serverID){
		long time = System.currentTimeMillis();
		long total = legionEntityDAO.queryCount(new QueryInfo(), serverID);
		QueryInfo queryInfo = new QueryInfo(1,100,"playerID");
		queryInfo.setTotal(total);
		queryInfo.initTotalPage();
		List<LegionEntity> legionList = null;
		while(queryInfo.getPage() <= queryInfo.getTotalPage()){
			legionList = legionEntityDAO.queryPage(queryInfo,serverID);
			if(legionList == null || legionList.isEmpty()){
				break;
			}
			for(int i = 0; i < legionList.size(); i++){
				LegionEntity legionEntity = legionList.get(i);
				legionEntity.setApplys(null);
			}
			queryInfo.setPage(queryInfo.getPage() + 1);
			legionEntityDAO.save(legionList,serverID);
		}
		logger.error("删除" + serverID + "区的公会申请数据成功,用时"
				+ (System.currentTimeMillis() - time) + "毫秒");
	}

	// 清除成员申请工会列表
	private void delMemberApply(String serverID) {
		long time = System.currentTimeMillis();
		long total = legionMemberEntityDAO
				.queryCount(new QueryInfo(), serverID);
		QueryInfo queryInfo = new QueryInfo(1, 100, "playerID");
		queryInfo.setTotal(total);
		queryInfo.initTotalPage();
		List<LegionMemberEntity> legionList = null;
		while (queryInfo.getPage() <= queryInfo.getTotalPage()) {
			legionList = legionMemberEntityDAO.queryPage(queryInfo, serverID);
			if (legionList == null || legionList.isEmpty()) {
				break;
			}
			for (int i = 0; i < legionList.size(); i++) {
				LegionMemberEntity memberEntity = legionList.get(i);
				memberEntity.setApplyList(null);
			}
			queryInfo.setPage(queryInfo.getPage() + 1);
			legionMemberEntityDAO.save(legionList, serverID);
		}
		logger.error("删除" + serverID + "区的成员申请工会数据成功,用时"
				+ (System.currentTimeMillis() - time) + "毫秒");
	}
	
	private void delTables(String serverID){
		QueryInfo queryInfo = new QueryInfo();
		arenaEntityDAO.delQuery(queryInfo,serverID);
		battleReportEntityDAO.delQuery(queryInfo);
		mailBoxEntityDAO.delQuery(queryInfo,serverID);
		playerBossEntityDAO.delQuery(queryInfo,serverID);
		legionHistoryEntityDAO.delQuery(queryInfo,serverID);
		arenaLuckyDAO.delQuery(queryInfo,serverID);
		tenListEntityDAO.delQuery(queryInfo, serverID);
	}
	
	private void mergeWorldBossEntity(String mergeServerID){
		WorldBossEntity worldBossEntity1 = worldBossEntityDAO.getEntity(10761 + "" , serverID);
		WorldBossEntity worldBossEntity2 = worldBossEntityDAO.getEntity(10761+"" ,  mergeServerID);
		WorldBossEntity worldBossEntity3 = null;
		if(worldBossEntity2.getLevel() > worldBossEntity1.getLevel()){
			worldBossEntity3 = worldBossEntity2;
		}else{
			worldBossEntity3 = worldBossEntity1;
		}
 		if(worldBossEntity3 != null){
 			worldBossEntity3.setKillID(null);
 			worldBossEntity3.setKillName(null);
 			worldBossEntity3.setTopNameList(null);
			worldBossEntityDAO.save(worldBossEntity3);
		}
	}
	
	/**
	 * 合并玩家基础数据
	 * */
	private void mergePlayerEntity(String mergeServerID){
		long time = System.currentTimeMillis();
		long total = playerEntitiyDAO.getCount(mergeServerID);
		QueryInfo queryInfo = new QueryInfo(1,100,"playerID");
		queryInfo.setTotal(total);
		queryInfo.initTotalPage();
		List<PlayerEntity> playerList = null;
		while(queryInfo.getPage() <= queryInfo.getTotalPage()){
			playerList = playerEntitiyDAO.queryPage(queryInfo,mergeServerID);
			if(playerList == null || playerList.isEmpty()){
				break;
			}
			for(int i = 0; i < playerList.size(); i++){
				PlayerEntity playerEntity = playerList.get(i);
				String name = playerEntity.getNickName();
				PlayerEntity findPlayerEntity  = playerEntitiyDAO.getEntityByName(name);
				logger.error("查询姓名为" + name);
				if(findPlayerEntity != null){
					logger.error("相同姓名的ID"
							+ findPlayerEntity.getKey().toString());
					name += ".s" + mergeServerID;
					playerEntity.setNickName(name);
				}
			}
			queryInfo.setPage(queryInfo.getPage() + 1);
			playerEntitiyDAO.save(playerList);
		}
		logger.debug("合并玩家基础数据成功, 用时:" + (System.currentTimeMillis() - time)
				+ "毫秒");
	}
	
	/***
	 * 合并玩家竞技场商店数据
	 * */
	private void mergeArenaShopEntity(String mergeServerID){
		this.mergeFunction(mergeServerID, arenaShopEntityDAO, "竞技场");
	}
	
	/**
	 * 合并背包数据
	 * */
	private void mergeBagEntity(String mergeServerID){
		this.mergeFunction(mergeServerID, bagEntityDAO, "背包");
	}
	
	/**
	 * 合并玩家阵容
	 * */
	private void mergeHeroTroopsEntity(String mergeServerID){
		this.mergeFunction(mergeServerID, heroTroopsDAO, "阵容");
	}
	
	/**
	 * 合并玩家
	 * */
	
	
	/**
	 * 合并每日任务
	 * */
	private void mergeDayTaskEntity(String mergeServerID){
		this.mergeFunction(mergeServerID, dayTaskEntityDAO, "每日任务");
	}
	
	/**
	 * 合并大关卡数据
	 * */
	private void mergeDungeonActMapEntity(String mergeServerID){
		this.mergeFunction(mergeServerID, dungeonActMapEntityDAO, "大关卡");
	}
	
	/**
	 * 合并活动关卡数据
	 * */
	private void mergeDungeonActiveEntity(String mergeServerID){
		this.mergeFunction(mergeServerID, dungeonActiveDAO, "活动关卡");
	}
	
	/**
	 * 合并普通副本
	 * */
	private void mergeDungeonMapEntity(String mergeServerID){
		this.mergeFunction(mergeServerID, dungeonMapEntityDAO, "普通副本");
	}
	
	/**
	 * 合并精英副本
	 * */
	private void mergeDungeonEliteEntity(String mergeServerID){
		this.mergeFunction(mergeServerID, dungeonEliteEntityDAO, "精英副本");
	}
	
	/**
	 * 合并玩家buff
	 * */
	private void mergePlayerEffect(String mergeServerID){
		this.mergeFunction(mergeServerID, playerEffectDAO, "玩家BUFF");
	}
	
	/**
	 * 合并玩家的装备
	 */
	private void mergeEquipmentEntity(String mergeServerID){
		this.mergeFunction(mergeServerID, equipmentEntityDAO, "装备");
	}
	
	/**
	 * 合并玩家的好友
	 * */
	private void mergeFriendEntity(String mergeServerID){
		this.mergeFunction(mergeServerID, friendEntityDAO, "好友");
	}
	
	/**
	 * 合并玩家的英雄
	 * */
	private void mergeHeroEntity(String mergeServerID){
		this.mergeFunction(mergeServerID, heroDAO, "英雄");
	}
	
	/**
	 * 合并玩家的抽奖
	 * */
	private void mergeHeroDrawEntity (String mergeServerID){
		this.mergeFunction(mergeServerID, heroDrawDAO, "抽奖");
	}
	
	/**
	 * 合并玩家的天命
	 * */
	private void mergeDestinyEntity(String mergeServerID){
		this.mergeFunction(mergeServerID, destinyEntityDAO, "天命");
	}
	
	/***
	 * 合并新手引导
	 * */
	private void mergeNewPlayerEntity(String mergeServerID){
		this.mergeFunction(mergeServerID, newPlayerEntityDAO, "新手引导");
	}
	
	/**
	 * 合并玩家状态
	 * */
	private void mergePlayerStatus(String mergeServerID){
		this.mergeFunction(mergeServerID, playerStatuesEntityDAO, "状态");
	}
	
	/**
	 * 合并玩家仙班
	 * */
	private void mergeHeroManualEntity(String mergeServerID){
		this.mergeFunction(mergeServerID, heroManualDAO, "仙班");
	}
	
	/**
	 * 合并玩家领奖中心
	 * */
	private void mergeRewardCenterEntity(String mergeServerID){
		this.mergeFunction(mergeServerID, rewardDAO, "领奖中心");
	}
	
	/**
	 * 合并玩家的宝物
	 * */
	private void mergeTalisman(String mergeServerID){
		this.mergeFunction(mergeServerID, talismanMapDAO, "宝物");
	}
	
	/**
	 * 合并玩家的宝物碎片
	 * */
	private void mergeTalismanShard(String mergeServerID){
		this.mergeFunction(mergeServerID, talismanShardDAO, "宝物碎片");
	}
	
	/**
	 * 合并试炼塔
	 * */
	private void mergeTowerEntity(String mergeServerID){
		this.mergeFunction(mergeServerID, towerEntityDAO, "试炼塔");
	}
	
	/**
	 * 合并体力耐力
	 * */
	private void mergeVitatly(String mergeServerID){
		this.mergeFunction(mergeServerID, vitatlyDAO, "体力耐力");
	}
	
	/**
	 * 合并玩家钱包
	 * */
	private void mergeWalletEntity(String mergeServerID){
		this.mergeFunction(mergeServerID, walletDAO, "钱包");
	}
	
	/**
	 * 合并英雄皮肤
	 * */
	private void mergeHeroSkinEntity(String mergeServerID){
		this.mergeFunction(mergeServerID, heroSkinEntityDAO, "皮肤");
	}
	
	/**
	 * 合并限时礼包
	 * */
	private void mergeActLimitRewardMapEntity(String mergeServerID){
		this.mergeFunction(mergeServerID, actLimitRewardMapDAO, "大关卡限时礼包");
	}
	
	/**
	 * 合并vip
	 * */
	private void mergeVipEntity(String mergeServerID){
		this.mergeFunction(mergeServerID, vipEnitiyDao, "vip");
	}
	
	/**
	 * 合并挂机
	 * */
	private void mergeFarmEntity(String mergeServerID){
		this.mergeFunction(mergeServerID, farmEntityDAO, "挂机");
	}
	
	/**
	 * 军团成员
	 * */
	private void mergeLegionMemberEntity(String mergeServerID){
		this.mergeFunction(mergeServerID, legionMemberEntityDAO, "军团成员");
	}
	
	/**
	 * 福利活动集合
	 * */
	private void mergeWelfareEntity(String mergeServerID){
		this.mergeFunction(mergeServerID, welfareEntityDao, "福利活动集合");
	}
	
	/**
	 * 等级礼包
	 * */
	private void mergeWelfareLevelEntity (String mergeServerID){
		this.mergeFunction(mergeServerID, welfareLevelEntityDao, "等级礼包");
	}
	
	/**
	 * 登陆礼包
	 * */
	private void mergeWelfareLoginEntity (String mergeServerID){
		this.mergeFunction(mergeServerID, welfareLoginEntityDao, "登陆礼包");
	}
	
	/**
	 * 月卡
	 * */
	private void mergeWelfareMonthEntity (String mergeServerID){
		this.mergeFunction(mergeServerID, welfareMonthEntityDao, "月卡");
	}
	
	/**
	 * 在线奖励
	 * */
	private void mergeWelfareOnlineEntity(String mergeServerID){
		this.mergeFunction(mergeServerID, welfareOnlineEntityDao, "在线奖励");
	}
	
	/**
	 * 解救悟空
	 * */
	private void mergeWelfareRescueSunEntity(String mergeServerID){
		this.mergeFunction(mergeServerID, welfareRescueSunEntityDao, "解救悟空");
	}
	
	/**
	 * 签到活动
	 * */
	private void mergeWelfareSignEntity(String mergeServerID){
		this.mergeFunction(mergeServerID, welfareSignEntityDao, "签到");
	}
	
	/**
	 * 合并充值
	 * */
	private void mergePayEntity(String mergeServerID){
		this.mergeFunction(mergeServerID, payEntityDao, "充值");
	}
	
	
	/**
	 * 合并主线任务
	 * */
	private void mergeMainTask(String mergeServerID) {
		this.mergeFunction(mergeServerID, mainTaskEntityDAO, "主线任务");
	}

	/**
	 * 合并比武历史
	 * */
	private void mergePkEntity(String mergeServerID) {
		this.mergeFunction(mergeServerID, pkEntityDao, "比武历史积分");
	}

	/**
	 * 合并比武兑换记录
	 * */
	private void mergePkExchange(String mergeServerID) {
		this.mergeFunction(mergeServerID, pkExchangeHistoryEntityDao, "比武兑换记录");
	}

	/**
	 * 合并比武荣誉值
	 * */
	private void mergePkReward(String mergeServerID) {
		this.mergeFunction(mergeServerID, pkRewardEntityDao, "比武荣誉值");
	}
	
	/**
	 * 合并宠物
	 * */
	private void mergePet(String mergeServerID) {
		this.mergeFunction(mergeServerID, petEntityDao, "宠物");
	}

	/**
	 * 比武删除仇人,重置积分
	 * 
	 * @param serverID
	 */
	private void delPkLostList(String serverID) {
		long time = System.currentTimeMillis();
		long total = pkEntityDao.queryCount(new QueryInfo(), serverID);
		QueryInfo queryInfo = new QueryInfo(1, 100, "playerID");
		queryInfo.setTotal(total);
		queryInfo.initTotalPage();
		List<PkEntity> pkList = null;
		while (queryInfo.getPage() <= queryInfo.getTotalPage()) {
			pkList = pkEntityDao.queryPage(queryInfo, serverID);
			if (pkList == null || pkList.isEmpty()) {
				break;
			}
			for (int i = 0; i < pkList.size(); i++) {
				PkEntity pkEntity = pkList.get(i);
				pkEntity.setLostTo(null);
				pkEntity.setPoints(1000);
			}
			queryInfo.setPage(queryInfo.getPage() + 1);
			playerEntitiyDAO.save(pkList, serverID);
		}
		logger.debug("删除" + serverID + "比武仇人,重置比武积分成功, 用时:"
				+ (System.currentTimeMillis() - time) + "毫秒");
	}
	
	/**
	 * 合并军团
	 * */
	private void mergeLegionEntity(String mergeServerID){
		long time = System.currentTimeMillis();
		long total  = legionEntityDAO.queryCount(new QueryInfo(), mergeServerID);
		QueryInfo queryInfo = new QueryInfo(1,100,"playerID");
		queryInfo.setTotal(total);
		queryInfo.initTotalPage();
		List<LegionEntity> legionList = null;
		while(queryInfo.getPage() <= queryInfo.getTotalPage()){
			legionList = legionEntityDAO.queryPage(queryInfo,mergeServerID);
			if(legionList == null || legionList.isEmpty()){
				break;
			}
			for(int i = 0; i < legionList.size(); i++){
				LegionEntity legionEntity = legionList.get(i);
				String name = legionEntity.getName();
				LegionEntity searchEntity = legionEntityDAO.getLegionByName(name);
				if(searchEntity != null){
					name += ".s" + mergeServerID;
					legionEntity.setName(name);
				}
			}
			queryInfo.setPage(queryInfo.getPage() + 1);
			legionEntityDAO.save(legionList,serverID);
		}
		logger.debug("合并公会数据成功, 用时:" + (System.currentTimeMillis() - time)
				+ "毫秒");
	}
	
	private void delFriendPresentRecord(String serverID){
		long time = System.currentTimeMillis();
		long total  = friendEntityDAO.queryCount(new QueryInfo(), serverID);
		QueryInfo queryInfo = new QueryInfo(1,100,"playerID");
		queryInfo.setTotal(total);
		queryInfo.initTotalPage();
		List<FriendEntity> friendList = null;
		while(queryInfo.getPage() <= queryInfo.getTotalPage()){
			friendList = friendEntityDAO.queryPage(queryInfo,serverID);
			if(friendList == null || friendList.isEmpty()){
				break;
			}
			for(int i = 0; i < friendList.size();i++){
				FriendEntity friendEntity = friendList.get(i);
				friendEntity.setPresentList(null);
			}
			queryInfo.setPage(queryInfo.getPage() + 1);
			playerEntitiyDAO.save(friendList,serverID);
		}
		logger.debug("删除" + serverID + "区好友赠送耐力成功, 用时:"
				+ (System.currentTimeMillis() - time) + "毫秒");
	}
	
	/**
	 * 删除不符合条件的账号
	 * */
	public void delOldAccount(String serverID){
		long time = System.currentTimeMillis();
		long total = total = playerEntitiyDAO.getCount(serverID);
		QueryInfo queryInfo = new QueryInfo(1,100,"playerID");
		queryInfo.setTotal(total);
		queryInfo.initTotalPage();
		List<PlayerEntity> playerList = null;
		List<String> removePlayers = new ArrayList<>();
		PayModule payModule = ModuleManager.getModule(ModuleNames.PayModule,PayModule.class);
		while(queryInfo.getPage() <= queryInfo.getTotalPage()){
			playerList = playerEntitiyDAO.queryPage(queryInfo,serverID);
			if(playerList == null || playerList.isEmpty()){
				break;
			}
			for(int i = 0; i < playerList.size(); i++){
				PlayerEntity playerEntity = playerList.get(i);
				String playerID = playerEntity.getKey().toString();
				int level = playerEntity.getLevel();
				
				if (level <= MergeServerDefine.minLevel) {
					PayEntity payEntity = payModule.getPayEntity(playerID);
					if(payEntity != null  && payEntity.getPayTotal() <= 0 ){
						long offLineTime = playerEntity.getOffLineTime();
						long nowTime = System.currentTimeMillis();
						if((nowTime - offLineTime) > MergeServerDefine.levelDay){
							String groupID = playerEntity.getGroupID();
							if(!groupID.equals("0")){
								LegionEntity legionEntity  = legionModule.getLegionEntity(groupID,serverID);
								String legatus = legionEntity.getLegatus();
								List<String> members = legionEntity.getMembers();
								if(!legatus.equals(playerID)){
									removePlayers.add(playerID);
									if(members.contains(playerID)){
										members.remove(playerID);
									}
									legionEntityDAO.save(legionEntity,serverID);
								}
								if(legatus.equals(playerID) ){
									if(members.size() == 1 && members.contains(playerID)){
										removePlayers.add(playerID);
										legionEntity.setMembers(new ArrayList<String>());
										legionEntity.setKill(true);
										legionEntityDAO.save(legionEntity,serverID);
									}
								}
							}else{
								removePlayers.add(playerID);
							}
						}
					}
				}
			}
			queryInfo.setPage(queryInfo.getPage() + 1);
			playerEntitiyDAO.save(playerList,serverID);
		}
		for(String delID : removePlayers){
			this.delAccount(delID,serverID);
		}
		logger.debug("删除" + serverID + "区玩家基础数据成功, 用时:"
				+ (System.currentTimeMillis() - time) + "毫秒");
	}

	private void delAccount(String playerID,String serverID){
		QueryInfo queryInfo = new QueryInfo();
		QueryBean queryBean = new QueryBean("playerID", QueryType.EQUAL, playerID);
		queryInfo.addQueryBean(queryBean);
		this.delFriend(playerID,serverID);
		this.delLegion(playerID,serverID);
		actLimitRewardMapDAO.delQuery(queryInfo, serverID);
		//analyEntityDAO.delQuery(queryInfo,serverID);
		arenaShopEntityDAO.delQuery(queryInfo,serverID);
		bagEntityDAO.delQuery(queryInfo,serverID);
		dayTaskEntityDAO.delQuery(queryInfo,serverID);
		dungeonActiveDAO.delQuery(queryInfo,serverID);
		dungeonActMapEntityDAO.delQuery(queryInfo,serverID);
		dungeonEliteEntityDAO.delQuery(queryInfo, serverID);
		dungeonMapEntityDAO.delQuery(queryInfo, serverID);
		equipmentEntityDAO.delQuery(queryInfo, serverID);
		chickenEntityDao.delQuery(queryInfo, serverID);
		drawIntegralEntityDao.delQuery(queryInfo, serverID);
		drawPayEntityDao.delQuery(queryInfo, serverID);
		exchangeEntityDao.delQuery(queryInfo, serverID);
		expenseEntityDao.delQuery(queryInfo, serverID);
		exploreEntityDao.delQuery(queryInfo, serverID);
		eventPayEntityDao.delQuery(queryInfo,serverID);
		eventShopEntityDao.delQuery(queryInfo,serverID);
		eventTimeLimitDao.delQuery(queryInfo,serverID);
		eventTraderEntityDao.delQuery(queryInfo, serverID);
		farmEntityDAO.delQuery(queryInfo,serverID);
		heroDrawDAO.delQuery(queryInfo,serverID);
		heroDAO.delQuery(queryInfo,serverID);
		heroManualDAO.delQuery(queryInfo,serverID);
		heroSkinEntityDAO.delQuery(queryInfo,serverID);
		heroTroopsDAO.delQuery(queryInfo,serverID);
		destinyEntityDAO.delQuery(queryInfo,serverID);
		legionMemberEntityDAO.delQuery(queryInfo,serverID);
		newPlayerEntityDAO.delQuery(queryInfo,serverID);
		playerEffectDAO.delQuery(queryInfo,serverID);
		playerEntitiyDAO.delQuery(queryInfo,serverID);
		playerStatuesEntityDAO.delQuery(queryInfo,serverID);
		rewardDAO.delQuery(queryInfo,serverID);
		talismanMapDAO.delQuery(queryInfo,serverID);
	
		talismanShardDAO.delQuery(queryInfo,serverID);
		towerEntityDAO.delQuery(queryInfo,serverID);
		vipEnitiyDao.delQuery(queryInfo,serverID);
		vitatlyDAO.delQuery(queryInfo,serverID);
		walletDAO.delQuery(queryInfo,serverID);

		pkEntityDao.delQuery(queryInfo, serverID);
		pkExchangeHistoryEntityDao.delQuery(queryInfo, serverID);
		pkRewardEntityDao.delQuery(queryInfo, serverID);
		petEntityDao.delQuery(queryInfo, serverID);
		logger.debug("删除用户" + playerID);
	}
	
	private void delFriend(String playerID,String serverID){
		FriendEntity friendEntity = friendModule.getFriendEntity(playerID,serverID);
		Map<String,FriendState> friendMap= friendEntity.getFriendList();
		Set<String> keySet = friendMap.keySet();
		for(String friendID : keySet){
			FriendEntity tempEntity = friendModule.getFriendEntity(friendID,serverID);
			Map<String, FriendState> tempMap = friendEntity.getFriendList();
			if (tempMap.containsKey(playerID)) {
				tempMap.remove(playerID);
				friendEntityDAO.save(tempEntity,serverID);
			}
		}
		friendEntityDAO.del(playerID);
	}
	
	private void delLegion(String playerID,String serverID){

		PlayerEntity playerEntity = loginModule.getPlayerEntity(playerID,serverID);
		String legionID = playerEntity.getGroupID();
		if(!legionID.equals("0")){
			LegionEntity legionEntity = legionModule.getLegionEntity(legionID,serverID);
			if(legionEntity != null){
				List<String> members = legionEntity.getMembers();
				if(members.contains(playerID)){
					logger.error("公会移除用户 :" + playerID);
					members.remove(playerID);
					legionEntityDAO.save(legionEntity,serverID);
				}
			}
		}
	}
	
	
	
	/**
	 * 合并公用方法
	 * */
	public <A extends BaseEntity> void mergeFunction(String mergeServerID, AbstractBaseDAO<A> dao,String debugInfo){
		long time = System.currentTimeMillis();
		long total  = dao.queryCount(new QueryInfo(), mergeServerID);
		QueryInfo queryInfo = new QueryInfo(1,100,"playerID");
		queryInfo.setTotal(total);
		queryInfo.initTotalPage();
		List<A> bagList = null;
		while (queryInfo.getPage() <= queryInfo.getTotalPage()) {
			bagList = dao.queryPage(queryInfo, mergeServerID);
			if (bagList == null || bagList.isEmpty()) {
				break;
			}
			queryInfo.setPage(queryInfo.getPage() + 1);
			dao.save(bagList);
		}
		logger.debug(
				"合并玩家" + debugInfo + "数据成功, 用时:"
						+ (System.currentTimeMillis() - time) + "毫秒", "共合并"
						+ total + "条数据");
	}
	
	/**
	 * 合并竞技场数据
	 * */
	
//	private void mergeArenaEntiy(String mergeServerID){
//		long total  = arenaEntityDAO.getCount(mergeServerID);
//		QueryInfo queryInfo = new QueryInfo(1,100);
//		queryInfo.setTotal(total);
//		queryInfo.initTotalPage();
//		List<ArenaEntity> arenaList = null;
	// // 分页查询数据
//		while (queryInfo.getPage() <= queryInfo.getTotalPage()) {
//			arenaList = arenaEntityDAO.queryPage(queryInfo,mergeServerID);
//			if (arenaList == null || arenaList.isEmpty()) {
//				break;
//			}
//			for (int i = 0; i < arenaList.size(); i++) {
//				ArenaEntity arenaEntity = arenaList.get(i);
//				String key = arenaEntity.getKey().toString();
//				if(!Utilities.isNpc(key)){
//					try{
//						arenaEntity.setRank(1001);
//						arenaEntityDAO.save(arenaEntity);
//					}catch(Exception ex){
//						logger.error(ex.getMessage());
//					}
//				}
//			}
//			queryInfo.setPage(queryInfo.getPage() + 1);
//		}
	// logger.debug("合并竞技场成功");
//	}
}
