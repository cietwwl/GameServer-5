package com.mi.game.module.login;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.Date;

import com.mi.core.dao.KeyGeneratorDAO;
import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.TemplateManager;
import com.mi.core.engine.annotation.Module;
import com.mi.core.pojo.BaseEntity;
import com.mi.core.pojo.KeyGenerator;
import com.mi.core.protocol.BaseProtocol;
import com.mi.core.template.BaseTemplate;
import com.mi.core.util.ClassScanUtil;
import com.mi.core.util.ConfigUtil;
import com.mi.core.util.DateTimeUtil;
import com.mi.core.util.RegExpValidatorUtil;
import com.mi.core.util.XmlTemplateUtil;
import com.mi.game.defines.ErrorIds;
import com.mi.game.defines.KindIDs;
import com.mi.game.defines.ModuleNames;
import com.mi.game.defines.RewardType;
import com.mi.game.defines.SysConstants;
import com.mi.game.module.admin.handler.TemplateManageHandler;
import com.mi.game.module.analyse.AnalyseModule;
import com.mi.game.module.analyse.pojo.AnalyEntity;
import com.mi.game.module.arena.ArenaModule;
import com.mi.game.module.arena.pojo.ArenaShopEntity;
import com.mi.game.module.arena.pojo.LuckRankMapEntity;
import com.mi.game.module.bag.BagModule;
import com.mi.game.module.bag.pojo.BagEntity;
import com.mi.game.module.bag.pojo.BagItem;
import com.mi.game.module.base.BaseModule;
import com.mi.game.module.base.bean.init.ConfigVersionData;
import com.mi.game.module.base.bean.init.server.ServerInfoData;
import com.mi.game.module.dungeon.DungeonModule;
import com.mi.game.module.dungeon.pojo.ActLimitRewardMapEntity;
import com.mi.game.module.dungeon.pojo.DungeonActMapEntity;
import com.mi.game.module.dungeon.pojo.DungeonActiveEntity;
import com.mi.game.module.dungeon.pojo.DungeonEliteEntity;
import com.mi.game.module.dungeon.pojo.DungeonMapEntity;
import com.mi.game.module.effect.EffectModule;
import com.mi.game.module.effect.pojo.PlayerEffectEntity;
import com.mi.game.module.equipment.EquipmentModule;
import com.mi.game.module.equipment.pojo.EquipmentMapEntity;
import com.mi.game.module.farm.FarmModule;
import com.mi.game.module.farm.pojo.FarmEntity;
import com.mi.game.module.hero.HeroModule;
import com.mi.game.module.hero.pojo.HeroEntity;
import com.mi.game.module.hero.pojo.HeroTroopsEntity;
import com.mi.game.module.heroDraw.HeroDrawModule;
import com.mi.game.module.heroDraw.pojo.HeroDrawEntity;
import com.mi.game.module.lead.LeadModule;
import com.mi.game.module.lead.pojo.LeadDesitnyEntity;
import com.mi.game.module.login.dao.LoginInfoDAO;
import com.mi.game.module.login.dao.MinuEntityDao;
import com.mi.game.module.login.dao.NewPlayerEntityDAO;
import com.mi.game.module.login.dao.PlayerEntitiyDAO;
import com.mi.game.module.login.dao.PlayerStatuesEntityDAO;
import com.mi.game.module.login.dao.ServerInfoDAO;
import com.mi.game.module.login.dao.TestUserRewardEntityDao;
import com.mi.game.module.login.pojo.LoginInfoEntity;
import com.mi.game.module.login.pojo.MinuEntity;
import com.mi.game.module.login.pojo.NewPlayerEntity;
import com.mi.game.module.login.pojo.PlayerEntity;
import com.mi.game.module.login.pojo.PlayerStatusEntity;
import com.mi.game.module.login.pojo.ServerInfoEntity;
import com.mi.game.module.login.pojo.TestUserRewardEntity;
import com.mi.game.module.login.protocol.LoginInfoProtocol;
import com.mi.game.module.login.task.RandomNameTask;
import com.mi.game.module.mailBox.MailBoxModule;
import com.mi.game.module.manual.ManualModule;
import com.mi.game.module.manual.pojo.HeroManualsEntity;
import com.mi.game.module.pay.PayModule;
import com.mi.game.module.pet.PetModule;
import com.mi.game.module.pet.pojo.PetEntity;
import com.mi.game.module.reward.RewardModule;
import com.mi.game.module.reward.data.GoodsBean;
import com.mi.game.module.reward.pojo.RewardCenterEntity;
import com.mi.game.module.talisman.TalismanModule;
import com.mi.game.module.talisman.pojo.TalismanMapEntity;
import com.mi.game.module.talisman.pojo.TalismanShard;
import com.mi.game.module.tower.TowerModule;
import com.mi.game.module.tower.pojo.TowerEntity;
import com.mi.game.module.vitatly.VitatlyModule;
import com.mi.game.module.vitatly.pojo.VitatlyEntity;
import com.mi.game.module.wallet.WalletModule;
import com.mi.game.module.wallet.pojo.WalletEntity;
import com.mi.game.util.CommonMethod;
import com.mi.game.util.MD5FileUtil;
import com.mi.game.util.TestUserSendRewardUtil;
import com.mi.game.util.Utilities;

/**
 * 用户模块逻辑处理
 * */
@Module(name = ModuleNames.LoginModule, clazz = LoginModule.class)
public class LoginModule extends BaseModule {
	private LoginInfoDAO loginInfoDAO = LoginInfoDAO.getInstance();
	private static MinuEntityDao minuEntityDao = MinuEntityDao.getInstance();
	private static TestUserRewardEntityDao testUserRewardEntityDao = TestUserRewardEntityDao.getInstance();
	private ServerInfoDAO serverInfoDAO = ServerInfoDAO.getInstance();
	private PlayerEntitiyDAO playerEntitiyDAO = PlayerEntitiyDAO.getInstance();
	private PlayerStatuesEntityDAO playerStatuesEntityDAO = PlayerStatuesEntityDAO.getInstance();
	private NewPlayerEntityDAO newPlayerEntityDAO = NewPlayerEntityDAO.getInstance();
	private KeyGeneratorDAO keyGeneratorDAO = KeyGeneratorDAO.getInstance();
	private static int status; // 全区停服状态
	private static String stopServerMessage;
	private static String serverMd5;
	public static String[] openPlayerIDList = ConfigUtil.getStringArray("openPlayerID");
	/** 配置列表缓存 **/
	public static Map<String, ConfigVersionData> configVersionData = new HashMap<String, ConfigVersionData>();
	/** 服务器列表信息 */
	public static List<ServerInfoEntity> ServerInfoList = null;
	/** 随机男名字集合 */
	private static List<String> maleRandomName = new ArrayList<String>();
	/** 随机女名字集合 */
	private static List<String> femaleRandomName = new ArrayList<String>();

	public String getStopServerMessage() {
		if (stopServerMessage == null || stopServerMessage.isEmpty()) {
			stopServerMessage = "停机维护";
		}
		return stopServerMessage;
	}

	public void setStopServerMessage(String stopServerMessage) {
		LoginModule.stopServerMessage = stopServerMessage;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		LoginModule.status = status;
	}

	public static String getServerMd5() {
		return serverMd5;
	}

	public static void setServerMd5(String serverMd5) {
		LoginModule.serverMd5 = serverMd5;
	}

	@Override
	public void init() {
		if (logger.isInfoEnabled())
			logger.info("LoginModule loadder");
		initAttr();
		initServerInfoList();
		initRandomNameInfo();
		initVisitorID();
		initServerInfoFileMd5();
		initConfigVersion();

		// 初始化测试用户列表
		TestUserSendRewardUtil.init();
	}
	
	private void initServerInfoFileMd5() {
		String md5 = getServerMd5String();
		setServerMd5(md5);
	}

	private void initRandomNameInfo() {
		Timer timer = new Timer();
		RandomNameTask task = new RandomNameTask();
		timer.schedule(task, 100);
	}

	public void setMaleRandomName(List<String> maleRandomName) {
		LoginModule.maleRandomName = maleRandomName;
	}

	public void setFeManleRandomName(List<String> femaleRandomName) {
		LoginModule.femaleRandomName = femaleRandomName;
	}

	/**
	 * 初始化服务器信息列表
	 */
	public void initServerInfoList() {
		// ServerInfoList = null;
		// if (ServerInfoList == null || ServerInfoList.isEmpty()) {
		ServerInfoList = new ArrayList<ServerInfoEntity>();
		List<ServerInfoData> data = TemplateManager.getTemplateList(ServerInfoData.class);
		for (ServerInfoData serverInfo : data) {
			ServerInfoEntity serverInfoEntity = new ServerInfoEntity(serverInfo.getServerID(), serverInfo.getServerName(), serverInfo.getUrl(), serverInfo.getStatus(),
					serverInfo.getRecommend(), serverInfo.getMessage());
			ServerInfoList.add(serverInfoEntity);
		}
		serverInfoDAO.save(ServerInfoList);
		// }
	}

	private void initAttr() {

	}

	private void initConfigVersion() {
		List<ConfigVersionData> dataList = TemplateManager.getTemplateList(ConfigVersionData.class);
		for (ConfigVersionData data : dataList) {
			configVersionData.put(data.getName(), data);
		}
	}

	/**
	 * 用户注册
	 * */
	public int registerUser(String playerName, String passwd, String email, boolean sign) {
		if (playerName == null || playerName.isEmpty()) {
			throw new IllegalArgumentException(ErrorIds.NameLength + "");
		}
		if (sign) {
			LoginInfoEntity loginInfoEntity = new LoginInfoEntity();
			loginInfoEntity.setPassword(passwd);
			loginInfoEntity.setPlayerName(playerName);
			// 记录注册时间
			loginInfoEntity.setRegisterTime(new Date().getTime());
			loginInfoDAO.save(loginInfoEntity);
			if (logger.isDebugEnabled()) {
				logger.debug(playerName + "注册成功");
			}
			return 0;
		}
		int canRegister = checkUser(playerName, passwd, email);
		if (canRegister == 0) {
			LoginInfoEntity loginInfoEntity = new LoginInfoEntity();
			loginInfoEntity.setPassword(passwd);
			loginInfoEntity.setPlayerName(playerName);
			// 记录注册时间
			loginInfoEntity.setRegisterTime(new Date().getTime());
			loginInfoDAO.save(loginInfoEntity);
			if (logger.isDebugEnabled()) {
				logger.debug(playerName + "注册成功");
			}
		}
		return canRegister;
	}

	/**
	 * 检测注册合法性
	 * */
	private int checkUser(String playerName, String passwd, String email) {
		// 检验用户名的合法性
		if (!Utilities.IsUserName(playerName)) {
			return ErrorIds.UserNameIllegal;
		}
		// 检验密码的合法性
		if (!Utilities.IsUserName(passwd)) {
			return ErrorIds.PasswdIllegal;
		}

		if (!email.isEmpty()) {
			if (!RegExpValidatorUtil.isEmail(email)) {
				logger.error("邮箱格式错误");
				return ErrorIds.EmailFormatWrong;
			}
			LoginInfoEntity loginInfoEntity = loginInfoDAO.getEntityByEmail(email);
			if (loginInfoEntity != null) {
				logger.error("邮箱已被注册");
				return ErrorIds.EamilRegistered;
			}
		}

		// 查看是否注册
		LoginInfoEntity loginInfoEntity = loginInfoDAO.getEntityByName(playerName);
		if (loginInfoEntity != null) {
			return ErrorIds.UserRegistered;
		}
		return 0;
	}

	/**
	 * 获取服务器列表
	 * */
	public List<ServerInfoEntity> getServerList() {
		return ServerInfoList;
	}

	public void saveNewPlayerEntity(NewPlayerEntity entity) {
		newPlayerEntityDAO.save(entity);
	}

	public NewPlayerEntity getNewPlayerEntity(String playerID) {
		NewPlayerEntity entity = newPlayerEntityDAO.getEntity(playerID);
		if (entity == null) {
			entity = this.initNewPlayerEntity(playerID);
			this.saveNewPlayerEntity(entity);
		}
		return entity;
	}

	public NewPlayerEntity getNewPlayerEntity(String playerID, IOMessage ioMessage) {
		NewPlayerEntity newPlayerEntity = null;
		if (ioMessage != null) {
			String key = NewPlayerEntity.class.getName() + playerID;
			newPlayerEntity = (NewPlayerEntity) ioMessage.getInputParse(key);
			if (newPlayerEntity == null) {
				newPlayerEntity = this.getNewPlayerEntity(playerID);
				ioMessage.getInputParse().put(key, newPlayerEntity);
			}
		} else {
			newPlayerEntity = this.getNewPlayerEntity(playerID);
		}
		return newPlayerEntity;
	}

	private NewPlayerEntity initNewPlayerEntity(String playerID) {
		NewPlayerEntity entity = new NewPlayerEntity();
		entity.setKey(playerID);
		entity.setFinality(false);
		entity.setProperId(10741);
		entity.setTriggerId("1013901");
		entity.setGameLevelID(10101);
		return entity;
	}

	public PlayerEntity getPlayerEntityByName(String name) {
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException(ErrorIds.PasswdWrong + "");
		}
		return playerEntitiyDAO.getEntityByName(name);
	}

	public List<PlayerEntity> getPlayerEntityLikeName(String name) {
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException(ErrorIds.SearchNameWrong + "");
		}
		return playerEntitiyDAO.getLikeNameList(name);
	}

	/**
	 * 用户密码登录
	 * */
	public boolean chechkUserPasswd(String playerName, String passwd, BaseProtocol protocol) {
		LoginInfoEntity loginInfoEntity = loginInfoDAO.getEntityByName(playerName);
		if (loginInfoEntity == null) {
			logger.error("账号不存在");
			throw new IllegalArgumentException(ErrorIds.ACCOUNTNOTEXITS + "");
		}
		if (!loginInfoEntity.getPassword().equals(passwd)) {
			logger.error("用户名密码错误");
			throw new IllegalArgumentException(ErrorIds.PasswdWrong + "");
		}
		loginInfoEntity.setLastLoginTime(new Date().getTime());
		loginInfoDAO.save(loginInfoEntity);
		protocol.setPlayerID(loginInfoEntity.getKey());
		return true;
	}

	/**
	 * 三方平台帐号登录
	 * */
	public boolean chechkUserPasswd(String playerName, String passwd, BaseProtocol protocol, boolean sign) {
		if (sign) {
			LoginInfoEntity loginInfoEntity = loginInfoDAO.getEntityByName(playerName);
			if (loginInfoEntity == null) {
				return false;
			}
			if (!loginInfoEntity.getPassword().equals(passwd)) {
				return false;
			}
			loginInfoDAO.save(loginInfoEntity);
			protocol.setPlayerID(loginInfoEntity.getKey());
		}
		return true;
	}

	/**
	 * 获取用户信息
	 * */
	public PlayerEntity getPlayerEntity(String playerID) {
		PlayerEntity playerEntity = playerEntitiyDAO.getEntity(playerID);
		if (playerEntity == null) {
			logger.error("用户实体为空" + playerID);
			throw new IllegalArgumentException(ErrorIds.NoEntity + "");
		}
		return playerEntity;
	}

	public PlayerEntity getPlayerEntityNoError(String playerID) {
		PlayerEntity playerEntity = playerEntitiyDAO.getEntity(playerID);
		return playerEntity;
	}

	/** 获取更新的用户信息 */
	public PlayerEntity getUpdatePlayerEntity(String playerID, IOMessage ioMessage) {
		long nowTime = System.currentTimeMillis();
		PlayerEntity playerEntity = this.getPlayerEntity(playerID);
		long updareTime = playerEntity.getUpdateTime();

		HeroModule heroModule = ModuleManager.getModule(ModuleNames.HeroModule, HeroModule.class);
		int fightValue = heroModule.calculateFightValue(playerID, ioMessage);
		playerEntity.setFightValue(fightValue);
		if (!DateTimeUtil.isSameDay(updareTime, nowTime)) {
			playerEntity.setAttackFriendNum(20);
			playerEntity.setBeFriendAttackNum(20);
			playerEntity.setUpdateTime(System.currentTimeMillis());
		}
		this.savePlayerEntity(playerEntity);
		return playerEntity;
	}

	/**
	 * 获取用户信息
	 * */
	public PlayerEntity getPlayerEntity(String playerID, String serverID) {
		return playerEntitiyDAO.getEntity(playerID, serverID);
	}

	/**
	 * 保存用户信息
	 * */
	public void savePlayerEntity(PlayerEntity playerEntity) {
		playerEntitiyDAO.save(playerEntity);
	}

	/** 获取用户的状态 */
	public PlayerStatusEntity getPlayerStatusEntity(String playerID) {
		PlayerStatusEntity entity = playerStatuesEntityDAO.getEntity(playerID);
		if (entity == null) {
			logger.error("用户状态实体为空");
			throw new IllegalArgumentException(ErrorIds.NoEntity + "");
		}
		return entity;
	}

	/**
	 * 保存用户状态
	 * */
	public void savePlayerStatusEntity(PlayerStatusEntity entity) {
		playerStatuesEntityDAO.save(entity);
	}

	/**
	 * 更改用户的奖励状态
	 * */
	public PlayerStatusEntity changePlayerRewardEntity(String playerID, boolean isAdd) {
		PlayerStatusEntity entity = this.getPlayerStatusEntity(playerID);
		if (entity != null) {
			if (isAdd) {
				if (entity.getReward() != 1) {
					entity.setReward(1);
					this.savePlayerStatusEntity(entity);
				}
			} else {
				if (entity.getReward() != 0) {
					entity.setReward(0);
					this.savePlayerStatusEntity(entity);
				}
			}
		}
		return entity;
	}

	/**
	 * 更改用户的消息状态
	 * */
	public PlayerStatusEntity changePlayerRewardNewsEntity(String playerID, boolean isAdd) {
		PlayerStatusEntity entity = this.getPlayerStatusEntity(playerID);
		if (isAdd) {
			if (entity.getNews() != 1) {
				entity.setNews(1);
				this.savePlayerStatusEntity(entity);
			}
		} else {
			if (entity.getNews() != 0) {
				entity.setNews(0);
				this.savePlayerStatusEntity(entity);
			}
		}
		return entity;
	}

	/***
	 * 更改用户的好友状态
	 * */
	public PlayerStatusEntity changePlayerRewardFriendEntity(String playerID, boolean isAdd) {
		PlayerStatusEntity entity = this.getPlayerStatusEntity(playerID);
		if (isAdd) {
			if (entity.getFriend() != 1) {
				entity.setFriend(1);
				this.savePlayerStatusEntity(entity);
			}
		} else {
			if (entity.getFriend() != 0) {
				entity.setFriend(0);
				this.savePlayerStatusEntity(entity);
			}
		}
		return entity;
	}

	/**
	 * 更改用户的任务状态
	 * */
	public void changePlayerTaskEntity(String playerID, boolean isAdd) {
		PlayerStatusEntity entity = this.getPlayerStatusEntity(playerID);
		if (isAdd) {
			if (entity.getTask() != 1) {
				entity.setTask(1);
				this.savePlayerStatusEntity(entity);
			}
		} else {
			if (entity.getTask() != 0) {
				entity.setTask(0);
				this.savePlayerStatusEntity(entity);
			}
		}
	}

	/**
	 * 用户状态初始化
	 * */
	public PlayerStatusEntity initplPlayerStatusEntity(String playerID) {
		PlayerStatusEntity entity = new PlayerStatusEntity();
		entity.setKey(playerID);
		return entity;
	}

	/**
	 * 用户信息初始化
	 * */
	public PlayerEntity initPlayerEntity(String playerID, int sex, String nickName, String platform, IOMessage ioMessage) {
		PlayerEntity playerEntity = this.getPlayerEntityNoError(playerID);
// if (playerEntity != null) {
// throw new IllegalArgumentException(ErrorIds.UserRegistered + "");
// }
		this.checkName(nickName, sex);
		playerEntity = new PlayerEntity();
		playerEntity.setPlatform(platform);
		playerEntity.setKey(playerID);
		playerEntity.setLoginTime(System.currentTimeMillis());
		playerEntity.setNickName(nickName);
		playerEntity.setSex(sex);
		doRegister(playerID, playerEntity, ioMessage);
		return playerEntity;
	}

	/**
	 * 玩家登陆服务器
	 * */

	public void serverLogin(String playerID, LoginInfoProtocol protocol, IOMessage ioMessage) {
		PlayerEntity playerEntity = this.getPlayerEntity(playerID);
		
		long nowTime = System.currentTimeMillis();
		String uniqueKey = Utilities.getUniqueKey(nowTime, playerID);
		
		playerEntity.setUniqueKey(uniqueKey);
		//playerEntity.setLoginTime(nowTime);
		
		this.savePlayerEntity(playerEntity);
		
		protocol.setUniqueKey(uniqueKey);
		this.getUserInfo(playerID, protocol, ioMessage);
	}

	/**
	 * 玩家信息注册
	 * **/
	private void doRegister(String playerID, PlayerEntity playerEntity, IOMessage ioMessage) {
		WalletModule walletModule = ModuleManager.getModule(ModuleNames.WalletModule, WalletModule.class);
		WalletEntity walletEntity = walletModule.initWalletEntity(playerID);
		HeroModule heroModule = ModuleManager.getModule(ModuleNames.HeroModule, HeroModule.class);
		HeroEntity heroEntity = heroModule.initHeroEnitiy(playerID, playerEntity);
		HeroTroopsEntity heroTroopsEntity = heroModule.initHeroTroops(playerID, playerEntity.getSex());
		DungeonModule dungeonModule = ModuleManager.getModule(ModuleNames.DungeonModule, DungeonModule.class);
		DungeonMapEntity dungeonMapEntity = dungeonModule.initDungeonMapEntity(playerID);
		DungeonActMapEntity actMapEntity = dungeonModule.initDungeonAct(playerID);
		DungeonEliteEntity eliteEntiy = dungeonModule.initDungeonEliteEntity(playerID);
		DungeonActiveEntity activeEntity = dungeonModule.initActiveEntity(playerID);
		VitatlyModule vitatlyModule = ModuleManager.getModule(ModuleNames.VitatlyModule, VitatlyModule.class);
		VitatlyEntity vitatlyEntity = vitatlyModule.initVitatlyEntity(playerID);
		BagModule bagModule = ModuleManager.getModule(ModuleNames.BagModule, BagModule.class);
		BagEntity bagEntity = bagModule.initBagEntity(playerID);
		EquipmentModule equipmentModule = ModuleManager.getModule(ModuleNames.EquipmentModule, EquipmentModule.class);
		EquipmentMapEntity equipEntity = equipmentModule.initEquipMentEntity(playerID);
		TalismanModule talismanModule = ModuleManager.getModule(ModuleNames.TalismanModule, TalismanModule.class);
		TalismanMapEntity talismanEntity = talismanModule.initTalismanEntity(playerID);
		PetModule petModule = ModuleManager.getModule(ModuleNames.PetModule, PetModule.class);
		PetEntity petEntity = petModule.initEntity(playerID);
		TowerModule towerModule = ModuleManager.getModule(ModuleNames.TowerModule, TowerModule.class);
		TowerEntity towerEntity = towerModule.initEntity(playerID);
		RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
		RewardCenterEntity rewadCenterEntity = rewardModule.initCenterEntity(playerID);
		PlayerStatusEntity playerStatusEntity = this.initplPlayerStatusEntity(playerID);
		LeadModule leadModule = ModuleManager.getModule(ModuleNames.LeadModule, LeadModule.class);
		LeadDesitnyEntity leadDesitnyEntity = leadModule.initDestinyEntity(playerID);
		ArenaModule arenaModule = ModuleManager.getModule(ModuleNames.ArenaModule, ArenaModule.class);
		ArenaShopEntity arenaShopEntity = arenaModule.initShopEntity(playerID);
		HeroDrawModule heroDrawModule = ModuleManager.getModule(ModuleNames.HeroDrawModule, HeroDrawModule.class);
		HeroDrawEntity heroDrawEntity = heroDrawModule.initEntity(playerID);
		NewPlayerEntity newPlayerEntity = this.initNewPlayerEntity(playerID);
		EffectModule effectModule = ModuleManager.getModule(ModuleNames.EffectModule, EffectModule.class);
		PlayerEffectEntity playerEffectEntity = effectModule.initPlayerEffectEntity(playerID);
		ManualModule manualModule = ModuleManager.getModule(ModuleNames.ManualModule, ManualModule.class);
		HeroManualsEntity heroManualsEntity = manualModule.initEntity(playerID);
		List<BaseEntity> entityList = new ArrayList<BaseEntity>();
		entityList.add(playerEffectEntity);
		entityList.add(newPlayerEntity);
		entityList.add(heroDrawEntity);
		entityList.add(arenaShopEntity);
		entityList.add(leadDesitnyEntity);
		entityList.add(playerStatusEntity);
		entityList.add(activeEntity);
		entityList.add(eliteEntiy);
		entityList.add(actMapEntity);
		entityList.add(towerEntity);
		entityList.add(talismanEntity);
		entityList.add(walletEntity);
		entityList.add(bagEntity);
		entityList.add(vitatlyEntity);
		entityList.add(heroEntity);
		entityList.add(playerEntity);
		entityList.add(heroTroopsEntity);
		entityList.add(dungeonMapEntity);
		entityList.add(equipEntity);
		entityList.add(petEntity);
		entityList.add(rewadCenterEntity);
		entityList.add(heroManualsEntity);
		playerEntitiyDAO.save(entityList);
		talismanModule.initTalismanShard(playerID);
		heroModule.initHeroMaps(playerID, ioMessage);
//		MailBoxModule mailBoxModule = ModuleManager.getModule(ModuleNames.MailBoxModule, MailBoxModule.class);
//		mailBoxModule.addMail(playerID, null, null, null, MailType.SYSTEMTYPE, SystemMsgType.TestPay, null);
//		mailBoxModule.addMail(playerID, null, null, null, MailType.SYSTEMTYPE, SystemMsgType.WelcomeInfo, null);
	}

	/**
	 * 获取玩家基本信息
	 * */
	public void getBaseUserInfo(String playerID, LoginInfoProtocol protocol, IOMessage ioMessage) {
		HeroDrawModule heroDrawModule = ModuleManager.getModule(ModuleNames.HeroDrawModule, HeroDrawModule.class);
		HeroDrawEntity heroDrawEntity = heroDrawModule.getEntity(playerID);
		VitatlyModule vitatlyModule = ModuleManager.getModule(ModuleNames.VitatlyModule, VitatlyModule.class);
		VitatlyEntity vitatlyEntity = vitatlyModule.getVitatlyEntity(playerID);

		BagModule bagModule = ModuleManager.getModule(ModuleNames.BagModule, BagModule.class);
		BagEntity bagEntity = bagModule.getBagEntity(playerID);
		WalletModule walletModule = ModuleManager.getModule(ModuleNames.WalletModule, WalletModule.class);
		WalletEntity walletEntity = walletModule.getwalletEntity(playerID);
		PlayerStatusEntity playerStatusEntity = this.getPlayerStatusEntity(playerID);
		ArenaModule arenaModule = ModuleManager.getModule(ModuleNames.ArenaModule, ArenaModule.class);
		LuckRankMapEntity luckRankMapEntity = arenaModule.getLuckyRankList();
		ArenaShopEntity arenaShopEntity = arenaModule.getExchangeList(playerID);
		NewPlayerEntity newPlayerEntity = this.getNewPlayerEntity(playerID);
		EffectModule effectModule = ModuleManager.getModule(ModuleNames.EffectModule, EffectModule.class);
		PlayerEffectEntity playerEffectEntity = effectModule.getPlayerEffectEntity(playerID);

		protocol.setPlayerEffectEntity(playerEffectEntity);
		protocol.setHeroDrawEntity(heroDrawEntity);
		protocol.setNewPlayerEntity(newPlayerEntity);
		protocol.setArenaShopEntity(arenaShopEntity);
		protocol.setLuckRankMapEntity(luckRankMapEntity);
		protocol.setPlayerStatusEntity(playerStatusEntity);
		protocol.setWalletEntity(walletEntity);
		protocol.setBagEntity(bagEntity);
		protocol.setVitatlyEntity(vitatlyEntity);
	}

	/***
	 * 获取玩家战斗信息
	 * */
	public void getBattleInfo(String playerID, LoginInfoProtocol protocol, IOMessage ioMessage) {
		PlayerEntity playerEntity = this.getUpdatePlayerEntity(playerID, ioMessage);
		long nowTime = System.currentTimeMillis();
		String uniqueKey = Utilities.getUniqueKey(nowTime, playerID);
		playerEntity.setUniqueKey(uniqueKey);
		this.savePlayerEntity(playerEntity);
		HeroModule heroModule = ModuleManager.getModule(ModuleNames.HeroModule, HeroModule.class);
		HeroTroopsEntity heroTroopsEntity = heroModule.getHeroTroopsEntity(playerID);
		HeroEntity heroEntity = heroModule.getHeroEntity(playerID, ioMessage);
		EquipmentModule equipmentModule = ModuleManager.getModule(ModuleNames.EquipmentModule, EquipmentModule.class);
		EquipmentMapEntity equipmentEntity = equipmentModule.getEquipmentMapEntity(playerID, ioMessage);
		TalismanModule talismanModule = ModuleManager.getModule(ModuleNames.TalismanModule, TalismanModule.class);
		TalismanMapEntity talismanEntity = talismanModule.getEntity(playerID, ioMessage);
		List<TalismanShard> talismanShardList = talismanModule.getTalismanShardList(playerID);
		ManualModule manualModule = ModuleManager.getModule(ModuleNames.ManualModule, ManualModule.class);
		HeroManualsEntity heroManualsEntity = manualModule.getEntity(playerID, ioMessage);
		LeadModule leadModule = ModuleManager.getModule(ModuleNames.LeadModule, LeadModule.class);
		LeadDesitnyEntity leadDesitnyEntity = leadModule.getDesitnyEntity(playerID, ioMessage);
		PetModule petModule = ModuleManager.getModule(ModuleNames.PetModule,PetModule.class);
		PetEntity petEntity = petModule.getEntity(playerID);
		protocol.setUniqueKey(uniqueKey);
		protocol.setLeadDesitnyEntity(leadDesitnyEntity);
		protocol.setPlayerEntity(playerEntity);
		protocol.setHeroTroopsEntity(heroTroopsEntity);
		protocol.setHeroManualsEntity(heroManualsEntity);
		protocol.setHeroEntity(heroEntity);
		protocol.setTalismanMapEntity(talismanEntity);
		protocol.setTalismanShardList(talismanShardList);
		protocol.setEquipmentMapEntity(equipmentEntity);
		protocol.setPetEntity(petEntity);
	}

	/**
	 * 获取玩家副本信息
	 * */
	public void getDungeonInfo(String playerID, LoginInfoProtocol protocol, IOMessage ioMessage) {
		DungeonModule dungeonModule = ModuleManager.getModule(ModuleNames.DungeonModule, DungeonModule.class);
		DungeonMapEntity dungeonMapEntity = dungeonModule.getResponseDungeonMapEntity(playerID);
		DungeonActMapEntity dungeonActMapEntity = dungeonModule.getDungeonActEntity(playerID);
		DungeonEliteEntity dungeonEliteEntity = dungeonModule.getResponseEilteEntity(playerID);
		DungeonActiveEntity dungeonActiveEntity = dungeonModule.getResponseActiveEntity(playerID);
		ActLimitRewardMapEntity actLimitRewardMapEntity = dungeonModule.getUpdateActLimitEntity(playerID);
		FarmModule farmModule = ModuleManager.getModule(ModuleNames.FarmModule, FarmModule.class);
		FarmEntity farmEntity = farmModule.getEntity(playerID);
		protocol.setDungeonActiveEntity(dungeonActiveEntity);
		protocol.setDungeonMapEntity(dungeonMapEntity);
		protocol.setDungeonEliteEntity(dungeonEliteEntity);
		protocol.setDungeonActMapEntity(dungeonActMapEntity);
		protocol.setActLimitRewardMapEntity(actLimitRewardMapEntity);
		protocol.setFarmEntity(farmEntity);
	}

	/**
	 * 获取玩家登陆数据
	 * 
	 * @param playerID
	 *            String 玩家ID
	 * @param protocol
	 *            LoginInfoPortocol 登陆返回协议
	 * */
	private void getUserInfo(String playerID, LoginInfoProtocol protocol, IOMessage ioMessage) {
		PlayerEntity playerEntity = this.getUpdatePlayerEntity(playerID, ioMessage);
		protocol.setPlayerEntity(playerEntity);
		HeroModule heroModule = ModuleManager.getModule(ModuleNames.HeroModule, HeroModule.class);
		HeroTroopsEntity heroTroopsEntity = heroModule.getHeroTroopsEntity(playerID);
		HeroEntity heroEntity = heroModule.getHeroEntity(playerID, ioMessage);
		HeroDrawModule heroDrawModule = ModuleManager.getModule(ModuleNames.HeroDrawModule, HeroDrawModule.class);
		HeroDrawEntity heroDrawEntity = heroDrawModule.getEntity(playerID);
		DungeonModule dungeonModule = ModuleManager.getModule(ModuleNames.DungeonModule, DungeonModule.class);
		DungeonMapEntity dungeonMapEntity = dungeonModule.getResponseDungeonMapEntity(playerID);
		DungeonActMapEntity dungeonActMapEntity = dungeonModule.getDungeonActEntity(playerID);
		DungeonEliteEntity dungeonEliteEntity = dungeonModule.getResponseEilteEntity(playerID);
		DungeonActiveEntity dungeonActiveEntity = dungeonModule.getResponseActiveEntity(playerID);
		ActLimitRewardMapEntity actLimitRewardMapEntity = dungeonModule.getUpdateActLimitEntity(playerID);
		VitatlyModule vitatlyModule = ModuleManager.getModule(ModuleNames.VitatlyModule, VitatlyModule.class);
		VitatlyEntity vitatlyEntity = vitatlyModule.getVitatlyEntity(playerID);
		BagModule bagModule = ModuleManager.getModule(ModuleNames.BagModule, BagModule.class);
		BagEntity bagEntity = bagModule.getBagEntity(playerID);
		EquipmentModule equipmentModule = ModuleManager.getModule(ModuleNames.EquipmentModule, EquipmentModule.class);
		EquipmentMapEntity equipmentEntity = equipmentModule.getEquipmentMapEntity(playerID, ioMessage);
		WalletModule walletModule = ModuleManager.getModule(ModuleNames.WalletModule, WalletModule.class);
		WalletEntity walletEntity = walletModule.getwalletEntity(playerID);
		TalismanModule talismanModule = ModuleManager.getModule(ModuleNames.TalismanModule, TalismanModule.class);
		TalismanMapEntity talismanEntity = talismanModule.getEntity(playerID, ioMessage);
		List<TalismanShard> talismanShardList = talismanModule.getTalismanShardList(playerID);
		PetModule petModule = ModuleManager.getModule(ModuleNames.PetModule,PetModule.class);
		PetEntity petEntity = petModule.getEntity(playerID);
		ManualModule manualModule = ModuleManager.getModule(ModuleNames.ManualModule, ManualModule.class);
		HeroManualsEntity heroManualsEntity = manualModule.getEntity(playerID, ioMessage);
		PlayerStatusEntity playerStatusEntity = this.getPlayerStatusEntity(playerID);
		LeadModule leadModule = ModuleManager.getModule(ModuleNames.LeadModule, LeadModule.class);
		LeadDesitnyEntity leadDesitnyEntity = leadModule.getDesitnyEntity(playerID, ioMessage);
		ArenaModule arenaModule = ModuleManager.getModule(ModuleNames.ArenaModule, ArenaModule.class);
		LuckRankMapEntity luckRankMapEntity = arenaModule.getLuckyRankList();
		ArenaShopEntity arenaShopEntity = arenaModule.getExchangeList(playerID);
		NewPlayerEntity newPlayerEntity = this.getNewPlayerEntity(playerID);
		EffectModule effectModule = ModuleManager.getModule(ModuleNames.EffectModule, EffectModule.class);
		PlayerEffectEntity playerEffectEntity = effectModule.getPlayerEffectEntity(playerID);
		FarmModule farmModule = ModuleManager.getModule(ModuleNames.FarmModule, FarmModule.class);
		FarmEntity farmEntity = farmModule.getUpdateEntity(playerID);
		protocol.setActLimitRewardMapEntity(actLimitRewardMapEntity);
		protocol.setFarmEntity(farmEntity);
		protocol.setTalismanShardList(talismanShardList);
		protocol.setPlayerEffectEntity(playerEffectEntity);
		protocol.setHeroDrawEntity(heroDrawEntity);
		protocol.setNewPlayerEntity(newPlayerEntity);
		protocol.setArenaShopEntity(arenaShopEntity);
		protocol.setLuckRankMapEntity(luckRankMapEntity);
		protocol.setLeadDesitnyEntity(leadDesitnyEntity);
		protocol.setPlayerStatusEntity(playerStatusEntity);
		protocol.setDungeonActiveEntity(dungeonActiveEntity);
		protocol.setDungeonEliteEntity(dungeonEliteEntity);
		protocol.setDungeonActMapEntity(dungeonActMapEntity);
		protocol.setHeroManualsEntity(heroManualsEntity);
		protocol.setPetEntity(petEntity);
		protocol.setTalismanMapEntity(talismanEntity);
		protocol.setWalletEntity(walletEntity);
		protocol.setEquipmentMapEntity(equipmentEntity);
		protocol.setBagEntity(bagEntity);
		protocol.setVitatlyEntity(vitatlyEntity);
		protocol.setDungeonMapEntity(dungeonMapEntity);
		protocol.setHeroEntity(heroEntity);
		protocol.setHeroTroopsEntity(heroTroopsEntity);
	}

	/**
	 * 获取随机名字列表
	 * */
	public List<String> getRandomNameList(int sex) {
		List<String> returnList = new ArrayList<String>(10);
		List<String> randomList = new ArrayList<>();
		if (sex == 0) {
			randomList = maleRandomName;
		} else {
			randomList = femaleRandomName;
		}
		int size = randomList.size();
		int random = 0;
		if (size > 10) {
			random = Utilities.getRandomInt(size - 10);
			returnList = randomList.subList(random, random + 10);
		} else {
			returnList = randomList;
		}
		return returnList;
	}

	/**
	 * 修改玩家聊天头像
	 * */
	public void changePhotoID(String playerID, int photoID) {
		PlayerEntity playerEntity = this.getPlayerEntity(playerID);
		playerEntity.setChatPhotoID(photoID);
		this.savePlayerEntity(playerEntity);
	}

	/**
	 * 修改玩家的新手状态
	 * */
	public void saveNewPlayerState(String playerID, int properId, boolean isFinality, String triggerId) {
		NewPlayerEntity entity = this.getNewPlayerEntity(playerID);
		entity.setFinality(isFinality);
		entity.setProperId(properId);
		entity.setTriggerId(triggerId);
		this.saveNewPlayerEntity(entity);
	}

	/**
	 * 记录玩家的新手关
	 * */
	public NewPlayerEntity saveNewPlayerGameLevel(String playerID, int gameLevelID, IOMessage ioMessage) {
		NewPlayerEntity entity = this.getNewPlayerEntity(playerID, ioMessage);
		int saveID = entity.getGameLevelID();
		if (saveID < gameLevelID) {
			entity.setGameLevelID(gameLevelID);
			this.saveNewPlayerEntity(entity);
		}
		return entity;
	}

	/**
	 * 获取day 天内20级以上登录用户
	 * 
	 * @param day
	 * @return
	 */
	public List<PlayerEntity> getaActivePlayerList(int day, int level, boolean type) {
		return playerEntitiyDAO.getaActivePlayerList(day, level, type);
	}

	/**
	 * 获取达到level级的用户id列表
	 * 
	 * @param level
	 * @return
	 */
	public List<String> getPlayerListByLevel(int level) {
		return playerEntitiyDAO.getPlayerListByLevel(level);
	}
	
	/**
	 * 获取达到VipLevel的用户列表
	 * @param vipLevel
	 * @return
	 */
	public List<PlayerEntity> getPlayerListByVIPLevel(int vipLevel) {
		return playerEntitiyDAO.getPlayerListByVIPLevel(vipLevel);
	}

	/**
	 * 获取战斗力排行用户id列表
	 * 
	 * @param size
	 * @return
	 */
	public List<String> getPlayerListByfightValue(int size) {
		return playerEntitiyDAO.getPlayerListByfightValue(size);
	}

	/**
	 * 获取战斗力排行用户实体列表
	 * 
	 * @param size
	 * @return
	 */
	public List<PlayerEntity> getPlayerEntityListByfightValue(int size) {
		return playerEntitiyDAO.getPlayerEntityListByFightValue(size);
	}

	/**
	 * 修改玩家姓名
	 * */
	public void changePlayerName(String playerID, String nickName, IOMessage ioMessage, LoginInfoProtocol protocol) {
		BagModule bagModule = ModuleManager.getModule(ModuleNames.BagModule, BagModule.class);
		BagItem bagItem = bagModule.getBagItem(playerID, 101763, ioMessage);
		PlayerEntity playerEntity = this.getPlayerEntity(playerID);
		this.checkName(nickName, playerEntity.getSex());
		int pid = 0;
		int num = 0;
		if (bagItem != null && bagItem.getNum() > 0) {
			pid = 101763;
			num = 1;
		} else {
			pid = KindIDs.GOLDTYPE;
			num = 100;
		}
		Map<String, Object> itemMap = new HashMap<>();
		RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
		int code = rewardModule.useGoods(playerID, pid, num, 0, true, null, itemMap, ioMessage);
		if (code != 0) {
			protocol.setCode(code);
			return;
		}

		// ////
		// // 元宝消耗记录
		// ///
		AnalyseModule analyseModule = ModuleManager.getModule(ModuleNames.AnalyseModule, AnalyseModule.class);
		analyseModule.goldCostLog(playerID, num, 1, num, "changePlayerName", "login");

		playerEntity.setNickName(nickName);
		this.savePlayerEntity(playerEntity);
		protocol.setItemMap(itemMap);
	}

	private void checkName(String nickName, int sex) {
		if (nickName.length() > 5 || nickName.length() < 1) {
			logger.error(" 名字长度不合法");
			throw new IllegalArgumentException(ErrorIds.NameLength + "");
		}
		if (!CommonMethod.checkNameShieldedKeyword(nickName)) {
			logger.error("名字不合法");
			throw new IllegalArgumentException(ErrorIds.NameNoLegal + "");
		}
		if (this.getPlayerEntityByName(nickName) != null) {
			throw new IllegalArgumentException(ErrorIds.SameName + "");
		}
		List<String> randomList = new ArrayList<>();
		if (sex == 0) {
			randomList = maleRandomName;
		} else {
			randomList = femaleRandomName;
		}
		if (randomList != null) {
			if (randomList.contains(nickName)) {
				randomList.remove(nickName);
			}
		}

	}

	/**
	 * 测试用户发送测试奖励
	 * 
	 * @param playerID
	 */
	public void testUserSendReward(String playerID) {
		AnalyseModule analyseModule = ModuleManager.getModule(ModuleNames.AnalyseModule, AnalyseModule.class);
		AnalyEntity analyEntity = analyseModule.getAnalyEntity(playerID);
		if (analyEntity == null) {
			logger.error("测试用户发放奖励时,找不到用户. playerID=" + playerID);
			return;
		}
		String uid = analyseModule.getAnalyEntity(playerID).getUid();
		boolean isTest = TestUserSendRewardUtil.checkTestUser(uid);
		if (isTest) {
			
			TestUserRewardEntity entity = getTestUserRewardEntity(uid);
			if (entity == null) {
				int payValue = TestUserSendRewardUtil.getTestUserPayValue(uid) * 3;
				// 邮件发送
				MailBoxModule mailBoxModule = ModuleManager.getModule(ModuleNames.MailBoxModule, MailBoxModule.class);
				String title = "删档测试充值三倍返还";
				String message = "您好！根据您在删档测试期间的充值额度，我们向您返还"+payValue+"元宝。感谢您对《天天西游2》的支持，如有疑问，请与客服联系。";
				mailBoxModule.sendEventRewardMail(playerID, title, message);
				// 奖励发送
				RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
				List<GoodsBean> goodsList = new ArrayList<GoodsBean>();
				goodsList.add(new GoodsBean(KindIDs.GOLDTYPE, payValue));
				rewardModule.addReward(playerID, goodsList, RewardType.systemReward);
				// 保存发送用户uid
				entity = new TestUserRewardEntity();
				entity.setUid(uid);
				entity.setDateTime(System.currentTimeMillis());
				saveTestUserRewardEntity(entity);
			}
		}

	}

	public int getPlayerLevel(String playerID) {
		PlayerEntity playerEntity = this.getPlayerEntity(playerID);
		return playerEntity.getLevel();
	}

	public void saveMinuEntity(MinuEntity entity) {
		minuEntityDao.save(entity);
	}

	public MinuEntity getMinuEntity(String minu_uid) {
		return minuEntityDao.getMinuEntity(minu_uid);
	}

	public void saveTestUserRewardEntity(TestUserRewardEntity entity) {
		testUserRewardEntityDao.save(entity);
	}

	public TestUserRewardEntity getTestUserRewardEntity(String uid) {
		return testUserRewardEntityDao.getTestUserRewardEntity(uid);
	}

	public void changeServerInfo() throws Exception {
		TemplateManager.removeTemplateData(ServerInfoData.class);
		Set<Class<?>> classSet = ClassScanUtil.getClasses("com.mi.game.module.base.bean.init.server");
		for (Class<?> item : classSet) {
			@SuppressWarnings("unchecked")
			List<BaseTemplate> cardList = XmlTemplateUtil.loadXmlTemplate((Class<BaseTemplate>) item);
			TemplateManager.addTemplateData(cardList);
		}
		this.initServerInfoList();

		// 更新分发服务器列表
		PayModule payModule = ModuleManager.getModule(ModuleNames.PayModule, PayModule.class);
		payModule.initServerListData();
	}

	/***
	 * 初始化游客ID
	 * */
	private void initVisitorID() {
		String clsName = SysConstants.visitorIDEntity;
		KeyGenerator keyGenerator = keyGeneratorDAO.getEntity(clsName);
		if (keyGenerator == null) {
			keyGenerator = new KeyGenerator();
			keyGenerator.setKey(clsName);
			keyGenerator.setNextId(SysConstants.visitorStartID);
			keyGeneratorDAO.save(keyGenerator);
		}
	}

	/**
	 * 获取游客唯一ID
	 * */
	public long getVisitorID() {
		String clsName = SysConstants.visitorIDEntity;
		long equipID = keyGeneratorDAO.updateInc(clsName);
		return equipID;
	}

	/**
	 * 根据游客ID获取注册信息
	 * */
	public LoginInfoEntity getLoginInfoEntityByVisitorID(String visitorID) {
		LoginInfoEntity loginInfoEntity = loginInfoDAO.getEntityByVisitorID(visitorID);
		return loginInfoEntity;
	}

	/**
	 * 游客登录
	 * */
	public LoginInfoEntity visitorLogin(String visitorID) {
		if (visitorID == null || visitorID.isEmpty()) {
			throw new IllegalArgumentException(ErrorIds.ParamWrong + "");
		}
		LoginInfoEntity loginInfoEntity = this.getLoginInfoEntityByVisitorID(visitorID);
		if (loginInfoEntity == null) {
			long id = this.getVisitorID();
			loginInfoEntity = new LoginInfoEntity();
			loginInfoEntity.setVisitorID(id);
			loginInfoEntity.setVisitorPlayerName(visitorID);
			String playerName = "visitor" + id;
			String password = this.getVisitorPassword(visitorID);
			loginInfoEntity.setPlayerName(playerName);
			loginInfoEntity.setPassword(password);
			loginInfoEntity.setLastLoginTime(new Date().getTime());
			loginInfoDAO.save(loginInfoEntity);
		}
		return loginInfoEntity;
	}

	/**
	 * 获取游客随机密码
	 * */
	private String getVisitorPassword(String visitorID) {
		String temp = visitorID + System.currentTimeMillis();
		String password = Utilities.MD5Encode(temp).substring(0, 8);
		return password;
	}

	/**
	 * 游客绑定账户
	 * */
	public void visitorBind(String phoneID, String playerName, String passwd, String email) {
		if (phoneID == null || phoneID.isEmpty()) {
			logger.error("参数错误");
			throw new IllegalArgumentException(ErrorIds.ParamWrong + "");
		}
		LoginInfoEntity loginInfoEntity = this.getLoginInfoEntityByVisitorID(phoneID);
		if (loginInfoEntity == null) {
			logger.error("设备ID未注册");
			throw new IllegalArgumentException(ErrorIds.PhoneIDNoRegister + "");
		}
		boolean bind = loginInfoEntity.isBind();
		if (bind) {
			logger.error("该账号已绑定");
			throw new IllegalArgumentException(ErrorIds.PhoneIDBind + "");
		}
		int canRegister = checkUser(playerName, passwd, email);
		if (canRegister == 0) {
			loginInfoEntity.setPassword(passwd);
			loginInfoEntity.setPlayerName(playerName);
			loginInfoEntity.setBind(true);
			loginInfoEntity.setRegisterTime(new Date().getTime());
			loginInfoDAO.save(loginInfoEntity);
			if (logger.isDebugEnabled()) {
				logger.debug(playerName + "注册成功");
			}
		} else {
			throw new IllegalArgumentException(canRegister + "");
		}
	}

	/**
	 * 修改密码
	 * */
	public void changePassword(String playerName, String oldPassword, String newPassword) {
		LoginInfoEntity loginInfoEntity = loginInfoDAO.getEntityByName(playerName);
		if (loginInfoEntity == null) {
			logger.error("用户账号名错误");
			throw new IllegalArgumentException(ErrorIds.PasswdWrong + "");
		}
		if (loginInfoEntity.getPassword().equals(oldPassword)) {
			if (!Utilities.IsUserName(newPassword)) {
				logger.error("新密码错误");
				throw new IllegalArgumentException(ErrorIds.PasswdIllegal + "");
			}
			loginInfoEntity.setPassword(newPassword);
			loginInfoDAO.save(loginInfoEntity);
		} else {
			logger.error("原始密码错误");
			throw new IllegalArgumentException(ErrorIds.PasswdWrong + "");
		}
	}

	public String getServerMd5String() {
		String fileName = "serverInfo.xml";
		String path = TemplateManageHandler.class.getResource("/com/mi/template/").getPath();
		String filePath = path + fileName;
		File big = new File(filePath);
		String md5 = "";
		try {
			md5 = MD5FileUtil.getFileMD5String(big);
		} catch (IOException ex) {
			logger.error("获取serverInfo文件失败");
			System.out.println(ex);
		}
		return md5;
	}

}
