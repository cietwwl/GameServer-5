package com.mi.game.module.vip;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.TemplateManager;
import com.mi.core.engine.annotation.Module;
import com.mi.game.defines.ErrorIds;
import com.mi.game.defines.KindIDs;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.analyse.AnalyseModule;
import com.mi.game.module.base.BaseModule;
import com.mi.game.module.dungeon.DungeonModule;
import com.mi.game.module.dungeon.pojo.DungeonActiveEntity;
import com.mi.game.module.dungeon.pojo.DungeonEliteEntity;
import com.mi.game.module.login.LoginModule;
import com.mi.game.module.login.pojo.PlayerEntity;
import com.mi.game.module.pay.PayModule;
import com.mi.game.module.pay.pojo.PayEntity;
import com.mi.game.module.pet.PetModule;
import com.mi.game.module.pet.pojo.PetEntity;
import com.mi.game.module.reward.RewardModule;
import com.mi.game.module.tower.TowerModule;
import com.mi.game.module.tower.pojo.TowerEntity;
import com.mi.game.module.vip.dao.VipEnitiyDao;
import com.mi.game.module.vip.data.VipData;
import com.mi.game.module.vip.data.VipPriceData;
import com.mi.game.module.vip.define.VipConstants;
import com.mi.game.module.vip.pojo.VipEntity;
import com.mi.game.module.vip.protocol.VipProtocol;
import com.mi.game.module.vitatly.VitatlyModule;
import com.mi.game.module.vitatly.pojo.VitatlyEntity;
import com.mi.game.util.Utilities;

@Module(name = ModuleNames.VipModule, clazz = VipModule.class)
public class VipModule extends BaseModule {

	private static RewardModule rewardModule;
	private static LoginModule loginModule;
	private static DungeonModule dungeonModule;
	private static TowerModule towerModule;
	private static PayModule payModule;
	private static PetModule petModule;
	private static VitatlyModule vitatlyModule;
	private static VipEnitiyDao vipDao = VipEnitiyDao.getInstance();
	private static Map<Integer, VipData> vipDataMap = new TreeMap<Integer, VipData>();
	private static Map<Integer, VipPriceData> vipPriceMap = new HashMap<Integer, VipPriceData>();

	// 获取用户累计充值数
	public int playerPayTotal(String playerID) {
		PayEntity payEntity = payModule.getPayEntity(playerID);
		if (payEntity != null) {
			return payEntity.getPayTotal();
		}
		return VipConstants.PAY_GOLD_TOTAL;
	}

	/**
	 * 获取用户vip信息
	 * 
	 * @param playerID
	 * @param protocol
	 */
	public void vipInfo(String playerID, VipProtocol protocol) {
		protocol.setVipEntity(initVipEntity(playerID));
	}

	/**
	 * 减少特权次数
	 * 
	 * @param playerID
	 * @param actionID
	 * @param protocol
	 * @param protocol
	 */
	public void vipPermission(String playerID, String actionID, VipProtocol protocol) {
		Map<String, Object> itemMap = new HashMap<String, Object>();
		VipEntity vipEntity = initVipEntity(playerID);
		vipEntity = reducePermissionValue(vipEntity, actionID);

		int price = permissionPrice(vipEntity, actionID);
		// 减少货币
		rewardModule.useGoods(playerID, KindIDs.GOLDTYPE, price, 0, true, null, itemMap, null);

		// 特权次数不足
		if (vipEntity == null) {
			protocol.setCode(ErrorIds.VIP_PERMISSION_NOT_ENOUGH);
			return;
		}

		// ////
		// // 元宝消耗记录
		// ///
		AnalyseModule analyseModule = ModuleManager.getModule(ModuleNames.AnalyseModule, AnalyseModule.class);
		analyseModule.goldCostLog(playerID, price, 1, price, "vipPermission", "vip");

		protocol.setItemMap(itemMap);
		protocol.setVipEntity(vipEntity);
	}

	/**
	 * 获取特权购买价格
	 * 
	 * @param vipEntity
	 * @param actionID
	 * @return
	 */
	public int permissionPrice(VipEntity vipEntity, String actionID) {
		// 获取特权已购买次数
		int permissionNum = 0;
		if (vipEntity.getPermission().containsKey(actionID)) {
			permissionNum = vipEntity.getPermission().get(actionID);
		}
		VipPriceData priceData = getPriceByActionID(actionID);
		// 特权购买价格
		int price = priceData.getPrice() + priceData.getPricePlus() * permissionNum;
		return price;
	}

	/**
	 * 减少用户vip特权使用次数
	 * 
	 * @param playerID
	 * @param actionID
	 * @return
	 */
	public VipEntity reducePermissionValue(VipEntity vipEntity, String actionID) {
		Map<String, Integer> permission = vipEntity.getPermission();
		if (permission.containsKey(actionID)) {
			// 已购买次数
			int value = permission.get(actionID);
			VipData vipData = getVipDataByLevel(vipEntity.getVipLevel());
			Map<String, Integer> dataMap = vipData.getPermission();
			// 购买上限
			int dataValue = dataMap.get(actionID);
			if (value < dataValue) {
				permission.put(actionID, value + 1);
			} else {
				return null;
			}
		} else {
			vipEntity.getPermission().put(actionID, 1);
		}
		saveVipEntity(vipEntity);
		return vipEntity;
	}

	/**
	 * 根据用户id,行为id获取vip特权次数上限
	 * 
	 * @param playerID
	 * @param actionID
	 * @return
	 */
	public int getPermissionValue(String playerID, String actionID) {
		VipEntity vipEntity = initVipEntity(playerID);
		VipData vipData = getVipDataByLevel(vipEntity.getVipLevel());
		return vipData.getPermission().get(actionID);
	}

	// 根据行为id获取价格
	private VipPriceData getPriceByActionID(String actionID) {
		int actionIntID = Integer.parseInt(actionID);
		// 遍历获取vip等级特权
		Set<Entry<Integer, VipPriceData>> vipSet = vipPriceMap.entrySet();
		VipPriceData vipPrice = null;
		for (Entry<Integer, VipPriceData> entry : vipSet) {
			VipPriceData temp = entry.getValue();
			if (temp.getMethodID() == actionIntID) {
				vipPrice = temp;
				break;
			}
		}
		return vipPrice;
	}

	// 根据vip等级配置文件读取vip特权
	public VipData getVipDataByLevel(int vipLevel) {
		// 遍历获取vip等级特权
		Set<Entry<Integer, VipData>> vipSet = vipDataMap.entrySet();
		VipData vipData = null;
		for (Entry<Integer, VipData> entry : vipSet) {
			VipData temp = entry.getValue();
			if (temp.getLevel() == vipLevel) {
				vipData = temp;
				break;
			}
		}
		return vipData;
	}

	// 根据充值总数获取vip特权
	private VipData getVipDataByPayTotal(int buyTotal) {
		// 遍历获取vip等级特权
		Set<Entry<Integer, VipData>> vipSet = vipDataMap.entrySet();
		VipData vipData = null;
		for (Entry<Integer, VipData> entry : vipSet) {
			VipData temp = entry.getValue();
			if (temp.getPrice() > buyTotal) {
				break;
			}
			vipData = temp;
		}
		return vipData;
	}

	/**
	 * 根据用户id获取vip信息
	 * 
	 * @param playerID
	 * @return
	 */
	public VipEntity initVipEntity(String playerID) {
		// 根据用户id获取vip特权
		VipEntity vipEntity = getVipEntity(playerID);
		if (vipEntity == null) {
			vipEntity = new VipEntity();
			vipEntity.setVipLevel(-1);
			vipEntity.setPlayerID(playerID);
			vipEntity.setVipTime(Utilities.getDateTime());
		}
		// 获取充值总数
		int payTotal = playerPayTotal(playerID);
		VipData vipData = getVipDataByPayTotal(payTotal);
		// vip等级发生变化
		if (vipData.getLevel() != vipEntity.getVipLevel()) {
			vipEntity.setVipLevel(vipData.getLevel());
			saveVipEntity(vipEntity);

			VitatlyEntity vitatlyEntity = vitatlyModule.getVitatlyEntity(playerID);
			// 更新最大体力上限
			if (vitatlyEntity.getMaxVitatly() != vipData.getMaxVitatly()) {
				vitatlyEntity.setMaxVitatly(vipData.getMaxVitatly());
				vitatlyModule.saveVitatlyEntity(vitatlyEntity);
			}
			// 保存playerEntity vip等级信息
			PlayerEntity playerEntity = loginModule.getPlayerEntity(playerID);
			if (vipEntity.getVipLevel() != playerEntity.getVipLevel()) {
				playerEntity.setVipLevel(vipEntity.getVipLevel());
				loginModule.savePlayerEntity(playerEntity);
			}
			// 重置活动fb 可购买次数
			initDungeonEliteEntity(playerID, vipEntity.getVipLevel());
			initDungeonActiveEntity(playerID, vipEntity.getVipLevel());
			initTowerEntity(playerID, vipEntity.getVipLevel());
			initPetEntity(playerID, vipEntity.getVipLevel());
		}
		// 每天重置vip特权
		if (!vipEntity.isPermission()) {
// initDungeonEliteEntity(playerID, vipEntity.getVipLevel());
// initDungeonActiveEntity(playerID, vipEntity.getVipLevel());
// initTowerEntity(playerID, vipEntity.getVipLevel());
// initPetEntity(playerID, vipEntity.getVipLevel());
			vipEntity.initPermission();
			saveVipEntity(vipEntity);
		}
		return vipEntity;
	}

	// 初始化宠物最大栏数
	private void initPetEntity(String playerID, int vipLevel) {
		PetEntity petEntity = petModule.getEntity(playerID);
		VipData vipData = getVipDataByLevel(vipLevel);
		Map<String, Integer> permission = vipData.getPermission();
		int permissionValue = 0;
		if (permission.containsKey(VipConstants.PET_FIELD)) {
			permissionValue = permission.get(VipConstants.PET_FIELD);
		}
		petEntity.setMaxFieldNum(permissionValue);
		petModule.saveEntity(petEntity);
	}

	// 初始化精英副本最大购买次数
	private void initDungeonEliteEntity(String playerID, int vipLevel) {
		DungeonEliteEntity dungeonEliteEntity = dungeonModule.getDungeonEliteEntity(playerID);
		VipData vipData = getVipDataByLevel(vipLevel);
		Map<String, Integer> permission = vipData.getPermission();
		int permissionValue = 0;
		if (permission.containsKey(VipConstants.BUY_ELITE_DUNGEON_ACTIONID)) {
			permissionValue = permission.get(VipConstants.BUY_ELITE_DUNGEON_ACTIONID);
		}
		dungeonEliteEntity.setMaxPayNum(permissionValue);
		dungeonModule.saveEliteDungeonEntity(dungeonEliteEntity);
	}

	// 初始化活动活动副本最大购买次数
	private void initDungeonActiveEntity(String playerID, int vipLevel) {
		DungeonActiveEntity dungeonActiveEntity = dungeonModule.getDungeonActiveEntity(playerID);
		VipData vipData = getVipDataByLevel(vipLevel);
		Map<String, Integer> permission = vipData.getPermission();
		int permission10512 = 0;

		if (permission.containsKey(VipConstants.BUY_ACTIVE_DUNGEON_ACTIONID)) {
			permission10512 = permission.get(VipConstants.BUY_ACTIVE_DUNGEON_ACTIONID);
		}
		// 设置经验书马最大购买次数
		if (dungeonActiveEntity.getDungeonList().containsKey(VipConstants.BOOK_HORSE_ID)) {
			dungeonActiveEntity.getDungeonList().get(VipConstants.BOOK_HORSE_ID).setMaxPayNum(permission10512);
		}
		int permission10513 = 0;
		if (permission.containsKey(VipConstants.BUY_ACTIVE_TREE_MONEY_ACTIONID)) {
			permission10513 = permission.get(VipConstants.BUY_ACTIVE_TREE_MONEY_ACTIONID);
		}
		// 设置摇钱树最大购买次数
		if (dungeonActiveEntity.getDungeonList().containsKey(VipConstants.MONEY_TREE_ID)) {
			dungeonActiveEntity.getDungeonList().get(VipConstants.MONEY_TREE_ID).setMaxPayNum(permission10513);
		}
		dungeonModule.saveDungeonActiveEntity(dungeonActiveEntity);
	}

	// 初始化试炼副本最大重置次数,试炼心购买次数
	private void initTowerEntity(String playerID, int vipLevel) {
		TowerEntity towerEntity = towerModule.getEntity(playerID);
		VipData vipData = getVipDataByLevel(vipLevel);
		Map<String, Integer> permission = vipData.getPermission();
		int permissionValue = 0;
		// 重置次数购买上限
		if (permission.containsKey(VipConstants.BUY_TOWER_ACTIONID)) {
			permissionValue = permission.get(VipConstants.BUY_TOWER_ACTIONID);
		}
		towerEntity.setMaxPayResetNum(permissionValue);
		// 试练心数购买上限
		int permissionValue2 = 0;
		if (permission.containsKey(VipConstants.BUY_TOWER_NUM_ACTIONID)) {
			permissionValue2 = permission.get(VipConstants.BUY_TOWER_NUM_ACTIONID);
		}
		towerEntity.setMaxHeartBuyNum(permissionValue2);
		towerModule.saveTowerEntity(towerEntity);
	}

	private void saveVipEntity(VipEntity vipEntity) {
		vipDao.save(vipEntity);
	}

	private VipEntity getVipEntity(String playerID) {
		return vipDao.getVipEntity(playerID);
	}

	@Override
	public void init() {
		payModule = ModuleManager.getModule(ModuleNames.PayModule, PayModule.class);
		rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
		loginModule = ModuleManager.getModule(ModuleNames.LoginModule, LoginModule.class);
		dungeonModule = ModuleManager.getModule(ModuleNames.DungeonModule, DungeonModule.class);
		towerModule = ModuleManager.getModule(ModuleNames.TowerModule, TowerModule.class);
		petModule = ModuleManager.getModule(ModuleNames.PetModule, PetModule.class);
		vitatlyModule = ModuleManager.getModule(ModuleNames.VitatlyModule, VitatlyModule.class);
		initVipData();
		initVipPriceData();
	}

	private void initVipPriceData() {
		List<VipPriceData> dataList = TemplateManager.getTemplateList(VipPriceData.class);
		for (VipPriceData vipData : dataList) {
			vipPriceMap.put(vipData.getPid(), vipData);
		}
	}

	private void initVipData() {
		List<VipData> dataList = TemplateManager.getTemplateList(VipData.class);
		for (VipData vipData : dataList) {
			vipDataMap.put(vipData.getPid(), vipData);
		}
	}
}
