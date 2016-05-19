package com.mi.game.module.pk;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mi.core.cache.QueryInfo;
import com.mi.core.cache.QueryType;
import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.TemplateManager;
import com.mi.core.engine.annotation.Module;
import com.mi.core.job.BaseJob;
import com.mi.core.job.QuartzJobService;
import com.mi.core.protocol.BaseProtocol;
import com.mi.core.util.DateTimeUtil;
import com.mi.game.defines.ErrorIds;
import com.mi.game.defines.KindIDs;
import com.mi.game.defines.ModuleNames;
import com.mi.game.defines.PkRankType;
import com.mi.game.defines.RewardType;
import com.mi.game.defines.SysConstants;
import com.mi.game.module.base.BaseModule;
import com.mi.game.module.battleReport.BattleReportModule;
import com.mi.game.module.hero.HeroModule;
import com.mi.game.module.hero.pojo.Hero;
import com.mi.game.module.hero.pojo.HeroEntity;
import com.mi.game.module.legion.LegionModule;
import com.mi.game.module.legion.pojo.LegionEntity;
import com.mi.game.module.login.dao.PlayerEntitiyDAO;
import com.mi.game.module.login.pojo.PlayerEntity;
import com.mi.game.module.mailBox.MailBoxModule;
import com.mi.game.module.pk.dao.PkEntityDao;
import com.mi.game.module.pk.dao.PkExchangeHistoryEntityDao;
import com.mi.game.module.pk.dao.PkRewardEntityDao;
import com.mi.game.module.pk.data.PkRewardData;
import com.mi.game.module.pk.data.PkRewardShopData;
import com.mi.game.module.pk.data.PkScoreData;
import com.mi.game.module.pk.data.PkSendRewardData;
import com.mi.game.module.pk.pojo.Pk;
import com.mi.game.module.pk.pojo.PkEntity;
import com.mi.game.module.pk.pojo.PkExchangeHistoryEntity;
import com.mi.game.module.pk.pojo.PkPlayer;
import com.mi.game.module.pk.pojo.PkRewardEntity;
import com.mi.game.module.pk.protocol.PkProtocol;
import com.mi.game.module.reward.RewardModule;
import com.mi.game.module.reward.data.GoodsBean;
import com.mi.game.module.vitatly.dao.VitatlyDAO;
import com.mi.game.module.vitatly.pojo.VitatlyEntity;
import com.mi.game.util.Base64Coder;
import com.mi.game.util.GZIPUtil;
import com.mi.game.util.Utilities;

/**
 * 
 * @author 赵鹏翔 比武模块 2015年03月25日 17:42:30
 *
 */
@Module(name = ModuleNames.PkModule, clazz = PkModule.class)
public class PkModule extends BaseModule {
	private PkEntityDao entityDao = PkEntityDao.getInstance();

	private PlayerEntitiyDAO playerEntitiyDAO = PlayerEntitiyDAO.getInstance();

	private static PkRewardEntityDao pkRewardEntityDao = PkRewardEntityDao
			.getInstance();

	private static VitatlyDAO vitatlyDAO = VitatlyDAO.getInstance(); // 体力,耐力接口

	private static PkExchangeHistoryEntityDao historyEntityDao = PkExchangeHistoryEntityDao
			.getInstance();
	private static PkScoreData pkScoreDate;
	private final static int settleJobID = 9008010;
	@Override
	public void init() {
		initScheduler(); // 初始化发送比武奖励job
		initPkScoreData();
	}

	public void initPkScoreData() {
		pkScoreDate = TemplateManager.getTemplateData(13071, PkScoreData.class);
	}

	/**
	 * 将定时发送奖励的方法添加到任务调度器中
	 */
	@SuppressWarnings("unchecked")
	private void initScheduler() {
		PkSendRewardData jobData = TemplateManager.getTemplateData(settleJobID,
				PkSendRewardData.class);
		if (jobData == null) {
			if (logger.isErrorEnabled()) {
				throw new IllegalArgumentException(
						ErrorIds.PKSENDREWARDJJOBDATAWRONG + "");
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
			throw new IllegalArgumentException(
					ErrorIds.PKSENDREWARDJJOBDATAWRONG
					+ "");
		}
		quartz.createJob(jobData.getCount(), jobData.getInterval(), timeString,
				classz);
	}

	/**
	 * 新增比武信息
	 * 
	 * @param playerID
	 *            玩家ID
	 * @param pkPlayerID
	 *            对战玩家ID
	 * @param result
	 *            结果 0:胜利;1:失败
	 * @param updatePoint
	 *            胜利传正数,失败传负数
	 * @param battleString
	 *            战斗过程
	 */
	public void creatPkEntity(String playerID, String pkPlayerID, int result,
			int updatePoint, PkProtocol protocol, String battleString,
			IOMessage ioMessage) {
		// String today = Utilities.getDateTime("yyyyMMdd");
		// String weekNum = Utilities.getWeekNumOfYear(new Date());
		String today = Utilities.getDateTime("yyyyMMdd");
		PkEntity pkEntity = entityDao.getEntity(playerID);
		if (pkEntity == null) { // 没有比武记录,添加
			pkEntity = new PkEntity();
		}
		// 修改比武记录
		Pk pk = null;
		if (pkEntity.getItems() != null) {
			pk = pkEntity.getItems().get(today);
		}
		if (pk == null) {
			pk = new Pk();
			pk.setEnergy(2); // 体力值加2
			pk.setNum(1); // 次数加1
			Map<String, Pk> items = new HashMap<String, Pk>();
			items.put(today, pk);
			pkEntity.setItems(items);

			// 算积分
			int oldPoints = pkEntity.getPoints();
			if (updatePoint > 0) {
				int newPoint = (oldPoints + updatePoint) > 0 ? (oldPoints + updatePoint)
						: 0;
				pkEntity.setPoints(newPoint);
			}

		} else {
			pk.setEnergy(pk.getEnergy() + 2);
			pk.setNum(pk.getNum() + 1);
			pkEntity.getItems().put(today, pk);
			int oldPoints = pkEntity.getPoints();
			if (updatePoint > 0) {
				int newPoint = (oldPoints + updatePoint) > 0 ? (oldPoints + updatePoint)
						: 0;
				pkEntity.setPoints(newPoint);
			}
		}

		if (result == 1) { // 比武失败
		// List<String> lostList = pkEntity.getLostTo();
		// if (lostList == null) {
		// lostList = new ArrayList();
		// }
		// if (!lostList.contains(pkPlayerID)) {
			// // 最多保存十个复仇对象,如果达到上限,将最开始的记录删除
		// if (lostList.size() == 20) {
		// lostList.remove(0);
		// }
			// lostList.add(pkPlayerID); // 将对战玩家的id添加到list中
		// pkEntity.setLostTo(lostList);
		// }
		} else {
			// 表示比武成功,查询是否有失败记录,有的话删除此对手
			List<String> lostList = pkEntity.getLostTo();
			if (lostList != null) {
				if (lostList.contains(pkPlayerID)) {
					lostList.remove(pkPlayerID);
					pkEntity.setLostTo(lostList);
				}
			}

			// 对方扣减积分,把我添加到对方的仇人列表中
			addToLost(pkPlayerID, updatePoint, playerID);
			// 添加荣誉值
			addReward(playerID);
		}

		// 扣减耐力值
		RewardModule rewardModule = ModuleManager.getModule(
				ModuleNames.RewardModule, RewardModule.class);
		Map<String, Object> itemMap = new HashMap<String, Object>();
		rewardModule.useGoods(playerID, KindIDs.ENERGY, 2, 0, true, null,
				itemMap, ioMessage);
		// 修改最新获得积分时间,排序用
		pkEntity.setUpdateTime(System.currentTimeMillis());
		entityDao.save(pkEntity); // 保存记录
		this.getPlayerPkRankInfo(playerID, protocol, today);
		protocol.setItemMap(itemMap);
		
		// 保存战斗录像
		Map<String, Object> msgParam = new HashMap<String, Object>();
		MailBoxModule mailBoxModule = ModuleManager.getModule(
				ModuleNames.MailBoxModule, MailBoxModule.class);

		if (!Utilities.isNpc(pkPlayerID)) {
			if (!battleString.isEmpty()) {
				BattleReportModule battleReportModule = ModuleManager
						.getModule(ModuleNames.BattleReportModule,
								BattleReportModule.class);
				try {
					// 压缩
					byte[] bytes = GZIPUtil.compress(battleString);
					// 编码
					String base64 = new String(Base64Coder.encode(bytes));
					// 保存
					long reportID = battleReportModule.saveReport(base64);
					// 将战斗录像ID返回给前端
				} catch (Exception e) {
					logger.error("战斗录像压缩存储错误!");
				}
			}
		}
	}

	/**
	 * 比武对手减积分
	 * 
	 * @param pkPlayerID
	 * @param updatePoint
	 */
	public void addToLost(String pkPlayerID, int updatePoint, String lostID) {
		PkEntity pkEntity = entityDao.getEntity(pkPlayerID);
		if (pkEntity == null) {
			pkEntity = new PkEntity();
		}
		List<String> lostList = pkEntity.getLostTo();
		if (lostList == null) {
			lostList = new ArrayList<String>();
		}

		if (!lostList.contains(lostID)) {
			lostList.add(lostID);
			pkEntity.setLostTo(lostList);
		}

		int oldPoints = pkEntity.getPoints();
		updatePoint = updatePoint * -1;
		int newPoint = (oldPoints + updatePoint) > 0 ? (oldPoints + updatePoint)
				: 0;
		pkEntity.setPoints(newPoint);
		entityDao.save(pkEntity);
	}

	/**
	 * 比武获胜,增加五点荣誉值
	 * 
	 * @param playerID
	 */
	public void addReward(String playerID) {
		PkRewardEntity rewardEntity = pkRewardEntityDao
				.getRewardEntity(playerID);
		if (rewardEntity == null) {
			rewardEntity = new PkRewardEntity();
			rewardEntity.setPlayerID(playerID);
		}
		int oldReward = rewardEntity.getReward();
		rewardEntity.setReward(oldReward + 5);
		pkRewardEntityDao.save(rewardEntity);
	}

	/**
	 * 加载可以参加比武的玩家列表
	 */
	public void initPkPlayers(String today) {
		QueryInfo queryInfo = new QueryInfo();
		queryInfo
				.addQueryCondition("level", QueryType.GREATERTHAN_OR_EQUAL, 30);
		List<Object> idList = this.getPkPlayerIDList();
		if (idList != null && idList.size() > 0) {
			queryInfo.addMultipleQueryCondition("playerID", QueryType.NOT_IN,
					idList);
		}
		List<PlayerEntity> playerList = playerEntitiyDAO.queryList(queryInfo); // 查询所有大于30级的玩家
		if (playerList != null && playerList.size() > 0) {
			for (PlayerEntity playerEntity : playerList) {
				String playerID = playerEntity.getKey().toString();
				this.checkPlayerInPkList(playerID, today);
			}
		}
	}

	/**
	 * 查询已经添加到可比武人员表中的玩家ID
	 * 
	 * @return
	 */
	public List<Object> getPkPlayerIDList() {
		List<Object> resultList = null;
		resultList = entityDao.getPkPlayerIDList();
		return resultList;
	}

	/**
	 * 获取玩家比武排名信息
	 * 
	 * @param playerID
	 * @param protocol
	 * @param today
	 *            当前周数
	 */
	public void getPlayerPkRankInfo(String playerID, PkProtocol protocol,
			String weekNum) {
		PkEntity pkEntity = entityDao.getEntity(playerID);
		if (pkEntity == null) {
			pkEntity = new PkEntity();
		}
		Map<String, Pk> items = pkEntity.getItems();
		PkPlayer pkPlayer = new PkPlayer();
		
		if (items == null || items.get(weekNum) == null) { // 本周没有比武记录,为初始值
			pkPlayer.setEnergy(SysConstants.PK_MAX_ENERGY);
			pkPlayer.setNum(SysConstants.PK_MAX_NUM);
		} else {
			Pk pk = items.get(weekNum);
			pkPlayer.setEnergy(SysConstants.PK_MAX_ENERGY - pk.getEnergy());
			pkPlayer.setNum(SysConstants.PK_MAX_NUM - pk.getNum());
		}

		// 查询玩家积分排名
		int points = pkEntity.getPoints();
		long rankNum = this.getPointsRank(points, playerID);
		
		pkPlayer.setPlayerID(playerID);
		pkPlayer.setPoints(points);
		pkPlayer.setRankNum(rankNum);
		PlayerEntity playerEntity = playerEntitiyDAO.getEntity(playerID);
		pkPlayer.setFightValue(playerEntity.getFightValue());
		pkPlayer.setLevel(playerEntity.getLevel());

		// 查询玩家兑换记录
		Map<String, Integer> historyMap = this
				.getExchangeHistoryById(playerID);
		if (historyMap != null && !historyMap.isEmpty()) {
			protocol.setHistoryMap(historyMap);
		}

		// 查询当前荣誉值
		int canUse = this.getRewardNumByID(playerID);
		pkPlayer.setVipLevel(playerEntity.getVipLevel());
		String groupID = playerEntity.getGroupID();
		this.getGroupNameById(groupID, pkPlayer);
		protocol.setCode(0);
		protocol.setPlayerInfo(pkPlayer);
		protocol.setNowReward(canUse);
	}

	/**
	 * 根据军团ID,获取军团名称.封装进PkPlayer中
	 * 
	 * @param groupID
	 * @param pkPlayer
	 */
	public void getGroupNameById(String groupID, PkPlayer pkPlayer) {
		if (groupID != null && !"".equals(groupID)) {
			LegionModule legionModule = ModuleManager.getModule(
					ModuleNames.LegionModule, LegionModule.class);
			LegionEntity legionEntity = legionModule.getLegionEntity(groupID);
			if (legionEntity != null) {
				pkPlayer.setGroupName(legionEntity.getName());
				pkPlayer.setGroupID(groupID);
			}
		}
	}

	/**
	 * 寻找比武对象
	 * 
	 * @param playerID
	 *            玩家id
	 * @param protocol
	 */
	public void findPkPlayers(String playerID, PkProtocol protocol) {
		PkEntity pkEntity = entityDao.getEntity(playerID);
		int myPoints = pkEntity.getPoints();
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.addQueryCondition("points", QueryType.GREATERTHAN_OR_EQUAL,
				myPoints - 100);
		queryInfo.addQueryCondition("points", QueryType.LESSTHAN_OR_EQUAL,
				myPoints + 100);
		queryInfo.addQueryCondition("playerID", QueryType.NOT_EQUAL, playerID);

		List<PkEntity> pkPlayersList = entityDao.queryList(queryInfo);

		// 将达到体力值、比武次数上限的对象去除
		if (pkPlayersList != null && pkPlayersList.size() > 0) {
			// String today = Utilities.getDateTime("yyyyMMdd");
			String weekNum = Utilities.getWeekNumOfYear(new Date());
			for (PkEntity pkEntity2 : pkPlayersList) {
				if (pkEntity2.getItems() != null
						&& pkEntity2.getItems().get(weekNum) != null) {
					Pk pk = pkEntity2.getItems().get(weekNum);
					if (pk.getNum() >= SysConstants.PK_MAX_NUM
							|| pk.getEnergy() >= SysConstants.PK_MAX_ENERGY) {
						pkPlayersList.remove(pkEntity2);
					}
				}
			}
		}

		Map<String, PkPlayer> pkMap = new HashMap<String, PkPlayer>(); // 存放查询到的比武对象

		if (pkPlayersList == null || pkPlayersList.size() == 0) { // 没有合适的比武对象
			protocol.setCode(0);
			protocol.setPkMap(new HashMap<String, PkPlayer>());
		} else if (pkPlayersList.size() >= 1 && pkPlayersList.size() <= 3) { // 合适的比武对象人数少于三人

			// 判断对象是不是在要复仇的列表中
			for (PkEntity fightEntity : pkPlayersList) {
				String fightPlayerID = fightEntity.getKey().toString();
				List<String> lostList = pkEntity.getLostTo();
				if (lostList != null && lostList.contains(fightPlayerID)) {
					continue;
				}
				PkPlayer pkPlayer = getPkPlayerInfo(fightPlayerID, myPoints);
				pkMap.put(fightPlayerID, pkPlayer);

			}

		} else if (pkPlayersList.size() > 3) {
			// 随机获取三个下标,将信息放入map中
			int[] playerIndexs = Utilities.randomCommon(0,
					pkPlayersList.size(), 3);
			for (int i = 0; i < playerIndexs.length; i++) {
				int playerIndex = playerIndexs[i];
				PkEntity toMaEntity = pkPlayersList.get(playerIndex); // 根据下标获得的比武对手
				PkPlayer pkPlayer = getPkPlayerInfo(toMaEntity.getPlayerID(),
						myPoints);
				pkMap.put(toMaEntity.getPlayerID(), pkPlayer);
			}
		}

		// 将对手信息放入protocol中
		if (!pkMap.isEmpty()) {
			protocol.setPkMap(pkMap);
		}
	}

	/**
	 * 查询比武对手信息
	 * 
	 * @param playerID
	 *            对手玩家ID
	 * @param myPoint
	 *            我的积分
	 * @return
	 */
	public PkPlayer getPkPlayerInfo(String playerID, int myPoint) {
		PkPlayer pkPlayer = new PkPlayer();
		PlayerEntity playerEntity = playerEntitiyDAO.getEntity(playerID);
		pkPlayer.setLevel(playerEntity.getLevel());
		pkPlayer.setPlayerID(playerID);
		pkPlayer.setPlayerName(playerEntity.getNickName());
		pkPlayer.setFightValue(playerEntity.getFightValue());
		PkEntity pkEntity = entityDao.getEntity(playerID); // 设置可用比武次数以及可用耐力值
		if (pkEntity == null) {
			pkEntity = new PkEntity();
		}
		Map<String, Pk> items = new HashMap<String, Pk>();
		// String today = Utilities.getDateTime("yyyyMMdd");
		String weekNum = Utilities.getWeekNumOfYear(new Date());
		if (items.get(weekNum) == null || items.isEmpty()) {
			pkPlayer.setEnergy(SysConstants.PK_MAX_ENERGY);
			pkPlayer.setNum(SysConstants.PK_MAX_NUM);
		} else {
			pkPlayer.setEnergy(SysConstants.PK_MAX_ENERGY
					- pkEntity.getItems().get(weekNum).getEnergy());
			pkPlayer.setNum(SysConstants.PK_MAX_NUM
					- -pkEntity.getItems().get(weekNum).getNum());
		}

		pkPlayer.setRankNum(this.getPointsRank(pkEntity.getPoints(), playerID));

		int diffPoint = pkEntity.getPoints() - myPoint; // 根据当前玩家和对手积分分差,判断对手类型
		if (-100 <= diffPoint && diffPoint <= -50) {
			pkPlayer.setType(1); // 弱势对手
		} else if (-49 <= diffPoint && diffPoint <= 50) {
			pkPlayer.setType(2); // 平等对手
		} else if (51 <= diffPoint && diffPoint <= 100) {
			pkPlayer.setType(3); // 强势对手
		}

		// 查找出战阵容
		List<Integer> teamList = this.getTeamList(playerID);
		pkPlayer.setTeamList(teamList);
		return pkPlayer;
	}

	/**
	 * 得到比武应该添加的分数
	 * 
	 * @param playerID
	 * @param pkPlayerID
	 * @return
	 */
	public int getPkAddPoint(String playerID, String pkPlayerID) {
		PkEntity myEntity = entityDao.getEntity(playerID);
		PkEntity hisEntity = entityDao.getEntity(pkPlayerID);
		int mypoints = myEntity.getPoints();
		int hispoints = hisEntity.getPoints();
		int diffPoint = hispoints - mypoints;
		if (-100 <= diffPoint && diffPoint <= -50) {
			return pkScoreDate.getLower(); // 弱势对手
		} else if (-50 < diffPoint && diffPoint <= 50) {
			return pkScoreDate.getNormal(); // 平等对手
		} else if (50 < diffPoint && diffPoint <= 100) {
			return pkScoreDate.getHigher(); // 强势对手
		}
		return 0;
	}

	/**
	 * 查着出战阵容
	 * 
	 * @param playerID
	 * @return
	 */
	public List<Integer> getTeamList(String playerID) {
		HeroModule heroModule = ModuleManager.getModule(ModuleNames.HeroModule,
				HeroModule.class);
		HeroEntity heroEntity = heroModule.getHeroEntity(playerID);
		List<Long> teamList = heroEntity.getTeamList();
		List<Integer> heroTemplateList = new ArrayList<Integer>();
		for (long heroID : teamList) {
			if (heroID != 0) {
				Hero hero = heroEntity.getHeroMap().get(heroID + "");
				if (hero != null) {
					heroTemplateList.add(hero.getTemplateID());
				}
			}
		}
		
		return heroTemplateList;
	}

	/**
	 * 判断玩家是否存在于比武列表中
	 * 
	 * @param playerID
	 */
	public void checkPlayerInPkList(String playerID, String today) {
		PkEntity pkEntity = entityDao.getEntity(playerID);
		if (pkEntity == null) { // 不存在,添加到数据库中
			pkEntity = new PkEntity();
			pkEntity.setPlayerID(playerID);
			pkEntity.setPoints(SysConstants.PK_INIT_POINTS); // 初始1000积分
			pkEntity.setLostTo(new ArrayList<String>());
			pkEntity.setItems(new HashMap<String, Pk>());
			
			long updateTime = System.currentTimeMillis(); // 得到系统时间毫秒数
			pkEntity.setUpdateTime(updateTime);
			entityDao.save(pkEntity);
		}


	}

	/**
	 * 查询玩家积分排名
	 * 
	 * @param points
	 *            玩家积分
	 * @return
	 */
	public long getPointsRank(int points, String playerID) {
		return entityDao.getPointsRank(points, playerID);
	}

	/**
	 * 每周日凌晨1点,比武积分清零,删除复仇列表
	 */
	public void clearPoints() {
		QueryInfo queryInfo = new QueryInfo();
		List<PkEntity> list = entityDao.queryList(queryInfo);
		if (list != null && list.size() > 0) {
			for (PkEntity pkEntity : list) {
				String playerID = pkEntity.getKey().toString();
				entityDao.updateSet(playerID, "points",
						SysConstants.PK_INIT_POINTS); // 每个比武周期,初始积分数为1000
				entityDao
						.updateSet(playerID, "lostTo", new ArrayList<String>());
				entityDao.updateSet(playerID, "items",
						new HashMap<String, Pk>());
			}
		}

	}

	public void sendPkRewardNew() {
		// 1.查询排名前一百的玩家
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.setSize(5000);
		queryInfo.setOrder("-points,+playerID");
		List<PkEntity> list = entityDao.queryList(queryInfo);
		List<PkRewardData> rewardDatas = TemplateManager
				.getTemplateList(PkRewardData.class); // 奖励模版
		// 2.根据排名计算应该添加的荣誉值
		if (list != null && list.size() > 0) {
			List<PkEntity> resultList = null;
			if (list.size() > 300) {
				resultList = list.subList(0, 300);
			} else {
				resultList = list;
			}
			for (int i = 0; i < resultList.size(); i++) {
				PkEntity pkEntity = resultList.get(i);
				String playerID = pkEntity.getKey().toString();
				for (PkRewardData pkReward : rewardDatas) {
					int rangeLow = pkReward.getRangeLow() - 1;
					int rangeHigh = pkReward.getRangeHigh() - 1;
					int pid = pkReward.getPid();
					if (i >= rangeLow && i <= rangeHigh) {
						addReward(playerID, pid);
						break;
					}

				}
			}
		}

		// 清空积分
		this.clearPoints();
	}

	/**
	 * 计算比武排名,发送比武奖励到奖励中心
	 */
	public void sendPkReward() {
		// 1.查询排名前一百的玩家
		QueryInfo queryInfo = new QueryInfo();
		// queryInfo.addQueryCondition("points", QueryType.GREATERTHAN, 0);
		queryInfo.setSize(5000);
		queryInfo.setOrder("-points,+playerID");
		List<PkEntity> list = entityDao.queryList(queryInfo);

		// 2.根据排名计算应该添加的荣誉值
		if (list != null && list.size() > 0) {
			List<PkEntity> resultList = null;
			if (list.size() > 300) {
				resultList = list.subList(0, 300);
			} else {
				resultList = list;
			}
			for (int i = 0; i < resultList.size(); i++) {
				PkEntity pkEntity = resultList.get(i);
				String playerID = pkEntity.getKey().toString();
				if (i == 0) { // 第1名奖励
					addReward(playerID, PkRankType.PK_RANK_TYPE_ONE);
					continue;
				}
				if (i == 1) { // 第2名奖励
					addReward(playerID, PkRankType.PK_RANK_TYPE_TWO);
					continue;
				}

				if (2 <= i && i <= 4) { // 第3-5名奖励
					addReward(playerID, PkRankType.PK_RANK_TYPE_THREE);
					continue;
				}

				if (5 <= i && i <= 9) { // 第6-10名奖励
					addReward(playerID, PkRankType.PK_RANK_TYPE_FOUR);
					continue;
				}
				if (10 <= i && i <= 24) { // 第11-25名奖励
					addReward(playerID, PkRankType.PK_RANK_TYPE_FIVE);
					continue;
				}

				if (25 <= i && i <= 49) { // 第26-50名奖励
					addReward(playerID, PkRankType.PK_RANK_TYPE_SIX);
					continue;
				}

				if (50 <= i && i <= 99) { // 第51-100名奖励
					addReward(playerID, PkRankType.PK_RANK_TYPE_SEVEN);
					continue;
				}

				if (100 <= i && i <= 199) { // 第101-200名奖励
					addReward(playerID, PkRankType.PK_RANK_TYPE_EIGHT);
					continue;
				}

				if (200 <= i && i <= 299) { // 第201-300名奖励
					addReward(playerID, PkRankType.PK_RANK_TYPE_NINE);
					continue;
				}

				// if (500 <= i && i <= 999) { // 第501-1000名奖励
				// addReward(playerID, PkRankType.PK_RANK_TYPE_TEN);
				// continue;
				// }
				//
				// if (1000 <= i && i <= 1999) { // 第1001-2000名奖励
				// addReward(playerID, PkRankType.PK_RANK_TYPE_ELEVEN);
				// continue;
				// }
				//
				// if (2000 <= i && i <= 4999) { // 第2001-5000名奖励
				// addReward(playerID, PkRankType.PK_RANK_TYPE_TWELVE);
				// continue;
				// }
			}
		}

		// 清空积分
		this.clearPoints();
	}

	/**
	 * 发送奖励
	 * 
	 * @param pid
	 *            奖励类型,参见配置表：TournamentRewardPrototype
	 */
	public void addReward(String playerID, int type) {
		// 读取比武系统奖励配置文件
		PkRewardData pkRewardData = TemplateManager.getTemplateData(type,
				PkRewardData.class);
		List<GoodsBean> goodsList = new ArrayList<GoodsBean>();
		// 调用奖励中心接口发送奖励
		String itemID = pkRewardData.getItemID();
		if (itemID.indexOf(",") >= 0) { // 多个奖励物品
			String[] googs = itemID.split(",");
			for (int i = 0; i < googs.length; i++) {
				GoodsBean goodsBean = new GoodsBean();
				String[] good = googs[i].split("=");
				goodsBean.setPid(Integer.valueOf(good[0]));
				goodsBean.setNum(Integer.valueOf(good[1]));
				goodsList.add(goodsBean);
			}
		} else { // 单个奖励物品
			String[] good = itemID.split("=");
			GoodsBean goodsBean = new GoodsBean();
			goodsBean.setPid(Integer.valueOf(good[0]));
			goodsBean.setNum(Integer.valueOf(good[1]));
			goodsList.add(goodsBean);
		}
		if (goodsList != null && goodsList.size() > 0) {
			RewardModule rewardModule = ModuleManager.getModule(
					ModuleNames.RewardModule, RewardModule.class);
			rewardModule.addReward(playerID, goodsList,
					RewardType.contestReward);
		}
	}

	/**
	 * 检查是否可以进行比武
	 * 
	 * @param playerID
	 *            玩家ID
	 * @param pkPlayerID
	 *            比武对象ID
	 * @param protocol
	 *            返回结果,能比武code为1,不能比code参照com.mi.game.defines.ErrorIds中的定义
	 * @return
	 */
	public boolean checkCanPlayPk(String playerID, String pkPlayerID,
			BaseProtocol protocol) {
		boolean flag = true;
		// 1.判断双方是否有足够的耐力值
		VitatlyEntity myVitatly = vitatlyDAO.getEntity(playerID);
		if (myVitatly.getEnergy() < 2) {
			logger.error("没有足够的耐力");
			protocol.setCode(ErrorIds.PK_NOT_ENOUGH_ENERGY_SELF); // 自己没有足够的耐力值
			return false;
		}
		// 2.判断双方是否达到比武次数、当天消耗耐力值上限
		PkEntity myPkEntity = entityDao.getEntity(playerID);
		// String today = Utilities.getDateTime("yyyyMMdd");
		String weekNum = Utilities.getWeekNumOfYear(new Date());
		if (myPkEntity.getItems() != null
				&& myPkEntity.getItems().get(weekNum) != null) {
			Pk pk = myPkEntity.getItems().get(weekNum);
			if (pk.getNum() >= 15) {
				logger.error("比武次数达到上限");
				protocol.setCode(ErrorIds.PK_NUM_REACH_LIMIT_SELF);
				return false;
			}
		}

		// 3.判断双方是不是积分远大于其他玩家
		if (checkPointsIsVeryBig(playerID)) {
			protocol.setCode(ErrorIds.PK_POINT_TOO_BIG);
			return false;
		}
		protocol.setCode(0);
		return flag;
	}

	// 判断用户是否排名第一,并且积分远大于第二名
	public boolean checkPointsIsVeryBig(String playerID) {
		boolean flag = false;
		List<PkEntity> list = entityDao.getListLimtTwo();
		if (list != null && list.size() == 2) {
			PkEntity limtOne = list.get(0);
			PkEntity limtTwo = list.get(1);
			int diffPoint = limtOne.getPoints() - limtTwo.getPoints();
			if (limtOne.getKey().toString().equals(playerID)
					&& diffPoint >= 1000) {
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * 查询玩家复仇信息,等级、昵称、战斗力(积分)、默认阵容
	 * 
	 * @param playerID
	 *            玩家ID
	 */
	public void getRevengeInfo(String playerID, PkProtocol protocol) {
		PkEntity pkEntity = entityDao.getEntity(playerID);
		if (pkEntity != null && pkEntity.getLostTo() != null) {
			List<PkPlayer> revengeList = new ArrayList<PkPlayer>();
			List<String> lostList = pkEntity.getLostTo(); // 得到复仇对象playerID列表
			for (String pkPlayerID : lostList) { // 循环列表,根据玩家ID,查询玩家信息
				PlayerEntity playerEntity = playerEntitiyDAO
						.getEntity(pkPlayerID);
				int level = playerEntity.getLevel(); // 等级
				String nickName = playerEntity.getNickName(); // 昵称
				int fightValue = playerEntity.getFightValue(); // 战斗力
				PkPlayer pkPlayer = new PkPlayer();
				pkPlayer.setLevel(level);
				pkPlayer.setPlayerID(pkPlayerID);
				pkPlayer.setPlayerName(nickName);
				pkPlayer.setFightValue(fightValue);
				// 玩家默认阵容信息
				List<Integer> teamList = getTeamList(pkPlayerID);
				pkPlayer.setTeamList(teamList);
				pkPlayer.setVipLevel(playerEntity.getVipLevel());
				String groupID = playerEntity.getGroupID();
				this.getGroupNameById(groupID, pkPlayer);
				revengeList.add(pkPlayer);
			}
			// 将查询结果放入protocol中
			if (revengeList != null && revengeList.size() > 0) {
				protocol.setPkLostList(revengeList);
			}
		}
	}

	/**
	 * 查询积分排行榜前十名的玩家信息
	 * 
	 * @param protocol
	 */
	public void getPkPointTopTen(PkProtocol protocol) {
		List<PkPlayer> topTen = new ArrayList<PkPlayer>();
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.setOrder("-points,+playerID");
		// queryInfo.setOrder("-updateTime");
		// queryInfo.addQueryCondition("points", QueryType.GREATERTHAN, 0);
		List<PkEntity> listAll = entityDao.queryList(queryInfo);
		List<PkEntity> tenList;
		if (listAll != null && listAll.size() > 10) { // 截取前十名
			tenList = listAll.subList(0, 10);
		} else {
			tenList = listAll;
		}
		if (tenList != null && tenList.size() > 0) {
			int i = 0;
			for (PkEntity pkEntity : tenList) {
				i++;
				String playerID = pkEntity.getKey().toString();
				PkPlayer pkPlayer = this.getTopTenPlayerInfo(playerID);
				// 玩家默认阵容信息
				List<Integer> teamList = getTeamList(playerID);
				pkPlayer.setTeamList(teamList);
				pkPlayer.setRankNum(i);
				topTen.add(pkPlayer);
			}
		}

		if (topTen != null && topTen.size() > 0) {
			protocol.setTopTen(topTen);
		}
	}

	/**
	 * 根据id查询排行榜前十名玩家信息
	 * 
	 * @param playerID
	 *            玩家ID
	 * @return
	 */
	public PkPlayer getTopTenPlayerInfo(String playerID) {
		PkPlayer pkPlayer = new PkPlayer();
		PlayerEntity playerEntity = playerEntitiyDAO.getEntity(playerID);
		pkPlayer.setLevel(playerEntity.getLevel());
		pkPlayer.setVipLevel(playerEntity.getVipLevel());
		pkPlayer.setPlayerID(playerID);
		pkPlayer.setPlayerName(playerEntity.getNickName());
		pkPlayer.setFightValue(playerEntity.getFightValue());
		PkEntity pkEntity = entityDao.getEntity(playerID); // 设置可用比武次数以及可用耐力值
		pkPlayer.setRankNum(this.getPointsRank(pkEntity.getPoints(), playerID));
		pkPlayer.setPoints(pkEntity.getPoints());
		String groupID = playerEntity.getGroupID();
		this.getGroupNameById(groupID, pkPlayer);
		return pkPlayer;
	}

	/**
	 * 检查是否可以兑换比武奖励
	 * 
	 * @param playerID
	 *            玩家ID
	 * @param pid
	 *            要兑换的商品ID
	 * @param num
	 *            兑换数量
	 */
	public boolean exchangePrize(String playerID, Integer pid, int num,
			PkProtocol protocol, IOMessage ioMessage) {
		boolean flag = false;
		// 根据pid,获取物品模版信息
		PkRewardShopData shopData = TemplateManager.getTemplateData(pid,
				PkRewardShopData.class);

		// 获得兑换商品的信息
		GoodsBean exchangeBean = new GoodsBean();
		String[] itemIDS = shopData.getItemID().split("=");
		int itemID = Integer.valueOf(itemIDS[0]);// 兑换的物品id
		int itemNum = Integer.valueOf(itemIDS[1]); // 兑换物品的类型
		exchangeBean.setPid(pid); // 兑换商店模版ID
		exchangeBean.setNum(num * itemNum); // 兑换数量

		GoodsBean useBean = new GoodsBean(); // 荣誉值
		String[] costArr = shopData.getCost().split("=");
		useBean.setPid(Integer.valueOf(costArr[0])); // 消耗物品id
		useBean.setNum(Integer.valueOf(costArr[1])); // 所需数量
		// 是否有足够的荣誉值
		PkRewardEntity rewardEntity = pkRewardEntityDao
				.getRewardEntity(playerID);
		if (rewardEntity == null) { // 荣誉值不足
			protocol.setCode(ErrorIds.PK_EXCHANGE_REWARD_NOTENOUGH);
			return flag;
		}
		if (rewardEntity.getReward() < useBean.getNum() * num) {
			protocol.setCode(ErrorIds.PK_EXCHANGE_REWARD_NOTENOUGH);
			return flag;
		}

		// 查询兑换记录,看是否满足商品兑换条件
		String[] timesArr = shopData.getTimes().split("=");
		int sumarry = Integer.valueOf(timesArr[0]);
		int limitNum = Integer.valueOf(timesArr[1]);
		int code = checkHistory(playerID, sumarry, limitNum,
				exchangeBean.getPid());
		if (code != 0) {
			protocol.setCode(code);
			return false;
		}
		if (num < 0) {
			throw new IllegalArgumentException(ErrorIds.ParamWrong + "");
		}
		// 所有条件都满足,进行兑换,保存兑换信息
		RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
		Map<String, Object> itemMap = new HashMap<String, Object>();
		code = rewardModule.addGoods(playerID, itemID, exchangeBean.getNum(),
				null, true, null, itemMap, ioMessage);
		if (code != 0) {
			protocol.setCode(code);
			return false;
		}

		// 添加兑换记录
		int useReward = useBean.getNum() * num;
		this.saveExchangeEntity(playerID, exchangeBean.getPid(), num,
				useReward, sumarry);

		// 扣除声望
		// rewardModule.useGoods(playerID, useBean.getPid(), useReward, 0, true,
		// null, itemMap, null);
		this.usePkReward(playerID, useReward);

		// 查询兑换历史
		// 查询玩家兑换记录
		PkPlayer pkPlayer = new PkPlayer();
		Map<String, Integer> historyMap = this.getExchangeHistoryById(playerID);
		if (historyMap != null && !historyMap.isEmpty()) {
			protocol.setHistoryMap(historyMap);
		}

		List<GoodsBean> showMap = new ArrayList<GoodsBean>();
		showMap.add(exchangeBean);
		// 查询可用荣誉值
		int canUse = this.getRewardNumByID(playerID);
		protocol.setNowReward(canUse);
		protocol.setItemMap(itemMap);
		protocol.setShowMap(showMap);
		return true;
	}
	
	/**
	 * 根据用户id,查询用户可用荣誉值
	 * 
	 * @param playerID
	 * @return
	 */
	public int getRewardNumByID(String playerID) {
		int reward = 0;
		PkRewardEntity rewardEntity = pkRewardEntityDao
				.getRewardEntity(playerID);
		if (rewardEntity != null) {
			reward = rewardEntity.getReward();
		}
		return reward;
	}

	/**
	 * 根据玩家ID,查询玩家兑换历史
	 * 
	 * @param playerID
	 * @return map,key:物品ID,value:兑换次数
	 */
	public Map<String, Integer> getExchangeHistoryById(String playerID) {
		// 存放物品ID和兑换次数
		Map<String, Integer> map = new HashMap<String, Integer>();
		List<String> daysList = Utilities.getDayOfWeek("yyyyMMdd", 6); // 获取当前周周一到周六的日期
		// 查询每周
		QueryInfo weekQuery = new QueryInfo();
		weekQuery.addQueryCondition("playerID", QueryType.EQUAL, playerID);
		weekQuery.addQueryCondition("limitType", QueryType.EQUAL, 1);
		weekQuery.addMultipleQueryCondition("week", QueryType.EQUAL,
				Utilities.getWeekNumOfYear(new Date()));
		List<PkExchangeHistoryEntity> weekList = this
				.getHistoryByWeek(playerID);
		this.getHistoryNums(map, weekList);
		// 查询每天
		List<PkExchangeHistoryEntity> dayList = this.getHistoryByDay(playerID);
		this.getHistoryNums(map, dayList);
		// 查询only
		List<PkExchangeHistoryEntity> onlyList = this
				.getHistoryByOnly(playerID);
		this.getHistoryNums(map, onlyList);
		return map;
	}

	/**
	 * 封装物品id和历史兑换次数到map
	 * 
	 * @param map
	 *            保存数据的map,key：物品ID;value：兑换次数
	 * @param list
	 *            兑换历史
	 */
	public void getHistoryNums(Map<String, Integer> map,
			List<PkExchangeHistoryEntity> list) {
		if (list != null && list.size() > 0) {
			for (PkExchangeHistoryEntity historyEntity : list) {
				String goodID = historyEntity.getGoodId() + "";
				int num = historyEntity.getNum();
				if (map.get(goodID) != null) {
					num = num + map.get(goodID);
				}
				map.put(goodID + "", num);
			}
		}
	}

	/**
	 * 按周查询兑换记录
	 * 
	 * @return
	 */
	public List<PkExchangeHistoryEntity> getHistoryByWeek(String playerID) {
		List<String> daysList = Utilities.getDayOfWeek("yyyyMMdd", 6); // 获取当前周周一到周六的日期
		// 查询每周
		QueryInfo weekQuery = new QueryInfo();
		weekQuery.addQueryCondition("playerID", QueryType.EQUAL, playerID);
		weekQuery.addQueryCondition("limitType", QueryType.EQUAL, 1);
		weekQuery.addQueryCondition("week", QueryType.EQUAL,
				Utilities.getWeekNumOfYear(new Date()));
		String today = Utilities.getDateTime("yyyyMMdd");
		weekQuery.addQueryCondition("year", QueryType.EQUAL,
				today.substring(0, 4));
		List<PkExchangeHistoryEntity> weekList = historyEntityDao
				.queryList(weekQuery);
		return weekList;
	}

	/**
	 * 按天查询兑换记录
	 * 
	 * @return
	 */
	public List<PkExchangeHistoryEntity> getHistoryByDay(String playerID) {
		String today = Utilities.getDateTime("yyyyMMdd");
		// 查询今天的兑换历史
		QueryInfo dayQuery = new QueryInfo();
		dayQuery.addQueryCondition("playerID", QueryType.EQUAL, playerID);
		dayQuery.addQueryCondition("limitType", QueryType.EQUAL, 2);
		dayQuery.addQueryCondition("day", QueryType.EQUAL, today);
		List<PkExchangeHistoryEntity> dayList = historyEntityDao
				.queryList(dayQuery);
		return dayList;
	}

	/**
	 * 查询历史兑换记录
	 * 
	 * @return
	 */
	public List<PkExchangeHistoryEntity> getHistoryByOnly(String playerID) {
		// 查询每周
		QueryInfo onlyQuery = new QueryInfo();
		onlyQuery.addQueryCondition("playerID", QueryType.EQUAL, playerID);
		onlyQuery.addQueryCondition("limitType", QueryType.EQUAL, 3);
		List<PkExchangeHistoryEntity> onlyList = historyEntityDao
				.queryList(onlyQuery);
		return onlyList;
	}

	/**
	 * 领取荣誉值
	 * 
	 * @param playerID
	 * @param reward
	 */
	public PkRewardEntity getPkReward(String playerID, int reward) {
		PkRewardEntity rewardEntity = pkRewardEntityDao
				.getRewardEntity(playerID);
		if (rewardEntity == null) {
			rewardEntity = new PkRewardEntity();
		}
		int oldReward = rewardEntity.getReward();
		int newReward = oldReward + reward;
		rewardEntity.setPlayerID(playerID);
		rewardEntity.setReward(newReward);
		pkRewardEntityDao.save(rewardEntity);
		return rewardEntity;
	}

	/**
	 * 使用荣誉值
	 * 
	 * @param playerID
	 *            玩家ID
	 * @param useReward
	 *            消耗的荣誉值
	 */
	public PkRewardEntity usePkReward(String playerID, int useReward) {
		PkRewardEntity rewardEntity = pkRewardEntityDao
				.getRewardEntity(playerID);
		int reward = rewardEntity.getReward();
		int newReward = (reward - useReward ) > 0 ? reward-useReward :0;
		rewardEntity.setReward(newReward);
		pkRewardEntityDao.save(rewardEntity);
		return rewardEntity;
	}
	
	/**
	 * 保存兑换记录
	 * 
	 * @param playerID
	 * @param templateID
	 * @param num
	 */
	public void saveExchangeEntity(String playerID, int templateID, int num,
			int useReward, int limitType) {
		PkExchangeHistoryEntity historyEntity = new PkExchangeHistoryEntity();
		String today = Utilities.getDateTime("yyyyMMdd");
		String week = Utilities.getWeekNumOfYear(new Date());
		String year = today.substring(0, 4);
		historyEntity.setDay(today);
		historyEntity.setGoodId(templateID);
		historyEntity.setNum(num);
		historyEntity.setUseReward(useReward);
		historyEntity.setWeek(week);
		historyEntity.setYear(year);
		historyEntity.setLimitType(limitType);
		historyEntity.setPlayerID(playerID);
		historyEntity.setUpdateTime(new Date().getTime());
		historyEntityDao.save(historyEntity);
	}

	/**
	 * 检查是否达到兑换上限
	 * 
	 * @param playerID
	 * @param sumarry
	 * @param num
	 * @return
	 */
	public int checkHistory(String playerID, int sumarry, int num, int googdID) {
		String today = Utilities.getDateTime("yyyyMMdd");
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.addQueryCondition("goodId", QueryType.EQUAL, googdID);
		queryInfo.addQueryCondition("playerID", QueryType.EQUAL, playerID);
		if (sumarry == 1) { // 按周
			queryInfo.addQueryCondition("year", QueryType.EQUAL,
					today.substring(0, 4)); // 年
			queryInfo.addQueryCondition("week", QueryType.EQUAL,
					Utilities.getWeekNumOfYear(new Date()));
		} else if (sumarry == 2) { // 按天
			queryInfo.addQueryCondition("day", QueryType.EQUAL, today);
		}

		List<PkExchangeHistoryEntity> historyEntities = historyEntityDao
				.queryList(queryInfo);
		if (historyEntities == null || historyEntities.size() == 0) {
			return 0;
		}
		int total = 0;
		for (PkExchangeHistoryEntity historyEntity : historyEntities) {
			total += historyEntity.getNum();
		}
		if (num - total <= 0) {
			if (sumarry == 1) {
				return ErrorIds.PK_TOP_WEEK;
			} else if (sumarry == 2) {
				return ErrorIds.PK_TOP_DAY;
			} else if (sumarry == 3) {
				return ErrorIds.PK_TOP_HISTORY;
			}
		}
		return 0;
	}

}
