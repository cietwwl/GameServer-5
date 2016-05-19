package com.mi.game.module.bag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.TemplateManager;
import com.mi.core.engine.annotation.Module;
import com.mi.game.defines.ActionType;
import com.mi.game.defines.ErrorIds;
import com.mi.game.defines.KindIDs;
import com.mi.game.defines.ModuleNames;
import com.mi.game.defines.SysConstants;
import com.mi.game.defines.UseType;
import com.mi.game.module.analyse.AnalyseModule;
import com.mi.game.module.bag.dao.BagEntityDAO;
import com.mi.game.module.bag.data.BagData;
import com.mi.game.module.bag.pojo.BagEntity;
import com.mi.game.module.bag.pojo.BagItem;
import com.mi.game.module.bag.protocol.BagProtocol;
import com.mi.game.module.base.BaseModule;
import com.mi.game.module.dayTask.DayTaskModule;
import com.mi.game.module.drop.DropModule;
import com.mi.game.module.mainTask.MainTaskModule;
import com.mi.game.module.manual.ManualModule;
import com.mi.game.module.manual.protocol.ManualAddExp;
import com.mi.game.module.pet.PetModule;
import com.mi.game.module.pet.pojo.Pet;
import com.mi.game.module.reward.RewardModule;
import com.mi.game.module.reward.data.GoodsBean;
import com.mi.game.util.Utilities;

/**
 * @author 刘凯旋 背包模块 2014年6月10日 上午11:16:26
 */
@Module(name = ModuleNames.BagModule, clazz = BagModule.class)
public class BagModule extends BaseModule {

	private BagEntityDAO bagEntityDAO = BagEntityDAO.getInstance();

	/**
	 * 初始化背包实体
	 * 
	 * @param String
	 *            playerID
	 * 
	 * */
	public BagEntity initBagEntity(String playerID) {
		BagEntity entity = new BagEntity();
		Map<Integer, BagItem> bagList = this.initBagItem();
		entity.setBagList(bagList);
		entity.setKey(playerID);
		entity.setMaxBagSellNum(SysConstants.bagInitNum);
		return entity;
	}

	/***
	 * 初始化背包的道具
	 * */
	private Map<Integer, BagItem> initBagItem() {
		// List<BagData> bagList =
		// TemplateManager.getTemplateList(BagData.class);
		Map<Integer, BagItem> bagMap = new HashMap<Integer, BagItem>();
		BagItem bagItem = new BagItem();
		bagItem.setTemplateID(10174);
		bagItem.setNum(100);
		bagMap.put(10174, bagItem);
		// for(BagData data : bagList){
		// int templateID = data.getPid();
		// BagItem bagItem = new BagItem();
		// bagItem.setTemplateID(templateID);
		// bagItem.setNum(99999);
		// bagMap.put(templateID, bagItem);
		// }
		return bagMap;
	}

	/**
	 * 保存背包实体
	 * 
	 * @param bagEntity
	 *            entity
	 * */

	public void saveBagEntity(BagEntity entity) {
		bagEntityDAO.save(entity);
	}

	/**
	 * 获取背包实体
	 * 
	 * @param String
	 *            playerID
	 * */
	public BagEntity getBagEntity(String playerID) {
		BagEntity bagEntity = bagEntityDAO.getEntity(playerID);
		if (bagEntity == null) {
			logger.error("背包实体为空");
			throw new IllegalArgumentException(ErrorIds.NoEntity + "");
		}
		return bagEntity;
	}

	public BagEntity getBagEntity(String playerID, IOMessage ioMessage) {
		BagEntity entity = null;

		if (ioMessage != null) {
			entity = (BagEntity) ioMessage.getInputParse().get(BagEntity.class.getName());

			if (entity == null) {
				entity = bagEntityDAO.getEntity(playerID);
				ioMessage.getInputParse().put(BagEntity.class.getName(), entity);
			}
		} else {
			entity = bagEntityDAO.getEntity(playerID);
		}
		return entity;
	}

	/**
	 * 添加背包道具
	 * */
	public int addItem(String playerID, int templateID, int num) {
		BagEntity bagEntity = this.getBagEntity(playerID);
		Map<Integer, BagItem> bagMap = bagEntity.getBagList();
// int code = 0;
// code = this.checkBag(bagEntity.getMaxBagSellNum(), bagMap);
// if (code != 0) {
// return code;
// }
		BagItem bagItem = null;
		if (bagMap.containsKey(templateID)) {
			bagItem = bagMap.get(templateID);
			bagItem.setNum(bagItem.getNum() + num);
		} else {
			bagItem = new BagItem();
			bagItem.setTemplateID(templateID);
			bagItem.setNum(num);
			bagMap.put(templateID, bagItem);
		}
		this.saveBagEntity(bagEntity);
		return 0;
	}

	/**
	 * 添加背包道具
	 * */
	public BagItem addItem(String playerID, int templateID, int num, boolean isSave, IOMessage ioMessage) {
		BagEntity bagEntity = this.getBagEntity(playerID, ioMessage);
		Map<Integer, BagItem> bagMap = bagEntity.getBagList();
		// int code = 0;
		// code = this.checkBag(bagEntity.getMaxBagSellNum(), bagMap);
		// if(code != 0){
		// throw new IllegalArgumentException(ErrorIds.ItemNotEnough+ "");
		// }
		BagItem bagItem = null;
		if (bagMap.containsKey(templateID)) {
			bagItem = bagMap.get(templateID);
			bagItem.setNum(bagItem.getNum() + num);
		} else {
			bagItem = new BagItem();
			bagItem.setTemplateID(templateID);
			bagItem.setNum(num);
			bagMap.put(templateID, bagItem);
		}
		if (isSave) {
			this.saveBagEntity(bagEntity);
		}
		return bagItem;
	}

	/**
	 * 检查背包是否可以使用
	 * */
	private int checkBag(int maxNum, Map<Integer, BagItem> bagMap) {
		int nowNum = bagMap.size();
		if (nowNum >= maxNum) {
			logger.error("背包已满");
			return ErrorIds.BagFull;
		}
		return 0;
	}

	/**
	 * 扩充背包格子数
	 * 
	 * @param playerID
	 *            String
	 * */
	public void expandBagNum(String playerID, BagProtocol bagProtocol) {
		BagEntity bagEntity = this.getBagEntity(playerID);
		int nowSellNum = bagEntity.getMaxBagSellNum();
		int n = (nowSellNum - SysConstants.bagInitNum) / SysConstants.bagSellAddNum + 1;
		int price = n * 25;
		int maxNum = nowSellNum + SysConstants.bagSellAddNum;
		bagEntity.setMaxBagSellNum(maxNum);
		RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
		Map<String, Object> itemMap = new HashMap<String, Object>();
		rewardModule.useGoods(playerID, KindIDs.GOLDTYPE, price, 0, true, null, itemMap, null);
		bagProtocol.setItemMap(itemMap);
		bagProtocol.setMaxNum(maxNum);
		this.saveBagEntity(bagEntity);

		// ////
		// // 元宝消耗记录
		// ///
		AnalyseModule analyseModule = ModuleManager.getModule(ModuleNames.AnalyseModule, AnalyseModule.class);
		analyseModule.goldCostLog(playerID, price, n, 25, "expandBagNum", "bag");

	}

	/**
	 * 道具出售
	 * 
	 * */
	public void sellItem(String playerID, List<Object> sellList, BagProtocol protocol) {
		int price = 0;
		BagEntity bagEntity = this.getBagEntity(playerID);
		Map<Integer, BagItem> bagList = bagEntity.getBagList();
		for (Object temp : sellList) {
			int templateID = Integer.parseInt(temp.toString());
			BagItem bagItem = bagList.get(templateID);
			if (bagItem == null) {
				logger.error("未找到道具");
				protocol.setCode(ErrorIds.NotFoundItem);
				return;
			}
			int num = bagItem.getNum();
			if (num < 0) {
				throw new IllegalArgumentException(ErrorIds.ParamWrong + "");
			}
			BagData bagData = TemplateManager.getTemplateData(templateID, BagData.class);
			if (bagData == null) {
				logger.error("未找到道具");
				protocol.setCode(ErrorIds.NotFoundItem);
				return;
			}
			if (bagData.getSell() == 1) {
				price += bagData.getPrice() * num;
			} else {
				logger.error("该道具不可出售");
				protocol.setCode(ErrorIds.ItemDontSell);
				return;
			}
			bagList.remove(templateID);
		}
		this.saveBagEntity(bagEntity);
		Map<String, Object> itemMap = new HashMap<String, Object>();
		Map<String, GoodsBean> showMap = new HashMap<>();
		RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
		rewardModule.addGoods(playerID, KindIDs.SILVERTYPE, price, null, true, showMap, itemMap, null);
		protocol.setItemMap(itemMap);
		protocol.setBagList(bagList.values());
		protocol.setShowMap(showMap);
	}

	/**
	 * 扣除道具
	 * 
	 * @param playerID
	 *            String 玩家ID
	 * @param tempalteID
	 *            int 道具ID
	 * @param num
	 *            int 扣除数量
	 * @param boolean isSave 是否保存
	 * @param IOMessage
	 *            ioMessage 输入输出封装
	 * */
	public BagItem deductItem(String playerID, int templateID, int num, boolean isSave, IOMessage ioMessage) {
		BagEntity bagEntity = this.getBagEntity(playerID, ioMessage);

		BagItem bagItem = null;
		if (bagEntity != null) {
			Map<Integer, BagItem> bagList = bagEntity.getBagList();
			// Map<Integer,BagItem> bagList = this.initBagItem();
			if (bagList == null) {
				logger.error("背包为空");
				throw new IllegalArgumentException(ErrorIds.NotEnoughSilver + "");
			}
			bagItem = bagList.get(templateID);
			if (bagItem != null) {
				int nowNum = bagItem.getNum();
				if (nowNum < num || num < 0) {
					logger.error("有人恶意攻击道具使用,检查 #" + playerID + "# 用户情况!");
					throw new IllegalArgumentException(ErrorIds.ItemNotEnough + "");
				}
				bagItem.setNum(nowNum - num);
				if (bagItem.getNum() == 0) {
					bagList.remove(templateID);
				}
				if (isSave) {
					this.saveBagEntity(bagEntity);
				}
			} else {
				logger.error("没有该道具");
				throw new IllegalArgumentException(ErrorIds.NotFoundItem + "");

			}
		}
		return bagItem;
	}

	/**
	 * 使用物品
	 * 
	 * **/
	public BagItem useItem(String playerID, int templateID, int num, long targetID, boolean isSave, Map<String, GoodsBean> showMap, Map<String, Object> itemMap,
			BagData bagData, IOMessage ioMessage) {
		// 扣减数量
		BagItem bagItem = null;

		bagItem = this.deductItem(playerID, templateID, num, isSave, ioMessage);
		Map<Integer, Integer> delFunc = bagData.getDelFunc();
		if (delFunc != null) {
			for (Entry<Integer, Integer> entry : delFunc.entrySet()) {
				int id = entry.getKey();
				int delnum = entry.getValue() * num;
				BagItem extraDelBagItem = this.deductItem(playerID, id, delnum, isSave, ioMessage);
				this.addItemToItemMap(itemMap, extraDelBagItem);
			}
		}
		Map<Integer, Integer> useFunc = bagData.getUseFunc();
		for (Entry<Integer, Integer> entry : useFunc.entrySet()) {
			int type = entry.getKey();
			int value = entry.getValue();
			this.doUse(playerID, type, num, value, targetID, isSave, showMap, itemMap, ioMessage);
		}
		MainTaskModule mainTaskModule = ModuleManager.getModule(ModuleNames.MainTaskModule, MainTaskModule.class);
		mainTaskModule.updateTaskByActionType(playerID, ActionType.USEVITATLYITEM, templateID, ioMessage);
		return bagItem;
	}

	private void doUse(String playerID, int type, int num, int value, long targetID, boolean isSave, Map<String, GoodsBean> showMap, Map<String, Object> itemMap,
			IOMessage ioMessage) {
		if (type != 0) {
			RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
			switch (type) {
			case UseType.useDropBox:
				Map<Integer, GoodsBean> goodsList = new HashMap<Integer, GoodsBean>();
				for (int i = 0; i < num; i++) {
					int dropID = this.newPlayerOpenBox(value, ioMessage);
					GoodsBean goodsBean = DropModule.doDrop(dropID);
					int pid = goodsBean.getPid();
					GoodsBean saveBean = goodsList.get(pid);
					if (saveBean != null) {
						saveBean.setNum(saveBean.getNum() + goodsBean.getNum());
					} else {
						goodsList.put(pid, goodsBean);
					}
				}
				int code = rewardModule.addGoods(playerID, Utilities.getGoodList(goodsList), isSave, showMap, itemMap, ioMessage);
				if (code != 0) {
					throw new IllegalArgumentException(code + "");
				}
				break;
			case UseType.addEnergy:
				rewardModule.addGoods(playerID, KindIDs.ENERGY, num * value, null, isSave, null, itemMap, ioMessage);
				break;
			case UseType.addVitatly:
				rewardModule.addGoods(playerID, KindIDs.VITALITY, num * value, null, isSave, null, itemMap, ioMessage);
				break;
			case UseType.addSilver:
				rewardModule.addGoods(playerID, KindIDs.SILVERTYPE, num * value, null, isSave, null, itemMap, ioMessage);
				break;
			case UseType.addGold:
				rewardModule.addGoods(playerID, KindIDs.GOLDTYPE, num * value, null, isSave, null, itemMap, ioMessage);
				break;
			case UseType.gift:
				DayTaskModule dayTaskModule = ModuleManager.getModule(ModuleNames.DayTaskModule, DayTaskModule.class);
				dayTaskModule.addScore(playerID, ActionType.MANUALPRESENT, num);
				ManualModule module = ModuleManager.getModule(ModuleNames.ManualModule, ManualModule.class);
				ManualAddExp manualAddExp = module.addManualExp(playerID, num * value, targetID, itemMap, ioMessage);
				itemMap.put("manualAddExpInfo", manualAddExp);
				break;			
			}
		}
	}

	/**
	 * 新手开箱子判断
	 * */
	private int newPlayerOpenBox(int dropID, IOMessage ioMessage) {
		int newDropID = dropID;
		BagEntity bagEntity = (BagEntity) ioMessage.getInputParse(BagEntity.class.getName());
		if (SysConstants.copperBoxDropID == dropID) {
			int copperNum = bagEntity.getUseCopperNum();
			if (copperNum < SysConstants.newOpenBoxNum) {
				copperNum += 1;
				bagEntity.setUseCopperNum(copperNum);
				newDropID = SysConstants.newCopperBoxDropID;
			}
		} else if (SysConstants.silverBoxDropID == dropID) {
			int silverNum = bagEntity.getUseSilverBoxNum();
			if (silverNum < SysConstants.newOpenBoxNum) {
				silverNum += 1;
				bagEntity.setUseSilverBoxNum(silverNum);
				newDropID = SysConstants.newSilverBoxDropID;
			}
		} else if (SysConstants.goldBoxDropID == dropID) {
			int goldNum = bagEntity.getUseGoldBoxNum();
			if (goldNum < SysConstants.newOpenBoxNum) {
				goldNum += 1;
				bagEntity.setUseGoldBoxNum(goldNum);
				newDropID = SysConstants.newGoldBoxDropID;
			}
		}
		return newDropID;
	}

	@SuppressWarnings("unchecked")
	public void addItemToItemMap(Map<String, Object> itemMap, BagItem bagItem) {
		if (itemMap.containsKey("bagItem")) {
			boolean needAdd = true;
			List<BagItem> list = (List<BagItem>) itemMap.get("bagItem");
			for (BagItem item : list) {
				if (item.getTemplateID() == bagItem.getTemplateID()) {
					item.setNum(bagItem.getNum()); // 更新数量
					needAdd = false;
					break;
				}
			}
			if (needAdd) {
				list.add(bagItem);
			}
		} else {
			List<BagItem> list = new ArrayList<BagItem>();
			list.add(bagItem);
			itemMap.put("bagItem", list);
		}
	}

	public BagItem getBagItem(String playerID, int templateID, IOMessage ioMessage) {
		BagEntity bagEntity = this.getBagEntity(playerID, ioMessage);
		Map<Integer, BagItem> bagList = bagEntity.getBagList();
		BagItem bagItem = bagList.get(templateID);
		return bagItem;
	}
	
	public long getBagCount(){
		return bagEntityDAO.getBagCount();
	}
	
}
