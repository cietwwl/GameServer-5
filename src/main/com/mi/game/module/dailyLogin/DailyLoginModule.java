package com.mi.game.module.dailyLogin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.mi.core.dao.KeyGeneratorDAO;
import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.TemplateManager;
import com.mi.core.engine.annotation.Module;
import com.mi.core.util.DateTimeUtil;
import com.mi.game.defines.ErrorIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.defines.RewardType;
import com.mi.game.defines.SysConstants;
import com.mi.game.module.base.BaseModule;
import com.mi.game.module.dailyLogin.dao.DailyLoginEntityDao;
import com.mi.game.module.dailyLogin.dao.DailyLoginPayEntityDao;
import com.mi.game.module.dailyLogin.data.DailyLoginRewardData;
import com.mi.game.module.dailyLogin.pojo.DailyLoginEntity;
import com.mi.game.module.dailyLogin.pojo.DailyLoginPayEntity;
import com.mi.game.module.dailyLogin.protocol.DailyLoginPayProtocol;
import com.mi.game.module.event.dao.EventConfigDao;
import com.mi.game.module.event.data.ActiveData;
import com.mi.game.module.event.data.ChargeDayGiftData;
import com.mi.game.module.event.define.EventConstans;
import com.mi.game.module.event.pojo.EventConfigEntity;
import com.mi.game.module.event.util.EventUtils;
import com.mi.game.module.limitShop.dao.LimitShopEntityDao;
import com.mi.game.module.limitShop.data.LimitShopData;
import com.mi.game.module.reward.RewardModule;
import com.mi.game.module.reward.data.GoodsBean;
import com.mi.game.module.wallet.WalletModule;
import com.mi.game.util.Utilities;

@Module(name = ModuleNames.DailyLoginModule, clazz = DailyLoginModule.class)
public class DailyLoginModule extends BaseModule {
	private static RewardModule rewardModule;
	private static WalletModule walletModule;
	private static LimitShopEntityDao limitShopEntityDao = LimitShopEntityDao.getInstance();
	private static DailyLoginPayEntityDao dailyLoginPayEntityDao = DailyLoginPayEntityDao.getInstance();
	private static DailyLoginEntityDao loginEntityDao = DailyLoginEntityDao.getInstance();
	private static EventConfigDao configDao = EventConfigDao.getInstance();
	private final KeyGeneratorDAO keyGeneratorDAO = KeyGeneratorDAO.getInstance();

	private static Map<Integer, LimitShopData> limitShopMap = new HashMap<Integer, LimitShopData>();
	// 每日充值活动奖励
	private static Map<Integer, ChargeDayGiftData> chargeDayGiftMap = new HashMap<Integer, ChargeDayGiftData>();
	// 连续登录奖励
	private static Map<Integer, DailyLoginRewardData> loginRewardMap = new HashMap<Integer, DailyLoginRewardData>();
	// 活动配置
	private static Map<Integer, EventConfigEntity> eventConfig;
	// 活动排序
	private static List<EventConfigEntity> eventList;
	// 活动配置表
	private static Map<Integer, ActiveData> activeMap = new HashMap<Integer, ActiveData>();

	@Override
	public void init() {
		// TODO 初始化各种参数
		rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
		walletModule = ModuleManager.getModule(ModuleNames.WalletModule, WalletModule.class);

		initActiveMap();
		eventConfig = initEventConfig(true);

		// TODO 初始化模版
		initChargeDayGiftMap();
		initLoginRewardData(); // 每日登录、充值奖励
	}

	/**
	 * 查询奖励模版以及已经领取的奖励
	 * 
	 * @param playerID
	 * @param protocol
	 */
	public void activeLoginPayInit(String playerID, DailyLoginPayProtocol protocol) {
		List<DailyLoginRewardData> dataList = TemplateManager.getTemplateList(DailyLoginRewardData.class);
		if (dataList != null && dataList.size() > 0) {
			protocol.setRewardList(dataList); // 奖励模版列表
		}
		String today = Utilities.getDateTime("yyyyMMdd");
		this.getDailyRewardList(playerID, protocol, today);

		EventConfigEntity eventConfigEntity = eventConfig.get(EventConstans.EVENT_TYPE_DAILY_LOGIN);

		int todayReward = 0;
		if (eventConfigEntity != null) {
			todayReward = eventConfigEntity.getInfoPid();
		}
		if (todayReward != 0) {
			protocol.setTodayReward(todayReward);
		}
		int payNum = getTodayPayNum(playerID, today);
		protocol.setPayNum(payNum);
		// 查询今日充值金额
	}

	public int getTodayPayNum(String playerID, String today) {
		DailyLoginPayEntity payEntity = dailyLoginPayEntityDao.getEventPayEveryDayEntity(playerID, today);
		if (payEntity != null) {
			return payEntity.getPayTotal();
		}
		return 0;
	}

	/**
	 * 根据活动pid获取活动奖励
	 * 
	 * @param activeID
	 *            活动pid
	 */
	public void getActiveReward(String activeID, DailyLoginPayProtocol protocol) {
		if (activeID == null || "".equals(activeID)) {
			protocol.setCode(ErrorIds.ParamWrong);
			return;
		}
		EventConfigEntity eventConfigEntity = eventConfig.get(EventConstans.EVENT_TYPE_DAILY_LOGIN);
		if (eventConfigEntity == null) {
			protocol.setCode(ErrorIds.EVENT_NOINTIME);
			return;
		}
		int rewardPid = eventConfigEntity.getInfoPid(); // 获得奖励模版id
		DailyLoginRewardData rewardData = loginRewardMap.get(rewardPid);
		if (rewardData == null) {
			protocol.setCode(ErrorIds.REWARD_NOTFOUND);
			return;
		}
	}

	/**
	 * 查询已经领取的登录充值奖励
	 * 
	 * @param playerID
	 *            玩家ID
	 * @param day
	 *            日期,格式yyyyMMdd
	 * @param protocol
	 */
	public void getDailyRewardList(String playerID, DailyLoginPayProtocol protocol, String today) {
		List<Integer> loginList = loginEntityDao.queryLoginRewardList(playerID, today);
		if (loginList != null && loginList.size() > 0) {
			protocol.setLoginDrawList(loginList);
		}
		List<Integer> payList = dailyLoginPayEntityDao.queryLoginRewardList(playerID, today);
		if (payList != null && payList.size() > 0) {
			protocol.setPayDrawList(payList);
		}
	}

	/**
	 * 获取登录签到奖励
	 * 
	 * @param playerID
	 *            玩家ID
	 * @param rewardId
	 *            奖励id
	 * @param num
	 */
	public void getDailyLoginReward(String playerID, String rewardID, DailyLoginPayProtocol protocol, IOMessage ioMessage) {
		Map<String, Object> itemMap = new HashMap<String, Object>();
		DailyLoginEntity loginEntity = getDailyLoginEntity(playerID);
		if (loginEntity == null) {
			loginEntity = new DailyLoginEntity();
			loginEntity.setPlayerID(playerID);
			EventConfigEntity eventInfo = eventConfig.get(EventConstans.EVENT_TYPE_CONSUMED_DAILY);
			String start = eventInfo.getStartTime();
			String end = eventInfo.getEndTime();
			long startTime = EventUtils.strToDate(start, EventConstans.YMDHMS).getTime();
			long endTime = EventUtils.strToDate(start, EventConstans.YMDHMS).getTime();
			loginEntity.setBeginTime(startTime);
			loginEntity.setEndTime(endTime);
		}
		// TODO检查是不是在活动范围内
		// 检查奖励id
		if (null == rewardID || rewardID.isEmpty()) {
			protocol.setCode(ErrorIds.WELFARE_REWARDID_NOTNULL);
			return;
		}
		int rewardIntID = Integer.parseInt(rewardID);
		// 检查是否领取
		if (loginEntity.isGetReward(rewardIntID)) {
			protocol.setCode(ErrorIds.WELFARE_REWARD_ISGET);
			return;
		}
		// 查找奖励信息
		DailyLoginRewardData loginRewardData = loginRewardMap.get(rewardIntID);
		if (loginRewardData == null) {
			protocol.setCode(ErrorIds.WELFARE_REWARD_NOTFOUND);
			return;
		}

		// 封装奖励物品
		String[] itemArr = loginRewardData.getItemID().split(",");
		List<GoodsBean> goodsList = new ArrayList<GoodsBean>();
		for (String reward : itemArr) {
			int pid = Integer.valueOf(reward.split("=")[0]);
			int num = Integer.valueOf(reward.split("=")[1]);
			GoodsBean goods = new GoodsBean();
			goods.setPid(pid);
			goods.setNum(num);
			goodsList.add(goods);
		}
		// 增加奖励物品
		int code = rewardModule.addGoods(playerID, goodsList, true, null, itemMap, ioMessage);
		if (code != 0) {
			protocol.setCode(code);
			return;
		}
		loginEntity.addRewardID(loginRewardData.getPid());
		loginEntity.setDay(Utilities.getDateTime("yyyyMMdd"));
		loginEntityDao.save(loginEntity);
		protocol.setLoginDrawList(loginEntity.getRewardList());
		protocol.setItemMap(itemMap);
	}

	public DailyLoginEntity getDailyLoginEntity(String playerID) {
		String today = Utilities.getDateTime("yyyyMMdd");
		DailyLoginEntity loginEntity = loginEntityDao.getLoginEntity(playerID, today);
		EventConfigEntity eventInfo = eventConfig.get(EventConstans.EVENT_TYPE_CONSUMED_DAILY);
		if (loginEntity != null) { // 检查开始结束时间跟现在的活动时间是否一致
			String start = eventInfo.getStartTime();
			String end = eventInfo.getEndTime();
			long startTime = EventUtils.strToDate(start, EventConstans.YMDHMS).getTime();
			long endTime = EventUtils.strToDate(start, EventConstans.YMDHMS).getTime();

			if (loginEntity.getBeginTime() != startTime || loginEntity.getEndTime() != endTime) {
				loginEntity.setBeginTime(startTime);
				loginEntity.setEndTime(endTime);
				loginEntity.getRewardList().clear();
				loginEntityDao.save(loginEntity);
			}
		}
		return loginEntityDao.getLoginEntity(playerID, today);
	}

	/**
	 * 每日登录充值活动,领取奖励
	 * 
	 * @param playerID
	 *            玩家id
	 * @param rewardID
	 *            奖励模版id
	 * @param protocol
	 */
	public void getDailyPayReward(String playerID, String rewardID, DailyLoginPayProtocol protocol, IOMessage ioMessage) {
		if (rewardID == null || rewardID.isEmpty()) {
			protocol.setCode(ErrorIds.REWARD_ID_NOTNULL);
			return;
		}
		// 获取奖励pid
		int rewardIntID = Integer.parseInt(rewardID);
		if (!loginRewardMap.containsKey(rewardIntID)) {
			protocol.setCode(ErrorIds.REWARD_NOTFOUND);
			return;
		}
		// 获取充值奖励实体
		DailyLoginPayEntity loginPayEntity = initEventPayEveryDayEntity(playerID);
		// 检查领取记录
		if (loginPayEntity.isReward(rewardIntID)) {
			protocol.setCode(ErrorIds.REWARD_ITEM_ISHAS);
			return;
		}

		// 检查充值金额
		if (loginPayEntity.getPayTotal() <= 0) {
			protocol.setCode(ErrorIds.REWARD_NOTENOUGH);
			return;
		}
		// 获取奖励内容
		DailyLoginRewardData loginRewardData = loginRewardMap.get(rewardIntID);
		String[] chargeItemArr = loginRewardData.getChargeItemID().split(",");
		List<GoodsBean> goodsList = new ArrayList<GoodsBean>();
		for (String reward : chargeItemArr) {
			GoodsBean goods = new GoodsBean();
			int pid = Integer.valueOf(reward.split("=")[0]);
			int num = Integer.valueOf(reward.split("=")[1]);
			goods.setPid(pid);
			goods.setNum(num);
			goodsList.add(goods);
		}
		Map<String, Object> itemMap = new HashMap<String, Object>();
		// 增加物品
		int code = rewardModule.addGoods(playerID, goodsList, true, null, itemMap, ioMessage);
		if (code != 0) {
			protocol.setCode(code);
			return;
		}
		// 增加领取记录
		loginPayEntity.addReward(rewardIntID);
		dailyLoginPayEntityDao.save(loginPayEntity);
		protocol.setPayDrawList(loginPayEntity.getRewardList());
		protocol.setItemMap(itemMap);
		// protocol.setShowMap(goodsList);
	}

	/**
	 * 每日充值,发送昨天未领取的奖励
	 * 
	 * @param playerID
	 *            玩家ID
	 * @param eventPID
	 *            活动ID
	 */
	public void sendLoginPayReward(String playerID, int eventPID) {
		// 获取昨天的充值记录
		String yesterday = Utilities.getYesterDay("yyyyMMdd");
		DailyLoginPayEntity loginPayEntity = dailyLoginPayEntityDao.getEventPayEveryDayEntity(playerID, yesterday);
		if (loginPayEntity == null) { // 没有充值记录
			return;
		}
		// 获得已经领取的充值奖励
		List<Integer> rewardList = loginPayEntity.getRewardList();
		List<Integer> waitList = new ArrayList<Integer>(); // 待领取奖励

		// 循环奖励Map,判断是否领取过
		for (Map.Entry<Integer, ChargeDayGiftData> entry : chargeDayGiftMap.entrySet()) {
			int rewardId = entry.getKey();
			if (!rewardList.contains(rewardId)) { // 没有领取过,判断是否符合领取条件
				int type = this.checkCanGetPayEveryDayReward(playerID, rewardId, yesterday);
				if (type == 0) { // 可以领取
					waitList.add(rewardId);
				}
			}
		}

		// 获取待领取奖励列表
		if (waitList.size() > 0) { // 有未领取的记录,循环领取
			List<GoodsBean> goodsList = new ArrayList<GoodsBean>();
			for (Integer rewardIntID : waitList) {
				ChargeDayGiftData chargeGiftData = chargeDayGiftMap.get(rewardIntID);
				List<Integer> rewardTypeList = chargeGiftData.getKeys(chargeGiftData.getReward());
				for (Integer rewardType : rewardTypeList) {
					GoodsBean goods = new GoodsBean(rewardType, chargeGiftData.getRewardValue(rewardType));
					goodsList.add(goods);
				}
				loginPayEntity.addReward(rewardIntID); // 添加到已领取奖励列表
			}
			if (goodsList.size() > 0) {
				rewardModule.addReward(playerID, goodsList, RewardType.event_pay_everyday); // 将奖励发送到奖励中心

				dailyLoginPayEntityDao.save(loginPayEntity); // 保存领取记录
			}
		}
	}

	/**
	 * 每日充值,判断是否符合奖励领取条件
	 * 
	 * @param playerID
	 * @param rewardID
	 * @return
	 */
	public int checkCanGetPayEveryDayReward(String playerID, int rewardID, String day) {
		// 获取充值奖励实体
		DailyLoginPayEntity loginPayEntity = dailyLoginPayEntityDao.getEventPayEveryDayEntity(playerID, day);
		if (loginPayEntity == null) { // 没有充值记录
			return 1;
		}
		// 检查领取记录
		if (loginPayEntity.isReward(rewardID)) { // 已经领取过
			return 1;
		}
		// 获取奖励内容
		ChargeDayGiftData chargeGiftData = chargeDayGiftMap.get(rewardID);
		// 检查领取条件
		if (chargeGiftData.getGold() > loginPayEntity.getPayTotal()) { // 不符合领取条件
			return 1;
		}
		return 0;
	}

	// TODO 登录充值接口
	/**
	 * 每日充值活动数据收集接口
	 * 
	 * @param playerID
	 */
	public void intefaceDailyLoginPay(String playerID, int payNum) {
		EventConfigEntity eventInfo = eventConfig.get(EventConstans.EVENT_TYPE_PAY_EVERYDAY);
		if (eventInfo != null) {
			if (EventUtils.isEventTime(eventInfo, EventConstans.YMDHMS)) { // 在活动范围内
				// 1.查询今天有没有充值记录,有的话累加
				DailyLoginPayEntity loginPayEntity = initEventPayEveryDayEntity(playerID);
				// 2.今天没有充值记录,新增充值记录
				loginPayEntity.addPayTotal(payNum);
				dailyLoginPayEntityDao.save(loginPayEntity);
			}
		}
	}

	/**
	 * 初始化登录充值实体
	 * 
	 * @param playerID
	 * @return
	 */
	private DailyLoginPayEntity initEventPayEveryDayEntity(String playerID) {
		EventConfigEntity eventInfo = eventConfig.get(EventConstans.EVENT_TYPE_PAY);
		long startTime = DateTimeUtil.getDate(eventInfo.getStartTime()).getTime();
		long endTime = DateTimeUtil.getDate(eventInfo.getEndTime()).getTime();
		String today = Utilities.getDateTime("yyyyMMdd");
		DailyLoginPayEntity loginPayEntity = dailyLoginPayEntityDao.getEventPayEveryDayEntity(playerID, today);
		if (loginPayEntity == null) {
			loginPayEntity = new DailyLoginPayEntity();
			loginPayEntity.setPlayerID(playerID);
			loginPayEntity.setEndTime(endTime);
			loginPayEntity.setStartTime(startTime);
			loginPayEntity.setDay(today);
			dailyLoginPayEntityDao.save(loginPayEntity);
		}
		return loginPayEntity;
	}

	/**
	 * 初始化充值奖励列表
	 */
	private void initChargeDayGiftMap() {
		List<ChargeDayGiftData> dataList = TemplateManager.getTemplateList(ChargeDayGiftData.class);
		for (ChargeDayGiftData data : dataList) {
			chargeDayGiftMap.put(data.getPid(), data);
		}
	}

	public List<EventConfigEntity> getAllEventConfigEntity(boolean sign) {
		return configDao.getAllEventConfigEntity(sign);
	}

	private void initActiveMap() {
		List<ActiveData> dataList = TemplateManager.getTemplateList(ActiveData.class);
		for (ActiveData data : dataList) {
			activeMap.put(data.getPid(), data);
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

	// 初始化活动配置
	private Map<Integer, EventConfigEntity> initEventConfig(boolean sign) {
		List<EventConfigEntity> configEntityList = null;
		if (sign) {
			// 获取全部活动,包含状态1,0
			configEntityList = getAllEventConfigEntity(true);
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

	public void saveEventConfig(EventConfigEntity eventConfigEntity) {
		configDao.save(eventConfigEntity);
	}

	private void initLoginRewardData() {
		List<DailyLoginRewardData> dataList = TemplateManager.getTemplateList(DailyLoginRewardData.class);
		for (DailyLoginRewardData data : dataList) {
			loginRewardMap.put(data.getPid(), data);
		}
	}
}
