package com.mi.game.module.hero;

import java.util.ArrayList;
import java.util.Date;
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
import com.mi.core.job.QuartzJobService;
import com.mi.core.pojo.BaseEntity;
import com.mi.core.pojo.KeyGenerator;
import com.mi.core.util.DateTimeUtil;
import com.mi.game.defines.ActionType;
import com.mi.game.defines.EquipmentPart;
import com.mi.game.defines.ErrorIds;
import com.mi.game.defines.FriendshipType;
import com.mi.game.defines.InformationMessageType;
import com.mi.game.defines.KindIDs;
import com.mi.game.defines.ModuleNames;
import com.mi.game.defines.SysConstants;
import com.mi.game.module.achievement.AchievementModule;
import com.mi.game.module.analyse.AnalyseModule;
import com.mi.game.module.bag.pojo.BagEntity;
import com.mi.game.module.base.BaseModule;
import com.mi.game.module.chat.ChatModule;
import com.mi.game.module.equipment.EquipmentModule;
import com.mi.game.module.equipment.pojo.Equipment;
import com.mi.game.module.equipment.pojo.EquipmentMapEntity;
import com.mi.game.module.hero.dao.HeroDAO;
import com.mi.game.module.hero.dao.HeroSkinEntityDAO;
import com.mi.game.module.hero.dao.HeroTroopsDAO;
import com.mi.game.module.hero.data.AdvanceHeroData;
import com.mi.game.module.hero.data.FriendShipData;
import com.mi.game.module.hero.data.HeroData;
import com.mi.game.module.hero.data.HeroPostionData;
import com.mi.game.module.hero.data.HeroShardData;
import com.mi.game.module.hero.data.TalentData;
import com.mi.game.module.hero.job.FightValueUpdateJob;
import com.mi.game.module.hero.pojo.Hero;
import com.mi.game.module.hero.pojo.HeroEntity;
import com.mi.game.module.hero.pojo.HeroShard;
import com.mi.game.module.hero.pojo.HeroSkinEntity;
import com.mi.game.module.hero.pojo.HeroTroopsEntity;
import com.mi.game.module.hero.protocol.HeroInfoProtocol;
import com.mi.game.module.lead.LeadModule;
import com.mi.game.module.lead.pojo.HeroPrototype;
import com.mi.game.module.lead.pojo.LeadDesitnyEntity;
import com.mi.game.module.login.LoginModule;
import com.mi.game.module.login.pojo.PlayerEntity;
import com.mi.game.module.mainTask.MainTaskModule;
import com.mi.game.module.manual.ManualModule;
import com.mi.game.module.pet.PetModule;
import com.mi.game.module.reward.RewardModule;
import com.mi.game.module.reward.data.GoodsBean;
import com.mi.game.module.talisman.TalismanModule;
import com.mi.game.module.talisman.pojo.TalismanEntity;
import com.mi.game.module.talisman.pojo.TalismanMapEntity;
import com.mi.game.module.wallet.WalletModule;
import com.mi.game.module.wallet.pojo.WalletEntity;
import com.mi.game.util.Utilities;

/**
 * @author 刘凯旋
 *
 *         2014年5月27日 下午5:21:23
 */
@Module(name = ModuleNames.HeroModule, clazz = HeroModule.class)
public class HeroModule extends BaseModule {
	private final HeroDAO heroDAO = HeroDAO.getInstance();
	private final KeyGeneratorDAO keyGeneratorDAO = KeyGeneratorDAO.getInstance();
	private final HeroTroopsDAO heroTroopsDAO = HeroTroopsDAO.getInstance();
	private final HeroSkinEntityDAO heroSkinEntityDAO = HeroSkinEntityDAO.getInstance();
	private final Map<Integer, Integer> HeroPostionData = new HashMap<Integer, Integer>();
	private final static int maxClassLevel = 7;
	private final static long maleHero = SysConstants.maleHero;
	private final static long femaleHero = SysConstants.femaleHero;
	private final static List<GoodsBean> initHeroIDList = new ArrayList<>();
	private final int blueHeroType = 4;
	private final int purpleHeroType = 5;
	private final int maleHeroID = SysConstants.MaleHeroID;
	private final int femaleHeroID = SysConstants.FemaleHeroID;
	private final List<Integer> addPropList = new ArrayList<>();
	private final List<Integer> initHeroShardList = new ArrayList<>();
	private final Map<Integer, Integer> heroPostionMap = new HashMap<>();
	private final int strengMinLevel = 3;
	/**
	 * 初始化
	 * */
	@Override
	public void init() {
		initHeroID();
		initHeroPostion();
		initAdvanceData();
		initHeroIDList();
		initPropList();
		initShardList();
		initPostion();
		initFightValueJob();
	}

	private void initFightValueJob(){
		QuartzJobService quartz = QuartzJobService.getInstance();
		Date date  = Utilities.getOclock();
		String timeString = DateTimeUtil.getStringDate(date);
//		Date date = new Date();
//		String timeString = DateTimeUtil.getStringDate(date);
		quartz.createJob(9999999, (int)DateTimeUtil.ONE_HOUR_TIME_S, timeString, FightValueUpdateJob.class);
	}
	
	private void initPostion() {
		List<HeroPostionData> list = TemplateManager.getTemplateList(HeroPostionData.class);
		for (HeroPostionData data : list) {
			int postion = data.getPostion();
			int level = data.getLevel();
			heroPostionMap.put(postion, level);
		}
	}

	private void initPropList() {
		addPropList.add(SysConstants.Hp);
		addPropList.add(SysConstants.Attack);
		addPropList.add(SysConstants.PDef);
		addPropList.add(SysConstants.MDef);
	}

	private void initShardList() {
		// for(int i = 1; i< 67; i++){
		// initHeroShardList.add(Integer.parseInt("1021" + i ));
		// }
	}

	private void initHeroIDList() {
		initHeroIDList.add(new GoodsBean(1002965, 1));
	}

	private void initAdvanceData() {
		// List<AdvanceHeroData> dataList =
		// TemplateManager.getTemplateList(AdvanceHeroData.class);
		// Map<Integer,AdvanceHeroData> qualityMap = null;
		// for(AdvanceHeroData data : dataList ){
		// int rank = data.getRank();
		// int quality = data.getQuality();
		// if(advanceData.get(quality) == null){
		// qualityMap = new HashMap<>();
		// }else{
		// qualityMap = advanceData.get(quality);
		// }
		// qualityMap.put(rank, data);
		// advanceData.put(quality, qualityMap);
		// }
	}

	/**
	 * 初始化英雄ID
	 * */
	private void initHeroID() {
		String clsName = SysConstants.heroIDEntitiy;
		KeyGenerator keyGenerator = keyGeneratorDAO.getEntity(clsName);
		if (keyGenerator == null) {
			keyGenerator = new KeyGenerator();
			keyGenerator.setKey(clsName);
			keyGenerator.setNextId(SysConstants.heroStartID);
			keyGeneratorDAO.save(keyGenerator);
		}
	}

	/**
	 * 初始化英雄位置解锁数据
	 * */
	private void initHeroPostion() {
		List<HeroPostionData> datas = TemplateManager.getTemplateList(HeroPostionData.class);
		for (HeroPostionData data : datas) {
			HeroPostionData.put(data.getLevel(), data.getPostion());
		}
	}

	public void initHeroMaps(String playerID, IOMessage ioMessage) {
		RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
		Map<String, Object> itemMap = new HashMap<>();
		rewardModule.addGoods(playerID, initHeroIDList, true, null, itemMap, ioMessage);
	}

	/**
	 * 初始化英雄实体
	 * */
	public HeroEntity initHeroEnitiy(String playerID, PlayerEntity playerEntity) {
		HeroEntity entity = new HeroEntity();
		entity.setKey(playerID);
		Map<String, Hero> heroMap = new HashMap<>();
		Hero lead = this.initLead(playerEntity);
		List<Long> teamList = new ArrayList<>();
		for (int i = 0; i < 12; i++) {
			teamList.add(0l);
		}
		heroMap.put(lead.getHeroID() + "", lead);
		teamList.set(0, lead.getHeroID());
		entity.setLeadID(lead.getHeroID());
		entity.setMaxHeroNum(SysConstants.heroBagInitNum);
		entity.setTeamList(teamList);
		entity.setShardMap(initHeroShard());
		entity.setHeroMap(heroMap);
		return entity;
	}

	/** 初始化主角实体 */
	public Hero initLead(PlayerEntity playerEntity) {
		int templateID = 0;
		long heroID = 0;
		if (playerEntity.getSex() == 0) {
			heroID = maleHero;
			templateID = maleHeroID;
		} else {
			heroID = femaleHero;
			templateID = femaleHeroID;
		}
		playerEntity.setPhotoID(templateID);
		Hero hero = new Hero();
		hero.setLevel(1);
		hero.setTemplateID(templateID);
		hero.setHeroID(heroID);
		return hero;
	}

	private Map<String, HeroShard> initHeroShard() {
		Map<String, HeroShard> shardMap = new HashMap<>();
		for (int shardID : initHeroShardList) {
			HeroShard heroShard = new HeroShard();
			heroShard.setShardID(shardID);
			heroShard.setNum(100);
			shardMap.put(shardID + "", heroShard);
		}
		return shardMap;
	}

	/**
	 * 初始化队列
	 * */
	public HeroTroopsEntity initHeroTroops(String playerID, int sex) {
		List<Long> troopsList = new ArrayList<>();
		for (int i = 0; i < 6; i++) {
			troopsList.add(0l);
		}
		long leadID = 0;
		if (sex == 0) {
			leadID = maleHero;
		} else {
			leadID = femaleHero;
		}
		troopsList.set(1, leadID);
		HeroTroopsEntity entity = new HeroTroopsEntity();
		entity.setKey(playerID);
		entity.setTroops(troopsList);
		return entity;
	}

	/**
	 * 获取经验排行
	 * 
	 * @param size
	 * @return
	 */
	public List<String> getExpRank(int size) {
		return heroDAO.getExpRankList(size);
	}

	/**
	 * 保存英雄实体
	 * */

	public void saveHeroEntity(HeroEntity entity) {
		heroDAO.save(entity);
	}

	/**
	 * 保存实体集合
	 * */
	public void saveEntityList(List<BaseEntity> entityList) {
		heroDAO.save(entityList);
	}

	/**
	 * 获取英雄皮肤实体
	 * */
	public HeroSkinEntity getHeroSkin(String playerID) {
		HeroSkinEntity entity = heroSkinEntityDAO.getEntity(playerID);
		if (entity == null) {
			entity = new HeroSkinEntity();
			entity.setKey(playerID);
		}
		return entity;
	}

	/**
	 * 获取英雄实体
	 * */
	public HeroSkinEntity getHerosSkinEntity(String playerID, IOMessage ioMessage) {
		HeroSkinEntity heroSkinEntity = null;
		if (ioMessage != null) {
			String key = HeroSkinEntity.class.getName() + playerID;
			heroSkinEntity = (HeroSkinEntity) ioMessage.getInputParse(key);
			if (heroSkinEntity == null) {
				heroSkinEntity = this.getHeroSkin(playerID);
				ioMessage.getInputParse().put(key, heroSkinEntity);
			}
		} else {
			heroSkinEntity = this.getHeroSkin(playerID);
		}
		return heroSkinEntity;
	}

	/**
	 * 保存英雄皮肤实体
	 * */
	public void saveHeroSkinEntity(HeroSkinEntity heroSkinEntity) {
		heroSkinEntityDAO.save(heroSkinEntity);
	}

	/**
	 * 获取英雄实体
	 * */
	public HeroEntity getHeroEntity(String playerID) {
		HeroEntity heroEntity = heroDAO.getEntity(playerID);
		return heroEntity;
	}

	/**
	 * 获取英雄实体(缓存)
	 * */
	public HeroEntity getHeroEntity(String playerID, IOMessage ioMessage) {
		HeroEntity entity = null;
		if (ioMessage != null) {
			entity = (HeroEntity) ioMessage.getInputParse().get(HeroEntity.class.getName() + playerID);
			if (entity == null) {
				entity = this.getHeroEntity(playerID);
				ioMessage.getInputParse().put(HeroEntity.class.getName() + playerID, entity);
			}
		} else {
			entity = this.getHeroEntity(playerID);
		}
		return entity;
	}

	/**
	 * 获取英雄
	 * */
	public Hero getHero(String playerID, long heroID, IOMessage ioMessage) {
		HeroEntity heroEntity = this.getHeroEntity(playerID, ioMessage);
		Map<String, Hero> heroMap = heroEntity.getHeroMap();
		Hero hero = heroMap.get(heroID + "");
		if (hero == null) {
			logger.error("武将不存在");
			throw new IllegalArgumentException(ErrorIds.HeroNotFound + "");
		}
		return hero;
	}

	/**
	 * 获取英雄队伍
	 * */
	public HeroTroopsEntity getHeroTroopsEntity(String playerID) {
		return heroTroopsDAO.getEntity(playerID);
	}

	/**
	 * 保存英雄队伍
	 * */
	public void saveHeroTroops(HeroTroopsEntity entity) {
		heroTroopsDAO.save(entity);
	}

	/**
	 * 获取玩家英雄
	 * */

	public Hero getLead(String playerID, IOMessage ioMessage) {
		HeroEntity heroEntity = this.getHeroEntity(playerID, ioMessage);
		long leadID = heroEntity.getLeadID();
		Map<String, Hero> heroMap = heroEntity.getHeroMap();
		return heroMap.get(leadID + "");
	}

	/**
	 * 添加英雄到队伍
	 * 
	 * @param long heroID 英雄唯一ID
	 * @param int postion 英雄要添加到的位置
	 * 
	 * */

	public void addHeroToTeam(String playerID, long heroID, int postion, HeroInfoProtocol protocol,IOMessage ioMessage) {
		HeroEntity heroEntity = this.getHeroEntity(playerID,ioMessage);
		Map<String, Hero> heroMaps = heroEntity.getHeroMap();
		Hero hero = heroMaps.get(heroID + "");
		List<Long> teamList = heroEntity.getTeamList();
		if (postion == 0 || heroID == femaleHero || heroID == maleHero) {
			logger.error("主角位置不可替换");
			protocol.setCode(ErrorIds.HeroPostionNoChange);
			return;
		}
		// 检验位置与是否有相同武将
		int code = checkPostion(playerID, postion, heroMaps, teamList, heroID);
		if (code != 0) {
			protocol.setCode(code);
		} else {
			// 自动替换上阵武将的位置
			if (postion < 6) {
				long tempHeroID = teamList.get(postion);
				HeroTroopsEntity troopsEntity = this.getHeroTroopsEntity(playerID);
				List<Long> troops = troopsEntity.getTroops();
				if (troops.contains(heroID)) {
					throw new IllegalArgumentException(ErrorIds.HeroAlreadyInTroops + "");
				}
				List<TalismanEntity> talismanList = new ArrayList<>();
				List<Equipment> equipmentList = new ArrayList<>();
				if (tempHeroID != 0) {
					TalismanModule talismanModule = ModuleManager.getModule(ModuleNames.TalismanModule, TalismanModule.class);
					TalismanMapEntity talismanMapEntity = talismanModule.getEntity(playerID);
					Map<String, TalismanEntity> talismanMap = talismanMapEntity.getTalismanMap();
					boolean isTalismanChange = false;
					boolean isEquipmentChange = false;
					Hero oldHero = heroMaps.get(tempHeroID + "");
					Map<String, Long> equipMap = oldHero.getEquipMap();
					hero.setEquipMap(equipMap);
					EquipmentModule equipmentModule = ModuleManager.getModule(ModuleNames.EquipmentModule, EquipmentModule.class);
					EquipmentMapEntity entity = equipmentModule.getEquipmentMapEntity(playerID,ioMessage);
					Map<String, Equipment> allEquipment = entity.getEquipMap();
					for (Entry<String, Long> entry : equipMap.entrySet()) {
						long equipID = entry.getValue();
						int part = Integer.parseInt(entry.getKey().toString());
						if (part != EquipmentPart.horse && part != EquipmentPart.book) {
							Equipment equipment = allEquipment.get(equipID + "");
							if (equipment != null) {
								isEquipmentChange = true;
								equipment.setHeroID(heroID);
								equipmentList.add(equipment);
							} 
						}else {
							isTalismanChange = true;
							TalismanEntity talismanEntity = talismanMap.get(equipID + "");
							if (talismanEntity != null) {
								talismanEntity.setHeroID(heroID);
								talismanList.add(talismanEntity);
							}
						}
					}
					if (isTalismanChange) {
						talismanModule.saveEntity(talismanMapEntity);
					}
					if (isEquipmentChange) {
						equipmentModule.saveEquipmentEntity(entity);
					}
					oldHero.setEquipMap(null);
					protocol.setChangeHero(oldHero);
					int troopsIndex = troops.indexOf(tempHeroID);
					if (troopsIndex == -1) {
						throw new IllegalArgumentException(ErrorIds.WrongHeroPostion + "");
					}
					troops.set(troopsIndex, heroID);
					List<Integer> tempList = equipmentModule.getHeroEquipmentList(playerID, hero,ioMessage);
					talismanModule.changeTalismanFriendship(playerID, hero);
					equipmentModule.changeEquipmentFriendship(playerID, hero,tempList);
					equipmentModule.changeEquipmentSet(tempList, hero);
				} else {
					if (troops.get(4) == 0) {
						troops.set(4, heroID);
					} else {
						if (troops.get(2) == 0) {
							troops.set(2, heroID);
						} else {
							for (int i = 0; i < 6; i++) {
								if (troops.get(i) == 0) {
									troops.set(i, heroID);
									break;
								}
							}
						}
					}
				}
			
				this.saveHeroTroops(troopsEntity);
				protocol.setTroops(troops);
				if (!talismanList.isEmpty()) {
					protocol.setTalismanList(talismanList);
				}
				if (!equipmentList.isEmpty()) {
					protocol.setEquipmentList(equipmentList);
				}
			}
			
			protocol.setHero(hero);
			teamList.set(postion, heroID);
			changeFriendShip(playerID, teamList, heroMaps, heroID);
		
			heroEntity.setTeamList(teamList);
			protocol.setTeamList(teamList);
			this.saveHeroEntity(heroEntity);
			MainTaskModule mainTaskModule = ModuleManager.getModule(ModuleNames.MainTaskModule,MainTaskModule.class);
			mainTaskModule.updateTaskByActionType(playerID, ActionType.BLUEHERONUM, 0, ioMessage);
			mainTaskModule.updateTaskByActionType(playerID, ActionType.PURPLEHERONUM, 0, ioMessage);
		}
	}

	private void changeFriendShip(String playerID, List<Long> teamList, Map<String, Hero> heroMaps, long changeHeroID) {
		List<Integer> heroList = new ArrayList<>();
		for (long teamHeroID : teamList) {
			if (teamHeroID != 0) {
				int templateID = heroMaps.get(teamHeroID + "").getTemplateID();
				HeroData heroData = TemplateManager.getTemplateData(templateID, HeroData.class);
				heroList.add(heroData.getCharactorID());
			}
		}
		for (int i = 0; i < 6; i++) {
			long heroID = teamList.get(i);
			if (heroID != 0 && heroID != femaleHero && heroID != maleHero) {
				Hero oldHero = heroMaps.get(heroID + "");
				Map<Integer, Double> heroFriendshipProp = new HashMap<>();
				int oldTemplateID = oldHero.getTemplateID();
				HeroData heroData = TemplateManager.getTemplateData(oldTemplateID, HeroData.class);
				List<Integer> friendship = heroData.getFriendship();
				for (int friendshipID : friendship) {
					FriendShipData friendShipData = TemplateManager.getTemplateData(friendshipID, FriendShipData.class);
					if (friendShipData.getActiveType() == FriendshipType.heroType) {
						List<Integer> activeLimit = friendShipData.getActiveLimit();
						boolean complete = true;
						for (int activeID : activeLimit) {
							if (!heroList.contains(activeID)) {
								complete = false;
								break;
							}
						}
						if (complete) {
							Map<Integer, Double> property = friendShipData.getProperty();
							for (Entry<Integer, Double> entry : property.entrySet()) {
								double value = entry.getValue();
								int key = entry.getKey();
								if (heroFriendshipProp.containsKey(key)) {
									heroFriendshipProp.put(key, heroFriendshipProp.get(key) + value);
								} else {
									heroFriendshipProp.put(key, value);
								}
							}
						}
					}
				}
				oldHero.setHeroFriendShipProp(heroFriendshipProp);
			}
		}
		Hero chanageHero = heroMaps.get(changeHeroID + "");
		TalismanModule talismanModule = ModuleManager.getModule(ModuleNames.TalismanModule, TalismanModule.class);
		talismanModule.changeTalismanFriendship(playerID, chanageHero);
	}

	/**
	 * 卸下武将 将卸下的武将位置归0 不能直接remove
	 * **/
	public void dischargeHero(String playerID, long heroID, HeroInfoProtocol protocol) {
		if (heroID == femaleHero || heroID == maleHero) {
			logger.error("主角位置不可替换");
			protocol.setCode(ErrorIds.HeroPostionNoChange);
			return;
		}
		HeroEntity heroEntity = this.getHeroEntity(playerID);
		List<Long> teamList = heroEntity.getTeamList();
		// 修改上阵阵型
		HeroTroopsEntity troopsEntity = this.getHeroTroopsEntity(playerID);
		List<Long> troops = troopsEntity.getTroops();
		if (troops.contains(heroID)) {
			protocol.setCode(ErrorIds.TroopsHeroUnDischarge);
			return;
			// throw new IllegalArgumentException(ErrorIds.TroopsHeroUnDischarge
			// + "");
			// int troopsIndex = troops.indexOf(heroID);
			// if(troopsIndex != -1){
			// troops.set(troopsIndex, 0l);
			// }
			// this.saveHeroTroops(troopsEntity);
			// protocol.setTroops(troops);
		}
		int teamIndex = teamList.indexOf(heroID);
		if (teamIndex != -1) {
			teamList.set(teamIndex, 0l);
		}

		protocol.setTeamList(teamList);
		this.saveHeroEntity(heroEntity);
	}

	/**
	 * 修改阵型
	 * */
	public List<Long> changeTroops(String playerID, long heroID, int postion) {
		HeroTroopsEntity troopsEntity = this.getHeroTroopsEntity(playerID);
		List<Long> troops = troopsEntity.getTroops();
		long changeHero = troops.get(postion);
		int index = troops.indexOf(heroID);
		if (index == -1) {
			/** 不存在移动的武将 */
		} else {
			troops.set(postion, heroID);
			troops.set(index, changeHero);
		}
		troopsEntity.setTroops(troops);
		this.saveHeroTroops(troopsEntity);
		return troops;
	}

	/**
	 * 检查是否开启该位置
	 * */
	private int checkPostion(String playerID, int postion, Map<String, Hero> heroMaps, List<Long> teamList, long heroID) {
		LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule, LoginModule.class);
		PlayerEntity playerEntity = loginModule.getPlayerEntity(playerID);
		int level = playerEntity.getLevel();
		int needLevel = heroPostionMap.get(postion);
		if (level < needLevel) {
			throw new IllegalArgumentException(ErrorIds.HeroPostionWrong + "");
		}
		int heroTemplateID = heroMaps.get(heroID + "").getTemplateID();
		for (long teamHeroID : teamList) {
			if (teamHeroID == 0) {
				continue;
			}
			Hero hero = heroMaps.get(teamHeroID + "");
			if (hero != null) {
				int teamTemplateID = heroMaps.get(teamHeroID + "").getTemplateID();
				if (teamTemplateID == heroTemplateID) {
					return ErrorIds.SameHeroAdd;
				}
			}
		}

		return 0;
	}

	/**
	 * 获取英雄唯一ID
	 * */

	public long getHeroID() {
		String clsName = SysConstants.heroIDEntitiy;
		long heroID = keyGeneratorDAO.updateInc(clsName);
		return heroID;
	}

	/**
	 * 武将强化(通过武将材料强化)
	 * 
	 * **/
	public void strengHero(String playerID, Hero hero, Map<String, Hero> heroList, List<Object> strengList, HeroInfoProtocol protocol, int playerLevel) {
		int exp = 0;
		for (Object temp : strengList) {
			long strengHeroID = Long.parseLong(temp.toString());
			Hero strengHero = heroList.get(strengHeroID + "");
			if (strengHero == null) {
				if (strengList != null) {
					logger.error("武将不存在" + playerID + strengList.toString());
				} else {
					logger.error("武将不存在null" + playerID);
				}
				protocol.setCode(ErrorIds.HeroNotFound);
				return;
			}
			int templateID = strengHero.getTemplateID();
			int heroExp = strengHero.getExp();
			HeroData heroData = TemplateManager.getTemplateData(templateID, HeroData.class);
			if (heroData.getCanSwallow() != 1) {
				logger.error("武将不可作为强化材料");
				protocol.setCode(ErrorIds.NoStrengHero);
				return;
			}
			exp += heroExp + heroData.getSwallowExp();
			heroList.remove(strengHeroID + "");
		}
		Map<String, Object> itemMap = new HashMap<String, Object>();
		this.addHeroExp(playerID, hero, exp, playerLevel,itemMap);

		RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
		int code = rewardModule.useGoods(playerID, KindIDs.SILVERTYPE, exp, 0, true, null, itemMap, null);
		if (code != 0) {
			throw new IllegalArgumentException(code + "");
		}
		itemMap.put("removeHeroList", strengList);
		protocol.setHero(hero);
		protocol.setItemMap(itemMap);

	}

	public void doStreng(int type, String playerID, String heroID, List<Object> strengList, IOMessage ioMessage, HeroInfoProtocol protocol) {
		if (heroID.equals(femaleHero + "") || heroID.equals(maleHero + "")) {
			logger.error("主角不可强化");
			protocol.setCode(ErrorIds.HeroNoStreng);
			return;
		}
		LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule, LoginModule.class);
		PlayerEntity playerEntity = loginModule.getPlayerEntity(playerID);
		int level = playerEntity.getLevel();
		if(level < strengMinLevel){
			logger.error("主角等级小于3级不可强化装备");
			throw new IllegalArgumentException(ErrorIds.LEADLEVELSTRENGEHERONOENOUGH + "");
		}
		HeroEntity heroEntity = this.getHeroEntity(playerID);
		Map<String, Hero> heroList = heroEntity.getHeroMap();
		Hero hero = heroList.get(heroID);
		if (hero == null) {
			throw new IllegalArgumentException(ErrorIds.HeroNotFound + "");
		}
		int heroLevel = hero.getLevel();
		if (heroLevel >= level) {
			throw new IllegalArgumentException(ErrorIds.HeroLevelNoExceedPlayerLevel + "");
		}
		switch (type) {
		case 1:
			this.strengHero(playerID, hero, heroList, strengList, protocol, level);
			break;
		case 2:
			this.strengHero(playerID, hero, ioMessage, protocol, 1);
			break;
		case 3:
			int levelup = level - heroLevel;
			if (levelup > 5) {
				levelup = 5;
			}
			this.strengHero(playerID, hero, ioMessage, protocol, levelup);
			break;
		default:
			logger.error("错误的强化类型");
			protocol.setCode(ErrorIds.WrongHeroStrengType);
			break;
		}
		JSONObject params = new JSONObject();
		int templateID = hero.getTemplateID();
		HeroData data = TemplateManager.getTemplateData(templateID, HeroData.class);
		params.put("quality", data.getQuality());
		params.put("level", hero.getLevel());
		MainTaskModule mainTaskModule = ModuleManager.getModule(ModuleNames.MainTaskModule,MainTaskModule.class);
		mainTaskModule.updateTaskByActionType(playerID, ActionType.HEROLEVEL,  hero.getLevel(), null);
		AnalyseModule analyseModule = ModuleManager.getModule(ModuleNames.AnalyseModule, AnalyseModule.class);
		analyseModule.analyLog(playerID, "DISCIPLE_ENHENCE", params, null, null, null);
		this.saveHeroEntity(heroEntity);
	}

	/**
	 * 武将强化(将魂)
	 * */
	public void strengHero(String playerID, Hero hero, IOMessage ioMessage, HeroInfoProtocol protocol, int levelup) {

		int exp = hero.getExp();
		int level = hero.getLevel();
		int templateID = hero.getTemplateID();
		HeroData heroData = TemplateManager.getTemplateData(templateID, HeroData.class);
		int quality = heroData.getQuality();
		WalletModule walletModule = ModuleManager.getModule(ModuleNames.WalletModule, WalletModule.class);
		int temp = 0;
		for (int i = 1; i <= levelup; i++) {
			int nextExp = this.getHeroNextExp(level + i, quality);
			int needExp = nextExp - exp;
			int code = walletModule.verifyCurrency(playerID, KindIDs.HEROSOUL, needExp, ioMessage);
			if (code != 0) {
				break;
			} else {
				temp += 1;
			}
		}
		if (temp < 1) {
			logger.error("将魂不足");
			throw new IllegalArgumentException(ErrorIds.NotEnoughHeroSoul + "");
		}
		levelup = temp;
		int nextExp = this.getHeroNextExp(level + levelup, quality);
		int needExp = nextExp - exp;
		List<GoodsBean> list = new ArrayList<GoodsBean>();
		GoodsBean soulGoods = new GoodsBean();
		soulGoods.setPid(KindIDs.HEROSOUL);
		soulGoods.setNum(needExp);
		GoodsBean silverGoods = new GoodsBean();
		silverGoods.setPid(KindIDs.SILVERTYPE);
		silverGoods.setNum(needExp);
		list.add(silverGoods);
		list.add(soulGoods);
		Map<String, Object> itemMap = new HashMap<String, Object>();
		RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
		int code = rewardModule.useGoods(playerID, list, 0, false, null, itemMap, ioMessage);
		if (code != 0) {
			logger.error("将魂不足 或银币不足");
			throw new IllegalArgumentException(code + "");
		}
		WalletEntity walletEntity = (WalletEntity) ioMessage.getInputParse().get(WalletEntity.class.getName());
		if (walletEntity == null) {
			logger.error("钱包实体为空");
			throw new IllegalArgumentException(ErrorIds.WalletNull + "");
		}
		protocol.setItemMap(itemMap);
		protocol.setHero(hero);
		walletModule.saveWalletEntity(walletEntity);
		hero.setLevel(level + levelup);
		hero.setExp(nextExp);
		AchievementModule acModule = ModuleManager.getModule(ModuleNames.AchievementModule, AchievementModule.class);
		acModule.refreshAchievement(playerID, ActionType.HEROLEVEL, hero.getLevel());
	}

	/**
	 * 武将增加经验
	 * 
	 * */

	private Hero addHeroExp(String playerID, Hero hero, int exp, int playerLevel,Map<String, Object> itemMap) {
		int templateID = hero.getTemplateID();
		int nowExp = hero.getExp();
		HeroData heroData = TemplateManager.getTemplateData(templateID, HeroData.class);
		int quality = heroData.getQuality();
		int allExp = nowExp + exp;
		int level = hero.getLevel();
		int nowLevel = this.getHeroExpLevel(quality, allExp, level);
		if (nowLevel > playerLevel) {
			int tempExp = this.getHeroNextExp(playerLevel, quality);
			int heroSoul = allExp - tempExp;
			if(heroSoul > 0){
				RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule,RewardModule.class);
				rewardModule.addGoods(playerID, KindIDs.HEROSOUL, heroSoul, 0, true, null, itemMap, null);
			}
			hero.setExp(tempExp);
			hero.setLevel(playerLevel);
		} else {
			hero.setExp(allExp);
			hero.setLevel(nowLevel);
		}
		AchievementModule acModule = ModuleManager.getModule(ModuleNames.AchievementModule, AchievementModule.class);
		acModule.refreshAchievement(playerID, ActionType.HEROLEVEL, nowLevel);
		return hero;
	}

	/**
	 * 获取英雄指定等级升级的经验
	 * */
	private int getHeroNextExp(int level, int quality) {
		int exp = this.expFormula(level);
		switch (quality) {
		case 1:
			exp = exp * 4;
			break;
		case 2:
			exp = exp * 6;
			break;
		case 3:
			exp = exp * 8;
			break;
		case 4:
			exp = exp * 10;
			break;
		case 5:
			exp = exp * 12;
		}
		return exp;
	}

	/**
	 * 总经验计算公式
	 * */
	private int expFormula(int level) {
		return level * (level + 1) * (2 * level + 1) / 6;
	}

	/** 获取经验对应的等级 */
	public int getHeroExpLevel(int quality, int exp, int level) {
		for (int i = level; i < 9999; i++) {
			int maxExp = this.getHeroNextExp(i, quality);
			if (maxExp > exp) {
				return i - 1;
			}
		}
		return level;
	}

	/**
	 * 武将进阶
	 * 
	 * */
	public void advanceHero(String playerID, String heroID, HeroInfoProtocol protocol, IOMessage ioMessage) {
		HeroEntity heroEntity = this.getHeroEntity(playerID, ioMessage);
		Map<String, Hero> heroList = heroEntity.getHeroMap();
		Hero hero = heroList.get(heroID);
		if (hero == null) {
			logger.error("武将不存在");
			protocol.setCode(ErrorIds.HeroNotFound);
			return;
		}
		int classLevel = hero.getClassLevel();
		if (classLevel >= maxClassLevel) {
			protocol.setCode(ErrorIds.MaxClassLevel);
			return;
		}
		int templaeID = hero.getTemplateID();
		HeroData heroData = TemplateManager.getTemplateData(templaeID, HeroData.class);
		int nextID = heroData.getNextID();
		if (nextID == 0) {
			logger.error("武将不可进阶");
			protocol.setCode(ErrorIds.HeroCouldNotAdvance);
			return;
		}
		int nextReq = heroData.getNextReq();
		AdvanceHeroData advanceData = TemplateManager.getTemplateData(nextReq, AdvanceHeroData.class);
		int levelReq = advanceData.getLevelReq();
		if (hero.getLevel() < levelReq) {
			logger.error("武将等级不够");
			protocol.setCode(ErrorIds.HeroLevelLow);
			return;
		}

		Map<Integer, Integer> itemReq = advanceData.getItemReq();
		List<GoodsBean> goodsList = new ArrayList<GoodsBean>();
		if (itemReq != null) {
			for (Entry<Integer, Integer> entry : itemReq.entrySet()) {
				int pid = entry.getKey();
				int num = entry.getValue();
				if (pid == KindIDs.templateID) {
					pid = heroData.getOriginalID();
				}
				if (num > 0) {
					GoodsBean goodsBean = new GoodsBean();
					goodsBean.setNum(num);
					goodsBean.setPid(pid);
					goodsList.add(goodsBean);
				}
			}
			RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
			Map<String, Object> itemMap = new HashMap<String, Object>();
			int code = rewardModule.useGoods(playerID, goodsList, 0, false, null, itemMap, ioMessage);
			if (code != 0) {
				protocol.setCode(code);
				return;
			}
			protocol.setItemMap(itemMap);
		}

		List<BaseEntity> entityList = new ArrayList<BaseEntity>();
		BagEntity cacheBagEntity = (BagEntity) ioMessage.getInputParse().get(BagEntity.class.getName());
		if (cacheBagEntity != null) {
			entityList.add(cacheBagEntity);
		}
		TalismanMapEntity talismanMapEntity = (TalismanMapEntity) ioMessage.getInputParse().get(TalismanMapEntity.class.getName());
		if (talismanMapEntity != null) {
			entityList.add(talismanMapEntity);
		}

		entityList.add(heroEntity);
		hero.setClassLevel(classLevel + 1);
		hero.setTemplateID(nextID);
		if (hero.getClassLevel() >= 5) {
			ChatModule chatModule = ModuleManager.getModule(ModuleNames.ChatModule, ChatModule.class);
			if (heroData.getName() != null) {
				chatModule.addInformation(playerID, InformationMessageType.advanceHero, hero.getClassLevel(), heroData.getName());
			}
		}
		Map<Integer, Double> advanceProp = hero.getHeroAdvanceProp();
		Map<Integer, Integer> talent = heroData.getTalent();
		if (talent != null) {
			for(Entry<Integer,Integer> talentEntry : talent.entrySet()){
				int talentID = talentEntry.getKey();
				int advanceLevel = talentEntry.getValue();
				if(advanceLevel == hero.getClassLevel()){
					TalentData talentData = TemplateManager.getTemplateData(talentID, TalentData.class);
					Map<Integer, Double> addition = talentData.getAddition();
					if (addition != null) {
						for (Entry<Integer, Double> entry : addition.entrySet()) {
							int key = entry.getKey();
							double value = entry.getValue();
							if (addPropList.contains(key)) {
								if (advanceProp.get(key) != null) {
									advanceProp.put(key, advanceProp.get(key) + value);
								} else {
									advanceProp.put(key, value);
									hero.setHeroAdvanceProp(advanceProp);
								}
							}
						}
					}
				}
			}
		}
		this.saveHeroEntity(heroEntity);
		this.saveEntityList(entityList);

		protocol.setHero(hero);
		AchievementModule acModule = ModuleManager.getModule(ModuleNames.AchievementModule, AchievementModule.class);
		int actionType = 0;
		if (heroID.equals(femaleHero + "") || heroID.equals(maleHero + "")) {
			LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule, LoginModule.class);
			PlayerEntity playerEntity = loginModule.getPlayerEntity(playerID);
			playerEntity.setPhotoID(hero.getTemplateID());
			loginModule.savePlayerEntity(playerEntity);
			actionType = ActionType.LEADADVANCE;
		} else {
			actionType = ActionType.HEROCLASSLEVEL;
		}
		acModule.refreshAchievement(playerID, actionType, hero.getClassLevel());
		JSONObject params = new JSONObject();
		params.put("potentiality", heroData.getAptitude());
		params.put("stage", hero.getClassLevel());
		MainTaskModule mainTaskModule = ModuleManager.getModule(ModuleNames.MainTaskModule,MainTaskModule.class);
		mainTaskModule.updateTaskByActionType(playerID, actionType, hero.getClassLevel(), null);
		AnalyseModule analyseModule = ModuleManager.getModule(ModuleNames.AnalyseModule, AnalyseModule.class);
		analyseModule.analyLog(playerID, "DISCIPLE_PROMOTE", null, null, null, null);
	}

	/**
	 * 移除英雄
	 * 
	 * @throws IllegalAccessException
	 * */
	public long deductHero(String playerID, int templateID, boolean isSave, IOMessage ioMessage) {
		HeroEntity heroEntity = this.getHeroEntity(playerID, ioMessage);
		Map<String, Hero> heroMap = heroEntity.getHeroMap();
		Set<Entry<String, Hero>> entrySet = heroMap.entrySet();
		List<Long> teamList = heroEntity.getTeamList();
		long heroID = 0;
		for (Entry<String, Hero> entry : entrySet) {
			Hero hero = entry.getValue();
			if (hero.getClassLevel() == 0 && !teamList.contains(hero.getHeroID()) && hero.getTemplateID() == templateID && hero.getExp() == 0) {
				heroID = hero.getHeroID();
				break;
			}
		}
		if (heroID == 0) {
			logger.error("未找到进阶所需的英雄");
			throw new IllegalArgumentException(ErrorIds.HeroNotFound + "");
		} else if (heroID == maleHero || heroID == femaleHero) {
			logger.error("不可移除主角");
			throw new IllegalArgumentException(ErrorIds.HeroNoRemove + "");
		} else {
			heroMap.remove(heroID + "");
		}
		if (isSave) {
			this.saveHeroEntity(heroEntity);
		}

		return heroID;
	}

	/**
	 * 出售英雄
	 * */
	public int sellHeros(String playerID, List<Object> sellList, Map<String, Object> itemMap, Map<String, GoodsBean> showMap, IOMessage ioMessage) {
		HeroEntity heroEntity = this.getHeroEntity(playerID, ioMessage);
		Map<String, Hero> heroMap = heroEntity.getHeroMap();
		List<Long> teamList = heroEntity.getTeamList();
		int price = 0;
		for (Object obj : sellList) {
			String heroID = obj.toString();
			Hero hero = heroMap.get(heroID);
			if (hero == null) {
				logger.error("英雄未找到");
				return ErrorIds.HeroNotFound;
			}
			if (teamList.contains(Long.parseLong(heroID))) {
				logger.error("英雄不可出售");
				return ErrorIds.HeroDontSell;
			}
			int templateID = hero.getTemplateID();
			HeroData data = TemplateManager.getTemplateData(templateID, HeroData.class);
			if (data.getCanSell() == 0) {
				logger.error("英雄不可出售");
				return ErrorIds.HeroDontSell;
			}
			price += data.getSalePrice();
			heroMap.remove(heroID);
		}
		RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
		rewardModule.addGoods(playerID, KindIDs.SILVERTYPE, price, null, true, showMap, itemMap, ioMessage);
		AnalyseModule analyseModule = ModuleManager.getModule(ModuleNames.AnalyseModule, AnalyseModule.class);
		analyseModule.analyLog(playerID, "DISCIPLE_SELL", null, null, null, null);
		this.saveHeroEntity(heroEntity);
		return 0;
	}

	/**
	 * 扩展背包
	 * */
	public void doExpand(String playerID, HeroInfoProtocol protocol) {
		HeroEntity heroEntity = this.getHeroEntity(playerID);
		int nowSellNum = heroEntity.getMaxHeroNum();
		int n = (nowSellNum - SysConstants.heroBagInitNum) / SysConstants.bagSellAddNum + 1;
		int price = n * 25;
		heroEntity.setMaxHeroNum(nowSellNum + SysConstants.bagSellAddNum);
		Map<String, Object> itemMap = new HashMap<String, Object>();
		RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
		int code = rewardModule.useGoods(playerID, KindIDs.GOLDTYPE, price, 0, true, null, itemMap, null);
		if (code != 0) {
			protocol.setCode(code);
			return;
		}
		protocol.setItemMap(itemMap);
		protocol.setMaxSellNum(heroEntity.getMaxHeroNum());
		this.saveHeroEntity(heroEntity);

		// ////
		// // 元宝消耗记录
		// ///
		AnalyseModule analyseModule = ModuleManager.getModule(ModuleNames.AnalyseModule, AnalyseModule.class);
		analyseModule.goldCostLog(playerID, price, 1, price, "doExpand", "hero");

	}

	/**
	 * 英雄合成
	 * */
	public void compoundHero(String playerID, String shardID, HeroInfoProtocol protocol, IOMessage ioMessage) {
		HeroEntity heroEntity = this.getHeroEntity(playerID, ioMessage);
//		int maxNum = heroEntity.getMaxHeroNum();
//		int size = heroEntity.getHeroMap().size();
//		if (size >= maxNum) {
//			throw new IllegalArgumentException(ErrorIds.HeroBagNumWrong + "");
//		}
		Map<String, HeroShard> map = heroEntity.getShardMap();
		HeroShard shard = map.get(shardID);
		HeroShardData data = TemplateManager.getTemplateData(shard.getShardID(), HeroShardData.class);
		int composeNum = data.getComposeNum();
		int nowNum = shard.getNum();
		if (nowNum >= composeNum) {
			shard.setNum(nowNum - composeNum);
			if (shard.getNum() == 0) {
				map.remove(shardID);
			}
		} else {
			logger.error("碎片不足");
			throw new IllegalArgumentException(ErrorIds.HeroShardNotEnoungh + "");
		}
		int targetID = data.getTargetID();
		Map<String, Object> itemMap = new HashMap<>();
		RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
		rewardModule.addGoods(playerID, targetID, 1, null, true, null, itemMap, ioMessage);
		HeroData heroData = TemplateManager.getTemplateData(targetID, HeroData.class);
		if (heroData != null) {
			int quailty = heroData.getQuality();
			int actionType = 0;
			if (quailty == purpleHeroType) {
				actionType = ActionType.COMPOSEPURPLEHERO;
			} else if (quailty == blueHeroType) {
				actionType = ActionType.COMPOSEPURPLEHERO;
			}
			AchievementModule acModule = ModuleManager.getModule(ModuleNames.AchievementModule, AchievementModule.class);
			acModule.refreshAchievement(playerID, actionType, 1);
		}
		protocol.setItemMap(itemMap);
		protocol.setHeroShard(shard);
	}

	/**
	 * 增加英雄
	 * */
	public Hero addHero(String playerID, int templateID, boolean isSave, IOMessage ioMessage) {
		HeroEntity heroEntity = this.getHeroEntity(playerID, ioMessage);
		Map<String, Hero> heroMap = heroEntity.getHeroMap();
		// int size = heroMap.size();
		// if(size > heroEntity.getMaxHeroNum()){
		// logger.error("超过背包上限");
		// throw new IllegalArgumentException( ErrorIds.BagFull + "");
		// }
		Hero hero = new Hero();
		HeroData heroData = TemplateManager.getTemplateData(templateID,HeroData.class);
		Map<Integer, Integer> talent = heroData.getTalent();
		if (talent != null) {
			Map<Integer, Double> advanceProp = new HashMap<>();
			for(Entry<Integer,Integer> talentEntry : talent.entrySet()){
				int talentID = talentEntry.getKey();
				int advanceLevel = talentEntry.getValue();
				if(advanceLevel == hero.getClassLevel()){
					TalentData talentData = TemplateManager.getTemplateData(talentID, TalentData.class);
					Map<Integer, Double> addition = talentData.getAddition();
					if (addition != null) {
						for (Entry<Integer, Double> entry : addition.entrySet()) {
							int key = entry.getKey();
							double value = entry.getValue();
							if (addPropList.contains(key)) {
								if (advanceProp.get(key) != null) {
									advanceProp.put(key, advanceProp.get(key) + value);
								} else {
									advanceProp.put(key, value);
								}
							}
						}
					}
				}
			}
			hero.setHeroAdvanceProp(advanceProp);
		}
		
		long heroID = this.getHeroID();
		hero.setHeroID(heroID);
		hero.setTemplateID(templateID);
		heroMap.put(heroID + "", hero);
		if (isSave) {
			this.saveHeroEntity(heroEntity);
		}
		return hero;
	}

	/**
	 * 获取进阶材料
	 * 
	 * @param templateID
	 *            int 模板ID
	 * @param list
	 *            List<GoodsBean> 材料列表
	 * */
	public void getClassStuff(Map<Integer, GoodsBean> list, int templateID, int firstID) {
		AdvanceHeroData data = TemplateManager.getTemplateData(templateID, AdvanceHeroData.class);
		Map<Integer, Integer> itemReq = data.getItemReq();
		for (Entry<Integer, Integer> itemMap : itemReq.entrySet()) {
			int pid = itemMap.getKey();
			if (pid == 9999) {
				pid = firstID;
			}
			int num = itemMap.getValue();
			GoodsBean goodsBean;
			if (list.containsKey(pid)) {
				goodsBean = list.get(pid);
				goodsBean.setNum(goodsBean.getNum() + num);
			} else {
				goodsBean = new GoodsBean(pid, num);
			}
			list.put(pid, goodsBean);
		}
	}

	/**
	 * 计算战斗力
	 * */

	public int calculateFightValue(String playerID, IOMessage ioMessage) {
		HeroEntity entity = this.getHeroEntity(playerID, ioMessage);
		HeroTroopsEntity troopsEntity = this.getHeroTroopsEntity(playerID);
		Map<String, Hero> heroMap = entity.getHeroMap();
		List<Long> teamList = troopsEntity.getTroops();
		ManualModule manualModule = ModuleManager.getModule(ModuleNames.ManualModule, ManualModule.class);
		// 计算装备属性
		EquipmentModule equipmentModule = ModuleManager.getModule(ModuleNames.EquipmentModule, EquipmentModule.class);
		EquipmentMapEntity equipmentMapEntity = equipmentModule.getEquipmentMapEntity(playerID, ioMessage);
		Map<String, Equipment> eqMap = equipmentMapEntity.getEquipMap();
		TalismanModule talismanModule = ModuleManager.getModule(ModuleNames.TalismanModule, TalismanModule.class);
		TalismanMapEntity talismanMapEntity = talismanModule.getEntity(playerID, ioMessage);
		Map<String, TalismanEntity> talismanMap = talismanMapEntity.getTalismanMap();
		
		// 计算宠物战斗力
		PetModule petModule = ModuleManager.getModule(ModuleNames.PetModule, PetModule.class);
		double petFrightValue=petModule.countCombat(playerID);
		

		double hp = 0;
		double attack = 0;
		double pdef = 0;
		double mdef = 0;
		int heroPropTotal = 0;
		int heroNum = 0;
		for (long heroID : teamList) {
			if (heroID != 0) {
				heroNum += 1;
				Hero hero = heroMap.get(heroID + "");
				Map<String, Long> equipedMap = hero.getEquipMap();
				Map<Integer, Double> equipedPropMap = new HashMap<>();
				Map<String, Double> refineProp = new HashMap<>();
				Map<Integer, Double> heroFriendshipProp = new HashMap<>();
				Map<Integer, Double> equipFriendShipProp = hero.getEquipFriendShipProp();
				Map<Integer,Integer> equipSetProp = hero.getEquipmentSetProp();
				for (Entry<Integer, Double> equipFriendShipPropEntry : equipFriendShipProp.entrySet()) {
					int key = equipFriendShipPropEntry.getKey();
					double value = equipFriendShipPropEntry.getValue();
					if (heroFriendshipProp.containsKey(key)) {
						heroFriendshipProp.put(key, heroFriendshipProp.get(key) + value);
					} else {
						heroFriendshipProp.put(key, value);
					}
				}
//				for (Entry<Integer, Double> equipFriendShipPropEntry : equipFriendShipProp.entrySet()) {
//					int key = equipFriendShipPropEntry.getKey();
//					double value = equipFriendShipPropEntry.getValue();
//					if (heroFriendshipProp.containsKey(key)) {
//						heroFriendshipProp.put(key, heroFriendshipProp.get(key) + value);
//					}
//				}
				Map<Integer, Double> heroFriendShipProp = hero.getHeroFriendShipProp();
				for (Entry<Integer, Double> heroFriendShipPropEntry : heroFriendShipProp.entrySet()) {
					int key = heroFriendShipPropEntry.getKey();
					double value = heroFriendShipPropEntry.getValue();
					if (heroFriendshipProp.containsKey(key)) {
						heroFriendshipProp.put(key, heroFriendshipProp.get(key) + value);
					} else {
						heroFriendshipProp.put(key, value);
					}
				}
				Map<Integer, Double> talismanFriendShipProp = hero.getTalismanFriendShipProp();
				for (Entry<Integer, Double> talismanFriendShipPropEntry : talismanFriendShipProp.entrySet()) {
					int key = talismanFriendShipPropEntry.getKey();
					double value = talismanFriendShipPropEntry.getValue();
					if (heroFriendshipProp.containsKey(key)) {
						heroFriendshipProp.put(key, heroFriendshipProp.get(key) + value);
					} else {
						heroFriendshipProp.put(key, value);
					}
				}
				
				for (Long equipID : equipedMap.values()) {
					if (equipID == 0)
						continue;
					Map<String, Double> equipProp = new HashMap<>();
					Equipment equipment = eqMap.get(equipID + "");
					if (equipment == null) {
						TalismanEntity talisman = talismanMap.get(equipID + "");
						if (talisman != null) {
							equipProp = talisman.getPrototype();
							refineProp = talisman.getRefinePrototype();
						}
					} else {
						equipProp = equipment.getPrototype();
						refineProp = equipment.getRefine();
					}

					for (Entry<String, Double> equipPropEntry : equipProp.entrySet()) {
						int key = Integer.parseInt(equipPropEntry.getKey());
						if (equipedPropMap.get(key) != null) {
							equipedPropMap.put(key, equipedPropMap.get(key) + equipPropEntry.getValue());
						} else {
							equipedPropMap.put(key, equipPropEntry.getValue());
						}
					}
					for (Entry<String, Double> equipRefineEntry : refineProp.entrySet()) {
						int key = Integer.parseInt(equipRefineEntry.getKey());
						if (equipedPropMap.get(key) != null) {
							equipedPropMap.put(key, equipedPropMap.get(key) + equipRefineEntry.getValue());
						} else {
							equipedPropMap.put(key, equipRefineEntry.getValue());
						}
					}
				}
				int templateID = hero.getTemplateID();
				HeroData data = TemplateManager.getTemplateData(templateID, HeroData.class);
				int atype = data.getAtype();
				Map<Integer, Double> manualProp = new HashMap<>();
				if (data.getCharactorID() != maleHero && data.getCharactorID() != femaleHero) {
					manualProp = manualModule.getPrototype(playerID, data.getCharactorID(), atype, ioMessage);
				} else {
					LeadModule leadModule = ModuleManager.getModule(ModuleNames.LeadModule, LeadModule.class);
					LeadDesitnyEntity leadDesitnyEntity = leadModule.getDesitnyEntity(playerID);
					Map<Integer, HeroPrototype> desitnyPrototype = leadDesitnyEntity.getPrototype();
					for (HeroPrototype heroPrototype : desitnyPrototype.values()) {
						manualProp.put(heroPrototype.getPid(), heroPrototype.getValue());
					}
				}

				double manualHp = manualProp.get(SysConstants.Hp) != null ? manualProp.get(SysConstants.Hp) : 0;
				double manualAttack = manualProp.get(SysConstants.Attack) != null ? manualProp.get(SysConstants.Attack) : 0;
				double manualPDef = manualProp.get(SysConstants.PDef) != null ? manualProp.get(SysConstants.PDef) : 0;
				double manualMDef = manualProp.get(SysConstants.MDef) != null ? manualProp.get(SysConstants.MDef) : 0;
				double equipHp = equipedPropMap.get(SysConstants.Hp) != null ? equipedPropMap.get(SysConstants.Hp) : 0;
				double equipAttack = equipedPropMap.get(SysConstants.Attack) != null ? equipedPropMap.get(SysConstants.Attack) : 0;
				double equipPDef = equipedPropMap.get(SysConstants.PDef) != null ? equipedPropMap.get(SysConstants.PDef) : 0;
				double equipMDef = equipedPropMap.get(SysConstants.MDef) != null ? equipedPropMap.get(SysConstants.MDef) : 0;
				double setHp = equipSetProp.get(SysConstants.Hp) != null ? equipSetProp.get(SysConstants.Hp) : 0;
				double setAttack = equipSetProp.get(SysConstants.Attack) != null ? equipSetProp.get(SysConstants.Attack) : 0;
				double setPDef  = equipSetProp.get(SysConstants.PDef) != null ? equipSetProp.get(SysConstants.PDef) : 0;
				double setMdef =  equipSetProp.get(SysConstants.MDef) != null ? equipSetProp.get(SysConstants.MDef) : 0;
				// 计算天赋值
				Map<Integer, Double> advanceProp = hero.getHeroAdvanceProp();
				double advanceHp = advanceProp.get(SysConstants.Hp) != null ? advanceProp.get(SysConstants.Hp) : 0;
				double advanceAttack = advanceProp.get(SysConstants.Attack) != null ? advanceProp.get(SysConstants.Attack) : 0;
				double advancePDef = advanceProp.get(SysConstants.PDef) != null ? advanceProp.get(SysConstants.PDef) : 0;
				double advanceMDef = advanceProp.get(SysConstants.MDef) != null ? advanceProp.get(SysConstants.MDef) : 0;
				// 得到宝物加成
				double hpTalismanPercent = equipedPropMap.get(SysConstants.HpPer) != null ? equipedPropMap.get(SysConstants.HpPer) : 0;
				double attackTalismanPercent = equipedPropMap.get(SysConstants.AttackPer) != null ? equipedPropMap.get(SysConstants.AttackPer) : 0;
				// 得到羁绊加成
				double hpFriendshipPercent = heroFriendshipProp.get(SysConstants.HpPer) != null ? heroFriendshipProp.get(SysConstants.HpPer) : 0;
				double attackFriendshipPercent = heroFriendshipProp.get(SysConstants.AttackPer) != null ? heroFriendshipProp.get(SysConstants.AttackPer) : 0;
				double PDefFriendshipPercent = heroFriendshipProp.get(SysConstants.PDefPer) != null ? heroFriendshipProp.get(SysConstants.PDefPer) : 0;
				double MDefFriendshipPercent = heroFriendshipProp.get(SysConstants.MDefPer) != null ? heroFriendshipProp.get(SysConstants.MDefPer) : 0;
				double baseHp = data.getBaseHP();
				double baseAttack = data.getBaseATK();
				double baseDEF = data.getBaseDEF();
				double baseRES = data.getBaseRES();
				double level = hero.getLevel();
				double aptitude = data.getAptitude();
				double godhood = data.getGodhood();
				double spirit = data.getSpirit();
				double force = data.getForce();
//				double tempHp = (baseHp + level * ((aptitude / 20) * (godhood + 50)) + manualHp + equipHp + advanceHp + setHp) * (1 + hpTalismanPercent + hpFriendshipPercent);
//				double tempAttack = (baseAttack + level * aptitude + manualAttack + equipAttack + advanceAttack + setAttack) * (1 + attackTalismanPercent + attackFriendshipPercent);
//				double tempPdef =  (baseDEF + level * aptitude / 4 + manualPDef + equipPDef + advancePDef + setPDef) * (1 + PDefFriendshipPercent);
//				double tempMdef =  (baseRES + level * aptitude / 4 + manualMDef + equipMDef + advanceMDef + setMdef) * (1 + MDefFriendshipPercent);
				
				hp += (baseHp + level * ((aptitude / 20) * (godhood + 50)) + manualHp + equipHp + advanceHp + setHp) * (1 + hpTalismanPercent + hpFriendshipPercent);
				attack += (baseAttack + level * aptitude + manualAttack + equipAttack + advanceAttack + setAttack) * (1 + attackTalismanPercent + attackFriendshipPercent);
				pdef += (baseDEF + level * aptitude / 4 + manualPDef + equipPDef + advancePDef + setPDef) * (1 + PDefFriendshipPercent);
				mdef += (baseRES + level * aptitude / 4 + manualMDef + equipMDef + advanceMDef + setMdef) * (1 + MDefFriendshipPercent);
				heroPropTotal += godhood + spirit + force;
			}
		}
		double frightValue = hp * 0.2 + attack + pdef + mdef + heroPropTotal * 10 - heroNum * 1500 +petFrightValue;
		MainTaskModule mainTaskModule = ModuleManager.getModule(ModuleNames.MainTaskModule,MainTaskModule.class);
		mainTaskModule.updateTaskByActionType(playerID, ActionType.FIGHTVALUE, (int) frightValue, null);
		return (int) frightValue;
	}

	/**
	 * 添加碎片
	 * */
	public HeroShard addHeroShard(String playerID, int shardID, int num, boolean isSave, IOMessage ioMessage) {
		HeroEntity heroEntity = this.getHeroEntity(playerID, ioMessage);
		Map<String, HeroShard> heroShardList = heroEntity.getShardMap();
		HeroShard heroShard = heroShardList.get(shardID + "");
		if (heroShard == null) {
			heroShard = new HeroShard();
			heroShard.setShardID(shardID);
			heroShard.setNum(num);
			heroShardList.put(shardID + "", heroShard);
		} else {
			heroShard.setNum(heroShard.getNum() + num);
		}
		if (isSave) {
			this.saveHeroEntity(heroEntity);
		}
		return heroShard;
	}

	/**
	 * 移除碎片
	 * */
	public HeroShard removeHeroShard(String playerID, int shardID, int num, boolean isSave, IOMessage ioMessage) {
		HeroEntity heroEntity = this.getHeroEntity(playerID, ioMessage);
		Map<String, HeroShard> heroShardList = heroEntity.getShardMap();
		HeroShard heroShard = heroShardList.get(shardID + "");
		if (heroShard != null && heroShard.getNum() >= num) {
			heroShard.setNum(heroShard.getNum() - num);
			if (heroShard.getNum() == 0) {
				heroShardList.remove(heroShard);
			}
		} else {
			throw new IllegalArgumentException(ErrorIds.HeroShardNotEnoungh + "");
		}
		if (isSave) {
			this.saveHeroEntity(heroEntity);
		}
		return heroShard;
	}

	/**
	 * 修改玩家的英雄皮肤信息
	 * */
	public HeroSkinEntity changeHeroSkin(String playerID, int charID, int skinID, IOMessage ioMessage) {
		HeroSkinEntity heroSkinEntity = this.getHerosSkinEntity(playerID, ioMessage);
		Map<String, Integer> skinMap = heroSkinEntity.getHeroSkin();
		skinMap.put(charID + "", skinID);
		this.saveHeroSkinEntity(heroSkinEntity);
		return heroSkinEntity;
	}

	/**
	 * 增加玩家的英雄皮肤信息
	 * */
	public HeroSkinEntity addHeroSkin(String playerID, int charID, int skinID, IOMessage ioMessage) {
		HeroSkinEntity heroSkinEntity = this.getHerosSkinEntity(playerID, ioMessage);
		Map<String, List<Integer>> skinMap = heroSkinEntity.getAllHeroSkin();
		List<Integer> list = skinMap.get(charID + "");
		if (list == null) {
			list = new ArrayList<>();
		}
		if (!list.contains(skinID)) {
			list.add(skinID);
		}
		skinMap.put(charID + "", list);
		heroSkinEntity.setAllHeroSkin(skinMap);
		this.saveHeroSkinEntity(heroSkinEntity);
		return heroSkinEntity;
	}

}
