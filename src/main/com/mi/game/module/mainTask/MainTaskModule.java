package com.mi.game.module.mainTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.TemplateManager;
import com.mi.core.engine.annotation.Module;
import com.mi.game.defines.ActionType;
import com.mi.game.defines.EquipmentPart;
import com.mi.game.defines.ErrorIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.defines.SysConstants;
import com.mi.game.module.arena.ArenaModule;
import com.mi.game.module.arena.pojo.ArenaEntity;
import com.mi.game.module.base.BaseModule;
import com.mi.game.module.dungeon.DungeonModule;
import com.mi.game.module.dungeon.pojo.ActReward;
import com.mi.game.module.dungeon.pojo.Dungeon;
import com.mi.game.module.dungeon.pojo.DungeonAct;
import com.mi.game.module.dungeon.pojo.DungeonActMapEntity;
import com.mi.game.module.dungeon.pojo.DungeonMapEntity;
import com.mi.game.module.equipment.EquipmentModule;
import com.mi.game.module.equipment.data.EquipmentData;
import com.mi.game.module.equipment.pojo.Equipment;
import com.mi.game.module.equipment.pojo.EquipmentMapEntity;
import com.mi.game.module.hero.HeroModule;
import com.mi.game.module.hero.data.HeroData;
import com.mi.game.module.hero.pojo.Hero;
import com.mi.game.module.hero.pojo.HeroEntity;
import com.mi.game.module.lead.LeadModule;
import com.mi.game.module.lead.pojo.LeadDesitnyEntity;
import com.mi.game.module.legion.LegionModule;
import com.mi.game.module.legion.pojo.LegionMemberEntity;
import com.mi.game.module.login.LoginModule;
import com.mi.game.module.login.pojo.PlayerEntity;
import com.mi.game.module.mainTask.dao.MainTaskEntityDAO;
import com.mi.game.module.mainTask.data.MainTaskData;
import com.mi.game.module.mainTask.pojo.MainTaskEntity;
import com.mi.game.module.mainTask.pojo.MainTaskInfo;
import com.mi.game.module.mainTask.protocol.MainTaskProtocol;
import com.mi.game.module.manual.ManualModule;
import com.mi.game.module.manual.pojo.HeroManual;
import com.mi.game.module.manual.pojo.HeroManualsEntity;
import com.mi.game.module.reward.RewardModule;
import com.mi.game.module.reward.data.GoodsBean;
import com.mi.game.module.store.StoreModule;
import com.mi.game.module.store.pojo.StoreEntity;
import com.mi.game.module.talisman.TalismanModule;
import com.mi.game.module.talisman.data.TalismanData;
import com.mi.game.module.talisman.pojo.TalismanEntity;
import com.mi.game.module.talisman.pojo.TalismanMapEntity;
import com.mi.game.module.tower.TowerModule;
import com.mi.game.module.tower.pojo.TowerEntity;
import com.mi.game.module.welfare.WelfareModule;
import com.mi.game.module.welfare.pojo.WelfareLoginEntity;
import com.mi.game.util.Utilities;

@Module(name = ModuleNames.MainTaskModule, clazz = MainTaskModule.class)
public class MainTaskModule extends BaseModule {
	public final static List<Integer> mainTaskList = new ArrayList<Integer>();
	public MainTaskEntityDAO mainTaskEntityDAO = MainTaskEntityDAO.getInstance();

	/**
	 * 初始化
	 */
	@Override
	public void init() {
		mainTaskList.add(1113001);
	}

	public MainTaskEntity initTaskEntity(String playerID) {
		MainTaskEntity mainTaskEntity = new MainTaskEntity();
		mainTaskEntity.setKey(playerID);
		this.resetMainTask(mainTaskEntity);
		this.saveTaskEntity(mainTaskEntity);
		return mainTaskEntity;
	}

	public void resetMainTask(MainTaskEntity taskEntity) {
		taskEntity.setMainTask(this.newBaseTaskList(taskEntity.getKey().toString(), mainTaskList, null));
	}

	/**
	 * new 多个taskInfo
	 * 
	 * @param taskIdList
	 * @return
	 */
	private List<MainTaskInfo> newBaseTaskList(String playerID, List<Integer> taskIdList, IOMessage ioMessage) {
		List<MainTaskInfo> taskList = new ArrayList<>();
		if (taskIdList != null && !taskIdList.isEmpty()) {
			for (Integer taskId : taskIdList) {
				taskList.add(newBaseTask(playerID, taskId, ioMessage));
			}
		}
		return taskList;
	}

	/**
	 * new 一个BaseTask
	 * 
	 * @param taskId
	 * @return
	 */
	private MainTaskInfo newBaseTask(String playerID, int taskId, IOMessage ioMessage) {
		MainTaskInfo baseTask = new MainTaskInfo();
		baseTask.setTaskID(taskId);
		this.refreshTask(playerID, baseTask, ioMessage);
		return baseTask;
	}

	/**
	 * 按等级开启任务
	 * */
	public List<MainTaskInfo> addTask(String playerID, int level) {
		MainTaskEntity mainTaskEntity = this.getTaskEntity(playerID);
		List<Integer> completeList = mainTaskEntity.getCompleteList();
		List<MainTaskData> dataList = TemplateManager.getTemplateList(MainTaskData.class);
		List<Integer> addList = new ArrayList<>();
		for (MainTaskData data : dataList) {
			int acceptLevel = data.getOpenLevel();
			if (acceptLevel == level) {
				int backID = data.getBackID();
				if (backID != 0) {
					if (completeList.contains(backID)) {
						addList.add(data.getPid());
					}
				}
			}
		}
		if (!addList.isEmpty()) {
			mainTaskEntity.addTaskList(newBaseTaskList(playerID, addList, null));
			this.saveTaskEntity(mainTaskEntity);
		}
		return mainTaskEntity.getMainTask();
	}

	/**
	 * 按开启条件开启任务
	 * */
	public void addTask(String playerID, int nextID, IOMessage ioMessage) {
		MainTaskEntity mainTaskEntity = this.getTaskEntity(playerID, ioMessage);
		MainTaskData mainTaskData = TemplateManager.getTemplateData(nextID, MainTaskData.class);
		LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule, LoginModule.class);
		PlayerEntity playerEntity = loginModule.getPlayerEntity(playerID);
		int level = playerEntity.getLevel();
		int needLevel = mainTaskData.getOpenLevel();
		if (needLevel > level) {
			this.saveTaskEntity(mainTaskEntity);
			return;
		}
		List<Integer> addList = new ArrayList<>();
		addList.add(nextID);
		mainTaskEntity.addTaskList(newBaseTaskList(playerID, addList, ioMessage));
		this.saveTaskEntity(mainTaskEntity);
	}

	public MainTaskEntity getTaskEntity(String playerID) {
		MainTaskEntity mainTaskEntity = mainTaskEntityDAO.getEntity(playerID);
		if (mainTaskEntity == null) {
			mainTaskEntity = this.initTaskEntity(playerID);
		}
		return mainTaskEntity;
	}

	public MainTaskEntity getTaskEntity(String playerID, IOMessage ioMessage) {
		MainTaskEntity mainTaskEntity = null;
		String key = MainTaskEntity.class.getName();
		if (ioMessage != null) {
			mainTaskEntity = (MainTaskEntity) ioMessage.getInputParse(key);
			if (mainTaskEntity == null) {
				mainTaskEntity = this.getTaskEntity(playerID);
				ioMessage.getInputParse().put(key, mainTaskEntity);
			}
		} else {
			mainTaskEntity = this.getTaskEntity(playerID);
		}
		return mainTaskEntity;
	}

	public void saveTaskEntity(MainTaskEntity mainTaskEntity) {
		mainTaskEntityDAO.save(mainTaskEntity);
	}

	/**
	 * 根据actionType 更新任务实体
	 * */

	public void updateTaskByActionType(String playerID, int actionType, int num, IOMessage ioMessage) {
		if (actionType == 0) {
			logger.error("错误的行为类型");
			return;
		}
		MainTaskEntity mainTaskEntity = this.getTaskEntity(playerID, ioMessage);
		List<MainTaskInfo> taskList = mainTaskEntity.getMainTask();
		boolean isSave = false;
		if (taskList != null && !taskList.isEmpty()) {
			for (MainTaskInfo mainTask : taskList) {
				int taskID = mainTask.getTaskID();
				MainTaskData data = TemplateManager.getTemplateData(taskID, MainTaskData.class);
				if (actionType == data.getActionType()) {
					isSave = true;
					this.freshTaskInfo(playerID, mainTask, num, ioMessage);
				}
			}
		}
		if (isSave) {
			this.saveTaskEntity(mainTaskEntity);
		}
	}

	/**
	 * 按行为类型更新实体
	 * */
	public void freshTaskInfo(String playerID, MainTaskInfo mainTaskInfo, int nowNum, IOMessage ioMessage) {
		MainTaskData data = mainTaskInfo.taskData();
		int actionType = data.getActionType();
		switch (actionType) {
		case ActionType.DUNGEONREACHSTARLEVEL: {
			DungeonModule dungeonModule = ModuleManager.getModule(ModuleNames.DungeonModule, DungeonModule.class);
			int actID = data.getParse()[0];
			DungeonActMapEntity actMapEntity = dungeonModule.getDungeonActEntity(playerID);
			DungeonAct dungeonAct = actMapEntity.getDungeMap().get(actID);
			if (dungeonAct != null) {
				int actPoint = dungeonAct.getActPoint();
				mainTaskInfo.setNum(actPoint);
			}
			break;
		}
		case ActionType.GETFARMTIMES: {
			int limit = data.getParse()[0];
			mainTaskInfo.addNum(1, limit);
			break;
		}
		case ActionType.MANUALLEVEL: {
			if (nowNum > mainTaskInfo.getNum()) {
				mainTaskInfo.setNum(nowNum);
			}
			break;
		}
		case ActionType.BLUEHERONUM: {
			HeroModule heroModule = ModuleManager.getModule(ModuleNames.HeroModule, HeroModule.class);
			HeroEntity heroEntity = heroModule.getHeroEntity(playerID, ioMessage);
			Map<String, Hero> heroMaps = heroEntity.getHeroMap();
			List<Long> teamList = heroEntity.getTeamList();
			int blueNum = 0;
			for (long heroID : teamList) {
				if (SysConstants.maleHero == heroID || SysConstants.femaleHero == heroID || 0 == heroID) {
					continue;
				}
				Hero hero = heroMaps.get(heroID + "");
				HeroData heroData = TemplateManager.getTemplateData(hero.getTemplateID(), HeroData.class);
				int quality = heroData.getQuality();
				if (quality > 3) {
					blueNum += 1;
				}
			}
			mainTaskInfo.setNum(blueNum);
			break;
		}
		case ActionType.PURPLEHERONUM: {
			HeroModule heroModule = ModuleManager.getModule(ModuleNames.HeroModule, HeroModule.class);
			HeroEntity heroEntity = heroModule.getHeroEntity(playerID, ioMessage);
			Map<String, Hero> heroMaps = heroEntity.getHeroMap();
			List<Long> teamList = heroEntity.getTeamList();
			int purpleNum = 0;
			for (long heroID : teamList) {
				if (SysConstants.maleHero == heroID || SysConstants.femaleHero == heroID || 0 == heroID) {
					continue;
				}
				Hero hero = heroMaps.get(heroID + "");
				HeroData heroData = TemplateManager.getTemplateData(hero.getTemplateID(), HeroData.class);
				int quality = heroData.getQuality();
				if (quality > 4) {
					purpleNum += 1;
				}
			}
			mainTaskInfo.setNum(purpleNum);
			break;
		}
		case ActionType.EQUIPMENTLEVEL: {
			if (nowNum > mainTaskInfo.getNum()) {
				mainTaskInfo.setNum(nowNum);
			}
			break;
		}
		case ActionType.SMELTEQUIPMENT: {
			int limit = data.getParse()[0];
			mainTaskInfo.addNum(1, limit);
			break;
		}
		case ActionType.SMELTHERO: {
			int limit = data.getParse()[0];
			mainTaskInfo.addNum(1, limit);
			break;
		}
		case ActionType.STONESHOPEXCHAGE: {
			int limit = data.getParse()[0];
			mainTaskInfo.addNum(1, limit);
			break;
		}
		case ActionType.COMPOUNDTALISMAN: {
			int limit = data.getParse()[0];
			mainTaskInfo.addNum(1, limit);
			break;
		}
		case ActionType.COMPOUNDHORSE: {
			int limit = data.getParse()[0];
			mainTaskInfo.addNum(1, limit);
			break;
		}
		case ActionType.ARENAEXCHANGE: {
			int limit = data.getParse()[0];
			mainTaskInfo.addNum(1, limit);
			break;
		}
		case ActionType.SENDENERGY: {
			int limit = data.getParse()[0];
			mainTaskInfo.addNum(1, limit);
			break;
		}
		case ActionType.FIGHTVALUE: {
			if (nowNum > mainTaskInfo.getNum()) {
				mainTaskInfo.setNum(nowNum);
			}
			break;
		}
		case ActionType.LEGIONDEVOTE: {
			int limit = data.getParse()[0];
			mainTaskInfo.addNum(1, limit);
			break;
		}
		case ActionType.HEROCLASSLEVEL: {
			if (nowNum > mainTaskInfo.getNum()) {
				mainTaskInfo.setNum(nowNum);
			}
			break;
		}
		case ActionType.HEROLEVEL: {
			if (nowNum > mainTaskInfo.getNum()) {
				mainTaskInfo.setNum(nowNum);
			}
			break;
		}
		case ActionType.BESTDRAWHERO: {
			int limit = data.getParse()[0];
			mainTaskInfo.addNum(1, limit);
			break;
		}
		case ActionType.TALISMANPLUNDERNUM: {
			int limit = data.getParse()[0];
			mainTaskInfo.addNum(nowNum, limit);
			break;
		}
		case ActionType.DUNGEONELITECOMPLETENUM: {
			int limit = data.getParse()[0];
			mainTaskInfo.addNum(1, limit);
			break;
		}
		case ActionType.ATTACKWORLDBOSS: {
			int limit = data.getParse()[0];
			mainTaskInfo.addNum(1, limit);
			break;
		}
		case ActionType.ATTACKTREE: {
			int limit = data.getParse()[0];
			mainTaskInfo.addNum(1, limit);
			break;
		}
		case ActionType.ATTACKTALISMAN: {
			int limit = data.getParse()[0];
			mainTaskInfo.addNum(1, limit);
			break;
		}
		case ActionType.TOWERLEVEL: {
			if (nowNum > mainTaskInfo.getNum()) {
				mainTaskInfo.setNum(nowNum);
			}
			break;
		}
		case ActionType.ARENARANK: {
			if (nowNum < mainTaskInfo.getNum()) {
				mainTaskInfo.setNum(nowNum);
			}
			break;
		}
		case ActionType.DESTINYPOINT: {
			if (nowNum > mainTaskInfo.getNum()) {
				mainTaskInfo.setNum(nowNum);
			}
			break;
		}
		case ActionType.GETBLUEEQUIPMENTNUM: {
			int limit = data.getParse()[0];
			mainTaskInfo.addNum(1, limit);
			break;
		}
		case ActionType.LEGIONGG: {
			int limit = data.getParse()[0];
			mainTaskInfo.addNum(1, limit);
			break;
		}
		case ActionType.DUNGEONCOMPLETENUM: {
			int limit = data.getParse()[0];
			mainTaskInfo.addNum(1, limit);
			break;
		}
		case ActionType.LEGIONEXCHANGE: {
			int limit = data.getParse()[0];
			mainTaskInfo.addNum(1, limit);
			break;
		}
		case ActionType.PASSDUNGEON: {
			int limit = data.getParse()[0];
			if (limit == nowNum) {
				mainTaskInfo.addNum(1, 1);
			}
			break;
		}
		case ActionType.LEADLEVELFORMAINTASK: {
			if (nowNum > mainTaskInfo.getNum()) {
				mainTaskInfo.setNum(nowNum);
			}
			break;
		}
		case ActionType.LEADADVANCE: {
			if (nowNum > mainTaskInfo.getNum()) {
				mainTaskInfo.setNum(nowNum);
			}
			break;
		}
		case ActionType.HORSELEVEL: {
			if (nowNum > mainTaskInfo.getNum()) {
				mainTaskInfo.setNum(nowNum);
			}
			break;
		}
		case ActionType.USEVITATLYITEM: {
			int limit = data.getParse()[0];
			if (nowNum == limit) {
				mainTaskInfo.setNum(1);
			}
			break;
		}
		case ActionType.BUYVITATLYITEM: {
			int limit = data.getParse()[0];
			if (nowNum == limit) {
				mainTaskInfo.setNum(1);
			}
			break;
		}
		case ActionType.BUYVIPITEM: {
			int limit = data.getParse()[0];
			if (nowNum == limit) {
				mainTaskInfo.setNum(1);
			}
			break;
		}
		case ActionType.GETACTBOX: {
			int actID = data.getParse()[0];
			DungeonModule dungeonModule = ModuleManager.getModule(ModuleNames.DungeonModule, DungeonModule.class);
			DungeonActMapEntity actMapEntity = dungeonModule.getDungeonActEntity(playerID, ioMessage);
			DungeonAct dungeonAct = actMapEntity.getDungeMap().get(actID);
			if (dungeonAct != null) {
				Map<Integer, ActReward> dungeonMap = dungeonAct.getRewardlist();
				int point = data.getParse()[1];
				ActReward actReward = dungeonMap.get(point);
				if (actReward != null) {
					if (actReward.getState() == 1) {
						mainTaskInfo.setNum(1);
					}
				}
			}
			break;
		}
		case ActionType.GETNEWSERVERBOX: {
			int limit = data.getParse()[0];
			if (nowNum == limit) {
				mainTaskInfo.setNum(1);
			}
			break;
		}
		case ActionType.ARENANUM: {
			int limit = data.getParse()[0];
			mainTaskInfo.addNum(1, limit);
			break;
		}
		}
	}

	/**
	 * 判断已有的条件
	 * */
	private void refreshTask(String playerID, MainTaskInfo mainTaskInfo, IOMessage ioMessage) {
		MainTaskData data = mainTaskInfo.taskData();
		int actionType = data.getActionType();
		switch (actionType) {
		case ActionType.DUNGEONREACHSTARLEVEL: {
			DungeonModule dungeonModule = ModuleManager.getModule(ModuleNames.DungeonModule, DungeonModule.class);
			int actID = data.getParse()[0];
			DungeonActMapEntity actMapEntity = dungeonModule.getDungeonActEntity(playerID);
			DungeonAct dungeonAct = actMapEntity.getDungeMap().get(actID);
			if (dungeonAct != null) {
				int actPoint = dungeonAct.getActPoint();
				mainTaskInfo.setNum(actPoint);
			}
			break;
		}
		case ActionType.MANUALLEVEL: {
			ManualModule manualModule = ModuleManager.getModule(ModuleNames.ManualModule, ManualModule.class);
			HeroManualsEntity heroManualsEntity = manualModule.getEntity(playerID, ioMessage);
			int level = 0;
			Map<Integer, HeroManual> map = heroManualsEntity.getHeroList();
			Set<Entry<Integer, HeroManual>> entrySet = map.entrySet();
			for (Entry<Integer, HeroManual> entry : entrySet) {
				HeroManual heroManual = entry.getValue();
				if (heroManual.getLevel() > level) {
					level = heroManual.getLevel();
				}
			}
			mainTaskInfo.setNum(level);
			break;
		}
		case ActionType.EQUIPMENTLEVEL: {
			EquipmentModule module = ModuleManager.getModule(ModuleNames.EquipmentModule, EquipmentModule.class);
			EquipmentMapEntity equipmentMapEntity = module.getEquipmentMapEntity(playerID);
			Map<String, Equipment> equips = equipmentMapEntity.getEquipMap();
			Set<Entry<String, Equipment>> entrySet = equips.entrySet();
			int level = 0;
			for (Entry<String, Equipment> entry : entrySet) {
				Equipment equipment = entry.getValue();
				int strengLevel = equipment.getStrengLevel();
				if (strengLevel > level) {
					level = strengLevel;
				}
			}
			mainTaskInfo.setNum(level);
			break;
		}
		case ActionType.BLUEHERONUM: {
			HeroModule heroModule = ModuleManager.getModule(ModuleNames.HeroModule, HeroModule.class);
			HeroEntity heroEntity = heroModule.getHeroEntity(playerID, ioMessage);
			Map<String, Hero> heroMaps = heroEntity.getHeroMap();
			List<Long> teamList = heroEntity.getTeamList();
			int blueNum = 0;
			for (long heroID : teamList) {
				if (SysConstants.maleHero == heroID || SysConstants.femaleHero == heroID || 0 == heroID) {
					continue;
				}
				Hero hero = heroMaps.get(heroID + "");
				HeroData heroData = TemplateManager.getTemplateData(hero.getTemplateID(), HeroData.class);
				int quality = heroData.getQuality();
				if (quality > 3) {
					blueNum += 1;
				}
			}
			mainTaskInfo.setNum(blueNum);
			break;
		}
		case ActionType.PURPLEHERONUM: {
			HeroModule heroModule = ModuleManager.getModule(ModuleNames.HeroModule, HeroModule.class);
			HeroEntity heroEntity = heroModule.getHeroEntity(playerID, ioMessage);
			Map<String, Hero> heroMaps = heroEntity.getHeroMap();
			List<Long> teamList = heroEntity.getTeamList();
			int purpleNum = 0;
			for (long heroID : teamList) {
				if (SysConstants.maleHero == heroID || SysConstants.femaleHero == heroID || 0 == heroID) {
					continue;
				}
				Hero hero = heroMaps.get(heroID + "");
				HeroData heroData = TemplateManager.getTemplateData(hero.getTemplateID(), HeroData.class);
				int quality = heroData.getQuality();
				if (quality > 4) {
					purpleNum += 1;
				}
			}
			mainTaskInfo.setNum(purpleNum);
			break;
		}
		case ActionType.FIGHTVALUE: {
			LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule, LoginModule.class);
			int fightValue = loginModule.getPlayerEntity(playerID).getFightValue();
			mainTaskInfo.setNum(fightValue);
			break;
		}
		case ActionType.LEGIONDEVOTE: {
			LegionModule legionModule = ModuleManager.getModule(ModuleNames.LegionModule, LegionModule.class);
			LegionMemberEntity legionMemberEntity = legionModule.getLegionMemberEntity(playerID);
			if (legionMemberEntity != null) {
				long maxDevote = legionMemberEntity.getMaxDevote();
				if (maxDevote > 0) {
					mainTaskInfo.setNum(1);
				}
			}
			break;
		}
		case ActionType.HEROCLASSLEVEL: {
			HeroModule heroModule = ModuleManager.getModule(ModuleNames.HeroModule, HeroModule.class);
			HeroEntity heroEntity = heroModule.getHeroEntity(playerID);
			Map<String, Hero> heroMap = heroEntity.getHeroMap();
			Set<Entry<String, Hero>> entrySet = heroMap.entrySet();
			int level = 0;
			for (Entry<String, Hero> entry : entrySet) {
				Hero hero = entry.getValue();
				long heroID = hero.getHeroID();
				if (SysConstants.maleHero == heroID || SysConstants.femaleHero == heroID || 0 == heroID) {
					continue;
				}
				int classLevel = hero.getClassLevel();
				if (classLevel > level) {
					level = classLevel;
				}
			}
			mainTaskInfo.setNum(level);
			break;
		}
		case ActionType.HEROLEVEL: {
			HeroModule heroModule = ModuleManager.getModule(ModuleNames.HeroModule, HeroModule.class);
			HeroEntity heroEntity = heroModule.getHeroEntity(playerID);
			Map<String, Hero> heroMap = heroEntity.getHeroMap();
			Set<Entry<String, Hero>> entrySet = heroMap.entrySet();
			int level = 0;
			for (Entry<String, Hero> entry : entrySet) {
				Hero hero = entry.getValue();
				long heroID = hero.getHeroID();
				if (SysConstants.maleHero == heroID || SysConstants.femaleHero == heroID || 0 == heroID) {
					continue;
				}
				int heroLevel = hero.getLevel();
				if (heroLevel > level) {
					level = heroLevel;
				}
			}
			mainTaskInfo.setNum(level);
			break;
		}
		case ActionType.TOWERLEVEL: {
			TowerModule towerModule = ModuleManager.getModule(ModuleNames.TowerModule, TowerModule.class);
			TowerEntity towerEntity = towerModule.getEntity(playerID);
			int passLevel = towerEntity.getPassLevel();
			int level = 0;
			if (passLevel != 0) {
				level = passLevel - TowerModule.initLevel + 1;
			}
			mainTaskInfo.setNum(level);
			break;
		}
		case ActionType.ARENARANK: {
			ArenaModule arenaModule = ModuleManager.getModule(ModuleNames.ArenaModule, ArenaModule.class);
			ArenaEntity arenaEntity = arenaModule.getArenaEntity(playerID);
			long rank = arenaEntity.getRank();
			mainTaskInfo.setNum((int) rank);
			break;
		}
		case ActionType.DESTINYPOINT: {
			LeadModule leadModule = ModuleManager.getModule(ModuleNames.LeadModule, LeadModule.class);
			LeadDesitnyEntity leadDesitnyEntity = leadModule.getDesitnyEntity(playerID);
			int num = leadDesitnyEntity.getDestinyNum();
			mainTaskInfo.setNum(num);
			break;
		}
		case ActionType.GETBLUEEQUIPMENTNUM: {
			EquipmentModule module = ModuleManager.getModule(ModuleNames.EquipmentModule, EquipmentModule.class);
			EquipmentMapEntity equipmentMapEntity = module.getEquipmentMapEntity(playerID);
			Map<String, Equipment> equips = equipmentMapEntity.getEquipMap();
			Set<Entry<String, Equipment>> entrySet = equips.entrySet();
			int num = 0;
			for (Entry<String, Equipment> entry : entrySet) {
				Equipment equipment = entry.getValue();
				int templateID = equipment.getTemplateID();
				EquipmentData equipmentData = TemplateManager.getTemplateData(templateID, EquipmentData.class);
				int quality = equipmentData.getQuality();
				if (quality > 3) {
					num++;
				}
			}
			mainTaskInfo.setNum(num);
			break;
		}
		case ActionType.PASSDUNGEON: {
			DungeonModule dungeonModule = ModuleManager.getModule(ModuleNames.DungeonModule, DungeonModule.class);
			DungeonMapEntity dungeonMapEntity = dungeonModule.getDungeonMapEntity(playerID, ioMessage);
			Map<String, Dungeon> dungeonMaps = dungeonMapEntity.getDungeonMap();
			Set<Entry<String, Dungeon>> entrySet = dungeonMaps.entrySet();
			for (Entry<String, Dungeon> entry : entrySet) {
				Dungeon dungeon = entry.getValue();
				if (dungeon.getTemplateID() == data.getParse()[0] && dungeon.getStarLevel() > 0) {
					mainTaskInfo.setNum(1);
					break;
				}
			}
			break;
		}
		case ActionType.LEADLEVELFORMAINTASK: {
			LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule, LoginModule.class);
			PlayerEntity playerEntity = loginModule.getPlayerEntity(playerID);
			int level = playerEntity.getLevel();
			mainTaskInfo.setNum(level);
			break;
		}
		case ActionType.LEADADVANCE: {
			HeroModule heroModule = ModuleManager.getModule(ModuleNames.HeroModule, HeroModule.class);
			Hero lead = heroModule.getLead(playerID, ioMessage);
			int classLevel = lead.getClassLevel();
			mainTaskInfo.setNum(classLevel);
			break;
		}
		case ActionType.HORSELEVEL: {
			TalismanModule talismanModule = ModuleManager.getModule(ModuleNames.TalismanModule, TalismanModule.class);
			TalismanMapEntity talismanMapEntity = talismanModule.getEntity(playerID, ioMessage);
			Map<String, TalismanEntity> talismanMap = talismanMapEntity.getTalismanMap();
			Set<Entry<String, TalismanEntity>> entrySet = talismanMap.entrySet();
			int level = 0;
			for (Entry<String, TalismanEntity> entry : entrySet) {
				TalismanEntity talismanEntity = entry.getValue();
				int templateID = talismanEntity.getTemplateID();
				TalismanData talismanData = TemplateManager.getTemplateData(templateID, TalismanData.class);
				int part = talismanData.getPart();
				if (part == EquipmentPart.horse) {
					level = talismanEntity.getStrengLevel();
				}
			}
			mainTaskInfo.setNum(level);
			break;
		}
		case ActionType.BUYVIPITEM: {
			StoreModule storeModule = ModuleManager.getModule(ModuleNames.StoreModule, StoreModule.class);
			StoreEntity storeEntity = storeModule.initStoreEntity(playerID);
			List<Integer> giftList = storeEntity.getVipGiftList();
			int limit = data.getParse()[0];
			if (giftList.contains(limit)) {
				mainTaskInfo.setNum(1);
			}
			break;
		}
		case ActionType.GETACTBOX: {
			int actID = data.getParse()[0];
			DungeonModule dungeonModule = ModuleManager.getModule(ModuleNames.DungeonModule, DungeonModule.class);
			DungeonActMapEntity actMapEntity = dungeonModule.getDungeonActEntity(playerID);
			DungeonAct dungeonAct = actMapEntity.getDungeMap().get(actID);
			if (dungeonAct != null) {
				Map<Integer, ActReward> dungeonMap = dungeonAct.getRewardlist();
				int point = data.getParse()[1];
				ActReward actReward = dungeonMap.get(point);
				if (actReward != null) {
					if (actReward.getState() == 1) {
						mainTaskInfo.setNum(1);
					}
				}
			}
			break;
		}
		case ActionType.GETNEWSERVERBOX: {
			WelfareModule welfareModule = ModuleManager.getModule(ModuleNames.WelfareModule, WelfareModule.class);
			WelfareLoginEntity loginEntity = welfareModule.getLoginEntity(playerID);
			List<Integer> getList = loginEntity.getRewardList();
			int limit = data.getParse()[0];
			if (getList != null) {
				if (getList.contains(limit)) {
					mainTaskInfo.setNum(1);
				}
			}
			break;
		}
		}
	}

	/**
	 * 领取任务奖励
	 * */
	public void getTaskReward(String playerID, int taskID, IOMessage ioMessage, MainTaskProtocol protocol) {
		MainTaskEntity mainTaskEntity = this.getTaskEntity(playerID, ioMessage);
		List<MainTaskInfo> taskList = mainTaskEntity.getMainTask();
		MainTaskInfo mainTaskInfo = null;
		for (MainTaskInfo taskInfo : taskList) {
			if (taskInfo.getTaskID() == taskID) {
				mainTaskInfo = taskInfo;
				break;
			}
		}
		if (mainTaskInfo != null) {
			MainTaskData data = TemplateManager.getTemplateData(taskID, MainTaskData.class);
			List<GoodsBean> goodsList = data.getReward();
			int num = mainTaskInfo.getNum();
			int goal = 0;
			if (ActionType.DUNGEONREACHSTARLEVEL == data.getActionType()) {
				goal = data.getParse()[1];
			} else if (ActionType.PASSDUNGEON == data.getActionType()) {
				goal = 1;
			} else if (ActionType.USEVITATLYITEM == data.getActionType()) {
				goal = 1;
			} else if (ActionType.BUYVITATLYITEM == data.getActionType()) {
				goal = 1;
			} else if (ActionType.BUYVIPITEM == data.getActionType()) {
				goal = 1;
			} else if (ActionType.GETACTBOX == data.getActionType()) {
				goal = 1;
			} else if (ActionType.GETNEWSERVERBOX == data.getActionType()) {
				goal = 1;
			} else {
				goal = data.getParse()[0];
			}
			if (ActionType.ARENARANK == data.getActionType()) {
				if (num > goal) {
					logger.error("没有达到任务完成条件");
					throw new IllegalArgumentException(ErrorIds.NOREACHTASKGOAL + "");
				}
			} else {
				if (num < goal) {
					logger.error("没有达到任务完成条件");
					throw new IllegalArgumentException(ErrorIds.NOREACHTASKGOAL + "");
				}
			}

			Map<String, GoodsBean> showMap = new HashMap<String, GoodsBean>();
			Map<String, Object> itemMap = new HashMap<>();
			RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
			int code = rewardModule.addGoods(playerID, goodsList, true, showMap, itemMap, ioMessage);
			if (code != 0) {
				protocol.setCode(code);
				return;
			}
			protocol.setShowMap(showMap);
			protocol.setItemMap(itemMap);
			List<Integer> completeList = mainTaskEntity.getCompleteList();
			completeList.add(taskID);
			mainTaskEntity.setCompleteList(completeList);
			taskList.remove(mainTaskInfo);
			int nextID = data.getNextID();
			if (nextID != 0) {
				this.addTask(playerID, nextID, ioMessage);
			} else {
				this.saveTaskEntity(mainTaskEntity);
			}

		}

	}

	public static void main(String[] args) {
		for (int i = 1; i <= 3; i++) {
			System.out.println(Utilities.MD5Encode((System.currentTimeMillis() + i) + "").substring(0, 8));
		}
	}
}
