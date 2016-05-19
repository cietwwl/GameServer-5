package com.mi.game.module.legion;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.mi.core.dao.KeyGeneratorDAO;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.TemplateManager;
import com.mi.core.engine.annotation.Module;
import com.mi.core.util.ConfigUtil;
import com.mi.game.defines.ActionType;
import com.mi.game.defines.ErrorIds;
import com.mi.game.defines.KindIDs;
import com.mi.game.defines.ModuleNames;
import com.mi.game.defines.SysConstants;
import com.mi.game.module.analyse.AnalyseModule;
import com.mi.game.module.base.BaseModule;
import com.mi.game.module.dayTask.DayTaskModule;
import com.mi.game.module.legion.dao.LegionEntityDAO;
import com.mi.game.module.legion.dao.LegionHistoryEntityDAO;
import com.mi.game.module.legion.dao.LegionMemberEntityDAO;
import com.mi.game.module.legion.data.LegionBuildData;
import com.mi.game.module.legion.data.LegionDevoteData;
import com.mi.game.module.legion.data.LegionDropData;
import com.mi.game.module.legion.data.LegionShopData;
import com.mi.game.module.legion.data.LegionVisitRewardData;
import com.mi.game.module.legion.define.LegionConstans;
import com.mi.game.module.legion.define.LegionKindIDs;
import com.mi.game.module.legion.define.LegionManageType;
import com.mi.game.module.legion.pojo.LegionEntity;
import com.mi.game.module.legion.pojo.LegionGG;
import com.mi.game.module.legion.pojo.LegionHall;
import com.mi.game.module.legion.pojo.LegionHistoryEntity;
import com.mi.game.module.legion.pojo.LegionMemberEntity;
import com.mi.game.module.legion.pojo.LegionShop;
import com.mi.game.module.legion.pojo.LegionShopItem;
import com.mi.game.module.legion.protocol.LegionProtocol;
import com.mi.game.module.login.LoginModule;
import com.mi.game.module.login.pojo.PlayerEntity;
import com.mi.game.module.mailBox.MailBoxModule;
import com.mi.game.module.mainTask.MainTaskModule;
import com.mi.game.module.reward.RewardModule;
import com.mi.game.module.vip.VipModule;
import com.mi.game.module.vip.pojo.VipEntity;
import com.mi.game.module.vitatly.VitatlyModule;
import com.mi.game.module.vitatly.pojo.VitatlyEntity;
import com.mi.game.module.wallet.WalletModule;
import com.mi.game.module.wallet.pojo.WalletEntity;
import com.mi.game.util.CommonMethod;
import com.mi.game.util.Utilities;

@Module(name = ModuleNames.LegionModule, clazz = LegionModule.class)
public class LegionModule extends BaseModule {

	private LoginModule loginModule;
	private WalletModule walletModule;
	private VitatlyModule vitatlyModule;
	private VipModule vipModule;
	private AnalyseModule analyseModule;
	private MailBoxModule mailBoxModule;
	private final KeyGeneratorDAO keyGeneratorDAO = KeyGeneratorDAO.getInstance();
	private LegionEntityDAO legionDao = LegionEntityDAO.getInstance();
	private LegionMemberEntityDAO memberDao = LegionMemberEntityDAO.getInstance();
	private LegionHistoryEntityDAO historyDAO = LegionHistoryEntityDAO.getInstance();
	private final static String serverID = ConfigUtil.getString("server.id");

	/**
	 * 修复玩家公会id异常
	 */
	public void repairLegion() {
		List<LegionHistoryEntity> historyList = historyDAO.getLegionCreateHistoryList();
		for (LegionHistoryEntity history : historyList) {
			String playerID = history.getPlayerID();
			String legionID = history.getLegionID();
			LegionEntity legionEntity = getLegionEntity(legionID);
			if (legionEntity.isKill() || !legionEntity.getLegatus().equals(playerID)) {
				continue;
			}
			PlayerEntity playerEntity = loginModule.getPlayerEntity(playerID);
			String playerLegionID = playerEntity.getGroupID();
			if (StringUtils.isNotEmpty(playerLegionID) && !legionID.equals(playerLegionID)) {
				// 公会中移除该玩家
				legionEntity.delLegatus(playerID);
				legionEntity.delMember(playerID);
				saveLegionEntity(legionEntity);

				// 设置玩家公会为自己创建的公会
				playerEntity.setGroupID(legionID);
				loginModule.savePlayerEntity(playerEntity);

				logger.error("修复玩家 #" + playerID + "# 工会id #" + playerLegionID + "# 为 #" + legionID + "#");
			}
		}
	}

	/**
	 * 检查是否加入军团
	 * 
	 * @param playerID
	 * @param protocol
	 */
	public void isJoinLegion(String playerID, LegionProtocol protocol) {
		PlayerEntity playerEntity = loginModule.getPlayerEntity(playerID);
		if (playerEntity == null) {
			protocol.setCode(ErrorIds.LEGION_PLAYERNOTEXIST);
			return;
		}
		protocol.setLegionID(playerEntity.getGroupID());
	}

	/**
	 * 获取军团动态
	 * 
	 * @param playerID
	 * @param legionID
	 * @param protocol
	 */
	public void getHistorys(String playerID, String legionID, LegionProtocol protocol) {
		// 根据用户id获取用户信息
		PlayerEntity playerEntity = loginModule.getPlayerEntity(playerID);
		if (playerEntity == null) {
			protocol.setCode(ErrorIds.LEGION_PLAYERNOTEXIST);
			return;
		}
		if (playerEntity.getGroupID().equals("0")) {
			protocol.setCode(ErrorIds.LEGION_NOTJOIN);
			return;
		}
		// 校验军团id
		if (legionID == null || legionID.isEmpty()) {
			protocol.setCode(ErrorIds.LEGION_ID_ISNOTNULL);
			return;
		}
		List<LegionHistoryEntity> historyList = getLegionHistoryEntityList(legionID);
		protocol.setHistoryList(historyList);
	}

	/**
	 * 军团关公殿
	 * 
	 * @param playerID
	 * @param legionID
	 * @param legionType
	 * @param protocol
	 */
	public void legionGG(String playerID, String legionID, String legionType, LegionProtocol protocol) {
		if (legionID == null || legionID.isEmpty()) {
			protocol.setCode(ErrorIds.LEGION_ID_ISNOTNULL);
			return;
		}
		// 根据用户id获取用户信息
		PlayerEntity playerEntity = loginModule.getPlayerEntity(playerID);
		if (playerEntity == null) {
			protocol.setCode(ErrorIds.LEGION_PLAYERNOTEXIST);
			return;
		}
		if (playerEntity.getGroupID().equals("0")) {
			protocol.setCode(ErrorIds.LEGION_NOTJOIN);
			return;
		}
		// 根据军团id获取军团信息
		LegionEntity legionEntity = getLegionEntity(legionID);
		// 根据用户id获取用户钱包
		WalletEntity walletEntity = walletModule.getwalletEntity(playerID);
		// 根据用户id获取用户体力耐力
		VitatlyEntity vitatlyEntity = vitatlyModule.getVitatlyEntity(playerID);
		if (legionEntity == null) {
			protocol.setCode(ErrorIds.LEGION_ISNOTHING);
			return;
		}
		// 判断操作类型
		if (legionType == null || legionType.isEmpty()) {
			protocol.setCode(ErrorIds.LEGION_PARAMEERROR);
			return;
		}
		// 根据用户id 获取成员属性
		LegionMemberEntity memberEntity = getLegionMemberEntity(playerID);
		// 获取参拜奖励配置
		Set<Entry<Integer, LegionVisitRewardData>> set = visitRewardDataMap.entrySet();
		LegionVisitRewardData vistReward = null;
		for (Entry<Integer, LegionVisitRewardData> entry : set) {
			vistReward = entry.getValue();
			if (vistReward.getBulidLimit() == Integer.parseInt(legionEntity.getLegiongg().getCurrentPid())) {
				break;
			}
		}
		// 重新设置成员参拜次数,关公殿参拜次数
		if (!memberEntity.isVisitRefresh(true)) {
			memberEntity.setVisit(LegionConstans.LEGION_VISITMAX);
			legionEntity.getLegiongg().setVisitNum(legionEntity.getLegiongg().getMaxVisit());
			legionEntity.getLegiongg().setVisitTime(Utilities.getDateTime());
		}

		switch (legionType) {
		// 关公殿升级
		case LegionManageType.UPGRADE:
			// 检查权限
			if (!legionEntity.isLegatus(playerID)) {
				protocol.setCode(ErrorIds.LEGION_NOTMANAGE);
				return;
			}

			// 获取军团关公殿配置信息
			LegionBuildData ggBuild = buildDataMap.get(Integer.parseInt(legionEntity.getLegiongg().getCurrentPid()));

			int limit = ggBuild.getUpgradeLimit();
			int hallPid = Integer.parseInt(legionEntity.getLegionHall().getCurrentPid());
			if (hallPid < limit) {
				protocol.setCode(ErrorIds.LEGION_UPGRADE_MISS);
				return;
			}

			if (ggBuild.getNextID() == 0) {
				protocol.setCode(ErrorIds.LEGION_BUILDLEVELMAX);
				return;
			}

			// 获取升级消耗
			List<Integer> upgradeTypeList = ggBuild.getKeys(ggBuild.getUpgradeRequire());
			for (Integer upgradeType : upgradeTypeList) {
				// 获取消耗数量
				Integer upgradeValue = ggBuild.getValue(upgradeType);
				if (KindIDs.SILVERTYPE == upgradeType) {
					if (walletEntity.getSilver() < upgradeValue) {
						protocol.setCode(ErrorIds.LEGION_SILVERINSUFFICIENT);
						return;
					}
					// 减少银币
					walletEntity = walletModule.consumeCurrencyWalletEntity(playerID, KindIDs.SILVERTYPE, upgradeValue, true, null);
				}
				if (LegionKindIDs.LEGION_DEVOTE == upgradeType) {
					if (legionEntity.getLegionDevote() < upgradeValue) {
						protocol.setCode(ErrorIds.LEGION_DEVOTESUFFICIENT);
						return;
					}
					// 减少军团贡献
					legionEntity.consumeLegionDevote(upgradeValue);
				}
			}
			// 设置军团关公殿当前pid
			legionEntity.getLegiongg().setCurrentPid(ggBuild.getNextID() + "");
			// 保存军团信息
			saveLegionEntity(legionEntity);
			// 钱币变化
			protocol.setWalletEntity(walletEntity);
			protocol.setLegionEntity(legionEntity);
			// 记录升级动态
			foundHistory(playerID, legionID, LegionManageType.GGUP, ggBuild.getPid() + "#" + ggBuild.getNextID());
			// 军团动态
			protocol.setHistoryList(getLegionHistoryEntityList(legionEntity.getLegionID()));

			// //////////////////////////////////////////////
			// 军团关公殿升级日志
			JSONObject params = new JSONObject();
			params.put(legionID, legionID);
			params.put("bulidID", legionEntity.getLegiongg().getCurrentPid());
			analyseModule.analyLog(playerID, "ALLIANCE_BUILDING_UP", params, null, null, null);

			return;
		case LegionManageType.GOLD:
			// 不在参拜时间内
			if (!isVisterTime()) {
				protocol.setCode(ErrorIds.LEGION_VISTER_TIME_ERROR);
				return;
			}
			VipEntity vipEntity = vipModule.initVipEntity(playerID);
			// 获取参拜价格
			int goldVisit = vipModule.permissionPrice(vipEntity, LegionConstans.LEGION_GOLD_ACTIONID);
			if (goldVisit > walletEntity.getGold()) {
				protocol.setCode(ErrorIds.LEGION_GOLDINSUFFICIENT);
				return;
			}
			vipEntity = vipModule.reducePermissionValue(vipEntity, LegionConstans.LEGION_GOLD_ACTIONID);
			if (vipEntity == null) {
				protocol.setCode(ErrorIds.LEGION_VISITISMAX);
				return;
			}
			// 减少金币
			walletEntity = walletModule.consumeCurrencyWalletEntity(playerID, KindIDs.GOLDTYPE, goldVisit, true, null);
			protocol.setVipEntity(vipEntity);
			// //
			// 每日任务,拜关公
			// //
			DayTaskModule dayTaskModule2 = ModuleManager.getModule(ModuleNames.DayTaskModule, DayTaskModule.class);
			dayTaskModule2.addScore(playerID, ActionType.WORSHIP, 1);
			// 主线任务
			MainTaskModule mainTaskModule = ModuleManager.getModule(ModuleNames.MainTaskModule, MainTaskModule.class);
			mainTaskModule.updateTaskByActionType(playerID, ActionType.LEGIONGG, 0, null);

			// //////////////////////////////////////////////
			// 金币参拜日志
			JSONObject canbai = new JSONObject();
			canbai.put("legionID", legionID);
			canbai.put("legionType", LegionManageType.GOLD);
			analyseModule.analyLog(playerID, "ALLIANCE_CANBAI", canbai, null, null, null);

			// ////
			// // 元宝消耗记录
			// ///
			analyseModule.goldCostLog(playerID, goldVisit, 1, goldVisit, "legionGG" + LegionManageType.GOLD, "legion");

			break;
		case LegionManageType.DEVOTE:
			// 不在参拜时间内
			if (!isVisterTime()) {
				protocol.setCode(ErrorIds.LEGION_VISTER_TIME_ERROR);
				return;
			}
			// 检查参拜次数
			if (memberEntity.getVisit() == 0) {
				protocol.setCode(ErrorIds.LEGION_VISITISMAX);
				return;
			}
			if (LegionConstans.LEGION_FIRSTVISIT > memberEntity.getDevote()) {
				protocol.setCode(ErrorIds.LEGION_DEVOTESUFFICIENT);
				return;
			}
			// 减少个人贡献
			memberEntity.consumeDevote(50);
			// 成员减少参拜次数
			memberEntity.setVisit(memberEntity.getVisit() - 1);
			// 增加参拜次数
			legionEntity.getLegiongg().setVisitNum(legionEntity.getLegiongg().getVisitNum() - 1);
			legionEntity.getLegiongg().setVisitTime(Utilities.getDateTime());
			// //
			// 每日任务,拜关公
			// //
			DayTaskModule dayTaskModule = ModuleManager.getModule(ModuleNames.DayTaskModule, DayTaskModule.class);
			dayTaskModule.addScore(playerID, ActionType.WORSHIP, 1);
			MainTaskModule mainTaskModule2 = ModuleManager.getModule(ModuleNames.MainTaskModule, MainTaskModule.class);
			mainTaskModule2.updateTaskByActionType(playerID, ActionType.LEGIONGG, 0, null);

			// //////////////////////////////////////////////
			// 贡献参拜日志
			JSONObject canbai2 = new JSONObject();
			canbai2.put("legionID", legionID);
			canbai2.put("legionType", LegionManageType.GOLD);
			analyseModule.analyLog(playerID, "ALLIANCE_CANBAI", canbai2, null, null, null);

			break;
		default:
			// 成员减少参拜次数
			memberEntity.setVisit(memberEntity.getVisit() - 1);
			// 增加参拜次数
			legionEntity.getLegiongg().setVisitNum(legionEntity.getLegiongg().getVisitNum() - 1);
			break;
		}
		// 保存成员信息
		saveLegionMemberEntity(memberEntity);
		// 获取奖励列表
		List<Integer> rewardTypeList = vistReward.getKeys(vistReward.getReward());
		for (Integer rewardType : rewardTypeList) {
			int rewardValue = vistReward.getValue(rewardType);
			if (KindIDs.SILVERTYPE == rewardType) {
				// 加银币
				walletEntity = walletModule.addCurrencyWalletEntity(playerID, KindIDs.SILVERTYPE, rewardValue, true, null);
				continue;
			}
			if (KindIDs.VITALITY == rewardType) {
				// 加体力
				vitatlyEntity = vitatlyModule.addVitatly(playerID, rewardValue);
				vitatlyModule.saveVitatlyEntity(vitatlyEntity);
				continue;
			}
			if (KindIDs.REPUTATION == rewardType) {
				// 加声望
				walletEntity = walletModule.addCurrencyWalletEntity(playerID, KindIDs.REPUTATION, rewardValue, true, null);
				continue;
			}
		}
		saveLegionEntity(legionEntity);
		protocol.setLegionEntity(legionEntity);
		// 钱币变化
		protocol.setWalletEntity(walletEntity);
		// 体力变化
		protocol.setVitatlyEntity(vitatlyEntity);
		// 记录参拜动态
		foundHistory(playerID, legionID, LegionManageType.VISIT, vistReward.getPid() + "");
		// 军团动态
		protocol.setHistoryList(getLegionHistoryEntityList(legionEntity.getLegionID()));
		return;
	}

	/**
	 * 军团商店
	 * 
	 * @param playerID
	 * @param legionID
	 * @param legionType
	 * @param itemID
	 * @param protocol
	 */
	public void legionShop(String playerID, String legionID, String legionType, String itemID, LegionProtocol protocol) {
		if (legionID == null || legionID.isEmpty()) {
			protocol.setCode(ErrorIds.LEGION_ID_ISNOTNULL);
			return;
		}
		// 根据用户id获取用户信息
		PlayerEntity playerEntity = loginModule.getPlayerEntity(playerID);
		if (playerEntity == null) {
			protocol.setCode(ErrorIds.LEGION_PLAYERNOTEXIST);
			return;
		}
		if (playerEntity.getGroupID().equals("0")) {
			protocol.setCode(ErrorIds.LEGION_NOTJOIN);
			return;
		}
		MainTaskModule mainTaskModule = ModuleManager.getModule(ModuleNames.MainTaskModule, MainTaskModule.class);
		Map<String, Object> itemMap = new HashMap<String, Object>();
		// 根据军团id获取军团信息
		LegionEntity legionEntity = getLegionEntity(legionID);
		// 根据用户id获取用户钱包
		WalletEntity walletEntity = walletModule.getwalletEntity(playerID);
		// 根据用户id获取用户军团成员属性
		LegionMemberEntity memberEntity = getLegionMemberEntity(playerID);
		// 物品id
		int itemIntID = 0;
		if (itemID != null && !itemID.isEmpty()) {
			itemIntID = Integer.parseInt(itemID);
		}
		// 军团商店升级
		if (legionType == null || legionType.isEmpty()) {
			protocol.setCode(ErrorIds.LEGION_PARAMEERROR);
			return;
		}
		switch (legionType) {
		case LegionManageType.UPGRADE:
			// 检查权限
			if (!legionEntity.isLegatus(playerID)) {
				protocol.setCode(ErrorIds.LEGION_NOTMANAGE);
				return;
			}
			// 获取军团商店配置信息
			LegionBuildData shopBuild = buildDataMap.get(Integer.parseInt(legionEntity.getLegionShop().getCurrentPid()));

			int limit = shopBuild.getUpgradeLimit();
			int hallPid = Integer.parseInt(legionEntity.getLegionHall().getCurrentPid());
			if (hallPid < limit) {
				protocol.setCode(ErrorIds.LEGION_UPGRADE_MISS);
				return;
			}

			if (shopBuild.getNextID() == 0) {
				protocol.setCode(ErrorIds.LEGION_BUILDLEVELMAX);
				return;
			}

			// 获取升级消耗
			List<Integer> upgradeTypeList = shopBuild.getKeys(shopBuild.getUpgradeRequire());
			for (Integer upgradeType : upgradeTypeList) {
				// 获取消耗数量
				Integer upgradeValue = shopBuild.getValue(upgradeType);
				if (KindIDs.SILVERTYPE == upgradeType) {
					if (walletEntity.getSilver() < upgradeValue) {
						protocol.setCode(ErrorIds.LEGION_SILVERINSUFFICIENT);
						return;
					}
					// 减少银币
					walletEntity = walletModule.consumeCurrencyWalletEntity(playerID, KindIDs.SILVERTYPE, upgradeValue, true, null);
				}
				if (LegionKindIDs.LEGION_DEVOTE == upgradeType) {
					if (legionEntity.getLegionDevote() < upgradeValue) {
						protocol.setCode(ErrorIds.LEGION_DEVOTESUFFICIENT);
						return;
					}
					// 减少军团贡献
					legionEntity.consumeLegionDevote(upgradeValue);
				}
			}
			// 设置军团商店当前pid
			legionEntity.getLegionShop().setCurrentPid(shopBuild.getNextID() + "");
			// 保存军团信息
			saveLegionEntity(legionEntity);
			// 钱币变化
			protocol.setWalletEntity(walletEntity);
			protocol.setLegionEntity(legionEntity);

			// 保存购买记录
			foundHistory(playerID, legionID, LegionManageType.SHOPUP, shopBuild.getPid() + "#" + shopBuild.getNextID());
			// 军团动态
			protocol.setHistoryList(getLegionHistoryEntityList(legionEntity.getLegionID()));

			// //////////////////////////////////////////////
			// 军团商店升级日志
			JSONObject params = new JSONObject();
			params.put("legionID", legionID);
			params.put("bulidID", legionEntity.getLegionShop().getCurrentPid());
			analyseModule.analyLog(playerID, "ALLIANCE_BUILDING_UP", params, null, null, null);
			return;

		case LegionManageType.BUY: // 道具购买
			if (itemID == null || itemID.isEmpty()) {
				protocol.setCode(ErrorIds.LEGION_ITEMISNULL);
				return;
			}
			// 根据配置文件获取商品列表
			Set<Entry<Integer, LegionShopData>> shopSet = shopDataMap.entrySet();
			// 军团商店解锁物品
			Map<Integer, LegionShopData> unlockGoods = new HashMap<Integer, LegionShopData>();
			for (Entry<Integer, LegionShopData> entry : shopSet) {
				int currentPid = Integer.parseInt(legionEntity.getLegionShop().getCurrentPid());
				if (currentPid >= entry.getValue().getUnlockLimit()) {
					unlockGoods.put(entry.getKey(), entry.getValue());
				}
			}
			// 根据itemID获取解锁物品信息
			Set<Entry<Integer, LegionShopData>> unlockSet = unlockGoods.entrySet();
			LegionShopData unlockItem = null;
			for (Entry<Integer, LegionShopData> entry : unlockSet) {
				LegionShopData temp = entry.getValue();
				List<Integer> itemIDs = temp.getKeys(temp.getItemID());
				if (itemIDs.get(0) == itemIntID) {
					unlockItem = temp;
					break;
				}
			}
			if (unlockItem == null) {
				protocol.setCode(ErrorIds.LEGION_NOTFOUNDITEM);
				return;
			}
			// 重置购买物品列表
			if (!memberEntity.isBuyRefresh(true)) {
				memberEntity.initBuyItem();
			} else {
				if (memberEntity.isCanBuy(itemID, unlockItem.getDayLimit())) {
					protocol.setCode(ErrorIds.LEGION_ITEMISBUY);
					return;
				}
			}
			// 获取物品数量
			int itemValue = unlockItem.getItemValue(itemIntID);
			// 获取物品货币类型
			int coinType = unlockItem.getKeys(unlockItem.getPrice()).get(0);
			// 获取物品货币数量
			int coinValue = unlockItem.getPriceValue(coinType);
			if (coinValue > memberEntity.getDevote()) {
				protocol.setCode(ErrorIds.LEGION_DEVOTESUFFICIENT);
				return;
			}
			// 减少贡献
			memberEntity.consumeDevote(coinValue);
			// 增加购买记录
			memberEntity.addBuyItem(itemID);
			saveLegionMemberEntity(memberEntity);
			// 增加物品
			RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
			rewardModule.addGoods(playerID, itemIntID, itemValue, null, true, null, itemMap, null);
			// 保存购买记录
			foundHistory(playerID, legionID, LegionManageType.BUY, itemID + "#" + itemValue);
			// 军团动态
			protocol.setHistoryList(getLegionHistoryEntityList(legionEntity.getLegionID()));
			protocol.setLegionEntity(legionEntity);
			protocol.setItemMap(itemMap);
			mainTaskModule.updateTaskByActionType(playerID, ActionType.LEGIONEXCHANGE, 0, null);
			return;
		case LegionManageType.BUY3: // 珍品购买
			// 检查是否已经购买过
			if (!memberEntity.isGemRefresh(true)) {
				memberEntity.initGemItem();
			} else {
				if (memberEntity.isGemCanBuy(itemID)) {
					protocol.setCode(ErrorIds.LEGION_ITEMISBUY);
					return;
				}
			}
			LegionShopItem shopItem = legionEntity.getLegionShop().getShopItem(itemIntID);
			if (shopItem == null) {
				protocol.setCode(ErrorIds.LEGION_NOTFOUNDITEM);
				protocol.setLegionEntity(legionEntity);
				return;
			}

			// 检查珍品数量
			if (!legionEntity.getLegionShop().isHaveItem(itemIntID)) {
				protocol.setCode(ErrorIds.LEGION_ITEMSELLOUT);
				return;
			}

			// 获取珍品数量
			int itemValue3 = shopItem.getItemNum();
			// 获取珍品货币数量
			int coinValue3 = shopItem.getCoinValue();
			// 判断贡献是否足够
			if (coinValue3 > memberEntity.getDevote()) {
				protocol.setCode(ErrorIds.LEGION_DEVOTESUFFICIENT);
				return;
			}
			// 减少购买次数
			legionEntity.getLegionShop().reduceItemNum(itemIntID);
			saveLegionEntity(legionEntity);
			// 减少贡献
			memberEntity.consumeDevote(coinValue3);
			// 增加购买记录
			memberEntity.addGemItem(itemID);
			saveLegionMemberEntity(memberEntity);
			// 增加物品
			RewardModule rewardModule3 = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
			rewardModule3.addGoods(playerID, itemIntID, itemValue3, null, true, null, itemMap, null);
			// 保存购买记录
			foundHistory(playerID, legionID, LegionManageType.BUY3, itemID + "#" + itemValue3);
			// 军团动态
			protocol.setHistoryList(getLegionHistoryEntityList(legionEntity.getLegionID()));
			protocol.setLegionEntity(legionEntity);
			protocol.setItemMap(itemMap);
			protocol.setLegionEntity(legionEntity);
			mainTaskModule.updateTaskByActionType(playerID, ActionType.LEGIONEXCHANGE, 0, null);
			return;
		}
	}

	/**
	 * 军团大厅
	 * 
	 * @param playerID
	 * @param legionID
	 * @param legionType
	 * @param buildType
	 * @param protocol
	 */
	public void legionHall(String playerID, String legionID, String legionType, String buildType, LegionProtocol protocol) {
		if (legionID == null || legionID.isEmpty()) {
			protocol.setCode(ErrorIds.LEGION_ID_ISNOTNULL);
			return;
		}

		// 根据用户id获取用户信息
		PlayerEntity playerEntity = loginModule.getPlayerEntity(playerID);
		if (playerEntity == null) {
			protocol.setCode(ErrorIds.LEGION_PLAYERNOTEXIST);
			return;
		}
		if (playerEntity.getGroupID().equals("0")) {
			protocol.setCode(ErrorIds.LEGION_NOTJOIN);
			return;
		}

		// 根据军团id获取军团信息
		LegionEntity legionEntity = getLegionEntity(legionID);
		if (legionEntity == null) {
			protocol.setCode(ErrorIds.LEGION_ISNOTHING);
			return;
		}
		if (null == legionType || legionType.isEmpty()) {
			protocol.setCode(ErrorIds.LEGION_PARAMEERROR);
			return;
		}

		// 根据用户id获取用户钱包
		WalletEntity walletEntity = walletModule.getwalletEntity(playerID);
		switch (legionType) {
		case LegionManageType.BUILD:
			if (null == buildType || buildType.isEmpty()) {
				protocol.setCode(ErrorIds.LEGION_BUILDTYPENULL);
				return;
			}
			// 根据用户id 获取成员属性
			LegionMemberEntity memberEntity = getLegionMemberEntity(playerID);
			// 获取军团建设配置信息
			LegionDevoteData legionDevote = devoteDataMap.get(Integer.parseInt(buildType));

			// 重置成员每日建设
			if (!memberEntity.isBuildRefresh(true)) {
				memberEntity.setBuild(LegionConstans.LEGION_DEFAULT_BUILD);
			} else {
				protocol.setCode(ErrorIds.LEGION_ISBUILD);
				return;
			}
			// 开始建设
			// 建设消耗类型
			List<Integer> requireTypeList = legionDevote.getKeys(legionDevote.getDevoteRequire());
			for (Integer requireType : requireTypeList) {
				// 获取消耗数量
				Integer requireValue = legionDevote.getRequireValue(requireType);
				if (KindIDs.SILVERTYPE == requireType) {
					if (walletEntity.getSilver() < requireValue) {
						protocol.setCode(ErrorIds.LEGION_SILVERINSUFFICIENT);
						return;
					}
					// 减少银币
					walletEntity = walletModule.consumeCurrencyWalletEntity(playerID, KindIDs.SILVERTYPE, requireValue, true, null);
				}
				if (KindIDs.GOLDTYPE == requireType) {
					if (walletEntity.getGold() < requireValue) {
						protocol.setCode(ErrorIds.LEGION_GOLDINSUFFICIENT);
						return;
					}
					// 减少金币
					walletEntity = walletModule.consumeCurrencyWalletEntity(playerID, KindIDs.GOLDTYPE, requireValue, true, null);

					// ////
					// // 元宝消耗记录
					// ///
					analyseModule.goldCostLog(playerID, requireValue, 1, requireValue, "legionHall" + requireType, "legion");

					continue;
				}
			}
			// 建设完成
			memberEntity.setBuild(buildType);
			memberEntity.setBuildTime(Utilities.getDateTime());

			// 开始增加奖励
			// 获取奖励列表
			List<Integer> rewardTypeList = legionDevote.getKeys(legionDevote.getDevoteReward());
			for (Integer rewardType : rewardTypeList) {
				// 增加个人军团贡献
				if (LegionKindIDs.LEGION_PRO_DEVOTE == rewardType) {
					memberEntity.addDevote(legionDevote.getRewardValue(rewardType));
					continue;
				}
				// 增加军团贡献
				if (LegionKindIDs.LEGION_DEVOTE == rewardType) {
					legionEntity.addLegionDevote(legionDevote.getRewardValue(rewardType));
					continue;
				}
			}
			// 保存成员信息
			saveLegionMemberEntity(memberEntity);

			// 保存军团信息
			saveLegionEntity(legionEntity);

			// 钱币变化
			protocol.setWalletEntity(walletEntity);
			// 军团变化
			protocol.setLegionEntity(legionEntity);
			// 记录动态
			foundHistory(playerID, legionID, LegionManageType.BUILD, buildType);
			// 军团动态
			protocol.setHistoryList(getLegionHistoryEntityList(legionEntity.getLegionID()));
			MainTaskModule mainTaskModule = ModuleManager.getModule(ModuleNames.MainTaskModule, MainTaskModule.class);
			mainTaskModule.updateTaskByActionType(playerID, ActionType.LEGIONDEVOTE, 0, null);
			break;
		case LegionManageType.UPGRADE:
			// 检查权限
			if (!legionEntity.isLegatus(playerID)) {
				protocol.setCode(ErrorIds.LEGION_NOTMANAGE);
				return;
			}
			// 获取军团大厅配置信息
			LegionBuildData hallBuild = buildDataMap.get(Integer.parseInt(legionEntity.getLegionHall().getCurrentPid()));

			if (hallBuild.getNextID() == 0) {
				protocol.setCode(ErrorIds.LEGION_BUILDLEVELMAX);
				return;
			}
			// 获取升级消耗
			List<Integer> upgradeTypeList = hallBuild.getKeys(hallBuild.getUpgradeRequire());
			for (Integer upgradeType : upgradeTypeList) {
				// 获取消耗数量
				Integer upgradeValue = hallBuild.getValue(upgradeType);
				if (KindIDs.SILVERTYPE == upgradeType) {
					if (walletEntity.getSilver() < upgradeValue) {
						protocol.setCode(ErrorIds.LEGION_SILVERINSUFFICIENT);
						return;
					}
					// 减少银币
					walletEntity = walletModule.consumeCurrencyWalletEntity(playerID, KindIDs.SILVERTYPE, upgradeValue, true, null);
				}
				if (LegionKindIDs.LEGION_DEVOTE == upgradeType) {
					if (legionEntity.getLegionDevote() < upgradeValue) {
						protocol.setCode(ErrorIds.LEGION_DEVOTESUFFICIENT);
						return;
					}
					// 减少军团贡献
					legionEntity.consumeLegionDevote(upgradeValue);
				}
			}
			// 设置军团大厅当前pid
			legionEntity.getLegionHall().setCurrentPid(hallBuild.getNextID() + "");

			// 获取军团大厅下一级配置信息
			LegionBuildData nextHallBuild = buildDataMap.get(hallBuild.getNextID());
			// 设置军团等级
			legionEntity.setLevel(nextHallBuild.getBuildLevel());
			// 设置最大人数
			legionEntity.setMaxNum(nextHallBuild.getMaxMember());
			// 设置关公殿最大参拜次数
			legionEntity.getLegiongg().setMaxVisit(nextHallBuild.getMaxMember());
			saveLegionEntity(legionEntity);

			// 钱币变化
			protocol.setWalletEntity(walletEntity);
			// 军团变化
			protocol.setLegionEntity(legionEntity);

			// 记录升级动态
			foundHistory(playerID, legionID, LegionManageType.HALLUP, hallBuild.getPid() + "#" + hallBuild.getNextID());
			// 军团动态
			protocol.setHistoryList(getLegionHistoryEntityList(legionEntity.getLegionID()));

			// //////////////////////////////////////////////
			// 军团大厅升级日志
			JSONObject params = new JSONObject();
			params.put("legionID", legionID);
			params.put("bulidID", legionEntity.getLegionHall().getCurrentPid());
			analyseModule.analyLog(playerID, "ALLIANCE_BUILDING_UP", params, null, null, null);
			break;
		}
	}

	/**
	 * 获取军团信息,返回成员信息
	 * 
	 * @param legionID
	 * @param protocol
	 */
	public void getInfoLegion(String playerID, String legionID, LegionProtocol protocol) {
		if (legionID == null || legionID.isEmpty()) {
			protocol.setCode(ErrorIds.LEGION_ID_ISNOTNULL);
			return;
		}
		LegionEntity legionEntity = getLegionEntity(legionID);
		if (legionEntity == null) {
			protocol.setCode(ErrorIds.LEGION_ISNOTHING);
			return;
		}
		// 根据用户id获取用户信息
		PlayerEntity playerEntity = loginModule.getPlayerEntity(playerID);
		if (playerEntity == null) {
			protocol.setCode(ErrorIds.LEGION_PLAYERNOTEXIST);
			return;
		}
		if (playerEntity.getGroupID().equals("0")) {
			protocol.setCode(ErrorIds.LEGION_NOTJOIN);
			return;
		}
		// 重置珍品列表
		if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) >= LegionConstans.LEGION_GOODS_FLUSHTIME) {
			// 判断是否需要重置珍品列表
			if (!legionEntity.getLegionShop().isGoodFlush()) {
				initLegionShopGoods(legionEntity);
			}
		}
		protocol.setLegionEntity(legionEntity);
		// 军团动态
		List<LegionHistoryEntity> historyList = getLegionHistoryEntityList(legionEntity.getLegionID());
		protocol.setHistoryList(historyList);
	}

	/**
	 * 获取成员列表
	 * 
	 * @param legionID
	 * @param protocol
	 */
	public void memberLegion(String playerID, String legionID, LegionProtocol protocol) {
		if (legionID == null || legionID.isEmpty()) {
			protocol.setCode(ErrorIds.LEGION_ID_ISNOTNULL);
			return;
		}
		LegionEntity legionEntity = getLegionEntity(legionID);
		if (legionEntity == null) {
			protocol.setCode(ErrorIds.LEGION_ISNOTHING);
			return;
		}
		// 根据用户id获取用户信息
		PlayerEntity playerEntity = loginModule.getPlayerEntity(playerID);
		if (playerEntity == null) {
			protocol.setCode(ErrorIds.LEGION_PLAYERNOTEXIST);
			return;
		}
		if (playerEntity.getGroupID().equals("0")) {
			protocol.setCode(ErrorIds.LEGION_NOTJOIN);
			return;
		}
		protocol.setLegionEntity(legionEntity);
	}

	/**
	 * 获取申请加入列表
	 * 
	 * @param playerID
	 * @param protocol
	 */
	public void applysLegion(String playerID, String legionID, LegionProtocol protocol) {
		if (legionID == null || legionID.isEmpty()) {
			protocol.setCode(ErrorIds.LEGION_ID_ISNOTNULL);
			return;
		}
		LegionEntity legionEntity = getLegionEntity(legionID);
		if (legionEntity == null) {
			protocol.setCode(ErrorIds.LEGION_ISNOTHING);
			return;
		}
		if (!legionEntity.isLegatus(playerID)) {
			protocol.setCode(ErrorIds.LEGION_NOTMANAGE);
			return;
		}
		protocol.setLegionEntity(legionEntity);
	}

	/**
	 * 加入退出军团
	 * 
	 * @param playerID
	 * @param legionID
	 * @param legionType
	 * @param protocol
	 */
	public void JoinOutLegion(String playerID, String legionID, String legionType, LegionProtocol protocol) {
		if (null == legionID || legionID.isEmpty()) {
			protocol.setCode(ErrorIds.LEGION_ID_ISNOTNULL);
			return;
		}
		if (null == legionType || legionType.isEmpty() || null == playerID || playerID.isEmpty()) {
			protocol.setCode(ErrorIds.LEGION_PARAMEERROR);
			return;
		}
		// 根据用户id获取用户信息
		PlayerEntity playerEntity = loginModule.getPlayerEntity(playerID);
		// 根据军团id 获取军团信息
		LegionEntity legionEntity = getLegionEntity(legionID);
		if (playerEntity == null) {
			protocol.setCode(ErrorIds.LEGION_PLAYERNOTEXIST);
			return;
		}
		if (playerEntity.getLevel() < LegionConstans.LEGION_HAVINGLEVEL) {
			protocol.setCode(ErrorIds.LEGION_LEVELINSUFFICIENT);
			return;
		}
		if (legionEntity == null) {
			protocol.setCode(ErrorIds.LEGION_ISNOTHING);
			return;
		}
		switch (legionType) {
		case LegionManageType.JOIN:
			if (!playerEntity.getGroupID().equals("0")) {
				protocol.setCode(ErrorIds.LEGION_ISHAVE);
				return;
			}
			if (getJoinCooling(playerID) > 0) {
				protocol.setCode(ErrorIds.LEGION_JOINCOOLING);
				return;
			}
			List<String> joinList = getJoinLegionList(playerID, null);
			if (joinList != null && joinList.contains(legionID)) {
				protocol.setCode(ErrorIds.LEGION_ISAPPLYJOIN);
				return;
			}

			joinList = getJoinLegionList(playerID, legionID);
			if (joinList.size() <= LegionConstans.LEGION_JOINNUM) {
				// 保存军团申请列表
				legionEntity.addApplys(playerID, Utilities.getDateTime(LegionConstans.YMDHMS));
				saveLegionEntity(legionEntity);
				protocol.setLegionID(legionID);
				return;
			} else {
				protocol.setCode(ErrorIds.LEGION_JOINNUMERROR);
				return;
			}
		case LegionManageType.CANCAL:
			List<String> applyList = getJoinLegionList(playerID, null);
			if (applyList != null && applyList.contains(legionID)) {
				applyList = resetJoinLegionList(playerID, legionID);
				// 保存军团申请列表
				legionEntity.delApplys(playerID);
				saveLegionEntity(legionEntity);

				protocol.setLegionID(legionID);
			} else {
				protocol.setCode(ErrorIds.LEGION_NOTAPPLYJOIN);
			}
			return;
		case LegionManageType.EXIT:
			if (playerEntity.getGroupID().equals("0")) {
				protocol.setCode(ErrorIds.LEGION_NOTJOIN);
				return;
			}
			// 军团长无法退出军团
			if (legionEntity.getLegatus().equals(playerID)) {
				protocol.setCode(ErrorIds.LEGION_LEGATUSNOTOUT);
				return;
			}

			// 获取成员信息
			LegionMemberEntity memberEntity = getLegionMemberEntity(playerID);
			memberEntity.setLastTime(System.currentTimeMillis());
			saveLegionMemberEntity(memberEntity);

			// 军团删除成员
			legionEntity.delMember(playerID);
			saveLegionEntity(legionEntity);

			// 修改军团id为0,表示退出
			playerEntity.setGroupID("");
			loginModule.savePlayerEntity(playerEntity);

			foundHistory(playerID, legionID, LegionManageType.EXIT, null);

			// //////////////////////////////////////////////
			// 退出军团记录日志
			JSONObject params = new JSONObject();
			params.put("legionID", legionID);
			analyseModule.analyLog(playerID, "ALLIANCE_EXIT", params, null, null, null);
			return;
		}
	}

	/**
	 * 获取军团列表
	 * 
	 * @param playerID
	 * @param protocol
	 */
	public void getListLegion(String playerID, String page, String name, LegionProtocol protocol) {
		List<LegionEntity> legionList = null;
		if (null != page && !page.isEmpty()) {
			legionList = getLegionList(Integer.parseInt(page), name);
		} else {
			legionList = getLegionList(1, name);
		}
		LegionMemberEntity memberEntity = getLegionMemberEntity(playerID);
		if (memberEntity != null) {
			protocol.setJoinList(memberEntity.getApplyList());
		}
		protocol.setCoolingTime(getJoinCooling(memberEntity) + "");
		protocol.setLegionList(legionList);
	}

	/**
	 * 军团管理,成员管理
	 * 
	 * @param playerID
	 * @param legionID
	 * @param legionType
	 * @param legionPlayerID
	 * @param protocol
	 */
	public void manageLegion(String playerID, String legionID, String legionType, String legionPlayerID, String password, LegionProtocol protocol) {
		if (null == legionID || legionID.isEmpty()) {
			protocol.setCode(ErrorIds.LEGION_ID_ISNOTNULL);
			return;
		}
		if (null == legionType || legionType.isEmpty()) {
			protocol.setCode(ErrorIds.LEGION_PARAMEERROR);
			return;
		}
		// 根据用户id获取用户信息
		PlayerEntity playerEntity = loginModule.getPlayerEntity(playerID);
		if (playerEntity == null) {
			protocol.setCode(ErrorIds.LEGION_PLAYERNOTEXIST);
			return;
		}
		if (playerEntity.getGroupID().equals("0")) {
			protocol.setCode(ErrorIds.LEGION_NOTJOIN);
			return;
		}

		// 根据军团id 获取军团信息
		LegionEntity legionEntity = getLegionEntity(legionID);
		if (legionEntity == null) {
			protocol.setCode(ErrorIds.LEGION_ISNOTHING);
			return;
		}
		if (!legionEntity.isLegatus(playerID)) {
			protocol.setCode(ErrorIds.LEGION_NOTMANAGE);
			return;
		}
		switch (legionType) {
		case LegionManageType.PASS:
			if (legionPlayerID == null || legionPlayerID.isEmpty()) {
				protocol.setCode(ErrorIds.LEGION_PLAYERNOTEXIST);
				return;
			}
			// 根据用户id获取申请加入用户信息
			PlayerEntity joinEntity = loginModule.getPlayerEntity(legionPlayerID);
			// 根据用户id 获取军团成员信息
			LegionMemberEntity joinMemberEntity = getLegionMemberEntity(legionPlayerID);
			if (joinEntity == null) {
				protocol.setCode(ErrorIds.LEGION_PLAYERNOTEXIST);
				return;
			}
			if (legionEntity.getMaxNum() <= legionEntity.getMembers().size()) {
				protocol.setCode(ErrorIds.LEGION_MEMBERISFULL);
				return;
			}
			if (!joinEntity.getGroupID().equals("0")) {
				protocol.setCode(ErrorIds.LEGION_MEMBERJOINOTHERORCANCAL);
				return;
			}
			if (!legionEntity.getApplys().containsKey(legionPlayerID)) {
				protocol.setCode(ErrorIds.LEGION_MEMBERJOINOTHERORCANCAL);
				return;
			}

			// 清除军团申请记录
			if (joinMemberEntity.getApplyList().size() > 0) {
				List<String> applyList = joinMemberEntity.getApplyList();
				for (String applyLegion : applyList) {
					LegionEntity applyLegionEntity = getLegionEntity(applyLegion);
					applyLegionEntity.delApplys(legionPlayerID);
					saveLegionEntity(applyLegionEntity);
				}
			}

			// 清除成员申请记录
			joinMemberEntity.delApplyList(legionID);
			saveLegionMemberEntity(joinMemberEntity);

			// 设置用户军团id
			joinEntity.setGroupID(legionID);
			loginModule.savePlayerEntity(joinEntity);
			// 增加成员
			legionEntity.addMember(legionPlayerID);
			legionEntity.delApplys(legionPlayerID);
			saveLegionEntity(legionEntity);

			protocol.setLegionEntity(legionEntity);

			// 记录加入日志
			foundHistory(legionPlayerID, legionID, LegionManageType.JOIN, null);
			// 军团动态
			protocol.setHistoryList(getLegionHistoryEntityList(legionID));
			return;
		case LegionManageType.PASS_ALL:
			if (legionEntity.getMaxNum() <= legionEntity.getMembers().size()) {
				protocol.setCode(ErrorIds.LEGION_MEMBERISFULL);
				return;
			}
			// 获取军团申请加入列表
			Set<Entry<String, String>> applys = legionEntity.getApplys().entrySet();
			// 申请列表为空
			if (legionEntity.getApplys().size() == 0) {
				protocol.setCode(ErrorIds.LEGION_APPLYSISNULL);
				return;
			}
			// 遍历申请列表
			for (Entry<String, String> entry : applys) {
				String applyPlayerID = entry.getKey();
				// 根据用户id获取申请加入用户信息
				PlayerEntity tempEntity = loginModule.getPlayerEntity(applyPlayerID);
				LegionMemberEntity tempMember = getLegionMemberEntity(applyPlayerID);
				if (tempEntity.getGroupID().equals("0")) {
					// 清除军团申请列表
					if (tempMember.getApplyList().size() > 0) {
						List<String> applyList = tempMember.getApplyList();
						for (String applyLegion : applyList) {
							LegionEntity applyLegionEntity = getLegionEntity(applyLegion);
							applyLegionEntity.delApplys(legionPlayerID);
							saveLegionEntity(applyLegionEntity);
						}
					}
					break;
				}
				// 增加成员
				legionEntity.addMember(applyPlayerID);
				// 清除全部申请记录
				tempMember.delApplyAll();
				saveLegionMemberEntity(tempMember);
				// 用户增加军团id
				tempEntity.setGroupID(legionID);
				loginModule.savePlayerEntity(tempEntity);
				// 记录加入日志
				foundHistory(applyPlayerID, legionID, LegionManageType.JOIN, null);
			}
			legionEntity.delApplyAll();
			saveLegionEntity(legionEntity);
			protocol.setLegionEntity(legionEntity);
			// 军团动态
			protocol.setHistoryList(getLegionHistoryEntityList(legionEntity.getLegionID()));
			return;
		case LegionManageType.REFUSE:
			if (legionPlayerID == null || legionPlayerID.isEmpty()) {
				protocol.setCode(ErrorIds.LEGION_PLAYERNOTEXIST);
				return;
			}
			// 根据用户id 获取军团成员信息
			LegionMemberEntity refuseMember = getLegionMemberEntity(legionPlayerID);
			refuseMember.delApplyList(legionID);
			saveLegionMemberEntity(refuseMember);

			legionEntity.delApplys(legionPlayerID);
			saveLegionEntity(legionEntity);

			protocol.setLegionEntity(legionEntity);
			return;
		case LegionManageType.REFUSE_ALL:
			// 获取军团申请加入列表
			Set<Entry<String, String>> refuses = legionEntity.getApplys().entrySet();
			// 申请列表为空
			if (legionEntity.getApplys().size() == 0) {
				protocol.setCode(ErrorIds.LEGION_APPLYSISNULL);
				return;
			}
			// 遍历申请列表
			for (Entry<String, String> entry : refuses) {
				String applyPlayerID = entry.getKey();
				LegionMemberEntity tempMember = getLegionMemberEntity(applyPlayerID);
				tempMember.delApplyList(legionID);
				saveLegionMemberEntity(tempMember);
			}
			// 清空军团申请列表
			legionEntity.delApplyAll();
			saveLegionEntity(legionEntity);
			protocol.setLegionEntity(legionEntity);
			break;
		case LegionManageType.KICK:
			if (legionPlayerID == null || legionPlayerID.isEmpty()) {
				protocol.setCode(ErrorIds.LEGION_PLAYERNOTEXIST);
				return;
			}
			// 根据用户id获取申请加入用户信息
			PlayerEntity kickEntity = loginModule.getPlayerEntity(legionPlayerID);
			// 根据用户id 获取军团成员信息
			LegionMemberEntity kickMember = getLegionMemberEntity(legionPlayerID);

			// 副军团长不能被踢出
			if (legionEntity.isLegatus2(legionPlayerID)) {
				protocol.setCode(ErrorIds.LEGION_NOKILLLEGATUS2);
				return;
			}

			// 军团长不能被踢出
			if (legionEntity.isLegatus(legionPlayerID)) {
				protocol.setCode(ErrorIds.LEGION_NOKILLLEGATUS);
				return;
			}

			// 检查踢出用户是否副军团长
			if (legionEntity.isLegatus2(legionPlayerID)) {
				legionEntity.delLegatus(legionPlayerID);
			}

			legionEntity.delMember(legionPlayerID);
			saveLegionEntity(legionEntity);
			// 设置踢出时间
			kickMember.setLastTime(System.currentTimeMillis());
			saveLegionMemberEntity(kickMember);
			// 设置用户军团id
			kickEntity.setGroupID("");
			loginModule.savePlayerEntity(kickEntity);
			// 记录踢人日志
			foundHistory(legionPlayerID, legionID, LegionManageType.KICK, playerEntity.getNickName());
			// 军团动态
			protocol.setHistoryList(getLegionHistoryEntityList(legionEntity.getLegionID()));
			protocol.setLegionEntity(legionEntity);

			// 发送邮件
			String msg = "你已经被 [" + playerEntity.getNickName() + "] 从 <" + legionEntity.getName() + "> 踢出! ";
			mailBoxModule.sendEventRewardMail(legionPlayerID, "公会通知", msg);

			// //////////////////////////////////////////////
			// 军团踢人记录日志
			JSONObject params = new JSONObject();
			params.put("legionID", legionID);
			params.put("legionPlayerID", legionPlayerID);
			analyseModule.analyLog(playerID, "ALLIANCE_EXIT", params, null, null, null);

			return;
		case LegionManageType.UPDOWAN:
			if (legionPlayerID == null || legionPlayerID.isEmpty()) {
				protocol.setCode(ErrorIds.LEGION_PLAYERNOTEXIST);
				return;
			}
			// 降级
			if (legionEntity.isLegatus2(legionPlayerID)) {
				legionEntity.delLegatus(legionPlayerID);
				// 记录降职日志
				foundHistory(legionPlayerID, legionID, LegionManageType.DOWN, null);
			} else {
				if (legionEntity.getLegatusList().size() >= LegionConstans.LEGION_LEGATUSNUM) {
					protocol.setCode(ErrorIds.LEGION_LEGATUSISFULL);
					return;
				}
				legionEntity.addLegatus(legionPlayerID);
				// 记录升职日志
				foundHistory(legionPlayerID, legionID, LegionManageType.UP, null);
			}
			saveLegionEntity(legionEntity);
			// 军团动态
			protocol.setHistoryList(getLegionHistoryEntityList(legionEntity.getLegionID()));
			protocol.setLegionEntity(legionEntity);
			return;
		case LegionManageType.TRANSFER:
			// 军团转让
			if (null == password || password.isEmpty() || !password.equals(legionEntity.getPwd())) {
				protocol.setCode(ErrorIds.LEGION_PASSERROR);
				return;
			}
			if (legionEntity.isLegatus(playerID)) {
				legionEntity.setLegatus(legionPlayerID);
				if (legionEntity.isLegatus2(legionPlayerID)) {
					legionEntity.delLegatus(legionPlayerID);
				}
				// 保存军团数据
				saveLegionEntity(legionEntity);
				// 记录转让军团日志
				foundHistory(legionPlayerID, legionID, LegionManageType.TRANSFER, null);
			} else {
				protocol.setCode(ErrorIds.LEGION_NOTMANAGE);
				return;
			}
			protocol.setLegionEntity(legionEntity);

			// //////////////////////////////////////////////
			// 军团转让记录日志
			JSONObject transfer = new JSONObject();
			transfer.put("legionID", legionID);
			transfer.put("legionPlayerID", legionPlayerID);
			analyseModule.analyLog(playerID, "ALLIANCE_EXIT", transfer, null, null, null);
			return;
		}
	}

	/**
	 * @param playerID
	 * @param legionID
	 * @param legionType
	 * @param notice
	 * @param declaration
	 * @param password
	 * @param newPassword
	 * @param protocol
	 */
	public void updateLegion(String playerID, String legionID, String legionType, String notice, String declaration, String password, String newPassword,
			LegionProtocol protocol) {
		if (null == legionID || legionID.isEmpty()) {
			protocol.setCode(ErrorIds.LEGION_ID_ISNOTNULL);
			return;
		}
		if ((null == legionType || legionType.isEmpty())) {
			protocol.setCode(ErrorIds.LEGION_PARAMEERROR);
			return;
		}
		// 根据用户id获取用户信息
		PlayerEntity playerEntity = loginModule.getPlayerEntity(playerID);
		if (playerEntity == null) {
			protocol.setCode(ErrorIds.LEGION_PLAYERNOTEXIST);
			return;
		}
		if (playerEntity.getGroupID().equals("0")) {
			protocol.setCode(ErrorIds.LEGION_NOTJOIN);
			return;
		}
		LegionEntity legionEntity = getLegionEntity(legionID);
		if (legionEntity == null) {
			protocol.setCode(ErrorIds.LEGION_ISNOTHING);
			return;
		}
		if (!legionEntity.isLegatus(playerID)) {
			protocol.setCode(ErrorIds.LEGION_NOTMANAGE);
			return;
		}

		switch (legionType) {
		case LegionManageType.PASSWORD:
			if (password == null || password.isEmpty()) {
				protocol.setCode(ErrorIds.LEGION_OLDPASSISNULL);
				return;
			}
			if (newPassword == null || newPassword.isEmpty()) {
				protocol.setCode(ErrorIds.LEGION_NEWPASSISNULL);
				return;
			}
			if (!password.equals(legionEntity.getPwd())) {
				protocol.setCode(ErrorIds.LEGION_PASSERROR);
				return;
			}
			if (!Utilities.checkLenght(newPassword, 4, 8)) {
				protocol.setCode(ErrorIds.LEGION_NEWPASSLENGTHERROR);
				return;
			}
			// 修改军团密码
			legionEntity.setPwd(newPassword);
			break;
		case LegionManageType.DECLARATION:
			if (null == declaration || declaration.isEmpty()) {
				protocol.setCode(ErrorIds.LEGION_DECLARATIONNOTNULL);
				return;
			}
			if (!Utilities.checkLenght(declaration, 2, 50)) {
				protocol.setCode(ErrorIds.LEGION_DECLARATIONLENGTHERROR);
				return;
			}
			legionEntity.setDeclaration(declaration);

			if (!CommonMethod.checkNameShieldedKeyword(declaration)) {
				logger.error("工会宣言不合法");
				throw new IllegalArgumentException(ErrorIds.NameNoLegal + "");
			}

			// 记录修改宣言
			foundHistory(playerID, legionID, LegionManageType.DECLARATION, null);
			break;
		case LegionManageType.NOTICE:
			if (null == notice || notice.isEmpty()) {
				protocol.setCode(ErrorIds.LEGION_NOTICENOTNULL);
				return;
			}
			if (!Utilities.checkLenght(notice, 2, 50)) {
				protocol.setCode(ErrorIds.LEGION_NOTICELENGTHERROR);
				return;
			}
			legionEntity.setNotice(notice);

			if (!CommonMethod.checkNameShieldedKeyword(notice)) {
				logger.error("工会公告不合法");
				throw new IllegalArgumentException(ErrorIds.NameNoLegal + "");
			}

			// 记录修改公告
			foundHistory(playerID, legionID, LegionManageType.NOTICE, notice);
			break;
		case LegionManageType.KILL:
			if (password == null || password.isEmpty()) {
				protocol.setCode(ErrorIds.LEGION_OLDPASSISNULL);
				return;
			}
			if (!password.equals(legionEntity.getPwd())) {
				protocol.setCode(ErrorIds.LEGION_PASSERROR);
				return;
			}

			if (legionEntity.getMembers().size() > 1) {
				protocol.setCode(ErrorIds.LEGION_HAS_MEMBER);
				return;
			}

			// 军团解散,所有成员军团设置为0,表示没有军团,同时加入冷却时间
			for (String member : legionEntity.getMembers()) {
				PlayerEntity temp = loginModule.getPlayerEntity(member);
				LegionMemberEntity memberEntity = getLegionMemberEntity(member);
				memberEntity.setLastTime(System.currentTimeMillis());
				saveLegionMemberEntity(memberEntity);
				temp.setGroupID("");
				loginModule.savePlayerEntity(temp);
			}
			legionEntity.setKill(true);

			break;
		}
		saveLegionEntity(legionEntity);
	}

	/**
	 * 创建军团
	 * 
	 * @param playerID
	 * @param legionName
	 * @param legionDeclaration
	 * @return
	 */
	public void createLegion(String playerID, String legionName, String legionType, LegionProtocol protocol) {
		// 根据用户id获取用户信息
		PlayerEntity playerEntity = loginModule.getPlayerEntity(playerID);
		// 根据用户id获取用户钱包
		WalletEntity walletEntity = walletModule.getwalletEntity(playerID);
		// 根据军团名字获取军团信息
		LegionEntity legionEntity = legionDao.getLegionByName(legionName);

		if (null == legionName || legionName.isEmpty()) {
			protocol.setCode(ErrorIds.LEGION_NAMENOTNULL);
			return;
		}
		if (legionEntity != null) {
			protocol.setCode(ErrorIds.LEGION_NAMEISEXIST);
			return;
		}
		if (playerEntity == null) {
			protocol.setCode(ErrorIds.LEGION_PLAYERNOTEXIST);
			return;
		}
		if (!playerEntity.getGroupID().equals("0")) {
			protocol.setCode(ErrorIds.LEGION_ISHAVE);
			return;
		}
		if (!Utilities.checkLenght(legionName, 2, 10)) {
			protocol.setCode(ErrorIds.LEGION_NAMELENGTHERROR);
			return;
		}
		if (playerEntity.getLevel() < LegionConstans.LEGION_HAVINGLEVEL) {
			protocol.setCode(ErrorIds.LEGION_LEVELINSUFFICIENT);
			return;
		}

		if (getJoinCooling(playerID) > 0) {
			protocol.setCode(ErrorIds.LEGION_JOINCOOLING);
			return;
		}

		if (!CommonMethod.checkNameShieldedKeyword(legionName)) {
			logger.error("工会名不合法");
			throw new IllegalArgumentException(ErrorIds.NameNoLegal + "");
		}

		if (LegionManageType.GOLD.equals(legionType)) {
			if (walletEntity.getGold() < LegionConstans.LEGION_CREATEGOLD) {
				protocol.setCode(ErrorIds.LEGION_GOLDINSUFFICIENT);
				return;
			}
			// 减少金币
			walletEntity = walletModule.consumeCurrencyWalletEntity(playerID, KindIDs.GOLDTYPE, LegionConstans.LEGION_CREATEGOLD, true, null);
		}

		if (null == legionType || LegionManageType.SILVER.equals(legionType)) {
			if (walletEntity.getSilver() < LegionConstans.LEGION_CREATESILVER) {
				protocol.setCode(ErrorIds.LEGION_SILVERINSUFFICIENT);
				return;
			}
			// 减少银币
			walletEntity = walletModule.consumeCurrencyWalletEntity(playerID, KindIDs.SILVERTYPE, LegionConstans.LEGION_CREATESILVER, true, null);
		}

		legionEntity = initLegion(playerID, legionName);

		// 设置玩家军团id
		playerEntity.setGroupID(legionEntity.getLegionID());
		loginModule.savePlayerEntity(playerEntity);
		protocol.setWalletEntity(walletEntity);
		protocol.setLegionEntity(legionEntity);
		// 记录创建日志
		foundHistory(playerID, legionEntity.getLegionID(), LegionManageType.CREATE, legionType);
		List<LegionHistoryEntity> historyList = getLegionHistoryEntityList(legionEntity.getLegionID());
		protocol.setHistoryList(historyList);

		// //////////////////////////////////////////////
		// 创建军团日志
		JSONObject params = new JSONObject();
		params.put("legionName", legionName);
		params.put("legionType", legionType);
		analyseModule.analyLog(playerID, "CREATE_ALLIANCE", params, null, null, null);

		// ////
		// // 元宝消耗记录
		// ///
		if (LegionManageType.GOLD.equals(legionType)) {
			analyseModule.goldCostLog(playerID, LegionConstans.LEGION_CREATEGOLD, 1, LegionConstans.LEGION_CREATEGOLD, "createLegion", "legion");
		}
	}

	private String getLegionID() {
		String clsName = SysConstants.LegionIDEntity;
		long legionID = keyGeneratorDAO.updateInc(clsName);
		return legionID + "";
	}

	private String getLegionHistoryID() {
		String clsName = SysConstants.LegionHistoryIDEntity;
		long historyID = keyGeneratorDAO.updateInc(clsName);
		return historyID + "";
	}

	// 军团信息初始化
	private LegionEntity initLegion(String playerID, String legionName) {
		LegionEntity legionEntity = new LegionEntity();
		legionEntity.setLegionID(serverID + "-" + getLegionID());
		legionEntity.setName(legionName);
		legionEntity.setLegatus(playerID);
		legionEntity.setPwd(LegionConstans.LEGION_DEFAULTPASSWORD);
		legionEntity.setDeclaration(LegionConstans.DECLARATION);
		legionEntity.setNotice(LegionConstans.NOTICE);

		// 方便测试加入军团贡献10000
		// legionEntity.setLegionDevote(10000);

		// 军团成员初始化
		LegionMemberEntity memberEntity = memberDao.getEntity(playerID);
		if (memberEntity == null) {
			memberEntity = new LegionMemberEntity();
			memberEntity.setPlayerID(playerID);
			memberEntity.setLastTime(System.currentTimeMillis());
			memberEntity.setBuild(LegionConstans.LEGION_DEFAULT_BUILD);
			memberEntity.setVisit(LegionConstans.LEGION_VISITMAX);
			memberEntity.setCompare(LegionConstans.LEGION_COMPAREMAX);
			// 方便测试加入个人贡献10000
			// memberEntity.addDevote(10000);
		}
		saveLegionMemberEntity(memberEntity);
		legionEntity.addMember(playerID);

		// 军团大厅初始化
		LegionHall legionHall = new LegionHall();
		LegionBuildData defaultHall = buildDataMap.get(LegionConstans.LEGION_DEFAULT_HALL);
		legionHall.setCurrentPid(LegionConstans.LEGION_DEFAULT_HALL + "");
		legionEntity.setLegionHall(legionHall);
		legionEntity.setMaxNum(defaultHall.getMaxMember());
		// 军团商店初始化
		LegionShop legionShop = new LegionShop();
		legionShop.setCurrentPid(LegionConstans.LEGION_DEFAULT_SHOP);
		// 珍品初始化
		legionShop.initGoods(dropData);
		legionEntity.setLegionShop(legionShop);
		// 军团关公殿初始化
		LegionGG legionGG = new LegionGG();
		legionGG.setCurrentPid(LegionConstans.LEGION_DEFAULT_GG);
		legionGG.setMaxVisit(legionEntity.getMaxNum());
		legionGG.setVisitNum(legionEntity.getMaxNum());
		legionEntity.setLegiongg(legionGG);
		saveLegionEntity(legionEntity);

		legionEntity = getLegionByName(legionName);
		return legionEntity;
	}

	/**
	 * 重置军团商店珍品列表
	 * 
	 * @param legionID
	 */
	public void initLegionShopGoods(LegionEntity legionEntity) {
		legionEntity.getLegionShop().initGoods(dropData);
		saveLegionEntity(legionEntity);
	}

	// 获取军团操作冷却时间
	private long getJoinCooling(String playerID) {
		LegionMemberEntity memberEntity = getLegionMemberEntity(playerID);
		long coolingTime = 0;
		if (memberEntity != null) {
			coolingTime = LegionConstans.LEGION_COOLING * 3600 * 1000;
			coolingTime -= (System.currentTimeMillis() - memberEntity.getLastTime());
			coolingTime = coolingTime < 0 ? 0 : coolingTime;
		}
		return coolingTime;
	}

	// 获取军团操作冷却时间
	private long getJoinCooling(LegionMemberEntity memberEntity) {
		long coolingTime = 0;
		if (memberEntity != null) {
			coolingTime = LegionConstans.LEGION_COOLING * 3600 * 1000;
			coolingTime -= (System.currentTimeMillis() - memberEntity.getLastTime());
			coolingTime = coolingTime < 0 ? 0 : coolingTime;
		}
		return coolingTime;
	}

	// 获取申请加入军团列表
	private List<String> getJoinLegionList(String playerID, String legionID) {
		LegionMemberEntity memberEntity = getLegionMemberEntity(playerID);
		if (memberEntity == null) {
			memberEntity = new LegionMemberEntity();
			memberEntity.setPlayerID(playerID);
			memberEntity.setBuild(LegionConstans.LEGION_DEFAULT_BUILD);
			memberEntity.setVisit(LegionConstans.LEGION_VISITMAX);
			memberEntity.setCompare(LegionConstans.LEGION_COMPAREMAX);
		}
		if (legionID != null) {
			memberEntity.addApplyList(legionID);
			if (memberEntity.getApplyList().size() <= LegionConstans.LEGION_JOINNUM) {
				saveLegionMemberEntity(memberEntity);
			}
		}
		return memberEntity.getApplyList();
	}

	// 更新申请加入军团列表
	private List<String> resetJoinLegionList(String playerID, String legionID) {
		LegionMemberEntity memberEntity = getLegionMemberEntity(playerID);
		memberEntity.delApplyList(legionID);
		saveLegionMemberEntity(memberEntity);
		return memberEntity.getApplyList();
	}

	/**
	 * 保存操作日志
	 * 
	 * @param playerID
	 * @param legionID
	 * @param type
	 * @param content
	 */
	public void foundHistory(String playerID, String legionID, String type, String content) {
		// 创建军团操作记录
		LegionHistoryEntity legionHistoryEntity = new LegionHistoryEntity();
		legionHistoryEntity.setHistoryID(getLegionHistoryID());
		legionHistoryEntity.setContent(content);
		legionHistoryEntity.setType(type);
		legionHistoryEntity.setLegionID(legionID);
		legionHistoryEntity.setPlayerID(playerID);
		legionHistoryEntity.setCreateTime(System.currentTimeMillis());
		// 保存记录
		saveLegionHistoryEntity(legionHistoryEntity);
	}

	/**
	 * 检查参拜时间
	 */
	private static boolean isVisterTime() {
		String[] times = LegionConstans.LEGION_VISIT_TIME.split("-");
		int start = Integer.parseInt(times[0]);
		int end = Integer.parseInt(times[1]);
		int now = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		// 是否活动时间
		if (now >= start && now <= end) {
			return true;
		}
		return false;
	}

	public LegionEntity getLegionEntity(String legionID) {
		return legionDao.getEntity(legionID);
	}

	public LegionEntity getLegionEntity(String legionID, String serverID) {
		return legionDao.getEntity(legionID, serverID);
	}

	public void saveLegionEntity(LegionEntity legionEntity) {
		legionDao.save(legionEntity);
	}

	public LegionEntity getLegionByName(String legionName) {
		return legionDao.getLegionByName(legionName);
	}

	public List<LegionEntity> getLegionList(int page, String name) {
		return legionDao.getLegionList(page, name);
	}

	/**
	 * 获取level等级的军团列表
	 * 
	 * @param level
	 * @return
	 */
	public List<LegionEntity> getLegionLevelList(int level) {
		return legionDao.getLegionLevelList(level);
	}

	/**
	 * 获取最后一次建设时间
	 * 
	 * @param playerID
	 * @return
	 */
	public long getLegionHistoryBuildDay(String playerID) {
		LegionHistoryEntity historyEntity = historyDAO.getLegionHistoryEntityByPlayerID(playerID, "build");
		if (historyEntity == null) {
			return 0;
		} else {
			return historyEntity.getCreateTime();
		}
	}

	public void saveLegionHistoryEntity(LegionHistoryEntity legionHistoryEntity) {
		historyDAO.save(legionHistoryEntity);
	}

	public List<LegionHistoryEntity> getLegionHistoryEntityList(String legionID) {
		return historyDAO.getLegionHistoryList(legionID);
	}

	public void saveLegionMemberEntity(LegionMemberEntity legionMemberEntity) {
		memberDao.save(legionMemberEntity);
	}

	public LegionMemberEntity getLegionMemberEntity(String playerID) {
		return memberDao.getEntity(playerID);
	}

	@Override
	public void init() {
		loginModule = ModuleManager.getModule(ModuleNames.LoginModule, LoginModule.class);
		walletModule = ModuleManager.getModule(ModuleNames.WalletModule, WalletModule.class);
		vitatlyModule = ModuleManager.getModule(ModuleNames.VitatlyModule, VitatlyModule.class);
		vipModule = ModuleManager.getModule(ModuleNames.VipModule, VipModule.class);
		analyseModule = ModuleManager.getModule(ModuleNames.AnalyseModule, AnalyseModule.class);
		mailBoxModule = ModuleManager.getModule(ModuleNames.MailBoxModule, MailBoxModule.class);
		initLegionBuild();
		initLegionDevote();
		initLegionShop();
		initLegionVisitReward();
		initDropData();
	}

	/**** 军团设施建造 **/
	public static Map<Integer, LegionBuildData> buildDataMap = new HashMap<Integer, LegionBuildData>();
	/**** 军团建造消耗 **/
	public static Map<Integer, LegionDevoteData> devoteDataMap = new HashMap<Integer, LegionDevoteData>();
	/**** 军团商店物品 **/
	public static Map<Integer, LegionShopData> shopDataMap = new HashMap<Integer, LegionShopData>();
	/**** 军团关公殿参拜 **/
	public static Map<Integer, LegionVisitRewardData> visitRewardDataMap = new HashMap<Integer, LegionVisitRewardData>();
	/**** 军团商店珍品 **/
	public static LegionDropData dropData = new LegionDropData();

	private void initLegionBuild() {
		List<LegionBuildData> dataList = TemplateManager.getTemplateList(LegionBuildData.class);
		for (LegionBuildData data : dataList) {
			buildDataMap.put(data.getPid(), data);
		}
	}

	private void initLegionDevote() {
		List<LegionDevoteData> dataList = TemplateManager.getTemplateList(LegionDevoteData.class);
		for (LegionDevoteData data : dataList) {
			devoteDataMap.put(data.getPid(), data);
		}
	}

	private void initLegionShop() {
		List<LegionShopData> dataList = TemplateManager.getTemplateList(LegionShopData.class);
		for (LegionShopData data : dataList) {
			shopDataMap.put(data.getPid(), data);
		}
	}

	private void initDropData() {
		dropData = TemplateManager.getTemplateData(LegionConstans.LEGION_DROPDATA, LegionDropData.class);
	}

	private void initLegionVisitReward() {
		List<LegionVisitRewardData> dataList = TemplateManager.getTemplateList(LegionVisitRewardData.class);
		for (LegionVisitRewardData data : dataList) {
			visitRewardDataMap.put(data.getPid(), data);
		}
	}
}
