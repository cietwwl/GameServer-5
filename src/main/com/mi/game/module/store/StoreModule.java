package com.mi.game.module.store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.TemplateManager;
import com.mi.core.engine.annotation.Module;
import com.mi.game.defines.ActionType;
import com.mi.game.defines.ErrorIds;
import com.mi.game.defines.KindIDs;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.analyse.AnalyseModule;
import com.mi.game.module.base.BaseModule;
import com.mi.game.module.mainTask.MainTaskModule;
import com.mi.game.module.reward.RewardModule;
import com.mi.game.module.reward.data.GoodsBean;
import com.mi.game.module.store.dao.StoreEntityDao;
import com.mi.game.module.store.data.StoreData;
import com.mi.game.module.store.data.StoreVipGiftData;
import com.mi.game.module.store.define.StoreConstants;
import com.mi.game.module.store.pojo.StoreEntity;
import com.mi.game.module.store.protocol.StoreProtocol;
import com.mi.game.module.vip.VipModule;
import com.mi.game.module.vip.pojo.VipEntity;
import com.mi.game.util.Utilities;

@Module(name = ModuleNames.StoreModule, clazz = StoreModule.class)
public class StoreModule extends BaseModule {
	private static VipModule vipModule;
	private static RewardModule rewardModule;
	private static AnalyseModule analyseModule;
	private static StoreEntityDao storeDao = StoreEntityDao.getInstance();
	// 商店物品配置
	private static Map<Integer, StoreData> storeDataMap = new HashMap<Integer, StoreData>();
	// vip等级礼包配置
	private static Map<Integer, StoreVipGiftData> storeVipGiftDataMap = new HashMap<Integer, StoreVipGiftData>();

	/**
	 * 获取商城已购买信息
	 * 
	 * @param playerID
	 * @param protocol
	 */
	public void storeInfo(String playerID, StoreProtocol protocol) {
		StoreEntity storeEntity = getStoreEntity(playerID);
		if (storeEntity != null) {
			protocol.setStoreEntity(storeEntity);
		}
	}

	/**
	 * 商城道具购买
	 * 
	 * @param playerID
	 * @param itemID
	 * @param itemID2
	 * @param protocol
	 */
	public void storeBuy(String playerID, String storeType, String itemID, String itemNum, StoreProtocol protocol) {

		if (storeType == null || storeType.isEmpty()) {
			protocol.setCode(ErrorIds.STORE_TYPE_NULL);
			return;
		}
		if (itemID == null || itemID.isEmpty()) {
			protocol.setCode(ErrorIds.STORE_ITEM_NOTFOUND);
			return;
		}
		Map<String, Object> itemMap = new HashMap<String, Object>();
		VipEntity vipEntity = vipModule.initVipEntity(playerID);
		StoreEntity storeEntity = initStoreEntity(playerID);
		int itemIntID = Integer.parseInt(itemID);
		int code = 0;
		switch (storeType) {
		case StoreConstants.STORE_TYPE_ITEM: {
			if (itemNum == null || itemNum.isEmpty()) {
				protocol.setCode(ErrorIds.STORE_ITEMNUM_NULL);
				return;
			}
			int itemIntNum = Integer.parseInt(itemNum);
			if (itemIntNum < 0 || itemIntNum > 1000) {
				logger.error("有人恶意攻击道具购买次数,检查 #" + playerID + "# 用户情况!");
				throw new IllegalArgumentException(ErrorIds.ParamWrong + "");
			}
			if (!storeEntity.isRefresh()) {
				// 清空购买列表
				storeEntity.getBuyItems().clear();
			}
			// 根据物品id,获取配置信息
			StoreData storeData = getStorDataByItemID(itemIntID);
			if (storeData == null) {
				protocol.setCode(ErrorIds.STORE_ITEM_NOTFOUND);
				return;
			}
			// 购买所需价格
			int buyGold = 0;
			if (storeData.getVipLimit() != null) {
				// 获取当前vip等级购买数量上线
				int buyMaxNum = storeData.getVipLimit()[vipEntity.getVipLevel()];
				// 获取已购买数量
				int buyNum = storeEntity.getBuyItemNum(itemID);
				// 可购买数量
				int hasNum = buyMaxNum - buyNum;
				if (hasNum < itemIntNum || hasNum < 0) {
					protocol.setCode(ErrorIds.STORE_BUY_NUM_ERROR);
					return;
				}
				// 购买多个计算价格
				for (int i = 0; i < itemIntNum; i++) {
					// 计算购买所需价格
					buyGold += storeData.getPrice() + storeData.getPricePlus() * (buyNum + i);
				}

				// 增加已购买数量
				storeEntity.addBuyItemNum(itemID, itemIntNum);
			} else {
				buyGold = storeData.getPrice() * itemIntNum;
			}
			// 减钱
			code = rewardModule.useGoods(playerID, storeData.getCurrencyType(), buyGold, 0, true, null, itemMap, null);
			if (code != 0) {
				protocol.setCode(code);
				return;
			}
			// 加物品
			code = rewardModule.addGoods(playerID, itemIntID, itemIntNum, null, true, null, itemMap, null);
			if (code != 0) {
				protocol.setCode(code);
				return;
			}
			saveStoreEntity(storeEntity);
			// ////
			// // 元宝消耗记录
			// ///
			analyseModule.goldCostLog(playerID, buyGold, itemIntNum, storeData.getPrice(), storeData.getPid() + "", "store");
			MainTaskModule mainTaskModule = ModuleManager.getModule(ModuleNames.MainTaskModule, MainTaskModule.class);
			mainTaskModule.updateTaskByActionType(playerID, ActionType.BUYVITATLYITEM, itemIntID, null);
			break;
		}
		case StoreConstants.STORE_TYPE_GIFT: {
			StoreVipGiftData storeVipGiftData = storeVipGiftDataMap.get(itemIntID);
			if (storeVipGiftData == null) {
				protocol.setCode(ErrorIds.STORE_ITEM_NOTFOUND);
				return;
			}
			if (vipEntity.getVipLevel() < storeVipGiftData.getVipLevel()) {
				protocol.setCode(ErrorIds.STORE_VIPLEVEL_ERROR);
				return;
			}
			if (storeEntity.isBuyVipGift(itemIntID)) {
				protocol.setCode(ErrorIds.STORE_BUY_AGAIN);
				return;
			}
			// 减钱
			code = rewardModule.useGoods(playerID, KindIDs.GOLDTYPE, storeVipGiftData.getPrice(), 0, true, null, itemMap, null);
			if (code != 0) {
				protocol.setCode(code);
				return;
			}
			List<GoodsBean> goodsList = new ArrayList<GoodsBean>();
			List<Integer> giftList = storeVipGiftData.getKeys(storeVipGiftData.getItemID());
			for (Integer giftID : giftList) {
				GoodsBean goods = new GoodsBean();
				goods.setPid(giftID);
				goods.setNum(storeVipGiftData.getItemValue(giftID));
				goodsList.add(goods);
			}
			// 加物品
			code = rewardModule.addGoods(playerID, goodsList, true, null, itemMap, null);
			if (code != 0) {
				protocol.setCode(code);
				return;
			}
			// 记录购买礼包
			storeEntity.addVipGift(itemIntID);
			saveStoreEntity(storeEntity);
			protocol.setShowMap(goodsList);

			// ////
			// // 元宝消耗记录
			// ///
			analyseModule.goldCostLog(playerID, storeVipGiftData.getPrice(), 1, storeVipGiftData.getPrice(), itemIntID + "", "store");
			MainTaskModule mainTaskModule = ModuleManager.getModule(ModuleNames.MainTaskModule, MainTaskModule.class);
			mainTaskModule.updateTaskByActionType(playerID, ActionType.BUYVIPITEM, itemIntID, null);
			break;
		}
		}
		protocol.setItemMap(itemMap);
		protocol.setStoreEntity(storeEntity);

	}

	private StoreData getStorDataByItemID(int itemID) {
		Set<Entry<Integer, StoreData>> set = storeDataMap.entrySet();
		StoreData storeData = null;
		for (Entry<Integer, StoreData> entry : set) {
			StoreData temp = entry.getValue();
			if (temp.getItemID() == itemID) {
				storeData = temp;
				break;
			}
		}
		return storeData;
	}

	@Override
	public void init() {
		vipModule = ModuleManager.getModule(ModuleNames.VipModule, VipModule.class);
		rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
		analyseModule = ModuleManager.getModule(ModuleNames.AnalyseModule, AnalyseModule.class);
		initStoreData();
		initStoreVipGiftData();
	}

	private void initStoreVipGiftData() {
		List<StoreVipGiftData> dataList = TemplateManager.getTemplateList(StoreVipGiftData.class);
		for (StoreVipGiftData data : dataList) {
			storeVipGiftDataMap.put(data.getPid(), data);
		}
	}

	private void initStoreData() {
		List<StoreData> dataList = TemplateManager.getTemplateList(StoreData.class);
		for (StoreData data : dataList) {
			storeDataMap.put(data.getPid(), data);
		}
	}

	public StoreEntity initStoreEntity(String playerID) {
		StoreEntity storeEntity = getStoreEntity(playerID);
		if (storeEntity == null) {
			storeEntity = new StoreEntity();
			storeEntity.setPlayerID(playerID);
			storeEntity.setShopTime(Utilities.getDateTime());
			saveStoreEntity(storeEntity);
		}
		return storeEntity;
	}

	public StoreEntity getStoreEntity(String playerID) {
		return storeDao.getStoreEntity(playerID);
	}

	public void saveStoreEntity(StoreEntity storeEntity) {
		storeDao.save(storeEntity);
	}

}
