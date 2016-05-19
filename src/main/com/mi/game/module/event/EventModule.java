package com.mi.game.module.event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.mi.core.dao.KeyGeneratorDAO;
import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.TemplateManager;
import com.mi.core.engine.annotation.Module;
import com.mi.core.job.BaseJob;
import com.mi.core.job.QuartzJobService;
import com.mi.core.pojo.BaseEntity;
import com.mi.core.pojo.KeyGenerator;
import com.mi.core.util.DateTimeUtil;
import com.mi.game.defines.ActionType;
import com.mi.game.defines.ErrorIds;
import com.mi.game.defines.InformationMessageType;
import com.mi.game.defines.KindIDs;
import com.mi.game.defines.ModuleNames;
import com.mi.game.defines.RewardType;
import com.mi.game.defines.SysConstants;
import com.mi.game.module.analyse.AnalyseModule;
import com.mi.game.module.analyse.pojo.AnalyEntity;
import com.mi.game.module.arena.ArenaModule;
import com.mi.game.module.arena.data.RankRewardData;
import com.mi.game.module.base.BaseModule;
import com.mi.game.module.chat.ChatModule;
import com.mi.game.module.drop.DropModule;
import com.mi.game.module.event.dao.EventChickenEntityDao;
import com.mi.game.module.event.dao.EventConfigDao;
import com.mi.game.module.event.dao.EventDailyExpenseEntityDao;
import com.mi.game.module.event.dao.EventDrawIntegralEntityDao;
import com.mi.game.module.event.dao.EventDrawPayEntityDao;
import com.mi.game.module.event.dao.EventExchangeEntityDao;
import com.mi.game.module.event.dao.EventExpenseEntityDao;
import com.mi.game.module.event.dao.EventExploreEntityDao;
import com.mi.game.module.event.dao.EventFortunaGiftsEntityDao;
import com.mi.game.module.event.dao.EventLuckyDrawEntityDao;
import com.mi.game.module.event.dao.EventMonthCardDao;
import com.mi.game.module.event.dao.EventPayEntityDao;
import com.mi.game.module.event.dao.EventPayEveryDayEntityDao;
import com.mi.game.module.event.dao.EventShopEntityDao;
import com.mi.game.module.event.dao.EventTimeLimitDao;
import com.mi.game.module.event.dao.EventTraderEntityDao;
import com.mi.game.module.event.dao.EventUniversalVerfareEntityDao;
import com.mi.game.module.event.dao.EventVipDailyEntityDao;
import com.mi.game.module.event.dao.EventVipDigoShopEntityDao;
import com.mi.game.module.event.dao.EventVipGrowEntityDao;
import com.mi.game.module.event.dao.NewServerEventEntityDao;
import com.mi.game.module.event.dao.WarGodEntityDao;
import com.mi.game.module.event.data.ActiveConsumeData;
import com.mi.game.module.event.data.ActiveDailyRewardData;
import com.mi.game.module.event.data.ActiveData;
import com.mi.game.module.event.data.ActiveFortunaData;
import com.mi.game.module.event.data.ActiveLuckyDrawData;
import com.mi.game.module.event.data.ActiveMessageData;
import com.mi.game.module.event.data.ChargeActiveData;
import com.mi.game.module.event.data.ChargeDayGiftData;
import com.mi.game.module.event.data.ChargeDropData;
import com.mi.game.module.event.data.ChargeGiftData;
import com.mi.game.module.event.data.ConsumeGiftData;
import com.mi.game.module.event.data.DailyPayGiftData;
import com.mi.game.module.event.data.ExchangeData;
import com.mi.game.module.event.data.ExploreData;
import com.mi.game.module.event.data.LimitHeroData;
import com.mi.game.module.event.data.MonthCarData;
import com.mi.game.module.event.data.MysteryItemData;
import com.mi.game.module.event.data.NewServerData;
import com.mi.game.module.event.data.NewServerRewardData;
import com.mi.game.module.event.data.ScoreDropData;
import com.mi.game.module.event.data.ScoreGainData;
import com.mi.game.module.event.data.VipDailyData;
import com.mi.game.module.event.data.VipDigoShopData;
import com.mi.game.module.event.data.VipGrowData;
import com.mi.game.module.event.data.WelfareData;
import com.mi.game.module.event.define.EventConstans;
import com.mi.game.module.event.pojo.DailyEspenseEntity;
import com.mi.game.module.event.pojo.DailyPayEntity;
import com.mi.game.module.event.pojo.DrawItem;
import com.mi.game.module.event.pojo.EventChickenEntity;
import com.mi.game.module.event.pojo.EventConfigEntity;
import com.mi.game.module.event.pojo.EventDailyExpenseEntity;
import com.mi.game.module.event.pojo.EventDefaultEntity;
import com.mi.game.module.event.pojo.EventDrawIntegralEntity;
import com.mi.game.module.event.pojo.EventDrawPayEntity;
import com.mi.game.module.event.pojo.EventExchangeEntity;
import com.mi.game.module.event.pojo.EventExpenseEntity;
import com.mi.game.module.event.pojo.EventExploreEntity;
import com.mi.game.module.event.pojo.EventFortunaGiftsEntity;
import com.mi.game.module.event.pojo.EventLuckyDrawEntity;
import com.mi.game.module.event.pojo.EventMonthCardEntity;
import com.mi.game.module.event.pojo.EventPayEntity;
import com.mi.game.module.event.pojo.EventPayEveryDayEntity;
import com.mi.game.module.event.pojo.EventShopEntity;
import com.mi.game.module.event.pojo.EventTimeLimitEntity;
import com.mi.game.module.event.pojo.EventTraderEntity;
import com.mi.game.module.event.pojo.EventUniversalVerfareEntity;
import com.mi.game.module.event.pojo.EventVipDailyEntity;
import com.mi.game.module.event.pojo.EventVipDigoShopEntity;
import com.mi.game.module.event.pojo.EventVipGrowEntity;
import com.mi.game.module.event.pojo.EventWelfareEntity;
import com.mi.game.module.event.pojo.Exchange;
import com.mi.game.module.event.pojo.ExploreItem;
import com.mi.game.module.event.pojo.LoginPayEntity;
import com.mi.game.module.event.pojo.MysteryItem;
import com.mi.game.module.event.pojo.NewServerEventEntity;
import com.mi.game.module.event.pojo.VipLimitShopEntity;
import com.mi.game.module.event.pojo.WarGodRankEntity;
import com.mi.game.module.event.pojo.WarGodRankInfo;
import com.mi.game.module.event.protocol.EventAllProtocol;
import com.mi.game.module.event.protocol.EventChickenProtocol;
import com.mi.game.module.event.protocol.EventDailyExpenseProtocol;
import com.mi.game.module.event.protocol.EventDrawIntegralProtocol;
import com.mi.game.module.event.protocol.EventDrawPayProtocol;
import com.mi.game.module.event.protocol.EventExchangeProtocol;
import com.mi.game.module.event.protocol.EventExpenseProtocol;
import com.mi.game.module.event.protocol.EventExploreProtocol;
import com.mi.game.module.event.protocol.EventFortunaGiftsProtocol;
import com.mi.game.module.event.protocol.EventLuckyDrawProtocol;
import com.mi.game.module.event.protocol.EventPayEveryDayProtocol;
import com.mi.game.module.event.protocol.EventPayProtocol;
import com.mi.game.module.event.protocol.EventShopProtocol;
import com.mi.game.module.event.protocol.EventTimeLimitProtocol;
import com.mi.game.module.event.protocol.EventTraderProtocol;
import com.mi.game.module.event.protocol.EventUniversalVerfareProtocol;
import com.mi.game.module.event.protocol.EventVipDailyProtocol;
import com.mi.game.module.event.protocol.EventVipDigoShopProtocol;
import com.mi.game.module.event.protocol.EventVipGrowProtocol;
import com.mi.game.module.event.protocol.RankRewardProtocol;
import com.mi.game.module.event.util.EventUtils;
import com.mi.game.module.festival.FestivalModule;
import com.mi.game.module.festival.define.FestivalConstants;
import com.mi.game.module.hero.HeroModule;
import com.mi.game.module.legion.LegionModule;
import com.mi.game.module.legion.pojo.LegionEntity;
import com.mi.game.module.login.LoginModule;
import com.mi.game.module.login.dao.PlayerEntitiyDAO;
import com.mi.game.module.login.pojo.PlayerEntity;
import com.mi.game.module.mailBox.MailBoxModule;
import com.mi.game.module.mainTask.MainTaskModule;
import com.mi.game.module.pay.PayModule;
import com.mi.game.module.pay.pojo.PayEntity;
import com.mi.game.module.pk.data.PkSendRewardData;
import com.mi.game.module.reward.RewardModule;
import com.mi.game.module.reward.data.GoodsBean;
import com.mi.game.module.vip.VipModule;
import com.mi.game.module.vip.data.VipData;
import com.mi.game.module.vip.pojo.VipEntity;
import com.mi.game.module.wallet.WalletModule;
import com.mi.game.module.wallet.pojo.WalletEntity;
import com.mi.game.util.CommonMethod;
import com.mi.game.util.Logs;
import com.mi.game.util.Utilities;

@Module(name = ModuleNames.EventModule, clazz = EventModule.class)
public class EventModule extends BaseModule {

	private static RewardModule rewardModule;
	private static LoginModule loginModule;
	private static LegionModule legionModule;
	private static VipModule vipModule;
	private static PayModule payModule;
	private static AnalyseModule analyseModule;
	private static HeroModule heroModule;
	private static MailBoxModule mailBoxModule;
	private static EventChickenEntityDao chickenEntityDao = EventChickenEntityDao.getInstance();
	private static EventShopEntityDao shopEntityDao = EventShopEntityDao.getInstance();
	private static EventTraderEntityDao traderEntityDao = EventTraderEntityDao.getInstance();
	private static EventVipDailyEntityDao vipDailyEntityDao = EventVipDailyEntityDao.getInstance();
	private static EventVipGrowEntityDao vipGrowEntityDao = EventVipGrowEntityDao.getInstance();
	private static EventExploreEntityDao exploreDao = EventExploreEntityDao.getInstance();
	private static EventExpenseEntityDao expenseDao = EventExpenseEntityDao.getInstance();
	private static EventPayEntityDao eventPayDao = EventPayEntityDao.getInstance();
	private static EventUniversalVerfareEntityDao eventUniversalVerfareDao = EventUniversalVerfareEntityDao.getInstance();
	private static EventLuckyDrawEntityDao eventLuckyDrawDao = EventLuckyDrawEntityDao.getInstance();
	private static EventFortunaGiftsEntityDao eventFortunaGiftsEntityDao = EventFortunaGiftsEntityDao.getInstance();
	private static EventDrawPayEntityDao drawPayDao = EventDrawPayEntityDao.getInstance();
	private static EventDrawIntegralEntityDao drawIntegralDao = EventDrawIntegralEntityDao.getInstance();
	private static EventExchangeEntityDao exchangeDao = EventExchangeEntityDao.getInstance();
	private static EventMonthCardDao monthCardDao = EventMonthCardDao.getInstance();
	private static EventConfigDao configDao = EventConfigDao.getInstance();
	private static EventTimeLimitDao timeLimitDao = EventTimeLimitDao.getInstance();
	private static NewServerEventEntityDao newServerDao = NewServerEventEntityDao.getInstance();
	private final KeyGeneratorDAO keyGeneratorDAO = KeyGeneratorDAO.getInstance();
	private static WarGodEntityDao warGodEntityDao = WarGodEntityDao.getInstance();
	private static EventVipDigoShopEntityDao vipDigoShopEntityDao = EventVipDigoShopEntityDao
			.getInstance(); // vip折扣店购买历史
	private EventPayEveryDayEntityDao eventPayEveryDayDao = EventPayEveryDayEntityDao
			.getInstance(); // 每日充值
	private static EventDailyExpenseEntityDao dailyExpenseEntityDao = EventDailyExpenseEntityDao
			.getInstance();
	private static PlayerEntitiyDAO playerEntitiyDAO = PlayerEntitiyDAO
			.getInstance();
	// 活动配置
	private static Map<Integer, EventConfigEntity> eventConfig;
	// 活动排序
	private static List<EventConfigEntity> eventList;
	// 战神之王排行榜长度
	private static int warGodRankLength = 10;
	// 战神之王实体key
	private String warGodKey = EventConstans.EVENT_TYPE_WARGOD + "";

	
	/**
	 * 获取全部活动信息
	 * 
	 * @param playerID
	 * @param protocol
	 */
	public void eventAll(String playerID, EventAllProtocol protocol) {
		Set<Entry<Integer, EventConfigEntity>> configSet = eventConfig.entrySet();
		for (Entry<Integer, EventConfigEntity> entry : configSet) {
			if (EventUtils.isEventTime(entry.getValue(), EventConstans.YMDHMS)) {
				protocol.addEntity(entry.getKey() + "", initEventEntity(entry.getKey(), playerID, entry.getValue()));
			}
		}
		// 活动顺序
		for (EventConfigEntity event : eventList) {
			if (EventUtils.isEventTime(event, EventConstans.YMDHMS)) {
				protocol.addOrder(event.getPid() + "");
			}
		}
		// //////////////////////////////////////////////
		// 进入活动页面
		JSONObject params = new JSONObject();
		params.put("activityId", protocol.getOrder());
		// 记录统计日志
		analyseModule.analyLog(playerID, "activity_enter", params, null, null, null);
	}

	private BaseEntity initEventEntity(int pid, String playerID, EventConfigEntity eventInfo) {
		switch (pid) {
		case EventConstans.EVENT_TYPE_FIRSTCHARGE:
			return initFirstPayEntity(playerID);
		case EventConstans.EVENT_TYPE_VIPGROW:
			return initVipGrowEntity(playerID);
		case EventConstans.EVENT_TYPE_MONETH_CARD:
			return initMonthCardEntity(playerID);
		case EventConstans.EVENT_TYPE_VIPDAILY:
			return initVipDailyEntity(playerID);
		case EventConstans.EVENT_TYPE_CHICKEN:
			return initChickenEntity(playerID);
		case EventConstans.EVENT_TYPE_TRADER:
			return initTrader(playerID, false);
		case EventConstans.EVENT_TYPE_SHOP:
			return initShop(playerID);
		case EventConstans.EVENT_TYPE_WELFARE:
			return initWelfareEntity(eventInfo);
		case EventConstans.EVENT_TYPE_EXPLORE:
			return initExploreEntity(playerID);
		case EventConstans.EVENT_TYPE_TIME_LIMIT:
			return initEventTimeLimitEntity(playerID);
		case EventConstans.EVENT_TYPE_UNIVERSAL_VERFARE:
			return initEventUniversalVerfareEntity(playerID);					
		case EventConstans.EVENT_TYPE_FORTUNA_GIFTS:
			return initEventFortunaGiftsEntity(playerID);
		case EventConstans.EVENT_TYPE_LUCKY_DRAW:
			return initEventLuckyDrawEntity(playerID);			
		case EventConstans.EVENT_TYPE_PAY:
			return initEventPayEntity(playerID);
		case EventConstans.EVENT_TYPE_EXPENSE:
			return initExpenseEntity(playerID);
		case EventConstans.EVENT_TYPE_DRAW_PAY:
			return initDrawPayEntity(playerID);
		case EventConstans.EVENT_TYPE_DRAW_INTEGRAL:
			return initDrawIntegralEntity(playerID);
		case EventConstans.EVENT_TYPE_EXCHANGE:
			return initExchangeEntity(playerID);
		case EventConstans.EVENT_TYPE_LEVELUP:
			// return initNewServerLevelUpActive(eventInfo, playerID);
			return initNewServerActive(eventInfo);
		case EventConstans.EVENT_TYPE_UPKING:
			return initNewServerActive(eventInfo);
		case EventConstans.EVENT_TYPE_WARGOD:
			return initNewServerActive(eventInfo);
		case EventConstans.EVENT_TYPE_LEGIONKING:
			return initNewServerActive(eventInfo);
		case EventConstans.EVENT_TYPE_PKELITE:
			return initNewServerActive(eventInfo);
		case EventConstans.EVENT_TYPE_KILLMONSTER:
			return initNewServerActive(eventInfo);
		case EventConstans.EVENT_TYPE_DOUBLEEXP:
			return initNewServerActive(eventInfo);
		case EventConstans.EVENT_TYPE_PAYWELFARE:
			return initNewServerActive(eventInfo);
		case EventConstans.EVENT_TYPE_LIMIT_SHOP: // 限时抢购
			return initLimitShopActive(eventInfo);
		case EventConstans.EVENT_TYPE_VIP_DIGO: // VIP折扣
			return initVipLimitShopActive(eventInfo);
		case EventConstans.EVENT_TYPE_PAY_EVERYDAY: // 每日充值
			return initDailyPayActive(eventInfo);
		case EventConstans.EVENT_TYPE_CONSUMED_DAILY: // 每日消费
			return initDailyEspenseActive(eventInfo);
		case EventConstans.EVENT_TYPE_DAILY_LOGIN: // 每日登录
			return initDailyLoginActive(eventInfo);
		case EventConstans.EVENT_TYPE_SPORTS_RANK: // 竞技排行
			return initNewServerActive(eventInfo);
		}
		return null;
	}

	/************ 赵鹏翔 begin **************/

	/**
	 * 将定时发送奖励的方法添加到任务调度器中 9008011:每日充值 9008012：每日消费 9008013:竞技排行
	 */
	@SuppressWarnings("unchecked")
	private void initScheduler(int jobID) {
		PkSendRewardData jobData = TemplateManager.getTemplateData(jobID,
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
					ErrorIds.PKSENDREWARDJJOBDATAWRONG + "");
		}
		quartz.createJob(jobData.getCount(), jobData.getInterval(), timeString,
				classz);
	}

	/**
	 * 发送升级活动奖励
	 */
	public void sendLevelUpRewardNew() {
		EventConfigEntity eventInfo = eventConfig
				.get(EventConstans.EVENT_TYPE_LEVELUP);
		if (eventInfo == null) {
			return;
		}
		int eventDays = 0;
		try {
			String beginStr = eventInfo.getStartTime().substring(0, 10);
			String endStr = eventInfo.getEndTime().substring(0, 10);
			eventDays = Utilities.daysBetweenForStr(beginStr, endStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		if (eventDays == 0) {
			return;
		}
		String desc = eventInfo.getDesc();
		
		// 判断活动是否结束
		if (EventUtils.isEventTime(eventInfo, EventConstans.YMDHMS)
				&& eventInfo.getStatus() == 0) {
			// 发送18级奖励
			NewServerRewardData rewardData1 = getRewardData(10991);
			List listOne = playerEntitiyDAO.getPlayerListByLevel(rewardData1
					.getLevelRequest());
			getPlayerAndRewardList(listOne, rewardData1, 1, desc,eventInfo);

			// 发送26级奖励
			NewServerRewardData rewardData2 = getRewardData(10992);
			List listTwo = playerEntitiyDAO.getPlayerListByLevel(rewardData2
					.getLevelRequest());
			getPlayerAndRewardList(listTwo, rewardData2, 2, desc,eventInfo);

			// 发送40级奖励
			NewServerRewardData rewardData3 = getRewardData(10993);
			List listEnd = playerEntitiyDAO.getPlayerListByLevel(rewardData3
					.getLevelRequest());
			getPlayerAndRewardListFor40(listEnd, rewardData3, 7, desc,
					eventInfo);
		}
	}

	/**
	 * 为四十级玩家发送奖励
	 * 
	 * @param playerList
	 * @param rewardData
	 * @param eventDays
	 * @param desc
	 */
	public void getPlayerAndRewardListFor40(List<String> playerList,
			NewServerRewardData rewardData, int eventDays, String desc,
			EventConfigEntity eventInfo) {

		Set<Entry<Integer, Integer>> set = rewardData.getReward().entrySet();
		List<GoodsBean> goodsList = new ArrayList<GoodsBean>();
		for (Entry<Integer, Integer> entry : set) {
			goodsList.add(new GoodsBean(entry.getKey(), entry.getValue()));
		}

		ActiveMessageData messageData = activeMessageMap
				.get(EventConstans.EVENT_TYPE_LEVELUP);
		List<String> sendList = new ArrayList<String>();

		if (playerList != null && playerList.size() > 0) {
			// 判断今天是不是建号的第一天
			for (String playerID : playerList) {
				try {
					// 判断建号时间是不是在活动期间,今天是不是建号的第七天
					boolean flag = checkAddDay(playerID, eventInfo);
					int addDays = getAddDays(playerID);
					if (flag && addDays == 7) {
						sendList.add(playerID);
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
		
		if (sendList.size() > 0) { // 发送奖励
			Logs.logger.info(desc + "开始发放" + rewardData.getName());
			long nowTime = System.currentTimeMillis();
			for (String playerID : sendList) {
				Logs.logger.info("奖励接收人ID:" + playerID);
				rewardModule.addReward(playerID, goodsList,
						RewardType.event_levelup);
				if (messageData != null) {
					mailBoxModule.sendEventRewardMail(playerID,
							messageData.getTitle(), messageData.getContents());
				}
			}
			Logs.logger.info("奖励发放等级:" + rewardData.getLevelRequest()
					+ ",发放人数:" + sendList.size());
			Logs.logger.info(rewardData.getName() + "奖励发放结束,耗时:"
					+ (System.currentTimeMillis() - nowTime) + "ms");
		}

	}

	/**
	 * 判断建号时间是不是处于活动期间
	 * 
	 * @param eventInfo
	 * @return
	 * @throws ParseException
	 */
	public boolean checkAddDay(String playerID, EventConfigEntity eventInfo)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(EventConstans.YMDHMS);
		AnalyEntity analyEntity = analyseModule.getAnalyEntity(playerID);
		String addTime = analyEntity.getAdd_time();
		Date beginDate = null;
		try {
			beginDate = sdf.parse(addTime);
			Date startTime = sdf.parse(eventInfo.getStartTime());
			Date endTime = sdf.parse(eventInfo.getEndTime());
			if (beginDate.after(startTime) && beginDate.before(endTime)) {
				return true;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}

	public NewServerRewardData getRewardData(int rewardPid) {
		NewServerRewardData rewardData = newServerRewardMap.get(rewardPid);
		return rewardData;
	}

	/**
	 * 查询所有达到等级要求的玩家ID和奖励
	 * 
	 * @param playerList
	 * @param goodsList
	 * @param eventDays
	 *            活动总天数
	 */
	public void getPlayerAndRewardList(List<String> playerList,
			NewServerRewardData rewardData, int eventDays, String desc,
			EventConfigEntity eventInfo) {

		Set<Entry<Integer, Integer>> set = rewardData.getReward().entrySet();
		List<GoodsBean> goodsList = new ArrayList<GoodsBean>();
		for (Entry<Integer, Integer> entry : set) {
			goodsList.add(new GoodsBean(entry.getKey(), entry.getValue()));
		}

		ActiveMessageData messageData = activeMessageMap
				.get(EventConstans.EVENT_TYPE_LEVELUP);
		List<String> sendList = new ArrayList<String>();

		if (playerList != null && playerList.size() > 0) {
			// 判断今天是不是建号的第一天
			for (String playerID : playerList) {
				try {
					int addDays = getAddDays(playerID);
					if (addDays == eventDays
							&& checkAddDay(playerID, eventInfo)) {
						sendList.add(playerID);
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}

		if (sendList.size() > 0) { // 发送奖励
			Logs.logger.info(desc + "开始发放"
					+ rewardData.getName());
			long nowTime = System.currentTimeMillis();
			for (String playerID : sendList) {
				Logs.logger.info("奖励接收人ID:" + playerID);
				rewardModule.addReward(playerID, goodsList,
						RewardType.event_levelup);
				if (messageData != null) {
					mailBoxModule.sendEventRewardMail(playerID,
							messageData.getTitle(), messageData.getContents());
				}
			}
			Logs.logger.info("奖励发放等级:" + rewardData.getLevelRequest()
					+ ",发放人数:" + sendList.size());
			Logs.logger.info(rewardData.getName() + "奖励发放结束,耗时:"
					+ (System.currentTimeMillis() - nowTime) + "ms");
		}
		
	}

	/**
	 * 建号天数
	 * 
	 * @param eventInfo
	 * @return
	 * @throws ParseException
	 */
	public int getAddDays(String playerID) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(EventConstans.YMD);
		AnalyEntity analyEntity = analyseModule.getAnalyEntity(playerID);
		String addTime = analyEntity.getAdd_time().substring(0, 10);
		Date beginDate = null;
		try {
			beginDate = sdf.parse(addTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String today = Utilities.getDateTime(EventConstans.YMD);
		Date nowDate = sdf.parse(today);
		return Utilities.daysBetween(beginDate, nowDate) + 1;
	}

	/**
	 * 判断活动开始几天了
	 * 
	 * @param eventInfo
	 * @return
	 * @throws ParseException
	 */
	public int getActiveBegDays(EventConfigEntity eventInfo)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(EventConstans.YMDHMS);
		String startTime = eventInfo.getStartTime();
		Date beginDate = null;
		try {
			beginDate = sdf.parse(startTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date nowDate = new Date();
		return Utilities.daysBetween(beginDate, nowDate) + 1;
	}

	// 获取开服冲级活动信息
	private EventWelfareEntity initNewServerLevelUpActive(
			EventConfigEntity eventInfo, String playerID) {
		// 获取用户注册时间
		AnalyseModule analyseModule = ModuleManager.getModule(
				ModuleNames.AnalyseModule, AnalyseModule.class);
		AnalyEntity analyEntity = analyseModule.getAnalyEntity(playerID);
		EventWelfareEntity welfareEntity = null;
		if (eventInfo != null) {
			if (newServerMap.containsKey(eventInfo.getPid())) {
				NewServerData newServerData = newServerMap.get(eventInfo
						.getPid());
				welfareEntity = new EventWelfareEntity();
				welfareEntity.setName(newServerData.getName());
				welfareEntity.setDesc(newServerData.getDesc());
				welfareEntity.setContents(newServerData.getContents());
				welfareEntity.setStartTime(DateTimeUtil.getDate(
						analyEntity.getAdd_time()).getTime()); // 开始时间

				// 获取开始时间结束时间相差的天数
				try {
					int diffDay = Utilities.daysBetween(
							eventInfo.getStartTime(), eventInfo.getEndTime());
					Date date = new Date(welfareEntity.getStartTime());
					Date endDate = Utilities.getDateAfter(date, diffDay);
					welfareEntity.setEndTime(endDate.getTime()); // 结束时间为注册时间加上活动天数
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
		return welfareEntity;
	}

	// TODO 获取排行榜奖励物品
	/**
	 * 获取竞技排行奖励物品
	 */
	public void getSportsRankRewardGoods(RankRewardProtocol protocol) {
		ArenaModule arenaModule = ModuleManager.getModule(
				ModuleNames.ArenaModule, ArenaModule.class);
		List<RankRewardData> rewardList = arenaModule.getRankRewardList();
		if (rewardList != null && rewardList.size() > 0) {
			protocol.setRewardList(rewardList);
		}
	}

	/**
	 * vip折扣店
	 * 
	 * @param playerID
	 *            玩家id
	 * @param shopType
	 * @param ioMessage
	 */
	public void vipAgioShop(String playerID, String shopType,
			IOMessage ioMessage) {
		String today = Utilities.getDateTime("yyyyMMdd");
		EventVipDigoShopProtocol protocol = new EventVipDigoShopProtocol();
		PlayerEntity playerEntity = playerEntitiyDAO.getEntity(playerID);
		// 获取玩家vip等级
		int vipLevel = playerEntity.getVipLevel();
		// 1.检查是否有足够的元宝
		WalletModule walletModule = ModuleManager.getModule(
				ModuleNames.WalletModule, WalletModule.class);
		WalletEntity walletEntity = walletModule.getwalletEntity(playerID);

		switch (shopType) {
		case EventConstans.VIP_AGIO_SHOP_INIT: // 获取商店商品

			List<VipDigoShopData> shopList = TemplateManager
					.getTemplateList(VipDigoShopData.class);
			protocol.setShopList(shopList);

			// 查询购买历史
			List<Integer> historyList = vipDigoShopEntityDao.getBuyHistory(
					playerID, today);
			if (historyList != null && historyList.size() > 0) {
				protocol.setHistoryList(historyList);
			}

			ioMessage.setProtocol(protocol);
			break;

		case EventConstans.VIP_AGIO_SHOP_BUY: // 购买商品
			String pid = "";
			if (ioMessage.getInputParse("pid") != null) {
				pid = ioMessage.getInputParse("pid").toString();
			}
			int itemNum = 0;
			if (ioMessage.getInputParse("num") != null) {
				itemNum = Integer.valueOf(ioMessage.getInputParse("num")
						.toString());
			}
			VipDigoShopData shopData = TemplateManager.getTemplateData(
					Integer.valueOf(pid), VipDigoShopData.class);
			long ownGold = walletEntity.getGold(); // 获取玩家身上剩余的金币数量
			// 1.查询玩家身上的金币是否足够
			String[] costArr = shopData.getCost().split("="); // 获得物品ID，数量
			int useGoodsID = Integer.valueOf(costArr[0]); // 需要消耗的物品id
			int useGold = Integer.valueOf(costArr[1]) * itemNum; // 需要消耗的物品数量
			if (ownGold - useGold < 0) {
				protocol.setCode(ErrorIds.NotEnoughGold);
				ioMessage.setProtocol(protocol);
				return;
			}
			// 2.检查vip等级是否达到物品购买要求,获取所购物品的vip限定等级
			Integer vipTemplateID = shopData.getVipLimit();
			VipData vipData = TemplateManager.getTemplateData(vipTemplateID,
					VipData.class);
			int vipLimitLevel = vipData.getLevel();
			if (vipLevel < vipLimitLevel) { // 玩家vip等级不符合要求
				protocol.setCode(ErrorIds.VIP_DIGO_VIPLEVEL_LIMIT);
				ioMessage.setProtocol(protocol);
				return;
			}

			// 获得物品id和数量
			String[] itemArr = shopData.getItemID().split("=");
			int goodsID = Integer.valueOf(itemArr[0]); // 获得的物品id
			int buyNum = itemNum * Integer.valueOf(itemArr[1]); // 获得的物品数量

			// 3.查询购买历史(一样商品一天只能买一次)

			int code = vipDigoShopEntityDao.checkCanBuy(playerID, pid, today);
			if (code != 0) {
				protocol.setCode(code);
				ioMessage.setProtocol(protocol);
				return;
			}
			// 3.扣除元宝
			Map<String, Object> itemMap = new HashMap<String, Object>();
			rewardModule.useGoods(playerID, useGoodsID, useGold, 0L, true,
					null, itemMap, ioMessage);
			// 4.购买物品
			GoodsBean buyGoods = new GoodsBean();
			buyGoods.setPid(goodsID);
			buyGoods.setNum(buyNum);
			Map<String, GoodsBean> showMap = new HashMap<String, GoodsBean>();
			showMap.put("showMap", buyGoods);
			rewardModule.addGoods(playerID, goodsID, buyNum, null, true,
					showMap, itemMap, ioMessage);
			// 5.购买记录保存到数据库
			EventVipDigoShopEntity shopEntity = new EventVipDigoShopEntity();
			shopEntity.setGoodsID(goodsID);
			shopEntity.setBuyNum(buyNum);
			shopEntity.setDay(today);
			shopEntity.setPlayerID(playerID);
			shopEntity.setPrice(useGold);
			shopEntity.setPid(Integer.valueOf(pid));
			shopEntity.setUpdateTime(System.currentTimeMillis());
			vipDigoShopEntityDao.save(shopEntity);

			// 查询购买历史
			List<Integer> historyList1 = vipDigoShopEntityDao.getBuyHistory(
					playerID, today);
			if (historyList1 != null && historyList1.size() > 0) {
				protocol.setHistoryList(historyList1);
			}

			protocol.setCode(0);
			protocol.setItemMap(itemMap);
			ioMessage.setProtocol(protocol);
			break;

		case EventConstans.VIP_AGIO_SHOP_BUY_HISTORY: // 查询购买历史
			// List<Integer> historyList = vipDigoShopEntityDao.getBuyHistory(
			// playerID, today);
			// if (historyList != null && historyList.size() > 0) {
			// protocol.setHistoryList(historyList);
			// }
			// ioMessage.setProtocol(protocol);
			// break;

		case "3": //

			break;
		}
	}

	/**
	 * 每日消费活动数据收集接口
	 * 
	 * @param playerID
	 */
	public void intefaceEventDailyExpense(String playerID, int expenseNum) {
		EventConfigEntity eventInfo = eventConfig
				.get(EventConstans.EVENT_TYPE_CONSUMED_DAILY);
		if (eventInfo != null) {
			if (EventUtils.isEventTime(eventInfo, EventConstans.YMDHMS)) {
				String today = Utilities.getDateTime("yyyyMMdd");
				EventDailyExpenseEntity expenseEntity = initDailyExpenseEntity(
						playerID, today);
				expenseEntity.addExpenseTotal(expenseNum);
				this.saveDailyExpenseEntity(expenseEntity);
			}
		}
	}

	/**
	 * 每日充值活动数据收集接口
	 * 
	 * @param playerID
	 */
	public void intefaceEventPayEveryDay(String playerID, int payNum) {
		EventConfigEntity eventInfo = eventConfig
				.get(EventConstans.EVENT_TYPE_PAY_EVERYDAY);
		if (eventInfo != null) {
			if (EventUtils.isEventTime(eventInfo, EventConstans.YMDHMS)) { // 在活动范围内
				// 1.查询今天有没有充值记录,有的话累加
				EventPayEveryDayEntity everyDayEntity = initEventPayEveryDayEntity(playerID);
				// 2.今天没有充值记录,新增充值记录
				everyDayEntity.addPayTotal(payNum);
				eventPayEveryDayDao.save(everyDayEntity);
			}
		}
	}

	private void initChargeDayGiftMap() {
		List<ChargeDayGiftData> dataList = TemplateManager
				.getTemplateList(ChargeDayGiftData.class);
		for (ChargeDayGiftData data : dataList) {
			chargeDayGiftMap.put(data.getPid(), data);
		}
	}


	public void saveDailyExpenseEntity(EventDailyExpenseEntity expenseEntity) {
		dailyExpenseEntityDao.save(expenseEntity);
	}

	public EventDailyExpenseEntity getEventDailyExpenseEntity(String playerID,
			String day) {
		return dailyExpenseEntityDao.getDailyExpenseEntity(playerID, day);
	}

	/**
	 * 初始化每日消费
	 * 
	 * @param playerID
	 *            玩家ID
	 * @param day
	 *            日期,格式:yyyyMMdd
	 * @return
	 */
	private EventDailyExpenseEntity initDailyExpenseEntity(String playerID,
			String day) {
		EventConfigEntity eventInfo = eventConfig
				.get(EventConstans.EVENT_TYPE_EXPENSE);
		long startTime = DateTimeUtil.getDate(eventInfo.getStartTime())
				.getTime();
		long endTime = DateTimeUtil.getDate(eventInfo.getEndTime()).getTime();
		EventDailyExpenseEntity expenseEntity = getEventDailyExpenseEntity(
				playerID, day);
		if (expenseEntity == null) {
			expenseEntity = new EventDailyExpenseEntity();
			expenseEntity.setPlayerID(playerID);
			expenseEntity.setStartTime(startTime);
			expenseEntity.setEndTime(endTime);
			expenseEntity.setDay(day);
			saveDailyExpenseEntity(expenseEntity);
		}
		return expenseEntity;
	}

	// 初始化每日充值
	private EventPayEveryDayEntity initEventPayEveryDayEntity(String playerID) {
		EventConfigEntity eventInfo = eventConfig
				.get(EventConstans.EVENT_TYPE_PAY);
		long startTime = DateTimeUtil.getDate(eventInfo.getStartTime())
				.getTime();
		long endTime = DateTimeUtil.getDate(eventInfo.getEndTime()).getTime();
		String today = Utilities.getDateTime("yyyyMMdd");
		EventPayEveryDayEntity payEveryDayEntity = eventPayEveryDayDao
				.getEventPayEveryDayEntity(playerID, today);
		if (payEveryDayEntity == null) {
			payEveryDayEntity = new EventPayEveryDayEntity();
			payEveryDayEntity.setPlayerID(playerID);
			payEveryDayEntity.setEndTime(endTime);
			payEveryDayEntity.setStartTime(startTime);
			payEveryDayEntity.setDay(today);
			eventPayEveryDayDao.save(payEveryDayEntity);
		}
		return payEveryDayEntity;
	}

	// 每日消费活动信息
	private LoginPayEntity initDailyLoginActive(EventConfigEntity eventInfo) {
		LoginPayEntity dailyPayEntity = null;
		if (eventInfo != null) {
			if (activeMap.containsKey(eventInfo.getPid())) {
				ActiveData activeData = activeMap.get(eventInfo.getPid());
				dailyPayEntity = new LoginPayEntity();
				dailyPayEntity.setDesc(activeData.getDesc());
				dailyPayEntity.setStartTime(DateTimeUtil.getDate(
						eventInfo.getStartTime()).getTime());
				dailyPayEntity.setEndTime(DateTimeUtil.getDate(
						eventInfo.getEndTime()).getTime());
			}
		}
		return dailyPayEntity;
	}

	// 每日消费活动信息
	private DailyEspenseEntity initDailyEspenseActive(
			EventConfigEntity eventInfo) {
		DailyEspenseEntity dailyPayEntity = null;
		if (eventInfo != null) {
			if (activeMap.containsKey(eventInfo.getPid())) {
				ActiveData activeData = activeMap.get(eventInfo.getPid());
				dailyPayEntity = new DailyEspenseEntity();
				dailyPayEntity.setDesc(activeData.getDesc());
				dailyPayEntity.setStartTime(DateTimeUtil.getDate(
						eventInfo.getStartTime()).getTime());
				dailyPayEntity.setEndTime(DateTimeUtil.getDate(
						eventInfo.getEndTime()).getTime());
			}
		}
		return dailyPayEntity;
	}

	// 每日充值活动信息
	private DailyPayEntity initDailyPayActive(EventConfigEntity eventInfo) {
		DailyPayEntity dailyPayEntity = null;
		if (eventInfo != null) {
			if (activeMap.containsKey(eventInfo.getPid())) {
				ActiveData activeData = activeMap.get(eventInfo.getPid());
				dailyPayEntity = new DailyPayEntity();
				dailyPayEntity.setDesc(activeData.getDesc());
				dailyPayEntity.setStartTime(DateTimeUtil.getDate(
						eventInfo.getStartTime()).getTime());
				dailyPayEntity.setEndTime(DateTimeUtil.getDate(
						eventInfo.getEndTime()).getTime());
			}
		}
		return dailyPayEntity;
	}

	// vip折扣店活动信息
	private VipLimitShopEntity initVipLimitShopActive(
			EventConfigEntity eventInfo) {
		VipLimitShopEntity dailyPayEntity = null;
		if (eventInfo != null) {
			if (activeMap.containsKey(eventInfo.getPid())) {
				ActiveData activeData = activeMap.get(eventInfo.getPid());
				dailyPayEntity = new VipLimitShopEntity();
				dailyPayEntity.setDesc(activeData.getDesc());
				dailyPayEntity.setStartTime(DateTimeUtil.getDate(
						eventInfo.getStartTime()).getTime());
				dailyPayEntity.setEndTime(DateTimeUtil.getDate(
						eventInfo.getEndTime()).getTime());
			}
		}
		return dailyPayEntity;
	}

	// 限时抢购活动信息
	private DailyPayEntity initLimitShopActive(EventConfigEntity eventInfo) {
		DailyPayEntity dailyPayEntity = null;
		if (eventInfo != null) {
			if (activeMap.containsKey(eventInfo.getPid())) {
				ActiveData activeData = activeMap.get(eventInfo.getPid());
				dailyPayEntity = new DailyPayEntity();
				dailyPayEntity.setDesc(activeData.getDesc());
				dailyPayEntity.setStartTime(DateTimeUtil.getDate(
						eventInfo.getStartTime()).getTime());
				dailyPayEntity.setEndTime(DateTimeUtil.getDate(
						eventInfo.getEndTime()).getTime());
			}
		}
		return dailyPayEntity;
	}

	/**
	 * 每日充值活动,领取奖励
	 * 
	 * @param playerID
	 *            玩家id
	 * @param rewardID
	 *            奖励模版id
	 * @param protocol
	 */
	public void eventPayEveryDay(String playerID, String rewardID,
			EventPayEveryDayProtocol protocol, IOMessage ioMessage) {
		if (rewardID == null || rewardID.isEmpty()) {
			protocol.setCode(ErrorIds.REWARD_ID_NOTNULL);
			ioMessage.setProtocol(protocol);
			return;
		}
		// 获取奖励pid
		int rewardIntID = Integer.parseInt(rewardID);
		if (!chargeDayGiftMap.containsKey(rewardIntID)) {
			protocol.setCode(ErrorIds.REWARD_NOTFOUND);
			ioMessage.setProtocol(protocol);
			return;
		}
		// 获取充值奖励实体
		EventPayEveryDayEntity eventPayEntity = initEventPayEveryDayEntity(playerID);
		// 检查领取记录
		if (eventPayEntity.isReward(rewardIntID)) {
			protocol.setCode(ErrorIds.REWARD_ITEM_ISHAS);
			ioMessage.setProtocol(protocol);
			return;
		}
		// 获取奖励内容
		ChargeDayGiftData chargeGiftData = chargeDayGiftMap.get(rewardIntID);
		// 检查领取条件
		if (chargeGiftData.getGold() > eventPayEntity.getPayTotal()) {
			protocol.setCode(ErrorIds.REWARD_NOTENOUGH);
			ioMessage.setProtocol(protocol);
			return;
		}
		// 获取奖励列表
		List<Integer> rewardTypeList = chargeGiftData.getKeys(chargeGiftData
				.getReward());
		List<GoodsBean> goodsList = new ArrayList<GoodsBean>();
		for (Integer rewardType : rewardTypeList) {
			GoodsBean goods = new GoodsBean(rewardType,
					chargeGiftData.getRewardValue(rewardType));
			goodsList.add(goods);
		}
		Map<String, Object> itemMap = new HashMap<String, Object>();
		// 增加物品
		int code = rewardModule.addGoods(playerID, goodsList, true, null,
				itemMap, ioMessage);
		if (code != 0) {
			protocol.setCode(code);
			ioMessage.setProtocol(protocol);
			return;
		}
		// 增加领取记录
		eventPayEntity.addReward(rewardIntID);
		eventPayEveryDayDao.save(eventPayEntity);
		protocol.setRewardList(eventPayEntity.getRewardList());
		protocol.setItemMap(itemMap);
		protocol.setShowMap(goodsList);
		protocol.setPayTotal(eventPayEntity.getPayTotal());
		ioMessage.setProtocol(protocol);
	}
	
	// TODO 发送每日充值奖励
	/**
	 * 查询昨天充值过的玩家,发送昨天未领取的奖励
	 */
	public void sendPayEveryDayRewardForAll() {
		// 判断昨天是不是处于活动范围内
		EventConfigEntity payEvent = eventConfig
				.get(EventConstans.EVENT_TYPE_PAY_EVERYDAY);
		if (payEvent == null) {
			return;
		}
		boolean flag = EventUtils.isEventTimeYesterday(payEvent,
				EventConstans.YMDHMS);
		if (flag) {
			List<String> list = everyDayPayGetPlayerIDForYesterday();
			if (list != null && list.size() > 0) {
				for (String playerID : list) {
					sendPayEveryDayRewardByPlayerID(playerID);
				}
			}
		}
	}

	/**
	 * 查询昨天充值过的玩家ID
	 * 
	 * @return
	 */
	public List<String> everyDayPayGetPlayerIDForYesterday() {
		String yesterday = Utilities.getYesterDay("yyyyMMdd");
		return eventPayEveryDayDao.getYesterDayPayPlayerID(yesterday);
	}

	/**
	 * 每日充值,根据玩家ID，发送奖励
	 * 
	 * @param playerID
	 *            玩家ID
	 * @param eventPID
	 *            活动ID
	 */
	public void sendPayEveryDayRewardByPlayerID(String playerID) {
		// 获取昨天的充值记录
		String yesterday = Utilities.getYesterDay("yyyyMMdd");
		EventPayEveryDayEntity everyDayEntity = eventPayEveryDayDao
				.getEventPayEveryDayEntity(playerID, yesterday);
		if (everyDayEntity == null) { // 没有充值记录
			return;
		}
		// 获得已经领取的充值奖励
		List<Integer> rewardList = everyDayEntity.getRewardList();
		List<Integer> waitList = new ArrayList<Integer>(); // 待领取奖励

		// 循环奖励Map,判断是否领取过
		for (Map.Entry<Integer, ChargeDayGiftData> entry : chargeDayGiftMap
				.entrySet()) {
			int rewardId = entry.getKey();
			if (!rewardList.contains(rewardId)) { // 没有领取过,判断是否符合领取条件
				int type = this.checkCanGetPayEveryDayReward(playerID,
						rewardId, yesterday);
				if (type == 0) { // 可以领取
					waitList.add(rewardId);
				}
			}
		}

		// 获取待领取奖励列表
		if (waitList.size() > 0) { // 有未领取的记录,循环领取
			List<GoodsBean> goodsList = new ArrayList<GoodsBean>();
			for (Integer rewardIntID : waitList) {
				ChargeDayGiftData chargeGiftData = chargeDayGiftMap
						.get(rewardIntID);
				List<Integer> rewardTypeList = chargeGiftData
						.getKeys(chargeGiftData.getReward());
				for (Integer rewardType : rewardTypeList) {
					GoodsBean goods = new GoodsBean(rewardType,
							chargeGiftData.getRewardValue(rewardType));
					goodsList.add(goods);
				}
				everyDayEntity.addReward(rewardIntID); // 添加到已领取奖励列表
			}
			if (goodsList.size() > 0) {
				rewardModule.addReward(playerID, goodsList,
						RewardType.event_pay_everyday); // 将奖励发送到奖励中心

				eventPayEveryDayDao.save(everyDayEntity); // 保存领取记录
			}
		}
	}

	/**
	 * 每日充值,获取当天已领取的奖励列表
	 * 
	 * @param playerID
	 *            玩家ID
	 * @param day
	 *            日期,格式yyyyMMdd
	 */
	public void getPayEveryDayRewardhistory(String playerID, String day,
			EventPayEveryDayProtocol protocol) {
		EventPayEveryDayEntity everyDayEntity = eventPayEveryDayDao
				.getEventPayEveryDayEntity(playerID, day);
		if (everyDayEntity != null && everyDayEntity.getRewardList() != null
				&& everyDayEntity.getRewardList().size() > 0) {
			protocol.setRewardList(everyDayEntity.getRewardList());
		}
	}

	/**
	 * 每日充值,判断是否符合奖励领取条件
	 * 
	 * @param playerID
	 * @param rewardID
	 * @return
	 */
	public int checkCanGetPayEveryDayReward(String playerID, int rewardID,
			String day) {
		// 获取充值奖励实体
		EventPayEveryDayEntity payEveryDayEntity = eventPayEveryDayDao
				.getEventPayEveryDayEntity(playerID, day);
		if (payEveryDayEntity == null) { // 没有充值记录
			return 1;
		}
		// 检查领取记录
		if (payEveryDayEntity.isReward(rewardID)) { // 已经领取过
			return 1;
		}
		// 获取奖励内容
		ChargeDayGiftData chargeGiftData = chargeDayGiftMap.get(rewardID);
		// 检查领取条件
		if (chargeGiftData.getGold() > payEveryDayEntity.getPayTotal()) { // 不符合领取条件
			return 1;
		}
		return 0;
	}

	/**
	 * 每日充值,查询奖励列表,以及已领取奖励
	 * 
	 * @param playerID
	 * @param protocol
	 */
	public void payEveryDayInfoInit(String playerID,
			EventPayEveryDayProtocol protocol) {
		List<DailyPayGiftData> dataList = TemplateManager
				.getTemplateList(DailyPayGiftData.class);
		if (dataList != null && dataList.size() > 0) {
			protocol.setGiftList(dataList);
		}
		String today = Utilities.getDateTime("yyyyMMdd");
		EventPayEveryDayEntity everyDayEntity = eventPayEveryDayDao
				.getEventPayEveryDayEntity(playerID, today);
		int payTotal = 0;
		if (everyDayEntity != null) {
			payTotal = everyDayEntity.getPayTotal();
			protocol.setRewardList(everyDayEntity.getRewardList());
		}
		protocol.setPayTotal(payTotal); // 充值总数
	}

	/**
	 * 每日消费,领取活动奖励
	 * 
	 * @param playerID
	 *            玩家ID
	 * @param rewardID
	 *            奖励模版ID
	 * @param protocol
	 */
	public void eventDailyExpense(String playerID, String rewardID,
			EventDailyExpenseProtocol protocol, IOMessage ioMessage) {		
		if (rewardID == null || rewardID.isEmpty()) {
			protocol.setCode(ErrorIds.REWARD_ID_NOTNULL);
			ioMessage.setProtocol(protocol);
			return;
		}		
		// 获取奖励pid
		int rewardIntID = Integer.parseInt(rewardID);
		if (!activeConsumeMap.containsKey(rewardIntID)) {
			protocol.setCode(ErrorIds.REWARD_NOTFOUND);
			ioMessage.setProtocol(protocol);
			return;
		}		
		// 获取消费送礼实体
		String today = Utilities.getDateTime("yyyyMMdd");
		EventDailyExpenseEntity expenseEntity = initDailyExpenseEntity(
				playerID, today);
		// 检查领取记录
		if (expenseEntity.isReward(rewardIntID)) {
			protocol.setCode(ErrorIds.REWARD_ITEM_ISHAS);
			ioMessage.setProtocol(protocol);
			return;
		}		
		// 获取奖励内容
		ActiveConsumeData activeConsumeData = activeConsumeMap.get(rewardIntID);
		// 检查领取条件
		if (activeConsumeData.getGold() > expenseEntity.getExpenseTotal()) {
			protocol.setCode(ErrorIds.REWARD_NOTENOUGH);
			ioMessage.setProtocol(protocol);
			return;
		}		
		// 获取奖励列表
		List<Integer> rewardTypeList = activeConsumeData.getKeys(activeConsumeData
				.getRewardValues());
		List<GoodsBean> goodsList = new ArrayList<GoodsBean>();
		for (Integer rewardType : rewardTypeList) {
			GoodsBean goods = new GoodsBean(rewardType,
					activeConsumeData.getRewardValue(rewardType));
			goodsList.add(goods);
		}		
		Map<String, Object> itemMap = new HashMap<String, Object>();
		// 增加奖励物品
		int code = rewardModule.addGoods(playerID, goodsList, true, null,
				itemMap, ioMessage);
		if (code != 0) {
			protocol.setCode(code);
			ioMessage.setProtocol(protocol);
			return;
		}		
		// 增加领取记录
		expenseEntity.addReward(rewardIntID);
		saveDailyExpenseEntity(expenseEntity);
		protocol.setRewardList(expenseEntity.getRewardList());
		protocol.setItemMap(itemMap);
		protocol.setExpenseTotal(expenseEntity.getExpenseTotal());
		ioMessage.setProtocol(protocol);
	}

	/**
	 * 每日消费,领取奖励历史
	 * 
	 * @param playerID
	 *            玩家ID
	 */
	public void getDailyExpenseRewardHistory(String playerID, String day,
			EventDailyExpenseProtocol protocol) {
		// 获取昨天的消费
		EventDailyExpenseEntity dailyExpenseEntity = dailyExpenseEntityDao
				.getDailyExpenseEntity(playerID, day);
		if (dailyExpenseEntity == null) { // 没有充值记录
			protocol.setCode(0);
			return;
		}
		// 获得已经领取的充值奖励
		List<Integer> rewardList = dailyExpenseEntity.getRewardList();
		if (rewardList != null && rewardList.size() == 0) {
			protocol.setRewardList(rewardList);
		}
	}

	// TODO 每日消费自动发送昨天未领取的奖励
	/**
	 * 查询昨天消费过的玩家,发送昨天未领取的奖励
	 */
	public void sendDailyExpenseRewardForAll() {
		// 判断昨天是不是处于活动范围内
		EventConfigEntity payEvent = eventConfig
				.get(EventConstans.EVENT_TYPE_CONSUMED_DAILY);
		if (payEvent == null) {
			return;
		}
		boolean flag = EventUtils.isEventTimeYesterday(payEvent,
				EventConstans.YMDHMS);
		if (flag) {
			List<String> list = dailyExpenseGetPlayerIDForYesterday();
			if (list != null && list.size() > 0) {
				for (String playerID : list) {
					sendDailyExpenseReward(playerID);
				}
			}
		}
	}

	/**
	 * 查询昨天消费过的玩家ID
	 * 
	 * @return
	 */
	public List<String> dailyExpenseGetPlayerIDForYesterday() {
		String yesterday = Utilities.getYesterDay("yyyyMMdd");
		return dailyExpenseEntityDao.getYesterDayExpensePlayerID(yesterday);
	}

	/**
	 * 每日消费,发送昨天未领取的奖励
	 * 
	 * @param playerID
	 *            玩家ID
	 * @param eventPID
	 *            活动ID
	 */
	public void sendDailyExpenseReward(String playerID) {
		// 获取昨天的消费
		String yesterday = Utilities.getYesterDay("yyyyMMdd");
		EventDailyExpenseEntity dailyExpenseEntity = dailyExpenseEntityDao
				.getDailyExpenseEntity(playerID, yesterday);

		if (dailyExpenseEntity == null) { // 没有充值记录
			return;
		}
		// 获得已经领取的充值奖励
		List<Integer> rewardList = dailyExpenseEntity.getRewardList();
		List<Integer> waitList = new ArrayList<Integer>(); // 待领取奖励

		// 循环奖励Map,判断是否领取过
		for (Map.Entry<Integer, ActiveConsumeData> entry : activeConsumeMap
				.entrySet()) {
			int rewardId = entry.getKey();
			if (!rewardList.contains(rewardId)) { // 没有领取过,判断是否符合领取条件
				int type = this.checkCanGetDailyExpenseReward(playerID,
						rewardId, yesterday);
				if (type == 0) { // 可以领取
					waitList.add(rewardId);
				}
			}
		}

		// 获取待领取奖励列表
		if (waitList.size() > 0) { // 有未领取的记录,循环领取
			List<GoodsBean> goodsList = new ArrayList<GoodsBean>();
			for (Integer rewardIntID : waitList) {
				ActiveConsumeData consumeGiftData = activeConsumeMap
						.get(rewardIntID);
				List<Integer> rewardTypeList = consumeGiftData
						.getKeys(consumeGiftData.getRewardValues());
				for (Integer rewardType : rewardTypeList) {
					GoodsBean goods = new GoodsBean(rewardType,
							consumeGiftData.getRewardValue(rewardType));
					goodsList.add(goods);
				}
				dailyExpenseEntity.addReward(rewardIntID); // 添加到已领取奖励列表
			}
			if (goodsList.size() > 0) {
				rewardModule.addReward(playerID, goodsList,
						RewardType.event_daily_expense); // 将奖励发送到奖励中心
				saveDailyExpenseEntity(dailyExpenseEntity);// 保存领取记录
			}
		}
	}

	/**
	 * 每日消费,判断是否符合奖励领取条件
	 * 
	 * @param playerID
	 * @param rewardID
	 * @return
	 */
	public int checkCanGetDailyExpenseReward(String playerID, int rewardID,
			String day) {
		// 获取充值奖励实体
		EventDailyExpenseEntity dailyExpenseEntity = dailyExpenseEntityDao
				.getDailyExpenseEntity(playerID, day);
		if (dailyExpenseEntity == null) { // 没有充值记录
			return 1;
		}
		// 检查领取记录
		if (dailyExpenseEntity.isReward(rewardID)) { // 已经领取过
			return 1;
		}
		// 获取奖励内容
		ActiveConsumeData activeConsumeData = activeConsumeMap.get(rewardID);
		// 检查领取条件
		if (activeConsumeData.getGold() > dailyExpenseEntity.getExpenseTotal()) { // 不符合领取条件
			return 1;
		}
		return 0;
	}

	/**
	 * 每日消费活动,查询活动奖励信息以及已领取奖励
	 * 
	 * @param playerID
	 * @param protocol
	 */
	public void dailyExpenseRewardInit(String playerID,
			EventDailyExpenseProtocol protocol) {
		String today = Utilities.getDateTime("yyyyMMdd");
		EventDailyExpenseEntity dailyExpenseEntity = dailyExpenseEntityDao
				.getDailyExpenseEntity(playerID, today);
		int total = 0;
		if (dailyExpenseEntity != null) {
			total = dailyExpenseEntity.getExpenseTotal();
			if (dailyExpenseEntity.getRewardList().size() > 0) {
				protocol.setRewardList(dailyExpenseEntity.getRewardList());
			}
		} else {
			protocol.setRewardList(new ArrayList<Integer>());
		}
		protocol.setExpenseTotal(total);
		// 查询奖励模版
		List<ActiveConsumeData> dataList = TemplateManager
				.getTemplateList(ActiveConsumeData.class);
		if (dataList != null && dataList.size() > 0) {	
			List<ActiveConsumeData> newDataList=new ArrayList<ActiveConsumeData>();
			for(ActiveConsumeData activeConsumeData:dataList){
				ActiveConsumeData newActiveConsumeData=new ActiveConsumeData();
				newActiveConsumeData.setPid(activeConsumeData.getPid());
				newActiveConsumeData.setGold(activeConsumeData.getGold());
				newActiveConsumeData.setName(activeConsumeData.getName());
				newActiveConsumeData.setReward(activeConsumeData.getReward());
				newActiveConsumeData.getRewardValues().clear();
				newDataList.add(newActiveConsumeData);
			}
			protocol.setGiftList(newDataList);
		}
	}

	public void sendSportsRankReward() {
		ArenaModule arenaModule = ModuleManager.getModule(
				ModuleNames.ArenaModule, ArenaModule.class);
		EventConfigEntity eventConfigEntity = eventConfig.get(EventConstans.EVENT_TYPE_SPORTS_RANK);
		if (EventUtils.isEventTime(eventConfigEntity, EventConstans.YMDHMS)) {
			arenaModule.sendTopTwoHundredReward(eventConfigEntity);
		}
	}

	/********** 赵鹏翔 end ********************/
	/**
	 * 限时热卖
	 * 
	 * @param playerID
	 * @param drawType
	 * @param protocol
	 */
	public void timeLimitDraw(String playerID, int drawType, EventTimeLimitProtocol protocol) {
		EventTimeLimitEntity timeLimitEntity = initEventTimeLimitEntity(playerID);

		// 判断活动是否结束
		if (timeLimitEntity == null || System.currentTimeMillis() > timeLimitEntity.getEndTime()) {
			protocol.setCode(ErrorIds.EVENT_NOINTIME);
			return;
		}
		// 抽将类型
		int drawID = limitHeroMap.get(timeLimitEntity.getPid()).getDropID();
		Map<String, Object> itemMap = new HashMap<>();
		int code = 0;
		// 如果是免费抽
		if (drawType == 0) {
			long nextFreeTime = timeLimitEntity.getNextFreeTime();
			if (nextFreeTime > System.currentTimeMillis()) {
				protocol.setCode(ErrorIds.TimeLimitDrawHero_HadReward);
				return;
			} else {
				timeLimitEntity.setNextFreeTime(CommonMethod.getTomorrowFirstTime());
			}
		} else {
			if (!timeLimitEntity.canGiftDraw()) {
				// 减钱
				code = rewardModule.useGoods(playerID, KindIDs.GOLDTYPE, SysConstants.TimeLimitDrawHero_GOLD, 0, true, null, itemMap, null);
				if (code != 0) {
					protocol.setCode(code);
					return;
				}
				// ////
				// // 元宝消耗记录
				// ///
				analyseModule.goldCostLog(playerID, SysConstants.TimeLimitDrawHero_GOLD, 1, SysConstants.TimeLimitDrawHero_GOLD, "timeLimitDraw", "event");

				timeLimitEntity.setBuyNums(timeLimitEntity.getBuyNums() + 1);
				timeLimitEntity.setScore(timeLimitEntity.getScore() + 10); // 增加积分
			} else {
				timeLimitEntity.setGiftNums(timeLimitEntity.getGiftNums() + 1);
			}
			if (timeLimitEntity.getBuyNums() % 11 == 0) { // 紫卡包
				drawID = limitHeroMap.get(timeLimitEntity.getPid()).getSPdropID();
			}
			// 消费抽取,修改时间;免费不修改
			timeLimitEntity.setBuyTime(System.currentTimeMillis());
		}
		List<GoodsBean> goodsList = new ArrayList<GoodsBean>();
		goodsList.add(DropModule.doDrop(drawID));
		// 增加物品
		code = rewardModule.addGoods(playerID, goodsList, true, null, itemMap, null);
		if (code != 0) {
			protocol.setCode(code);
			return;
		}
		saveTimeLimitEntity(timeLimitEntity);
		timeLimitEntity.setRank(timeLimitDao.getRank(timeLimitEntity));
		protocol.setTimeLimitEntity(timeLimitEntity);
		protocol.setItemMap(itemMap);
		protocol.setShowMap(goodsList);
		protocol.setRankList(getRankList(20));
	}

	/**
	 * 道具兑换
	 * 
	 * @param playerID
	 * @param exchangeID
	 * @param exchangeType
	 * @param protocol
	 */
	public void eventExchange(String playerID, String exchangeID, String exchangeType, EventExchangeProtocol protocol, IOMessage ioMessage) {
		EventExchangeEntity exchangeEntity = null;
		if (exchangeID == null || exchangeID.isEmpty()) {
			protocol.setCode(ErrorIds.EXCHANGE_PID_NOTNULL);
			return;
		}
		int exchangeIntID = Integer.parseInt(exchangeID);
		Map<String, Object> itemMap = new HashMap<String, Object>();
		if (exchangeType == null || exchangeType.isEmpty()) {
			if (!exchangeMap.containsKey(exchangeIntID)) {
				protocol.setCode(ErrorIds.EXCHANGE_PID_NOTFOUND);
				return;
			}
			exchangeEntity = initExchangeEntity(playerID);
			Exchange exchange = exchangeEntity.getExchanges().get(exchangeIntID);
			// 是否可以兑换
			if (!exchange.checkExchange()) {
				protocol.setCode(ErrorIds.EXCHANGE_NUM_ENOUGH);
				return;
			}
			// 减物品
			List<GoodsBean> goodsList = new ArrayList<GoodsBean>();
			goodsList.add(new GoodsBean(KindIDs.GOLDTYPE, exchange.getGold()));
			for (GoodsBean goods : exchange.getWantList()) {
				goodsList.add(goods);
			}
			int code = rewardModule.useGoods(playerID, goodsList, 0, true, null, itemMap, ioMessage);
			if (code != 0) {
				protocol.setCode(code);
				return;
			}
			// 加物品
			goodsList.clear();
			goodsList.add(new GoodsBean(exchange.getResult().getPid(), exchange.getResult().getNum()));
			code = rewardModule.addGoods(playerID, goodsList, true, null, itemMap, ioMessage);
			if (code != 0) {
				protocol.setCode(code);
				return;
			}
			protocol.setShowMap(goodsList);

			// ////
			// // 元宝消耗记录
			// ///
			analyseModule.goldCostLog(playerID, exchange.getGold(), 1, exchange.getGold(), "eventExchange", "event");

		} else {
			// 检查刷新type
			if (!exchangeType.equals(EventConstans.EXCHANGE_TYPE)) {
				protocol.setCode(ErrorIds.EXCHANGE_TYPE_ERROR);
				return;
			}
			exchangeEntity = initExchangeEntity(playerID);
			Exchange exchange = exchangeEntity.getExchanges().get(exchangeIntID);
			// 减钱
			int gold = exchange.getRefrushNum() * 20;
			int code = rewardModule.useGoods(playerID, KindIDs.GOLDTYPE, gold, 0, true, null, itemMap, null);
			if (code != 0) {
				protocol.setCode(code);
				return;
			}
			// 刷新兑换公式
			Exchange newExchange = getRandomExchange(exchangeIntID);
			newExchange.setRefrushNum(exchange.getRefrushNum());
			newExchange.addRefrushNum();
			exchangeEntity.getExchanges().put(exchangeIntID, newExchange);

			// ////
			// // 元宝消耗记录
			// ///
			analyseModule.goldCostLog(playerID, gold, exchange.getRefrushNum(), 20, "eventExchange" + EventConstans.EXCHANGE_TYPE, "event");
		}
		saveExchangeEntity(exchangeEntity);
		protocol.setExchangeEntity(exchangeEntity);
		protocol.setItemMap(itemMap);
	}

	/**
	 * 积分抽奖
	 * 
	 * @param playerID
	 * @param protocol
	 */
	public synchronized void eventDrawIntegral(String playerID, EventDrawIntegralProtocol protocol) {
		EventDrawIntegralEntity integralEntity = initDrawIntegralEntity(playerID);
		if (integralEntity.getIntegraTotal() < scoreDropData.getCost()) {
			protocol.setCode(ErrorIds.DRAW_INTEGRAL_NOTENOUGH);
			return;
		}
		DrawItem integralItem = getRandomIntegralItem();
		List<GoodsBean> goodsList = new ArrayList<GoodsBean>();
		goodsList.add(new GoodsBean(integralItem.getItemId(), integralItem.getAmount()));
		Map<String, Object> itemMap = new HashMap<String, Object>();
		// 增加物品
		int code = rewardModule.addGoods(playerID, goodsList, true, null, itemMap, null);
		if (code != 0) {
			protocol.setCode(code);
			return;
		}
		// 增加积分抽奖次数
		integralEntity.setDrawNum(integralEntity.getDrawNum() + 1);
		List<GoodsBean> showGoodsBeanList = getShowIntegralItem(integralItem, 5);
		saveDrawIntegralEntity(integralEntity);
		protocol.setDrawIntegralEntity(integralEntity);
		protocol.setItemMap(itemMap);
		protocol.setShowMap(goodsList);
		protocol.setShowGoodsBeanList(showGoodsBeanList);
	}
	
	/**
	 * 全民福利
	 * 
	 * @param playerID
	 * @param rewardID
	 * @param protocol
	 */
	public void eventUniversalVerfare(String playerID,int type, EventUniversalVerfareProtocol protocol) {
		VipEntity vipEntity = vipModule.initVipEntity(playerID);// 获取玩家vip信息
		int vipLevel=vipEntity.getVipLevel();
		
		EventConfigEntity eventConfigEntity=eventConfig.get(EventConstans.EVENT_TYPE_UNIVERSAL_VERFARE);
		// 获取奖励pid
		int rewardIntID=eventConfigEntity.getInfoPid();
		
		if (rewardIntID == 0) {
			protocol.setCode(ErrorIds.REWARD_ID_NOTNULL);
			return;
		}
		
		
		if (!universalVerfareMap.containsKey(rewardIntID)) {
			protocol.setCode(ErrorIds.REWARD_NOTFOUND);
			return;
		}
		// 获取充值奖励实体
		EventUniversalVerfareEntity eventUniversalVerfareEntity = initEventUniversalVerfareEntity(playerID);
		
		ActiveDailyRewardData activeDailyRewardData=universalVerfareMap.get(rewardIntID);
		
		// 获取奖励列表
		List<Integer> rewardTypeList =new ArrayList<Integer>();
		
		// 判断是普通还是vip 0：普通 1：vip
		if(type==0){
			// 检查领取记录
			if (eventUniversalVerfareEntity.isReward(rewardIntID)) {
				protocol.setCode(ErrorIds.REWARD_ITEM_ISHAS);
				return;
			}
			rewardTypeList=activeDailyRewardData.getKeys(activeDailyRewardData.getItemID());
			// 增加领取记录
			eventUniversalVerfareEntity.addReward(rewardIntID);
			
		}else if(type==1){
			if(vipLevel<activeDailyRewardData.getVipRequest()){
				protocol.setCode(ErrorIds.REWARD_VIPLEVEL_ERROR);
				return;
			}
			// 检查领取记录
			if (eventUniversalVerfareEntity.isVipReward(rewardIntID)) {
				protocol.setCode(ErrorIds.REWARD_ITEM_ISHAS);
				return;
			}
			rewardTypeList=activeDailyRewardData.getKeys(activeDailyRewardData.getVIPItemID());
			// 增加领取记录
			eventUniversalVerfareEntity.addVipReward(rewardIntID);
		}				
						
		List<GoodsBean> goodsList = new ArrayList<GoodsBean>();
		for (Integer rewardType : rewardTypeList) {
			GoodsBean goods = new GoodsBean(rewardType, activeDailyRewardData.getRewardValue(type,rewardType));
			goodsList.add(goods);
		}
		Map<String, Object> itemMap = new HashMap<String, Object>();
		// 增加物品
		int code = rewardModule.addGoods(playerID, goodsList, true, null, itemMap, null);
		if (code != 0) {
			protocol.setCode(code);
			return;
		}
		
		saveEventUniversalVerfareEntity(eventUniversalVerfareEntity);
//		protocol.setEventUniversalVerfareEntity(eventUniversalVerfareEntity);
		protocol.setItemMap(itemMap);
		protocol.setShowMap(goodsList);
		protocol.setRewardList(eventUniversalVerfareEntity.getRewardList());
		protocol.setRewardVipList(eventUniversalVerfareEntity.getRewardVipList());
	}
	
	/**
	 * 财神送礼
	 * 
	 * @param playerID
	 * @param rewardID
	 * @param protocol
	 */
	public void eventFortunaGifts(String playerID,int type, EventFortunaGiftsProtocol protocol) {														
		EventFortunaGiftsEntity fortunaGiftsEntity=initEventFortunaGiftsEntity(playerID);
		long receiveTime = fortunaGiftsEntity.getReceiveTime();// 下次领取时间
		long nowTime = System.currentTimeMillis() / 1000;// 现在时间（秒）
		
		
		int kingID=0;
		int kingValues=0;
		int num = fortunaGiftsEntity.getNum();// 剩下次数
		if(num<=0){
			protocol.setCode(ErrorIds.EVENT_FORTUNA_GIFTS_NUM);
			return;
		}
		ActiveFortunaData activeFortunaData=newActiveFortunaMap.get(13081);
//		newActiveFortunaMap.entrySet()
		// 判断是普通还是type 0：低 1：高
		if(type==0){									
			kingID=KindIDs.SILVERTYPE;
			Map<Integer,Integer> map=activeFortunaData.getLowPriceMap();			
			for(Entry<Integer,Integer> entry:map.entrySet()){
				kingID=entry.getKey();
				kingValues=entry.getValue();
			}			
		}else if(type==1){						
			Map<Integer,Integer> map=activeFortunaData.getHighPriceMap();		
			for(Entry<Integer,Integer> entry:map.entrySet()){
				kingID=entry.getKey();
				kingValues=entry.getValue();
			}						
		}	
		if(nowTime<receiveTime){
			protocol.setCode(ErrorIds.EVENT_FORTUNA_GIFTS_TIME);
			return;
		}
		fortunaGiftsEntity.setGold(fortunaGiftsEntity.getGold()+kingValues);				
		List<GoodsBean> goodsList = new ArrayList<GoodsBean>();		
		Map<String, Object> itemMap = new HashMap<String, Object>();
		int code =rewardModule.useGoods(playerID, kingID, kingValues, 0, true, null, itemMap, null);
		if (code != 0) {
			protocol.setCode(code);
			return;
		}						
		fortunaGiftsEntity.setNum(num-1);
		
        if(fortunaGiftsEntity.getNum()==0){
        	itemMap = new HashMap<String, Object>();
        	GoodsBean goodsBean=new GoodsBean();
        	goodsBean.setPid(kingID);
        	goodsBean.setNum(fortunaGiftsEntity.getGold()*2);
        	goodsList.add(goodsBean);
			// 增加物品
    		code = rewardModule.addGoods(playerID, goodsList, true, null, itemMap, null);
		}
        if(fortunaGiftsEntity.getNum()<=0){
        	fortunaGiftsEntity.setReceiveTime(0);
        }else{
        	fortunaGiftsEntity.setReceiveTime(nowTime+15*60);
        }				
		saveEventFortunaGiftsEntity(fortunaGiftsEntity);
		protocol.setEventFortunaGiftsEntity(fortunaGiftsEntity);
		protocol.setItemMap(itemMap);
		protocol.setShowMap(goodsList);		
	}
	
	/**
	 * 幸运抽奖
	 * 
	 * @param playerID
	 * @param type
	 * @param protocol
	 */
	public void eventLuckyDraw(String playerID,int type, EventLuckyDrawProtocol protocol) {														
		EventLuckyDrawEntity luckyDrawEntity=getEventLuckyDrawEntity(playerID);								
		int overplus = luckyDrawEntity.getOverplus();// 剩下次数
		ActiveLuckyDrawData activeLuckyDrawData=null;
		for(Entry<Integer,ActiveLuckyDrawData> entry:newActiveLuckyDrawMap.entrySet()){			
			activeLuckyDrawData=entry.getValue();
		}
		Map<String, Object> itemMap = new HashMap<String, Object>();		
		int dropID = 0;// 掉落包id
		// 判断是普通还是type 0：免费 1：付费
		if(type==0){												
			dropID=activeLuckyDrawData.getFreeDraw();
			luckyDrawEntity.setOverplus(overplus-1);
			if(luckyDrawEntity.getOverplus()<0){
				protocol.setCode(ErrorIds.EVENT_LUCKY_DRAW_OVERPLUS);
				return;
			}
		}else if(type==1){	
			luckyDrawEntity.setDrawNum(luckyDrawEntity.getDrawNum()+1);
			dropID=activeLuckyDrawData.getPayDraw();
			if(luckyDrawEntity.getDrawNum()>activeLuckyDrawData.getMaxDraw()){
				protocol.setCode(ErrorIds.EVENT_LUCKY_DRAW_MAXDRAW);
				return;
			}
			Map<Integer,Integer> map=activeLuckyDrawData.getPriceMap();
			for(Entry<Integer,Integer> entry:map.entrySet()){				
				int code =rewardModule.useGoods(playerID, entry.getKey(), entry.getValue(), 0, true, null, itemMap, null);
				if (code != 0) {
					protocol.setCode(code);
					return;
				}
			}
			
		}									
		List<GoodsBean> goodsList = new ArrayList<GoodsBean>();										
             	
		/* 抽取物品 */
    	GoodsBean dropBean = DropModule.doDrop(dropID);       	
    	goodsList.add(dropBean);
		// 增加物品
		int code = rewardModule.addGoods(playerID, goodsList, true, null, itemMap, null);
		if (code != 0) {
			protocol.setCode(code);
			return;
		}
						
		saveEventLuckyDrawEntity(luckyDrawEntity);
		protocol.setEventLuckyDrawEntity(luckyDrawEntity);
		protocol.setItemMap(itemMap);
		protocol.setShowMap(goodsList);		
	}

	/**
	 * 充值抽奖
	 * 
	 * @param playerID
	 * @param rewardID
	 * @param drawType
	 * @param protocol
	 */
	public void eventDrawPay(String playerID, String rewardID, String drawType, EventDrawPayProtocol protocol) {
		List<GoodsBean> goodsList = new ArrayList<GoodsBean>();
		Map<String, Object> itemMap = new HashMap<String, Object>();
		EventDrawPayEntity drawPayEntity = null;
		if (drawType == null || drawType.isEmpty()) {
			if (rewardID == null || rewardID.isEmpty()) {
				protocol.setCode(ErrorIds.DRAW_TYPE_NOTNULL);
				return;
			}
			// 抽奖id
			int rewardIntID = Integer.parseInt(rewardID);
			if (!chargeActiveMap.containsKey(rewardIntID)) {
				protocol.setCode(ErrorIds.REWARD_NOTFOUND);
				return;
			}
			drawPayEntity = initDrawPayEntity(playerID);
			if (drawPayEntity.isReward(rewardIntID)) {
				protocol.setCode(ErrorIds.DRAW_PAY_AGAIN);
				return;
			}
			ChargeActiveData chargeActiveData = chargeActiveMap.get(rewardIntID);
			if (drawPayEntity.getPayTotal() < chargeActiveData.getGold()) {
				protocol.setCode(ErrorIds.DRAW_PAY_NOTENOUGH);
				return;
			}
			// 获取掉落配置
			ChargeDropData chargeDropData = chargeDropMap.get(chargeActiveData.getDropID());
			// 生成掉落物品
			DrawItem drawItem = getRandomPayItem(chargeDropData);
			goodsList.add(new GoodsBean(drawItem.getItemId(), drawItem.getAmount()));
			// 增加物品
			int code = rewardModule.addGoods(playerID, goodsList, true, null, itemMap, null);
			if (code != 0) {
				protocol.setCode(code);
				return;
			}
			drawPayEntity.addReward(rewardIntID);
			saveDrawPayEntity(drawPayEntity);
		} else {
			// 领取每日首冲奖励
			if (!drawType.equals(EventConstans.DRAW_TYPE_FIRST)) {
				protocol.setCode(ErrorIds.DRAW_PAY_TYPE_ERROR);
			}
			drawPayEntity = initDrawPayEntity(playerID);
			if (drawPayEntity.getState() != 1) {
				protocol.setCode(ErrorIds.REWARD_NOTENOUGH);
				return;
			}
			goodsList.add(new GoodsBean(KindIDs.SILVERTYPE, EventConstans.DRAW_FIRST_TOTAL));
			int code = rewardModule.addGoods(playerID, goodsList, true, null, itemMap, null);
			if (code != 0) {
				protocol.setCode(code);
				return;
			}
			drawPayEntity.setState(2);
			saveDrawPayEntity(drawPayEntity);
		}
		protocol.setDrawPayEntity(drawPayEntity);
		protocol.setShowMap(goodsList);
		protocol.setItemMap(itemMap);
	}
	
	/**
	 * 获取充值送礼xml中的数据
	 */
	public void eventGetPayData(EventPayProtocol protocol){
		List<ChargeGiftData> dataList = TemplateManager.getTemplateList(ChargeGiftData.class);
		List<ChargeGiftData> newDataList=new ArrayList<ChargeGiftData>();
		
		for(ChargeGiftData chargeGiftData:dataList){
			ChargeGiftData newChargeGiftData=new ChargeGiftData();
			newChargeGiftData.setPid(chargeGiftData.getPid());
			newChargeGiftData.setGold(chargeGiftData.getGold());
			newChargeGiftData.setRewardValues(chargeGiftData.getRewardValues());
			newChargeGiftData.setName(chargeGiftData.getName());
			newDataList.add(newChargeGiftData);			
		}				
		protocol.setChargeGiftList(newDataList);
	}
	
	/**
	 * 获取幸运抽奖xml中的数据
	 */
	public void eventGetLuckyDrawData(EventLuckyDrawProtocol protocol){
		List<ActiveLuckyDrawData> dataList = TemplateManager.getTemplateList(ActiveLuckyDrawData.class);		
		if(dataList!=null&&!dataList.isEmpty()){
			ActiveLuckyDrawData activeLuckyDrawData=dataList.get(0);		
			ActiveLuckyDrawData newActiveLuckyDrawData=new ActiveLuckyDrawData();
			newActiveLuckyDrawData.setPid(activeLuckyDrawData.getPid());
			newActiveLuckyDrawData.setName(activeLuckyDrawData.getName());
			newActiveLuckyDrawData.setPrice(activeLuckyDrawData.getPrice());
			newActiveLuckyDrawData.setMaxDraw(activeLuckyDrawData.getMaxDraw());
			newActiveLuckyDrawData.setShowItem(activeLuckyDrawData.getShowItem());
			newActiveLuckyDrawData.setFreeDraw(activeLuckyDrawData.getFreeDraw());
			newActiveLuckyDrawData.setPayDraw(activeLuckyDrawData.getPayDraw());
			newActiveLuckyDrawData.getPriceMap().clear();
			protocol.setLuckyDrawData(newActiveLuckyDrawData);
		}				
	}
	
	/**
	 * 获取消费送礼xml中的数据
	 */
	public void eventGetExpenseData(EventExpenseProtocol protocol){
		List<ConsumeGiftData> dataList = TemplateManager.getTemplateList(ConsumeGiftData.class);
		List<ConsumeGiftData> newDataList=new ArrayList<ConsumeGiftData>();
		for(ConsumeGiftData consumeGiftData:dataList){
			ConsumeGiftData newConsumeGiftData=new ConsumeGiftData();
			newConsumeGiftData.setPid(consumeGiftData.getPid());
			newConsumeGiftData.setGold(consumeGiftData.getGold());
			newConsumeGiftData.setRewardValues(consumeGiftData.getRewardValues());
			newConsumeGiftData.setName(consumeGiftData.getName());
			newDataList.add(newConsumeGiftData);
		}
		protocol.setConsumeGiftList(newDataList);
	}
	
	/**
	 * 获取充值抽奖xml中的数据
	 */
	public void eventGetDrawPayData(EventDrawPayProtocol protocol){
		List<ChargeActiveData> dataList = TemplateManager.getTemplateList(ChargeActiveData.class);		
		protocol.setDrawPayList(dataList);
	}
	
	/**
	 * 获取全民福利xml中的数据
	 */
	public void eventGetUniversalVerfareData(EventUniversalVerfareProtocol protocol){
		List<ActiveDailyRewardData> dataList = TemplateManager.getTemplateList(ActiveDailyRewardData.class);
		EventConfigEntity eventConfigEntity=eventConfig.get(EventConstans.EVENT_TYPE_UNIVERSAL_VERFARE);
		// 获取奖励pid
		int rewardIntID=eventConfigEntity.getInfoPid();
		ActiveDailyRewardData activeDailyRewardData=new ActiveDailyRewardData();
		ActiveDailyRewardData activeDailyReward=null;
		if(rewardIntID==0){
			activeDailyReward=dataList.get(0);
		}else{
			activeDailyReward=TemplateManager.getTemplateData(rewardIntID, ActiveDailyRewardData.class);
		}
		if(activeDailyReward!=null){
			activeDailyRewardData.setPid(activeDailyReward.getPid());
			activeDailyRewardData.setItemIDValue(activeDailyReward.getItemIDValue());
			activeDailyRewardData.setVIPItemIDValue(activeDailyReward.getVIPItemIDValue());
			activeDailyRewardData.setVipRequest(activeDailyReward.getVipRequest());
		}
		protocol.setDailyRewardData(activeDailyRewardData);
	}
	

	/**
	 * 充值送礼
	 * 
	 * @param playerID
	 * @param rewardID
	 * @param protocol
	 */
	public void eventPay(String playerID, String rewardID, EventPayProtocol protocol) {
		if (rewardID == null || rewardID.isEmpty()) {
			protocol.setCode(ErrorIds.REWARD_ID_NOTNULL);
			return;
		}
		// 获取奖励pid
		int rewardIntID = Integer.parseInt(rewardID);
		if (!chargeGiftMap.containsKey(rewardIntID)) {
			protocol.setCode(ErrorIds.REWARD_NOTFOUND);
			return;
		}
		// 获取充值奖励实体
		EventPayEntity eventPayEntity = initEventPayEntity(playerID);
		// 检查领取记录
		if (eventPayEntity.isReward(rewardIntID)) {
			protocol.setCode(ErrorIds.REWARD_ITEM_ISHAS);
			return;
		}
		// 获取奖励内容
		ChargeGiftData chargeGiftData = chargeGiftMap.get(rewardIntID);
		// 检查领取条件
		if (chargeGiftData.getGold() > eventPayEntity.getPayTotal()) {
			protocol.setCode(ErrorIds.REWARD_NOTENOUGH);
			return;
		}
		// 获取奖励列表
		List<Integer> rewardTypeList = chargeGiftData.getKeys(chargeGiftData.getReward());
		List<GoodsBean> goodsList = new ArrayList<GoodsBean>();
		for (Integer rewardType : rewardTypeList) {
			GoodsBean goods = new GoodsBean(rewardType, chargeGiftData.getRewardValue(rewardType));
			goodsList.add(goods);
		}
		Map<String, Object> itemMap = new HashMap<String, Object>();
		// 增加物品
		int code = rewardModule.addGoods(playerID, goodsList, true, null, itemMap, null);
		if (code != 0) {
			protocol.setCode(code);
			return;
		}
		// 增加领取记录
		eventPayEntity.addReward(rewardIntID);
		saveEventPayEntity(eventPayEntity);
		protocol.setEventPayEntity(eventPayEntity);
		protocol.setItemMap(itemMap);
		protocol.setShowMap(goodsList);
	}		

	/**
	 * 消费送礼
	 * 
	 * @param playerID
	 * @param rewardID
	 * @param protocol
	 */
	public void eventExpense(String playerID, String rewardID, EventExpenseProtocol protocol) {
		if (rewardID == null || rewardID.isEmpty()) {
			protocol.setCode(ErrorIds.REWARD_ID_NOTNULL);
			return;
		}
		// 获取奖励pid
		int rewardIntID = Integer.parseInt(rewardID);
		if (!consumeGiftMap.containsKey(rewardIntID)) {
			protocol.setCode(ErrorIds.REWARD_NOTFOUND);
			return;
		}
		// 获取消费送礼实体
		EventExpenseEntity expenseEntity = initExpenseEntity(playerID);
		// 检查领取记录
		if (expenseEntity.isReward(rewardIntID)) {
			protocol.setCode(ErrorIds.REWARD_ITEM_ISHAS);
			return;
		}
		// 获取奖励内容
		ConsumeGiftData consumeGiftData = consumeGiftMap.get(rewardIntID);
		// 检查领取条件
		if (consumeGiftData.getGold() > expenseEntity.getExpenseTotal()) {
			protocol.setCode(ErrorIds.REWARD_NOTENOUGH);
			return;
		}
		// 获取奖励列表
		List<Integer> rewardTypeList = consumeGiftData.getKeys(consumeGiftData.getReward());
		List<GoodsBean> goodsList = new ArrayList<GoodsBean>();
		for (Integer rewardType : rewardTypeList) {
			GoodsBean goods = new GoodsBean(rewardType, consumeGiftData.getRewardValue(rewardType));
			goodsList.add(goods);
		}
		Map<String, Object> itemMap = new HashMap<String, Object>();
		// 增加奖励物品
		int code = rewardModule.addGoods(playerID, goodsList, true, null, itemMap, null);
		if (code != 0) {
			protocol.setCode(code);
			return;
		}
		// 增加领取记录
		expenseEntity.addReward(rewardIntID);
		saveExpenseEntity(expenseEntity);
		protocol.setExpenseEntity(expenseEntity);
		protocol.setItemMap(itemMap);
		protocol.setShowMap(goodsList);
	}

	/**
	 * 获取福利活动奖励倍数
	 * 
	 * @param pid
	 * @return
	 */
	public int getWelfareReward(int pid) {
		EventConfigEntity eventInfo = eventConfig.get(EventConstans.EVENT_TYPE_WELFARE);
		if (eventInfo != null) {
			if (pid != eventInfo.getInfoPid()) {
				return 1;
			}
			if (!EventUtils.isEventTime(eventInfo, EventConstans.YMDHMS)) {
				return 1;
			}
			if (welfareMap.containsKey(pid)) {
				WelfareData welfareData = welfareMap.get(pid);
				return welfareData.getParam();
			}
		}
		return 1;
	}

	/**
	 * 获取开服活动,奖励翻倍数据
	 * 
	 * @param pid
	 * @return
	 */
	public int getNewServerEventReward(int pid) {
		EventConfigEntity eventInfo = eventConfig.get(pid);
		if (eventInfo != null) {
			if (!EventUtils.isEventTime(eventInfo, EventConstans.YMDHMS)) {
				return 1;
			}
			if (newServerMap.containsKey(pid)) {
				NewServerData newServerData = newServerMap.get(pid);
				return newServerData.getParam();
			}
		}
		return 1;
	}

	// 获取福利活动信息
	private EventWelfareEntity initWelfareEntity(EventConfigEntity eventInfo) {
		EventWelfareEntity welfareEntity = null;
		if (eventInfo != null) {
			if (welfareMap.containsKey(eventInfo.getInfoPid())) {
				WelfareData welfareData = welfareMap.get(eventInfo.getInfoPid());
				welfareEntity = new EventWelfareEntity();
				welfareEntity.setName(welfareData.getName());
				welfareEntity.setDesc(welfareData.getDesc());
				welfareEntity.setContents(welfareData.getContents());
				welfareEntity.setStartTime(DateTimeUtil.getDate(eventInfo.getStartTime()).getTime());
				welfareEntity.setEndTime(DateTimeUtil.getDate(eventInfo.getEndTime()).getTime());
			}
		}
		return welfareEntity;
	}

	// 获取开服活动信息
	private EventWelfareEntity initNewServerActive(EventConfigEntity eventInfo) {
		EventWelfareEntity welfareEntity = null;
		if (eventInfo != null) {
			if (newServerMap.containsKey(eventInfo.getPid())) {
				NewServerData newServerData = newServerMap.get(eventInfo.getPid());
				welfareEntity = new EventWelfareEntity();
				welfareEntity.setName(newServerData.getName());
				welfareEntity.setDesc(newServerData.getDesc());
				welfareEntity.setContents(newServerData.getContents());
				welfareEntity.setStartTime(DateTimeUtil.getDate(eventInfo.getStartTime()).getTime());
				welfareEntity.setEndTime(DateTimeUtil.getDate(eventInfo.getEndTime()).getTime());
			}
		}
		return welfareEntity;
	}

	/**
	 * 皇陵探宝
	 * 
	 * @param playerID
	 * @param exploreType
	 * @param protocol
	 */
	public void eventExplore(String playerID, String exploreType, EventExploreProtocol protocol) {
		if (exploreType == null || exploreType.isEmpty()) {
			protocol.setCode(ErrorIds.EXPLORE_TYPE_NOTNULL);
			return;
		}
		EventExploreEntity exploreEntity = initExploreEntity(playerID);
		Map<String, Object> itemMap = new HashMap<String, Object>();
		// 随机获取皇陵探宝物品
		ExploreItem exploreItem = getRandomExploreItem();
		// 物品列表
		List<GoodsBean> goodsList = new ArrayList<GoodsBean>();
		goodsList.add(new GoodsBean(exploreItem.getItemId(), exploreItem.getAmount()));
		int code = 0;
		switch (exploreType) {
		case EventConstans.EXPLORE_FREE:
			if (exploreEntity.getFreeNum() <= 0) {
				protocol.setCode(ErrorIds.EXPLORE_NUM_ERROR);
				return;
			}
			// 增加物品
			code = rewardModule.addGoods(playerID, goodsList, true, null, itemMap, null);
			if (code != 0) {
				protocol.setCode(code);
				return;
			}
			exploreEntity.setFreeNum(exploreEntity.getFreeNum() - 1);
			break;
		case EventConstans.EXPLORE_GOLD:
			if (exploreEntity.getGoldNum() <= 0) {
				protocol.setCode(ErrorIds.EXPLORE_NUM_ERROR);
				return;
			}
			// 减钱
			code = rewardModule.useGoods(playerID, KindIDs.GOLDTYPE, EventConstans.EXPLORE_PRICE, 0, true, null, itemMap, null);
			if (code != 0) {
				protocol.setCode(code);
				return;
			}
			// 加物品
			code = rewardModule.addGoods(playerID, goodsList, true, null, itemMap, null);
			if (code != 0) {
				protocol.setCode(code);
				return;
			}
			exploreEntity.setGoldNum(exploreEntity.getGoldNum() - 1);

			// ////
			// // 元宝消耗记录
			// ///
			analyseModule.goldCostLog(playerID, EventConstans.EXPLORE_PRICE, 1, EventConstans.EXPLORE_PRICE, "eventExplore" + EventConstans.EXPLORE_GOLD, "event");
			break;
		case EventConstans.EXPLORE_FIVE:
			if (exploreEntity.getGoldNum() < 5) {
				protocol.setCode(ErrorIds.EXPLORE_NUM_ERROR);
				return;
			}
			// 减钱
			code = rewardModule.useGoods(playerID, KindIDs.GOLDTYPE, EventConstans.EXPLORE_PRICE * 5, 0, true, null, itemMap, null);
			if (code != 0) {
				protocol.setCode(code);
				return;
			}
			for (int i = 0; i < 4; i++) {
				exploreItem = getRandomExploreItem();
				goodsList.add(new GoodsBean(exploreItem.getItemId(), exploreItem.getAmount()));
			}
			code = rewardModule.addGoods(playerID, goodsList, true, null, itemMap, null);
			if (code != 0) {
				protocol.setCode(code);
				return;
			}
			exploreEntity.setGoldNum(exploreEntity.getGoldNum() - 5);

			// ////
			// // 元宝消耗记录
			// ///
			analyseModule.goldCostLog(playerID, EventConstans.EXPLORE_PRICE, 1, EventConstans.EXPLORE_PRICE, "eventExplore" + EventConstans.EXPLORE_GOLD, "event");

			break;
		}
		protocol.setItemMap(itemMap);
		protocol.setShowMap(goodsList);
		protocol.setExploreEntity(exploreEntity);
		saveExploreEntity(exploreEntity);
	}

	/**
	 * 领取vip成长礼包
	 */
	public void eventVipGrow(String playerID, String eventType, String rewardID, EventVipGrowProtocol protocol) {
		VipEntity vipEntity = vipModule.initVipEntity(playerID);
		if (vipEntity.getVipLevel() < EventConstans.VIP_GROW_BUY_LEVEL) {
			protocol.setCode(ErrorIds.REWARD_VIPLEVEL_ERROR);
			return;
		}
		Map<String, Object> itemMap = new HashMap<String, Object>();
		EventVipGrowEntity vipGrowEntity = getVipGrowEntity(playerID);
		int code = 0;
		switch (eventType) {
		case EventConstans.VIP_GROW_TYPE_BUY:
			if (vipGrowEntity != null) {
				protocol.setCode(ErrorIds.MYSTERY_BUYAGAIN);
				return;
			}
			vipGrowEntity = new EventVipGrowEntity();
			vipGrowEntity.setPlayerID(playerID);
			vipGrowEntity.setDateTime(Utilities.getDateTime());
			code = rewardModule.useGoods(playerID, KindIDs.GOLDTYPE, EventConstans.VIP_GROW_BUY_GOLD, 0, true, null, itemMap, null);
			if (code != 0) {
				protocol.setCode(code);
				return;
			}
			// ////
			// // 元宝消耗记录
			// ///
			analyseModule.goldCostLog(playerID, EventConstans.VIP_GROW_BUY_GOLD, 1, EventConstans.VIP_GROW_BUY_GOLD, "eventVipGrow", "event");
			break;
		case EventConstans.VIP_GROW_TYPE_GET:
			if (vipGrowEntity == null) {
				protocol.setCode(ErrorIds.REWARD_VIPGROW_NOTBUY);
				return;
			}
			if (rewardID == null || rewardID.isEmpty()) {
				protocol.setCode(ErrorIds.MYSTERY_NOTHING);
				return;
			}
			int rewardIntID = Integer.parseInt(rewardID);
			if (vipGrowEntity.isReward(rewardIntID)) {
				protocol.setCode(ErrorIds.REWARD_ISGET);
				return;
			}
			Set<Entry<Integer, VipGrowData>> set = vipGrowMap.entrySet();
			VipGrowData vipGrowData = null;
			for (Entry<Integer, VipGrowData> entry : set) {
				VipGrowData temp = entry.getValue();
				if (temp.getPid() == rewardIntID) {
					vipGrowData = temp;
					break;
				}
			}
			PlayerEntity playerEntity = loginModule.getPlayerEntity(playerID);
			if (playerEntity.getLevel() < vipGrowData.getLevel()) {
				protocol.setCode(ErrorIds.REWARD_LEVELWRONG);
				return;
			}
			List<Integer> rewardList = vipGrowData.getKeys(vipGrowData.getItemID());
			List<GoodsBean> goodsList = new ArrayList<GoodsBean>();
			for (Integer rewardType : rewardList) {
				int rewardValue = vipGrowData.getValue(rewardType);
				goodsList.add(new GoodsBean(rewardType, rewardValue));
			}
			code = rewardModule.addGoods(playerID, goodsList, true, null, itemMap, null);
			if (code != 0) {
				protocol.setCode(code);
				return;
			}
			vipGrowEntity.addRewardID(rewardIntID);
			break;
		}
		saveVipGrowEntity(vipGrowEntity);
		protocol.setVipGrowEntity(vipGrowEntity);
		protocol.setItemMap(itemMap);
	}

	/**
	 * 领取vip每日礼包
	 * 
	 * @param playerID
	 * @param protocol
	 */
	public void eventVipDaily(String playerID, EventVipDailyProtocol protocol) {
		VipEntity vipEntity = vipModule.initVipEntity(playerID);
		if (vipEntity.getVipLevel() == 0) {
			protocol.setCode(ErrorIds.REWARD_VIPLEVEL_ERROR);
			return;
		}
		EventVipDailyEntity vipDailyEntity = getVipDailyEntity(playerID);
		if (vipDailyEntity == null) {
			vipDailyEntity = new EventVipDailyEntity();
			vipDailyEntity.setPlayerID(playerID);
		}
		// 检查当日礼包是否已领取
		if (vipDailyEntity.isReward()) {
			protocol.setCode(ErrorIds.REWARD_ISGET);
			return;
		}
		// 根据vip等级获取礼包内容
		Set<Entry<Integer, VipDailyData>> set = vipDailyMap.entrySet();
		VipDailyData vipDailyData = null;
		for (Entry<Integer, VipDailyData> entry : set) {
			VipDailyData temp = entry.getValue();
			if (temp.getLevel() == vipEntity.getVipLevel()) {
				vipDailyData = temp;
				break;
			}
		}
		List<Integer> rewardList = vipDailyData.getKeys(vipDailyData.getItemID());
		Map<String, Object> itemMap = new HashMap<String, Object>();
		List<GoodsBean> goodsList = new ArrayList<GoodsBean>();
		for (Integer rewardType : rewardList) {
			int rewatdValue = vipDailyData.getValue(rewardType);
			goodsList.add(new GoodsBean(rewardType, rewatdValue));
		}
		int code = rewardModule.addGoods(playerID, goodsList, true, null, itemMap, null);
		if (code != 0) {
			protocol.setCode(code);
			return;
		}
		saveVipDailyEntity(vipDailyEntity);
		protocol.setVipDailyEntity(vipDailyEntity);
		protocol.setItemMap(itemMap);
	}

	/**
	 * 神秘商店
	 * 
	 * @param playerID
	 * @param shopType
	 * @param itemID
	 * @param protocol
	 */
	public void eventShop(String playerID, String shopType, String itemID, EventShopProtocol protocol) {
		Map<String, Object> itemMap = new HashMap<String, Object>();
		EventShopEntity shopEntity = getShopEntity(playerID);
		if (shopType == null || shopType.isEmpty()) {
			protocol.setCode(ErrorIds.MYSTERY_TYPE_NOTNULL);
			return;
		}
		int code = 0;
		switch (shopType) {
		case EventConstans.MYSTERY_TYPE_BUY:
			if (itemID == null || itemID.isEmpty()) {
				protocol.setCode(ErrorIds.MYSTERY_ID_NOTNULL);
				return;
			}
			int itemIntID = Integer.parseInt(itemID);
			// 检查商店是否存在物品
			if (!shopEntity.isHasShopItem(itemIntID)) {
				protocol.setCode(ErrorIds.MYSTERY_NOTHING);
				protocol.setShopEntity(shopEntity);
				return;
			}
			// 检查是否已购买
			if (shopEntity.isBuyShopItem(itemIntID)) {
				protocol.setCode(ErrorIds.MYSTERY_BUYAGAIN);
				return;
			}
			// 获取物品信息
			MysteryItem item = shopEntity.getShopItemByID(itemIntID);
			// 减少货币
			code = rewardModule.useGoods(playerID, item.getCoinType(), item.getCoinValue(), 0, true, null, itemMap, null);
			if (code != 0) {
				protocol.setCode(code);
				return;
			}
			// 增加物品
			code = rewardModule.addGoods(playerID, item.getItemID(), item.getItemNum(), null, true, null, itemMap, null);
			if (code != 0) {
				protocol.setCode(code);
				return;
			}
			// 增加购买记录
			shopEntity.addBuyShopItem(itemIntID);
			MainTaskModule mainTaskModule = ModuleManager.getModule(ModuleNames.MainTaskModule, MainTaskModule.class);
			mainTaskModule.updateTaskByActionType(playerID, ActionType.STONESHOPEXCHAGE, 0, null);
			break;
		case EventConstans.MYSTERY_TYPE_REFRESH:
			if (shopEntity.getRefresh() == 0) {
				protocol.setCode(ErrorIds.MYSTERY_REFRESH_ZERO);
				return;
			}
			// 减少刷新次数
			shopEntity.reduceRefresh();
			// 记录刷新时间
//			shopEntity.setLastRefreshTime();
			// 刷新商店物品,购买记录
			shopEntity.initShopItems(mysteryItemMap.get(EventConstans.MYSTERY_SHOP));
			break;
		case EventConstans.MYSTERY_TYPE_GOLD_REFRESH:
			VipEntity vipEntity = vipModule.initVipEntity(playerID);
			int refreshGold = vipModule.permissionPrice(vipEntity, EventConstans.VIP_MYSTERY_SHOP_REFRESH);
			// 减少vip特权次数
			vipEntity = vipModule.reducePermissionValue(vipEntity, EventConstans.VIP_MYSTERY_SHOP_REFRESH);
			if (vipEntity == null) {
				protocol.setCode(ErrorIds.MYSTERY_REFRESH_ZERO);
				return;
			}
			// 减少金币
			code = rewardModule.useGoods(playerID, KindIDs.GOLDTYPE, refreshGold, 0, true, null, itemMap, null);
			if (code != 0) {
				protocol.setCode(code);
				return;
			}
			// ////
			// // 元宝消耗记录
			// ///
			analyseModule.goldCostLog(playerID, refreshGold, 1, refreshGold, "eventShop" + EventConstans.MYSTERY_TYPE_GOLD_REFRESH, "event");

			// 刷新商店物品,购买记录
			shopEntity.initShopItems(mysteryItemMap.get(EventConstans.MYSTERY_SHOP));
			protocol.setVipEntity(vipEntity);
			break;
		case EventConstans.MYSTERY_TYPE_ITEM_REFRESH:
			// 使用刷新令
			code = rewardModule.useGoods(playerID, EventConstans.MYSTERY_ITEM_REFRESH, 1, 0, true, null, itemMap, null);
			if (code != 0) {
				protocol.setCode(code);
				return;
			}
			// 刷新商店物品,购买记录
			shopEntity.initShopItems(mysteryItemMap.get(EventConstans.MYSTERY_SHOP));
			break;
		case EventConstans.MYSTERY_TYPE_RREE_GET:
			// 初始化商店信息
			shopEntity = initShop(playerID);
			break;
		}
		// 获取免费次数不保存
		if (!EventConstans.MYSTERY_TYPE_RREE_GET.equals(shopType)) {
			saveShopEntity(shopEntity);
		}
		protocol.setShopEntity(shopEntity);
		protocol.setItemMap(itemMap);
	}

	/**
	 * 增加神秘商人出现时间
	 * 
	 * @param playerID
	 * @param existTime
	 *            -1表示永久
	 * @return
	 */
	public EventTraderEntity eventTrader(String playerID, long existTime) {
		EventTraderEntity traderEntity = initTrader(playerID, true);
		if (existTime != 0) {
			if (existTime == -1) {
				traderEntity.setExistTime(existTime);
			} else {
				traderEntity.addExistTime(existTime);
			}
			saveTraderEntity(traderEntity);
		}
		return traderEntity;
	}

	/**
	 * 神秘商人购买
	 * 
	 * @param playerID
	 * @param traderType
	 * @param itemID
	 * @param protocol
	 */
	public void eventTrader(String playerID, String traderType, String itemID, EventTraderProtocol protocol) {
		Map<String, Object> itemMap = new HashMap<String, Object>();
		EventTraderEntity traderEntity = getTraderEntity(playerID);
		if (traderType == null || traderType.isEmpty()) {
			protocol.setCode(ErrorIds.MYSTERY_TYPE_NOTNULL);
			return;
		}
		int code = 0;
		switch (traderType) {
		case EventConstans.MYSTERY_TYPE_BUY:
			if (itemID == null || itemID.isEmpty()) {
				protocol.setCode(ErrorIds.MYSTERY_ID_NOTNULL);
				return;
			}
			if (traderEntity.getExistTime() == 0) {
				protocol.setCode(ErrorIds.MYSTERY_TRADER_NOTHING);
				return;
			}
			int itemIntID = Integer.parseInt(itemID);
			// 检查商人是否存在物品
			if (!traderEntity.isHasTraderItem(itemIntID)) {
				protocol.setCode(ErrorIds.MYSTERY_NOTHING);
				protocol.setTraderEntity(traderEntity);
				return;
			}
			// 检查是否已购买
			if (traderEntity.isBuyTraderItem(itemIntID)) {
				protocol.setCode(ErrorIds.MYSTERY_BUYAGAIN);
				return;
			}
			// 获取物品信息
			MysteryItem item = traderEntity.getTraderItemByID(itemIntID);
			// 减少货币
			code = rewardModule.useGoods(playerID, item.getCoinType(), item.getCoinValue(), 0, true, null, itemMap, null);
			if (code != 0) {
				protocol.setCode(code);
				return;
			}
			// 增加物品
			code = rewardModule.addGoods(playerID, item.getItemID(), item.getItemNum(), null, true, null, itemMap, null);
			if (code != 0) {
				protocol.setCode(code);
				return;
			}
			// 增加购买记录
			traderEntity.addTraderItem(itemIntID);
			break;
		case EventConstans.MYSTERY_TYPE_GOLD_REFRESH:
			VipEntity vipEntity = vipModule.initVipEntity(playerID);
			// 获取刷新价格
			int goldRefresh = vipModule.permissionPrice(vipEntity, EventConstans.VIP_MYSTERY_TRADER_REFRESH);
			vipEntity = vipModule.reducePermissionValue(vipEntity, EventConstans.VIP_MYSTERY_TRADER_REFRESH);
			if (vipEntity == null) {
				protocol.setCode(ErrorIds.MYSTERY_REFRESH_ZERO);
				return;
			}
			// 减少金币
			code = rewardModule.useGoods(playerID, KindIDs.GOLDTYPE, goldRefresh, 0, true, null, itemMap, null);
			if (code != 0) {
				protocol.setCode(code);
				return;
			}

			// ////
			// // 元宝消耗记录
			// ///
			analyseModule.goldCostLog(playerID, goldRefresh, 1, goldRefresh, "eventTrader" + EventConstans.MYSTERY_TYPE_GOLD_REFRESH, "event");

			// 刷新商店物品,购买记录
			traderEntity.initTraderItems(mysteryItemMap.get(EventConstans.MYSTERY_TRADER));
			protocol.setVipEntity(vipEntity);
			break;
		case EventConstans.MYSTERY_ADD_EXISITIME:
			traderEntity.setExistTime(EventConstans.MYSTERY_TRADER_EXISTTIME);
			break;
		}
		saveTraderEntity(traderEntity);
		protocol.setTraderEntity(traderEntity);
		protocol.setItemMap(itemMap);
	}

	// 根据用户id 初始化用户神秘商店
	private EventShopEntity initShop(String playerID) {
		EventShopEntity shopEntity = null;
		// PlayerEntity playerEntity = loginModule.getPlayerEntity(playerID);
		// 检查用户等级
		// if (playerEntity.getLevel() >= EventConstans.MYSTERY_UNLOCKLEVEL) {
		shopEntity = getShopEntity(playerID);
		// 免费刷新次数
		int permissionValue = vipModule.getPermissionValue(playerID, EventConstans.VIP_MYSTERY_SHOP_REFRESH_MAX);
		if (shopEntity == null) {
			shopEntity = new EventShopEntity();
			shopEntity.setPlayerID(playerID);
			// 神秘商店
			shopEntity.initShopItems(mysteryItemMap.get(EventConstans.MYSTERY_SHOP));
			// 设置神秘商店日期
			shopEntity.setShopTime(Utilities.getDateTime());
			// 根据vip等级设置神秘商店默认刷新次数
			shopEntity.setRefresh(permissionValue);			
			saveShopEntity(shopEntity);
		}
		// 增加免费刷新次数
		if (shopEntity.getRefresh() < permissionValue) {
			int diffHour = DateTimeUtil.getDiffHour(System.currentTimeMillis(), shopEntity.getLastRefreshTime());
			int value = diffHour / 2;
			if (value > 0) {
				shopEntity.setLastRefreshTime();
				if ((shopEntity.getRefresh() + value) < permissionValue) {
					shopEntity.setRefresh(shopEntity.getRefresh() + value);
				} else {
					shopEntity.setRefresh(permissionValue);
				}					
				saveShopEntity(shopEntity);
			}			
		}
		// 检查当天是否刷新
		if (!shopEntity.isShop()) {
			shopEntity.initShopItems(mysteryItemMap.get(EventConstans.MYSTERY_SHOP));
			saveShopEntity(shopEntity);
		}
		// }
		return shopEntity;
	}

	// 根据用户id 初始化用户神秘商人
	private EventTraderEntity initTrader(String playerID, boolean sign) {
		EventTraderEntity traderEntity = getTraderEntity(playerID);
		if (traderEntity == null) {
			traderEntity = new EventTraderEntity();
			traderEntity.setPlayerID(playerID);
			// 神秘商人
			traderEntity.initTraderItems(mysteryItemMap.get(EventConstans.MYSTERY_TRADER));
			// 设置日期
			traderEntity.setTraderTime(Utilities.getDateTime());
			// 商人存在时间
			// traderEntity.addExistTime(10 * 3600 * 1000);
			saveTraderEntity(traderEntity);
		}
		// 检查当天是否刷新
		if (!traderEntity.isTrader()) {
			traderEntity.initTraderItems(mysteryItemMap.get(EventConstans.MYSTERY_TRADER));
			saveTraderEntity(traderEntity);
		}
		if (sign) {
			return traderEntity;
		} else {
			if (traderEntity.getExistTime() == 0) {
				return null;
			} else {
				return traderEntity;
			}
		}
	}

	/**
	 * 支付成功,增加月卡
	 * 
	 * @param playerID
	 * @param protocol
	 */
	public EventMonthCardEntity eventMonthCardBuy(String playerID, IOMessage ioMessage) {
		EventMonthCardEntity eventMonthCardEntity = CommonMethod.getEntity(playerID, true, ioMessage, EventMonthCardEntity.class);
		int days = 30; // 有效期 30 天
		// 购买成功，添加天数
		if (eventMonthCardEntity.getExpireTime() > System.currentTimeMillis()) { // 如果当前月卡有效
			eventMonthCardEntity.setExpireTime(eventMonthCardEntity.getExpireTime() + DateTimeUtil.ONE_DAY_TIME_MS * days);
		} else {
			eventMonthCardEntity.setStartTime(System.currentTimeMillis());
			eventMonthCardEntity.setExpireTime(CommonMethod.getTodayLastTime() + DateTimeUtil.ONE_DAY_TIME_MS * (days - 1));
		}
		if (!DateTimeUtil.isSameDay(eventMonthCardEntity.getReceiveTime(), System.currentTimeMillis())) {
			// 首次购买 直接领取
			MonthCarData monthCarData = TemplateManager.getTemplateData(10841, MonthCarData.class);
			List<GoodsBean> goodsList = monthCarData.getDailyReward();
			rewardModule.addReward(playerID, goodsList, RewardType.monthCardReward);
			eventMonthCardEntity.setReceiveTime(System.currentTimeMillis() + 100);
		}
		monthCardDao.save(eventMonthCardEntity);
		return eventMonthCardEntity;
	}

	/**
	 * 领取月卡奖励
	 * 
	 * @param playerID
	 * @param ioMessage
	 * @param showMap
	 * @param itemMap
	 * @return
	 */
	public EventMonthCardEntity eventMonthCardReward(String playerID, IOMessage ioMessage, Map<String, GoodsBean> showMap, Map<String, Object> itemMap) {
		EventMonthCardEntity eventMonthCardEntity = CommonMethod.getEntity(playerID, true, ioMessage, EventMonthCardEntity.class);

		if (eventMonthCardEntity.getExpireTime() < System.currentTimeMillis()) { // 如果当前月卡有效
			ioMessage.getOutputResult().setCode(ErrorIds.MONTH_CARD_NOT_BUY);
			return eventMonthCardEntity;
		}

		if (DateTimeUtil.isSameDay(eventMonthCardEntity.getReceiveTime(), System.currentTimeMillis())) {
			ioMessage.getOutputResult().setCode(ErrorIds.MONTH_CARD_HAD_REWARD);
			return eventMonthCardEntity;
		}

		MonthCarData monthCarData = TemplateManager.getTemplateData(10841, MonthCarData.class);

		List<GoodsBean> goodsList = monthCarData.getDailyReward();

		// 领取奖励
		int code = rewardModule.addGoods(playerID, goodsList, true, showMap, itemMap, ioMessage);

		if (code != 0) {
			ioMessage.getOutputResult().setCode(code);
			return eventMonthCardEntity;
		}

		eventMonthCardEntity.setReceiveTime(System.currentTimeMillis());

		EventMonthCardDao.getInstance().save(eventMonthCardEntity);
		return eventMonthCardEntity;
	}

	/***
	 * 吃烧鸡活动
	 * 
	 * @param playerID
	 * @param protocol
	 */
	public void eventChicken(String playerID, EventChickenProtocol protocol) {
		Map<String, Object> itemMap = new HashMap<String, Object>();
		EventChickenEntity chickenEntity = initChickenEntity(playerID);
		if (EventUtils.isChickenTime(EventConstans.CHICKEN_TIME)) {
			// 第一次
			if (EventUtils.isChickenTime(0)) {
				if (chickenEntity.isFirst()) {
					protocol.setCode(ErrorIds.CHICKEN_ISEAT);
					return;
				} else {
					chickenEntity.setFirst(true);
				}
			}
			// 第二次
			if (EventUtils.isChickenTime(1)) {
				if (chickenEntity.isSecond()) {
					protocol.setCode(ErrorIds.CHICKEN_ISEAT);
					return;
				} else {
					chickenEntity.setSecond(true);
				}
			}
		} else {
			protocol.setCode(ErrorIds.EVENT_NOINTIME);
			return;
		}

		chickenEntity.setTimeDate(Utilities.getDateTime());

		int vitalityValue = 50;

		// ///////////////////////////////////
		// 元宵节活动 吃元宵奖励 翻倍
		// /////////////
		FestivalModule festivalModule = ModuleManager.getModule(ModuleNames.FestivalModule, FestivalModule.class);
		if (festivalModule.nowFestivalActive() == FestivalConstants.LANTERN) {
			vitalityValue = vitalityValue * 2;
		}

		List<GoodsBean> showMap = new ArrayList<GoodsBean>();
		showMap.add(new GoodsBean(KindIDs.VITALITY, vitalityValue));

		int code = rewardModule.addGoods(playerID, showMap, true, null, itemMap, null);
		if (code != 0) {
			protocol.setCode(code);
			return;
		}
		saveChickenEntity(chickenEntity);

		protocol.setChickenEntity(chickenEntity);
		protocol.setShowMap(showMap);
		protocol.setItemMap(itemMap);
	}

	/**
	 * 世界boss活动奖励
	 */
	public void sendWorldBossReward(String playerID, int pid) {
		List<GoodsBean> goodsList = new ArrayList<GoodsBean>();
		EventConfigEntity eventInfo = eventConfig.get(EventConstans.EVENT_TYPE_KILLMONSTER);
		if (eventInfo == null) {
			return;
		}
		// 判断活动是否结束
		if (EventUtils.isEventTime(eventInfo, EventConstans.YMDHMS) && eventInfo.getStatus() == 0) {
			ActiveMessageData messageData = activeMessageMap.get(EventConstans.EVENT_TYPE_KILLMONSTER);
			NewServerRewardData rewardData = newServerRewardMap.get(pid);
			if (rewardData == null) {
				return;
			}
			Set<Entry<Integer, Integer>> set = rewardData.getReward().entrySet();
			for (Entry<Integer, Integer> entry : set) {
				goodsList.add(new GoodsBean(entry.getKey(), entry.getValue()));
			}
			rewardModule.addReward(playerID, goodsList, RewardType.event_killmonster);
			if (pid == SysConstants.worldBossThirdGrade) {
				ChatModule chatModule = ModuleManager.getModule(ModuleNames.ChatModule, ChatModule.class);
				chatModule.addInformation(playerID, InformationMessageType.worldBossActive, 0, "");
			}
			if (messageData != null) {
				mailBoxModule.sendEventRewardMail(playerID, messageData.getTitle(), messageData.getContents());

			}
		}
	}

	/**
	 * 公会之王奖励发放
	 */
	public void sendLegionKingReward() {
		EventConfigEntity eventInfo = eventConfig.get(EventConstans.EVENT_TYPE_LEGIONKING);
		if (eventInfo == null) {
			return;
		}
		// 判断活动是否结束
		if (EventUtils.eventIsEnd(eventInfo) && eventInfo.getStatus() == 0) {
			ActiveMessageData messageData = activeMessageMap.get(EventConstans.EVENT_TYPE_LEGIONKING);

			long nowTime = System.currentTimeMillis();
			// //////////////////////////////////////////一级奖励发放/////
			// 4级工会,会长奖励
			NewServerRewardData legatusReward = newServerRewardMap.get(10998);
			// 4级工会,成员奖励
			NewServerRewardData memberReward = newServerRewardMap.get(109911);
			if (legatusReward == null || memberReward == null) {
				return;
			}
			List<GoodsBean> legatusGoodsList = new ArrayList<GoodsBean>();
			List<GoodsBean> memberGoodsList = new ArrayList<GoodsBean>();
			Set<Entry<Integer, Integer>> legatusSet = legatusReward.getReward().entrySet();
			for (Entry<Integer, Integer> entry : legatusSet) {
				legatusGoodsList.add(new GoodsBean(entry.getKey(), entry.getValue()));
			}
			Set<Entry<Integer, Integer>> memberSet = memberReward.getReward().entrySet();
			for (Entry<Integer, Integer> entry : memberSet) {
				memberGoodsList.add(new GoodsBean(entry.getKey(), entry.getValue()));
			}
			Logs.logger.info(eventInfo.getDesc() + "结束.开始发放"
					+ legatusReward.getName() + "," + memberReward.getName());
			// 获取军团列表
			List<LegionEntity> legionList = legionModule.getLegionLevelList(3);
			for (LegionEntity entity : legionList) {
				String legatus = entity.getLegatus();
				List<String> memberList = entity.getMembers();
				for (String member : memberList) {
					// 会长奖励
					if (member.equals(legatus)) {
						rewardModule.addReward(legatus, legatusGoodsList, RewardType.event_legionking);
					} else {
						// 成员奖励
						rewardModule.addReward(member, memberGoodsList, RewardType.event_legionking);
					}
					if (messageData != null) {
						mailBoxModule.sendEventRewardMail(member, messageData.getTitle(), messageData.getContents());
					}
				}
			}

			// //////////////////////////////////////////二级奖励发放/////
			// 5级工会,会长奖励
			legatusReward = newServerRewardMap.get(10997);
			// 5级工会,成员奖励
			memberReward = newServerRewardMap.get(109910);
			if (legatusReward == null || memberReward == null) {
				return;
			}
			legatusGoodsList = new ArrayList<GoodsBean>();
			memberGoodsList = new ArrayList<GoodsBean>();
			legatusSet = legatusReward.getReward().entrySet();
			for (Entry<Integer, Integer> entry : legatusSet) {
				legatusGoodsList.add(new GoodsBean(entry.getKey(), entry.getValue()));
			}
			memberSet = memberReward.getReward().entrySet();
			for (Entry<Integer, Integer> entry : memberSet) {
				memberGoodsList.add(new GoodsBean(entry.getKey(), entry.getValue()));
			}
			Logs.logger.info(eventInfo.getDesc() + "结束.开始发放"
					+ legatusReward.getName() + "," + memberReward.getName());
			// 获取军团列表
			legionList = legionModule.getLegionLevelList(4);
			for (LegionEntity entity : legionList) {
				String legatus = entity.getLegatus();
				List<String> memberList = entity.getMembers();
				for (String member : memberList) {
					// 会长奖励
					if (member.equals(legatus)) {
						rewardModule.addReward(legatus, legatusGoodsList, RewardType.event_legionking);
					} else {
						// 成员奖励
						rewardModule.addReward(member, memberGoodsList, RewardType.event_legionking);
					}
					if (messageData != null) {
						mailBoxModule.sendEventRewardMail(member, messageData.getTitle(), messageData.getContents());
					}
				}
			}

			// //////////////////////////////////////////三级奖励发放/////
			// 6级工会,会长奖励
			legatusReward = newServerRewardMap.get(10996);
			// 6级工会,成员奖励
			memberReward = newServerRewardMap.get(10999);
			if (legatusReward == null || memberReward == null) {
				return;
			}
			legatusGoodsList = new ArrayList<GoodsBean>();
			memberGoodsList = new ArrayList<GoodsBean>();
			legatusSet = legatusReward.getReward().entrySet();
			for (Entry<Integer, Integer> entry : legatusSet) {
				legatusGoodsList.add(new GoodsBean(entry.getKey(), entry.getValue()));
			}
			memberSet = memberReward.getReward().entrySet();
			for (Entry<Integer, Integer> entry : memberSet) {
				memberGoodsList.add(new GoodsBean(entry.getKey(), entry.getValue()));
			}
			Logs.logger.info(eventInfo.getDesc() + "结束.开始发放"
					+ legatusReward.getName() + "," + memberReward.getName());
			// 获取军团列表
			legionList = legionModule.getLegionLevelList(5);
			for (LegionEntity entity : legionList) {
				String legatus = entity.getLegatus();
				List<String> memberList = entity.getMembers();
				for (String member : memberList) {
					// 会长奖励
					if (member.equals(legatus)) {
						rewardModule.addReward(legatus, legatusGoodsList, RewardType.event_legionking);
					} else {
						// 成员奖励
						rewardModule.addReward(member, memberGoodsList, RewardType.event_legionking);
					}
					if (messageData != null) {
						mailBoxModule.sendEventRewardMail(member, messageData.getTitle(), messageData.getContents());
					}
				}
			}
			// 活动结束,关闭活动
			eventInfo.setStatus(1);
			// 活动版本加1
			eventInfo.setVersion(eventInfo.getVersion() + 1);
			saveEventConfig(eventInfo);
			// 更新活动缓存
			eventConfig = initEventConfig(false);
			Logs.logger.info("奖励发放结束,耗时:"
					+ (System.currentTimeMillis() - nowTime) + "ms");
		}
	}

	public boolean isInWarGod() {
		EventConfigEntity eventInfo = eventConfig.get(EventConstans.EVENT_TYPE_WARGOD);
		if (eventInfo == null) {
			return false;
		}
		if (EventUtils.isEventTime(eventInfo, EventConstans.YMDHMS)) {
			return true;
		}
		return false;
	}

	/***
	 * 战神之王排行榜
	 * */
	public WarGodRankEntity warGodRankList() {
		EventConfigEntity eventInfo = eventConfig.get(EventConstans.EVENT_TYPE_WARGOD);
		if (eventInfo == null) {
			return null;
		}
		if (EventUtils.isEventTime(eventInfo, EventConstans.YMDHMS)) {
			List<PlayerEntity> playerList = loginModule.getPlayerEntityListByfightValue(warGodRankLength);
			List<WarGodRankInfo> rankList = new ArrayList<>();
			int size = playerList.size();
			for (int i = 0; i < size; i++) {
				PlayerEntity playerEntity = playerList.get(i);
				String playerID = playerEntity.getKey().toString();
				WarGodRankInfo warGodRankInfo = new WarGodRankInfo();
				warGodRankInfo.setFightValue(playerEntity.getFightValue());
				warGodRankInfo.setRank(i + 1);
				warGodRankInfo.setName(playerEntity.getNickName());
				warGodRankInfo.setPhotoID(playerEntity.getPhotoID());
				warGodRankInfo.setPlayerID(playerID);
				warGodRankInfo.setLevel(playerEntity.getLevel());
				String groupID = playerEntity.getGroupID();
				if (!groupID.equals("0")) {
					LegionEntity legionEntity = legionModule.getLegionEntity(groupID + "");
					if (legionEntity != null) {
						warGodRankInfo.setGroupName(legionEntity.getName());
					}
				}
				rankList.add(warGodRankInfo);
			}
			WarGodRankEntity warGodRankEntity = new WarGodRankEntity();
			warGodRankEntity.setRankList(rankList);
			warGodRankEntity.setKey(warGodKey);
			this.saveWarGodEntity(warGodRankEntity);
			return warGodRankEntity;
		} else {
			logger.error("不在活动时间内");
			throw new IllegalArgumentException(ErrorIds.EVENT_NOINTIME + "");
		}
	}

	/*
	 * 战神之王奖励发放
	 */
	public void sendWarGodReward() {
		EventConfigEntity eventInfo = eventConfig.get(EventConstans.EVENT_TYPE_WARGOD);
		if (eventInfo == null) {
			return;
		}
		// 判断活动是否结束
		if (EventUtils.eventIsEnd(eventInfo) && eventInfo.getStatus() == 0) {
			ActiveMessageData messageData = activeMessageMap.get(EventConstans.EVENT_TYPE_WARGOD);
			Logs.logger.info(eventInfo.getDesc() + "结束.开始发放奖励!");
			long nowTime = System.currentTimeMillis();
			// 获取战斗力最高10人
			List<String> playerList = loginModule.getPlayerListByfightValue(10);
			for (int i = 0; i < playerList.size(); i++) {
				NewServerRewardData rewardData = null;
				if (i == 0) {
					rewardData = newServerRewardMap.get(10995);
				}
				if (i >= 1 && i < 5) {
					rewardData = newServerRewardMap.get(109915);
				}
				if (i >= 5 && i < 10) {
					rewardData = newServerRewardMap.get(109916);
				}
				if (rewardData != null) {
					Set<Entry<Integer, Integer>> set = rewardData.getReward().entrySet();
					List<GoodsBean> goodsList = new ArrayList<GoodsBean>();
					for (Entry<Integer, Integer> entry : set) {
						goodsList.add(new GoodsBean(entry.getKey(), entry.getValue()));
					}
					rewardModule.addReward(playerList.get(i), goodsList, RewardType.event_wargod);
					if (messageData != null) {
						mailBoxModule.sendEventRewardMail(playerList.get(i), messageData.getTitle(), messageData.getContents());
					}
				}
			}
			// 活动结束,关闭活动
			eventInfo.setStatus(1);
			// 活动版本加1
			eventInfo.setVersion(eventInfo.getVersion() + 1);
			saveEventConfig(eventInfo);
			// 更新活动缓存
			eventConfig = initEventConfig(false);
			Logs.logger.info("奖励发放结束,耗时:"
					+ (System.currentTimeMillis() - nowTime) + "ms");
		}
	}

	/*
	 * 冲级之王奖励发放
	 */
	public void sendUpKingReward() {
		EventConfigEntity eventInfo = eventConfig.get(EventConstans.EVENT_TYPE_UPKING);
		if (eventInfo == null) {
			return;
		}
		// 判断活动是否结束
		if (EventUtils.eventIsEnd(eventInfo) && eventInfo.getStatus() == 0) {
			NewServerRewardData rewardData = newServerRewardMap.get(eventInfo.getInfoPid());
			ActiveMessageData messageData = activeMessageMap.get(EventConstans.EVENT_TYPE_UPKING);
			if (rewardData == null) {
				return;
			}
			Logs.logger.info(eventInfo.getDesc() + "结束.开始发放"
					+ rewardData.getName());
			long nowTime = System.currentTimeMillis();
			Set<Entry<Integer, Integer>> set = rewardData.getReward().entrySet();
			// 获取经验值最高10人
			List<String> playerList = heroModule.getExpRank(10);
			List<GoodsBean> goodsList = new ArrayList<GoodsBean>();
			for (Entry<Integer, Integer> entry : set) {
				goodsList.add(new GoodsBean(entry.getKey(), entry.getValue()));
			}
			for (String playerID : playerList) {
				rewardModule.addReward(playerID, goodsList, RewardType.event_upking);
				if (messageData != null) {
					mailBoxModule.sendEventRewardMail(playerID, messageData.getTitle(), messageData.getContents());
				}
			}
			// 活动结束,关闭活动
			eventInfo.setStatus(1);
			// 活动版本加1
			eventInfo.setVersion(eventInfo.getVersion() + 1);
			saveEventConfig(eventInfo);
			// 更新活动缓存
			eventConfig = initEventConfig(false);
			Logs.logger.info(rewardData.getName() + "奖励发放结束,耗时:"
					+ (System.currentTimeMillis() - nowTime) + "ms");
		}
	}

	/*
	 * 冲级送礼奖励发放
	 */
	public void sendLevelUpReward() {
		EventConfigEntity eventInfo = eventConfig.get(EventConstans.EVENT_TYPE_LEVELUP);
		if (eventInfo == null) {
			return;
		}
		// 判断活动是否结束
		if (EventUtils.eventIsEnd(eventInfo) && eventInfo.getStatus() == 0) {
			NewServerRewardData rewardData = newServerRewardMap.get(eventInfo.getInfoPid());
			ActiveMessageData messageData = activeMessageMap.get(EventConstans.EVENT_TYPE_LEVELUP);
			if (rewardData == null) {
				return;
			}
			Logs.logger.info(eventInfo.getDesc() + "结束.开始发放"
					+ rewardData.getName());
			long nowTime = System.currentTimeMillis();
			Set<Entry<Integer, Integer>> set = rewardData.getReward().entrySet();
			// 获取等级达到rewardData.getLevelRequest()的用户列表
			List<String> playerList = loginModule.getPlayerListByLevel(rewardData.getLevelRequest());
			Logs.logger.info("奖励发放等级:" + rewardData.getLevelRequest()
					+ ",发放人数:" + playerList.size());
			List<GoodsBean> goodsList = new ArrayList<GoodsBean>();
			for (Entry<Integer, Integer> entry : set) {
				goodsList.add(new GoodsBean(entry.getKey(), entry.getValue()));
			}
			for (String playerID : playerList) {
				rewardModule.addReward(playerID, goodsList, RewardType.event_levelup);
				if (messageData != null) {
					mailBoxModule.sendEventRewardMail(playerID, messageData.getTitle(), messageData.getContents());
				}
			}
			// 活动结束,关闭活动
			eventInfo.setStatus(1);
			// 活动版本加1
			eventInfo.setVersion(eventInfo.getVersion() + 1);
			saveEventConfig(eventInfo);
			// 更新活动缓存
			eventConfig = initEventConfig(false);
			Logs.logger.info(rewardData.getName() + "奖励发放结束,耗时:"
					+ (System.currentTimeMillis() - nowTime) + "ms");
		}
	}

	/**
	 * 限时热卖奖励发放
	 */
	public void sendTimeLimitRankReward() {
		EventConfigEntity eventInfo = eventConfig.get(EventConstans.EVENT_TYPE_TIME_LIMIT);
		if (eventInfo == null) {
			return;
		}
		// 判断活动是否结束
		if (EventUtils.eventIsEnd(eventInfo) && eventInfo.getStatus() == 0) {
			Logs.logger.info(eventInfo.getDesc() + "结束.开始发放奖励");
			long nowTime = System.currentTimeMillis();
			List<EventTimeLimitEntity> rankList = getRankList(50);
			// 奖励列表
			List<GoodsBean> goodsList = new ArrayList<GoodsBean>();
			LimitHeroData limitHero = limitHeroMap.get(eventInfo.getInfoPid());
			Map<Integer, Integer> rewardMap = null;
			int rank = 1;
			for (EventTimeLimitEntity entity : rankList) {
				// 清空奖励列表
				goodsList.clear();
				if (rank == 1) {
					// 第一名奖励
					rewardMap = limitHero.getRank1Reward();
				}
				if (rank >= 2 && rank <= 3) {
					// 第2-3名奖励
					rewardMap = limitHero.getRank2Reward();
				}
				if (rank >= 4 && rank <= 20) {
					// 第4-20名奖励
					rewardMap = limitHero.getRank3Reward();
				}
				if (rank >= 21 && rank <= 50) {
					// 第21-50 名奖励
					rewardMap = limitHero.getRank4Reward();
				}
				for (Integer rewardID : limitHero.getKeys(rewardMap)) {
					goodsList.add(new GoodsBean(rewardID, rewardMap.get(rewardID)));
				}
				rewardModule.addReward(entity.getPlayerID(), goodsList, RewardType.timeLimitEventReward);
				rank++;
			}
			// 奖励发放完成,关闭活动
			eventInfo.setStatus(1);
			// 活动版本加1
			eventInfo.setVersion(eventInfo.getVersion() + 1);
			saveEventConfig(eventInfo);
			// 更新活动缓存
			eventConfig = initEventConfig(false);
			Logs.logger.info(eventInfo.getDesc() + "奖励发放结束,耗时:"
					+ (System.currentTimeMillis() - nowTime) + "ms");
		}
	}

	/**
	 * 消费送礼活动数据收集接口
	 * 
	 * @param playerID
	 */
	public void intefaceEventExpense(String playerID, int expenseNum) {
		EventConfigEntity eventInfo = eventConfig.get(EventConstans.EVENT_TYPE_EXPENSE);
		if (eventInfo != null) {
			if (EventUtils.isEventTime(eventInfo, EventConstans.YMDHMS)) {
				EventExpenseEntity expenseEntity = initExpenseEntity(playerID);
				expenseEntity.addExpenseTotal(expenseNum);
				saveExpenseEntity(expenseEntity);
			}
		}
	}

	/**
	 * 充值送礼活动数据收集接口
	 * 
	 * @param playerID
	 */
	public void intefaceEventPay(String playerID, int payNum) {
		EventConfigEntity eventInfo = eventConfig.get(EventConstans.EVENT_TYPE_PAY);
		if (eventInfo != null) {
			if (EventUtils.isEventTime(eventInfo, EventConstans.YMDHMS)) {
				EventPayEntity eventPayEntity = initEventPayEntity(playerID);
				eventPayEntity.addPayTotal(payNum);
				saveEventPayEntity(eventPayEntity);
			}
		}
	}

	/**
	 * 充值抽奖活动数据收集接口
	 * 
	 * @param playerID
	 * @param payNum
	 */
	public EventDrawPayEntity intefaceDrawPay(String playerID, int payNum) {
		EventConfigEntity eventInfo = eventConfig.get(EventConstans.EVENT_TYPE_DRAW_PAY);
		EventDrawPayEntity drawPayEntity = null;
		if (eventInfo != null) {
			if (EventUtils.isEventTime(eventInfo, EventConstans.YMDHMS)) {
				drawPayEntity = initDrawPayEntity(playerID);
				if (drawPayEntity.getState() == 0) {
					drawPayEntity.setState(1);
				}
				drawPayEntity.addPayTotal(payNum);
				saveDrawPayEntity(drawPayEntity);
			}
		}
		return drawPayEntity;
	}

	/**
	 * 积分抽奖活动数据收集接口
	 * 
	 * @param playerID
	 * @param integralNum
	 */
	public void intefaceDrawIntegral(String playerID, int pid) {
		EventConfigEntity eventInfo = eventConfig.get(EventConstans.EVENT_TYPE_DRAW_INTEGRAL);
		if (eventInfo != null) {
			if (EventUtils.isEventTime(eventInfo, EventConstans.YMDHMS)) {
				// 积分pid
				int integralNum = scoreGainMap.get(pid).getActiveReward();
				EventDrawIntegralEntity drawIntegralEntity = initDrawIntegralEntity(playerID);
				drawIntegralEntity.addIntegraTotal(integralNum);
				saveDrawIntegralEntity(drawIntegralEntity);
			}
		}
	}

	/**
	 * 积分抽奖活动数据收集接口
	 * 
	 * @param playerID
	 * @param integralNum
	 * @param num
	 */
	public void intefaceDrawIntegral(String playerID, int pid, int num) {
		EventConfigEntity eventInfo = eventConfig.get(EventConstans.EVENT_TYPE_DRAW_INTEGRAL);
		if (eventInfo != null) {
			if (EventUtils.isEventTime(eventInfo, EventConstans.YMDHMS)) {
				// 积分pid
				int integralNum = scoreGainMap.get(pid).getActiveReward() * num;
				EventDrawIntegralEntity drawIntegralEntity = initDrawIntegralEntity(playerID);
				drawIntegralEntity.addIntegraTotal(integralNum);
				saveDrawIntegralEntity(drawIntegralEntity);
			}
		}
	}

	/**
	 * 获取活动信息
	 * 
	 * @param eventType
	 * @return
	 */
	public EventConfigEntity getEventInfo(int eventType) {
		EventConfigEntity eventInfo = eventConfig.get(eventType);
		return eventInfo;
	}

	public void saveEventTimeLimitEntity(EventTimeLimitEntity eventTimeLimitEntity) {
		EventTimeLimitDao.getInstance().save(eventTimeLimitEntity);
	}

	/**
	 * 获取我的排名
	 * 
	 * @param eventTimeLimitEntity
	 * @return
	 */
	public void getEventTimeLimitRank(String playerID, EventTimeLimitProtocol protocol) {
		EventConfigEntity eventInfo = getEventConfigByPid(EventConstans.EVENT_TYPE_TIME_LIMIT);
		if (eventInfo == null) {
			return;
		}
		EventTimeLimitEntity timeLimitEntity = initEventTimeLimitEntity(playerID);
		if (timeLimitEntity == null) {
			return;
		}
		// 个人排名
		int rank = timeLimitDao.getRank(timeLimitEntity);
		timeLimitEntity.setRank(rank);
		protocol.setTimeLimitEntity(timeLimitEntity);
		protocol.setRankList(getRankList(20));
	}

	// 初始化首冲奖励
	private BaseEntity initFirstPayEntity(String playerID) {
		PayEntity payEntity = payModule.getPayEntity(playerID);
		if (payEntity == null) {
			return new EventDefaultEntity();
		}
		return null;
	}

	// 初始化vip每日礼包
	private BaseEntity initVipDailyEntity(String playerID) {
		EventVipDailyEntity vipDailyEntity = getVipDailyEntity(playerID);
		if (vipDailyEntity == null) {
			VipEntity vipEntity = vipModule.initVipEntity(playerID);
			if (vipEntity.getVipLevel() >= 1) {
				return new EventDefaultEntity();
			} else {
				return null;
			}
		}
		return vipDailyEntity;
	}

	// 初始化月卡
	private BaseEntity initMonthCardEntity(String playerID) {
		EventMonthCardEntity monthCardEntity = getEventMonthCardEntity(playerID);
		if (monthCardEntity == null) {
			return new EventDefaultEntity();
		}
		// 当天未领取
		if (!DateTimeUtil.isSameDay(monthCardEntity.getReceiveTime(), System.currentTimeMillis())) {
			// 月卡未过期
			if (monthCardEntity.getExpireTime() > System.currentTimeMillis()) {
				// 领取时间
				long rewardTime = monthCardEntity.getReceiveTime();
				if (monthCardEntity.getStartTime() > rewardTime) {
					Date rewardDate = new Date(rewardTime);
					Date nowDate = new Date();
					// 获取相差天数
					int diffDay = DateTimeUtil.getDiffDay(nowDate, rewardDate);
					int rewardDay = diffDay - 1;
					if (rewardDay > 0) {
						MonthCarData monthCarData = TemplateManager.getTemplateData(10841, MonthCarData.class);
						List<GoodsBean> goodsList = monthCarData.getDailyReward();
						for (int i = 0; i < rewardDay; i++) {
							// 物品放入奖励中心
							rewardModule.addReward(playerID, goodsList, RewardType.monthCardReward);
						}
						// 设置月卡领取时间为昨天
						monthCardEntity.setReceiveTime((System.currentTimeMillis() - DateTimeUtil.ONE_DAY_TIME_MS));
						monthCardDao.save(monthCardEntity);
					}
				}
			}
		}
		return monthCardEntity;
	}

	// 初始化vip成长
	private BaseEntity initVipGrowEntity(String playerID) {
		EventVipGrowEntity vipGrowEntity = getVipGrowEntity(playerID);
		if (vipGrowEntity == null) {
			return new EventDefaultEntity();
		}
		return vipGrowEntity;
	}

	// 初始化道具兑换
	private EventExchangeEntity initExchangeEntity(String playerID) {
		EventConfigEntity eventInfo = eventConfig.get(EventConstans.EVENT_TYPE_EXCHANGE);
		long startTime = DateTimeUtil.getDate(eventInfo.getStartTime()).getTime();
		long endTime = DateTimeUtil.getDate(eventInfo.getEndTime()).getTime();
		EventExchangeEntity exchangeEntity = getExchangeEntity(playerID);
		if (exchangeEntity == null) {
			exchangeEntity = new EventExchangeEntity();
			exchangeEntity.setPlayerID(playerID);
			exchangeEntity.setStartTime(startTime);
			exchangeEntity.setEndTime(endTime);
			exchangeEntity.setDateTime(Utilities.getDateTime());
			exchangeEntity.addExchange(10931, getRandomExchange(10931));
			exchangeEntity.addExchange(10932, getRandomExchange(10932));
			exchangeEntity.addExchange(10933, getRandomExchange(10933));
			exchangeEntity.addExchange(10934, getRandomExchange(10934));
			saveExchangeEntity(exchangeEntity);
		} else {
			if (!exchangeEntity.isRefresh()) {
				exchangeEntity.getExchanges().clear();
				exchangeEntity.addExchange(10931, getRandomExchange(10931));
				exchangeEntity.addExchange(10932, getRandomExchange(10932));
				exchangeEntity.addExchange(10933, getRandomExchange(10933));
				exchangeEntity.addExchange(10934, getRandomExchange(10934));
				saveExchangeEntity(exchangeEntity);
			}
			if (startTime != exchangeEntity.getStartTime() && endTime != exchangeEntity.getEndTime()) {
				exchangeEntity.setStartTime(startTime);
				exchangeEntity.setEndTime(endTime);
				saveExchangeEntity(exchangeEntity);
			}
		}
		return exchangeEntity;
	}

	// 初始化积分抽奖
	private EventDrawIntegralEntity initDrawIntegralEntity(String playerID) {
		EventConfigEntity eventInfo = eventConfig.get(EventConstans.EVENT_TYPE_DRAW_INTEGRAL);
		long startTime = DateTimeUtil.getDate(eventInfo.getStartTime()).getTime();
		long endTime = DateTimeUtil.getDate(eventInfo.getEndTime()).getTime();
		EventDrawIntegralEntity drawIntegralEntity = getDrawIntegralEntity(playerID);
		if (drawIntegralEntity == null) {
			drawIntegralEntity = new EventDrawIntegralEntity();
			drawIntegralEntity.setPlayerID(playerID);
			drawIntegralEntity.setDateTime(Utilities.getDateTime());
			drawIntegralEntity.setStartTime(startTime);
			drawIntegralEntity.setEndTime(endTime);
			saveDrawIntegralEntity(drawIntegralEntity);
		} else {
			if (!drawIntegralEntity.isRefresh()) {
				drawIntegralEntity.setIntegraTotal(0);
				drawIntegralEntity.setDrawNum(0);
				saveDrawIntegralEntity(drawIntegralEntity);
			}
			if (startTime != drawIntegralEntity.getStartTime() && endTime != drawIntegralEntity.getEndTime()) {
				drawIntegralEntity.setStartTime(startTime);
				drawIntegralEntity.setEndTime(endTime);
				saveDrawIntegralEntity(drawIntegralEntity);
			}
		}
		return drawIntegralEntity;
	}

	// 初始化充值抽奖
	private EventDrawPayEntity initDrawPayEntity(String playerID) {
		EventConfigEntity eventInfo = eventConfig.get(EventConstans.EVENT_TYPE_DRAW_PAY);
		long startTime = DateTimeUtil.getDate(eventInfo.getStartTime()).getTime();
		long endTime = DateTimeUtil.getDate(eventInfo.getEndTime()).getTime();
		EventDrawPayEntity drawPayEntity = getDrawPayEntity(playerID);
		if (drawPayEntity == null) {
			drawPayEntity = new EventDrawPayEntity();
			drawPayEntity.setPlayerID(playerID);
			drawPayEntity.setDateTime(Utilities.getDateTime());
			drawPayEntity.setStartTime(startTime);
			drawPayEntity.setEndTime(endTime);
			saveDrawPayEntity(drawPayEntity);
		} else {
			if (!drawPayEntity.isRefresh()) {
				drawPayEntity.setPayTotal(0);
				drawPayEntity.setState(0);
				drawPayEntity.getRewardList().clear();
				saveDrawPayEntity(drawPayEntity);
			}
			if (startTime != drawPayEntity.getStartTime() && endTime != drawPayEntity.getEndTime()) {
				drawPayEntity.setStartTime(startTime);
				drawPayEntity.setEndTime(endTime);
				saveDrawPayEntity(drawPayEntity);
			}
		}
		return drawPayEntity;
	}

	// 初始化消费送礼
	private EventExpenseEntity initExpenseEntity(String playerID) {
		EventConfigEntity eventInfo = eventConfig.get(EventConstans.EVENT_TYPE_EXPENSE);
		long startTime = DateTimeUtil.getDate(eventInfo.getStartTime()).getTime();
		long endTime = DateTimeUtil.getDate(eventInfo.getEndTime()).getTime();
		EventExpenseEntity expenseEntity = getEventExpenseEntity(playerID);
		if (expenseEntity == null) {
			expenseEntity = new EventExpenseEntity();
			expenseEntity.setPlayerID(playerID);
			expenseEntity.setStartTime(startTime);
			expenseEntity.setEndTime(endTime);
			saveExpenseEntity(expenseEntity);
		} else {
			if (startTime != expenseEntity.getStartTime() && endTime != expenseEntity.getEndTime()) {
				expenseEntity.setStartTime(startTime);
				expenseEntity.setEndTime(endTime);
				expenseEntity.getRewardList().clear();
				expenseEntity.setExpenseTotal(0);
				saveExpenseEntity(expenseEntity);
			}
		}
		return expenseEntity;
	}

	// 初始化充值送礼
	private EventPayEntity initEventPayEntity(String playerID) {
		EventConfigEntity eventInfo = eventConfig.get(EventConstans.EVENT_TYPE_PAY);
		long startTime = DateTimeUtil.getDate(eventInfo.getStartTime()).getTime();
		long endTime = DateTimeUtil.getDate(eventInfo.getEndTime()).getTime();
		EventPayEntity eventPayEntity = getEventPayEntity(playerID);
		if (eventPayEntity == null) {
			eventPayEntity = new EventPayEntity();
			eventPayEntity.setPlayerID(playerID);
			eventPayEntity.setEndTime(endTime);
			eventPayEntity.setStartTime(startTime);
			saveEventPayEntity(eventPayEntity);
		} else {
			if (endTime != eventPayEntity.getEndTime() && startTime != eventPayEntity.getStartTime()) {
				eventPayEntity.setEndTime(endTime);
				eventPayEntity.setStartTime(startTime);
				eventPayEntity.getRewardList().clear();
				eventPayEntity.setPayTotal(0);
				saveEventPayEntity(eventPayEntity);
			}
		}
		return eventPayEntity;
	}
	
	// 初始化全民福利
	private EventUniversalVerfareEntity initEventUniversalVerfareEntity(String playerID) {
		EventConfigEntity eventInfo = eventConfig.get(EventConstans.EVENT_TYPE_UNIVERSAL_VERFARE);
		long startTime = DateTimeUtil.getDate(eventInfo.getStartTime()).getTime();
		long endTime = DateTimeUtil.getDate(eventInfo.getEndTime()).getTime();
		EventUniversalVerfareEntity eventUniversalVerfareEntity = getEventUniversalVerfareEntity(playerID);
		if (eventUniversalVerfareEntity == null) {
			eventUniversalVerfareEntity = new EventUniversalVerfareEntity();
			eventUniversalVerfareEntity.setPlayerID(playerID);
			eventUniversalVerfareEntity.setEndTime(endTime);
			eventUniversalVerfareEntity.setStartTime(startTime);
			saveEventUniversalVerfareEntity(eventUniversalVerfareEntity);
		} else {
			if (endTime != eventUniversalVerfareEntity.getEndTime() && startTime != eventUniversalVerfareEntity.getStartTime()) {
				eventUniversalVerfareEntity.setEndTime(endTime);
				eventUniversalVerfareEntity.setStartTime(startTime);
				eventUniversalVerfareEntity.getRewardList().clear();
				eventUniversalVerfareEntity.getRewardVipList().clear();
				saveEventUniversalVerfareEntity(eventUniversalVerfareEntity);
			}
		}
		return eventUniversalVerfareEntity;
	}
	
	// 初始化幸运抽奖
	private EventLuckyDrawEntity initEventLuckyDrawEntity(String playerID) {
		EventConfigEntity eventInfo = eventConfig.get(EventConstans.EVENT_TYPE_LUCKY_DRAW);
		long startTime = DateTimeUtil.getDate(eventInfo.getStartTime()).getTime();
		long endTime = DateTimeUtil.getDate(eventInfo.getEndTime()).getTime();
		EventLuckyDrawEntity eventLuckyDrawEntity = getEventLuckyDrawEntity(playerID);
		if (eventLuckyDrawEntity == null) {
			eventLuckyDrawEntity = new EventLuckyDrawEntity();
			eventLuckyDrawEntity.setPlayerID(playerID);
			eventLuckyDrawEntity.setEndTime(endTime);
			eventLuckyDrawEntity.setStartTime(startTime);
			eventLuckyDrawEntity.setOverplus(EventConstans.LUCKY_DRAW_NUM);
			saveEventLuckyDrawEntity(eventLuckyDrawEntity);
		} else {
			if (endTime != eventLuckyDrawEntity.getEndTime() && startTime != eventLuckyDrawEntity.getStartTime()) {
				eventLuckyDrawEntity.setEndTime(endTime);
				eventLuckyDrawEntity.setStartTime(startTime);
				eventLuckyDrawEntity.setOverplus(EventConstans.LUCKY_DRAW_NUM);
				eventLuckyDrawEntity.setDrawNum(0);
				saveEventLuckyDrawEntity(eventLuckyDrawEntity);
			}
		}
		return eventLuckyDrawEntity;
	}
	
	// 初始化财神送礼
	private EventFortunaGiftsEntity initEventFortunaGiftsEntity(String playerID) {
		ActiveFortunaData activeFortunaData=newActiveFortunaMap.get(13081);
		EventConfigEntity eventInfo = eventConfig.get(EventConstans.EVENT_TYPE_FORTUNA_GIFTS);
		long startTime = DateTimeUtil.getDate(eventInfo.getStartTime()).getTime();
		long endTime = DateTimeUtil.getDate(eventInfo.getEndTime()).getTime();
		EventFortunaGiftsEntity eventFortunaGiftsEntity = getEventFortunaGiftsEntity(playerID);
		if (eventFortunaGiftsEntity == null) {
			eventFortunaGiftsEntity = new EventFortunaGiftsEntity();
			eventFortunaGiftsEntity.setPlayerID(playerID);
			eventFortunaGiftsEntity.setEndTime(endTime);
			eventFortunaGiftsEntity.setStartTime(startTime);
			eventFortunaGiftsEntity.setLowPrice(activeFortunaData.getLowPrice());
			eventFortunaGiftsEntity.setHighPrice(activeFortunaData.getHighPrice());
			eventFortunaGiftsEntity.setNum(EventConstans.FORTUNA_GIFTS_NUM);
			eventFortunaGiftsEntity.setGold(0);
			saveEventFortunaGiftsEntity(eventFortunaGiftsEntity);
		} else {
			if (endTime != eventFortunaGiftsEntity.getEndTime() && startTime != eventFortunaGiftsEntity.getStartTime()) {
				eventFortunaGiftsEntity.setEndTime(endTime);
				eventFortunaGiftsEntity.setStartTime(startTime);				
				eventFortunaGiftsEntity.setReceiveTime(0);
				eventFortunaGiftsEntity.setLowPrice(activeFortunaData.getLowPrice());
				eventFortunaGiftsEntity.setHighPrice(activeFortunaData.getHighPrice());
				eventFortunaGiftsEntity.setNum(EventConstans.FORTUNA_GIFTS_NUM);
				eventFortunaGiftsEntity.setGold(0);
				saveEventFortunaGiftsEntity(eventFortunaGiftsEntity);
			}
		}
		return eventFortunaGiftsEntity;
	}
	
	// 定时任务财神送礼
	public void jobFortunaGiftsEntity() {		
		EventConfigEntity eventInfo = eventConfig.get(EventConstans.EVENT_TYPE_FORTUNA_GIFTS);
		if (EventUtils.isEventTime(eventInfo, EventConstans.YMDHMS)) {			
			List<EventFortunaGiftsEntity> list=eventFortunaGiftsEntityDao.getAllFortunaGiftsEntity();
			for(EventFortunaGiftsEntity fortunaGifts:list){
				fortunaGifts.setNum(EventConstans.FORTUNA_GIFTS_NUM);
				fortunaGifts.setGold(0);
				fortunaGifts.setReceiveTime(0);
				saveEventFortunaGiftsEntity(fortunaGifts);
			}
		}						
	}
	
	// 定时任务幸运抽奖
	public void jobLuckyDrawEntity() {		
		EventConfigEntity eventInfo = eventConfig.get(EventConstans.EVENT_TYPE_LUCKY_DRAW);
		if (EventUtils.isEventTime(eventInfo, EventConstans.YMDHMS)) {			
			List<EventLuckyDrawEntity> list=eventLuckyDrawDao.getAllLuckyDrawEntity();
			for(EventLuckyDrawEntity luckyDraw:list){
				luckyDraw.setOverplus(EventConstans.LUCKY_DRAW_NUM);
				luckyDraw.setDrawNum(0);
				saveEventLuckyDrawEntity(luckyDraw);
			}
		}						
	}
		
	// 定时任务全民福利
	public void jobUniversalVerfareEntity() {		
		EventConfigEntity eventInfo = eventConfig.get(EventConstans.EVENT_TYPE_UNIVERSAL_VERFARE);
		if (EventUtils.isEventTime(eventInfo, EventConstans.YMDHMS)) {			
			List<EventUniversalVerfareEntity> list=eventUniversalVerfareDao.getAllUniversalVerfareEntity();
			for(EventUniversalVerfareEntity universalVerfare:list){
				universalVerfare.getRewardList().clear();
				universalVerfare.getRewardVipList().clear();
				saveEventUniversalVerfareEntity(universalVerfare);
			}
		}						
	}

	// 初始化限时抽将
	private EventTimeLimitEntity initEventTimeLimitEntity(String playerID) {
		EventConfigEntity eventInfo = eventConfig.get(EventConstans.EVENT_TYPE_TIME_LIMIT);
		if (eventInfo == null) {
			return null;
		}
		long startTime = DateTimeUtil.getDate(eventInfo.getStartTime()).getTime();
		long endTime = DateTimeUtil.getDate(eventInfo.getEndTime()).getTime();
		EventTimeLimitEntity timeLimitEntity = getTimeLimitEntity(playerID, startTime, endTime);
		if (timeLimitEntity == null) {
			timeLimitEntity = new EventTimeLimitEntity();
			timeLimitEntity.setPlayerID(playerID);
			// 设置活动pid
			timeLimitEntity.setPid(eventInfo.getInfoPid());
			timeLimitEntity.setRank(0);
			timeLimitEntity.setEndTime(endTime);
			timeLimitEntity.setStartTime(startTime);
			timeLimitEntity.setBuyTime(System.currentTimeMillis());
			saveTimeLimitEntity(timeLimitEntity);
		} else {
			if (timeLimitEntity.getPid() != eventInfo.getInfoPid()) {
				// 设置活动pid
				timeLimitEntity.setPid(eventInfo.getInfoPid());
				saveTimeLimitEntity(timeLimitEntity);
			}
			// 新活动开启
			if (timeLimitEntity.getStartTime() != startTime && timeLimitEntity.getEndTime() != endTime) {
				timeLimitEntity.setBuyNums(0);
				timeLimitEntity.setGiftNums(0);
				timeLimitEntity.setScore(0);
				timeLimitEntity.setNextFreeTime(0);
				timeLimitEntity.setRank(0);
				timeLimitEntity.setEndTime(endTime);
				timeLimitEntity.setStartTime(startTime);
				timeLimitEntity.setBuyTime(System.currentTimeMillis());
				saveTimeLimitEntity(timeLimitEntity);
			}
		}
		// 没有活动id,不做显示
		if (timeLimitEntity.getPid() == 0) {
			return null;
		}
		return timeLimitEntity;
	}

	// 初始化皇陵探宝
	private EventExploreEntity initExploreEntity(String playerID) {
		EventConfigEntity eventInfo = eventConfig.get(EventConstans.EVENT_TYPE_EXPLORE);
		long startTime = DateTimeUtil.getDate(eventInfo.getStartTime()).getTime();
		long endTime = DateTimeUtil.getDate(eventInfo.getEndTime()).getTime();
		EventExploreEntity exploreEntity = getExploreEntity(playerID);
		if (exploreEntity == null) {
			exploreEntity = new EventExploreEntity();
			exploreEntity.setPlayerID(playerID);
			exploreEntity.setFreeNum(EventConstans.EXPLORE_FREENUM);
			exploreEntity.setGoldNum(EventConstans.EXPLORE_GOLDNUM);
			exploreEntity.setStartTime(startTime);
			exploreEntity.setEndTime(endTime);
			exploreEntity.setDateTime(Utilities.getDateTime());
			saveExploreEntity(exploreEntity);
		} else {
			// 新版本活动开启
			if (exploreEntity.getStartTime() != startTime && exploreEntity.getEndTime() != endTime) {
				exploreEntity.setFreeNum(EventConstans.EXPLORE_FREENUM);
				exploreEntity.setGoldNum(EventConstans.EXPLORE_GOLDNUM);
				exploreEntity.setStartTime(startTime);
				exploreEntity.setEndTime(endTime);
				saveExploreEntity(exploreEntity);
			}
		}
		if (!exploreEntity.isRefresh()) {
			exploreEntity.setFreeNum(EventConstans.EXPLORE_FREENUM);
			saveExploreEntity(exploreEntity);
		}
		return exploreEntity;
	}

	// 随机获取道具兑换公式
	private Exchange getRandomExchange(int pid) {
		List<GoodsBean> wantList = new ArrayList<GoodsBean>();
		ExchangeData exchangeData = exchangeMap.get(pid);
		GoodsBean item = DropModule.doDrop(exchangeData.getItemID());
		GoodsBean raw = DropModule.doDrop(exchangeData.getRawID());
		GoodsBean artifacts = DropModule.doDrop(exchangeData.getArtifactsID());
		GoodsBean artifact = DropModule.doDrop(exchangeData.getArtifactID());
		while (artifacts.getPid() == artifact.getPid()) {
			artifact = DropModule.doDrop(exchangeData.getArtifactID());
		}

		GoodsBean result = DropModule.doDrop(exchangeData.getResult());
		wantList.add(item);
		wantList.add(raw);
		wantList.add(artifacts);
		wantList.add(artifact);

		// 如目标物品与兑换材料重复,重新生成目标物品
		while (raw.getPid() == result.getPid()) {
			result = DropModule.doDrop(exchangeData.getResult());
		}
		Exchange exchange = new Exchange();
		exchange.setWantList(wantList);
		exchange.setNum(0);
		exchange.setMaxNum(exchangeData.getDailyTime());
		exchange.setGold(exchangeData.getGold());
		exchange.setResult(result);
		exchange.setPid(pid);
		return exchange;
	}

	// 随机获取充值抽奖物品
	private DrawItem getRandomPayItem(ChargeDropData chargeDropData) {
		int max = chargeDropData.getMax();
		int random = Utilities.getRandomInt(max);
		int weight = 0;
		DrawItem result = null;
		for (DrawItem item : chargeDropData.getItemList()) {
			weight = item.getWeight();
			if (weight >= random) {
				result = item;
				break;
			}
		}
		return result;
	}

	// 随机获取积分抽奖物品
	private DrawItem getRandomIntegralItem() {
		int max = scoreDropData.getMax();
		int random = Utilities.getRandomInt(max);
		int weight = 0;
		DrawItem result = null;
		for (DrawItem item : scoreDropData.getItemList()) {
			weight = item.getWeight();
			if (weight >= random) {
				result = item;
				break;
			}
		}
		return result;
	}

	/**
	 * 从掉落包里获取用来展示的其他物品
	 * 
	 * @param drawItem
	 * @param num
	 * @return
	 */
	private List<GoodsBean> getShowIntegralItem(DrawItem drawItem, int num) {
		List<GoodsBean> goodsBeanList = new ArrayList<>();

		int count = 0;

		while (count < num) {
			DrawItem show = getRandomIntegralItem();
			if (show.getItemId() != drawItem.getItemId()) {
				goodsBeanList.add(new GoodsBean(show.getItemId(), show.getAmount()));
				count++;
			}
		}

		return goodsBeanList;
	}

	// 随机获取探宝物品
	private ExploreItem getRandomExploreItem() {
		int max = exploreData.getMax();
		int random = Utilities.getRandomInt(max);
		int weight = 0;
		ExploreItem result = new ExploreItem();
		for (ExploreItem item : exploreData.getItemList()) {
			weight += item.getWeight();
			if (weight >= random) {
				result.setItemId(item.getItemId());
				result.setAmount(item.getAmount());
				break;
			}
		}
		return result;
	}

	private EventChickenEntity initChickenEntity(String playerID) {
		EventChickenEntity chickenEntity = getChickenEntity(playerID);
		if (chickenEntity == null) {
			chickenEntity = new EventChickenEntity();
			chickenEntity.setPlayerID(playerID);
			chickenEntity.setTimeDate(Utilities.getDateTime());
			saveChickenEntity(chickenEntity);
		}
		if (!chickenEntity.isChicken()) {
			chickenEntity.setFirst(false);
			chickenEntity.setSecond(false);
			saveChickenEntity(chickenEntity);
		}
		return chickenEntity;
	}

	// 初始化活动配置
	private Map<Integer, EventConfigEntity> initEventConfig(boolean sign) {
		List<EventConfigEntity> configEntityList = null;
		if (sign) {
			// 获取全部活动,包含状态1,0
			configEntityList = getAllEventConfigEntity(false);
			// 活动列表为空,读取配置文件生成活动列表
			if (configEntityList == null || configEntityList.size() == 0) {
				Set<Entry<Integer, ActiveData>> set = activeMap.entrySet();
				int index = EventConstans.EVENT_SORT.length;
				for (Entry<Integer, ActiveData> entry : set) {
					EventConfigEntity configEntity = new EventConfigEntity();
					configEntity.setEventID(getEventID() + "");
					configEntity.setPid(entry.getKey());
					boolean hasEvent = false;
					// 活动排序
					for (int i = 0; i < EventConstans.EVENT_SORT.length; i++) {
						if (entry.getKey() == EventConstans.EVENT_SORT[i]) {
							configEntity.setIndex(i);
							hasEvent = true;
							break;
						}
					}
					if (!hasEvent) {
						configEntity.setIndex(index);
						index++;
					}
					configEntity.setStatus(0);
					configEntity.setDesc(activeMap.get(entry.getKey()).getDesc());
					configEntity.setVersion(1);
					configEntity.setStartTime("2014-01-01 00:00:00");
					configEntity.setEndTime("2015-12-31 23:59:59");
					saveEventConfig(configEntity);
				}
			}
		}
		// 获取状态为0的活动,返回给客户端显示
		configEntityList = getAllEventConfigEntity(true);
		eventList = configEntityList;
		Map<Integer, EventConfigEntity> resultMap = new HashMap<Integer, EventConfigEntity>();
		for (EventConfigEntity event : configEntityList) {
			if (event != null) {
				resultMap.put(event.getPid(), event);
			}
		}
		return resultMap;
	}

	public void refreshEventConfig() {
		eventConfig = initEventConfig(true);
	}

	public void refreshEventConfigTime() {
		eventConfig = initEventConfig(false);
	}

	/**
	 * 初始化活动配置ID
	 * */
	private void initEventID() {
		String clsName = SysConstants.eventIDEntity;
		KeyGenerator keyGenerator = keyGeneratorDAO.getEntity(clsName);
		if (keyGenerator == null) {
			keyGenerator = new KeyGenerator();
			keyGenerator.setKey(clsName);
			keyGenerator.setNextId(SysConstants.orderStatID);
			keyGeneratorDAO.save(keyGenerator);
		}
	}

	/**
	 * 获取活动配置ID
	 * */
	public long getEventID() {
		String clsName = SysConstants.eventIDEntity;
		long eventID = keyGeneratorDAO.updateInc(clsName);
		return eventID;
	}

	@Override
	public void init() {
		vipModule = ModuleManager.getModule(ModuleNames.VipModule, VipModule.class);
		loginModule = ModuleManager.getModule(ModuleNames.LoginModule, LoginModule.class);
		rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
		payModule = ModuleManager.getModule(ModuleNames.PayModule, PayModule.class);
		analyseModule = ModuleManager.getModule(ModuleNames.AnalyseModule, AnalyseModule.class);
		heroModule = ModuleManager.getModule(ModuleNames.HeroModule, HeroModule.class);
		mailBoxModule = ModuleManager.getModule(ModuleNames.MailBoxModule, MailBoxModule.class);
		legionModule = ModuleManager.getModule(ModuleNames.LegionModule, LegionModule.class);
		initMysteryItemData();
		initVipDailyMap();
		initVipGrowMap();
		initWelfareMap();
		initChargeGiftMap();
		initConsumeGiftMap();
		initActiveConsumeMap();
		initScoreGainMap();
		initChargeDropMap();
		initChargeActiveMap();
		initExchangeMap();
		initActiveMap();
		initEventID();
		initLimitHeroMap();
		initNewServerActiveData();
		initNewServerRewardData();
		initActiveMessageData();
		initUniversalVerfareMap();
		initChargeDayGiftMap();
		initActiveFortunaData();
		initActiveLuckyDrawData();
		eventConfig = initEventConfig(true);
		// 添加定时发送奖励job 9008011:每日充值 9008012：每日消费 9008013:竞技排行 9008014:冲级活动
		initScheduler(9008011);
		initScheduler(9008012);
		initScheduler(9008013);
		initScheduler(9008014);

	}

	// 神秘商店,商人
	private static Map<Integer, MysteryItemData> mysteryItemMap = new HashMap<Integer, MysteryItemData>();
	// vip 每日礼包
	private static Map<Integer, VipDailyData> vipDailyMap = new HashMap<Integer, VipDailyData>();
	// vip 成长礼包
	private static Map<Integer, VipGrowData> vipGrowMap = new HashMap<Integer, VipGrowData>();
	// 福利活动内容
	private static Map<Integer, WelfareData> welfareMap = new HashMap<Integer, WelfareData>();
	// 开服活动
	private static Map<Integer, NewServerData> newServerMap = new HashMap<Integer, NewServerData>();
	// 开服活动奖励
	private static Map<Integer, NewServerRewardData> newServerRewardMap = new HashMap<Integer, NewServerRewardData>();
	// 财神送礼
	private static Map<Integer, ActiveFortunaData> newActiveFortunaMap = new HashMap<Integer, ActiveFortunaData>();	
	// 幸运抽奖
	private static Map<Integer, ActiveLuckyDrawData> newActiveLuckyDrawMap = new HashMap<Integer, ActiveLuckyDrawData>();
	// 活动奖励信息
	private static Map<Integer, ActiveMessageData> activeMessageMap = new HashMap<Integer, ActiveMessageData>();
	// 皇陵探宝掉落
	private static ExploreData exploreData = TemplateManager.getTemplateData(EventConstans.EXPLORE_DROP_ID, ExploreData.class);
	// 充值送礼活动奖励
	private static Map<Integer, ChargeGiftData> chargeGiftMap = new HashMap<Integer, ChargeGiftData>();
	// 全民福利
	private static Map<Integer, ActiveDailyRewardData> universalVerfareMap = new HashMap<Integer, ActiveDailyRewardData>();
	// 消费送礼活动奖励
	private static Map<Integer, ConsumeGiftData> consumeGiftMap = new HashMap<Integer, ConsumeGiftData>();
	// 每日消费活动奖励
	private static Map<Integer, ActiveConsumeData> activeConsumeMap = new HashMap<Integer, ActiveConsumeData>();
	// 积分抽奖规则
	private static Map<Integer, ScoreGainData> scoreGainMap = new HashMap<Integer, ScoreGainData>();
	// 积分抽奖掉落
	private static ScoreDropData scoreDropData = TemplateManager.getTemplateData(EventConstans.INTEGRAL_DROP_ID, ScoreDropData.class);
	// 充值抽奖规则
	private static Map<Integer, ChargeActiveData> chargeActiveMap = new HashMap<Integer, ChargeActiveData>();
	// 充值抽奖掉落
	private static Map<Integer, ChargeDropData> chargeDropMap = new HashMap<Integer, ChargeDropData>();
	// 道具兑换规则
	private static Map<Integer, ExchangeData> exchangeMap = new HashMap<Integer, ExchangeData>();
	// 活动配置表
	private static Map<Integer, ActiveData> activeMap = new HashMap<Integer, ActiveData>();
	// 限时热卖
	private static Map<Integer, LimitHeroData> limitHeroMap = new HashMap<Integer, LimitHeroData>();
	// 每日充值活动奖励
	private static Map<Integer, ChargeDayGiftData> chargeDayGiftMap = new HashMap<Integer, ChargeDayGiftData>();
	private void initLimitHeroMap() {
		List<LimitHeroData> dataList = TemplateManager.getTemplateList(LimitHeroData.class);
		for (LimitHeroData data : dataList) {
			limitHeroMap.put(data.getPid(), data);
		}
	}

	private void initActiveMap() {
		List<ActiveData> dataList = TemplateManager.getTemplateList(ActiveData.class);
		for (ActiveData data : dataList) {
			activeMap.put(data.getPid(), data);
		}
	}

	private void initExchangeMap() {
		List<ExchangeData> dataList = TemplateManager.getTemplateList(ExchangeData.class);
		for (ExchangeData data : dataList) {
			exchangeMap.put(data.getPid(), data);
		}
	}

	private void initChargeActiveMap() {
		List<ChargeActiveData> dataList = TemplateManager.getTemplateList(ChargeActiveData.class);
		for (ChargeActiveData data : dataList) {
			chargeActiveMap.put(data.getPid(), data);
		}
	}

	private void initChargeDropMap() {
		List<ChargeDropData> dataList = TemplateManager.getTemplateList(ChargeDropData.class);
		for (ChargeDropData data : dataList) {
			chargeDropMap.put(data.getPid(), data);
		}
	}

	private void initScoreGainMap() {
		List<ScoreGainData> dataList = TemplateManager.getTemplateList(ScoreGainData.class);
		for (ScoreGainData data : dataList) {
			scoreGainMap.put(data.getPid(), data);
		}
	}

	private void initChargeGiftMap() {
		List<ChargeGiftData> dataList = TemplateManager.getTemplateList(ChargeGiftData.class);
		for (ChargeGiftData data : dataList) {
			chargeGiftMap.put(data.getPid(), data);
		}
	}
	
	private void initUniversalVerfareMap() {
		List<ActiveDailyRewardData> dataList = TemplateManager.getTemplateList(ActiveDailyRewardData.class);
		for (ActiveDailyRewardData data : dataList) {
			universalVerfareMap.put(data.getPid(), data);
		}
	}

	private void initActiveConsumeMap() {
		List<ActiveConsumeData> dataList = TemplateManager.getTemplateList(ActiveConsumeData.class);
		for (ActiveConsumeData data : dataList) {
			activeConsumeMap.put(data.getPid(), data);
		}
	}
	
	private void initConsumeGiftMap() {
		List<ConsumeGiftData> dataList = TemplateManager.getTemplateList(ConsumeGiftData.class);
		for (ConsumeGiftData data : dataList) {
			consumeGiftMap.put(data.getPid(), data);
		}
	}

	private void initWelfareMap() {
		List<WelfareData> dataList = TemplateManager.getTemplateList(WelfareData.class);
		for (WelfareData data : dataList) {
			welfareMap.put(data.getPid(), data);
		}
	}

	private void initVipGrowMap() {
		List<VipGrowData> dataList = TemplateManager.getTemplateList(VipGrowData.class);
		for (VipGrowData data : dataList) {
			vipGrowMap.put(data.getPid(), data);
		}
	}

	private void initVipDailyMap() {
		List<VipDailyData> dataList = TemplateManager.getTemplateList(VipDailyData.class);
		for (VipDailyData data : dataList) {
			vipDailyMap.put(data.getPid(), data);
		}
	}

	private void initMysteryItemData() {
		List<MysteryItemData> dataList = TemplateManager.getTemplateList(MysteryItemData.class);
		for (MysteryItemData data : dataList) {
			mysteryItemMap.put(data.getPid(), data);
		}
	}

	private void initNewServerActiveData() {
		List<NewServerData> dataList = TemplateManager.getTemplateList(NewServerData.class);
		for (NewServerData data : dataList) {
			newServerMap.put(data.getActiveID(), data);
		}
	}

	private void initNewServerRewardData() {
		List<NewServerRewardData> dataList = TemplateManager.getTemplateList(NewServerRewardData.class);
		for (NewServerRewardData data : dataList) {
			newServerRewardMap.put(data.getPid(), data);
		}
	}
	
	private void initActiveFortunaData() {
		List<ActiveFortunaData> dataList = TemplateManager.getTemplateList(ActiveFortunaData.class);
		for (ActiveFortunaData data : dataList) {
			newActiveFortunaMap.put(data.getPid(), data);
		}
	}
	
	private void initActiveLuckyDrawData() {
		List<ActiveLuckyDrawData> dataList = TemplateManager.getTemplateList(ActiveLuckyDrawData.class);
		for (ActiveLuckyDrawData data : dataList) {
			newActiveLuckyDrawMap.put(data.getPid(), data);
		}
	}
		
	private void initActiveMessageData() {
		List<ActiveMessageData> dataList = TemplateManager.getTemplateList(ActiveMessageData.class);
		for (ActiveMessageData data : dataList) {
			activeMessageMap.put(data.getActiveID(), data);
		}
	}

	private List<EventTimeLimitEntity> getRankList(int size) {
		EventConfigEntity eventInfo = eventConfig.get(EventConstans.EVENT_TYPE_TIME_LIMIT);
		List<EventTimeLimitEntity> rankList = new ArrayList<EventTimeLimitEntity>();
		if (eventInfo == null) {
			return rankList;
		}
		long startTime = DateTimeUtil.getDate(eventInfo.getStartTime()).getTime();
		long endTime = DateTimeUtil.getDate(eventInfo.getEndTime()).getTime();
		// 排行榜
		rankList = timeLimitDao.getRanks(startTime, endTime, size);
		return rankList;
	}

	public void saveWarGodEntity(WarGodRankEntity entity) {
		warGodEntityDao.save(entity);
	}

	public WarGodRankEntity getWarGodRankEntity() {
		WarGodRankEntity entity = warGodEntityDao.getEntity(warGodKey);
		if (entity == null) {
			entity = this.warGodRankList();
		}
		return entity;
	}

	public void saveTimeLimitEntity(EventTimeLimitEntity timeLimitEntity) {
		timeLimitDao.save(timeLimitEntity);
	}

	public EventTimeLimitEntity getTimeLimitEntity(String playerID, long startTime, long endTime) {
		return timeLimitDao.getTimeLimitEntity(playerID, startTime, endTime);
	}

	public void saveEventMonthCardEntity(EventMonthCardEntity monthCardEntity) {
		monthCardDao.save(monthCardEntity);
	}

	public EventMonthCardEntity getEventMonthCardEntity(String playerID) {
		return monthCardDao.getMonthCardEntity(playerID);
	}

	public void saveEventConfig(EventConfigEntity eventConfigEntity) {
		configDao.save(eventConfigEntity);
	}

	public EventConfigEntity getEventConfigByPid(int pid) {
		return configDao.getEventConfigEntityByPid(pid);
	}

	public List<EventConfigEntity> getAllEventConfigEntity(boolean sign) {
		return configDao.getAllEventConfigEntity(sign);
	}

	public void saveExchangeEntity(EventExchangeEntity exchangeEntity) {
		exchangeDao.save(exchangeEntity);
	}

	public EventExchangeEntity getExchangeEntity(String playerID) {
		return exchangeDao.getExchangeEntity(playerID);
	}

	public void saveDrawIntegralEntity(EventDrawIntegralEntity integralEntity) {
		drawIntegralDao.save(integralEntity);
	}

	public EventDrawIntegralEntity getDrawIntegralEntity(String playerID) {
		return drawIntegralDao.getDrawIntegralEntity(playerID);
	}

	public void saveDrawPayEntity(EventDrawPayEntity drawPayEntity) {
		drawPayDao.save(drawPayEntity);
	}

	public EventDrawPayEntity getDrawPayEntity(String playerID) {
		return drawPayDao.getDrawPayEntity(playerID);
	}

	public void saveExpenseEntity(EventExpenseEntity expenseEntity) {
		expenseDao.save(expenseEntity);
	}

	public EventExpenseEntity getEventExpenseEntity(String playerID) {
		return expenseDao.getExpenseEntity(playerID);
	}

	public void saveEventPayEntity(EventPayEntity eventPayEntity) {
		eventPayDao.save(eventPayEntity);
	}

	public EventFortunaGiftsEntity getEventFortunaGiftsEntity(String playerID) {
		return eventFortunaGiftsEntityDao.getFortunaGiftsEntity(playerID);
	}
	
	public EventUniversalVerfareEntity getEventUniversalVerfareEntity(String playerID) {
		return eventUniversalVerfareDao.getUniversalVerfareEntity(playerID);
	}
	
	public EventLuckyDrawEntity getEventLuckyDrawEntity(String playerID) {
		return eventLuckyDrawDao.getLuckyDrawEntity(playerID);
	}
	
	public void saveEventLuckyDrawEntity(EventLuckyDrawEntity eventLuckyDrawEntity) {
		eventLuckyDrawDao.save(eventLuckyDrawEntity);
	}
	
	public void saveEventFortunaGiftsEntity(EventFortunaGiftsEntity eventFortunaGiftsEntity) {
		eventFortunaGiftsEntityDao.save(eventFortunaGiftsEntity);
	}
	
	public void saveEventUniversalVerfareEntity(EventUniversalVerfareEntity eventUniversalVerfareEntity) {
		eventUniversalVerfareDao.save(eventUniversalVerfareEntity);
	}
	
	public EventPayEntity getEventPayEntity(String playerID) {
		return eventPayDao.getEventPayEntity(playerID);
	}

	public void saveExploreEntity(EventExploreEntity exploreEntity) {
		exploreDao.save(exploreEntity);
	}

	public EventExploreEntity getExploreEntity(String playerID) {
		return exploreDao.getExploreEntity(playerID);
	}

	public void saveVipGrowEntity(EventVipGrowEntity vipGrowEntity) {
		vipGrowEntityDao.save(vipGrowEntity);
	}

	public EventVipGrowEntity getVipGrowEntity(String playerID) {
		return vipGrowEntityDao.getVipGrowEntity(playerID);
	}

	public void saveVipDailyEntity(EventVipDailyEntity vipDailyEntity) {
		vipDailyEntityDao.save(vipDailyEntity);
	}

	public EventVipDailyEntity getVipDailyEntity(String playerID) {
		return vipDailyEntityDao.getVipDailyEntity(playerID);
	}

	public EventChickenEntity getChickenEntity(String playerID) {
		return chickenEntityDao.getChickenEntity(playerID);
	}

	public void saveChickenEntity(EventChickenEntity chickenEntity) {
		chickenEntityDao.save(chickenEntity);
	}

	public EventShopEntity getShopEntity(String playerID) {
		return shopEntityDao.getShopEntity(playerID);
	}

	public void saveShopEntity(EventShopEntity shopEntity) {
		shopEntityDao.save(shopEntity);
	}

	public EventTraderEntity getTraderEntity(String playerID) {
		return traderEntityDao.getTraderEntity(playerID);
	}

	public void saveTraderEntity(EventTraderEntity traderEntity) {
		traderEntityDao.save(traderEntity);
	}

	public NewServerEventEntity getNewServerEventEntity(String pid) {
		return newServerDao.getNewServerEventEntity(pid);
	}

	public void saveNewServerEventEntity(NewServerEventEntity entity) {
		newServerDao.save(entity);
	}

	public List<EventPayEntity> getEventPayList() {
		return eventPayDao.getPayRank();
	}

	public List<EventExpenseEntity> getExpenseRank() {
		return expenseDao.getExpenseRank();
	}

}
