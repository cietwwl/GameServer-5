package com.mi.game.module.reward;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.mi.core.cache.QueryInfo;
import com.mi.core.cache.QueryType;
import com.mi.core.cache.bean.QueryBean;
import com.mi.core.dao.KeyGeneratorDAO;
import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.TemplateManager;
import com.mi.core.engine.annotation.Module;
import com.mi.core.pojo.BaseEntity;
import com.mi.core.pojo.KeyGenerator;
import com.mi.core.template.BaseTemplate;
import com.mi.core.util.DateTimeUtil;
import com.mi.game.defines.ErrorIds;
import com.mi.game.defines.KindIDs;
import com.mi.game.defines.ModuleNames;
import com.mi.game.defines.RewardType;
import com.mi.game.defines.SysConstants;
import com.mi.game.module.bag.BagModule;
import com.mi.game.module.bag.data.BagData;
import com.mi.game.module.bag.pojo.BagEntity;
import com.mi.game.module.bag.pojo.BagItem;
import com.mi.game.module.base.BaseModule;
import com.mi.game.module.equipment.EquipmentModule;
import com.mi.game.module.equipment.data.EquipmentData;
import com.mi.game.module.equipment.data.EquipmentShardData;
import com.mi.game.module.equipment.pojo.Equipment;
import com.mi.game.module.equipment.pojo.EquipmentMapEntity;
import com.mi.game.module.equipment.pojo.EquipmentShard;
import com.mi.game.module.hero.HeroModule;
import com.mi.game.module.hero.data.HeroData;
import com.mi.game.module.hero.data.HeroShardData;
import com.mi.game.module.hero.pojo.Hero;
import com.mi.game.module.hero.pojo.HeroEntity;
import com.mi.game.module.hero.pojo.HeroShard;
import com.mi.game.module.lead.LeadModule;
import com.mi.game.module.lead.protocol.ExpResponse;
import com.mi.game.module.login.LoginModule;
import com.mi.game.module.login.dao.PlayerEntitiyDAO;
import com.mi.game.module.login.pojo.PlayerEntity;
import com.mi.game.module.login.pojo.PlayerStatusEntity;
import com.mi.game.module.mailBox.MailBoxModule;
import com.mi.game.module.manual.ManualModule;
import com.mi.game.module.manual.pojo.HeroManual;
import com.mi.game.module.manual.pojo.HeroManualsEntity;
import com.mi.game.module.pet.PetModule;
import com.mi.game.module.pet.data.PetShardData;
import com.mi.game.module.pet.pojo.PetEntity;
import com.mi.game.module.pet.pojo.PetShard;
import com.mi.game.module.pk.PkModule;
import com.mi.game.module.pk.pojo.PkRewardEntity;
import com.mi.game.module.reward.dao.RewardDAO;
import com.mi.game.module.reward.dao.SystemGetListEntityDAO;
import com.mi.game.module.reward.dao.SystemRewardEntityDAO;
import com.mi.game.module.reward.data.GoodsBean;
import com.mi.game.module.reward.pojo.Reward;
import com.mi.game.module.reward.pojo.RewardCenterEntity;
import com.mi.game.module.reward.pojo.SystemGetListEntity;
import com.mi.game.module.reward.pojo.SystemRewardEntity;
import com.mi.game.module.reward.protocol.RewardProtocol;
import com.mi.game.module.talisman.TalismanModule;
import com.mi.game.module.talisman.data.TalismanData;
import com.mi.game.module.talisman.data.TalismanShardData;
import com.mi.game.module.talisman.pojo.TalismanEntity;
import com.mi.game.module.talisman.pojo.TalismanMapEntity;
import com.mi.game.module.talisman.pojo.TalismanShard;
import com.mi.game.module.vitatly.VitatlyModule;
import com.mi.game.module.vitatly.pojo.VitatlyEntity;
import com.mi.game.module.wallet.WalletModule;
import com.mi.game.module.wallet.pojo.WalletEntity;
import com.mi.game.util.CommonMethod;
import com.mi.game.util.Utilities;

@Module(name = ModuleNames.RewardModule, clazz = RewardModule.class)
public class RewardModule extends BaseModule {
	private final RewardDAO rewardDAO = RewardDAO.getInstance();
	private final SystemRewardEntityDAO systemRewardEntityDAO = SystemRewardEntityDAO.getInstance();
	private final SystemGetListEntityDAO systemGetListEntityDAO = SystemGetListEntityDAO.getInstance();
	private final KeyGeneratorDAO keyGeneratorDAO = KeyGeneratorDAO.getInstance();
	private static final long outOfDateTime = 14 * DateTimeUtil.ONE_DAY_TIME_MS;
	private final PlayerEntitiyDAO playerEntitiyDAO = PlayerEntitiyDAO.getInstance();

	@Override
	public void init() {
		initSystemID();
		initSystemRewardEntity();
	}

	public void initSystemID() {
		String clsName = SysConstants.systemRewardEntity;
		KeyGenerator keyGenerator = keyGeneratorDAO.getEntity(clsName);
		if (keyGenerator == null) {
			keyGenerator = new KeyGenerator();
			keyGenerator.setKey(clsName);
			keyGenerator.setNextId(SysConstants.systemRewardStartID);
			keyGeneratorDAO.save(keyGenerator);
		}
	}

	private void initSystemRewardEntity() {
		// 系统发放奖励列表为空,初始化默认
		if (systemRewardEntityDAO.getSystemRewardEntityList().size() == 0) {
			List<GoodsBean> goodsList = new ArrayList<GoodsBean>();
			// 进阶丹一个
			goodsList.add(new GoodsBean(10174, 1));
			this.addSystemReward("defaultReward", goodsList, "默认奖励类型请勿删除!");
		}
	}

	@SuppressWarnings("unchecked")
	public int addGoods(String playerID, int pid, int num, Object param, boolean isSave, Map<String, GoodsBean> showMap, Map<String, Object> itemMap, IOMessage ioMessage) {
		if (num > 1000) {
			if (pid != KindIDs.SILVERTYPE && pid != KindIDs.HEROSOUL && pid != KindIDs.LEADEXP) {
				logger.error("addGoods monitor ===用户 #" + playerID + "# 增加 #"
						+ num + "# 个 #" + pid);
			}
		}
		if (num < 0) {
			throw new IllegalArgumentException(ErrorIds.ParamWrong + "");
		}
		boolean addOk = false;
		if (pid == KindIDs.GOLDTYPE || pid == KindIDs.SILVERTYPE
				|| pid == KindIDs.HEROSOUL || pid == KindIDs.JEWELSOUL
				|| pid == KindIDs.REPUTATION) { // 如果是银币、金币、战魂
			WalletModule walletModule = ModuleManager.getModule(ModuleNames.WalletModule, WalletModule.class);
			WalletEntity wallet = walletModule.addCurrencyWalletEntity(playerID, pid, num, isSave, ioMessage);
			if (itemMap != null) {
				itemMap.put("WalletEntity", wallet.responseMap());
			}
			addOk = true;
		} else if (pid == KindIDs.LEADEXP) {
			HeroModule heroModule = ModuleManager.getModule(ModuleNames.HeroModule, HeroModule.class);
			Hero lead = heroModule.getLead(playerID, ioMessage);
			LeadModule leadModule = ModuleManager.getModule(ModuleNames.LeadModule, LeadModule.class);
			ExpResponse expResponse = leadModule.addExp(playerID, lead, isSave, num, itemMap, ioMessage);
			if (itemMap != null) {
				itemMap.put("expInfo", expResponse);
			}
			addOk = true;
		} else if (pid == KindIDs.ENERGY) {
			VitatlyModule vitatlyModule = ModuleManager.getModule(ModuleNames.VitatlyModule, VitatlyModule.class);
			VitatlyEntity vitatlyEntity = vitatlyModule.addEnergy(playerID, num);
			if (itemMap != null) {
				itemMap.put("vitatlyEntity", vitatlyEntity.responseMap());
			}
			addOk = true;
		} else if (pid == KindIDs.VITALITY) {
			VitatlyModule vitatlyModule = ModuleManager.getModule(ModuleNames.VitatlyModule, VitatlyModule.class);
			VitatlyEntity vitatlyEntity = vitatlyModule.addVitatly(playerID, num);
			if (itemMap != null) {
				itemMap.put("vitatlyEntity", vitatlyEntity.responseMap());
			}
			addOk = true;
		} else if (pid == KindIDs.PKREWARD) {
			PkModule pkModule = ModuleManager.getModule(ModuleNames.PkModule,
					PkModule.class);
			PkRewardEntity rewardEntity = pkModule.getPkReward(playerID, num);
			if (itemMap != null) {
				itemMap.put("rewardEntity", rewardEntity.responseMap());
			}
		} else {
			BaseTemplate template = TemplateManager.getTemplateData(pid);
			/** 英雄数据 */
			if (template instanceof HeroData) {
				List<Object> heroList;
				if (itemMap.get("addHeroList") == null) {
					heroList = new ArrayList<Object>();
				} else {
					heroList = (List<Object>) itemMap.get("addHeroList");
				}

				List<HeroManual> manualList;
				if (itemMap.get("addManualList") == null) {
					manualList = new ArrayList<>();
				} else {
					manualList = (List<HeroManual>) itemMap.get("addManualList");
				}

				HeroModule heroModuole = ModuleManager.getModule(ModuleNames.HeroModule, HeroModule.class);
				ManualModule manualModule = ModuleManager.getModule(ModuleNames.ManualModule, ManualModule.class);
				for (int i = 1; i <= num; i++) {
					Hero hero = heroModuole.addHero(playerID, pid, isSave, ioMessage);
					HeroManual manual = manualModule.addNewManual(playerID, hero.getTemplateID(), isSave, ioMessage);
					if (manual != null) {
						manualList.add(manual);
					}
					heroList.add(hero.responseMap());
				}
				itemMap.put("addManualList", manualList);
				itemMap.put("addHeroList", heroList);
				addOk = true;
			} else if(template instanceof PetShardData){
				/* 宠物碎片 */
				List<PetShard> petShardList;
				if (itemMap.containsKey("petShardList")) {
					petShardList = (List<PetShard>) itemMap.get("petShardList");
				} else {
					petShardList = new ArrayList<>();
				}
				PetModule petModule = ModuleManager.getModule(ModuleNames.PetModule, PetModule.class);
				try {
					petModule.addPetShard(playerID,petShardList, pid, num, isSave, ioMessage);					
				} catch (IllegalArgumentException ex) {
					return Integer.parseInt(ex.getMessage());
				}
				itemMap.put("petShardList", petShardList);
				addOk = true;
			}else if (template instanceof BagData) {
				/** 背包数据 */
				List<BagItem> bagList;
				if (itemMap.containsKey("bagItem")) {
					bagList = (List<BagItem>) itemMap.get("bagItem");
				} else {
					bagList = new ArrayList<BagItem>();
				}
				BagModule bagModule = ModuleManager.getModule(ModuleNames.BagModule, BagModule.class);
				try {
					BagItem bagItem = bagModule.addItem(playerID, pid, num, isSave, ioMessage);
					bagList.add(bagItem);
				} catch (Exception ex) {
					return Integer.parseInt(ex.getMessage());
				}
				itemMap.put("bagItem", bagList);
				addOk = true;
			} else
			/** 装备数据 */
			if (template instanceof EquipmentData) {
				List<Equipment> equipList;
				if (itemMap.containsKey("equipList")) {
					equipList = (List<Equipment>) itemMap.get("equipList");
				} else {
					equipList = new ArrayList<Equipment>();
				}
				EquipmentModule equipmentModule = ModuleManager.getModule(ModuleNames.EquipmentModule, EquipmentModule.class);
				try {
					for (int i = 1; i <= num; i++) {
						Equipment equipment = equipmentModule.addEquipment(playerID, pid, isSave, ioMessage);
						equipList.add(equipment);
					}
				} catch (Exception ex) {
					return Integer.parseInt(ex.getMessage());
				}
				itemMap.put("equipList", equipList);
				addOk = true;
			} else
			// 装备碎片添加
			if (template instanceof EquipmentShardData) {
				List<EquipmentShard> equipmentShardList;
				if (itemMap.containsKey("equipShardList")) {
					equipmentShardList = (List<EquipmentShard>) itemMap.get("equipShardList");
				} else {
					equipmentShardList = new ArrayList<>();
				}
				EquipmentModule equipmentModule = ModuleManager.getModule(ModuleNames.EquipmentModule, EquipmentModule.class);
				try {
					EquipmentShard equipmentShard = equipmentModule.addEquipmentShard(playerID, pid, num, isSave, ioMessage);
					equipmentShardList.add(equipmentShard);
				} catch (IllegalArgumentException ex) {
					return Integer.parseInt(ex.getMessage());
				}
				itemMap.put("equipShardList", equipmentShardList);
				addOk = true;
			} else
			/** 宝物添加 **/
			if (template instanceof TalismanData) {
				List<TalismanEntity> talismanList;
				if (itemMap.containsKey("addTalismanList")) {
					talismanList = (List<TalismanEntity>) itemMap.get("addTalismanList");
				} else {
					talismanList = new ArrayList<TalismanEntity>();
				}
				TalismanModule talismanModule = ModuleManager.getModule(ModuleNames.TalismanModule, TalismanModule.class);

				try {
					for (int i = 1; i <= num; i++) {
						int exp = 0;
						if (param != null) {
							exp = Integer.parseInt(param.toString());
						}
						TalismanEntity talisman = talismanModule.addTalisman(playerID, pid, exp, isSave, ioMessage);
						talismanList.add(talisman);
					}
				} catch (Exception ex) {
					return Integer.parseInt(ex.getMessage());
				}
				itemMap.put("addTalismanList", talismanList);
				addOk = true;
			} else /** 宝物碎片添加 */
			if (template instanceof TalismanShardData) {
				List<Map<String, Object>> talismanShardList;
				if (itemMap.containsKey("addTalismanShardList")) {
					talismanShardList = (List<Map<String, Object>>) itemMap.get("addTalismanShardList");
				} else {
					talismanShardList = new ArrayList<>();
				}
				TalismanModule talismanModule = ModuleManager.getModule(ModuleNames.TalismanModule, TalismanModule.class);
				try {
					TalismanShard talismanShard = talismanModule.addTalismanShard1(playerID, pid, num);
					talismanShardList.add(talismanShard.responseMap());
					itemMap.put("addTalismanShardList", talismanShardList);
				} catch (Exception ex) {
					return Integer.parseInt(ex.getMessage());
				}
				addOk = true;
			} else
			// 英雄碎片添加
			if (template instanceof HeroShardData) {
				List<HeroShard> heroShardList;
				if (itemMap.containsKey("addHeroShardList")) {
					heroShardList = (List<HeroShard>) itemMap.get("addHeroShardList");
				} else {
					heroShardList = new ArrayList<>();
				}
				HeroModule heroModuole = ModuleManager.getModule(ModuleNames.HeroModule, HeroModule.class);
				HeroShard heroShard = heroModuole.addHeroShard(playerID, pid, num, isSave, ioMessage);
				heroShardList.add(heroShard);
				itemMap.put("addHeroShardList", heroShardList);
				addOk = true;
			}

		}

		if (addOk) {
			CommonMethod.addToGoodsMap(pid, num, showMap);
		}
		return 0;
	}

	/**
	 * 批量添加物品
	 * 
	 * @param playerID
	 * @param pid
	 * @param num
	 * @param isSave
	 * @param goodsMap
	 * @param itemMap
	 * @param ioMessage
	 */
	// public void addGoods(String playerID, int[] pid, int[] num, boolean
	// isSave, Map<String, GoodsBean> goodsMap, Map<String, Object> itemMap,
	// IOMessage ioMessage) {
	// // 单个添加物品
	// for (int i = 0; i < pid.length; i++) {
	// this.addGoods(playerID, pid[i], num[i], false, goodsMap, itemMap,
	// ioMessage);
	// }
	// if (isSave) { // 如果是自动保存
	// saveItemMap(itemMap, ioMessage);
	// }
	// }

	/**
	 * 批量添加物品
	 * 
	 * @param playerID
	 * @param goodsList
	 * @param isSave
	 * @param goodsMap
	 * @param itemMap
	 * @param ioMessage
	 */
	public int addGoods(String playerID, List<GoodsBean> goodsList, boolean isSave, Map<String, GoodsBean> showMap, Map<String, Object> itemMap, IOMessage ioMessage) {
		int code = 0;
		// 单个添加物品
		for (GoodsBean item : goodsList) {
			code = this.addGoods(playerID, item.getPid(), item.getNum(), item.getParam(), true, showMap, itemMap, ioMessage);
			if (code != 0) {
				return code;
			}
		}

		if (isSave) { // 如果是自动保存
			saveItemMap(itemMap, ioMessage);
		}
		return code;
	}

	/**
	 * 保存添加物品后返回的itemMap到DB
	 * 
	 * @param itemMap
	 */
	public void saveItemMap(Map<String, Object> itemMap, IOMessage ioMessage) {
		if (ioMessage != null) {
			List<BaseEntity> entityList = new ArrayList<BaseEntity>();
			BagEntity cacheBagEntity = (BagEntity) ioMessage.getInputParse().get(BagEntity.class.getName());
			if (cacheBagEntity != null) {
				entityList.add(cacheBagEntity);
			}
			WalletEntity walletEntity = (WalletEntity) ioMessage.getInputParse().get(WalletEntity.class.getName());
			if (walletEntity != null) {
				entityList.add(walletEntity);
			}
			EquipmentMapEntity equipmentMapEntity = (EquipmentMapEntity) ioMessage.getInputParse().get(EquipmentMapEntity.class.getName() + ioMessage.getPlayerId());
			if (equipmentMapEntity != null) {
				entityList.add(equipmentMapEntity);
			}
			HeroManualsEntity heroManualsEntity = (HeroManualsEntity) ioMessage.getInputParse().get(HeroManualsEntity.class.getName() + ioMessage.getPlayerId());
			if (heroManualsEntity != null) {
				entityList.add(heroManualsEntity);
			}
			TalismanMapEntity talismanMapEntity = (TalismanMapEntity) ioMessage.getInputParse().get(TalismanMapEntity.class.getName());
			if (talismanMapEntity != null) {
				entityList.add(talismanMapEntity);
			}
			HeroEntity heroEntity = (HeroEntity) ioMessage.getInputParse().get(HeroEntity.class.getName() + ioMessage.getPlayerId());
			if (heroEntity != null) {
				entityList.add(heroEntity);
			}
			PetEntity petEntity = (PetEntity)ioMessage.getInputParse().get(PetEntity.class.getName());
			if(petEntity != null){
				entityList.add(petEntity);
			}
			this.saveEntityList(entityList);
		}
	}

	private void saveEntityList(List<BaseEntity> entityList) {
		KeyGeneratorDAO.getInstance().save(entityList);
	}

	/**
	 * 使用物品
	 * 
	 * @param playerID
	 *            玩家id
	 * @param pid
	 *            物品id
	 * @param num
	 *            数量
	 * @param isSave
	 *            是否立即保存到DB
	 * @param goodsMap
	 *            添加的物品描述map
	 * @param itemMap
	 *            具体添加给玩家的item信息
	 */

	@SuppressWarnings("unchecked")
	public int useGoods(String playerID, int pid, int num, long targetID, boolean isSave, Map<String, GoodsBean> showMap, Map<String, Object> itemMap, IOMessage ioMessage) {
		int code = 0;
		if (num > 1000) {
			if (pid != KindIDs.SILVERTYPE && pid != KindIDs.HEROSOUL && pid != KindIDs.LEADEXP) {
				logger.error("useGoods monitor ===用户 #" + playerID + "# 使用 #"
						+ num + "# 个 #" + pid);
			}
		}
		if (num < 0) {
			throw new IllegalArgumentException(ErrorIds.ParamWrong + "");
		}
		if (pid == KindIDs.GOLDTYPE || pid == KindIDs.SILVERTYPE
				|| pid == KindIDs.HEROSOUL || pid == KindIDs.JEWELSOUL
				|| pid == KindIDs.REPUTATION) { // 如果是金币、钻石、粮草
			WalletModule walletModule = ModuleManager.getModule(ModuleNames.WalletModule, WalletModule.class);
			code = walletModule.verifyCurrency(playerID, pid, num, ioMessage);
			if (code != 0) {
				return code;
			}
			WalletEntity wallet = walletModule.consumeCurrencyWalletEntity(playerID, pid, num, isSave, ioMessage);
			if (itemMap != null) {
				itemMap.put("WalletEntity", wallet.responseMap());
			}
		} else if (pid == KindIDs.VITALITY) {
			VitatlyModule module = ModuleManager.getModule(ModuleNames.VitatlyModule, VitatlyModule.class);
			VitatlyEntity vitatlyEntity = module.consumeVitatly(playerID, isSave, num);
			if (itemMap != null) {
				itemMap.put("vitatlyEntity", vitatlyEntity.responseMap());
			}
		} else if (pid == KindIDs.ENERGY) {
			VitatlyModule module = ModuleManager.getModule(ModuleNames.VitatlyModule, VitatlyModule.class);
			VitatlyEntity vitatlyEntity = module.consumeEnergy(playerID, num);
			if (itemMap != null) {
				itemMap.put("vitatlyEntity", vitatlyEntity.responseMap());
			}
		} else if (pid == KindIDs.PKREWARD) { // 比武荣誉
			PkModule pkModule = ModuleManager.getModule(ModuleNames.PkModule,
					PkModule.class);
			PkRewardEntity rewardEntity = pkModule.usePkReward(playerID, num);
			if (itemMap != null) {
				itemMap.put("rewardEntity", rewardEntity.responseMap());
			}
		} else {
			BaseTemplate template = TemplateManager.getTemplateData(pid);
			/** 背包数据 */
			if (template instanceof BagData) {
				BagModule bagModule = ModuleManager.getModule(ModuleNames.BagModule, BagModule.class);
				BagItem bagItem = bagModule.useItem(playerID, pid, num, targetID, isSave, showMap, itemMap, (BagData) template, ioMessage);
				bagModule.addItemToItemMap(itemMap, bagItem);
			} else
			/** 宝物移除 **/
			if (template instanceof TalismanData) {
				long removeID = 0;
				List<Long> removeTalismanList;
				if (itemMap.get("removeTalismanList") == null) {
					removeTalismanList = new ArrayList<Long>();
				} else {
					removeTalismanList = (List<Long>) itemMap.get("removeTalismanList");
				}
				TalismanModule talismanModule = ModuleManager.getModule(ModuleNames.TalismanModule, TalismanModule.class);
				try {
					for (int i = 1; i <= num; i++) {
						removeID = talismanModule.deductTalisman(playerID, pid, isSave, ioMessage);
						removeTalismanList.add(removeID);
					}
				} catch (Exception ex) {
					return Integer.parseInt(ex.getMessage());
				}
				itemMap.put("removeTalismanList", removeTalismanList);
			} else /** 英雄移除 */
			if (template instanceof HeroData) {
				long removeID = 0;
				List<Long> removeHeroList;
				if (itemMap.get("removeHeroList") == null) {
					removeHeroList = new ArrayList<Long>();
				} else {
					removeHeroList = (List<Long>) itemMap.get("removeHeroList");
				}
				HeroModule heroModule = ModuleManager.getModule(ModuleNames.HeroModule, HeroModule.class);
				try {
					for (int i = 1; i <= num; i++) {
						removeID = heroModule.deductHero(playerID, pid, isSave, ioMessage);
						removeHeroList.add(removeID);
					}
				} catch (Exception ex) {
					return Integer.parseInt(ex.getMessage());
				}
				itemMap.put("removeHeroList", removeHeroList);
			} else if (template instanceof EquipmentData) {
				List<Long> removeEquipmentList;
				if (itemMap.get("removeEquipmentList") == null) {
					removeEquipmentList = new ArrayList<Long>();
				} else {
					removeEquipmentList = (List<Long>) itemMap.get("removeEquipmentList");
				}
				EquipmentModule equipmentModule = ModuleManager.getModule(ModuleNames.EquipmentModule, EquipmentModule.class);
				try {
					for (int i = 0; i < num; i++) {
						long equipID = equipmentModule.removeEquipment(playerID, pid, isSave, ioMessage);
						removeEquipmentList.add(equipID);
					}
				} catch (Exception ex) {
					return Integer.parseInt(ex.getMessage());
				}
				itemMap.put("removeEquipmentList", removeEquipmentList);
			} else if (template instanceof HeroShardData) {
				List<HeroShard> heroShardList;
				if (itemMap.containsKey("removeHeroShardList")) {
					heroShardList = (List<HeroShard>) itemMap.get("removeHeroShardList");
				} else {
					heroShardList = new ArrayList<>();
				}
				HeroModule heroModuole = ModuleManager.getModule(ModuleNames.HeroModule, HeroModule.class);
				for (int i = 0; i < num; i++) {
					HeroShard heroShard = heroModuole.removeHeroShard(playerID, pid, num, isSave, ioMessage);
					heroShardList.add(heroShard);
				}
				itemMap.put("removeHeroShardList", heroShardList);
			} else {
				logger.error("未知类型: " + pid + "type" + ioMessage.getType());
				if (ioMessage.getType() == 602) {
					logger.error(ioMessage.getInputParse("goodsList").toString());
				}
				return ErrorIds.UnKnowType;
			}
		}

		// if (showMap != null) {
		// String key = pid + "";
		// GoodsBean bean = showMap.get(key);
		// if (bean != null) {
		// bean.setNum(bean.getNum() + num);
		// } else {
		// bean = new GoodsBean(pid, num);
		// showMap.put(key, bean);
		// }
		// }
		return 0;
	}

	/**
	 * 批量使用物品
	 * 
	 * @param playerID
	 * @param goodsList
	 * @param isSave
	 * @param goodsMap
	 * @param itemMap
	 * @param ioMessage
	 */
	public int useGoods(String playerID, List<GoodsBean> goodsList, long targetID, boolean isSave, Map<String, GoodsBean> showMap, Map<String, Object> itemMap,
			IOMessage ioMessage) {
		// 单个扣除物品
		for (GoodsBean item : goodsList) {
			int code = this.useGoods(playerID, item.getPid(), item.getNum(), targetID, false, showMap, itemMap, ioMessage);
			if (code != 0) {
				return code;
			}
		}
		if (isSave) { // 如果是自动保存
			saveItemMap(itemMap, ioMessage);
		}
		return 0;
	}

	/**
	 * 初始化奖励中心
	 * */
	public RewardCenterEntity initCenterEntity(String playerID) {
		RewardCenterEntity entity = new RewardCenterEntity();
		entity.setKey(playerID);
		return entity;
	}

	public RewardCenterEntity getRewardCenterEntity(String playerID) {
		RewardCenterEntity entity = this.getRewardEntity(playerID);
		List<Reward> rewardList = entity.getRewardList();
		long nowTime = System.currentTimeMillis();
		boolean isSave = false;
		for (Iterator<Reward> iter = rewardList.iterator(); iter.hasNext();) {
			Reward reward = iter.next();
			long endTime = reward.getTime();
			if (endTime + outOfDateTime < nowTime) {
				isSave = true;
				iter.remove();
			}
		}
		if (isSave) {
			this.saveRewardEntity(entity);
		}
		return entity;
	}

	/***
	 * 获取奖励中心信息
	 * */
	public RewardCenterEntity getRewardEntity(String playerID) {
		RewardCenterEntity entity = rewardDAO.getEntity(playerID);
		if (entity == null)
			entity = initCenterEntity(playerID);
		// throw new NullPointerException(ErrorIds.NoEntity + "");
		return entity;
	}

	/**
	 * 保存奖励中心信息
	 * */
	public void saveRewardEntity(RewardCenterEntity entity) {
		rewardDAO.save(entity);
	}

	/**
	 * 添加奖励
	 * 
	 * */
	public PlayerStatusEntity addReward(String playerID, List<GoodsBean> goodsList, int type) {
		if (goodsList == null || goodsList.isEmpty()) {
			throw new IllegalArgumentException(ErrorIds.RewardNotEmpty + "");
		}
		RewardCenterEntity entity = this.getRewardCenterEntity(playerID);
		List<Reward> rewardList = entity.getRewardList();
		long rewardID = entity.getCount();
		Reward reward = new Reward();
		reward.setTime(System.currentTimeMillis());
		reward.setGoodsList(goodsList);
		reward.setType(type);
		reward.setRewardID(rewardID);
		rewardList.add(reward);
		this.saveRewardEntity(entity);
		LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule, LoginModule.class);
		PlayerStatusEntity playerStatusEntity = loginModule.changePlayerRewardEntity(playerID, true);
		return playerStatusEntity;
	}

	/**
	 * 领取奖励
	 * */
	public void getReward(String playerID, long rewardID, RewardProtocol protocol) {
		RewardCenterEntity entity = this.getRewardCenterEntity(playerID);
		List<Reward> rewardList = entity.getRewardList();
		Reward reward = null;
		for (Reward temp : rewardList) {
			if (temp.getRewardID() == rewardID) {
				reward = temp;
				break;
			}
		}
		if (reward == null) {
			throw new IllegalArgumentException(ErrorIds.RewardIDWrong + "");
		}
		List<GoodsBean> goodsList = reward.getGoodsList();
		Map<String, Object> itemMap = new HashMap<String, Object>();
		this.addGoods(playerID, goodsList, true, null, itemMap, null);
		protocol.setItemMap(itemMap);
		protocol.setShowMap(goodsList);
		rewardList.remove(reward);
		if (rewardList.isEmpty()) {
			LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule, LoginModule.class);
			loginModule.changePlayerRewardEntity(playerID, false);
		}
		this.saveRewardEntity(entity);
	}

	/**
	 * 领取全部奖励
	 * */
	public void getAllReward(String playerID, RewardProtocol protocol) {
		RewardCenterEntity entity = this.getRewardCenterEntity(playerID);
		List<Reward> rewardList = entity.getRewardList();
		Map<Integer, GoodsBean> goodsList = new HashMap<>();
		if (!rewardList.isEmpty()) {
			for (Reward reward : rewardList) {
				List<GoodsBean> tempList = reward.getGoodsList();
				for (GoodsBean goodsBean : tempList) {
					int pid = goodsBean.getPid();
					GoodsBean tempBean = goodsList.get(pid);
					if (tempBean != null) {
						tempBean.setNum(tempBean.getNum() + goodsBean.getNum());
					} else {
						goodsList.put(pid, goodsBean);
					}
				}
			}
		}
		entity.setRewardList(new ArrayList<Reward>());
		Map<String, Object> itemMap = new HashMap<>();
		this.addGoods(playerID, Utilities.getGoodList(goodsList), true, null, itemMap, null);
		protocol.setItemMap(itemMap);
		LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule, LoginModule.class);
		loginModule.changePlayerRewardEntity(playerID, false);
		this.saveRewardEntity(entity);
	}

	/**
	 * 获取奖励内容
	 * */
	public SystemRewardEntity getSystemRewardEntity(String rewardKey) {
		SystemRewardEntity systemRewardEntity = systemRewardEntityDAO.getsSystemRewardEntityByKey(rewardKey);
		if (systemRewardEntity == null) {
			logger.error("系统奖励为空");
			throw new IllegalArgumentException(ErrorIds.NoEntity + "");
		}
		return systemRewardEntity;
	}

	/**
	 * 获取奖励内容
	 * */
	public SystemRewardEntity getSystemRewardEntity2(String rewardKey) {
		return systemRewardEntityDAO.getsSystemRewardEntityByKey(rewardKey);
	}

	/**
	 * 保存奖励内容
	 * */
	public void saveSystemRewardEntity(SystemRewardEntity systemRewardEntity) {
		systemRewardEntityDAO.save(systemRewardEntity);
	}

	/**
	 * 获取已领取的奖励
	 * */
	public SystemGetListEntity getList(String playerID) {
		SystemGetListEntity systemGetListEntity = systemGetListEntityDAO.getEntity(playerID);
		if (systemGetListEntity == null) {
			systemGetListEntity = new SystemGetListEntity();
			systemGetListEntity.setKey(playerID);
		}
		return systemGetListEntity;
	}

	/**
	 * 保存奖励列表
	 * */
	private void saveGetListEntity(SystemGetListEntity systemGetListEntity) {
		systemGetListEntityDAO.save(systemGetListEntity);
	}

	/**
	 * 发送奖励(单发)
	 * */
	public void giveReward(String rewardKey, String playerID) {
		SystemRewardEntity systemRewardEntity = this.getSystemRewardEntity(rewardKey);				
		if(playerID!=null){
			String[] str=playerID.split(",");
			if(str!=null){
				for(int i=0;i<str.length;i++){					
					playerID=str[i];
					SystemGetListEntity systemGetListEntity = this.getList(playerID);
					List<String> getList = systemGetListEntity.getGetList();
					if (getList.contains(rewardKey)) {
						throw new IllegalArgumentException(ErrorIds.AlreadyGetReward + "");
					}
					getList.add(rewardKey);
					this.saveGetListEntity(systemGetListEntity);
					List<GoodsBean> goodsList = systemRewardEntity.getGoodsList();

					this.addReward(playerID, goodsList, RewardType.systemReward);
				}				
			}
		}				
	}

	/**
	 * 群发
	 * */
	public void giveAllPlayerReward(String rewardKey) {
		SystemRewardEntity systemRewardEntity = this.getSystemRewardEntity(rewardKey);
		List<GoodsBean> goodsList = systemRewardEntity.getGoodsList();
		long count = rewardDAO.queryCount(new QueryInfo());
		QueryInfo queryInfo = new QueryInfo(1, 100, "playerID");
		queryInfo.setTotal(count);
		queryInfo.initTotalPage();
		List<RewardCenterEntity> queryList = null;
		long nowTime = System.currentTimeMillis();
		LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule, LoginModule.class);
		// 分页查询数据
		while (queryInfo.getPage() <= queryInfo.getTotalPage()) {
			queryList = rewardDAO.queryPage(queryInfo);
			if (queryList == null || queryList.isEmpty()) {
				break;
			}
			int size = queryList.size();
			for (int i = 0; i < size; i++) {
				RewardCenterEntity rewardCenterEntity = queryList.get(i);
				String playerID = rewardCenterEntity.getKey().toString();
				if (Utilities.isNpc(playerID)) {
					continue;
				}
				SystemGetListEntity systemGetListEntity = this.getList(playerID);
				List<String> getList = systemGetListEntity.getGetList();
				if (getList.contains(rewardKey)) {
					continue;
				}
				List<Reward> rewardList = rewardCenterEntity.getRewardList();
				Reward reward = new Reward();
				reward.setGoodsList(goodsList);
				reward.setTime(nowTime);
				reward.setType(RewardType.systemReward);
				reward.setRewardID(rewardCenterEntity.getCount());
				rewardList.add(reward);
				rewardCenterEntity.setRewardList(rewardList);
				getList.add(rewardKey);
				this.saveGetListEntity(systemGetListEntity);
				loginModule.changePlayerRewardEntity(playerID, true);
			}
			rewardDAO.save(queryList);
			queryInfo.setPage(queryInfo.getPage() + 1);
		}
	}

	/**
	 * 获取奖励ID
	 * */
	public long getSystemRewardID() {
		String clsName = SysConstants.systemRewardEntity;
		long equipID = keyGeneratorDAO.updateInc(clsName);
		return equipID;
	}

	/**
	 * 增加奖励类型
	 * */
	public void addSystemReward(String rewardKey, List<GoodsBean> goodsList, String desc) {
		SystemRewardEntity systemRewardEntity = new SystemRewardEntity();
		systemRewardEntity.setCreateTime(System.currentTimeMillis());
		systemRewardEntity.setKey(this.getSystemRewardID());
		systemRewardEntity.setRewardKey(rewardKey);
		systemRewardEntity.setGoodsList(goodsList);
		systemRewardEntity.setDesc(desc);
		this.saveSystemRewardEntity(systemRewardEntity);
	}

	/**
	 * 根据平台发送邮件和奖励
	 * */
	public void addMsgAndReward(String platform, String rewardKey, String msg, String title) {
		List<GoodsBean> goodsList = null;
		if (StringUtils.isNotBlank(rewardKey)) {
			SystemRewardEntity systemRewardEntity = this.getSystemRewardEntity(rewardKey);
			goodsList = systemRewardEntity.getGoodsList();
		}
		List<PlayerEntity> playerList = null;
		QueryInfo queryInfo = new QueryInfo(1, 100, "playerID");
		long count = 0;
		if (StringUtils.isNotBlank(platform)) {
			count = playerEntitiyDAO.getPlatformCount(platform);
			QueryBean queryBean = new QueryBean("platform", QueryType.EQUAL, platform);
			queryInfo.addQueryBean(queryBean);
		} else {
			count = playerEntitiyDAO.queryCount(new QueryInfo());
		}
		queryInfo.setTotal(count);
		queryInfo.initTotalPage();
		long nowTime = System.currentTimeMillis();
		LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule, LoginModule.class);
		MailBoxModule mailBoxModule = ModuleManager.getModule(ModuleNames.MailBoxModule, MailBoxModule.class);
		while (queryInfo.getPage() <= queryInfo.getTotalPage()) {
			playerList = playerEntitiyDAO.queryPage(queryInfo);
			if (playerList == null || playerList.isEmpty()) {
				break;
			}
			int size = playerList.size();
			List<RewardCenterEntity> rewaList = new ArrayList<>();
			for (int i = 0; i < size; i++) {
				PlayerEntity playerEntity = playerList.get(i);
				String playerID = playerEntity.getKey().toString();
				if (Utilities.isNpc(playerID)) {
					continue;
				}
				SystemGetListEntity systemGetListEntity = this.getList(playerID);
				List<String> getList = systemGetListEntity.getGetList();
				if (getList.contains(rewardKey)) {
					continue;
				}
				if (goodsList != null) {
					RewardCenterEntity rewardCenterEntity = this.getRewardCenterEntity(playerID);
					List<Reward> rewardList = rewardCenterEntity.getRewardList();
					Reward reward = new Reward();
					reward.setGoodsList(goodsList);
					reward.setTime(nowTime);
					reward.setType(RewardType.systemReward);
					reward.setRewardID(rewardCenterEntity.getCount());
					rewardList.add(reward);
					rewardCenterEntity.setRewardList(rewardList);
					getList.add(rewardKey);
					this.saveGetListEntity(systemGetListEntity);
					loginModule.changePlayerRewardEntity(playerID, true);
					rewaList.add(rewardCenterEntity);
				}
				if (StringUtils.isNotBlank(msg)) {
					mailBoxModule.addMail(playerID, msg, title);
				}
			}
			if (!rewaList.isEmpty()) {
				playerEntitiyDAO.save(rewaList);
			}
			queryInfo.setPage(queryInfo.getPage() + 1);
		}
		logger.error("平台奖励发放完毕,共发放" + count + "份,用时"
				+ (System.currentTimeMillis() - nowTime) + "毫秒");

	}

}
