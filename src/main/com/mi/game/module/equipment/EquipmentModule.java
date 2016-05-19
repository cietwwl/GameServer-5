package com.mi.game.module.equipment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.mi.core.dao.KeyGeneratorDAO;
import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.TemplateManager;
import com.mi.core.engine.annotation.Module;
import com.mi.core.pojo.KeyGenerator;
import com.mi.game.defines.ActionType;
import com.mi.game.defines.EquipmentPart;
import com.mi.game.defines.ErrorIds;
import com.mi.game.defines.FriendshipType;
import com.mi.game.defines.KindIDs;
import com.mi.game.defines.ModuleNames;
import com.mi.game.defines.QualityType;
import com.mi.game.defines.RefineType;
import com.mi.game.defines.SysConstants;
import com.mi.game.module.achievement.AchievementModule;
import com.mi.game.module.analyse.AnalyseModule;
import com.mi.game.module.base.BaseModule;
import com.mi.game.module.dayTask.DayTaskModule;
import com.mi.game.module.equipment.dao.EquipmentEntityDAO;
import com.mi.game.module.equipment.data.EquipRefine;
import com.mi.game.module.equipment.data.EquipmentData;
import com.mi.game.module.equipment.data.EquipmentSetData;
import com.mi.game.module.equipment.data.EquipmentShardData;
import com.mi.game.module.equipment.data.EquipmentUpData;
import com.mi.game.module.equipment.pojo.Equipment;
import com.mi.game.module.equipment.pojo.EquipmentMapEntity;
import com.mi.game.module.equipment.pojo.EquipmentShard;
import com.mi.game.module.equipment.protocol.EquipmentProtocol;
import com.mi.game.module.hero.HeroModule;
import com.mi.game.module.hero.data.FriendShipData;
import com.mi.game.module.hero.data.HeroData;
import com.mi.game.module.hero.pojo.Hero;
import com.mi.game.module.hero.pojo.HeroEntity;
import com.mi.game.module.mainTask.MainTaskModule;
import com.mi.game.module.reward.RewardModule;
import com.mi.game.module.reward.data.GoodsBean;
import com.mi.game.module.talisman.TalismanModule;
import com.mi.game.module.vip.VipModule;
import com.mi.game.module.wallet.WalletModule;
import com.mi.game.module.wallet.pojo.WalletEntity;
import com.mi.game.util.Utilities;

/**
 * @author 刘凯旋 装备模块 2014年6月11日 下午9:05:39
 */
@Module(name = ModuleNames.EquipmentModule, clazz = EquipmentModule.class)
public class EquipmentModule extends BaseModule {

	private final KeyGeneratorDAO keyGeneratorDAO = KeyGeneratorDAO.getInstance();
	private final EquipmentEntityDAO equipmentEntityDAO = EquipmentEntityDAO.getInstance();
	private int greenType = 3;
	private int blueType = 4;
	private int purpleType = 5;
	private Map<Integer, Map<Integer, EquipRefine>> refineProp;
	private int MaxStrengLevel = SysConstants.maxStrengLevel;
	private Map<Integer,Map<Integer,Integer>> upMap;
	@Override
	public void init() {
		initEquipmentID();
		initRefineProp();
		initUpMap();
	}
	
	private void initUpMap(){
		upMap = new HashMap<>();
		List<EquipmentUpData> dataList = TemplateManager.getTemplateList(EquipmentUpData.class);
		for(EquipmentUpData data : dataList){
			upMap.put(data.getLevel(),data.getCost());
		}
	}

	private void initRefineProp() {
		List<Integer> propList = new ArrayList<>();
		propList.add(SysConstants.Hp);
		propList.add(SysConstants.Attack);
		propList.add(SysConstants.PDef);
		propList.add(SysConstants.MDef);
		propList.add(SysConstants.FinalAttack);
		propList.add(SysConstants.FinalDef);
		List<Integer> refineList = new ArrayList<>();
		refineList.add(RefineType.stone);
		refineList.add(RefineType.silver);
		refineList.add(RefineType.gold);
		refineProp = new HashMap<>();
		for (int prop : propList) {
			Map<Integer, EquipRefine> equipRefineMap = new HashMap<>();
			if (prop == SysConstants.Hp) {
				for (int refineType : refineList) {
					EquipRefine equipRefine = null;
					if (refineType == RefineType.stone) {
						equipRefine = new EquipRefine(0.6, -20, 20);
					} else if (refineType == RefineType.silver) {
						equipRefine = new EquipRefine(0.7, -15, 20);
					} else if (refineType == RefineType.gold) {
						equipRefine = new EquipRefine(0.75, -15, 30);
					}
					equipRefineMap.put(refineType, equipRefine);
				}
			} else if (prop == SysConstants.Attack) {
				for (int refineType : refineList) {
					EquipRefine equipRefine = null;
					if (refineType == RefineType.stone) {
						equipRefine = new EquipRefine(0.6, -5, 5);
					} else if (refineType == RefineType.silver) {
						equipRefine = new EquipRefine(0.7, -3, 5);
					} else if (refineType == RefineType.gold) {
						equipRefine = new EquipRefine(0.75, -3, 8);
					}
					equipRefineMap.put(refineType, equipRefine);
				}
			} else if (prop == SysConstants.PDef) {
				for (int refineType : refineList) {
					EquipRefine equipRefine = null;
					if (refineType == RefineType.stone) {
						equipRefine = new EquipRefine(0.6, -5, 5);
					} else if (refineType == RefineType.silver) {
						equipRefine = new EquipRefine(0.7, -3, 5);
					} else if (refineType == RefineType.gold) {
						equipRefine = new EquipRefine(0.75, -3, 8);
					}
					equipRefineMap.put(refineType, equipRefine);
				}
			} else if (prop == SysConstants.MDef) {
				for (int refineType : refineList) {
					EquipRefine equipRefine = null;
					if (refineType == RefineType.stone) {
						equipRefine = new EquipRefine(0.6, -5, 5);
					} else if (refineType == RefineType.silver) {
						equipRefine = new EquipRefine(0.7, -3, 5);
					} else if (refineType == RefineType.gold) {
						equipRefine = new EquipRefine(0.75, -3, 10);
					}
					equipRefineMap.put(refineType, equipRefine);
				}
			} else if (prop == SysConstants.FinalAttack) {
				for (int refineType : refineList) {
					EquipRefine equipRefine = null;
					if (refineType == RefineType.stone) {
						equipRefine = new EquipRefine(0.5, -5, 5);
					} else if (refineType == RefineType.silver) {
						equipRefine = new EquipRefine(0.6, -3, 5);
					} else if (refineType == RefineType.gold) {
						equipRefine = new EquipRefine(0.7, -3, 10);
					}
					equipRefineMap.put(refineType, equipRefine);
				}
			} else if (prop == SysConstants.FinalDef) {
				for (int refineType : refineList) {
					EquipRefine equipRefine = null;
					if (refineType == RefineType.stone) {
						equipRefine = new EquipRefine(0.5, -5, 5);
					} else if (refineType == RefineType.silver) {
						equipRefine = new EquipRefine(0.6, -3, 5);
					} else if (refineType == RefineType.gold) {
						equipRefine = new EquipRefine(0.7, -3, 10);
					}
					equipRefineMap.put(refineType, equipRefine);
				}
			}
			this.refineProp.put(prop, equipRefineMap);
		}
	}

	/**
	 * 初始化的装备实体
	 * 
	 * */
	public EquipmentMapEntity initEquipMentEntity(String playerID) {
		EquipmentMapEntity entity = new EquipmentMapEntity();
		entity.setMaxEquipNum(SysConstants.bagInitNum);
		entity.setMaxFragmentNum(SysConstants.fragmentNum);
		entity.setKey(playerID);
		Map<String, Equipment> equipMap = new HashMap<>();
		Map<Integer, EquipmentShard> shardMap = new HashMap<>();
		entity.setShardList(shardMap);
		entity.setEquipMap(equipMap);
		return entity;
	}

	// /**
	// * 初始化装备列表
	// * */
	// private Map<String, Equipment> initEquipMap() {
	// Map<String, Equipment> map = new HashMap<String, Equipment>();
	// List<EquipmentData> equipList =
	// TemplateManager.getTemplateList(EquipmentData.class);
	// if (equipList != null) {
	// for (EquipmentData data : equipList) {
	// if (data != null) {
	// Equipment equipment = new Equipment();
	// int templateID = data.getPid();
	// int strengLevel = 1;
	// long equipID = this.getEquipID();
	// Map<String, Double> prototype = data.getProperty();
	// equipment.setTemplateID(templateID);
	// equipment.setPrototype(prototype);
	// equipment.setStrengLevel(strengLevel);
	// equipment.setEquipID(equipID);
	// map.put(equipID + "", equipment);
	// }
	// }
	// }
	// return map;
	// }

	/**
	 * 初始化装备碎片
	 * */
//	private Map<Integer, EquipmentShard> initEquipShard() {
//		Map<Integer, EquipmentShard> map = new HashMap<Integer, EquipmentShard>();
//		List<EquipmentShardData> shardList = TemplateManager.getTemplateList(EquipmentShardData.class);
//		for (EquipmentShardData data : shardList) {
//			EquipmentShard shard = new EquipmentShard();
//			shard.setNum(100);
//			shard.setShardID(data.getPid());
//			map.put(data.getPid(), shard);
//		}
//		return map;
//	}

	/**
	 * 获取装备实体
	 * */
	public EquipmentMapEntity getEquipmentMapEntity(String playerID) {
		EquipmentMapEntity entity = equipmentEntityDAO.getEntity(playerID);
		return entity;
	}

	/**
	 * 获取装备实体
	 * */
	public EquipmentMapEntity getEquipmentMapEntity(String playerID, IOMessage ioMessage) {
		EquipmentMapEntity entity = null;
		if (ioMessage != null) {
			entity = (EquipmentMapEntity) ioMessage.getInputParse().get(EquipmentMapEntity.class.getName() + playerID);
			if (entity == null) {
				entity = this.getEquipmentMapEntity(playerID);
				ioMessage.getInputParse().put(EquipmentMapEntity.class.getName() + playerID, entity);
			}
		} else {
			entity = this.getEquipmentMapEntity(playerID);
		}
		return entity;
	}

	/**
	 * 保存装备实体
	 * */
	public void saveEquipmentEntity(EquipmentMapEntity entity) {
		equipmentEntityDAO.save(entity);
	}

	/**
	 * 初始化装备ID
	 * */
	private void initEquipmentID() {
		String clsName = SysConstants.equipIDEntity;
		KeyGenerator keyGenerator = keyGeneratorDAO.getEntity(clsName);
		if (keyGenerator == null) {
			keyGenerator = new KeyGenerator();
			keyGenerator.setKey(clsName);
			keyGenerator.setNextId(SysConstants.equipStartID);
			keyGeneratorDAO.save(keyGenerator);
		}
	}

	/**
	 * 获取装备唯一ID
	 * */
	public long getEquipID() {
		String clsName = SysConstants.equipIDEntity;
		long equipID = keyGeneratorDAO.updateInc(clsName);
		return equipID;
	}

	/**
	 * 添加装备
	 * 
	 * @param playerID
	 *            String 玩家ID
	 * @param templateID
	 *            int 装备模板ID
	 * @param num
	 *            int 装备数量
	 * */
	public void addEquipment(String playerID, int templateID, int num) {
		EquipmentMapEntity entity = this.getEquipmentMapEntity(playerID);
		Map<String, Equipment> equipMap = entity.getEquipMap();
		long equipID = this.getEquipID();
		Equipment equipment = new Equipment();
		equipment.setTemplateID(templateID);
		equipment.setEquipID(equipID);
		EquipmentData data = TemplateManager.getTemplateData(templateID, EquipmentData.class);
		equipment.setStrengLevel(1);
		equipment.setPrototype(data.getProperty());
		equipMap.put(equipID + "", equipment);
		this.saveEquipmentEntity(entity);
	}
	
	public Equipment getInitEquipment(int templateID, long equipID){
		Equipment equipment = new Equipment();
		equipment.setTemplateID(templateID);
		equipment.setEquipID(equipID);
		EquipmentData data = TemplateManager.getTemplateData(templateID, EquipmentData.class);
		equipment.setStrengLevel(1);
		equipment.setPrototype(data.getProperty());
		return equipment;
	}

	/**
	 * 添加装备
	 * */

	public Equipment addEquipment(String playerID, int templateID, Boolean isSave, IOMessage ioMessage) {
		EquipmentData data = TemplateManager.getTemplateData(templateID, EquipmentData.class);
		EquipmentMapEntity entity = this.getEquipmentMapEntity(playerID, ioMessage);
		Map<String, Equipment> equipMap = entity.getEquipMap();
//		if (equipMap.size() > entity.getMaxEquipNum()) {
//			throw new IllegalArgumentException(ErrorIds.EquipmentShardBagFull + "");
//		}
		long equipID = this.getEquipID();
		Equipment equipment = new Equipment();
		equipment.setTemplateID(templateID);
		equipment.setEquipID(equipID);
		equipment.setStrengLevel(1);
		Map<String, Double> prop = new HashMap<>();
		for (Entry<String, Double> entry : data.getProperty().entrySet()) {
			prop.put(entry.getKey(), entry.getValue());
		}
		equipment.setPrototype(prop);
		equipMap.put(equipID + "", equipment);
		if (isSave) {
			this.saveEquipmentEntity(entity);
		}
		AchievementModule acModule = ModuleManager.getModule(ModuleNames.AchievementModule, AchievementModule.class);
		int actionType = 0;
		int quality = data.getQuality();
		int mainTaskType = 0;
		if (quality == greenType) {
			actionType = ActionType.GETGREENEQUIPMENT;
		} else if (quality == blueType) {
			actionType = ActionType.GETBLUEEQUIPMENT;
			mainTaskType = ActionType.GETBLUEEQUIPMENTNUM;
		} else if (quality == purpleType) {
			actionType = ActionType.GETPURPLEEQUIPMENT;
		}
		acModule.refreshAchievement(playerID, actionType, 1);
		if(mainTaskType != 0){
			MainTaskModule mainTaskModule = ModuleManager.getModule(ModuleNames.MainTaskModule,MainTaskModule.class);
			mainTaskModule.updateTaskByActionType(playerID, mainTaskType, 0, ioMessage);
		}
		return equipment;
	}

	/**
	 * 碎片合成
	 * */
	public void composeEquipShard(String playerID, int shardID, EquipmentProtocol protocol, IOMessage ioMessage) {
		EquipmentMapEntity entity = this.getEquipmentMapEntity(playerID, ioMessage);
		Map<Integer, EquipmentShard> shardList = entity.getShardList();
		EquipmentShard shard = shardList.get(shardID);
		if (shard == null) {
			logger.error("碎片未找到");
			protocol.setCode(ErrorIds.EquipmentShardNotFound);
			return;
		}
		EquipmentShardData data = TemplateManager.getTemplateData(shardID, EquipmentShardData.class);
		int composeNum = data.getComposeNum();
		int nowNum = shard.getNum() - composeNum;
		if (nowNum < 0) {
			logger.error("碎片不足");
			protocol.setCode(ErrorIds.EquipmentShardNotEnough);
			return;
		}
		int targetID = data.getTargetID();
		Map<String, Object> itemMap = new HashMap<String, Object>();
		RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
		rewardModule.addGoods(playerID, targetID, 1, null, false, null, itemMap, ioMessage);
		shard.setNum(nowNum);
		if (nowNum == 0) {
			shardList.remove(shardID);
		}
		this.saveEquipmentEntity(entity);
		protocol.setItemMap(itemMap);
		protocol.setShard(shard);
		JSONObject params = new JSONObject();
		params.put("equipItemId", targetID);
		AnalyseModule analyseModule = ModuleManager.getModule(ModuleNames.AnalyseModule, AnalyseModule.class);
		analyseModule.analyLog(playerID, "COMPOUND_EQUIP", params, null, null, null);
		
	}

	/**
	 * 出售装备
	 * 
	 * @param playerID
	 *            String 玩家ID
	 * @param heroID
	 *            long 英雄唯一ID
	 * @param equipID
	 *            long 装备的唯一ID
	 * */
	public void sellEquipment(String playerID, List<Object> equipIDList, EquipmentProtocol protocol) {
		EquipmentMapEntity equipmenEntity = this.getEquipmentMapEntity(playerID);
		Map<String, Equipment> equipmentMap = equipmenEntity.getEquipMap();
		int price = 0;
		for (Object equipID : equipIDList) {
			Equipment equipment = equipmentMap.get(equipID.toString());
			if (equipment == null) {
				protocol.setCode(ErrorIds.EquipNotFound);
				logger.error("装备不存在");
				return;
			}
			if (equipment.isEquiped()) {
				logger.error("已装备的不可出售");
				protocol.setCode(ErrorIds.DontSellEquiped);
				return;
			}
			int templateID = equipment.getTemplateID();
			EquipmentData data = TemplateManager.getTemplateData(templateID, EquipmentData.class);
			int quality = data.getQuality();
			if (quality > 3) {
				logger.error("该品质装备不可出售");
				protocol.setCode(ErrorIds.EquipQualityNotSell);
				return;
			}
			price += data.getPrice();
			price += equipment.getStrengSilver();
			equipmentMap.remove(equipID + "");
		}
		this.saveEquipmentEntity(equipmenEntity);
		RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
		Map<String, Object> itemMap = new HashMap<String, Object>();
		Map<String,GoodsBean> showMap = new HashMap<String,GoodsBean>();
		rewardModule.addGoods(playerID, KindIDs.SILVERTYPE, price, null, true, showMap, itemMap, null);
		// protocol.setRemoveList(equipIDList);
		itemMap.put("removeEquipmentList", equipIDList);
		protocol.setShowMap(showMap);
		protocol.setItemMap(itemMap);
	}

	/**
	 * 移除装备
	 * 
	 * */

	public long removeEquipment(String playerID, int templateID, boolean isSave, IOMessage ioMessage) {
		EquipmentMapEntity equipmenEntity = this.getEquipmentMapEntity(playerID, ioMessage);
		Map<String, Equipment> equipmentMap = equipmenEntity.getEquipMap();
		if (equipmentMap.isEmpty()) {
			logger.error("装备背包为空");
			throw new IllegalArgumentException(ErrorIds.EquipmentBagIsEmpty + "");
		}
		Set<Entry<String, Equipment>> entrySet = equipmentMap.entrySet();
		long equipmentID = 0;
		for (Entry<String, Equipment> entry : entrySet) {
			Equipment equipment = entry.getValue();
			if (equipment.getTemplateID() == templateID && equipment.getHeroID() == 0 && equipment.getStrengLevel() != 0) {
				equipmentID = equipment.getEquipID();
				break;
			}
		}
		if (equipmentID != 0) {
			equipmentMap.remove(equipmentID + "");
		} else {
			logger.error("未找到该装备");
			throw new IllegalArgumentException(ErrorIds.EquipNotFound + "");
		}
		if (isSave) {
			this.saveEquipmentEntity(equipmenEntity);
		}
		return equipmentID;
	}

	/**
	 * 穿装备
	 * 
	 * @param playerID
	 *            String 玩家ID
	 * @param heroID
	 *            long 英雄唯一ID
	 * @param equipID
	 *            long 装备的唯一ID
	 * */
	public void doEquip(String playerID, long heroID, long equipID, EquipmentProtocol protocol,IOMessage ioMessage) {
		EquipmentMapEntity equipmenEntity = this.getEquipmentMapEntity(playerID,ioMessage);
		Map<String, Equipment> equipmentMap = equipmenEntity.getEquipMap();
		Equipment equipment = equipmentMap.get(equipID + "");
		if (equipment == null) {
			protocol.setCode(ErrorIds.EquipNotFound);
			logger.error("装备不存在");
			return;
		}
		int templateID = equipment.getTemplateID();
		EquipmentData data = TemplateManager.getTemplateData(templateID, EquipmentData.class);
		int part = data.getPart();
		HeroModule heroModule = ModuleManager.getModule(ModuleNames.HeroModule, HeroModule.class);
		HeroEntity heroEntity = heroModule.getHeroEntity(playerID);
		Map<String, Hero> heroMap = heroEntity.getHeroMap();
		Hero hero = heroMap.get(heroID + "");
		if (hero == null) {
			protocol.setCode(ErrorIds.HeroNotFound);
			logger.error("英雄未找到");
			return;
		}
		Map<String, Long> heroEquipMap = hero.getEquipMap();
		if (!heroEquipMap.isEmpty()) {
			if (heroEquipMap.get(part + "") != null) {
				long oldEquipID = heroEquipMap.get(part + "");
				if (oldEquipID != equipID) {
					if (oldEquipID != 0) {
						Equipment oldEquipment = equipmenEntity.getEquipMap().get(oldEquipID + "");
						oldEquipment.setEquiped(false);
						oldEquipment.setHeroID(0l);
						protocol.setUnEquipment(oldEquipment);
					}
				} else {
					logger.error("不可穿戴同一件装备");
					protocol.setCode(ErrorIds.DontSameEquip);
					return;
				}
			}
		}
		boolean isEquiped = equipment.isEquiped();
		if (isEquiped) {
			long oldHeroID = equipment.getHeroID();
			if (oldHeroID != heroID) {
				Hero oldHero = heroMap.get(oldHeroID + "");
				if (oldHero == null) {
					logger.error("英雄未找到");
					protocol.setCode(ErrorIds.HeroNotFound);
					return;
				}
				Map<String, Long> oldHeroEquipMap = oldHero.getEquipMap();
				oldHeroEquipMap.put(part + "", 0l);
				List<Integer> equipmentList = this.getHeroEquipmentList(playerID, oldHero,ioMessage);
				this.changeEquipmentFriendship(playerID, oldHero,equipmentList);
				this.changeEquipmentSet(equipmentList, oldHero);
				protocol.setUnEquipHero(oldHero);
			} else {
				logger.error("不可穿戴同一件装备");
				protocol.setCode(ErrorIds.DontSameEquip);
				return;
			}
		} else {
			equipment.setEquiped(true);
		}
		equipment.setHeroID(heroID);
		heroEquipMap.put(part + "", equipID);
		List<Integer> equipmentList = this.getHeroEquipmentList(playerID, hero,ioMessage);
		this.changeEquipmentFriendship(playerID, hero,equipmentList);
		this.changeEquipmentSet(equipmentList, hero);
		heroModule.saveHeroEntity(heroEntity);
		this.saveEquipmentEntity(equipmenEntity);
		protocol.setEquipHero(hero);
		protocol.setEquipment(equipment);
	}
	
	/**
	 * 修改玩家套装信息
	 * */
	public void changeEquipmentSet(List<Integer> equipList, Hero hero ){
		Map<Integer,Integer> setList = new HashMap<>();
		for(Integer pid : equipList){
			EquipmentData equipmentData = TemplateManager.getTemplateData(pid, EquipmentData.class);
			int setID = equipmentData.getSetID();
			if(setID != 0){
				if(setList.containsKey(setID)){
					setList.put(setID,setList.get(setID) + 1);
				}else{
					setList.put(setID, 1);
				}
			}
		}
		Map<Integer,Integer> setPropData = new HashMap<>();
		for(Entry<Integer,Integer> entry : setList.entrySet()){
			int key = entry.getKey();
			EquipmentSetData setData = TemplateManager.getTemplateData(key,EquipmentSetData.class);
			int value  = entry.getValue();
			if(value >= 2){
				Map<Integer,Integer> prop = setData.getTwoPart();
				this.addPrototype(setPropData, prop);
			}
			if(value >= 3){
				Map<Integer,Integer> prop = setData.getThreePart();
				this.addPrototype(setPropData, prop);
			}
			if(value >= 4){
				Map<Integer,Integer> prop = setData.getFourPart();
				this.addPrototype(setPropData, prop);
			}
		}
		hero.setEquipmentSetProp(setPropData);
	}
	
	private void addPrototype(Map<Integer,Integer> propMap, Map<Integer,Integer> prop){
		for(Entry<Integer,Integer> temp : prop.entrySet()){
			int addKey = temp.getKey();
			int addValue = temp.getValue();
			if(propMap.containsKey(addKey)){
				propMap.put(addKey, propMap.get(addKey) + addValue);
			}else{
				propMap.put(addKey, addValue);
			}
		}
	}
	
	/**
	 * 修改英雄的装备羁绊
	 * */
//	private void changeFriendShip(int equipmentID,Hero hero){
//		int heroTemplateID = hero.getTemplateID();
//		HeroData heroData = TemplateManager.getTemplateData(heroTemplateID, HeroData.class);
//		List<Integer> friendShipList = heroData.getFriendship();
//		for(int friendShipID : friendShipList){
//			FriendShipData friendShipData = TemplateManager.getTemplateData(friendShipID,FriendShipData.class);
//			List<Integer> activeLimit = friendShipData.getActiveLimit();
//			boolean complete = true;
//			if(equipmentID != activeLimit.get(0)){
//				complete = false;
//			}
//			if(complete){
//				Map<Integer, Double> property = friendShipData.getProperty();
//				for (Entry<Integer, Double> entry : property.entrySet()) {
//					double value = entry.getValue();
//					int key = entry.getKey();
//					if (heroFriendshipProp.containsKey(key)) {
//						heroFriendshipProp.put(key, heroFriendshipProp.get(key) + value);
//					} else {
//						heroFriendshipProp.put(key, value);
//					}
//				}
//			}
//		}
//	}


	/**
	 * 卸载装备
	 * 
	 * @param playerID
	 *            String 玩家ID
	 * @param equipID
	 *            long 装备的唯一ID
	 * */
	public void unEquip(String playerID, long equipID, EquipmentProtocol protocol,IOMessage ioMessage) {
		EquipmentMapEntity equipmenEntity = this.getEquipmentMapEntity(playerID,ioMessage);
		Map<String, Equipment> equipmentMap = equipmenEntity.getEquipMap();
		Equipment equipment = equipmentMap.get(equipID + "");
		if (!equipment.isEquiped()) {
			logger.error("装备未装备");
			protocol.setCode(ErrorIds.UnEquip);
			return;
		}
		long heroID = equipment.getHeroID();
		HeroModule heroModule = ModuleManager.getModule(ModuleNames.HeroModule, HeroModule.class);
		HeroEntity heroEntity = heroModule.getHeroEntity(playerID);
		Hero hero = heroEntity.getHeroMap().get(heroID + "");
		if (hero == null) {
			logger.error("英雄未找到");
			protocol.setCode(ErrorIds.HeroNotFound);
			return;
		}
		Map<String, Long> heroEquipMap = hero.getEquipMap();
		equipment.setEquiped(false);
		int templateID = equipment.getTemplateID();
		EquipmentData data = TemplateManager.getTemplateData(templateID, EquipmentData.class);
		int part = data.getPart();
		heroEquipMap.put(part + "", 0l);
		equipment.setHeroID(0l);
		List<Integer> equipmentList = this.getHeroEquipmentList(playerID, hero,ioMessage);
		this.changeEquipmentFriendship(playerID, hero,equipmentList);
		this.changeEquipmentSet(equipmentList, hero);
		this.saveEquipmentEntity(equipmenEntity);
		heroModule.saveHeroEntity(heroEntity);
		protocol.setEquipHero(hero);
		protocol.setEquipment(equipment);
	}

	/**
	 * 精炼装备
	 * 
	 * @param playerID
	 *            String 玩家ID
	 * @param equipID
	 *            long 装备ID
	 * @param type
	 *            int 强化类型
	 * */
	public void refineEquip(String playerID, long equipID, int type, int refineNum, EquipmentProtocol protocol, IOMessage ioMessage) {
		if(refineNum < 0 ){
			throw new IllegalArgumentException(ErrorIds.ParamWrong + "");
		}
		EquipmentMapEntity equipEntity = this.getEquipmentMapEntity(playerID, ioMessage);
		Map<String, Equipment> equipmentMap = equipEntity.getEquipMap();
		Equipment equipment = equipmentMap.get(equipID + "");
		if (equipment == null) {
			protocol.setCode(ErrorIds.EquipNotFound);
			logger.error("装备不存在");
			return;
		}
		int templateID = equipment.getTemplateID();
		List<GoodsBean> goodsList = new ArrayList<GoodsBean>();
		GoodsBean stoneBean = new GoodsBean();
		stoneBean.setPid(SysConstants.refineStoneID);
		stoneBean.setNum(SysConstants.refineStartStone * refineNum);
		switch (type) {
		case RefineType.stone:
			stoneBean.setNum(2*refineNum);
			break;
		case RefineType.silver:
			GoodsBean silverBean = new GoodsBean();
			silverBean.setPid(KindIDs.SILVERTYPE);
			silverBean.setNum(SysConstants.refineSilver * refineNum);
			goodsList.add(silverBean);
			break;
		case RefineType.gold:
			GoodsBean goldBean = new GoodsBean();
			goldBean.setPid(KindIDs.GOLDTYPE);
			goldBean.setNum(SysConstants.refineGold * refineNum);
			goodsList.add(goldBean);
			break;
		default:
			logger.error("错误的精炼类型");
			protocol.setCode(ErrorIds.WrongRefineType);
			return;
		}
		goodsList.add(stoneBean);

		Map<String, Double> equipRefine = equipment.getRefine();
		Map<String,Double> showRefine = equipment.getRefineShow();
		Map<String, Double> changeRefine = new HashMap<>();
		changeRefine.putAll(equipRefine);
		if(showRefine != null && !showRefine.isEmpty()){
			for(Entry<String,Double> entrySet : showRefine.entrySet()){
				String key = entrySet.getKey();
				double value = entrySet.getValue();
				if(changeRefine.get(key) != null){
					changeRefine.put(key, changeRefine.get(key) + value);
				}else{
					changeRefine.put(key, value);
				}
			}
		}
		
		Map<Integer, Double> refine = this.getRefineMaxProp(equipment.getStrengLevel(), templateID);
		this.refine(type, refineNum, changeRefine, refine);
		Set<Entry<String, Double>> entrySet = changeRefine.entrySet();
		Map<String, Double> showMap = new HashMap<>();
		for (Entry<String, Double> entry : entrySet) {
			String key = entry.getKey();
			Double equipValue = 0d;
			if (equipRefine.get(key) != null) {
				equipValue = equipRefine.get(key);
			}
			double value = entry.getValue() - equipValue;
			showMap.put(key, value);
		}
		int allRefineNum = refineNum + equipment.getRefineNum();
		if (allRefineNum > Integer.MAX_VALUE) {
			allRefineNum = Integer.MAX_VALUE;
		}
		equipment.setRefineNum(allRefineNum);
		equipment.setRefineShow(showMap);
		protocol.setRefineShowMap(showMap);
		Map<String, Object> itemMap = new HashMap<>();
		RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
		int code = rewardModule.useGoods(playerID, goodsList, 0, true, null, itemMap, ioMessage);
		if (code != 0) {
			protocol.setCode(code);
			return;
		}
		protocol.setRefineShowMap(showMap);
		protocol.setItemMap(itemMap);
		DayTaskModule dayTaskModule = ModuleManager.getModule(ModuleNames.DayTaskModule, DayTaskModule.class);
		dayTaskModule.addScore(playerID, ActionType.EQUIPMENTREFINE, refineNum);
		AchievementModule acModule = ModuleManager.getModule(ModuleNames.AchievementModule, AchievementModule.class);
		acModule.refreshAchievement(playerID, ActionType.EQUIPMENTREFINENUM, refineNum);
		EquipmentData data = TemplateManager.getTemplateData(templateID, EquipmentData.class);

		JSONObject params = new JSONObject();
		params.put("quality", data.getQuality());
		AnalyseModule analyseModule = ModuleManager.getModule(ModuleNames.AnalyseModule, AnalyseModule.class);
		analyseModule.analyLog(playerID, "EQUIP_BAPTIZE", params, null, null, null);

		// ////
		// // 元宝消耗记录
		// ///
		if (type == RefineType.gold) {
			analyseModule.goldCostLog(playerID, SysConstants.refineGold * refineNum, refineNum, SysConstants.refineGold, "refineEquip", "equipment");
		}
	}

	private Map<Integer, Double> getRefineMaxProp(int equipLevel, int templateID) {
		EquipmentData data = TemplateManager.getTemplateData(templateID, EquipmentData.class);
		int quality = data.getQuality();
		Map<Integer, Double> refineMapProp = new HashMap<>();
		int base = 0;
		switch (quality) {
		case QualityType.epic:
			base = 60;
			break;
		case QualityType.legend:
			base = 90;
			break;
		default:
			throw new IllegalArgumentException(ErrorIds.EquipmentNoRefine + "");
		}
		base = (int) (((equipLevel / 20) + 0.5) * base);
		int part = data.getPart();
		double maxHp = 0;
		double maxAttack = 0;
		double maxPdef = 0;
		double maxMdef = 0;
		double maxFinalAttack = 0;
		double maxFinalDef = 0;
		switch (part) {
		case EquipmentPart.weapon:
			maxHp = 3.2;
			maxAttack = 1;
			maxFinalAttack = 0.8;
			maxFinalDef = 0.64;
			break;
		case EquipmentPart.clothes:
			maxHp = 3;
			maxPdef = 0.75;
			maxFinalAttack = 0.48;
			maxFinalDef = 0.6;
			break;
		case EquipmentPart.hat:
			maxHp = 3;
			maxMdef = 0.75;
			maxFinalAttack = 0.48;
			maxFinalDef = 0.6;
			break;
		case EquipmentPart.necklace:
			maxHp = 5;
			maxAttack = 0.8;
			maxPdef = 0.8;
			maxMdef = 0.64;
			break;
		default:
			break;
		}
		double hp = maxHp * base;
		refineMapProp.put(SysConstants.Hp, hp);
		if (maxAttack != 0) {
			double attack = maxAttack * base;
			refineMapProp.put(SysConstants.Attack, attack);
		}
		if (maxPdef != 0) {
			double pdef = maxPdef * base;
			refineMapProp.put(SysConstants.PDef, pdef);
		}
		if (maxMdef != 0) {
			double mdef = maxMdef * base;
			refineMapProp.put(SysConstants.MDef, mdef);
		}
		if (maxFinalAttack != 0) {
			double finalAttack = maxFinalAttack * base;
			refineMapProp.put(SysConstants.FinalAttack, finalAttack);
		}
		if (maxFinalDef != 0) {
			double finalDef = maxFinalDef * base;
			refineMapProp.put(SysConstants.FinalDef, finalDef);
		}
		return refineMapProp;
	}

	private void refine(int refineType, int num, Map<String, Double> refine, Map<Integer, Double> equipRefine) {
		if (num > 10) {
			throw new IllegalArgumentException(ErrorIds.EquipmentRefineNum + "");
		}
		for (int i = 1; i <= num; i++) {
			Set<Entry<Integer, Double>> entrySet = equipRefine.entrySet();
			for (Entry<Integer, Double> entry : entrySet) {
				int type = entry.getKey();
				double value = entry.getValue();
				EquipRefine eqRefine = this.refineProp.get(type).get(refineType);
				double percent = eqRefine.getPercent();
				double random = Utilities.getrandomDouble();
				if (random < percent) {
					int max = eqRefine.getMax() - eqRefine.getMin() + 1;
					int propRandom = Utilities.getRandomInt(max) + eqRefine.getMin();
					Double prop = 0d;
					if (refine.get(type + "") != null) {
						prop = refine.get(type + "");
					}
					prop += propRandom;
					if (prop < 0) {
						prop = 0d;
					}
					if (prop > value) {
						prop = value;
					}
					refine.put(type + "", prop);
				}
			}
		}
	}

	public void doRefine(String playerID, int type, long equipID, EquipmentProtocol protocol) {
		EquipmentMapEntity equipEntity = this.getEquipmentMapEntity(playerID);
		Map<String, Equipment> equipmentMap = equipEntity.getEquipMap();
		Equipment equipment = equipmentMap.get(equipID + "");
		if (equipment == null) {
			protocol.setCode(ErrorIds.EquipNotFound);
			logger.error("装备不存在");
			return;
		}
		switch (type) {
		case RefineType.accept:
			try {
				this.changeRefine(playerID, equipment);
			} catch (Exception ex) {
				protocol.setCode(Integer.parseInt(ex.getMessage()));
				return;
			}

			break;
		case RefineType.cancel:
			this.cancelRefine(equipment);
			break;
		default:
			logger.error("错误的类型");
			protocol.setCode(ErrorIds.WrongRefineType);
			return;
		}
		this.saveEquipmentEntity(equipEntity);
		protocol.setEquipment(equipment);

	}

	/**
	 * 替换精练
	 * */
	public void changeRefine(String playerID, Equipment equipment) {
		Map<String, Double> refineShow = equipment.getRefineShow();
		Map<String, Double> refine = equipment.getRefine();
		if (refineShow == null) {
			logger.error("没有可以替换的精炼属性");
			throw new IllegalArgumentException(ErrorIds.NoRefineChangeProperty + "");
		}
		for (Entry<String, Double> entry : refineShow.entrySet()) {
			String key = entry.getKey();
			Double value = entry.getValue();
			if (refine.containsKey(key)) {
				refine.put(key, refine.get(key) + value);
			} else {
				refine.put(key, value);
			}

		}
		equipment.setRefineShow(null);
	
		// 记录装备替换
		int templateID = equipment.getTemplateID();
		EquipmentData data = TemplateManager.getTemplateData(templateID, EquipmentData.class);
		JSONObject params = new JSONObject();
		params.put("quality", data.getQuality());
		AnalyseModule analyseModule = ModuleManager.getModule(ModuleNames.AnalyseModule, AnalyseModule.class);
		analyseModule.analyLog(playerID, "EQUIP_BAPTIZE_REPLACE", params, null, null, null);
	}

	/**
	 * 取消精练
	 * */
	private void cancelRefine(Equipment equipment) {
		equipment.setRefineShow(null);
	}

	/**
	 * 强化装备
	 * 
	 * @param playerID
	 *            String 玩家ID
	 * @param equipID
	 *            long 装备ID
	 * */
	public void strengEquipment(String playerID, long equipID, EquipmentProtocol protocol, IOMessage ioMessage) {
		EquipmentMapEntity equipEntity = this.getEquipmentMapEntity(playerID);
		Map<String, Equipment> equipmentMap = equipEntity.getEquipMap();
		Equipment equipment = equipmentMap.get(equipID + "");
		if(equipment == null ){
			logger.error("装备未找到");
			throw new IllegalArgumentException(ErrorIds.EquipNotFound + "");
		}
		int strengLevel = equipment.getStrengLevel();
		int strengMaxLevel = this.getMaxStrengLevel(playerID);
		if (strengLevel >= strengMaxLevel) {
			protocol.setCode(ErrorIds.EquipmentStrengLevel);
			logger.error("超过最大上限");
			return;
		}

		int templateID = equipment.getTemplateID();
		EquipmentData data = TemplateManager.getTemplateData(templateID, EquipmentData.class);
		int quality = data.getQuality();
		//int part = data.getPart();
		int silver = this.strengSilver(strengLevel, quality);
		RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
		Map<String, Object> itemMap = new HashMap<String, Object>();
		equipment.setStrengSilver(equipment.getStrengSilver() + silver);
		int code = rewardModule.useGoods(playerID, KindIDs.SILVERTYPE, silver, 0, true, null, itemMap, null);
		if (code != 0) {
			protocol.setCode(code);
			return;
		}
		int upLevel = this.randomStrengLevel(ioMessage);
		equipment.setStrengLevel(strengLevel + upLevel);
		Map<String, Double> prototype = equipment.getPrototype();
		Map<String, Double> intensify = data.getIntensify();
		for (Entry<String, Double> entry : intensify.entrySet()) {
			String key = entry.getKey();
			double value = entry.getValue();
			if(prototype.get(key) != null){
				prototype.put(key, prototype.get(key) + value * upLevel);
			}else{
				prototype.put(key, value * upLevel);
			}
		}
		this.saveEquipmentEntity(equipEntity);
		List<Integer> returnList = new ArrayList<Integer>();
		returnList.add(upLevel);
		protocol.setItemMap(itemMap);
		protocol.setEquipment(equipment);
		protocol.setStrengList(returnList);

		AchievementModule acModule = ModuleManager.getModule(ModuleNames.AchievementModule, AchievementModule.class);
		acModule.refreshAchievement(playerID, ActionType.EQUIPMENTLEVEL, equipment.getStrengLevel());
		MainTaskModule mainTaskModule = ModuleManager.getModule(ModuleNames.MainTaskModule,MainTaskModule.class);
		mainTaskModule.updateTaskByActionType(playerID, ActionType.EQUIPMENTLEVEL, equipment.getStrengLevel(), ioMessage);
		@SuppressWarnings("unchecked")
		Map<String, Object> wallet = (Map<String, Object>) itemMap.get("WalletEntity");
		this.analyseEquipmentSteng(playerID, templateID, equipment.getStrengLevel() - upLevel, equipment.getStrengLevel(), silver,
				Long.parseLong(wallet.get("silver").toString()), quality);
	}

	private void analyseEquipmentSteng(String playerID, int templateID, int oldLevel, int curLevel, long costSilver, long afterSilver, int quality) {
		// 统计日志所需参数
		JSONObject params = new JSONObject();
		params.put("equipId", templateID);
		params.put("oldLevel", oldLevel);
		params.put("curLevel", curLevel);
		params.put("costSilver", costSilver);
		params.put("afterSilver", afterSilver);
		params.put("quality", quality);
		AnalyseModule analyseModule = ModuleManager.getModule(ModuleNames.AnalyseModule, AnalyseModule.class);
		// 记录统计日志
		analyseModule.analyLog(playerID, "EQUIP_EHANCE", params, null, null, null);
	}

	/** 计算当前强化等级的金钱 */
	private int strengSilver(int level, int quality) {
		Map<Integer,Integer> map = upMap.get(level);
		if(map != null){
			int money = map.get(quality);
			return money;
		}else{
			logger.error("该等级的强化消耗为空");
			throw new IllegalArgumentException(ErrorIds.noEquipmentUpData + "");
		}
//		int pos = this.getStrengPos(part);
//		quality = this.getQuality(quality);
//		return quality * level * level * pos;
	}

	private int getQuality(int quality) {
		int num;
		switch (quality) {
		case QualityType.noraml:
			num = 1;
			break;
		case QualityType.excellent:
			num = 2;
			break;
		case QualityType.superior:
			num = 4;
			break;
		case QualityType.epic:
			num = 6;
			break;
		case QualityType.legend:
			num = 8;
			break;
		default:
			throw new IllegalArgumentException(ErrorIds.WrongStrengType + "");
		}
		return num;
	}

	private int getStrengPos(int part) {
		int pos = 1000000;
		switch (part) {
		case EquipmentPart.weapon:
			pos = 5;
			break;
		case EquipmentPart.clothes:
			pos = 3;
			break;
		case EquipmentPart.hat:
			pos = 3;
			break;
		case EquipmentPart.necklace:
			pos = 4;
			break;
		case EquipmentPart.horse:
			pos = 4;
			break;
		default:
			throw new IllegalArgumentException(ErrorIds.WrongStrengType + "");
		}
		return pos;
	}

	/**
	 * 强化随机等级
	 * */
	private int randomStrengLevel(IOMessage ioMessage) {
		int maxLevelTemp = 0;
		int minLevelTemp = 0;
		if (ioMessage.getInputParse("maxLevel") != null) {
			maxLevelTemp = (int) ioMessage.getInputParse("maxLevel");
		}
		if (ioMessage.getInputParse("minLevel") != null) {
			minLevelTemp = (int) ioMessage.getInputParse("minLevel");
		}
		if (maxLevelTemp == 0 || minLevelTemp == 0) {
			// 强化上下限关联vip等级
			VipModule vipModule = ModuleManager.getModule(ModuleNames.VipModule, VipModule.class);
			maxLevelTemp = vipModule.getPermissionValue(ioMessage.getPlayerId(), SysConstants.STRENG_MAX);
			minLevelTemp = vipModule.getPermissionValue(ioMessage.getPlayerId(), SysConstants.STRENG_MIN);
			ioMessage.getInputParse().put("maxLevel", maxLevelTemp);
			ioMessage.getInputParse().put("minLevel", minLevelTemp);
		}
		int upLevel = 0;
		upLevel = Utilities.getRandomInt(maxLevelTemp - minLevelTemp + 1) + minLevelTemp;
		return upLevel;
	}

	/**
	 * 强化消耗
	 * 
	 * */

	private int getMaxStrengLevel(String playerID) {
		HeroModule heroModule = ModuleManager.getModule(ModuleNames.HeroModule, HeroModule.class);
		Hero lead = heroModule.getLead(playerID, null);
		int level = lead.getLevel();
		return level * 2;
	}

	/**
	 * 自动强化装备
	 * 
	 * @param playerID
	 *            String 玩家ID
	 * @param equipID
	 *            long 装备ID
	 * */
	public void autoStreng(String playerID, long equipID, EquipmentProtocol protocol, IOMessage ioMessage) {
		EquipmentMapEntity equipEntity = this.getEquipmentMapEntity(playerID);
		Map<String, Equipment> equipmentMap = equipEntity.getEquipMap();
		Equipment equipment = equipmentMap.get(equipID + "");
		int maxStrengLevel = this.getMaxStrengLevel(playerID);
		if(equipment.getStrengLevel() > maxStrengLevel){
			throw new IllegalArgumentException(ErrorIds.EquipmentStrengLevel + "");
		}
		int nowLevel = equipment.getStrengLevel();
		int diff = maxStrengLevel - nowLevel;
		int sum = 0;
		int templateID = equipment.getTemplateID();
		EquipmentData data = TemplateManager.getTemplateData(templateID, EquipmentData.class);
		int quality = data.getQuality();
	//	int part = data.getPart();
		List<Integer> strengList = new ArrayList<Integer>();
		for (int i = 0; i < 9999; i++) {
			int random = this.randomStrengLevel(ioMessage);
			strengList.add(random);
			sum += random;
			if (sum >= diff) {
				break;
			}
		}
		WalletModule walletModule = ModuleManager.getModule(ModuleNames.WalletModule, WalletModule.class);
		WalletEntity walletEntity = walletModule.getwalletEntity(playerID);
		long silver = walletEntity.getSilver();
		int consumeSilver = 0;
		sum = 0;
		List<Integer> returnList = new ArrayList<Integer>();
		for (int temp : strengList) {
			int tempSilver = this.strengSilver(nowLevel + sum, quality);
			consumeSilver += tempSilver;
			if (consumeSilver > silver) {
				consumeSilver -= tempSilver;
				break;
			}
			returnList.add(temp);
			sum += temp;
		}
		if (returnList.isEmpty()) {
			protocol.setCode(ErrorIds.NotEnoughSilver);
			return;
		}
		RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
		Map<String, Object> itemMap = new HashMap<String, Object>();
		int code = rewardModule.useGoods(playerID, KindIDs.SILVERTYPE, consumeSilver, 0, true, null, itemMap, null);
		if (code != 0) {
			protocol.setCode(code);
			return;
		}
		Map<String, Double> prototype = equipment.getPrototype();
		Map<String, Double> intensify = data.getIntensify();
		for (Entry<String, Double> entry : intensify.entrySet()) {
			String key = entry.getKey();
			double value = entry.getValue();
			prototype.put(key, prototype.get(key) + value * sum);
		}
		equipment.setStrengSilver(equipment.getStrengSilver() + consumeSilver);
		equipment.setStrengLevel(nowLevel + sum);
		this.saveEquipmentEntity(equipEntity);
		protocol.setItemMap(itemMap);
		protocol.setEquipment(equipment);
		protocol.setStrengList(returnList);
		AchievementModule acModule = ModuleManager.getModule(ModuleNames.AchievementModule, AchievementModule.class);
		acModule.refreshAchievement(playerID, ActionType.EQUIPMENTLEVEL, equipment.getStrengLevel());
		MainTaskModule mainTaskModule = ModuleManager.getModule(ModuleNames.MainTaskModule,MainTaskModule.class);
		mainTaskModule.updateTaskByActionType(playerID, ActionType.EQUIPMENTLEVEL, equipment.getStrengLevel(), ioMessage);
		@SuppressWarnings("unchecked")
		Map<String, Object> wallet = (Map<String, Object>) itemMap.get("WalletEntity");
		this.analyseEquipmentSteng(playerID, templateID, equipment.getStrengLevel() - sum, equipment.getStrengLevel(), silver,
				Long.parseLong(wallet.get("silver").toString()), quality);

	}

	/**
	 * 一键装备
	 * 
	 * @param playerID
	 *            String 玩家ID
	 * @param heroID
	 *            long 英雄ID
	 * @param protocol
	 *            EquipmentProtocol 装备通信协议
	 * */
	public void autoEquip(String playerID, long heroID, EquipmentProtocol protocol,IOMessage ioMessage) {
		EquipmentMapEntity equipmentMapEntity = this.getEquipmentMapEntity(playerID,ioMessage);
		Map<String, Equipment> equips = equipmentMapEntity.getEquipMap();
		HeroModule heroModule = ModuleManager.getModule(ModuleNames.HeroModule, HeroModule.class);
		HeroEntity heroEntity = heroModule.getHeroEntity(playerID);
		Hero hero = heroEntity.getHeroMap().get(heroID + "");
		if (hero == null) {
			logger.error("英雄未找到");
			protocol.setCode(ErrorIds.HeroNotFound);
			return;
		}
		Map<Long,Equipment> equipList = new HashMap<>();
		Map<String, Long> equipdMap = hero.getEquipMap();
		Map<Integer, Integer> aptitudeMap = new HashMap<>();
		for (Entry<String, Equipment> entry : equips.entrySet()) {
			Equipment equipment = entry.getValue();
			if (equipment.getHeroID() == 0 || equipment.getHeroID() == heroID) {
				int templateID = equipment.getTemplateID();
				EquipmentData data = TemplateManager.getTemplateData(templateID, EquipmentData.class);
				int aptitude = data.getAptitude();
				int part = data.getPart();

				if (aptitudeMap.get(part) == null) {
					aptitudeMap.put(part, aptitude);
					equipdMap.put(part + "", equipment.getEquipID());
				}else {
				if (aptitudeMap.get(part) < aptitude) {
					long changeID = equipdMap.get(part + "");
					Equipment changeEquipment = equips.get(changeID + "");
					changeEquipment.setEquiped(false);
					changeEquipment.setHeroID(0l);
					equipList.put(changeEquipment.getEquipID(),changeEquipment);
					aptitudeMap.put(part, aptitude);
					equipdMap.put(part + "", equipment.getEquipID());
				}else
				if(aptitudeMap.get(part) == aptitude){
					int tempLevel = equipment.getStrengLevel();
					long equipedID = equipdMap.get(part + "");
					Equipment tempEquipment = equips.get(equipedID + "");
					int strengLevel = tempEquipment.getStrengLevel();
					if(tempLevel > strengLevel){
						equipdMap.put(part + "", equipment.getEquipID());
						if(tempEquipment.getHeroID() != 0){
							tempEquipment.setHeroID(0);
							tempEquipment.setEquiped(false);
							equipList.put(tempEquipment.getEquipID(),tempEquipment);
						}
					}
				}
			}
			if (equipment.getHeroID() == heroID) {
				if (equipdMap.get(part + "") != equipment.getEquipID()) {
					equipment.setEquiped(false);
					equipment.setHeroID(0l);
					equipList.put(equipment.getEquipID(),equipment);
				}
			}
		}
	}
		for (Entry<String, Long> entry : equipdMap.entrySet()) {
			long equipID = entry.getValue();
			String part = entry.getKey();
			int temp = Integer.parseInt(part);
			if (temp != EquipmentPart.horse && temp != EquipmentPart.book) {
				Equipment equipment = equips.get(equipID + "");
				if (equipment != null) {
					equipment.setHeroID(heroID);
					equipment.setEquiped(true);
					equipList.put(equipment.getEquipID(),equipment);
				}
			}
		}
		List<Integer> equipmentList = this.getHeroEquipmentList(playerID, hero,ioMessage);
		this.changeEquipmentFriendship(playerID, hero,equipmentList);
		this.changeEquipmentSet(equipmentList, hero);
		TalismanModule talismanModule = ModuleManager.getModule(ModuleNames.TalismanModule, TalismanModule.class);
		talismanModule.autoEquip(playerID, heroID, equipdMap, protocol);
		talismanModule.changeTalismanFriendship(playerID, hero);
		this.saveEquipmentEntity(equipmentMapEntity);
		heroModule.saveHeroEntity(heroEntity);
		protocol.setEquipHero(hero);
		protocol.setEquipList(equipList);
	
	}

	/**
	 * 扩大装备背包
	 * */
	public void expandEquipmentBag(String playerID, IOMessage ioMessage, EquipmentProtocol protocol) {
		EquipmentMapEntity entity = this.getEquipmentMapEntity(playerID);
		int equipNum = entity.getMaxEquipNum();
		/** 临时 */
		if (equipNum == 0) {
			logger.error("临时修改装备格子数");
			equipNum = SysConstants.bagInitNum;
		}
		int n = (equipNum - SysConstants.bagInitNum) / SysConstants.bagSellAddNum + 1;
		int code = 0;
		if (n < 0) {
			protocol.setCode(ErrorIds.BagNumWrong);
			return;
		}
		int price = n * 25;
		RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
		Map<String, Object> itemMap = new HashMap<String, Object>();
		code = rewardModule.useGoods(playerID, KindIDs.GOLDTYPE, price, 0, true, null, itemMap, ioMessage);
		if (code != 0) {
			protocol.setCode(code);
			return;
		}
		int nowNum = equipNum + SysConstants.bagSellAddNum;
		entity.setMaxEquipNum(nowNum);
		this.saveEquipmentEntity(entity);
		protocol.setItemMap(itemMap);
		protocol.setMaxEquipNum(nowNum);

		// ////
		// // 元宝消耗记录
		// ///
		AnalyseModule analyseModule = ModuleManager.getModule(ModuleNames.AnalyseModule, AnalyseModule.class);
		analyseModule.goldCostLog(playerID, price, 1, price, "expandEquipmentBag", "equipment");

	}

	/**
	 * 扩大装备碎片背包
	 * */
	public void expandEquipmentShardBag(String playerID, IOMessage ioMessage, EquipmentProtocol protocol) {
		EquipmentMapEntity entity = this.getEquipmentMapEntity(playerID);
		int equipNum = entity.getMaxFragmentNum();
		/** 临时 */
		if (equipNum == 0) {
			logger.error("临时修改装备格子数");
			equipNum = SysConstants.fragmentNum;
		}
		int n = (equipNum - SysConstants.fragmentNum) / SysConstants.bagSellAddNum + 1;
		int code = 0;
		if (n < 0) {
			protocol.setCode(ErrorIds.BagNumWrong);
			return;
		}
		int price = n * 25;
		RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
		Map<String, Object> itemMap = new HashMap<String, Object>();
		code = rewardModule.useGoods(playerID, KindIDs.GOLDTYPE, price, 0, true, null, itemMap, ioMessage);
		if (code != 0) {
			protocol.setCode(code);
			return;
		}
		int nowNum = equipNum + SysConstants.bagSellAddNum;
		entity.setMaxFragmentNum(nowNum);
		this.saveEquipmentEntity(entity);
		protocol.setItemMap(itemMap);
		protocol.setMaxShardNum(nowNum);

		// ////
		// // 元宝消耗记录
		// ///
		AnalyseModule analyseModule = ModuleManager.getModule(ModuleNames.AnalyseModule, AnalyseModule.class);
		analyseModule.goldCostLog(playerID, price, 1, price, "expandEquipmentShardBag", "equipment");
	}

	/**
	 * 出售装备碎片
	 * */
	public void sellEquipmentShard(String playerID, List<Object> sellList, EquipmentProtocol protocol) {
		EquipmentMapEntity entity = this.getEquipmentMapEntity(playerID);
		Map<Integer, EquipmentShard> list = entity.getShardList();
		int price = 0;
		for (Object temp : sellList) {
			Integer shardID = Integer.parseInt(temp.toString());
			EquipmentShard shard = list.get(shardID);
			if (shard == null) {
				logger.error("碎片未找到");
				protocol.setCode(ErrorIds.EquipmentShardNotFound);
				return;
			}
			int num = shard.getNum();
			EquipmentShardData data = TemplateManager.getTemplateData(shardID, EquipmentShardData.class);
			price += data.getPrice() * num;
			list.remove(shardID);
		}
		Map<String, Object> itemMap = new HashMap<>();
		Map<String,GoodsBean> showMap = new HashMap<>();
		RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
		rewardModule.addGoods(playerID, KindIDs.SILVERTYPE, price, null, true, showMap, itemMap, null);
		itemMap.put("removeEquipmentShardList", sellList);
		protocol.setShowMap(showMap);
		protocol.setItemMap(itemMap);
		this.saveEquipmentEntity(entity);
	}

	public List<Integer> getHeroEquipmentList(String playerID, Hero hero,IOMessage ioMessage) {
		EquipmentMapEntity equipmentMapEntity = this.getEquipmentMapEntity(playerID,ioMessage);
		Map<String, Equipment> equipmentMap = equipmentMapEntity.getEquipMap();
		Map<String, Long> equipMap = hero.getEquipMap();
		List<Integer> equipmentList = new ArrayList<>();
		for (Entry<String, Long> entry : equipMap.entrySet()) {
			long value = entry.getValue();
			Equipment equipment = equipmentMap.get(value + "");
			if (equipment != null) {
				equipmentList.add(equipment.getTemplateID());
			}
		}
		return equipmentList;
	}

	public EquipmentShard addEquipmentShard(String playerID, int pid, int num, boolean isSave, IOMessage ioMessage) {
		EquipmentMapEntity entity = this.getEquipmentMapEntity(playerID, ioMessage);
		Map<Integer, EquipmentShard> list = entity.getShardList();
		EquipmentShard shard = list.get(pid);
		if (shard == null) {
			shard = new EquipmentShard();
			shard.setShardID(pid);
			shard.setNum(num);
			list.put(pid, shard);
		} else {
			shard.setNum(shard.getNum() + num);
		}
		if (isSave) {
			this.saveEquipmentEntity(entity);
		}
		return shard;
	}

	/**
	 * 修改玩家的装备羁绊
	 * */
	public void changeEquipmentFriendship(String playerID, Hero hero,List<Integer> equipmentList) {
		int templateID = hero.getTemplateID();
		HeroData heroData = TemplateManager.getTemplateData(templateID, HeroData.class);
		List<Integer> friendShip = heroData.getFriendship();
		Map<Integer, Double> equipFriendShipProp = new HashMap<>();
		for (int friendShipID : friendShip) {
			FriendShipData friendShipData = TemplateManager.getTemplateData(friendShipID, FriendShipData.class);
			if (friendShipData.getActiveType() == FriendshipType.equipType) {
				List<Integer> activeLimit = friendShipData.getActiveLimit();
				for (int activeID : activeLimit) {
					if (equipmentList.contains(activeID)) {
						Map<Integer, Double> property = friendShipData.getProperty();
						for (Entry<Integer, Double> entry : property.entrySet()) {
							int key = entry.getKey();
							double value = entry.getValue();
							if (equipFriendShipProp.get(key) != null) {
								equipFriendShipProp.put(key, equipFriendShipProp.get(key) + value);
							} else {
								equipFriendShipProp.put(key, value);
							}
						}
					}
				}
			}
		}
		hero.setEquipFriendShipProp(equipFriendShipProp);
	}
	
	/**
	 * 自动强化所有装备
	 * */
	
	public void autoStrengAllEquipment(String playerID, long heroID, IOMessage ioMessage,EquipmentProtocol protocol){
		HeroModule heroModule = ModuleManager.getModule(ModuleNames.HeroModule,HeroModule.class);
		Hero hero = heroModule.getHero(playerID, heroID, ioMessage);
		Hero lead = heroModule.getLead(playerID, ioMessage);
		int maxLevel = lead.getLevel() * 2;
		EquipmentMapEntity equipmentMapEntity = this.getEquipmentMapEntity(playerID, ioMessage);
		Map<String,Long> equipMap = hero.getEquipMap();
		WalletModule walletModule = ModuleManager.getModule(ModuleNames.WalletModule,WalletModule.class);
		WalletEntity walletEntity = walletModule.getwalletEntity(playerID);
		long silver = walletEntity.getSilver();
		Map<String,Equipment> equipMaps = equipmentMapEntity.getEquipMap();
		List<Equipment> equipList = new ArrayList<>();
		for(int i = 0; i< 4 ; i ++){
			equipList.add(null);
		}
		for(Entry<String,Long> entry : equipMap.entrySet()){
			String key = entry.getKey();
			long value = entry.getValue();
			int part = Integer.parseInt(key);
			if(value != 0 ){
				Equipment equipment = equipMaps.get(value + "");
				if(part ==  EquipmentPart.hat){
					equipList.set(0,equipment);
				}else
				if(part == EquipmentPart.weapon){
					equipList.set(1,equipment);
				}else
				if(part == EquipmentPart.necklace){
					equipList.set(2, equipment);
				}else
				if(part == EquipmentPart.clothes){
					equipList.set(3, equipment);
				}
			}
		}
		Map<Long,Map<String,Object>> strengList = new HashMap<>();
		long useSilver = 0;
		for(int i = 0; i < MaxStrengLevel;i++){
			int size = equipList.size();
			int checkNum = 0;
			int nowSilver = 0;
			int silverNum = 0;
			for(Equipment equipment : equipList){
				if(equipment == null){
					checkNum += 1;
					silverNum += 1;
					continue;
				}
				int strengLevel = equipment.getStrengLevel();
				if(strengLevel >= maxLevel ){
					checkNum += 1;
					continue;
				}
				int templateID = equipment.getTemplateID();
				EquipmentData data = TemplateManager.getTemplateData(templateID,EquipmentData.class);
				int quality = data.getQuality();
				//int part = data.getPart();
				nowSilver = this.strengSilver(strengLevel, quality);
				useSilver += nowSilver;
				if(useSilver > silver){
					useSilver -= nowSilver;
					silverNum += 1;
					continue;
				}
				equipment.setStrengSilver(equipment.getStrengSilver() + nowSilver);
				int upLevel = this.randomStrengLevel(ioMessage);
				long equipID = equipment.getEquipID();
				Map<String,Object> map = strengList.get(equipID);
				if(map != null){
					map.put("upLevel", (int)map.get("upLevel") + upLevel);
				}else{
					map = new HashMap<>();
					map.put("equipID", equipID);
					map.put("upLevel",upLevel);
					 strengList.put(equipID,map);
				}
				equipment.setStrengLevel(strengLevel + upLevel);
				Map<String, Double> prototype = equipment.getPrototype();
				Map<String, Double> intensify = data.getIntensify();
				for (Entry<String, Double> entry : intensify.entrySet()) {
					String key = entry.getKey();
					double value = entry.getValue();
					if(prototype.get(key) != null){
						prototype.put(key, prototype.get(key) + value * upLevel);
					}else{
						prototype.put(key, value * upLevel);
					}
				}
			}
			if(silverNum >=  size){
				break;
			}
			if(checkNum >= size){
				break;
			}
		}
		this.saveEquipmentEntity(equipmentMapEntity);
		RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
		Map<String, Object> itemMap = new HashMap<String, Object>();
		int code = rewardModule.useGoods(playerID, KindIDs.SILVERTYPE, (int)useSilver, 0, true, null, itemMap, ioMessage);
		if (code != 0) {
			protocol.setCode(code);
			return;
		}
		protocol.setAutoEquipList(equipList);
		protocol.setUpList(strengList);
		protocol.setItemMap(itemMap);
	}
	
	
	
}
