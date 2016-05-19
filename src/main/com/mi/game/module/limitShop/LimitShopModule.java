package com.mi.game.module.limitShop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.TemplateManager;
import com.mi.core.engine.annotation.Module;
import com.mi.game.defines.ErrorIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.BaseModule;
import com.mi.game.module.limitShop.dao.LimitShopEntityDao;
import com.mi.game.module.limitShop.data.LimitShopData;
import com.mi.game.module.limitShop.pojo.LimitShopEntity;
import com.mi.game.module.limitShop.protocol.LimitShopProtocol;
import com.mi.game.module.reward.RewardModule;
import com.mi.game.module.reward.data.GoodsBean;
import com.mi.game.module.wallet.WalletModule;
import com.mi.game.module.wallet.pojo.WalletEntity;
import com.mi.game.util.Utilities;


@Module(name = ModuleNames.LimitShopModule, clazz = LimitShopModule.class)
public class LimitShopModule extends BaseModule {
	private static RewardModule rewardModule;
	private static WalletModule walletModule;
	private static LimitShopEntityDao limitShopEntityDao = LimitShopEntityDao
			.getInstance();
	private static Map<Integer, LimitShopData> limitShopMap = new HashMap<Integer, LimitShopData>();

	@Override
	public void init() {
		// TODO 初始化各种参数
		rewardModule = ModuleManager.getModule(ModuleNames.RewardModule,
				RewardModule.class);
		walletModule = ModuleManager.getModule(ModuleNames.WalletModule,
				WalletModule.class);
		initShopList();
	}
	
	/**
	 * 获取所售商品列表
	 * 
	 * @param protocol
	 */
	public void getShopGoodsList(String playerID,LimitShopProtocol protocol){
		List<LimitShopData> shopList = TemplateManager
				.getTemplateList(LimitShopData.class);
		if (shopList != null && shopList.size() > 0) {
			protocol.setShopList(shopList);
		}
		
		// 查询购买历史
		String today = Utilities.getDateTime("yyyyMMdd");
		List<Integer> buyList = this.getBuyHistoryDay(playerID, today);
		if (buyList != null && buyList.size() > 0) {
			protocol.setBuyList(buyList);
		} else {
			protocol.setBuyList(new ArrayList<Integer>());
		}
	}
	
	/**
	 * 购买物品
	 * 
	 * @param playerID
	 * @param pid
	 * @param num
	 */
	public void buyShop(String playerID, int pid, int num, IOMessage ioMessage) {
		LimitShopProtocol protocol = new LimitShopProtocol();
		if (num <= 0) {
			protocol.setCode(ErrorIds.ParamWrong);
			ioMessage.setProtocol(protocol);
			return;
		}
		// 1.获得购买商品信息
		LimitShopData shopData = limitShopMap.get(pid);
		if (shopData == null) {
			protocol.setCode(ErrorIds.ParamWrong);
			ioMessage.setProtocol(protocol);
			return;
		}
		String[] itemArr = shopData.getItemID().split("=");
		int itemID = Integer.valueOf(itemArr[0]);
		int itemNum = Integer.valueOf(itemArr[1]);

		String[] costArr = shopData.getPrice().split("="); // 获取消耗物品信息
		int costID = Integer.valueOf(costArr[0]);
		int costNum = Integer.valueOf(costArr[1]);

		// 2.判断今天是否有购买记录,没有可以购买
		String today = Utilities.getDateTime("yyyyMMdd");
		LimitShopEntity shopEntity = limitShopEntityDao.getShopHistory(
				playerID, itemID, today);
		if (shopEntity != null) {
			protocol.setCode(ErrorIds.MYSTERY_BUYAGAIN);
			ioMessage.setProtocol(protocol);
			return;
		}

		// 3.检查是否有足够的金币
		WalletEntity walletEntity = walletModule.getwalletEntity(playerID);
		if (walletEntity.getGold() < costNum * num) {
			protocol.setCode(ErrorIds.NotEnoughGold);
			ioMessage.setProtocol(protocol);
			return;
		}
		// 4.购买物品
		Map<String, Object> itemMap = new HashMap<String, Object>();
		List<GoodsBean> goodsList = new ArrayList<GoodsBean>();
		GoodsBean addGoods = new GoodsBean();
		addGoods.setPid(itemID);
		addGoods.setNum(itemNum * num);

		GoodsBean useGoods = new GoodsBean();
		useGoods.setPid(costID);
		useGoods.setNum(costNum * num);
		goodsList.add(useGoods);

		List<GoodsBean> showMap = new ArrayList<GoodsBean>();
		showMap.add(addGoods);
		// 5.扣除金币
		rewardModule.useGoods(playerID, goodsList, 0L, true, null, itemMap,
				ioMessage);
		rewardModule.addGoods(playerID, itemID, itemNum * num, null, true,
				null, itemMap, ioMessage);
		protocol.setItemMap(itemMap);
		protocol.setShowMap(showMap);
		// TODO 添加showMap到protocol

		// 6.保存购买记录
		shopEntity = new LimitShopEntity();
		shopEntity.setBuyNum(itemNum * num);
		shopEntity.setDay(today);
		shopEntity.setGoodsID(itemID);
		shopEntity.setPlayerID(playerID);
		shopEntity.setPrice(costNum * num);
		shopEntity.setPid(pid);
		limitShopEntityDao.save(shopEntity);

		// 将新的购买记录传给前端
		List<Integer> idList = this.getBuyHistoryDay(playerID, today);
		if (idList != null && idList.size() > 0) {
			protocol.setBuyList(idList);
		}
		ioMessage.setProtocol(protocol);
	}
	
	/**
	 * 查询已购商品pid列表
	 * 
	 * @param playerID
	 * @param day
	 *            格式：yyyyMMdd
	 * @param protocol
	 */
	public void getBuyIdList(String playerID, String day,
			LimitShopProtocol protocol) {
		List<Integer> list = this.getBuyHistoryDay(playerID, day);
		if (list != null && list.size() > 0) {
			protocol.setBuyList(list);
		}
	}

	/**
	 * 根据日期得到当天已购商品模版ID列表
	 * 
	 * @param playerID
	 *            玩家ID
	 * @param day
	 *            日期：yyyyMMdd
	 */
	public List<Integer> getBuyHistoryDay(String playerID, String day) {
		return limitShopEntityDao.getBuyHistoryByDay(playerID, day);
	}
	
	/**
	 * 初始化限时抢购商品列表
	 */
	private void initShopList() {
		List<LimitShopData> dataList = TemplateManager
				.getTemplateList(LimitShopData.class);
		for (LimitShopData data : dataList) {
			limitShopMap.put(data.getPid(), data);
		}
	}
}
