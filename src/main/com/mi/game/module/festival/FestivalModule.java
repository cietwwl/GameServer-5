package com.mi.game.module.festival;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.TemplateManager;
import com.mi.core.engine.annotation.Module;
import com.mi.game.defines.ErrorIds;
import com.mi.game.defines.KindIDs;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.BaseModule;
import com.mi.game.module.drop.DropModule;
import com.mi.game.module.festival.dao.FoolDayEntityDao;
import com.mi.game.module.festival.dao.LaborDayNiceEntityDao;
import com.mi.game.module.festival.dao.LaborDayPayMoreEntityDao;
import com.mi.game.module.festival.dao.NewYearFirecrackerEntityDao;
import com.mi.game.module.festival.dao.NewYearPayMoreEntityDao;
import com.mi.game.module.festival.dao.ValentineEntityDao;
import com.mi.game.module.festival.data.FestivalActiveData;
import com.mi.game.module.festival.data.SpringChargeData;
import com.mi.game.module.festival.define.FestivalConstants;
import com.mi.game.module.festival.pojo.FestivalEntity;
import com.mi.game.module.festival.pojo.FoolDayEntity;
import com.mi.game.module.festival.pojo.LaborDayNiceEntity;
import com.mi.game.module.festival.pojo.LaborDayPayMoreEntity;
import com.mi.game.module.festival.pojo.NewYearFirecrackerEntity;
import com.mi.game.module.festival.pojo.NewYearPayMoreEntity;
import com.mi.game.module.festival.pojo.ValentineEntity;
import com.mi.game.module.festival.protocol.FestivalFirecrackerProtocol;
import com.mi.game.module.festival.protocol.FestivalFoolDayProtocol;
import com.mi.game.module.festival.protocol.FestivalLaborDayNiceProtocol;
import com.mi.game.module.festival.protocol.FestivalLaborDayPayMoreProtocol;
import com.mi.game.module.festival.protocol.FestivalPayMoreProtocol;
import com.mi.game.module.festival.protocol.FestivalProtocol;
import com.mi.game.module.festival.protocol.FestivalValentineProtocol;
import com.mi.game.module.festival.util.FestivalUtils;
import com.mi.game.module.reward.RewardModule;
import com.mi.game.module.reward.data.GoodsBean;
import com.mi.game.module.store.StoreModule;
import com.mi.game.util.Utilities;

@Module(name = ModuleNames.FestivalModule, clazz = FestivalModule.class)
public class FestivalModule extends BaseModule {

	private static RewardModule rewardModule;
	private static StoreModule storeModule;

	private static NewYearFirecrackerEntityDao newYearFirecrackerEntityDao = NewYearFirecrackerEntityDao.getInstance();
	private static NewYearPayMoreEntityDao newYearPayMoreEntityDao = NewYearPayMoreEntityDao.getInstance();
	private static ValentineEntityDao newYearValentineEntityDao = ValentineEntityDao.getInstance();
	private static FoolDayEntityDao foolDayEntityDao = FoolDayEntityDao.getInstance();
	private static LaborDayNiceEntityDao laborNiceEntityDao = LaborDayNiceEntityDao.getInstance();
	private static LaborDayPayMoreEntityDao laborPayMoreEntityDao = LaborDayPayMoreEntityDao.getInstance();

	/*
	 * 获取节日活动
	 */
	public void festival(String playerID, FestivalProtocol protocol) {
		int festivalID = nowFestivalActive();
		if (festivalID != 0) {
			// 返回当前节日活动id
			protocol.setFestivalID(festivalID + "");
			switch (festivalID) {
			case FestivalConstants.NEWYEAR:
				// 年兽
				FestivalEntity worldBossEntity = initFestivalActive(festivalID);
				// 放鞭炮
				NewYearFirecrackerEntity firecrackerEntity = initNewYearFirecrackerEntity(playerID);
				// 充值加码
				NewYearPayMoreEntity payMoreEntity = initNewYearPayMoreEntity(playerID);
				protocol.addEntity("1106001", worldBossEntity);
				protocol.addEntity("1106002", payMoreEntity);
				protocol.addEntity("1106003", firecrackerEntity);
				break;
			case FestivalConstants.LANTERN:
				protocol.addEntity("1106007", storeModule.initStoreEntity(playerID));
				break;
			case FestivalConstants.VALENTINE:
				protocol.addEntity("1106006", this.getNewYearValentineEntity(playerID));
				break;
			case FestivalConstants.FOOLDAY:
				protocol.addEntity("1106013", this.getFoolDayEntity(playerID));
				break;
			case FestivalConstants.LABORDAY:
				// 为五一点赞
				LaborDayNiceEntity laborNiceEntity = initLaborDayNiceEntity(playerID);
				// 劳动加码
				LaborDayPayMoreEntity laborPayMoreEntity = initLaborDayPayMoreEntity(playerID);
				protocol.addEntity("1106010", laborNiceEntity);
				protocol.addEntity("1106009", laborPayMoreEntity);
				break;
			case FestivalConstants.CHILDRENDAY:
				// 糖果大放送
				protocol.addEntity("1106014", storeModule.initStoreEntity(playerID));
				break;
			}
		}
	}

	/**
	 * 充值加码
	 * 
	 * @param playerID
	 * @param protocol
	 */
	public void payMore(String playerID, String moreType, FestivalPayMoreProtocol protocol) {
		int festivalID = nowFestivalActive();
		if (FestivalConstants.NEWYEAR != festivalID) {
			protocol.setCode(ErrorIds.EVENT_NOINTIME);
			return;
		}
		if (StringUtils.isEmpty(moreType)) {
			protocol.setCode(ErrorIds.PayMoreTypeNotNull);
			return;
		}
		NewYearPayMoreEntity payMoreEntity = initNewYearPayMoreEntity(playerID);

		int moreIntType = Integer.parseInt(moreType);

		if (payMoreEntity.getMoreList().contains(moreIntType)) {
			protocol.setCode(ErrorIds.PayMoreTypeHas);
			return;
		}
		// 配置读取所需充值元宝数量
		SpringChargeData springChargeData = springChargeMap.get(moreIntType);

		if (springChargeData == null) {
			protocol.setCode(ErrorIds.PayMoreTypeNotNull);
			return;
		}

		int payValue = springChargeData.getCharge();
		if (payMoreEntity.getPayTotal() < payValue) {
			protocol.setCode(ErrorIds.PayMoreTotalLess);
			return;
		}
		Map<String, Object> itemMap = new HashMap<String, Object>();

		int payGold = springChargeData.getGold();
		int code = rewardModule.addGoods(playerID, KindIDs.GOLDTYPE, payGold, null, true, null, itemMap, null);
		if (code != 0) {
			protocol.setCode(code);
			return;
		}
		payMoreEntity.addMoreType(moreIntType);
		saveNewYearPayMoreEntity(payMoreEntity);
		protocol.setItemMap(itemMap);
		protocol.setPayMoreEntity(payMoreEntity);
	}

	/**
	 * 劳动节-充值加码
	 * 
	 * @param playerID
	 * @param protocol
	 */
	public void laborDaypayMore(String playerID, String moreType, FestivalLaborDayPayMoreProtocol protocol) {
		int festivalID = nowFestivalActive();
		if (FestivalConstants.LABORDAY != festivalID) {
			protocol.setCode(ErrorIds.EVENT_NOINTIME);
			return;
		}
		if (StringUtils.isEmpty(moreType)) {
			protocol.setCode(ErrorIds.PayMoreTypeNotNull);
			return;
		}
		LaborDayPayMoreEntity payMoreEntity = initLaborDayPayMoreEntity(playerID);

		int moreIntType = Integer.parseInt(moreType);

		if (payMoreEntity.getMoreList().contains(moreIntType)) {
			protocol.setCode(ErrorIds.PayMoreTypeHas);
			return;
		}
		// 配置读取所需充值元宝数量
		SpringChargeData springChargeData = springChargeMap.get(moreIntType);

		if (springChargeData == null) {
			protocol.setCode(ErrorIds.PayMoreTypeNotNull);
			return;
		}

		int payValue = springChargeData.getCharge();
		if (payMoreEntity.getPayTotal() < payValue) {
			protocol.setCode(ErrorIds.PayMoreTotalLess);
			return;
		}
		Map<String, Object> itemMap = new HashMap<String, Object>();

		int payGold = springChargeData.getGold();
		int code = rewardModule.addGoods(playerID, KindIDs.GOLDTYPE, payGold, null, true, null, itemMap, null);
		if (code != 0) {
			protocol.setCode(code);
			return;
		}
		payMoreEntity.addMoreType(moreIntType);
		saveLaborDayPayMoreEntity(payMoreEntity);
		protocol.setItemMap(itemMap);
		protocol.setPayMoreEntity(payMoreEntity);
	}

	/**
	 *  为五一点赞
	 * 
	 * @param playerID
	 * @param fireType
	 * @param protocol
	 */
	public void laborDayNice(String playerID, String fireType, FestivalLaborDayNiceProtocol protocol, IOMessage ioMessage) {
		int festivalID = nowFestivalActive();
		if (FestivalConstants.LABORDAY != festivalID) {
			protocol.setCode(ErrorIds.EVENT_NOINTIME);
			return;
		}
		if (StringUtils.isEmpty(fireType)) {
			protocol.setCode(ErrorIds.FiftyOneTypeNotNull);
			return;
		}
		LaborDayNiceEntity entity = initLaborDayNiceEntity(playerID);
		int code;
		Map<String, Object> itemMap = new HashMap<String, Object>();
		List<GoodsBean> goodsList = new ArrayList<GoodsBean>();
		boolean hasGoods = false;
		switch (fireType) {
		case FestivalConstants.COPPER:
			if (entity.getMaxRed() >= 1) {
				if (entity.getFreeRed() >= 1) {
					entity.setFreeRed(entity.getFreeRed() - 1);
					entity.setMaxRed(entity.getMaxRed() - 1);
				} else {
					// 免费次数用完,扣除铜板
					code = rewardModule.useGoods(playerID, KindIDs.SILVERTYPE, FestivalConstants.COPPER_PRICE, 0, true, null, itemMap, ioMessage);
					if (code != 0) {
						protocol.setCode(code);
						return;
					}
					entity.setMaxRed(entity.getMaxRed() - 1);
				}
			} else {
				protocol.setCode(ErrorIds.FIFTY_ONE_REDNUM_SUFFICIENT);
				return;
			}
			// 劳动节铜板赞掉落：1037714
			goodsList.add(DropModule.doDrop(1037714));
			// 增加物品
			code = rewardModule.addGoods(playerID, goodsList, true, null, itemMap, ioMessage);
			if (code != 0) {
				protocol.setCode(code);
				return;
			}
			break;
		case FestivalConstants.COPPER10:
			if (entity.getMaxRed() >= 10) {
				code = rewardModule.useGoods(playerID, KindIDs.SILVERTYPE, FestivalConstants.COPPER_PRICE * 10, 0, true, null, itemMap, ioMessage);
				if (code != 0) {
					protocol.setCode(code);
					return;
				}
				entity.setMaxRed(entity.getMaxRed() - 10);
				for (int i = 0; i < 11; i++) {
					GoodsBean goodsBean = DropModule.doDrop(1037714);
					hasGoods = false;
					for (GoodsBean goods : goodsList) {
						if (goods.getPid() == goodsBean.getPid()) {
							goods.setNum(goods.getNum() + goodsBean.getNum());
							hasGoods = true;
							break;
						}
					}
					if (!hasGoods) {
						goodsList.add(goodsBean);
					}
				}
			} else {
				protocol.setCode(ErrorIds.FIFTY_ONE_REDNUM_SUFFICIENT_10);
				return;
			}
			code = rewardModule.addGoods(playerID, goodsList, true, null, itemMap, ioMessage);
			if (code != 0) {
				protocol.setCode(code);
				return;
			}
			break;
		case FestivalConstants.GOLD:
			if (entity.getMaxGold() >= 1) {
				if (entity.getFreeGold() >= 1) {
					entity.setFreeGold(entity.getFreeGold() - 1);
					entity.setMaxGold(entity.getMaxGold() - 1);
				} else {
					// 免费次数用完,扣除金币
					code = rewardModule.useGoods(playerID, KindIDs.GOLDTYPE, FestivalConstants.GOLD_PRICE, 0, true, null, itemMap, ioMessage);
					if (code != 0) {
						protocol.setCode(code);
						return;
					}
					entity.setMaxGold(entity.getMaxGold() - 1);

				}
			} else {
				protocol.setCode(ErrorIds.FIFTY_ONE_GOLDNUM_SUFFICIENT);
				return;
			}
			// 劳动节元宝赞掉落：1037715
			goodsList.add(DropModule.doDrop(1037715));
			// 增加物品
			code = rewardModule.addGoods(playerID, goodsList, true, null, itemMap, ioMessage);
			if (code != 0) {
				protocol.setCode(code);
				return;
			}
			break;
		case FestivalConstants.GOLD10:
			if (entity.getMaxGold() >= 10) {
				code = rewardModule.useGoods(playerID, KindIDs.GOLDTYPE, FestivalConstants.GOLD_PRICE * 10, 0, true, null, itemMap, ioMessage);
				if (code != 0) {
					protocol.setCode(code);
					return;
				}
				entity.setMaxGold(entity.getMaxGold() - 10);
				for (int i = 0; i < 11; i++) {
					GoodsBean goodsBean = DropModule.doDrop(1037715);
					hasGoods = false;
					for (GoodsBean goods : goodsList) {
						if (goods.getPid() == goodsBean.getPid()) {
							goods.setNum(goods.getNum() + goodsBean.getNum());
							hasGoods = true;
							break;
						}
					}
					if (!hasGoods) {
						goodsList.add(goodsBean);
					}
				}
			} else {
				protocol.setCode(ErrorIds.FIFTY_ONE_GOLDNUM_SUFFICIENT_10);
				return;
			}
			code = rewardModule.addGoods(playerID, goodsList, true, null, itemMap, ioMessage);
			if (code != 0) {
				protocol.setCode(code);
				return;
			}
			break;
		}
		saveLaborDayNiceEntity(entity);
		protocol.setNiceEntity(entity);
		protocol.setShowMap(goodsList);
		protocol.setItemMap(itemMap);
	}

	/**
	 * 放鞭炮
	 * 
	 * @param playerID
	 * @param fireType
	 * @param protocol
	 */
	public void firecracker(String playerID, String fireType, FestivalFirecrackerProtocol protocol, IOMessage ioMessage) {
		int festivalID = nowFestivalActive();
		if (FestivalConstants.NEWYEAR != festivalID) {
			protocol.setCode(ErrorIds.EVENT_NOINTIME);
			return;
		}
		if (StringUtils.isEmpty(fireType)) {
			protocol.setCode(ErrorIds.FirecrackerTypeNotNull);
			return;
		}
		NewYearFirecrackerEntity entity = initNewYearFirecrackerEntity(playerID);
		int code;
		Map<String, Object> itemMap = new HashMap<String, Object>();
		List<GoodsBean> goodsList = new ArrayList<GoodsBean>();
		boolean hasGoods = false;
		switch (fireType) {
		case FestivalConstants.COPPER:
			if (entity.getMaxRed() >= 1) {
				if (entity.getFreeRed() >= 1) {
					entity.setFreeRed(entity.getFreeRed() - 1);
					entity.setMaxRed(entity.getMaxRed() - 1);
				} else {
					// 免费次数用完,扣除铜板
					code = rewardModule.useGoods(playerID, KindIDs.SILVERTYPE, FestivalConstants.COPPER_PRICE, 0, true, null, itemMap, ioMessage);
					if (code != 0) {
						protocol.setCode(code);
						return;
					}
					entity.setMaxRed(entity.getMaxRed() - 1);
				}
			} else {
				protocol.setCode(ErrorIds.FIRECRACKER_REDNUM_SUFFICIENT);
				return;
			}
			// 红鞭炮掉落物品
			goodsList.add(DropModule.doDrop(1037701));
			// 增加物品
			code = rewardModule.addGoods(playerID, goodsList, true, null, itemMap, ioMessage);
			if (code != 0) {
				protocol.setCode(code);
				return;
			}
			break;
		case FestivalConstants.COPPER10:
			if (entity.getMaxRed() >= 10) {
				code = rewardModule.useGoods(playerID, KindIDs.SILVERTYPE, FestivalConstants.COPPER_PRICE * 10, 0, true, null, itemMap, ioMessage);
				if (code != 0) {
					protocol.setCode(code);
					return;
				}
				entity.setMaxRed(entity.getMaxRed() - 10);
				for (int i = 0; i < 11; i++) {
					GoodsBean goodsBean = DropModule.doDrop(1037701);
					hasGoods = false;
					for (GoodsBean goods : goodsList) {
						if (goods.getPid() == goodsBean.getPid()) {
							goods.setNum(goods.getNum() + goodsBean.getNum());
							hasGoods = true;
							break;
						}
					}
					if (!hasGoods) {
						goodsList.add(goodsBean);
					}
				}
			} else {
				protocol.setCode(ErrorIds.FIRECRACKER_REDNUM_SUFFICIENT_10);
				return;
			}
			code = rewardModule.addGoods(playerID, goodsList, true, null, itemMap, ioMessage);
			if (code != 0) {
				protocol.setCode(code);
				return;
			}
			break;
		case FestivalConstants.GOLD:
			if (entity.getMaxGold() >= 1) {
				if (entity.getFreeGold() >= 1) {
					entity.setFreeGold(entity.getFreeGold() - 1);
					entity.setMaxGold(entity.getMaxGold() - 1);
				} else {
					// 免费次数用完,扣除金币
					code = rewardModule.useGoods(playerID, KindIDs.GOLDTYPE, FestivalConstants.GOLD_PRICE, 0, true, null, itemMap, ioMessage);
					if (code != 0) {
						protocol.setCode(code);
						return;
					}
					entity.setMaxGold(entity.getMaxGold() - 1);

				}
			} else {
				protocol.setCode(ErrorIds.FIRECRACKER_GOLDNUM_SUFFICIENT);
				return;
			}
			// 金鞭炮掉落物品
			goodsList.add(DropModule.doDrop(1037702));
			// 增加物品
			code = rewardModule.addGoods(playerID, goodsList, true, null, itemMap, ioMessage);
			if (code != 0) {
				protocol.setCode(code);
				return;
			}
			break;
		case FestivalConstants.GOLD10:
			if (entity.getMaxGold() >= 10) {
				code = rewardModule.useGoods(playerID, KindIDs.GOLDTYPE, FestivalConstants.GOLD_PRICE * 10, 0, true, null, itemMap, ioMessage);
				if (code != 0) {
					protocol.setCode(code);
					return;
				}
				entity.setMaxGold(entity.getMaxGold() - 10);
				for (int i = 0; i < 11; i++) {
					GoodsBean goodsBean = DropModule.doDrop(1037702);
					hasGoods = false;
					for (GoodsBean goods : goodsList) {
						if (goods.getPid() == goodsBean.getPid()) {
							goods.setNum(goods.getNum() + goodsBean.getNum());
							hasGoods = true;
							break;
						}
					}
					if (!hasGoods) {
						goodsList.add(goodsBean);
					}
				}
			} else {
				protocol.setCode(ErrorIds.FIRECRACKER_GOLDNUM_SUFFICIENT_10);
				return;
			}
			code = rewardModule.addGoods(playerID, goodsList, true, null, itemMap, ioMessage);
			if (code != 0) {
				protocol.setCode(code);
				return;
			}
			break;
		}
		saveNewYearFirecrackerEntity(entity);
		protocol.setFirecrackerEntity(entity);
		protocol.setShowMap(goodsList);
		protocol.setItemMap(itemMap);
	}

	private NewYearPayMoreEntity initNewYearPayMoreEntity(String playerID) {
		NewYearPayMoreEntity entity = getNewYearPayMoreEntity(playerID);
		if (entity == null) {
			entity = new NewYearPayMoreEntity();
			entity.setPlayerID(playerID);
			saveNewYearPayMoreEntity(entity);
		}
		return entity;
	}

	private LaborDayPayMoreEntity initLaborDayPayMoreEntity(String playerID) {
		LaborDayPayMoreEntity entity = getLaborDayPayMoreEntity(playerID);
		if (entity == null) {
			entity = new LaborDayPayMoreEntity();
			entity.setPlayerID(playerID);
			saveLaborDayPayMoreEntity(entity);
		}
		return entity;
	}

	/***
	 * 兑换钻石宝箱
	 * */
	public void exchangeDiamond(String playerID, FestivalValentineProtocol protocol, IOMessage ioMessage) {
		if (!isInValentine()) {
			logger.error("不在情人节活动");
			throw new IllegalArgumentException(ErrorIds.EVENT_NOINTIME + "");
		}
		ValentineEntity valentineEntity = this.getNewYearValentineEntity(playerID);
		long goldNum = valentineEntity.getGoldNum();
		if (goldNum >= FestivalConstants.Valentine_Gold) {
			goldNum -= FestivalConstants.Valentine_Gold;
			valentineEntity.setGoldNum(goldNum);
			RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
			Map<String, Object> itemMap = new HashMap<>();
			List<GoodsBean> showMap = new ArrayList<>();
			rewardModule.addGoods(playerID, FestivalConstants.diamondID, FestivalConstants.diamondNum, 0, true, null, itemMap, ioMessage);
			showMap.add(new GoodsBean(FestivalConstants.diamondID, FestivalConstants.diamondNum));
			this.saveNewYearValentineEntity(valentineEntity);
			protocol.setItemMap(itemMap);
			protocol.setValentineEntity(valentineEntity);
			protocol.setShowMap(showMap);
		} else {
			logger.error("情人节可兑换金币不足");
			throw new IllegalArgumentException(ErrorIds.NotEnoughValentineGold + "");
		}
	}

	/**
	 * 判断是否在情人节活动
	 * */
	public boolean isInValentine() {
		int festivalID = nowFestivalActive();
		if (FestivalConstants.VALENTINE == festivalID) {
			return true;
		}
		return false;
	}

	/**
	 * 判断是否在愚人节活动
	 * */
	public boolean isInFoolDay() {
		int festivalID = nowFestivalActive();
		if (FestivalConstants.FOOLDAY == festivalID) {
			return true;
		}
		return false;
	}

	/** 增加情人节充值元宝 **/
	public void intefaceFestivaValentine(String playerID, int payValue) {
		int festivalID = nowFestivalActive();
		if (FestivalConstants.VALENTINE == festivalID) {
			ValentineEntity valentineEntity = this.getNewYearValentineEntity(playerID);
			long goldNum = valentineEntity.getGoldNum();
			goldNum += payValue;
			valentineEntity.setAllGold(valentineEntity.getAllGold() + payValue);
			valentineEntity.setGoldNum(goldNum);
			this.saveNewYearValentineEntity(valentineEntity);
		}
	}

	/**
	 * 增加情人节十连抽返回元宝
	 * */
	public void addValentineDrawGold(String playerID, int addGold) {
		if (isInValentine()) {
			ValentineEntity valentineEntity = this.getNewYearValentineEntity(playerID);
			long drawGold = valentineEntity.getDrawGold();
			drawGold += addGold;
			valentineEntity.setDrawGold(drawGold);
			this.saveNewYearValentineEntity(valentineEntity);
		}
	}

	/** 增加愚人节充值元宝 **/
	public void intefaceFestivalFoolDay(String playerID, int payValue) {
		int festivalID = nowFestivalActive();
		if (FestivalConstants.FOOLDAY == festivalID) {
			FoolDayEntity foolDayEntity = this.getFoolDayEntity(playerID);
			long goldNum = foolDayEntity.getGoldNum();
			goldNum += payValue;
			foolDayEntity.setAllGold(foolDayEntity.getAllGold() + payValue);
			foolDayEntity.setGoldNum(goldNum);
			this.saveFoolDayEntity(foolDayEntity);
		}
	}

	/**
	 * 增加愚人节十连抽返回元宝
	 * */
	public void addFoolDayDrawGold(String playerID, int addGold) {
		if (isInFoolDay()) {
			FoolDayEntity foolDayEntity = this.getFoolDayEntity(playerID);
			long drawGold = foolDayEntity.getDrawGold();
			drawGold += addGold;
			foolDayEntity.setDrawGold(drawGold);
			this.saveFoolDayEntity(foolDayEntity);
		}
	}

	/***
	 * 兑换愚人节整蛊魔盒
	 * */
	public void exchangeMagicBox(String playerID, FestivalFoolDayProtocol protocol, IOMessage ioMessage) {
		if (!isInFoolDay()) {
			logger.error("不在愚人节活动");
			throw new IllegalArgumentException(ErrorIds.EVENT_NOINTIME + "");
		}
		FoolDayEntity foolDayEntity = this.getFoolDayEntity(playerID);
		long goldNum = foolDayEntity.getGoldNum();
		if (goldNum >= FestivalConstants.FoolDay_Gold) {
			goldNum -= FestivalConstants.FoolDay_Gold;
			foolDayEntity.setGoldNum(goldNum);
			RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
			Map<String, Object> itemMap = new HashMap<>();
			List<GoodsBean> showMap = new ArrayList<>();
			rewardModule.addGoods(playerID, FestivalConstants.FoolDayMagicBoxID, FestivalConstants.FoolDayMagicBoxChangeNum, 0, true, null, itemMap, ioMessage);
			showMap.add(new GoodsBean(FestivalConstants.FoolDayMagicBoxID, FestivalConstants.FoolDayMagicBoxChangeNum));
			this.saveFoolDayEntity(foolDayEntity);
			protocol.setItemMap(itemMap);
			protocol.setFoolDayEntity(foolDayEntity);
			protocol.setShowMap(showMap);
		} else {
			logger.error("愚人节可兑换金币不足");
			throw new IllegalArgumentException(ErrorIds.NotEnoughValentineGold + "");
		}
	}

	/**
	 * 春节-充值加码活动数据收集接口
	 * 
	 * @param playerID
	 * @param payValue
	 */
	public void intefaceFestivalPayMore(String playerID, int payValue) {
		int festivalID = nowFestivalActive();
		if (FestivalConstants.NEWYEAR == festivalID) {
			NewYearPayMoreEntity payMoreEntity = initNewYearPayMoreEntity(playerID);
			payMoreEntity.addPayTotal(payValue);
			saveNewYearPayMoreEntity(payMoreEntity);
		}
	}

	/**
	 * 劳动节-充值加码活动数据收集接口
	 * 
	 * @param playerID
	 * @param payValue
	 */
	public void intefaceFestivalLaborDayPayMore(String playerID, int payValue) {
		int festivalID = nowFestivalActive();
		if (FestivalConstants.LABORDAY == festivalID) {
			LaborDayPayMoreEntity payMoreEntity = initLaborDayPayMoreEntity(playerID);
			payMoreEntity.addPayTotal(payValue);
			saveLaborDayPayMoreEntity(payMoreEntity);
		}
	}

	// 为无一点赞实体 初始化
	private LaborDayNiceEntity initLaborDayNiceEntity(String playerID) {
		LaborDayNiceEntity entity = getLaborDayNiceEntity(playerID);
		if (entity == null) {
			entity = new LaborDayNiceEntity();
			entity.setPlayerID(playerID);
			entity.setFreeGold(1);
			entity.setFreeRed(5);
			entity.setMaxGold(1000);
			entity.setMaxRed(1000);
			entity.setDateTime(Utilities.getDateTime());
			saveLaborDayNiceEntity(entity);
		}
		// 每日刷新次数
		if (!entity.isRefresh()) {
			entity.setFreeGold(1);
			entity.setFreeRed(5);
			saveLaborDayNiceEntity(entity);
		}
		return entity;
	}

	private NewYearFirecrackerEntity initNewYearFirecrackerEntity(String playerID) {
		NewYearFirecrackerEntity entity = getNewYearFirecrackerEntity(playerID);
		if (entity == null) {
			entity = new NewYearFirecrackerEntity();
			entity.setPlayerID(playerID);
			entity.setFreeGold(1);
			entity.setFreeRed(5);
			entity.setMaxGold(1000);
			entity.setMaxRed(1000);
			entity.setDateTime(Utilities.getDateTime());
			saveNewYearFirecrackerEntity(entity);
		}
		// 每日刷新次数
		if (!entity.isRefresh()) {
			entity.setFreeGold(1);
			entity.setFreeRed(5);
			entity.setMaxGold(1000);
			entity.setMaxRed(1000);
			saveNewYearFirecrackerEntity(entity);
		}
		return entity;
	}

	// 获取节日活动信息
	private FestivalEntity initFestivalActive(int festivalID) {
		FestivalActiveData festivalData = festivalMap.get(festivalID);
		FestivalEntity entity = new FestivalEntity();
		long startTime = FestivalUtils.strToDate(festivalData.getStartTime(), FestivalConstants.YMDHMS).getTime();
		long endTime = FestivalUtils.strToDate(festivalData.getEndTime(), FestivalConstants.YMDHMS).getTime();
		entity.setStartTime(startTime);
		entity.setEndTime(endTime);
		return entity;
	}

	public LaborDayPayMoreEntity getLaborDayPayMoreEntity(String playerID) {
		return laborPayMoreEntityDao.getNewYearPayMoreEntity(playerID);
	}

	public void saveLaborDayPayMoreEntity(LaborDayPayMoreEntity entity) {
		laborPayMoreEntityDao.save(entity);
	}

	public NewYearPayMoreEntity getNewYearPayMoreEntity(String playerID) {
		return newYearPayMoreEntityDao.getNewYearPayMoreEntity(playerID);
	}

	public void saveNewYearPayMoreEntity(NewYearPayMoreEntity entity) {
		newYearPayMoreEntityDao.save(entity);
	}

	public NewYearFirecrackerEntity getNewYearFirecrackerEntity(String playerID) {
		return newYearFirecrackerEntityDao.getNewYearFirecrackerEntity(playerID);
	}

	public void saveNewYearFirecrackerEntity(NewYearFirecrackerEntity entity) {
		newYearFirecrackerEntityDao.save(entity);
	}

	public LaborDayNiceEntity getLaborDayNiceEntity(String playerID) {
		return laborNiceEntityDao.getNewYearFirecrackerEntity(playerID);
	}

	public void saveLaborDayNiceEntity(LaborDayNiceEntity entity) {
		laborNiceEntityDao.save(entity);
	}

	public ValentineEntity getNewYearValentineEntity(String playerID) {
		ValentineEntity valentineEntity = newYearValentineEntityDao.getEntity(playerID);
		if (valentineEntity == null) {
			valentineEntity = new ValentineEntity();
			valentineEntity.setKey(playerID);
			this.saveNewYearValentineEntity(valentineEntity);
		}
		return valentineEntity;
	}

	public void saveNewYearValentineEntity(ValentineEntity valentineEntity) {
		newYearValentineEntityDao.save(valentineEntity);
	}

	public FoolDayEntity getFoolDayEntity(String playerID) {
		FoolDayEntity foolDayEntity = foolDayEntityDao.getEntity(playerID);
		if (foolDayEntity == null) {
			foolDayEntity = new FoolDayEntity();
			foolDayEntity.setKey(playerID);
			this.saveFoolDayEntity(foolDayEntity);
		}
		return foolDayEntity;
	}

	public void saveFoolDayEntity(FoolDayEntity foolDayEntity) {
		foolDayEntityDao.save(foolDayEntity);
	}

	// 节日活动时间配置
	private static Map<Integer, FestivalActiveData> festivalMap = new HashMap<Integer, FestivalActiveData>();
	// 充值加码配置
	private static Map<Integer, SpringChargeData> springChargeMap = new HashMap<Integer, SpringChargeData>();

	/**
	 * 获取当前节日活动id
	 */
	public int nowFestivalActive() {
		Set<Entry<Integer, FestivalActiveData>> set = festivalMap.entrySet();
		for (Entry<Integer, FestivalActiveData> entry : set) {
			if (FestivalUtils.isFestivalTime(entry.getValue(), FestivalConstants.YMDHMS)) {
				return entry.getKey();
			}
		}
		return 0;
	}

	@Override
	public void init() {
		rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
		storeModule = ModuleManager.getModule(ModuleNames.StoreModule, StoreModule.class);
		initFestivalMap();
		initSpringChargeMap();
	}

	private void initFestivalMap() {
		List<FestivalActiveData> dataList = TemplateManager.getTemplateList(FestivalActiveData.class);
		for (FestivalActiveData data : dataList) {
			festivalMap.put(data.getPid(), data);
		}
	}

	private void initSpringChargeMap() {
		List<SpringChargeData> dataList = TemplateManager.getTemplateList(SpringChargeData.class);
		for (SpringChargeData data : dataList) {
			springChargeMap.put(data.getPid(), data);
		}
	}

}
