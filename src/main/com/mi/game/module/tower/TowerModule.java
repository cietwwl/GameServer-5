package com.mi.game.module.tower;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.TemplateManager;
import com.mi.core.engine.annotation.Module;
import com.mi.core.util.DateTimeUtil;
import com.mi.game.defines.ActionType;
import com.mi.game.defines.ErrorIds;
import com.mi.game.defines.KindIDs;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.achievement.AchievementModule;
import com.mi.game.module.analyse.AnalyseModule;
import com.mi.game.module.base.BaseModule;
import com.mi.game.module.dayTask.DayTaskModule;
import com.mi.game.module.drop.DropModule;
import com.mi.game.module.dungeon.DungeonModule;
import com.mi.game.module.login.LoginModule;
import com.mi.game.module.login.pojo.PlayerEntity;
import com.mi.game.module.login.pojo.PlayerStatusEntity;
import com.mi.game.module.mainTask.MainTaskModule;
import com.mi.game.module.reward.RewardModule;
import com.mi.game.module.reward.data.GoodsBean;
import com.mi.game.module.tower.dao.TowerEntityDAO;
import com.mi.game.module.tower.data.HideTowerData;
import com.mi.game.module.tower.data.TowerData;
import com.mi.game.module.tower.pojo.HideInfo;
import com.mi.game.module.tower.pojo.TowerEntity;
import com.mi.game.module.tower.protocol.TowerProtocol;
import com.mi.game.module.tower.protocol.TowerTopInfo;
import com.mi.game.util.Utilities;

@Module(name = ModuleNames.TowerModule, clazz = TowerModule.class)
public class TowerModule extends BaseModule {
	private static final int initHeartNum = 2;
	private static final int maxLevel = 60;
	private static final int payResetNum = 1;
	private static final int freeResetNum = 2;
	public static final int initLevel = 10481001;
	private static final int maxLevelID = 10481060;
	private static final int resetGold = 200;
	private TowerEntityDAO towerEntityDAO = TowerEntityDAO.getInstance();
	private static final long clearTime = 30 * DateTimeUtil.ONE_SECOND_TIME_MS;
	public static final int towerRewardType = 10536;

	/**
	 * 初始化试练塔实体
	 * */
	public TowerEntity initEntity(String playerID) {
		TowerEntity entity = new TowerEntity();
		entity.setKey(playerID);
		entity.setHeartNum(initHeartNum);
		entity.setMaxLevel(maxLevel);
		entity.setFreeResetNum(freeResetNum);
		entity.setMaxPayResetNum(payResetNum);
		entity.setNowLevel(initLevel);
		return entity;
	}

	/**
	 * 获取试练塔实体
	 * */
	public TowerEntity getEntity(String playerID) {
		TowerEntity entity = towerEntityDAO.getEntity(playerID);
		if (entity == null) {
			throw new IllegalArgumentException(ErrorIds.TowerEntityIsNull + "");
		}
		return entity;
	}

	/**
	 * 获取试练塔实体
	 * */
	public TowerEntity getEntity(String playerID, IOMessage ioMessage) {
		TowerEntity entity = null;
		if (ioMessage != null) {
			entity = (TowerEntity) ioMessage.getInputParse().get(TowerEntity.class.getName());
			if (entity == null) {
				entity = this.getEntity(playerID);
				ioMessage.getInputParse().put(TowerEntity.class.getName(), entity);
			}
		} else {
			entity = this.getEntity(playerID);
		}
		return entity;
	}

	/**
	 * 保存试练塔实体
	 * */
	public void saveTowerEntity(TowerEntity entity) {
		towerEntityDAO.save(entity);
	}

	/**
	 * 获取试练塔前端实体
	 * */
	public TowerEntity getResponseEnity(String playerID, TowerProtocol protocol, IOMessage ioMessage) {
		TowerEntity entity = this.getUpdateHideEntity(playerID, ioMessage);
		long lastUpdateTime = entity.getLastUpdateTime();
		long nowTime = System.currentTimeMillis();
		
		boolean clear = entity.isClear();
		if (clear) {
			long overTime = entity.getOverTime();
			long diffTime = overTime - nowTime;
			if (diffTime <= 0) {
				this.getTowerReward(playerID, protocol, ioMessage);
			} else {
				int diffLevel = (int) Math.floor(((double) diffTime / clearTime));
				int passLevel = entity.getPassLevel();
				entity.setNowLevel(passLevel - diffLevel);
				// 最后添加层数时间更新
				entity.setLastAddTowerTime(System.currentTimeMillis());
			}
		}
		if (!DateTimeUtil.isSameDay(nowTime, lastUpdateTime)) {
			entity.setLastUpdateTime(nowTime);
			entity.setFreeResetNum(freeResetNum);
			entity.setMaxPayResetNum(payResetNum);
			entity.setPayResetNum(0);
			this.saveTowerEntity(entity);
		}
		List<HideInfo> hideList = entity.getHideList();
		for (Iterator<HideInfo> iter = hideList.iterator(); iter.hasNext();) {
			HideInfo hideInfo = iter.next();
			if (hideInfo.getOverTime() < nowTime) {
				iter.remove();
			}
		}
		protocol.setEntity(entity);
		return entity;
	}

	/**
	 * 获取更新隐藏关后的实体
	 * */
	public TowerEntity getUpdateHideEntity(String playerID,IOMessage ioMessage) {
		TowerEntity entity = this.getEntity(playerID,ioMessage);
		long nowTime = System.currentTimeMillis();
		List<HideInfo> hideList = entity.getHideList();
		for (Iterator<HideInfo> iter = hideList.iterator(); iter.hasNext();) {
			HideInfo hideInfo = iter.next();
			if (hideInfo.getOverTime() < nowTime) {
				iter.remove();
			}
		}
		return entity;
	}

	/**
	 * 挑战试练塔
	 * */
	public void challengeTower(String playerID, boolean win, TowerProtocol protocol) {
		TowerEntity entity = this.getEntity(playerID);
		int nowLevel = entity.getNowLevel();
		boolean lock = entity.isLock();
		boolean clear = entity.isClear();
		if (clear) {
			throw new IllegalArgumentException(ErrorIds.TowerIsClearing + "");
		}

		int heartNum = entity.getHeartNum();
		if (heartNum < 1) {
			throw new IllegalArgumentException(ErrorIds.TowerHeartNumNotEnough + "");
		}
		if (lock) {
			throw new IllegalArgumentException(ErrorIds.TowerGetMaxLevel + "");
		}
		if (win) {
			int passLevel = entity.getPassLevel();
			if (nowLevel > passLevel) {
				entity.setPassLevel(nowLevel);
			}
			TowerData nowTowerData = TemplateManager.getTemplateData(nowLevel, TowerData.class);
			List<GoodsBean> showList = nowTowerData.getGoodsList();
			Map<String, Object> itemMap = new HashMap<>();
			protocol.setShowMap(showList);
			List<GoodsBean> addList = new ArrayList<>();
			addList.addAll(showList);
			addList.add(new GoodsBean(KindIDs.SILVERTYPE, nowTowerData.getCoinReward()));
			addList.add(new GoodsBean(KindIDs.HEROSOUL, nowTowerData.getSoulReward()));
			RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
			rewardModule.addGoods(playerID, addList, false, null, itemMap, null);
			protocol.setItemMap(itemMap);
			int nextID = nowTowerData.getNextID();
			if (nextID != 0) {
				entity.setNowLevel(nextID);
			} else {
				entity.setNowLevel(nowTowerData.getPid());
				entity.setLock(true);
			}
			hidePercent(entity);
			AchievementModule acModule = ModuleManager.getModule(ModuleNames.AchievementModule, AchievementModule.class);
			acModule.refreshAchievement(playerID, ActionType.TOWERLEVEL, nowLevel);
			MainTaskModule mainTaskModule = ModuleManager.getModule(ModuleNames.MainTaskModule,MainTaskModule.class);
			mainTaskModule.updateTaskByActionType(playerID, ActionType.TOWERLEVEL, nowLevel, null);
			// 最后添加层数时间更新
			entity.setLastAddTowerTime(System.currentTimeMillis());
		} else {
			heartNum -= 1;
			entity.setHeartNum(heartNum);
		}
		protocol.setEntity(entity);

		this.saveTowerEntity(entity);
		DayTaskModule dayTaskModule = ModuleManager.getModule(ModuleNames.DayTaskModule, DayTaskModule.class);
		dayTaskModule.addScore(playerID, ActionType.TOWER, 1);
	}

	/**
	 * 重置试练塔
	 * */
	public void resetTower(String playerID, TowerProtocol protocol) {
		TowerEntity entity = this.getEntity(playerID);
		int resetNum = entity.getPayResetNum();
		int maxNum = entity.getMaxPayResetNum();
		int freeNum = entity.getFreeResetNum();
		boolean clear = entity.isClear();
		if (clear) {
			throw new IllegalArgumentException(ErrorIds.TowerIsClearing + "");
		}
		if (freeNum > 0) {
			freeNum -= 1;
			entity.setFreeResetNum(freeNum);
		} else {
			if (resetNum >= maxNum) {
				throw new IllegalArgumentException(ErrorIds.TowerResetNumNOtEnough + "");
			}
			resetNum += 1;
			Map<String, Object> itemMap = new HashMap<>();
			entity.setPayResetNum(resetNum);
			RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
			int code=rewardModule.useGoods(playerID, KindIDs.GOLDTYPE, resetGold, 0, true, null, itemMap, null);
			if(code!=0){
				protocol.setCode(code);
				return;
			}

			// ////
			// // 元宝消耗记录
			// ///
			AnalyseModule analyseModule = ModuleManager.getModule(ModuleNames.AnalyseModule, AnalyseModule.class);
			analyseModule.goldCostLog(playerID, resetGold, 1, resetGold, "resetTower", "tower");

			protocol.setItemMap(itemMap);
		}
		entity.setHeartNum(initHeartNum);
		entity.setNowLevel(initLevel);
		entity.setHeartBuyNum(0);
		entity.setLock(false);
		protocol.setEntity(entity);
		// 最后添加层数时间更新
		entity.setLastAddTowerTime(System.currentTimeMillis());
		this.saveTowerEntity(entity);
	}

	/**
	 * 扫荡试练塔
	 * */

	public void clearTower(String playerID, TowerProtocol protocol) {
		TowerEntity entity = this.getEntity(playerID);
		int heartNum = entity.getHeartNum();
		boolean lock = entity.isLock();
		int nowLevel = entity.getNowLevel();
		int passLevel = entity.getPassLevel();
		entity.setPassLevel(passLevel);
		boolean clear = entity.isClear();
		if (clear) {
			throw new IllegalAccessError(ErrorIds.TowerIsClearing + "");
		}
		if (heartNum < 1) {
			throw new IllegalArgumentException(ErrorIds.TowerHeartNumNotEnough + "");
		}
		if (lock) {
			throw new IllegalArgumentException(ErrorIds.TowerGetMaxLevel + "");
		}
		if (passLevel != initLevel) {
			if (nowLevel > passLevel || nowLevel == maxLevelID) {
				throw new IllegalArgumentException(ErrorIds.TowerNotClear + "");
			}
		}
		long clearAllTime = (passLevel - nowLevel + 1) * clearTime;
		long nowTime = System.currentTimeMillis();
		entity.setOverTime(nowTime + clearAllTime);
		entity.setClear(true);
		protocol.setEntity(entity);
		this.saveTowerEntity(entity);
	}

	/**
	 * 取消扫荡(领取奖励)
	 * */
	public void getTowerReward(String playerID, TowerProtocol protocol, IOMessage ioMessage) {
		TowerEntity entity = this.getEntity(playerID, ioMessage);
		Boolean clear = entity.isClear();
		if (clear) {
			long nowTime = System.currentTimeMillis();
			long overTime = entity.getOverTime();
			long diffTime = overTime - nowTime;
			int oldLevel = entity.getNowLevel();
			int nowLevel = 0;
			int passLevel = entity.getPassLevel();
			if (diffTime > 0) {
				int diffLevel = (int) Math.floor(((double) diffTime / clearTime));
				nowLevel = passLevel - diffLevel;
			} else {
				nowLevel = passLevel + 1;
			}
			if (nowLevel > oldLevel ) {
				Map<Integer, GoodsBean> map = new HashMap<>();
				for (int i = oldLevel; i < nowLevel; i++) {
					TowerData data = TemplateManager.getTemplateData(i, TowerData.class);
					List<GoodsBean> templist = data.getGoodsList();
					List<GoodsBean> addList = new ArrayList<>();
					addList.addAll(templist);
					addList.add(new GoodsBean(KindIDs.SILVERTYPE, data.getCoinReward()));
					addList.add(new GoodsBean(KindIDs.HEROSOUL, data.getSoulReward()));
					for (GoodsBean bean : addList) {
						int pid = bean.getPid();
						GoodsBean tempBean = map.get(pid);
						if (tempBean == null) {
							map.put(pid, new GoodsBean(bean.getPid(), bean.getNum()));
						} else {
							tempBean.setNum(bean.getNum() + tempBean.getNum());
							map.put(pid, tempBean);
						}
					}
				}
				List<GoodsBean> goodsList = Utilities.getGoodList(map);
				RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
				PlayerStatusEntity playerStatusEntity = rewardModule.addReward(playerID, goodsList, towerRewardType);
				protocol.setPlayerStatusEntity(playerStatusEntity);
				int passNum = nowLevel - oldLevel;
				for (int i = 0; i < passNum; i++) {
					hidePercent(entity);
				}
				DayTaskModule dayTaskModule = ModuleManager.getModule(ModuleNames.DayTaskModule, DayTaskModule.class);
				dayTaskModule.addScore(playerID, ActionType.TOWER, passNum);
			}
			entity.setOverTime(0);
			entity.setClear(false);
			if (nowLevel > maxLevelID) {
				nowLevel = maxLevelID;
				entity.setLock(true);
			}
			entity.setNowLevel(nowLevel);
			protocol.setEntity(entity);
			// 最后添加层数时间更新
			entity.setLastAddTowerTime(System.currentTimeMillis());
			this.saveTowerEntity(entity);
		}
	}

	/**
	 * 购买试炼塔心次数
	 * */
	public void buyHeartNum(String playerID, TowerProtocol protocol) {
		TowerEntity entity = this.getEntity(playerID);
		int heartNum = entity.getHeartNum();
		if (heartNum > 0) {
			throw new IllegalArgumentException(ErrorIds.HeartNotBuy + "");
		}
		int buyNum = entity.getHeartBuyNum();
		if (buyNum >= entity.getMaxHeartBuyNum()) {
			throw new IllegalArgumentException(ErrorIds.TowerNotBuyHeartNum + "");
		}
		entity.setHeartNum(1);
		entity.setHeartBuyNum(buyNum + 1);
		int gold = (buyNum + 1) * 10;
		Map<String, Object> itemMap = new HashMap<>();
		RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
		int code=rewardModule.useGoods(playerID, KindIDs.GOLDTYPE, gold, 0, true, null, itemMap, null);
		if(code!=0){
			protocol.setCode(code);
			return;
		}
		// ////
		// // 元宝消耗记录
		// ///
		AnalyseModule analyseModule = ModuleManager.getModule(ModuleNames.AnalyseModule, AnalyseModule.class);
		analyseModule.goldCostLog(playerID, gold, 1, gold, "buyHeartNum", "tower");

		// 去掉过期的隐藏关
		long nowTime = System.currentTimeMillis();
		List<HideInfo> hideList = entity.getHideList();
		for (Iterator<HideInfo> iter = hideList.iterator(); iter.hasNext();) {
			HideInfo hideInfo = iter.next();
			if (hideInfo.getOverTime() < nowTime) {
				iter.remove();
			}
		}

		protocol.setItemMap(itemMap);
		protocol.setEntity(entity);
		this.saveTowerEntity(entity);
	}

	/**
	 * 获取试练塔的排名列表
	 * */
	public void getTopList(TowerProtocol protocol) {
		List<TowerEntity> topEntityList = towerEntityDAO.getTopList();
		LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule, LoginModule.class);
		List<TowerTopInfo> topList = new ArrayList<>();
		for (TowerEntity entity : topEntityList) {
			String playerID = entity.getKey().toString();
			PlayerEntity playerEntity = loginModule.getPlayerEntity(playerID);
			TowerTopInfo topInfo = new TowerTopInfo();
			topInfo.setName(playerEntity.getNickName());
			topInfo.setLevel(playerEntity.getLevel());
			topInfo.setPhotoID(playerEntity.getPhotoID());
			topInfo.setPlayerID(playerID);
			topInfo.setVipLevel(playerEntity.getVipLevel());
			int passLevel = entity.getPassLevel();
			if (passLevel != 0) {
				topInfo.setTowerLevel(Integer.parseInt((entity.getPassLevel() + "").substring(5)));
			} else {
				topInfo.setTowerLevel(0);
			}
			topList.add(topInfo);
		}
		protocol.setTopList(topList);
	}

	/**
	 * 隐藏关出现几率
	 * */
	public void hidePercent(TowerEntity entity) {
		List<HideInfo> hideList = entity.getHideList();
			if (Utilities.getRandomInt(100) < 3) {
				GoodsBean goodsBean = DropModule.doDrop(103742);
				HideInfo hideInfo = new HideInfo(goodsBean.getPid(),
				entity.addHideCounter());
				hideList.add(hideInfo);
			}
	}

	/**
	 * 挑战隐藏关
	 * */
	public void attackHideTower(String playerID, long hideID, boolean win, TowerProtocol protocol,IOMessage ioMessage) {
		TowerEntity towerEntity = this.getUpdateHideEntity(playerID,ioMessage);
		List<HideInfo> list = towerEntity.getHideList();
		HideInfo hideInfo = null;
		for (HideInfo temp : list) {
			if (temp.getHideID() == hideID) {
				hideInfo = temp;
				break;
			}
		}
		if (hideInfo == null) {
			throw new IllegalArgumentException(ErrorIds.HideLevelNoExist + "");
		}
		Map<String, Object> itemMap = new HashMap<>();
		if (win) {
			RewardModule rewardModule = ModuleManager.getModule(
					ModuleNames.RewardModule, RewardModule.class);
			rewardModule.useGoods(playerID, KindIDs.VITALITY, 5, 0, true, null,
					itemMap, null);
			list.remove(hideInfo);
			int templateID = hideInfo.getTemplateID();
			HideTowerData data = TemplateManager.getTemplateData(templateID, HideTowerData.class);
			Map<Integer, Integer> dropInfo = data.getDropList();
			DungeonModule dungeonModule = ModuleManager.getModule(ModuleNames.DungeonModule, DungeonModule.class);
			Map<Integer, GoodsBean> dropList = new HashMap<Integer, GoodsBean>();
			Map<Integer, GoodsBean> showMap = new HashMap<Integer, GoodsBean>();
			dungeonModule.doDrop(dropInfo, dropList, showMap);
			rewardModule.addGoods(playerID, Utilities.getGoodList(dropList), true, null, itemMap, null);
			protocol.setShowMap(Utilities.getGoodList(showMap));
			this.saveTowerEntity(towerEntity);
		}
		protocol.setItemMap(itemMap);
	}

	/**
	 * 立即完成通天塔
	 * */
	public void quicklyCompleteTower(String playerID, TowerProtocol protocol, IOMessage ioMessage) {
		TowerEntity towerEntity = this.getEntity(playerID);
		Boolean clear = towerEntity.isClear();
		int gold = 0;
		int passLevel = towerEntity.getPassLevel();
		int oldLevel = towerEntity.getNowLevel();
		if (clear) {
			long nowTime = System.currentTimeMillis();
			long overTime = towerEntity.getOverTime();
			long diffTime = overTime - nowTime;
			int diffLevel = (int) Math.floor(((double) diffTime / clearTime));
			if (diffLevel > 0) {
				gold = diffLevel;
			} else {
				this.getTowerReward(playerID, protocol, ioMessage);
				return;
			}
		} else {
			if (oldLevel > passLevel || oldLevel >= maxLevelID) {
				throw new IllegalArgumentException(ErrorIds.TowerNoQuickComplete + "");
			}
			gold = passLevel - oldLevel;
		}
		gold += 1;
		Map<String, Object> itemMap = new HashMap<>();
		RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
		int code = rewardModule.useGoods(playerID, KindIDs.GOLDTYPE, gold, 0, true, null, itemMap, null);
		if (code != 0) {
			protocol.setCode(code);
			return;
		}

		// ////
		// // 元宝消耗记录
		// ///
		AnalyseModule analyseModule = ModuleManager.getModule(ModuleNames.AnalyseModule, AnalyseModule.class);
		analyseModule.goldCostLog(playerID, gold, 1, gold, "quicklyCompleteTower", "tower");

		Map<Integer, GoodsBean> map = new HashMap<>();
		for (int i = oldLevel; i <= passLevel; i++) {
			TowerData data = TemplateManager.getTemplateData(i, TowerData.class);
			List<GoodsBean> templist = data.getGoodsList();
			List<GoodsBean> addList = new ArrayList<>();
			addList.addAll(templist);
			addList.add(new GoodsBean(KindIDs.SILVERTYPE, data.getCoinReward()));
			addList.add(new GoodsBean(KindIDs.HEROSOUL, data.getSoulReward()));
			for (GoodsBean bean : addList) {
				int pid = bean.getPid();
				GoodsBean tempBean = map.get(pid);
				if (tempBean == null) {
					map.put(pid, new GoodsBean(bean.getPid(), bean.getNum()));
				} else {
					tempBean.setNum(bean.getNum() + tempBean.getNum());
					map.put(pid, tempBean);
				}
			}
		}
		List<GoodsBean> goodsList = Utilities.getGoodList(map);
		PlayerStatusEntity playerStatusEntity = rewardModule.addReward(playerID, goodsList, towerRewardType);
		protocol.setPlayerStatusEntity(playerStatusEntity);
		for (int i = 0; i < gold; i++) {
			hidePercent(towerEntity);
		}
		DayTaskModule dayTaskModule = ModuleManager.getModule(ModuleNames.DayTaskModule, DayTaskModule.class);
		dayTaskModule.addScore(playerID, ActionType.TOWER, gold);
		towerEntity.setOverTime(0);
		towerEntity.setClear(false);
		if (passLevel + 1 > maxLevelID) {
			towerEntity.setNowLevel(passLevel);
			towerEntity.setLock(true);
		} else {
			towerEntity.setNowLevel(passLevel + 1);
		}
		protocol.setEntity(towerEntity);
		protocol.setItemMap(itemMap);
		// 最后添加层数时间更新
		towerEntity.setLastAddTowerTime(System.currentTimeMillis());
		this.saveTowerEntity(towerEntity);
	}
}
