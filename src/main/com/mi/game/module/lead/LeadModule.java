package com.mi.game.module.lead;

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
import com.mi.game.module.achievement.AchievementModule;
import com.mi.game.module.base.BaseModule;
import com.mi.game.module.event.EventModule;
import com.mi.game.module.event.define.EventConstans;
import com.mi.game.module.hero.HeroModule;
import com.mi.game.module.hero.pojo.Hero;
import com.mi.game.module.hero.pojo.HeroEntity;
import com.mi.game.module.lead.dao.DestinyEntityDAO;
import com.mi.game.module.lead.data.LeadDestinyData;
import com.mi.game.module.lead.data.LeadExpData;
import com.mi.game.module.lead.pojo.HeroPrototype;
import com.mi.game.module.lead.pojo.LeadDesitnyEntity;
import com.mi.game.module.lead.protocol.DestinyProtocol;
import com.mi.game.module.lead.protocol.ExpResponse;
import com.mi.game.module.login.LoginModule;
import com.mi.game.module.login.pojo.NewPlayerEntity;
import com.mi.game.module.login.pojo.PlayerEntity;
import com.mi.game.module.mainTask.MainTaskModule;
import com.mi.game.module.reward.RewardModule;
import com.mi.game.module.reward.data.GoodsBean;

@Module(name = ModuleNames.LeadModule, clazz = LeadModule.class)
public class LeadModule extends BaseModule {
	private final Map<Integer, Integer> expListData = new HashMap<Integer, Integer>();
	private DestinyEntityDAO destinyEntityDAO = DestinyEntityDAO.getInstance();
	public static final int leadMaxLevel = 100;
	private final int initDestinyID = 10441;

	@Override
	public void init() {
		initExpData();
	}

	public void initExpData() {
		List<LeadExpData> expList = TemplateManager.getTemplateList(LeadExpData.class);
		for (LeadExpData data : expList) {
			int exp = data.getExp();
			int level = data.getLevel();
			expListData.put(level, exp);
		}
	}

	/**
	 * 增加主角经验
	 * 
	 * */
	public ExpResponse addExp(String playerID, Hero hero, boolean isSave, int exp, Map<String, Object> itemMap, IOMessage ioMessage) {
		ExpResponse expResponse = new ExpResponse();
		int nowExp = hero.getExp();

		// 开服活动,经验翻倍
		EventModule eventModule = ModuleManager.getModule(ModuleNames.EventModule, EventModule.class);
		LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule, LoginModule.class);
		NewPlayerEntity newPlayerEntity = loginModule.getNewPlayerEntity(playerID);
		if (newPlayerEntity != null) {
			if (newPlayerEntity.getProperId() != EventConstans.GUIDE_PROPERID) {
				exp = exp * eventModule.getNewServerEventReward(EventConstans.EVENT_TYPE_DOUBLEEXP);
			}
		}

		int allExp = nowExp + exp;
		int level = hero.getLevel();
		int maxExp = expListData.get(level);
		hero.setExp(allExp);
		if (allExp >= maxExp) {
			if (level != leadMaxLevel) {
				// 升级了
				List<GoodsBean> goodsList = heroLevelup(playerID, hero, itemMap, ioMessage);
				expResponse.setLevelUpList(goodsList);
				expResponse.setLevelUp(true);
				MainTaskModule mainTaskModule = ModuleManager.getModule(ModuleNames.MainTaskModule,MainTaskModule.class);
				mainTaskModule.addTask(playerID, hero.getLevel());
				mainTaskModule.updateTaskByActionType(playerID, ActionType.LEADLEVELFORMAINTASK, hero.getLevel(), ioMessage);
			}
		}
		if (isSave) {
			HeroModule heroModule = ModuleManager.getModule(ModuleNames.HeroModule, HeroModule.class);
			heroModule.saveHeroEntity((HeroEntity) ioMessage.getInputParse().get(HeroEntity.class.getName() + playerID));
		}
		expResponse.setLevel(hero.getLevel());
		expResponse.setExp(hero.getExp());
		return expResponse;
	}

	/**
	 * 升级
	 * 
	 * @param hero
	 * @param template
	 * @param exp
	 * @param playerID
	 */
	private List<GoodsBean> heroLevelup(String playerID, Hero hero, Map<String, Object> itemMap, IOMessage ioMessage) {
		int level = hero.getLevel();
		int exp = hero.getExp();
		int energy = 0;
		for (int i = level; i <= leadMaxLevel; i++) {
			if (exp < expListData.get(i)) {
				break;
			}
			if (i > 9) {
				energy += 20;
			}
			hero.setLevel(i + 1);
		}
		int gold = (hero.getLevel() - level) * 10;
		RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
		List<GoodsBean> goodsList = new ArrayList<GoodsBean>();
		GoodsBean goldBean = new GoodsBean(KindIDs.GOLDTYPE, gold);
		if(hero.getLevel() == 9){
			goodsList.add(new GoodsBean(101701, 1));
			goodsList.add(new GoodsBean(101702, 1));
		}
		if (energy != 0) {
			GoodsBean energyBean = new GoodsBean(KindIDs.ENERGY, energy);
			goodsList.add(energyBean);
		}
		goodsList.add(goldBean);
		rewardModule.addGoods(playerID, goodsList, true, null, itemMap, ioMessage);
		LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule, LoginModule.class);
		PlayerEntity playerEntity = loginModule.getPlayerEntity(playerID);
		playerEntity.setLevel(hero.getLevel());
		loginModule.savePlayerEntity(playerEntity);
		AchievementModule acModule = ModuleManager.getModule(ModuleNames.AchievementModule, AchievementModule.class);
		acModule.refreshAchievement(playerID, ActionType.LEADLEVEL, hero.getLevel());
		return goodsList;
	}

	/**
	 * 获得天命实体
	 * */
	public LeadDesitnyEntity getDesitnyEntity(String playerID) {
		LeadDesitnyEntity entity = destinyEntityDAO.getEntity(playerID);
		if (entity == null) {
			// entity = this.initDestinyEntity(playerID);
			// this.saveDestinyEntity(entity);
			logger.error("天命实体为空");
			throw new NullPointerException(ErrorIds.NoEntity + "");
		}
		return entity;
	}
	
	/**
	 * 获得天命实体
	 * */
	public LeadDesitnyEntity getDesitnyEntity(String playerID,IOMessage ioMessage) {
		LeadDesitnyEntity entity;
		if(ioMessage != null){
			entity = (LeadDesitnyEntity)ioMessage.getInputParse().get(LeadDesitnyEntity.class.getName());
			if(entity == null){
				entity = this.getDesitnyEntity(playerID);
				ioMessage.getInputParse().put(LeadDesitnyEntity.class.getName(), entity);
			}
		}else{
			entity = this.getDesitnyEntity(playerID);
		}
		return entity;
	}

	/**
	 * 保存天命实体
	 * */
	public void saveDestinyEntity(LeadDesitnyEntity entity) {
		destinyEntityDAO.save(entity);
	}

	/**
	 * 初始化天命实体
	 * */
	public LeadDesitnyEntity initDestinyEntity(String playerID) {
		LeadDesitnyEntity entity = new LeadDesitnyEntity();
		entity.setKey(playerID);
		entity.setDestinyID(initDestinyID);
		entity.setNextSilver(500);
		entity.setNextPoint(3);

		LeadDestinyData data = TemplateManager.getTemplateData(initDestinyID, LeadDestinyData.class);
		Map<Integer, Double> addition = data.getAddition();
		List<HeroPrototype> prototypeMap = new ArrayList<>();
		for (Entry<Integer, Double> entry : addition.entrySet()) {
			int key = entry.getKey();
			double value = entry.getValue() * 10;
			HeroPrototype heroPrototype = new HeroPrototype();
			heroPrototype.setPid(key);
			heroPrototype.setValue(value);
			prototypeMap.add(heroPrototype);
		}
		entity.setNextPrototype(prototypeMap);
		return entity;
	}

	/**
	 * 天命增加点数
	 * */
	public void heroAddDestinyPoint(String playerID) {
		LeadDesitnyEntity entity = this.getDesitnyEntity(playerID);
		entity.setPoint(entity.getPoint() + 1);
		this.saveDestinyEntity(entity);
		AchievementModule achievementModule = ModuleManager.getModule(ModuleNames.AchievementModule, AchievementModule.class);
		achievementModule.refreshAchievement(playerID, ActionType.DESTINYPOINT, entity.getPoint());
	}

	/**
	 * 点击天命
	 * */
	public void addDestiny(String playerID, DestinyProtocol protocol, IOMessage ioMessage) {
		LeadDesitnyEntity entity = this.getDesitnyEntity(playerID);
		int destinyNum = entity.getDestinyNum();
		int needStar = entity.getNextPoint();
		int destinyID = entity.getDestinyID();
		int needSilver = entity.getNextSilver();
		int point = entity.getPoint();
		point -= needStar;
		if (point < 0) {
			throw new IllegalArgumentException(ErrorIds.DestinyStarNotEnough + "");
		}
		if (destinyNum == 72) {
			HeroModule heroModule = ModuleManager.getModule(ModuleNames.HeroModule, HeroModule.class);
			Hero hero = heroModule.getLead(playerID, ioMessage);
			hero.setTemplateID(hero.getTemplateID() + 10);
			LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule , LoginModule.class);
			PlayerEntity playerEntity = loginModule.getPlayerEntity(playerID);
			playerEntity.setPhotoID(hero.getTemplateID());
			loginModule.savePlayerEntity(playerEntity);
			heroModule.saveHeroEntity((HeroEntity) ioMessage.getInputParse().get(HeroEntity.class.getName() + playerID));
			protocol.setLead(hero);
		} else {
			Map<Integer, HeroPrototype> prototype = entity.getPrototype();
			List<HeroPrototype> addPrototype = entity.getNextPrototype();
			for (HeroPrototype temp : addPrototype) {
				int pid = temp.getPid();
				Double value = temp.getValue();
				HeroPrototype savePrototype = prototype.get(pid);
				if (savePrototype == null) {
					savePrototype = new HeroPrototype();
					savePrototype.setPid(pid);
					savePrototype.setValue(value);
					prototype.put(pid, savePrototype);
				} else {
					savePrototype.setValue(value + savePrototype.getValue());
				}
			}
		}
		destinyNum += 1;
		LeadDestinyData data = TemplateManager.getTemplateData(destinyID, LeadDestinyData.class);
		int nextID = data.getNextID();
		Map<String, Object> itemMap = new HashMap<>();
		RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
		rewardModule.useGoods(playerID, KindIDs.SILVERTYPE, needSilver, 0, true, null, itemMap, null);
		needStar = this.getNeedStar(destinyNum);
		needSilver = this.getNeedSilver(destinyNum);
		if (destinyNum != 72) {
			List<HeroPrototype> addPrototye = this.getNeedPrototype(nextID, destinyNum);
			entity.setNextPrototype(addPrototye);
			entity.setDestinyID(nextID);
		}
		entity.setNextPoint(needStar);
		entity.setNextSilver(needSilver);
		entity.setPoint(point);
		entity.setDestinyNum(destinyNum);
		this.saveDestinyEntity(entity);
		protocol.setEntity(entity);
		protocol.setItemMap(itemMap);
		MainTaskModule mainTaskModule = ModuleManager.getModule(ModuleNames.MainTaskModule,MainTaskModule.class);
		mainTaskModule.updateTaskByActionType(playerID, ActionType.DESTINYPOINT,destinyNum, null);
	}

	/**
	 * 获取天命星数
	 * */
	private int getNeedStar(int destinyNum) {
		int needStar = 0;
		if (destinyNum >= 0 && destinyNum < 11) {
			needStar = 3;
		} else if (destinyNum > 10 && destinyNum < 21) {
			needStar = 4;
		} else if (destinyNum > 20 && destinyNum < 31) {
			needStar = 5;
		} else {
			needStar = 6;
		}
		return needStar;
	}

	/**
	 * 获取天命银币
	 * */
	private int getNeedSilver(int destinyNum) {
		int silver = (destinyNum + 1) * 500;
		return silver;
	}

	/**
	 * 获取天命属性
	 * */
	private List<HeroPrototype> getNeedPrototype(int destinyID, int destinyNum) {
		LeadDestinyData data = TemplateManager.getTemplateData(destinyID, LeadDestinyData.class);
		List<HeroPrototype> prototype = new ArrayList<>();
		Map<Integer, Double> addition = data.getAddition();
		int basePrototype = this.getBasePrototype(destinyNum);
		for (Entry<Integer, Double> entry : addition.entrySet()) {
			int key = entry.getKey();
			double value = entry.getValue();
			HeroPrototype tempPrototype = new HeroPrototype();
			if (key != 10003 && key != 10004 && key != 10005) {
				tempPrototype.setPid(key);
				tempPrototype.setValue((int) (value * basePrototype));
			} else {
				tempPrototype.setPid(key);
				tempPrototype.setValue((int) (value));
			}
			prototype.add(tempPrototype);
		}
		return prototype;
	}

	/**
	 * 获取基础值
	 * */
	private int getBasePrototype(int destinyNum) {
		int basePrototype = 0;
		if (destinyNum >= 0 && destinyNum < 11) {
			basePrototype = 10;
		} else if (destinyNum > 10 && destinyNum < 21) {
			basePrototype = 15;
		} else if (destinyNum > 20 && destinyNum < 31) {
			basePrototype = 20;
		} else {
			basePrototype = 25;
		}
		return basePrototype;
	}

	public ExpResponse addLevel(String playerID, Map<String, Object> itemMap, IOMessage ioMessage) {
		HeroModule heroModule = ModuleManager.getModule(ModuleNames.HeroModule, HeroModule.class);
		Hero hero = heroModule.getLead(playerID, ioMessage);
		int exp = hero.getExp();
		int level = hero.getLevel();
		int nextExp = expListData.get(level);
		int needExp = nextExp - exp;
		ExpResponse response = this.addExp(playerID, hero, true, needExp, itemMap, ioMessage);
		heroModule.saveHeroEntity((HeroEntity) ioMessage.getInputParse().get(HeroEntity.class.getName()));
		return response;
	}

	public int getDestinyNum(String playerID) {
		LeadDesitnyEntity entity = this.getDesitnyEntity(playerID);
		int num = entity.getPoint();
		return num;
	}
}
