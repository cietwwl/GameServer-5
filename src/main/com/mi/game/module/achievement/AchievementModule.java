package com.mi.game.module.achievement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.TemplateManager;
import com.mi.core.engine.annotation.Module;
import com.mi.game.defines.ActionType;
import com.mi.game.defines.ErrorIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.achievement.dao.AchievementDAO;
import com.mi.game.module.achievement.data.AchievementData;
import com.mi.game.module.achievement.pojo.AchievementEntity;
import com.mi.game.module.achievement.pojo.AchievementInfo;
import com.mi.game.module.achievement.protocol.AchievementProtocol;
import com.mi.game.module.base.BaseModule;
import com.mi.game.module.dungeon.DungeonModule;
import com.mi.game.module.dungeon.pojo.Dungeon;
import com.mi.game.module.dungeon.pojo.DungeonEliteEntity;
import com.mi.game.module.dungeon.pojo.DungeonMapEntity;
import com.mi.game.module.reward.RewardModule;
import com.mi.game.module.reward.data.GoodsBean;
@Module(name = ModuleNames.AchievementModule,clazz = AchievementModule.class)
public class AchievementModule extends BaseModule{
	private final Map<Integer,AchievementInfo> initAcMap = new HashMap<Integer, AchievementInfo>();
	private AchievementDAO achievementDAO  = AchievementDAO.getInstance();
	private final int dungeonType = 1;
	private final int leadType = 2;
	private final int heroType = 3;
	private final int equipType = 4;
	@Override
	public void init(){
		initAcMap();
	}
	
	private void initAcMap(){
		List<AchievementData> dataList = TemplateManager.getTemplateList(AchievementData.class);
		for(AchievementData data  : dataList){
			if(data.getIsOrigin() == 1){
				AchievementInfo achievementInfo = new AchievementInfo();
				achievementInfo.setPid(data.getPid());
				initAcMap.put(data.getActionID(), achievementInfo);
			}
		}
	}
	
	public AchievementEntity getEntity(String playerID){
		AchievementEntity achievementEntity = achievementDAO.getEntity(playerID);
		if(achievementEntity == null){
			achievementEntity = this.initaAchievementEntity(playerID);
		}
		return achievementEntity;
	}
	
	public AchievementEntity initaAchievementEntity(String playerID){
		AchievementEntity entity = new AchievementEntity();
		entity.setKey(playerID);
		entity.setAchievementList(initAcMap);
		return entity;
	}
	
	public void saveAchievementEntity(AchievementEntity entity){
		achievementDAO.save(entity);
	}
	
	/**
	 * 更新成就
	 * @param playerID String 玩家ID
	 * @param actionType int 行为类型
	 * @param value int 行为值
	 * */
	
	public void refreshAchievement(String playerID,int actionType,long value){
		AchievementEntity entity = this.getEntity(playerID);
		Map<Integer,AchievementInfo> acMap =  entity.getAchievementList();
		AchievementInfo acInfo = acMap.get(actionType);
		if(acInfo == null){
			return ;
		}
		int pid = acInfo.getPid();
		AchievementData data = TemplateManager.getTemplateData(pid, AchievementData.class);
		this.addAchievementValue(data, acInfo, value, actionType);
		this.saveAchievementEntity(entity);
	}
	
	private void addAchievementValue(AchievementData data, AchievementInfo acInfo, long value, int actionType){
		switch(actionType){
		case ActionType.FIRSTDOWNDUNGEONNORMAL:
			if(value == data.getNum()){
				acInfo.setNum(value);
			}
			break;
		case ActionType.FIRSTDOWNDUNGEONELITE:
			if(value == data.getNum()){
				acInfo.setNum(value);
			}
			break;
		case ActionType.TALISMANPLUNDERNUM:
			acInfo.setNum(acInfo.getNum() + value);
			break;
		case ActionType.GETDIFFBLUEHERO:
			acInfo.setNum(acInfo.getNum() + value);
			break;
		case ActionType.GETDIFFPURPLE:
			acInfo.setNum(acInfo.getNum() + value);
			break;
		case ActionType.COMPOSEBULEHERO:
			acInfo.setNum(acInfo.getNum() + value);
			break;
		case ActionType.COMPOSEPURPLEHERO:
			acInfo.setNum(acInfo.getNum() + value);
			break;
		case ActionType.TOWERLEVEL:
			int level = Integer.parseInt((value +"").substring(6));
			if(level > acInfo.getNum()){
				acInfo.setNum(level);
			}
			break;
		case ActionType.GETBLUEEQUIPMENT:
			acInfo.setNum(acInfo.getNum() + value);
			break;
		case ActionType.GETGREENEQUIPMENT:
			acInfo.setNum(acInfo.getNum() + value);
			break;
		case ActionType.GETPURPLEEQUIPMENT:
			acInfo.setNum(acInfo.getNum() + value);
			break;
		case ActionType.GETTALISMAN:
			acInfo.setNum(acInfo.getNum() + value);
			break;
		case ActionType.EQUIPMENTREFINENUM:
			acInfo.setNum(acInfo.getNum() + value);
			break;
		default:
			if(acInfo.getNum() < value){
				acInfo.setNum(value);
			}
			break;
		}
	}
	
	
	/**
	 * 领取成就奖励
	 * */
	public void getAchievementReward(String playerID,int pid,AchievementProtocol protocol){
		AchievementEntity entity = this.getEntity(playerID);
		Map<Integer, AchievementInfo> acMap = entity.getAchievementList();
		AchievementInfo acInfo = null;
		int removeID = 0;
		for(Entry<Integer,AchievementInfo> entry : acMap.entrySet()){
			AchievementInfo temp = entry.getValue();
			if(temp.getPid() == pid){
				acInfo = temp;
				removeID = entry.getKey();
			}
		}
		if(acInfo == null){
			throw new IllegalArgumentException(ErrorIds.AchievementIDNotFound + "");
		}
		AchievementData data = TemplateManager.getTemplateData(pid, AchievementData.class);
		long needNum = data.getNum();
		long value = acInfo.getNum();
		if(value < needNum){
			throw new IllegalArgumentException(ErrorIds.AchievementNoReach + "");
		}
		GoodsBean goodsBean = data.getReward();
		Map<String,Object> itemMap = new HashMap<>();
		List<GoodsBean> showMap = new ArrayList<>();
		showMap.add(goodsBean);
		RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
		rewardModule.addGoods(playerID, goodsBean.getPid(), goodsBean.getPid(), null, true, null, itemMap, null);
		protocol.setItemMap(itemMap);
		protocol.setShowMap(showMap);
		int type = data.getType();
		switch(type){
			case heroType :
				entity.setHeroCount(entity.getHeroCount() + 1);
				protocol.setHeroCount(entity.getHeroCount());
				break;
			case leadType:
				entity.setLeadCount(entity.getLeadCount() + 1);
				protocol.setLeadCount(entity.getLeadCount());
				break;
			case equipType:
				entity.setEquipCount(entity.getEquipCount() + 1);
				protocol.setEquipCount(entity.getEquipCount());
				break;
			case dungeonType :
				entity.setDungeonCount(entity.getDungeonCount() + 1);
				protocol.setDungeonCount(entity.getDungeonCount());
				break;
		}
		int nextID = data.getNextID();
		if(nextID != 0){
			acInfo.setPid(nextID);
			int actionType = data.getActionID();
			AchievementData nextData = TemplateManager.getTemplateData(nextID, AchievementData.class);
			long nextValue = nextData.getNum();
			switch(actionType){
				case ActionType.FIRSTDOWNDUNGEONNORMAL:
					acInfo.setNum(0);
					DungeonModule dungeonModule= ModuleManager.getModule(ModuleNames.DungeonModule,DungeonModule.class);
					DungeonMapEntity dungeonMapEntity = dungeonModule.getDungeonMapEntity(playerID);
					Map<String,Dungeon> dungeonMap = dungeonMapEntity.getDungeonMap();
					for(Entry<String, Dungeon> entry : dungeonMap.entrySet()){
						Dungeon dungeon = entry.getValue();
						if(dungeon.getTemplateID() > nextValue){
							acInfo.setNum(nextValue);
							break;
						}
					}
					break;
				case ActionType.FIRSTDOWNDUNGEONELITE:
					acInfo.setNum(0);
					dungeonModule= ModuleManager.getModule(ModuleNames.DungeonModule,DungeonModule.class);
					DungeonEliteEntity dungeonEliteEntity = dungeonModule.getDungeonEliteEntity(playerID);
					List<Integer> eliteList = dungeonEliteEntity.getEliteList();
					for(int eliteID : eliteList ){
						if(eliteID > nextValue){
							acInfo.setNum(nextValue);
							break;
						}
					}
					break;
			}
			protocol.setAcInfo(acInfo);
		}else{
			acMap.remove(removeID);
		}
		this.saveAchievementEntity(entity);
	}
	
}
