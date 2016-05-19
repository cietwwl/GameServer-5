package com.mi.game.module.welfare;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.TemplateManager;
import com.mi.core.engine.annotation.Module;
import com.mi.core.util.DateTimeUtil;
import com.mi.game.defines.ActionType;
import com.mi.game.defines.ErrorIds;
import com.mi.game.defines.KindIDs;
import com.mi.game.defines.ModuleNames;
import com.mi.game.defines.RescueType;
import com.mi.game.module.base.BaseModule;
import com.mi.game.module.login.LoginModule;
import com.mi.game.module.login.pojo.PlayerEntity;
import com.mi.game.module.mainTask.MainTaskModule;
import com.mi.game.module.pay.PayModule;
import com.mi.game.module.pay.pojo.PayEntity;
import com.mi.game.module.reward.RewardModule;
import com.mi.game.module.reward.data.GoodsBean;
import com.mi.game.module.welfare.dao.WelfareEntityDao;
import com.mi.game.module.welfare.dao.WelfareLevelEntityDao;
import com.mi.game.module.welfare.dao.WelfareLoginEntityDao;
import com.mi.game.module.welfare.dao.WelfareMonthEntityDao;
import com.mi.game.module.welfare.dao.WelfareOnlineEntityDao;
import com.mi.game.module.welfare.dao.WelfareRescueSunEntityDao;
import com.mi.game.module.welfare.dao.WelfareSignEntityDao;
import com.mi.game.module.welfare.data.LevelRewardData;
import com.mi.game.module.welfare.data.LoginRewardData;
import com.mi.game.module.welfare.data.MonkeyActiveData;
import com.mi.game.module.welfare.data.MonthRewardData;
import com.mi.game.module.welfare.data.OnlineTimeRewardData;
import com.mi.game.module.welfare.data.SigninRewardData;
import com.mi.game.module.welfare.define.WelfareConstants;
import com.mi.game.module.welfare.pojo.WelfareEntity;
import com.mi.game.module.welfare.pojo.WelfareLevelEntity;
import com.mi.game.module.welfare.pojo.WelfareLoginEntity;
import com.mi.game.module.welfare.pojo.WelfareMonthEntity;
import com.mi.game.module.welfare.pojo.WelfareOnlineEntity;
import com.mi.game.module.welfare.pojo.WelfareRescueSunEntity;
import com.mi.game.module.welfare.pojo.WelfareSignEntity;
import com.mi.game.module.welfare.protocol.WelfareAllProtocol;
import com.mi.game.module.welfare.protocol.WelfareLevelProtocol;
import com.mi.game.module.welfare.protocol.WelfareLoginProtocol;
import com.mi.game.module.welfare.protocol.WelfareMonthProtocol;
import com.mi.game.module.welfare.protocol.WelfareOnlineProtocol;
import com.mi.game.module.welfare.protocol.WelfareRescueSunProtocol;
import com.mi.game.module.welfare.protocol.WelfareSignProtocol;
import com.mi.game.util.Utilities;

@Module(name = ModuleNames.WelfareModule, clazz = WelfareModule.class)
public class WelfareModule extends BaseModule {
	private static RewardModule rewardModule;
	private static LoginModule loginModule;

	private static WelfareSignEntityDao signEventDao = WelfareSignEntityDao.getInstance();
	private static WelfareLoginEntityDao loginEntityDao = WelfareLoginEntityDao.getInstance();
	private static WelfareLevelEntityDao levelEntityDao = WelfareLevelEntityDao.getInstance();
	private static WelfareOnlineEntityDao onlineEntityDao = WelfareOnlineEntityDao.getInstance();
	private static WelfareEntityDao welfEntityDao = WelfareEntityDao.getInstance();
	private static WelfareMonthEntityDao monthEntityDao = WelfareMonthEntityDao.getInstance();
	private static WelfareRescueSunEntityDao rescueSunEntityDao = WelfareRescueSunEntityDao.getInstance();

	// 在线时间奖励
	private static Map<Integer, OnlineTimeRewardData> onlineRewardMap = new HashMap<Integer, OnlineTimeRewardData>();
	// 等级奖励
	private static Map<Integer, LevelRewardData> levelRewardMap = new HashMap<Integer, LevelRewardData>();
	// 登录奖励
	private static Map<Integer, LoginRewardData> loginRewardMap = new HashMap<Integer, LoginRewardData>();
	// 签到奖励
	private static Map<Integer, SigninRewardData> signinRewardMap = new HashMap<Integer, SigninRewardData>();
	// 月签到奖励
	private static Map<Integer, MonthRewardData> monthRewardMap = new HashMap<Integer, MonthRewardData>();

	/**
	 * 获取全部福利信息
	 * 
	 * @param playerID
	 * @param protocol
	 */
	public void welfareAll(String playerID, String welfareType, WelfareAllProtocol protocol) {

		if (StringUtils.isNotEmpty(welfareType)) {
			if ("login".equals(welfareType)) {
				WelfareLoginEntity loginEntity = getLoginEntity(playerID);
				// 检查当天登录
				if (!loginEntity.isLogin(true)) {
					// 增加登录次数
					loginEntity.addLoginNum();
					saveLoginEntity(loginEntity);
				}
				protocol.setLoginEntity(loginEntity);
			}

			if ("month".equals(welfareType)) {
				WelfareMonthEntity monthEntity = getWelfareMonthEntity(playerID);
				// 检查月份是否相同
				if (Calendar.getInstance().get(Calendar.MONTH) + 1 != monthEntity.getMonth()) {
					monthEntity.setLoginNum(0);
					monthEntity.setMonth(Calendar.getInstance().get(Calendar.MONTH) + 1);
					saveWelfareMonthEntity(monthEntity);
				}
				// 检查当天登录
				if (!monthEntity.isLogin(true)) {
					// 增加登录次数
					monthEntity.addLoginNum();
					saveWelfareMonthEntity(monthEntity);
				}
				protocol.setMonthEntity(monthEntity);
			}

			return;
		}
		WelfareEntity welfareEntity = getWelfareEntity(playerID);
		if (welfareEntity == null) {
			welfareEntity = new WelfareEntity();
			welfareEntity.setPlayerID(playerID);
			welfareEntity.setWelfareList(Arrays.asList(WelfareConstants.WELFARE_DEFAULT_ALL2));
			welfareEntity.setDateTime(Utilities.getDateTime());
			saveWelfareEntity(welfareEntity);
		}
		// 登录福利
		if (welfareEntity.getWelfareList().contains(WelfareConstants.WELFARE_DEFAULT_ALL[0])) {
			WelfareLoginEntity loginEntity = getLoginEntity(playerID);
			if (loginEntity != null) {
				// 检查当天登录
				if (!loginEntity.isLogin(true)) {
					// 增加登录次数
					loginEntity.addLoginNum();
					saveLoginEntity(loginEntity);
				}
			} else {
				loginEntity = new WelfareLoginEntity();
				loginEntity.setLoginNum(1);
				loginEntity.setPlayerID(playerID);
				loginEntity.setLoginTime(Utilities.getDateTime());
				saveLoginEntity(loginEntity);
			}
			protocol.setLoginEntity(loginEntity);
		}

		// 签到福利
		if (welfareEntity.getWelfareList().contains(WelfareConstants.WELFARE_DEFAULT_ALL[3])) {
			// WelfareSignEntity signEntity = getSignEntity(playerID);
			// if (signEntity != null) {
			// // 检查是否连续登录
			// if (signEntity.isContinuous(true)) {
			// // 检查当天是否已经登录
			// if (!signEntity.isLogin(true)) {
			// signEntity.addSignNum(true);
			// }
			// } else {
			// signEntity.refreshReward(true);
			// }
			// saveSignEntity(signEntity);
			// } else {
			// signEntity = new WelfareSignEntity();
			// signEntity.setPlayerID(playerID);
			// signEntity.setSignNum(1);
			// signEntity.setLoginTime(Utilities.getDateTime());
			// saveSignEntity(signEntity);
			// }
			// protocol.setSignEntity(signEntity);
		}

		// 月签到福利
		if (welfareEntity.getWelfareList().contains(WelfareConstants.WELFARE_DEFAULT_ALL[4])) {
			WelfareMonthEntity monthEntity = getWelfareMonthEntity(playerID);
			if (monthEntity != null) {
				// 检查月份是否相同
				if (Calendar.getInstance().get(Calendar.MONTH) + 1 != monthEntity.getMonth()) {
					monthEntity.setLoginNum(0);
					monthEntity.setMonth(Calendar.getInstance().get(Calendar.MONTH) + 1);
					saveWelfareMonthEntity(monthEntity);
				}
				// 检查当天登录
				if (!monthEntity.isLogin(true)) {
					// 增加登录次数
					monthEntity.addLoginNum();
					saveWelfareMonthEntity(monthEntity);
				}
			} else {
				monthEntity = new WelfareMonthEntity();
				monthEntity.setLoginNum(1);
				monthEntity.setPlayerID(playerID);
				monthEntity.setLoginTime(Utilities.getDateTime());
				monthEntity.setMonth(Calendar.getInstance().get(Calendar.MONTH) + 1);
				saveWelfareMonthEntity(monthEntity);
			}
			protocol.setMonthEntity(monthEntity);
		}

		// 等级福利
		if (welfareEntity.getWelfareList().contains(WelfareConstants.WELFARE_DEFAULT_ALL[1])) {
			WelfareLevelEntity levelEntity = getLevelEntity(playerID);
			protocol.setLevelEntity(levelEntity);
		}

		// 在线福利
		if (welfareEntity.getWelfareList().contains(WelfareConstants.WELFARE_DEFAULT_ALL[2])) {
			WelfareOnlineEntity onlineEntity = getOnlineEntity(playerID);
			String nowTime = Utilities.getDateTime();
			if (onlineEntity != null && !nowTime.equals(onlineEntity.getRewardTime())) {
				onlineEntity.setRewardID(0);
				onlineEntity.setRewardTime(nowTime);
				onlineEntity.setRewardList(new ArrayList<Integer>());
				saveOnlineEntity(onlineEntity);
			}
			protocol.setOnlineEntity(onlineEntity);
		}
		// 解救悟空
		if (welfareEntity.getWelfareList().contains(WelfareConstants.WELFARE_DEFAULT_ALL[5])) {
			WelfareRescueSunEntity rescueSunEntity = this.getWelfareRescueSunEntity(playerID);
			protocol.setRescueSunEntity(rescueSunEntity);
		}
		protocol.setWelfareEntity(welfareEntity);
	}

	/**
	 * 解救悟空
	 * */
	public void rescueSun(String playerID, int rescueType, WelfareRescueSunProtocol protocol, IOMessage ioMessage) {
		WelfareRescueSunEntity rescueSunEntity = this.getWelfareRescueSunEntity(playerID);
		boolean get = rescueSunEntity.isGet();
		MonkeyActiveData monkeyActiveData = TemplateManager.getTemplateData(RescueType.dataID, MonkeyActiveData.class);
		int total = 0;
		PayModule payModule = ModuleManager.getModule(ModuleNames.PayModule, PayModule.class);
		PayEntity payEntity = payModule.getPayEntity(playerID);
		if (payEntity != null) {
			total = payEntity.getPayTotal();
		}
		long endTime = rescueSunEntity.getOverTime();
		Map<String, GoodsBean> showMap = new HashMap<>();
		Map<String, Object> itemMap = new HashMap<>();
		int code = 0;
		switch (rescueType) {
		case RescueType.RESCUTE:
			if (get) {
				logger.error("已经领取过孙悟空");
				throw new IllegalArgumentException(ErrorIds.ALREADYGETSUNWUKONG + "");
			}
			if (endTime != 0) {
				logger.error("孙武空正在解救中");
				throw new IllegalArgumentException(ErrorIds.RESUCEINGSUNWUKONG + "");
			}
			if (total < RescueType.freeTotal) {
				String timeString = monkeyActiveData.getTime();
				Date date = DateTimeUtil.getDate(timeString);
				Date nowDate = DateTimeUtil.getDate();
				if (date.before(nowDate)) {
					date.setTime(date.getTime() + DateTimeUtil.ONE_DAY_TIME_MS);
				}
				rescueSunEntity.setOverTime(date.getTime());
			} else {
				rescueSunEntity.setOverTime(System.currentTimeMillis());
			}
			break;
		case RescueType.GET:
			if (get) {
				logger.error("已经领取过孙悟空");
				throw new IllegalArgumentException(ErrorIds.ALREADYGETSUNWUKONG + "");
			}
			long nowTime = System.currentTimeMillis();
			if (total < RescueType.freeTotal) {
				if (nowTime < endTime) {
					logger.error("未到解救时间");
					throw new IllegalArgumentException(ErrorIds.NOTREACHRESUCETIME + "");
				}
			}
			GoodsBean goodsBean = monkeyActiveData.getReward();
			code = rewardModule.addGoods(playerID, goodsBean.getPid(), goodsBean.getNum(), 0, true, showMap, itemMap, ioMessage);
			if (code != 0) {
				logger.error("添加孙悟空失败");
				protocol.setCode(code);
				return;
			}
		
			protocol.setShowMap(showMap);
			protocol.setItemMap(itemMap);
			rescueSunEntity.setGet(true);
			break;
		case RescueType.GETCLOUD:
			if (!get) {
				logger.error("需要先解救孙悟空");
				throw new IllegalArgumentException(ErrorIds.NEEDRESUCEMONKY + "");
			}
			boolean getCloud = rescueSunEntity.isGetCloud();
			if(getCloud){
				logger.error("已领取筋斗云");
				throw new IllegalArgumentException(ErrorIds.ALREADYGETCLOUD + "");
			}
			LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule,LoginModule.class);
			PlayerEntity playerEntity = loginModule.getPlayerEntity(playerID);
			int level = playerEntity.getLevel();
			if(level < RescueType.GETCLOUDLEVEL){
				logger.error("获得筋斗云的等级不足");
				throw new IllegalArgumentException(ErrorIds.GETCLOUDLEVELNOTENOUGH + "");
			}
			code = rewardModule.addGoods(playerID,RescueType.CLOUDID , RescueType.CLOUDNUM, 0, true, showMap, itemMap, ioMessage);
			if (code != 0) {
				logger.error("添加筋斗云失败");
				protocol.setCode(code);
				return;
			}
			WelfareEntity welfareEntity = getWelfareEntity(playerID);
			List<String> welfareList = welfareEntity.getWelfareList();
			if (welfareList.contains(WelfareConstants.WELFARE_DEFAULT_ALL[5])) {
				welfareList.remove(WelfareConstants.WELFARE_DEFAULT_ALL[5]);
				welfareEntity.setWelfareList(welfareList);
				this.saveWelfareEntity(welfareEntity);
			}
			protocol.setShowMap(showMap);
			protocol.setItemMap(itemMap);
			rescueSunEntity.setGetCloud(true);
			break;
		default:
			logger.error("错误的解救悟空参数");
			throw new IllegalArgumentException(ErrorIds.ParamWrong + "");
		}
		this.saveRescueSunEntity(rescueSunEntity);
		protocol.setRescueSunEntity(rescueSunEntity);
	}

	/**
	 * 在线时长礼包
	 * 
	 * @param playerID
	 * @param rewardID
	 * @param protocol
	 */
	public void eventOnline(String playerID, String rewardID, WelfareOnlineProtocol protocol) {
		Map<String, Object> itemMap = new HashMap<String, Object>();
		WelfareOnlineEntity onlineEntity = getOnlineEntity(playerID);
		if (onlineEntity == null) {
			onlineEntity = new WelfareOnlineEntity();
			onlineEntity.setPlayerID(playerID);
		}
		// 检查奖励id
		if (null == rewardID || rewardID.isEmpty()) {
			protocol.setCode(ErrorIds.WELFARE_REWARDID_NOTNULL);
			return;
		}
		Integer rewardIntID = Integer.parseInt(rewardID);
		if (onlineEntity.isGetReward(rewardIntID)) {
			protocol.setCode(ErrorIds.WELFARE_REWARD_ISGET);
			return;
		}
		// 查找奖励信息
		OnlineTimeRewardData onlinelRewardData = onlineRewardMap.get(rewardIntID);
		if (onlinelRewardData == null) {
			protocol.setCode(ErrorIds.WELFARE_REWARD_NOTFOUND);
			return;
		}
		// 获取奖励列表
		List<Integer> rewardList = onlinelRewardData.getKeys(onlinelRewardData.getReward());
		List<GoodsBean> goodsList = new ArrayList<GoodsBean>();
		for (Integer reward : rewardList) {
			GoodsBean goods = new GoodsBean();
			goods.setPid(reward);
			goods.setNum(onlinelRewardData.getRewardValue(reward));
			goodsList.add(goods);
		}
		// 增加奖励物品
		int code = rewardModule.addGoods(playerID, goodsList, true, null, itemMap, null);
		if (code != 0) {
			protocol.setCode(code);
			return;
		}
		String nowTime = Utilities.getDateTime();
		if (onlineEntity != null && !nowTime.equals(onlineEntity.getRewardTime())) {
			onlineEntity.setRewardID(0);			
			onlineEntity.setRewardList(new ArrayList<Integer>());			
		}else{
			onlineEntity.addRewardLevel(rewardIntID);
			onlineEntity.setRewardID(rewardIntID);
		}		
		onlineEntity.setRewardTime(Utilities.getDateTime());
		saveOnlineEntity(onlineEntity);
		protocol.setItemMap(itemMap);
		protocol.setEventOnlineEntity(onlineEntity);
	}

	/**
	 * 等级礼包
	 * 
	 * @param playerID
	 * @param rewardID
	 * @param protocol
	 */
	public void eventLevel(String playerID, String rewardID, WelfareLevelProtocol protocol) {
		Map<String, Object> itemMap = new HashMap<String, Object>();
		PlayerEntity playerEntity = loginModule.getPlayerEntity(playerID);
		WelfareLevelEntity levelEntity = getLevelEntity(playerID);
		if (levelEntity == null) {
			levelEntity = new WelfareLevelEntity();
			levelEntity.setPlayerID(playerID);
		}
		// 检查奖励id
		if (null == rewardID || rewardID.isEmpty()) {
			protocol.setCode(ErrorIds.WELFARE_REWARDID_NOTNULL);
			return;
		}
		int rewardIntID = Integer.parseInt(rewardID);
		if (levelEntity.isGetReward(rewardIntID)) {
			protocol.setCode(ErrorIds.WELFARE_REWARD_ISGET);
			return;
		}
		// 查找奖励信息
		LevelRewardData levelRewardData = levelRewardMap.get(rewardIntID);
		if (levelRewardData == null) {
			protocol.setCode(ErrorIds.WELFARE_REWARD_NOTFOUND);
			return;
		}
		// 检查用户等级
		if (levelRewardData.getLevel() > playerEntity.getLevel()) {
			protocol.setCode(ErrorIds.WELFARE_REWARD_LEVELWRONG);
			return;
		}
		// 获取奖励列表
		List<Integer> rewardList = levelRewardData.getKeys(levelRewardData.getReward());
		List<GoodsBean> goodsList = new ArrayList<GoodsBean>();
		for (Integer reward : rewardList) {
			GoodsBean goods = new GoodsBean();
			goods.setPid(reward);
			goods.setNum(levelRewardData.getRewardValue(reward));
			goodsList.add(goods);
		}
		// 增加奖励物品
		int code = rewardModule.addGoods(playerID, goodsList, true, null, itemMap, null);
		if (code != 0) {
			protocol.setCode(code);
			return;
		}
		levelEntity.addRewardLevel(rewardIntID);
		levelEntity.setRewardTime(Utilities.getDateTime());
		saveLevelEntity(levelEntity);
		WelfareEntity welfareEntity = getWelfareEntity(playerID);
		if (levelRewardMap.size() == levelEntity.getRewardList().size()) {
			welfareEntity.reduceWelfare(WelfareConstants.WELFARE_DEFAULT_ALL2[1]);
			saveWelfareEntity(welfareEntity);
		}
		protocol.setItemMap(itemMap);
		protocol.setEventLevelEntity(levelEntity);
	}

	/**
	 * 登录礼包
	 * 
	 * @param playerID
	 * @param rewardID
	 * @param protocol
	 */
	public void eventLogin(String playerID, String rewardID, WelfareLoginProtocol protocol) {
		Map<String, Object> itemMap = new HashMap<String, Object>();
		WelfareLoginEntity loginEntity = getLoginEntity(playerID);

		// 检查当天登录
		if (!loginEntity.isLogin(true)) {
			// 增加登录次数
			loginEntity.addLoginNum();
		}

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
		LoginRewardData loginRewardData = loginRewardMap.get(rewardIntID);
		if (loginRewardData == null) {
			protocol.setCode(ErrorIds.WELFARE_REWARD_NOTFOUND);
			return;
		}
		// 检查领取物品是否与登录天数匹配
		if (loginRewardData.getDays() > loginEntity.getLoginNum()) {
			protocol.setCode(ErrorIds.WELFARE_REWARDID_NODAY);
			return;
		}
		List<Integer> rewardList = loginRewardData.getKeys(loginRewardData.getReward());
		List<GoodsBean> goodsList = new ArrayList<GoodsBean>();
		for (Integer reward : rewardList) {
			GoodsBean goods = new GoodsBean();
			goods.setPid(reward);
			goods.setNum(loginRewardData.getRewardValue(reward));
			goodsList.add(goods);
		}
		// 增加奖励物品
		int code = rewardModule.addGoods(playerID, goodsList, true, null, itemMap, null);
		if (code != 0) {
			protocol.setCode(code);
			return;
		}
		MainTaskModule mainTaskModule = ModuleManager.getModule(ModuleNames.MainTaskModule,MainTaskModule.class);
		mainTaskModule.updateTaskByActionType(playerID, ActionType.GETNEWSERVERBOX, loginRewardData.getPid(), null);
		loginEntity.addRewardID(loginRewardData.getPid());
		// 刷新领奖时间
		loginEntity.isLogin(true);
		saveLoginEntity(loginEntity);

		WelfareEntity welfareEntity = getWelfareEntity(playerID);
		if (loginRewardMap.size() == loginEntity.getRewardList().size()) {
			welfareEntity.reduceWelfare(WelfareConstants.WELFARE_DEFAULT_ALL2[0]);
			saveWelfareEntity(welfareEntity);
		}

		protocol.setItemMap(itemMap);
		protocol.setEventLoginEntity(loginEntity);
	}

	/**
	 * 签到礼包
	 * 
	 * @param playerID
	 * @param rewardID
	 * @param protocol
	 */
	public void eventSign(String playerID, String rewardID, WelfareSignProtocol protocol) {
		PlayerEntity playerEntity = loginModule.getPlayerEntity(playerID);
		// 检查用户等级是否达到解锁
		if (playerEntity == null || playerEntity.getLevel() < WelfareConstants.SIGNIN_UNLOCKLEVEL) {
			protocol.setCode(ErrorIds.WELFARE_REWARD_LEVELWRONG);
			return;
		}
		Map<String, Object> itemMap = new HashMap<String, Object>();
		WelfareSignEntity signEntity = getSignEntity(playerID);
		// 检查奖励id
		if (null == rewardID || rewardID.isEmpty()) {
			protocol.setCode(ErrorIds.WELFARE_REWARDID_NOTNULL);
			return;
		}
		int rewardIntID = Integer.parseInt(rewardID);
		if (rewardIntID != WelfareConstants.SIGNIN_SEVEN) {
			// 检查是否领取
			if (signEntity.isReward(rewardIntID)) {
				protocol.setCode(ErrorIds.WELFARE_REWARD_ISGET);
				return;
			}
		} else {
			if (signEntity.isSeven(true)) {
				protocol.setCode(ErrorIds.WELFARE_REWARD_ISGET);
				return;
			}
		}
		// 获取奖励内容
		SigninRewardData signRewardData = signinRewardMap.get(rewardIntID);
		if (signRewardData == null) {
			protocol.setCode(ErrorIds.REWARD_NOTFOUND);
			return;
		}
		// 检查领取物品是否与签到次数匹配
		if (signRewardData.getDays() > signEntity.getSignNum()) {
			protocol.setCode(ErrorIds.WELFARE_REWARDID_NODAY);
			return;
		}
		List<Integer> rewardList = signRewardData.getKeys(signRewardData.getReward());
		List<GoodsBean> goodsList = new ArrayList<GoodsBean>();
		for (Integer reward : rewardList) {
			GoodsBean goods = new GoodsBean();
			goods.setPid(reward);
			goods.setNum(signRewardData.getRewardValue(reward));
			goodsList.add(goods);
		}
		// 增加奖励物品
		int code = rewardModule.addGoods(playerID, goodsList, true, null, itemMap, null);
		if (code != 0) {
			protocol.setCode(code);
			return;
		}
		signEntity.addRewardID(rewardIntID);
		saveSignEntity(signEntity);
		protocol.setItemMap(itemMap);
		protocol.setEventSignEntity(signEntity);
	}

	/**
	 * 月签到礼包
	 * 
	 * @param playerID
	 * @param rewardID
	 * @param protocol
	 */
	public void eventMonth(String playerID, String rewardID, WelfareMonthProtocol protocol) {

		PlayerEntity playerEntity = loginModule.getPlayerEntity(playerID);

		Map<String, Object> itemMap = new HashMap<String, Object>();
		WelfareMonthEntity monthEntity = getWelfareMonthEntity(playerID);
		// 检查月份是否相同
		if (Calendar.getInstance().get(Calendar.MONTH) + 1 != monthEntity.getMonth()) {
			monthEntity.setLoginNum(0);
			monthEntity.setMonth(Calendar.getInstance().get(Calendar.MONTH) + 1);
		}
		// 检查当天登录
		if (!monthEntity.isLogin(true)) {
			// 增加登录次数
			monthEntity.addLoginNum();
		}
		// 检查奖励id
		if (null == rewardID || rewardID.isEmpty()) {
			protocol.setCode(ErrorIds.WELFARE_REWARDID_NOTNULL);
			return;
		}
		int rewardIntID = Integer.parseInt(rewardID);
		// 检查是否领取
		if (monthEntity.isGetReward(rewardIntID)) {
			protocol.setCode(ErrorIds.WELFARE_REWARD_ISGET);
			return;
		}
		// 查找奖励信息
		MonthRewardData rewardData = monthRewardMap.get(rewardIntID);

		if (rewardData == null) {
			protocol.setCode(ErrorIds.WELFARE_REWARD_NOTFOUND);
			return;
		}
		// 检查领取物品是否与登录天数匹配
		if (rewardData.getDays() > monthEntity.getLoginNum()) {
			protocol.setCode(ErrorIds.WELFARE_REWARDID_NODAY);
			return;
		}
		List<Integer> rewardList = rewardData.getKeys(rewardData.getReward());
		List<GoodsBean> goodsList = new ArrayList<GoodsBean>();
		for (Integer reward : rewardList) {
			GoodsBean goods = new GoodsBean();
			goods.setPid(reward);
			if (rewardData.getVipLevel() != -1 && playerEntity.getVipLevel() >= rewardData.getVipLevel()) {
				goods.setNum(rewardData.getRewardValue(reward) * 2);
			} else {
				goods.setNum(rewardData.getRewardValue(reward));
			}
			goodsList.add(goods);
		}
		// 增加奖励物品
		int code = rewardModule.addGoods(playerID, goodsList, true, null, itemMap, null);
		if (code != 0) {
			protocol.setCode(code);
			return;
		}
		monthEntity.addRewardID(rewardData.getPid());
		// 刷新领奖时间
		monthEntity.isLogin(true);
		saveWelfareMonthEntity(monthEntity);

		// WelfareEntity welfareEntity = getWelfareEntity(playerID);
		// if (monthRewardMap.size() == monthEntity.getRewardList().size()) {
		// welfareEntity.reduceWelfare(WelfareConstants.WELFARE_DEFAULT_ALL[4]);
		// saveWelfareEntity(welfareEntity);
		// }

		protocol.setItemMap(itemMap);
		protocol.setMonthEntity(monthEntity);
	}

	@Override
	public void init() {
		rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
		loginModule = ModuleManager.getModule(ModuleNames.LoginModule, LoginModule.class);
		initOnlineTimeRewardData();
		initLevelRewardData();
		initLoginRewardData();
		initSigninRewardData();
		initOnlineRewardData();
		initMonthRewardData();
	}

	public void saveRescueSunEntity(WelfareRescueSunEntity rescueSunEntity) {
		rescueSunEntityDao.save(rescueSunEntity);
	}

	public WelfareRescueSunEntity getWelfareRescueSunEntity(String playerID) {
		WelfareRescueSunEntity rescueSunEntity = rescueSunEntityDao.getEntity(playerID);
		if (rescueSunEntity == null) {
			rescueSunEntity = new WelfareRescueSunEntity();
			rescueSunEntity.setKey(playerID);
			this.saveRescueSunEntity(rescueSunEntity);
		}
		return rescueSunEntity;
	}

	private void initOnlineTimeRewardData() {
		List<OnlineTimeRewardData> dataList = TemplateManager.getTemplateList(OnlineTimeRewardData.class);
		for (OnlineTimeRewardData data : dataList) {
			onlineRewardMap.put(data.getPid(), data);
		}
	}

	private void initLevelRewardData() {
		List<LevelRewardData> dataList = TemplateManager.getTemplateList(LevelRewardData.class);
		for (LevelRewardData data : dataList) {
			levelRewardMap.put(data.getPid(), data);
		}
	}

	private void initLoginRewardData() {
		List<LoginRewardData> dataList = TemplateManager.getTemplateList(LoginRewardData.class);
		for (LoginRewardData data : dataList) {
			loginRewardMap.put(data.getPid(), data);
		}
	}

	private void initSigninRewardData() {
		List<SigninRewardData> dataList = TemplateManager.getTemplateList(SigninRewardData.class);
		for (SigninRewardData data : dataList) {
			signinRewardMap.put(data.getPid(), data);
		}
	}

	private void initMonthRewardData() {
		List<MonthRewardData> dataList = TemplateManager.getTemplateList(MonthRewardData.class);
		for (MonthRewardData data : dataList) {
			monthRewardMap.put(data.getPid(), data);
		}
	}

	private void initOnlineRewardData() {
		List<OnlineTimeRewardData> dataList = TemplateManager.getTemplateList(OnlineTimeRewardData.class);
		for (OnlineTimeRewardData data : dataList) {
			onlineRewardMap.put(data.getPid(), data);
		}
	}

	public WelfareSignEntity getSignEntity(String playerID) {
		return signEventDao.getSignEntity(playerID);
	}

	public void saveSignEntity(WelfareSignEntity signEntity) {
		signEventDao.save(signEntity);
	}

	public WelfareLevelEntity getLevelEntity(String playerID) {
		return levelEntityDao.getLevelEntity(playerID);
	}

	public void saveLevelEntity(WelfareLevelEntity levelEntity) {
		levelEntityDao.save(levelEntity);
	}

	public WelfareLoginEntity getLoginEntity(String playerID) {
		return loginEntityDao.getLoginEntity(playerID);
	}

	public void saveLoginEntity(WelfareLoginEntity loginEntity) {
		loginEntityDao.save(loginEntity);
	}

	public WelfareOnlineEntity getOnlineEntity(String playerID) {
		return onlineEntityDao.getOnlineEntity(playerID);
	}

	public void saveOnlineEntity(WelfareOnlineEntity onlineEntity) {
		onlineEntityDao.save(onlineEntity);
	}

	public WelfareEntity getWelfareEntity(String playerID) {
		return welfEntityDao.getWelfareEntity(playerID);
	}

	public void saveWelfareEntity(WelfareEntity welfareEntity) {
		welfEntityDao.save(welfareEntity);
	}

	public WelfareMonthEntity getWelfareMonthEntity(String playerID) {
		return monthEntityDao.getMonthEntity(playerID);
	}

	public void saveWelfareMonthEntity(WelfareMonthEntity entity) {
		monthEntityDao.save(entity);
	}

}
