package com.mi.game.module.talisman;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.mi.core.dao.KeyGeneratorDAO;
import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.TemplateManager;
import com.mi.core.engine.annotation.Module;
import com.mi.core.pojo.KeyGenerator;
import com.mi.game.defines.ActionType;
import com.mi.game.defines.BattleMsgType;
import com.mi.game.defines.EffectType;
import com.mi.game.defines.EquipmentPart;
import com.mi.game.defines.ErrorIds;
import com.mi.game.defines.FriendshipType;
import com.mi.game.defines.KindIDs;
import com.mi.game.defines.MailType;
import com.mi.game.defines.ModuleNames;
import com.mi.game.defines.PlunderDefine;
import com.mi.game.defines.SysConstants;
import com.mi.game.module.achievement.AchievementModule;
import com.mi.game.module.analyse.AnalyseModule;
import com.mi.game.module.base.BaseModule;
import com.mi.game.module.battleReport.BattleReportModule;
import com.mi.game.module.dayTask.DayTaskModule;
import com.mi.game.module.drop.DropModule;
import com.mi.game.module.effect.EffectModule;
import com.mi.game.module.effect.pojo.Effect;
import com.mi.game.module.effect.pojo.PlayerEffectEntity;
import com.mi.game.module.equipment.protocol.EquipmentProtocol;
import com.mi.game.module.event.EventModule;
import com.mi.game.module.festival.FestivalModule;
import com.mi.game.module.festival.define.FestivalConstants;
import com.mi.game.module.hero.HeroModule;
import com.mi.game.module.hero.data.FriendShipData;
import com.mi.game.module.hero.data.HeroData;
import com.mi.game.module.hero.pojo.Hero;
import com.mi.game.module.hero.pojo.HeroEntity;
import com.mi.game.module.login.LoginModule;
import com.mi.game.module.login.pojo.PlayerEntity;
import com.mi.game.module.mailBox.MailBoxModule;
import com.mi.game.module.mainTask.MainTaskModule;
import com.mi.game.module.reward.RewardModule;
import com.mi.game.module.reward.data.GoodsBean;
import com.mi.game.module.talisman.dao.TalismanMapDAO;
import com.mi.game.module.talisman.dao.TalismanPlunderNumEntityDao;
import com.mi.game.module.talisman.dao.TalismanShardDAO;
import com.mi.game.module.talisman.dao.TalismanShardPlunderDAO;
import com.mi.game.module.talisman.data.TalismanData;
import com.mi.game.module.talisman.data.TalismanRefineData;
import com.mi.game.module.talisman.data.TalismanRobotData;
import com.mi.game.module.talisman.data.TalismanShardData;
import com.mi.game.module.talisman.pojo.PlunderNumEntity;
import com.mi.game.module.talisman.pojo.TalismanEntity;
import com.mi.game.module.talisman.pojo.TalismanMapEntity;
import com.mi.game.module.talisman.pojo.TalismanShard;
import com.mi.game.module.talisman.pojo.TalismanShardPlunderEntity;
import com.mi.game.module.talisman.protocol.DrawInfo;
import com.mi.game.module.talisman.protocol.PlunderInfo;
import com.mi.game.module.talisman.protocol.TalismanProtocol;
import com.mi.game.module.vitatly.VitatlyModule;
import com.mi.game.module.vitatly.pojo.VitatlyEntity;
import com.mi.game.util.Base64Coder;
import com.mi.game.util.GZIPUtil;
import com.mi.game.util.Utilities;

@Module(name = ModuleNames.TalismanModule, clazz = TalismanModule.class)
public class TalismanModule extends BaseModule {
	private final KeyGeneratorDAO keyGeneratorDAO = KeyGeneratorDAO.getInstance();
	private final TalismanMapDAO talismanMapDAO = TalismanMapDAO.getInstance();
	private final TalismanShardPlunderDAO talismanShardPlunderDAO = TalismanShardPlunderDAO.getInstance();
	private final TalismanShardDAO talismanShard1DAO = TalismanShardDAO.getInstance();
	private final TalismanPlunderNumEntityDao plunderNumEntityDao = TalismanPlunderNumEntityDao.getInstance(); 
	public static final Map<Integer, Map<Integer, TalismanRefineData>> refineData = new HashMap<>();
	public static final Map<Integer, List<Hero>> robotList = new HashMap<>();
	private final List<Integer> robotIDList = new ArrayList<>();
	private final int lockPrototype1 = 20;
	private final int lockPrototype2 = 40;
	private long heroIncID = 100;
	private int costEnergy = 2;
	private int drawDropID = 103729;
	private int valentineDropID = FestivalConstants.diamondArenaDrop;
	private int foolDayDropID = FestivalConstants.FoolDayboxArenaDrop;
	private int[] initTalismanShardList = new int[] { 102386, 102388 };
	private int talismanBagInitNum = 30;
	private int maxPlunderNum = 10;

	@Override
	public void init() {
		initTalismanID();
		initRefineData();
		initPluderNpc();
		initTalismanShardID();
	}

	private void initRefineData() {
		List<TalismanRefineData> refineDataList = TemplateManager.getTemplateList(TalismanRefineData.class);
		Map<Integer, TalismanRefineData> refineMap;
		for (TalismanRefineData data : refineDataList) {
			int quality = data.getQuality();
			int level = data.getIntensify();
			refineMap = refineData.get(quality);
			if (refineMap == null) {
				refineMap = new HashMap<Integer, TalismanRefineData>();
			}
			refineMap.put(level, data);
			refineData.put(quality, refineMap);
		}
	}

	private void initPluderNpc() {
		List<TalismanRobotData> robotDataList = TemplateManager.getTemplateList(TalismanRobotData.class);
		for (TalismanRobotData data : robotDataList) {
			int pid = data.getPid();
			List<Integer> heroIDList = data.getHero();
			List<Hero> heroList = new ArrayList<>();
			for (int heroID : heroIDList) {
				if (heroID != 0) {
					Hero hero = this.initHero(heroID);
					heroList.add(hero);
				}
			}
			robotIDList.add(pid);
			robotList.put(pid, heroList);
		}
		Collections.shuffle(robotDataList);
	}

	private Hero initHero(int templateID) {
		Hero hero = new Hero();
		hero.setHeroID(incrHeroID());
		hero.setTemplateID(templateID);
		return hero;
	}

	private long incrHeroID() {
		return heroIncID++;
	}

	/**
	 * 获取掠夺列表
	 * */
	public TalismanShardPlunderEntity getTalismanShardPlunderEntity(String playerID) {
		return talismanShardPlunderDAO.getEntity(playerID);
	}

	/**
	 * 初始化宝物实体
	 * */

	public TalismanMapEntity initTalismanEntity(String playerID) {
		TalismanMapEntity talismanMapEntity = new TalismanMapEntity();
		talismanMapEntity.setKey(playerID);
		Map<String, TalismanEntity> talismanMap = initTalismanMap();
		talismanMapEntity.setTalismanMap(talismanMap);
		talismanMapEntity.setMaxTalismanNum(talismanBagInitNum);
		return talismanMapEntity;
	}

	/**
	 * 初始化宝物MapEntity
	 * 
	 * */
	private Map<String, TalismanEntity> initTalismanMap() {
		Map<String, TalismanEntity> map = new HashMap<String, TalismanEntity>();
		// List<TalismanData> talismanist =
		// TemplateManager.getTemplateList(TalismanData.class);
		// if(talismanist != null){
		// for(TalismanData data : talismanist){
		// if(data != null){
		// TalismanEntity talisman = new TalismanEntity();
		// int templateID = data.getPid();
		// int strengLevel = 1;
		// long talismanID = this.getTalismanID();
		// Map<String,Double> prototype = data.getProperty();
		// talisman.setTemplateID(templateID);
		// talisman.setPrototype(prototype);
		// talisman.setStrengLevel(strengLevel);
		// talisman.setTalismanID(talismanID);
		// map.put(talismanID + "", talisman);
		// }
		// }
		// }
		return map;
	}

	/**
	 * 初始化宝物碎片实体
	 * 
	 * */
	public void initTalismanShard(String playerID) {
		for (int shardID : initTalismanShardList) {
			this.addTalismanShard1(playerID, shardID, 1);
		}
	}

	/**
	 * 初始化宝物ID
	 * */
	private void initTalismanID() {
		String clsName = SysConstants.talismanEntity;
		KeyGenerator keyGenerator = keyGeneratorDAO.getEntity(clsName);
		if (keyGenerator == null) {
			keyGenerator = new KeyGenerator();
			keyGenerator.setKey(clsName);
			keyGenerator.setNextId(SysConstants.talismanStartID);
			keyGeneratorDAO.save(keyGenerator);
		}
	}

	/**
	 * 获取宝物唯一号
	 * */

	public long getTalismanID() {
		String clsName = SysConstants.talismanEntity;
		long talismanID = keyGeneratorDAO.updateInc(clsName);
		return talismanID;
	}

	/** 初始化宝物碎片ID */
	private void initTalismanShardID() {
		String clsName = SysConstants.talismanShardEntity;
		KeyGenerator keyGenerator = keyGeneratorDAO.getEntity(clsName);
		if (keyGenerator == null) {
			keyGenerator = new KeyGenerator();
			keyGenerator.setKey(clsName);
			keyGenerator.setNextId(SysConstants.talismanShardStartID);
			keyGeneratorDAO.save(keyGenerator);
		}
	}

	/**
	 * 获取宝物碎片唯一号
	 * */

	public long getTalismanShardID() {

		String clsName = SysConstants.talismanShardEntity;
		long talismanID = keyGeneratorDAO.updateInc(clsName);
		return talismanID;
	}

	/**
	 * 获取宝物实体
	 * */
	public TalismanMapEntity getEntity(String playerID) {

		TalismanMapEntity talismanMapEntity = talismanMapDAO.getEntity(playerID);
		/** 临时 **/
		if (talismanMapEntity == null) {
			talismanMapEntity = this.initTalismanEntity(playerID);
			this.saveEntity(talismanMapEntity);
			logger.error("临时初始化宝物实体");
		}
		return talismanMapEntity;
	}

	/**
	 * 获取宝物实体
	 * */
	public TalismanMapEntity getEntity(String playerID, IOMessage ioMessage) {
		TalismanMapEntity entity = null;

		if (ioMessage != null) {
			entity = (TalismanMapEntity) ioMessage.getInputParse().get(TalismanMapEntity.class.getName());

			if (entity == null) {
				entity = talismanMapDAO.getEntity(playerID);
				ioMessage.getInputParse().put(TalismanMapEntity.class.getName(), entity);
			}
		} else {
			entity = talismanMapDAO.getEntity(playerID);
		}
		return entity;
	}

	/**
	 * 获取抢夺碎片宝物数量的实体
	 * */
	public PlunderNumEntity getTalismanShardNumEntity (String playerID){
		PlunderNumEntity plunderNumEntity = plunderNumEntityDao.getEntity(playerID);
		if(plunderNumEntity == null){
			plunderNumEntity = new PlunderNumEntity();
			plunderNumEntity.setKey(playerID);
		}
		return plunderNumEntity;
	}
	
	/**
	 * 获取抢夺碎片宝物数量的实体(缓存)
	 * */
	public PlunderNumEntity getTalismanShardNumEntity (String playerID,IOMessage ioMessage){
		PlunderNumEntity plunderNumEntity = null;
		if(ioMessage!= null){
			String key = PlunderNumEntity.class.getName();
			plunderNumEntity = (PlunderNumEntity) ioMessage.getInputParse(key);
			if(plunderNumEntity == null){
				plunderNumEntity = this.getTalismanShardNumEntity(playerID);
				ioMessage.getInputParse().put(key, plunderNumEntity);
			}
		}else{
			plunderNumEntity = this.getTalismanShardNumEntity(playerID);
		}
		return plunderNumEntity;
	}
	
	/**
	 * 保存掠夺宝物数量实体
	 * */
	public void savePlunderShardNumEntity(PlunderNumEntity plunderNumEntity){
		plunderNumEntityDao.save(plunderNumEntity);
	}
	
	// /**
	// * 获取宝物碎片实体
	// * */
	// public TalismanShardMapEntity getTalismanShardMapEntity(String playerID){
	// TalismanShardMapEntity entity =talismanShardDAO.getEntity(playerID);
	// if(entity == null){
	// entity = this.initTalismanShard(playerID);
	// this.saveTalismanShardMapEntity(entity);
	// }
	// return entity;
	// }
	//
	// /**
	// * 获取宝物碎片实体
	// * */
	// public TalismanShardMapEntity getTalismanShardMapEntity(String
	// playerID,IOMessage ioMessage){
	// TalismanShardMapEntity entity = null;
	// if(ioMessage != null){
	// entity =
	// (TalismanShardMapEntity)ioMessage.getInputParse().get(TalismanShardMapEntity.class.getName());
	// if(entity == null){
	// entity = this.getTalismanShardMapEntity(playerID);
	// ioMessage.getInputParse().put(TalismanShardMapEntity.class.getName(),
	// entity);
	// }
	// }else{
	// entity = this.getTalismanShardMapEntity(playerID);
	// }
	// return entity;
	// }

	// /**
	// * 保存宝物碎片实体
	// * */
	// public void saveTalismanShardMapEntity(TalismanShardMapEntity entity){
	// talismanShardDAO.save(entity);
	// }
	//
	/**
	 * 获取宝物
	 * */
	public TalismanEntity getTalisman(String playerID, long talismanID, IOMessage ioMessage) {
		TalismanMapEntity talismanMapEntity = this.getEntity(playerID, ioMessage);
		Map<String, TalismanEntity> talismanMap = talismanMapEntity.getTalismanMap();
		TalismanEntity talisman = talismanMap.get(talismanID + "");
		if (talisman == null) {
			throw new NullPointerException(ErrorIds.TalismanNotFound + "");
		}
		return talisman;
	}

	/**
	 * 保存宝物实体
	 * */
	public void saveEntity(TalismanMapEntity talismanMapEntity) {
		talismanMapDAO.save(talismanMapEntity);
	}

	/**
	 * 宝物强化
	 * 
	 * @param playerID
	 *            String 玩家ID
	 * @param talismanID
	 *            String 强化的宝物ID
	 * @param talismanList
	 *            List<Long> 强化的材料集合
	 * @param protocol
	 *            TalismanProtocol 返回协议
	 * */

	public void strengTalisman(String playerID, String talismanID, List<Object> talismanList, TalismanProtocol protocol) {
		TalismanMapEntity talismanMapEntity = this.getEntity(playerID);
		Map<String, TalismanEntity> talismanMap = talismanMapEntity.getTalismanMap();
		TalismanEntity talisman = talismanMap.get(talismanID);
		if (talisman == null) {
			protocol.setCode(ErrorIds.TalismanNotFound);
			logger.error("宝物不存在");
			return;
		}
		int templateID = talisman.getTemplateID();
		TalismanData talismanData = TemplateManager.getTemplateData(templateID, TalismanData.class);
		int part = talismanData.getPart();
		// if(talismanData.getCanEquip() != 1){
		// logger.error("宝物不可强化");
		// throw new IllegalArgumentException(ErrorIds.TalismanNotIntensify +
		// "");
		// }
		int exp = 0;
		for (Object temp : talismanList) {
			String strengID = temp.toString();
			TalismanEntity strengTalisman = talismanMap.get(strengID);
			if (strengTalisman == null) {
				protocol.setCode(ErrorIds.TalismanNotFound);
				logger.error("宝物不存在");
				return;
			}
			int strengTemplateID = strengTalisman.getTemplateID();
			TalismanData data = TemplateManager.getTemplateData(strengTemplateID, TalismanData.class);
			int tempPart = data.getPart();
			if (part != tempPart) {
				throw new IllegalArgumentException(ErrorIds.TalismanNotIntensify + "");
			}
			exp += data.getBaseEXP() + strengTalisman.getExp();
			talismanMap.remove(strengID);
		}
		Map<String, Object> itemMap = new HashMap<String, Object>();
		
		RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
		int code = rewardModule.useGoods(playerID, KindIDs.SILVERTYPE, exp * 15, 0, true, null, itemMap, null);
		if(code != 0){
			protocol.setCode(code);
			return ;
		}
		this.addTalismanExp(talisman, exp);
		itemMap.put("removeTalismanList", talismanList);
		protocol.setEquipTalisman(talisman);
		protocol.setItemMap(itemMap);
		this.saveEntity(talismanMapEntity);
		AchievementModule acModule = ModuleManager.getModule(ModuleNames.AchievementModule, AchievementModule.class);
		acModule.refreshAchievement(playerID, ActionType.TALISMANLEVEL, talisman.getStrengLevel());
		if(talismanData.getPart() == EquipmentPart.horse){
			MainTaskModule mainTaskModule = ModuleManager.getModule(ModuleNames.MainTaskModule,MainTaskModule.class);
			mainTaskModule.updateTaskByActionType(playerID, ActionType.HORSELEVEL, talisman.getStrengLevel(), null);	
		}
	}

	// private void addExpTalisman(String playerID,TalismanMapEntity entity){
	// Map<String,TalismanEntity> talismanMap = entity.getTalismanMap();
	// for(int i =1 ;i <10; i++){
	// TalismanEntity talismanEntity = new TalismanEntity();
	// talismanEntity.setTemplateID(10161);
	// talismanEntity.setTalismanID(this.getTalismanID());
	// talismanMap.put(talismanEntity.getTalismanID() + "", talismanEntity);
	// }
	//
	// this.saveEntity(entity);
	// }
	/**
	 * 法宝增加经验
	 * 
	 * */
	public void addTalismanExp(TalismanEntity talisman, int exp) {
		int templateID = talisman.getTemplateID();
		int nowExp = talisman.getExp();
		TalismanData talismanData = TemplateManager.getTemplateData(templateID, TalismanData.class);
		int quality = talismanData.getQuality();
		int allExp = nowExp + exp;
		int level = talisman.getStrengLevel();
		int nowLevel = this.getTalismanExpLevel(quality, allExp, level);

		int levelUp = nowLevel - level;
		if (levelUp > 0) {
			Map<String, Double> intensify = talismanData.getIntensify();
			Set<Entry<String, Double>> entrySet = intensify.entrySet();
			Map<String, Double> prototype = talisman.getPrototype();
			
			for (Entry<String, Double> entry : entrySet) {
				String key = entry.getKey();
				double value = entry.getValue();
				double addValue = Utilities.doubleMultiply(value, levelUp);
				if (prototype.containsKey(key)) {
					double oldValue = prototype.get(key);
					prototype.put(key, Utilities.doubleAdd(addValue, oldValue));
				} else {
					prototype.put(key, addValue);
				}
			}

			Map<String, Double> unlockPrototype = null;
			if(level < lockPrototype1 && nowLevel >= lockPrototype1){
				unlockPrototype = talismanData.getUnlockProperty1();
				if (unlockPrototype != null) {
					for (Entry<String, Double> entry : unlockPrototype.entrySet()) {
						String key = entry.getKey();
						Double value = entry.getValue();
						if (prototype.get(key) != null) {
							prototype.put(key, (Utilities.doubleAdd(prototype.get(key) ,value)));
						} else {
							prototype.put(key, value);
						}
					}
				}
			}
			if(level < lockPrototype2 && nowLevel >= lockPrototype2){
				unlockPrototype = talismanData.getUnlockProperty2();
				if (unlockPrototype != null) {
					for (Entry<String, Double> entry : unlockPrototype.entrySet()) {
						String key = entry.getKey();
						Double value = entry.getValue();
						if (prototype.get(key) != null) {
							prototype.put(key, (Utilities.doubleAdd(prototype.get(key) ,value)));
						} else {
							prototype.put(key, value);
						}
					}
				}
			}
		}
		talisman.setExp(allExp);
		talisman.setStrengLevel(nowLevel);
	
	}

	/** 获取经验对应的等级 */
	private int getTalismanExpLevel(int quality, int exp, int level) {
		for (int i = level; i < 9999; i++) {
			int maxExp = this.getTalismanNextExp(i, quality);
			if (maxExp > exp) {
				return i - 1;
			}
		}
		return level;
	}

	/**
	 * 获取宝物指定等级升级的经验
	 * */
	private int getTalismanNextExp(int level, int quality) {
		int exp = this.expFormula(level);
		int allExp = quality * 5 * exp + level * quality * 10;
		return allExp;
	}

	/**
	 * 总经验计算公式
	 * */
	private int expFormula(int level) {
		return level * (level + 1) * (2 * level + 1) / 6;
	}

	/***
	 * 装备宝物
	 * 
	 * @param playerID
	 *            String 玩家ID
	 * @param heroID
	 *            long 英雄ID
	 * @param talismanID
	 *            long 宝物ID
	 * */
	public void doEquip(String playerID, long talismanID, long heroID, TalismanProtocol protocol) {
		TalismanMapEntity talismanMapEntity = this.getEntity(playerID);
		Map<String, TalismanEntity> talismanMap = talismanMapEntity.getTalismanMap();
		TalismanEntity talisman = talismanMap.get(talismanID + "");
		if (talisman == null) {
			protocol.setCode(ErrorIds.TalismanNotFound);
			logger.error("宝物不存在");
			return;
		}
		int templateID = talisman.getTemplateID();
		TalismanData data = TemplateManager.getTemplateData(templateID, TalismanData.class);
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
				if (oldEquipID != talismanID) {
					if (oldEquipID != 0) {
						TalismanEntity oldEquipment = talismanMap.get(oldEquipID + "");
						oldEquipment.setHeroID(0l);
						protocol.setUnEquipTalisman(oldEquipment);
					}
					
				} else {
					logger.error("不可穿戴同一件装备");
					protocol.setCode(ErrorIds.DontSameEquip);
					return;
				}
			}
		}
		long oldHeroID = talisman.getHeroID();
		if (oldHeroID != 0) {
			if (oldHeroID != heroID) {
				Hero oldHero = heroMap.get(oldHeroID + "");
				if (oldHero == null) {
					logger.error("英雄未找到");
					protocol.setCode(ErrorIds.HeroNotFound);
					return;
				}
				Map<String, Long> oldHeroEquipMap = oldHero.getEquipMap();
				oldHeroEquipMap.put(part + "", 0l);
				this.changeTalismanFriendship(playerID, oldHero);
				protocol.setUnEquipHero(oldHero);
			} else {
				logger.error("不可穿戴同一件装备");
				protocol.setCode(ErrorIds.DontSameEquip);
				return;
			}
		}
		talisman.setHeroID(heroID);
		heroEquipMap.put(part + "", talismanID);
		this.changeTalismanFriendship(playerID, hero);
		heroModule.saveHeroEntity(heroEntity);
		this.saveEntity(talismanMapEntity);
		protocol.setEquipHero(hero);
		protocol.setEquipTalisman(talisman);
	}

	/**
	 * 卸载宝物
	 * 
	 * @param playerID
	 *            String 玩家ID
	 * @param talismanID
	 *            long 宝物的唯一ID
	 * */
	public void unEquip(String playerID, long talismanID, TalismanProtocol protocol) {
		TalismanMapEntity equipmenEntity = this.getEntity(playerID);
		Map<String, TalismanEntity> equipmentMap = equipmenEntity.getTalismanMap();
		TalismanEntity equipment = equipmentMap.get(talismanID + "");
		if (equipment == null) {
			protocol.setCode(ErrorIds.TalismanNotFound);
			logger.error("宝物不存在");
			return;
		}
		long heroID = equipment.getHeroID();
		if (heroID == 0) {
			logger.error("宝物未装备");
			protocol.setCode(ErrorIds.UnEquip);
			return;
		}
		HeroModule heroModule = ModuleManager.getModule(ModuleNames.HeroModule, HeroModule.class);
		HeroEntity heroEntity = heroModule.getHeroEntity(playerID);
		Hero hero = heroEntity.getHeroMap().get(heroID + "");
		if (hero == null) {
			logger.error("英雄未找到");
			protocol.setCode(ErrorIds.HeroNotFound);
			return;
		}
		Map<String, Long> heroEquipMap = hero.getEquipMap();
		int templateID = equipment.getTemplateID();
		TalismanData data = TemplateManager.getTemplateData(templateID, TalismanData.class);
		int part = data.getPart();
		heroEquipMap.put(part + "", 0l);
		equipment.setHeroID(0l);
		this.changeTalismanFriendship(playerID, hero);
		this.saveEntity(equipmenEntity);
		heroModule.saveHeroEntity(heroEntity);
		protocol.setUnEquipHero(hero);
		protocol.setUnEquipTalisman(equipment);
	}

	/**
	 * 一键装备法宝
	 * */

	public void autoEquip(String playerID, long heroID, Map<String, Long> heroEquipMap, EquipmentProtocol protocol) {
		TalismanMapEntity equipmenEntity = this.getEntity(playerID);
		Map<String, TalismanEntity> equipmentMap = equipmenEntity.getTalismanMap();
		Map<Integer, Integer> aptitudeMap = new HashMap<>();
		Map<Long,TalismanEntity> talismanList = new HashMap<>();
		for (Entry<String, TalismanEntity> entry : equipmentMap.entrySet()) {
			TalismanEntity talisman = entry.getValue();
			if (talisman.getHeroID() == 0 || talisman.getHeroID() == heroID) {
				int templateID = talisman.getTemplateID();
				TalismanData data = TemplateManager.getTemplateData(templateID, TalismanData.class);
				if (data.getCanEquip() == 0) {
					continue;
				}
				int aptitude = data.getAptitude();
				int part = data.getPart();
				if (aptitudeMap.get(part) == null) {
					aptitudeMap.put(part, aptitude);
					heroEquipMap.put(part + "", talisman.getTalismanID());
				} else {
					if (aptitudeMap.get(part) < aptitude) {
						long changeID = heroEquipMap.get(part + "");
						TalismanEntity tempTalisman = equipmentMap.get(changeID + "");
						tempTalisman.setHeroID(0l);
						talismanList.put(tempTalisman.getTalismanID(),tempTalisman);
						aptitudeMap.put(part, aptitude);
						heroEquipMap.put(part + "", talisman.getTalismanID());
					}else
						if(aptitudeMap.get(part) == aptitude){
							int tempLevel = talisman.getStrengLevel();
							long changeID = heroEquipMap.get(part + "");
							TalismanEntity tempTalisman = equipmentMap.get(changeID + "");
							int strengLevel = tempTalisman.getStrengLevel();
							if(tempLevel > strengLevel){
								heroEquipMap.put(part + "", talisman.getTalismanID());
								if(tempTalisman.getHeroID() != 0){
									tempTalisman.setHeroID(0);
									talismanList.put(tempTalisman.getTalismanID(),tempTalisman);
									heroEquipMap.put(part + "", talisman.getTalismanID());
								}
							}
						}
				}
				if (talisman.getHeroID() == heroID) {
					if (heroEquipMap.get(part + "") != talisman.getTalismanID()) {
						talisman.setHeroID(0l);
						talismanList.put(talisman.getTalismanID(),talisman);
					}
				}
			}
		}

		for (Entry<String, Long> entry : heroEquipMap.entrySet()) {
			long talismanID = entry.getValue();
			String part = entry.getKey();
			int temp = Integer.parseInt(part);
			if (temp == EquipmentPart.horse || temp == EquipmentPart.book) {
				TalismanEntity equipment = equipmentMap.get(talismanID + "");
				if (equipment != null) {
					equipment.setHeroID(heroID);
					talismanList.put(equipment.getTalismanID(),equipment);
				}
			}
		}
		protocol.setTalismanList(talismanList);
		this.saveEntity(equipmenEntity);
	}

	/**
	 * 扣除宝物
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
	public long deductTalisman(String playerID, int templateID, boolean isSave, IOMessage ioMessage) {
		TalismanMapEntity entity = this.getEntity(playerID, ioMessage);
		Map<String, TalismanEntity> talismanMap = entity.getTalismanMap();
		if (talismanMap.isEmpty()) {
			logger.error("宝物背包为空");
			throw new IllegalArgumentException(ErrorIds.TalismanBagEmpty + "");
		}
		Set<Entry<String, TalismanEntity>> entrySet = talismanMap.entrySet();
		long talismanID = 0;
		for (Entry<String, TalismanEntity> entry : entrySet) {
			TalismanEntity bagEntity = entry.getValue();
			if (bagEntity.getHeroID() == 0 && bagEntity.getTemplateID() == templateID && bagEntity.getRefineLevel() == 0 && bagEntity.getExp() == 0) {
				talismanID = bagEntity.getTalismanID();
				break;
			}
		}
		if (talismanID != 0) {
			talismanMap.remove(talismanID + "");
		} else {
			logger.error("未找到宝物" + templateID);
			throw new IllegalArgumentException(ErrorIds.TalismanNotFound + "");
		}
		if (isSave) {
			this.saveEntity(entity);
		}
		return talismanID;
	}

	/**
	 * 宝物出售
	 * */
	public void sellTalisman(String playerID, List<Object> sellList, TalismanProtocol protocol, IOMessage ioMessage) {
		TalismanMapEntity entity = this.getEntity(playerID, ioMessage);
		Map<String, TalismanEntity> map = entity.getTalismanMap();
		int price = 0;
		for (Object temp : sellList) {
			String talismanID = temp.toString();
			TalismanEntity talisman = map.get(talismanID);
			if (talisman != null) {
				int templateID = talisman.getTemplateID();
				TalismanData data = TemplateManager.getTemplateData(templateID, TalismanData.class);
				if (data.getSell() == 0) {
					logger.error("宝物不可出售");
					protocol.setCode(ErrorIds.TalismanNotSell);
					return;
				}
				price += talisman.getExp() * 15;
				price += data.getPrice();
				map.remove(talismanID);
			} else {
				logger.error("未找到宝物");
				protocol.setCode(ErrorIds.TalismanNotFound);
				return;
			}
		}
		Map<String, Object> itemMap = new HashMap<String, Object>();
		List<GoodsBean> showMap = new ArrayList<>();
		RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
		rewardModule.addGoods(playerID, KindIDs.SILVERTYPE, price, null, true, null, itemMap, ioMessage);
		showMap.add(new GoodsBean(KindIDs.SILVERTYPE, price));
		itemMap.put("removeTalismanList", sellList);
		this.saveEntity(entity);
		protocol.setItemMap(itemMap);
		protocol.setShowMap(showMap);
	}

	/**
	 * 宝物精炼
	 * */
	public void talismanRefine(String playerID, String talismanID, TalismanProtocol protocol, IOMessage ioMessage) {
		TalismanMapEntity entity = this.getEntity(playerID, ioMessage);
		Map<String, TalismanEntity> map = entity.getTalismanMap();
		TalismanEntity talisman = map.get(talismanID);
		if (talisman == null) {
			logger.error("宝物未找到");
			protocol.setCode(ErrorIds.TalismanNotFound);
			return;
		}
		int templateID = talisman.getTemplateID();
		int refineLevel = talisman.getRefineLevel();
		TalismanData data = TemplateManager.getTemplateData(templateID, TalismanData.class);
		int quality = data.getQuality();
		List<GoodsBean> goodsList = new ArrayList<GoodsBean>();
		if (data.getCanRefine() == 1) {
			if(refineLevel < data.getRefineMax()){
				TalismanRefineData tempfineData = TalismanModule.refineData.get(quality).get(refineLevel);
				GoodsBean silverBean = new GoodsBean(KindIDs.SILVERTYPE, tempfineData.getMoneyReq());
				goodsList.add(silverBean);
				GoodsBean itemBean = tempfineData.getItemReq();
				GoodsBean newBean = new GoodsBean(itemBean.getPid(),itemBean.getNum());
				if (newBean.getPid() == 9999) {
					newBean.setPid(templateID);
				}
				goodsList.add(newBean);
			}else{
				logger.error("宝物精炼最大等级");
				protocol.setCode(ErrorIds.TalismanNotRefine);
				return;
			}
		} else {
			logger.error("宝物不可精炼");
			protocol.setCode(ErrorIds.TalismanNotRefine);
			return;
		}
		Map<String, Double> refineData = data.getRefine();
		Map<String, Double> prototype = talisman.getPrototype();
		Map<String, Double> refinePrototype = talisman.getRefinePrototype();
		for (Entry<String, Double> entry : refineData.entrySet()) {
			String key = entry.getKey();
			Double value = entry.getValue();
			if (refinePrototype.get(key) != null) {
				double oldValue = 0;
				if(prototype != null && prototype.get(key) != null){
					oldValue = prototype.get(key);
				}
				refinePrototype.put(key, Utilities.doubleAdd(value, oldValue));
			} else {
				refinePrototype.put(key, value);
			}
		}
		refineLevel += 1;
//		Map<String, Double> unlockPrototype = null;
//		if (refineLevel == lockPrototype1) {
//			unlockPrototype = data.getUnlockProperty1();
//		} else if (refineLevel == lockPrototype2) {
//			unlockPrototype = data.getUnlockProperty2();
//		}
//		if (unlockPrototype != null) {
//			for (Entry<String, Double> entry : unlockPrototype.entrySet()) {
//				String key = entry.getKey();
//				Double value = entry.getValue();
//				if (prototype.get(key) != null) {
//					prototype.put(key, (prototype.get(key) * 100 + (value) * 100) / 100);
//				} else {
//					prototype.put(key, value);
//				}
//			}
//		}
		Map<String, Object> itemMap = new HashMap<>();
		talisman.setRefineLevel(refineLevel);
		talisman.setPrototype(prototype);
		RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
		int code = rewardModule.useGoods(playerID, goodsList, 0, true, null, itemMap, ioMessage);
		if (code != 0) {
			protocol.setCode(code);
			return;
		}
		protocol.setEquipTalisman(talisman);
		protocol.setItemMap(itemMap);
		AchievementModule achievementModule = ModuleManager.getModule(ModuleNames.AchievementModule, AchievementModule.class);
		achievementModule.refreshAchievement(playerID, ActionType.TALISMANREFINELEVEL, refineLevel);
	}

	/***
	 * 
	 * */

	public TalismanEntity getInitTalisman(int templateID, long talismanID) {
		TalismanEntity talisman = new TalismanEntity();
		TalismanData data = TemplateManager.getTemplateData(templateID, TalismanData.class);
		Map<String, Double> prototype = new HashMap<>();
		Map<String, Double> dataPrototype = data.getProperty();
		for (Entry<String, Double> entry : dataPrototype.entrySet()) {
			prototype.put(entry.getKey(), entry.getValue());
		}
		talisman.setTemplateID(templateID);
		talisman.setPrototype(prototype);
		talisman.setTalismanID(talismanID);
		return talisman;
	}

	/**
	 * 添加宝物
	 * */
	public TalismanEntity addTalisman(String playerID, int templateID, int exp, boolean isSave, IOMessage ioMessage) {
		TalismanData data = TemplateManager.getTemplateData(templateID, TalismanData.class);
		TalismanMapEntity entity = this.getEntity(playerID, ioMessage);
		Map<String, TalismanEntity> map = entity.getTalismanMap();
		// if(map.size() > entity.getMaxTalismanNum()){
		// throw new IllegalArgumentException(ErrorIds.TalismanBagFull + "");
		// }
		TalismanEntity talisman = new TalismanEntity();
		long talismanID = this.getTalismanID();
		Map<String, Double> prototype = new HashMap<>();

		Map<String, Double> dataPrototype = data.getProperty();
		for (Entry<String, Double> entry : dataPrototype.entrySet()) {
			prototype.put(entry.getKey(), entry.getValue());
		}
		talisman.setTemplateID(templateID);
		talisman.setPrototype(prototype);
		talisman.setTalismanID(talismanID);
		talisman.setExp(exp);
		map.put(talismanID + "", talisman);
		if (isSave) {
			this.saveEntity(entity);
		}
		AchievementModule acModule = ModuleManager.getModule(ModuleNames.AchievementModule, AchievementModule.class);
		acModule.refreshAchievement(playerID, ActionType.GETTALISMAN, 1);
		return talisman;
	}

	// /**
	// * 添加宝物碎片
	// * */
	// public TalismanShard addTalismanShard(String playerID,int templateID,int
	// num,boolean isSave,IOMessage ioMessage){
	// this.addTalismanShard1(playerID, templateID, num);
	// TalismanShardData data =
	// TemplateManager.getTemplateData(templateID,TalismanShardData.class);
	// int talismanID = data.getTalismanID();
	// TalismanShardMapEntity entity =
	// this.getTalismanShardMapEntity(playerID,ioMessage);
	// Map<Integer,List<TalismanShard>> shardMap = entity.getShardMap();
	// List<TalismanShard> shardList = shardMap.get(talismanID);
	// TalismanShard talismanShard = null;
	// boolean isFind = false;
	// if(shardList != null ){
	// for(TalismanShard shard : shardList){
	// if(shard.getShardID() == templateID){
	// shard.setNum(shard.getNum() + num);
	// isFind = true;
	// talismanShard = shard;
	// break;
	// }
	// }
	// }
	// if(!isFind){
	// TalismanShard shard = new TalismanShard();
	// shard.setNum(num);
	// shard.setShardID(templateID);
	// shardList.add(shard);
	// talismanShard = shard;
	// }
	// if(isSave){
	// this.saveTalismanShardMapEntity(entity);
	// }
	// addTalismanShard1(playerID, templateID, num);
	// return talismanShard;
	//
	// }

	private void saveTalismanShard1(TalismanShard talismanShard1) {
		talismanShard1DAO.save(talismanShard1);
	}

	public TalismanShard addTalismanShard1(String playerID, int templateID, int num) {
		LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule, LoginModule.class);
		PlayerEntity playerEntity = loginModule.getPlayerEntity(playerID);
		int level = playerEntity.getLevel();
		TalismanShard talismanShard = this.getTalismanShard1(playerID, templateID);
		if (talismanShard == null) {
			talismanShard = new TalismanShard();
			talismanShard.setKey(this.getTalismanShardID() + "");
			talismanShard.setShardID(templateID);
			talismanShard.setNum(num);
			talismanShard.setPlayerID(playerID);
		} else {
			talismanShard.setNum(num + talismanShard.getNum());
		}
		talismanShard.setLevel(level);
		this.saveTalismanShard1(talismanShard);
		return talismanShard;
	}

	/**
	 * 获取玩家宝物碎片信息
	 * */

	public TalismanShard getTalismanShard1(String playerID, int shardID) {
		TalismanShard talismanShard1 = talismanShard1DAO.getTalismanShard(playerID, shardID);
		return talismanShard1;
	}

	public TalismanShard getTalismanShard1(String playerID, int shardID, IOMessage ioMessage) {
		TalismanShard talismanShard = null;
		if (ioMessage != null) {
			String key = playerID + shardID;
			talismanShard = (TalismanShard) ioMessage.getInputParse().get(key);
			if (talismanShard == null) {
				talismanShard = this.getTalismanShard1(playerID, shardID);
				ioMessage.getInputParse().put(key, talismanShard);
			}
		} else {
			talismanShard = this.getTalismanShard1(playerID, shardID);
		}
		return talismanShard;
	}

	/***
	 * 判断玩家是否可以抢夺
	 * */
	private void checkPlunder(String playerID, TalismanShardData shardData) {
		int isDefult = shardData.getIsDefult();
		int shardID = shardData.getPid();
		TalismanShard sameShard = this.getTalismanShard1(playerID, shardID);
		if (sameShard != null && sameShard.getNum() > 0) {
			logger.error("");
			throw new IllegalArgumentException(ErrorIds.AlreadyHaveShard + "");
		}
		if (isDefult != 1) {
			int talismanID = shardData.getTalismanID();
			TalismanData talismanData = TemplateManager.getTemplateData(talismanID, TalismanData.class);
			List<Integer> talismanList = talismanData.getComposeID();
			boolean isFind = false;
			for (int tempID : talismanList) {
				if (tempID == shardID) {
					continue;
				}
				TalismanShard shard = this.getTalismanShard1(playerID, tempID);
				if (shard != null && shard.getNum() > 0) {
					isFind = true;
					break;
				}
			}
			if (!isFind) {
				logger.error("没有该类碎片 不可抢夺");
				throw new IllegalArgumentException(ErrorIds.NoTalismanShard + "");
			}
		}
	}

	private boolean canAttackPlayerTime() {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());
		int hour = c.get(Calendar.HOUR_OF_DAY);
		if (hour < 10) {
			return false;
		}
		return true;
	}

	/**
	 * 获取可以掠夺的玩家列表
	 * */
	public List<String> getPlunderPlayerList(String playerID, int shardID) {
		LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule, LoginModule.class);
		PlayerEntity playerEntity = loginModule.getPlayerEntity(playerID);
		int level = playerEntity.getLevel();
		List<TalismanShard> list = talismanShard1DAO.searchTalismanShard(playerID, shardID, level);
		List<String> playerList = new ArrayList<>();
		for (TalismanShard talismanShard1 : list) {
			playerList.add(talismanShard1.getPlayerID());
		}
		EffectModule effectModule = ModuleManager.getModule(ModuleNames.EffectModule, EffectModule.class);
		long nowTime = System.currentTimeMillis();
		List<String> tempList = new ArrayList<>();
		for(String tempID : playerList){
			long endTime = effectModule.getEndTime(tempID, EffectType.refuseBattle);
			if (endTime > nowTime) {
				continue;
			}
			if(!this.checkShardEnough(shardID, tempID)){
				continue;
			}
			tempList.add(tempID);
		}
		playerList = tempList;
		int playerLimit = 0;
		if (canAttackPlayerTime()) {
			playerLimit = 3;
		}
		List<String> plunderList = new ArrayList<>();
		for (int i = 0; i < playerLimit; i++) {
			int size = playerList.size();
			if (size > 0) {
				int robotSize = robotList.size();
				int max = robotSize + size * 10;
				int random = Utilities.getRandomInt(max);
				if (random > robotSize) {
					int index = (random - robotSize - 1) / 10;
					plunderList.add(playerList.get(index));
					playerList.remove(index);
				}
			} else {
				break;
			}
		}
		int plunderSize = plunderList.size();
		int robotNum = 4 - plunderSize;
		if (robotNum > 0) {
			List<Integer> randomRobotList = this.getRandomRobot(robotNum);
			for (int robotID : randomRobotList) {
				plunderList.add("npc" + robotID);
			}
		}
		return plunderList;
	}

	/** 获取新手引导列表 */
	public void getNewPlayerPlunderInfo(String playerID, int shardID, IOMessage ioMessage, TalismanProtocol protocol) {
		HeroModule heroModule = ModuleManager.getModule(ModuleNames.HeroModule, HeroModule.class);
		Hero lead = heroModule.getLead(playerID, ioMessage);
		int randomLevel = Utilities.getRandomInt(2) - 1;
		randomLevel = lead.getLevel() + randomLevel;
		if (randomLevel < 1) {
			randomLevel = 1;
		}
		TalismanShardData shardData = TemplateManager.getTemplateData(shardID, TalismanShardData.class);
		if (shardData == null) {
			throw new IllegalArgumentException(ErrorIds.ShardNotFound + "");
		}
		this.checkPlunder(playerID, shardData);
		List<PlunderInfo> plunderInfos = new ArrayList<PlunderInfo>();
		List<String> plunderList = new ArrayList<>();
		plunderList.add("npc10491");
		plunderList.add("npc10492");
		plunderList.add("npc10493");
		plunderList.add("npc10494");
		for (String plunderID : plunderList) {
			List<Integer> showHeroList = new ArrayList<>();
			int robotID = Integer.parseInt(plunderID.substring(3));
			List<Hero> heroList = robotList.get(robotID);
			List<Long> troops = new ArrayList<Long>();
			List<Long> teamList = new ArrayList<Long>();
			for (int i = 0; i < 12; i++) {
				teamList.add(0l);
			}
			for (int i = 0; i < heroList.size(); i++) {
				Hero hero = heroList.get(i);
				showHeroList.add(hero.getTemplateID());
				hero.setLevel(randomLevel);
				troops.add(hero.getHeroID());
				teamList.set(i, hero.getHeroID());
			}
			for (Hero hero : heroList) {
				hero.setLevel(randomLevel);
				troops.add(hero.getHeroID());
			}

			TalismanRobotData data = TemplateManager.getTemplateData(robotID, TalismanRobotData.class);
			PlunderInfo plunderInfo = new PlunderInfo();
			plunderInfo.setHeroList(showHeroList);
			plunderInfo.setName(data.getName());
			plunderInfo.setLevel(randomLevel);
			plunderInfo.setProbability(shardData.getSnatchNPCRate());
			plunderInfo.setPlunderID(plunderID);
			plunderInfo.setQuality(3);
			plunderInfos.add(plunderInfo);
		}
		protocol.setPlunderInfoList(plunderInfos);
	}

	/**
	 * 获取掠夺列表
	 * */
	public void getPlunderInfo(String playerID, int shardID, IOMessage ioMessage, TalismanProtocol protocol) {

		HeroModule heroModule = ModuleManager.getModule(ModuleNames.HeroModule, HeroModule.class);
		Hero lead = heroModule.getLead(playerID, ioMessage);
		LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule, LoginModule.class);
		int randomLevel = Utilities.getRandomInt(2) - 1;
		randomLevel = lead.getLevel() + randomLevel;
		if (randomLevel < 1) {
			randomLevel = 1 ;
		}
		TalismanShardData shardData = TemplateManager.getTemplateData(shardID, TalismanShardData.class);
		if (shardData == null) {
			throw new IllegalArgumentException(ErrorIds.ShardNotFound + "");
		}
		this.checkPlunder(playerID, shardData);
		List<PlunderInfo> plunderInfos = new ArrayList<PlunderInfo>();
		List<String> plunderList = this.getPlunderPlayerList(playerID, shardID);
		for (String plunderID : plunderList) {
			List<Integer> showHeroList = new ArrayList<>();
			if (Utilities.isNpc(plunderID)) {
				int robotID = Integer.parseInt(plunderID.substring(3));
				List<Hero> heroList = robotList.get(robotID);
				List<Long> troops = new ArrayList<Long>();
				List<Long> teamList = new ArrayList<Long>();
				for (int i = 0; i < 12; i++) {
					teamList.add(0l);
				}
				for (int i = 0; i < heroList.size(); i++) {
					Hero hero = heroList.get(i);
					showHeroList.add(hero.getTemplateID());
					hero.setLevel(randomLevel);
					troops.add(hero.getHeroID());
					teamList.set(i, hero.getHeroID());
				}
				for (Hero hero : heroList) {
					hero.setLevel(randomLevel);
					troops.add(hero.getHeroID());
				}

				TalismanRobotData data = TemplateManager.getTemplateData(robotID, TalismanRobotData.class);
				PlunderInfo plunderInfo = new PlunderInfo();
				plunderInfo.setHeroList(showHeroList);
				plunderInfo.setName(data.getName());
				plunderInfo.setLevel(randomLevel);
				plunderInfo.setProbability(shardData.getSnatchNPCRate());
				plunderInfo.setPlunderID(plunderID);
				plunderInfo.setQuality(3);
				plunderInfos.add(plunderInfo);
			} else {
				HeroEntity plunderHeroEntity = heroModule.getHeroEntity(plunderID, ioMessage);
				Hero tempLead = heroModule.getLead(playerID, ioMessage);
				HeroData heroData = TemplateManager.getTemplateData(tempLead.getTemplateID(), HeroData.class);
				List<Long> teamList = plunderHeroEntity.getTeamList();
				for (long heroID : teamList) {
					if (heroID != 0) {
						Hero hero = plunderHeroEntity.getHeroMap().get(heroID + "");
						showHeroList.add(hero.getTemplateID());
					}
				}
				PlayerEntity playerEntity = loginModule.getPlayerEntity(plunderID);
				PlunderInfo plunderInfo = new PlunderInfo();
				plunderInfo.setPlunderID(plunderID);
				plunderInfo.setHeroList(showHeroList);
				plunderInfo.setName(playerEntity.getNickName());
				plunderInfo.setLevel(playerEntity.getLevel());
				plunderInfo.setProbability(shardData.getSnatchPlayerRate());
				plunderInfo.setQuality(heroData.getQuality());
				plunderInfos.add(plunderInfo);
			}
		}
		protocol.setPlunderInfoList(plunderInfos);
	}

	private List<Integer> getRandomRobot(int num) {
		int size = robotIDList.size();
		int random = Utilities.getRandomInt(size - num);
		List<Integer> randomList = new ArrayList<>(num);
		for (int i = random; i < random + num; i++) {
			randomList.add(robotIDList.get(i));
		}
		return randomList;
	}

	/**
	 * 删除碎片
	 * */
	public void delShard(String playerID, int shardID, List<TalismanShard> removeList, IOMessage ioMessage) {
		if (!Utilities.isNpc(playerID)) {
			TalismanShard talismanShard = this.getTalismanShard1(playerID, shardID, ioMessage);
			if (talismanShard == null || talismanShard.getNum() < 1) {
				throw new IllegalArgumentException(ErrorIds.ShardNotEnough + "");
			}
			talismanShard.setNum(talismanShard.getNum() - 1);
			if (removeList != null) {
				removeList.add(talismanShard);
			}
			if (talismanShard.getNum() < 1) {
				talismanShard1DAO.del(talismanShard);
			} else {
				this.saveTalismanShard1(talismanShard);
			}
		}
	}

	/**
	 * 连续抢夺
	 * */
	public void continusPlunderShard(String playerID, int shardID, IOMessage ioMessage, TalismanProtocol protocol) {
		Map<String, Object> itemMap = new HashMap<>();
		TalismanShardData shardData = TemplateManager.getTemplateData(shardID, TalismanShardData.class);
		this.checkPlunder(playerID, shardData);

		VitatlyModule vitatlyModule = ModuleManager.getModule(ModuleNames.VitatlyModule, VitatlyModule.class);
		VitatlyEntity vitatlyEntity = vitatlyModule.getVitatlyEntity(playerID);
		long energy = vitatlyEntity.getEnergy();
		long plunderNum = energy / costEnergy;
		if (plunderNum > maxPlunderNum) {
			plunderNum = maxPlunderNum;
		}
		if (plunderNum < 1) {
			logger.error("体力不足");
			throw new IllegalArgumentException(ErrorIds.NotEnoughEnergy + "");
		}
		int rare = shardData.getNPCRate();
		List<GoodsBean> goodsList = new ArrayList<>();
		List<GoodsBean> showMap = new ArrayList<>();
		int realNum = 0;
		for (int i = 1; i <= plunderNum; i++) {
			int random = Utilities.getRandomInt(100);
			realNum += 1;
			if (rare > random) {
				GoodsBean shardBean = new GoodsBean(shardID, 1);
				goodsList.add(shardBean);
				showMap.add(shardBean);
				break;
			}
		}
		RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
		int code = rewardModule.useGoods(playerID, KindIDs.ENERGY, costEnergy * realNum, 0, true, null, itemMap, ioMessage);
		if (code != 0) {
			protocol.setCode(code);
			return;
		}
		LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule, LoginModule.class);
		PlayerEntity playerEntity = loginModule.getPlayerEntity(playerID);
		GoodsBean expBean = new GoodsBean(KindIDs.LEADEXP, playerEntity.getLevel() * 2 * realNum);
		goodsList.add(expBean);
		GoodsBean silverBean = new GoodsBean(KindIDs.SILVERTYPE, playerEntity.getLevel() * 100 * realNum);
		goodsList.add(silverBean);
		protocol.setDrawList(getDrawList(goodsList, realNum));
		rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
		rewardModule.addGoods(playerID, goodsList, true, null, itemMap, ioMessage);
		protocol.setShowMap(showMap);
		protocol.setItemMap(itemMap);
		protocol.setNum(realNum);
		DayTaskModule dayTaskModule = ModuleManager.getModule(ModuleNames.DayTaskModule, DayTaskModule.class);
		dayTaskModule.addScore(playerID, ActionType.TALISMANPLUNDER, realNum);
		MainTaskModule mainTaskModule = ModuleManager.getModule(ModuleNames.MainTaskModule,MainTaskModule.class);
		mainTaskModule.updateTaskByActionType(playerID, ActionType.TALISMANPLUNDERNUM, realNum, ioMessage);
		EventModule eventModule = ModuleManager.getModule(ModuleNames.EventModule, EventModule.class);
		eventModule.intefaceDrawIntegral(playerID, 10891, realNum);

	}

	/**
	 * 新手掠夺
	 * */
	public void newPlayerPlunderShard(String playerID, IOMessage ioMessage, TalismanProtocol protocol) {
		int shardID = 102387;
		TalismanShardData shardData = TemplateManager.getTemplateData(shardID, TalismanShardData.class);
		this.checkPlunder(playerID, shardData);
		Map<String, Object> itemMap = new HashMap<>();
		RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
		int code = rewardModule.useGoods(playerID, KindIDs.ENERGY, costEnergy, 0, true, null, itemMap, ioMessage);
		if (code != 0) {
			protocol.setCode(code);
			return;
		}
		HeroModule heroModule = ModuleManager.getModule(ModuleNames.HeroModule, HeroModule.class);
		Hero lead = heroModule.getLead(playerID, ioMessage);
		int level = lead.getLevel();
		List<GoodsBean> goodsList = new ArrayList<>();
		List<GoodsBean> showMap = new ArrayList<>();
		GoodsBean silverBean = new GoodsBean(KindIDs.SILVERTYPE, level * 100);
		goodsList.add(silverBean);
		showMap.add(silverBean);
		GoodsBean expBean = new GoodsBean(KindIDs.LEADEXP, level * 2);
		goodsList.add(expBean);
		showMap.add(expBean);
		GoodsBean shardBean = new GoodsBean(shardID, 1);
		goodsList.add(shardBean);
		showMap.add(shardBean);
		protocol.setDrawList(getDrawList(playerID,goodsList,ioMessage));
		protocol.setShowMap(showMap);
		rewardModule.addGoods(playerID, goodsList, true, null, itemMap, ioMessage);
		protocol.setItemMap(itemMap);
	}

	private void checkPlunderEffect(String plunderID) {
		long nowTime = System.currentTimeMillis();
		EffectModule effectModule = ModuleManager.getModule(ModuleNames.EffectModule, EffectModule.class);
		PlayerEffectEntity plunderEffectEntity = effectModule.getPlayerEffectEntity(plunderID);
		Map<Integer, Effect> plundereffectMap = plunderEffectEntity.getEffectMap();
		Effect plunderEffect = plundereffectMap.get(EffectType.refuseBattle);
		if (plunderEffect != null) {
			long plunderEndTime = plunderEffect.getEndTime();
			if (plunderEndTime > nowTime) {
				logger.error("用户在保护状态");
				throw new IllegalArgumentException(ErrorIds.InTalismanProtect + "");
			}
		}

	}

	private void checkSelfEffect(String playerID, TalismanProtocol protocol) {
		long nowTime = System.currentTimeMillis();
		EffectModule effectModule = ModuleManager.getModule(ModuleNames.EffectModule, EffectModule.class);
		PlayerEffectEntity playerEffectEntity = effectModule.getPlayerEffectEntity(playerID);
		Map<Integer, Effect> effectMap = playerEffectEntity.getEffectMap();
		Effect effect = effectMap.get(EffectType.refuseBattle);

		if (effect != null) {
			long endTime = effect.getEndTime();
			if (endTime > nowTime) {
				effect.setEndTime(0);
				protocol.setEffectEndTime(-1);
				effectModule.setPlayerEffectEntity(playerEffectEntity);
			}
		}
	}
	
	/**
	 * 检查是否可以掠夺
	 * */
	public void checkPlunder(String playerID,int shardID,String plunderID){
		boolean isNpc = Utilities.isNpc(plunderID);
		if (!isNpc) {
			this.checkPlunderEffect(plunderID);
			if (!canAttackPlayerTime()) {
				logger.error("不在抢夺时间内");
				throw new IllegalArgumentException(ErrorIds.NoInPlunderTime + "");
			}
		}
		TalismanShard talismanShard1 = this.getTalismanShard1(plunderID, shardID);
		if (talismanShard1 == null || talismanShard1.getNum() < 1) {
			logger.error("碎片不足");
			throw new IllegalArgumentException(ErrorIds.ShardNotEnough + "");
		}
		boolean isEnough = this.checkShardEnough(shardID, plunderID);
		if(!isEnough){
			logger.error("碎片只剩最后一片");
			throw new IllegalArgumentException(ErrorIds.ShardNotEnough + "");
		}
	}
	
	public boolean checkShardEnough(int shardID,String playerID){
		TalismanShardData shardData = TemplateManager.getTemplateData(shardID, TalismanShardData.class);
		int talismanID = shardData.getTalismanID();
		TalismanData talismanData = TemplateManager.getTemplateData(talismanID,TalismanData.class);
		if(talismanData != null){
			int isDefult = shardData.getIsDefult();
			if(isDefult != 1){
				List<Integer> talismanList = talismanData.getComposeID();
				boolean isFind = false;
				for (int tempID : talismanList) {
					if (tempID == shardID) {
						continue;
					}
					TalismanShard shard = this.getTalismanShard1(playerID, tempID);
					if (shard != null && shard.getNum() > 0) {
						isFind = true;
						break;
					}
				}
				if(!isFind){
					return false;
				}
			}
		}
		return true;
	}
	
	public int getPlunderDropID(String playerID,IOMessage ioMessage){
		int dropID = drawDropID;
		PlunderNumEntity plunderNumEntity = this.getTalismanShardNumEntity(playerID,ioMessage);
		int num = plunderNumEntity.getNum();
		if(num <= PlunderDefine.PLUNDEFIFTEEN){
				switch(num){
				case PlunderDefine.PLUNDERSECONDE:
					dropID = PlunderDefine.SECONDEDROPID;		
					break;
				case PlunderDefine.PLUNDERFIVE:
					dropID = PlunderDefine.FIVEDROPID;
					break;
				case PlunderDefine.PLUNDERNINE:
					dropID = PlunderDefine.NINEDROPID;
					break;
				case PlunderDefine.PLUNDEFIFTEEN:
					dropID = PlunderDefine.FIFTEENDROPID;
					break;
				default:
					break;
			}
			num ++;
			plunderNumEntity.setNum(num);
			this.savePlunderShardNumEntity(plunderNumEntity);
		}
		return dropID;
	}
	
	/**
	 * 掠夺碎片
	 * */
	public void plunderShard(String playerID, int shardID, String plunderID, boolean win,String battleString, IOMessage ioMessage, TalismanProtocol protocol) {

		Map<String, Object> itemMap = new HashMap<>();
		TalismanShardData shardData = TemplateManager.getTemplateData(shardID, TalismanShardData.class);
		this.checkPlunder(playerID, shardData);
		int rare = 0;
		boolean isNpc = Utilities.isNpc(plunderID);
//		if (!isNpc) {
//			this.checkPlunderEffect(plunderID);
//			if (!canAttackPlayerTime()) {
//				logger.error("不在抢夺时间内");
//				throw new IllegalArgumentException(ErrorIds.NoInPlunderTime + "");
//			}
//		}
		//List<GoodsBean> plunderGoodsBean = new ArrayList<GoodsBean>();
		if (isNpc) {
			rare = shardData.getNPCRate();
		} else {
			TalismanShard talismanShard1 = this.getTalismanShard1(plunderID, shardID);
			if (talismanShard1 == null || talismanShard1.getNum() < 1) {
//				logger.error("碎片不足");
//				throw new IllegalArgumentException(ErrorIds.ShardNotEnough + "");
				rare = 0;
			}else{
				boolean isEnough = this.checkShardEnough(shardID, plunderID);
				if(!isEnough){
					rare = 0;
				}else{
					rare = shardData.getPlayerRate();
				}
			}
//			GoodsBean goodsBean = new GoodsBean(shardID, 1);
//			plunderGoodsBean.add(goodsBean);
		}
		RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
		int code = rewardModule.useGoods(playerID, KindIDs.ENERGY, costEnergy, 0, true, null, itemMap, ioMessage);
		if (code != 0) {
			protocol.setCode(code);
			return;
		}
		if (!isNpc) {
			this.checkSelfEffect(playerID, protocol);
		}
		HeroModule heroModule = ModuleManager.getModule(ModuleNames.HeroModule, HeroModule.class);
		Hero lead = heroModule.getLead(playerID, ioMessage);
		int level = lead.getLevel();
		List<GoodsBean> goodsList = new ArrayList<>();
		List<GoodsBean> showMap = new ArrayList<>();
		GoodsBean expBean = new GoodsBean(KindIDs.LEADEXP, level * 2);
		goodsList.add(expBean);
		showMap.add(expBean);
		MailBoxModule mailBoxModule = ModuleManager.getModule(ModuleNames.MailBoxModule,MailBoxModule.class);
		Map<String, Object> msgParam = new HashMap<>();
		String color = this.getShardColor(shardData.getQuality());
		msgParam.put("color", color);
		msgParam.put("shardName",shardData.getName());
		if (!Utilities.isNpc(plunderID)) {
			if (!battleString.isEmpty()) {
				BattleReportModule battleReportModule = ModuleManager.getModule(ModuleNames.BattleReportModule, BattleReportModule.class);
				try {
					// 压缩
					byte[] bytes = GZIPUtil.compress(battleString);
					// 编码
					String base64 = new String(Base64Coder.encode(bytes));
					// 保存
					long reportID = battleReportModule.saveReport(base64);
					msgParam.put("reportID", reportID);
				} catch (Exception e) {
					logger.error("战斗录像压缩存储错误!");
				}
			}
		}
		if (win) {
			GoodsBean silverBean = new GoodsBean(KindIDs.SILVERTYPE, level * 100);
			goodsList.add(silverBean);
			showMap.add(silverBean);
			int random = Utilities.getRandomInt(100);
			if (rare > random) {
				GoodsBean shardBean = new GoodsBean(shardID, 1);
				goodsList.add(shardBean);
				showMap.add(shardBean);
				try {
					this.delShard(plunderID, shardID, null, ioMessage);
					if(!isNpc){
						mailBoxModule.addMail(plunderID, null, playerID, null, MailType.BATTLETYPE, BattleMsgType.PLUNDERSHARDSUCCESS, msgParam);
					}
				} catch (IllegalArgumentException ex) {
					goodsList.remove(shardBean);
					showMap.remove(shardBean);
				}
			}
			protocol.setDrawList(getDrawList(playerID,goodsList,ioMessage));
		}else{
			if(!isNpc){
				mailBoxModule.addMail(plunderID, null, playerID, null, MailType.BATTLETYPE, BattleMsgType.PLUNDERSHARDFAILED, msgParam);
			}
		}
	
		protocol.setShowMap(showMap);
		rewardModule.addGoods(playerID, goodsList, true, null, itemMap, ioMessage);
		protocol.setItemMap(itemMap);
		DayTaskModule dayTaskModule = ModuleManager.getModule(ModuleNames.DayTaskModule, DayTaskModule.class);
		dayTaskModule.addScore(playerID, ActionType.TALISMANPLUNDER, 1);
		AchievementModule acModule = ModuleManager.getModule(ModuleNames.AchievementModule, AchievementModule.class);
		acModule.refreshAchievement(playerID, ActionType.TALISMANPLUNDERNUM, 1);
		EventModule eventModule = ModuleManager.getModule(ModuleNames.EventModule, EventModule.class);
		eventModule.intefaceDrawIntegral(playerID, 10891);
		MainTaskModule mainTaskModule = ModuleManager.getModule(ModuleNames.MainTaskModule,MainTaskModule.class);
		mainTaskModule.updateTaskByActionType(playerID, ActionType.TALISMANPLUNDERNUM, 1, null);
	}
	
	private String getShardColor(int quality){
		String color = "";
		switch(quality){
			case 3:
				color = "[00FF00]";
				break;
			case 4:
				color = "[00FFFF]";
				break;
			case 5:
				color = "[d514f4]";
				break;
		}
		return color;
	}

	private List<DrawInfo> getDrawList(List<GoodsBean> goodsList, int num) {
		List<DrawInfo> drawList = new ArrayList<DrawInfo>();
		int dropID = 0;
		FestivalModule festivalModule = ModuleManager.getModule(ModuleNames.FestivalModule,FestivalModule.class);
		if(festivalModule.isInValentine()){
			dropID = valentineDropID;
		}else if(festivalModule.isInFoolDay())
		{
			dropID = foolDayDropID;
		}else{
			dropID = drawDropID;
		}
		for (int i = 1; i <= num; i++) {
			GoodsBean dropBean = DropModule.doDrop(dropID);
			DrawInfo drawInfo = new DrawInfo();
			drawInfo.setDraw(true);
			drawInfo.setPid(dropBean.getPid());
			drawInfo.setNum(dropBean.getNum());
			drawList.add(drawInfo);
			boolean isFind = false;
			for (GoodsBean goodsBean : goodsList) {
				if (goodsBean.getPid() == dropBean.getPid()) {
					goodsBean.setNum(goodsBean.getNum() + dropBean.getNum());
					isFind = true;
					break;
				}
			}
			if (!isFind) {
				goodsList.add(dropBean);
			}
		}
		return drawList;
	}

	private List<DrawInfo> getDrawList(String playerID,List<GoodsBean> goodsList,IOMessage ioMessage) {
		List<DrawInfo> drawList = new ArrayList<DrawInfo>();
		int dropID = this.getPlunderDropID(playerID, ioMessage);
		FestivalModule festivalModule = ModuleManager.getModule(ModuleNames.FestivalModule,FestivalModule.class);
		if(dropID == drawDropID){
			if(festivalModule.isInValentine()){
				dropID = valentineDropID;
			}
			if(festivalModule.isInFoolDay()){
				dropID = foolDayDropID;
			}
		}
		GoodsBean dropBean = DropModule.doDrop(dropID);
		DrawInfo drawInfo = new DrawInfo();
		drawInfo.setDraw(true);
		drawInfo.setPid(dropBean.getPid());
		drawInfo.setNum(dropBean.getNum());
		drawList.add(drawInfo);
		boolean isFind = false;
		for (GoodsBean goodsBean : goodsList) {
			if (goodsBean.getPid() == dropBean.getPid()) {
				goodsBean.setNum(goodsBean.getNum() + dropBean.getNum());
				isFind = true;
				break;
			}
		}
		if (!isFind) {
			goodsList.add(dropBean);
		}
		for (int i = 0; i < 2; i++) {
			DrawInfo unDrawInfo = new DrawInfo();
			GoodsBean unDropBean = DropModule.doDrop(drawDropID);
			unDrawInfo.setNum(unDropBean.getNum());
			unDrawInfo.setPid(unDropBean.getPid());
			drawList.add(unDrawInfo);
		}
		return drawList;
	}

	public void talismanShardCompose(String playerID, int talismanID, TalismanProtocol protocol, IOMessage ioMessage) {
		TalismanData data = TemplateManager.getTemplateData(talismanID, TalismanData.class);
		List<Integer> composeList = data.getComposeID();
		for (int composeID : composeList) {
			TalismanShard talismanShard = this.getTalismanShard1(playerID, composeID, ioMessage);
			if (talismanShard == null || talismanShard.getNum() < 1) {
				List<TalismanShard> talismanShardList = this.getTalismanShardList(playerID);
				protocol.setShardList(talismanShardList);
				throw new IllegalArgumentException(ErrorIds.ShardNotEnough + "");
			}
		}
		List<TalismanShard> removeList = new ArrayList<>();
		for (int composeID : composeList) {
			this.delShard(playerID, composeID, removeList, ioMessage);
		}

		protocol.setShardList(removeList);
		Map<String, Object> itemMap = new HashMap<>();
		RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
		rewardModule.addGoods(playerID, talismanID, 1, null, true, null, itemMap, ioMessage);
		MainTaskModule mainTaskModule = ModuleManager.getModule(ModuleNames.MainTaskModule,MainTaskModule.class);
		int part = data.getPart();
		if(part == EquipmentPart.horse){
			mainTaskModule.updateTaskByActionType(playerID, ActionType.COMPOUNDHORSE, 0, ioMessage);
		}else{
			mainTaskModule.updateTaskByActionType(playerID, ActionType.COMPOUNDTALISMAN, 0, ioMessage);
		}
		protocol.setItemMap(itemMap);
	}

	public void expendTalismanBag(String playerID, TalismanProtocol protocol) {
		TalismanMapEntity entity = this.getEntity(playerID);
		int maxNum = entity.getMaxTalismanNum();
		int gold = ((maxNum - talismanBagInitNum) / 5 + 1) * 25;
		Map<String, Object> itemMap = new HashMap<>();
		RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
		rewardModule.useGoods(playerID, KindIDs.GOLDTYPE, gold, 0, true, null, itemMap, null);

		// ////
		// // 元宝消耗记录
		// ///
		AnalyseModule analyseModule = ModuleManager.getModule(ModuleNames.AnalyseModule, AnalyseModule.class);
		analyseModule.goldCostLog(playerID, gold, 1, gold, "expendTalismanBag", "talisman");

		entity.setMaxTalismanNum(maxNum + 5);
		protocol.setMaxNum(entity.getMaxTalismanNum());
		protocol.setItemMap(itemMap);
		this.saveEntity(entity);
	}

	public List<TalismanShard> getTalismanShardList(String playerID) {
		return talismanShard1DAO.getTalismanShardList(playerID);
	}

	private List<Integer> getHeroTalismanList(String playerID, Hero hero) {
		TalismanMapEntity talismanMapEntity = this.getEntity(playerID);
		Map<String, TalismanEntity> talismanMap = talismanMapEntity.getTalismanMap();
		Map<String, Long> equipMap = hero.getEquipMap();
		List<Integer> talismanList = new ArrayList<>();
		for (Entry<String, Long> entry : equipMap.entrySet()) {
			long value = entry.getValue();
			TalismanEntity talismanEntity = talismanMap.get(value + "");
			if (talismanEntity != null) {
				talismanList.add(talismanEntity.getTemplateID());
			}
		}
		return talismanList;
	}

	/**
	 * 修改玩家的宝物羁绊
	 * */
	public void changeTalismanFriendship(String playerID, Hero hero) {
		List<Integer> talismanList = this.getHeroTalismanList(playerID, hero);
		Map<Integer, Double> talismanFriendShipProp = new HashMap<>();
		if(!talismanList.isEmpty()){
			int templateID = hero.getTemplateID();
			HeroData heroData = TemplateManager.getTemplateData(templateID, HeroData.class);
			List<Integer> friendShip = heroData.getFriendship();
			for (int friendShipID : friendShip) {
				FriendShipData friendShipData = TemplateManager.getTemplateData(friendShipID, FriendShipData.class);
				if (friendShipData.getActiveType() == FriendshipType.talismanType) {
					List<Integer> activeLimit = friendShipData.getActiveLimit();
					for (int activeID : activeLimit) {
						if (talismanList.contains(activeID)) {
							Map<Integer, Double> property = friendShipData.getProperty();
							for (Entry<Integer, Double> entry : property.entrySet()) {
								int key = entry.getKey();
								double value = entry.getValue();
								if (talismanFriendShipProp.get(key) != null) {
									talismanFriendShipProp.put(key, talismanFriendShipProp.get(key) + value);
								} else {
									talismanFriendShipProp.put(key, value);
								}
							}
						}
					}
				}
			}
		}
		hero.setTalismanFriendShipProp(talismanFriendShipProp);
	}
}
