package com.mi.game.module.smelt;

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
import com.mi.game.defines.EquipmentPart;
import com.mi.game.defines.ErrorIds;
import com.mi.game.defines.KindIDs;
import com.mi.game.defines.ModuleNames;
import com.mi.game.defines.RebirthType;
import com.mi.game.defines.SmeltType;
import com.mi.game.defines.SysConstants;
import com.mi.game.module.base.BaseModule;
import com.mi.game.module.equipment.EquipmentModule;
import com.mi.game.module.equipment.data.EquipmentData;
import com.mi.game.module.equipment.pojo.Equipment;
import com.mi.game.module.equipment.pojo.EquipmentMapEntity;
import com.mi.game.module.hero.HeroModule;
import com.mi.game.module.hero.data.HeroData;
import com.mi.game.module.hero.pojo.Hero;
import com.mi.game.module.hero.pojo.HeroEntity;
import com.mi.game.module.mainTask.MainTaskModule;
import com.mi.game.module.reward.RewardModule;
import com.mi.game.module.reward.data.GoodsBean;
import com.mi.game.module.smelt.protocol.SmeltProtocol;
import com.mi.game.module.talisman.TalismanModule;
import com.mi.game.module.talisman.data.TalismanData;
import com.mi.game.module.talisman.data.TalismanRefineData;
import com.mi.game.module.talisman.pojo.TalismanEntity;
import com.mi.game.module.talisman.pojo.TalismanMapEntity;
@Module(name= ModuleNames.SmeltModule,clazz = SmeltModule.class)
public class SmeltModule extends BaseModule{
	private static int silverHorse = 101635;
	private static int silverBook = 101637;
	/**
	 * 装备炼化
	 * */
	
	public void doSmelt(String playerID, List<Object> smeltList, int type ,IOMessage ioMessage,SmeltProtocol protocol){
		List<GoodsBean> goodsList = new ArrayList<>();
		Map<Integer,GoodsBean> addList = new HashMap<>();
		Map<String,Object> itemMap = new HashMap<String, Object>();
		Map<Integer,GoodsBean> showList = new HashMap<>();
		try{
			switch(type){
				case SmeltType.equipmentSmelt:
					addList = this.equipmentSmelt(playerID, smeltList, itemMap,ioMessage);
					break;
				case SmeltType.heroSmelt:
					addList = this.heroSmelt(playerID,smeltList,itemMap,ioMessage);
					break;
				case SmeltType.talismanSmelt:
					addList = this.talismanSmelt(playerID, smeltList,itemMap, ioMessage,showList);
			}
		}catch(Exception ex){
			protocol.setCode(Integer.parseInt(ex.getMessage()));
			return;
		}
		goodsList = this.getGoodList(addList);
		
		RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule,RewardModule.class);
		int code = rewardModule.addGoods(playerID, goodsList, true, null, itemMap, ioMessage);
		if(code != 0){
			protocol.setCode(code);
			return ;
		}
		if(type == SmeltType.talismanSmelt){
			if(!showList.isEmpty()){
				for(Entry<Integer,GoodsBean> entry : showList.entrySet()){
					int key = entry.getKey();
					GoodsBean value = entry.getValue();
					if(addList.containsKey(key)){
						GoodsBean tempBean = addList.get(key);
						tempBean.setNum(tempBean.getNum() + value.getNum());
						addList.put(key,tempBean);
					}else{
						addList.put(key, value);
					}
				}
			}
		}
		protocol.setShowMap(addList);
		protocol.setItemMap(itemMap);
	}
	
	private 	Map<Integer,GoodsBean> equipmentSmelt(String playerID,List<Object> smeltList,Map<String,Object> itemMap,IOMessage ioMessage){
		EquipmentModule module = ModuleManager.getModule(ModuleNames.EquipmentModule,EquipmentModule.class);
		EquipmentMapEntity entity = module.getEquipmentMapEntity(playerID, ioMessage);
		Map<String,Equipment>  map = entity.getEquipMap();
		Map<Integer,GoodsBean> goodsList = new HashMap<Integer, GoodsBean>();
		for(Object temp : smeltList){
			long equipID = Long.parseLong(temp.toString());
			Equipment equipment = map.get(equipID + "");
			if(equipment == null){
				logger.error("装备未找到");
				throw new IllegalArgumentException(ErrorIds.EquipNotFound + "");
			}
			int templateID = equipment.getTemplateID();
			EquipmentData data = TemplateManager.getTemplateData(templateID,EquipmentData.class);
			if(data.getCanResolve() != 1){
				logger.error("装备不可炼化");
				throw new IllegalArgumentException(ErrorIds.EquipmentNotSmelt + "");
			}
			if(equipment.getHeroID() != 0){
				logger.error("装备中的装备不可炼化");
				throw new IllegalArgumentException(ErrorIds.EquipedNotSmelt + "");
			}
			Map<Integer, Integer> resolveItemData = data.getResolveItem();
			Map<Integer,Integer> resolveItem = new HashMap<>();
			int refineNum = equipment.getRefineNum();
			if(resolveItemData.containsKey(SysConstants.refineStoneID)){
				resolveItem.put(SysConstants.refineStoneID,resolveItemData.get(SysConstants.refineStoneID) + refineNum);
			}else{
				if(refineNum != 0){
					resolveItem.put(SysConstants.refineStoneID, refineNum);
				}
			}
			int strengSilver =  equipment.getStrengSilver();
			if(resolveItemData.containsKey(KindIDs.SILVERTYPE)){
				resolveItem.put(KindIDs.SILVERTYPE,resolveItemData.get(KindIDs.SILVERTYPE) + strengSilver);
			}else{
				resolveItem.put(KindIDs.SILVERTYPE, strengSilver);
			}
			this.addGoodsList(resolveItem,goodsList);
			map.remove(equipID + "");
			MainTaskModule mainTaskModule = ModuleManager.getModule(ModuleNames.MainTaskModule,MainTaskModule.class);
			mainTaskModule.updateTaskByActionType(playerID, ActionType.SMELTEQUIPMENT, 0, ioMessage);
		}
		itemMap.put("removeEquipmentList",smeltList);
		return goodsList;
	}
	
	private  	Map<Integer,GoodsBean> heroSmelt(String playerID,List<Object> smeltList,Map<String,Object> itemMap,IOMessage ioMessage){
		HeroModule heroModule = ModuleManager.getModule(ModuleNames.HeroModule,HeroModule.class);
		HeroEntity heroEntity = heroModule.getHeroEntity(playerID, ioMessage);
		Map<String,Hero> heroMap = heroEntity.getHeroMap();
		List<Long> teamList = heroEntity.getTeamList();
		Map<Integer,GoodsBean> goodsList = new HashMap<Integer, GoodsBean>();
		for(Object temp : smeltList){
			long heroID = Long.parseLong(temp.toString());
			Hero hero = heroMap.get(heroID + "");
			if(hero == null){
				logger.error("英雄未找到" + playerID);
				throw new NullPointerException(ErrorIds.HeroNotFound + "");
			}
			int templateID = hero.getTemplateID();
			HeroData data = TemplateManager.getTemplateData(templateID,HeroData.class);
			if(data.getCanResolve() != 1){
				logger.error("英雄不可炼化");
				throw new IllegalArgumentException(ErrorIds.HeroNotSmelt + "");
			}
			if(teamList.contains(heroID)){
				logger.error("出战中的英雄不可炼化");
				throw new IllegalArgumentException(ErrorIds.EquipedNotSmelt + "");
			}
			if(hero.getClassLevel() != 0){
				logger.error("进阶的英雄不可炼化");
				throw new IllegalArgumentException(ErrorIds.AdvanceHeroNotSmelt + "");
			}
			Map<Integer, Integer> copyItem = data.getResolveItem();
			Map<Integer,Integer> resolveItem = new HashMap<>();
			resolveItem.putAll(copyItem);
			int exp = hero.getExp() ;
			if(exp != 0){
				if(resolveItem.containsKey(KindIDs.HEROSOUL)){
					resolveItem.put(KindIDs.HEROSOUL, resolveItem.get(KindIDs.HEROSOUL) + exp);
				}else{
					resolveItem.put(KindIDs.HEROSOUL, exp);
				}
				
				if(resolveItem.containsKey(KindIDs.SILVERTYPE)){
					resolveItem.put(KindIDs.SILVERTYPE, resolveItem.get(KindIDs.SILVERTYPE) + exp);
				}else{
					resolveItem.put(KindIDs.SILVERTYPE, exp);
				}
			}
			this.addGoodsList(resolveItem, goodsList);
			heroMap.remove(heroID + "");
			MainTaskModule mainTaskModule = ModuleManager.getModule(ModuleNames.MainTaskModule,MainTaskModule.class);
			mainTaskModule.updateTaskByActionType(playerID, ActionType.SMELTHERO, 0, ioMessage);
		}
		itemMap.put("removeHeroList",smeltList);
		return goodsList ;
	}
	
	private Map<Integer,GoodsBean> talismanSmelt(String playerID,List<Object> smeltList,Map<String,Object> itemMap,IOMessage ioMessage,Map<Integer,GoodsBean> showList){
		Map<Integer,GoodsBean> goodsList = new HashMap<Integer, GoodsBean>();
		TalismanModule module = ModuleManager.getModule(ModuleNames.TalismanModule,TalismanModule.class);
		TalismanMapEntity  entity = module.getEntity(playerID, ioMessage);
		Map<String,TalismanEntity> map = entity.getTalismanMap();
		RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule,RewardModule.class);
		for(Object temp : smeltList){
			long talismanID = Long.parseLong(temp.toString());
			TalismanEntity talisman = map.get(talismanID + "");
			if(talisman == null){
				logger.error("宝物未找到");
				throw new IllegalArgumentException(ErrorIds.TalismanNotFound + "");
			}
			int templateID = talisman.getTemplateID();
			TalismanData data = TemplateManager.getTemplateData(templateID,TalismanData.class);
			int quality = data.getQuality();
			if(data.getCanResolve() != 1){
				logger.error("宝物不可炼化");
				throw new IllegalArgumentException(ErrorIds.TalismanNotSmelt + "");
			}
			if(talisman.getHeroID() != 0){
				logger.error("装备中的宝物不可炼化");
				throw new IllegalArgumentException(ErrorIds.EquipedTalismanNotSmelt + "");	
			}
			Map<Integer,Integer> resolveItem =  data.getResolveItem();
			Map<Integer,Integer> copyItem = new HashMap<>();
			copyItem.putAll(resolveItem);
			int baseExp = data.getBaseEXP();
			int exp = talisman.getExp();
			if(exp != 0){
				if(copyItem.containsKey(KindIDs.SILVERTYPE)){
					copyItem.put(KindIDs.SILVERTYPE, exp*15 + copyItem.get(KindIDs.SILVERTYPE));
				}else{
					copyItem.put(KindIDs.SILVERTYPE, exp*15);
				}
			}
			exp += baseExp;
			int expPid = 0;
			if(data.getPart() == EquipmentPart.book){
				expPid = SysConstants.ExpSilverBook;
			}else if(data.getPart() == EquipmentPart.horse){
				expPid = SysConstants.ExpSilverHourse;
			}
			if(showList.containsKey(expPid)){
				GoodsBean tempBean = showList.get(expPid);
				tempBean.setNum(tempBean.getNum() + 1);
			}else{
				showList.put(expPid,new GoodsBean(expPid,1));
			}
			if(exp > 200){
				exp -= 200;
			}
			rewardModule.addGoods(playerID, expPid,1, exp, false, null, itemMap, ioMessage);
			int refineLevel = talisman.getRefineLevel();
			if(refineLevel > 0){
				for(int i = 0; i < refineLevel ; i ++){
					TalismanRefineData refineData = TalismanModule.refineData.get(quality).get(i);
					int moneyReq = refineData.getMoneyReq();
					GoodsBean silverBean = goodsList.get(KindIDs.SILVERTYPE);
					if(silverBean != null){
						silverBean.setNum(silverBean.getNum() + moneyReq);
					}else{
						silverBean = new GoodsBean(KindIDs.SILVERTYPE, moneyReq);
						goodsList.put(KindIDs.SILVERTYPE, silverBean);
					}
					GoodsBean dataBean =  refineData.getItemReq();
					GoodsBean goodsBean =new GoodsBean(dataBean.getPid(),dataBean.getNum());
					int pid = goodsBean.getPid();
					int num = goodsBean.getNum();
					if(goodsList.containsKey(pid)){
						goodsList.get(pid).setNum(goodsList.get(pid).getNum() + num);
					}else{
						goodsList.put(pid, new GoodsBean(pid,num));
					}
				}
			}
			this.addGoodsList(copyItem, goodsList);
			map.remove(talismanID + "");
		}

		itemMap.put("removeTalismanList",smeltList);
		return goodsList;
	}
	
	private void addGoodsList(Map<Integer, Integer> resolveItem,Map<Integer,GoodsBean> goodsList){
		for(Entry<Integer,Integer> entry : resolveItem.entrySet()){
			int key = entry.getKey();
			int value = entry.getValue();
			GoodsBean goodsBean ;
			if(goodsList.get(key) == null){
				goodsBean = new GoodsBean();
				goodsBean.setNum(value);
				goodsBean.setPid(key);
			}else{
				goodsBean = goodsList.get(key);
				goodsBean.setNum(goodsBean.getNum() + value);
			}
			goodsList.put(key, goodsBean);
		}
	}
	
	private List<GoodsBean> getGoodList(	Map<Integer,GoodsBean> goodsList){
		List<GoodsBean> beanList = new ArrayList<>();
		for (GoodsBean bean :goodsList.values()){
			beanList.add(bean);
		}
		return beanList;
	}
	
	/**
	 * 重生
	 * */
	public void doRebirth(String playerID, long targetID,int type,IOMessage ioMessage,SmeltProtocol protocol){
		List<GoodsBean> goodsList = new ArrayList<>();
		Map<Integer,GoodsBean> addList = new HashMap<>();
		Map<String,Object> itemMap = new HashMap<String, Object>();
		List<GoodsBean> removeList = new ArrayList<>();
		try{
				switch(type){
					case RebirthType.equipmentRebirth:
						addList = this.equipmentRebirth(playerID,targetID,ioMessage,itemMap,removeList);
						break;
					case RebirthType.heroRebirth:
						addList = this.heroRebirth(playerID, targetID, ioMessage,itemMap,removeList);
						break;
					case RebirthType.talismanRebirth:
						addList = this.talismanRebirth(playerID, targetID, ioMessage,itemMap,removeList);
						break;
					default:
						protocol.setCode(ErrorIds.UnKnowType);
						return;
				}	
		}catch(Exception ex){
			protocol.setCode(Integer.parseInt(ex.getMessage()));
		}
		int code = 0;
		RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule,RewardModule.class);
		code = rewardModule.useGoods(playerID, removeList, targetID, false, null, itemMap, ioMessage);
		if(code != 0){
			protocol.setCode(code);
			return ;
		}
		goodsList = this.getGoodList(addList) ;
		code = rewardModule.addGoods(playerID, goodsList, true, null, itemMap, ioMessage);
		if(code != 0){
			protocol.setCode(code);
			return ;
		}
		protocol.setItemMap(itemMap);
		protocol.setShowMap(addList);
	}
	
	private Map<Integer,GoodsBean> equipmentRebirth(String playerID, long targetID,IOMessage ioMessage,Map<String,Object> itemMap,List<GoodsBean> removeList){
		EquipmentModule equipmentModule  = ModuleManager.getModule(ModuleNames.EquipmentModule,EquipmentModule.class);
		EquipmentMapEntity entity = equipmentModule.getEquipmentMapEntity(playerID, ioMessage);
		Map<String,Equipment> eqMap  = entity.getEquipMap();
		Equipment equipment = eqMap.get(targetID + "");
		if(equipment == null){
			logger.error("装备未找到");
			throw new IllegalArgumentException(ErrorIds.EquipNotFound + "");
		}
		Map<Integer,GoodsBean> list = new HashMap<>();
		int templateID = equipment.getTemplateID();
		int level = equipment.getStrengLevel();
		long heroID = equipment.getHeroID();
		EquipmentData data = TemplateManager.getTemplateData(templateID,EquipmentData.class);
		if(level <2 || data.getCanRebirth() != 1 || heroID != 0){
			logger.error("装备不可重生");
			throw new IllegalArgumentException(ErrorIds.EquipmentNotRebirth + "");
		}
		int silver = equipment.getStrengSilver();
		int refineNum = equipment.getRefineNum();
		if(silver != 0){
			GoodsBean silverBean = new GoodsBean(KindIDs.SILVERTYPE,silver);
			list.put(KindIDs.SILVERTYPE,silverBean);
		}
		if(refineNum != 0){
			GoodsBean stoneBean = new GoodsBean(SysConstants.refineStoneID, refineNum);
			list.put(SysConstants.refineStoneID,stoneBean);
		}
		equipment  =  equipmentModule.getInitEquipment(templateID, targetID);
		List<Equipment> equipList = new ArrayList<Equipment>();
		eqMap.put(targetID + "", equipment);
		equipList.add(equipment);
		itemMap.put("equipList", equipList);
		GoodsBean goldBean = new GoodsBean(KindIDs.GOLDTYPE,50);
		removeList.add(goldBean);
		return list;
	}
	
	private  Map<Integer,GoodsBean> heroRebirth(String playerID, long targetID,IOMessage ioMessage,Map<String,Object> itemMap,List<GoodsBean> removeList){
		HeroModule heroModule = ModuleManager.getModule(ModuleNames.HeroModule,HeroModule.class);
		Hero hero = heroModule.getHero(playerID, targetID, ioMessage);
		int level  = hero.getClassLevel();
		int templateID = hero.getTemplateID();
		int exp = hero.getExp();
		int silver = exp;
		Map<Integer,GoodsBean> list = new HashMap<Integer,GoodsBean>();
		if(silver != 0){
			GoodsBean silverBean = new GoodsBean(KindIDs.SILVERTYPE, silver);
			list.put(KindIDs.SILVERTYPE, silverBean);
		}
		if(exp != 0){
			GoodsBean soulBean = new GoodsBean(KindIDs.HEROSOUL,exp);
			list.put(KindIDs.HEROSOUL,soulBean);
		}
		HeroData data = TemplateManager.getTemplateData(templateID,HeroData.class);
		int firstID = data.getOriginalID();
		for(int i = 1; i<= level ; i++){
			int backID = templateID - i;
			HeroData heroBackData = TemplateManager.getTemplateData(backID,HeroData.class);
			heroModule.getClassStuff(list, heroBackData.getNextReq(),firstID);
		}
		hero.setLevel(1);
		hero.setClassLevel(0);
		hero.setExp(0);
		hero.setHeroAdvanceProp(new HashMap<Integer ,Double>());
		
		hero.setTemplateID(firstID);
		List<Object> heroList = new ArrayList<>();
		heroList.add(hero.responseMap());
		itemMap.put("addHeroList", heroList);
		GoodsBean goldBean = new GoodsBean(KindIDs.GOLDTYPE,this.getHeroRebirthGold(level));
		removeList.add(goldBean);
		return list;
	}
	
	/**
	 * 计算英雄重生金币
	 * */
	private int getHeroRebirthGold(int classLevel){
		return 20*(classLevel + 1);
	}
	
	/**
	 * 宝物重生
	 * */
	private Map<Integer,GoodsBean> talismanRebirth(String playerID, long targetID,IOMessage ioMessage,Map<String,Object> itemMap,List<GoodsBean> removeList){
		TalismanModule talismanModule = ModuleManager.getModule(ModuleNames.TalismanModule, TalismanModule.class);
		TalismanMapEntity talismanMapEntity = talismanModule.getEntity(playerID, ioMessage);
		Map<String, TalismanEntity> talismanMap = talismanMapEntity.getTalismanMap();
		TalismanEntity talisman = talismanMap.get(targetID + "");
		if (talisman == null) {
			throw new NullPointerException(ErrorIds.TalismanNotFound + "");
		}
		long heroID = talisman.getHeroID();
		int exp = talisman.getExp();
		int templateID = talisman.getTemplateID();
		TalismanData data = TemplateManager.getTemplateData(templateID,TalismanData.class);
		int level = talisman.getStrengLevel();
		int refineLevel = talisman.getRefineLevel();
		int quality = data.getQuality();
		if( data.getCanRebirth() != 1 || heroID != 0 ){
			logger.error("法宝不可重生");
			throw new IllegalArgumentException(ErrorIds.TalismanNotRebirth + "");
		}
		if(level < 1 && refineLevel < 1){
			logger.error("法宝不可重生");
			throw new IllegalArgumentException(ErrorIds.TalismanNotRebirth + "");
		}
		Map<Integer,GoodsBean> list = new HashMap<Integer,GoodsBean>();
		if(exp > 0){
			int silver = exp * 15;
			//int silver = exp * this.getTalismanRebirthSilverRate(quality);
			GoodsBean silverBean = new GoodsBean(KindIDs.SILVERTYPE, silver);
			list.put(KindIDs.SILVERTYPE, silverBean);
			int expID = 0;
			if(data.getPart() == EquipmentPart.book){
				expID = silverBook;
			}else{
				expID = silverHorse;
			}
			GoodsBean expBean = new GoodsBean(expID,1);
			if(exp >= 200){
				exp -=200;
			}
			expBean.setParam(exp);
			list.put(expID, expBean);
		}

		if(refineLevel > 0){
			for(int i = 0; i < refineLevel ; i ++){
				TalismanRefineData refineData = TalismanModule.refineData.get(quality).get(i);
				int moneyReq = refineData.getMoneyReq();
				GoodsBean silverBean = list.get(KindIDs.SILVERTYPE);
				if(silverBean != null){
					silverBean.setNum(silverBean.getNum() + moneyReq);
				}else{
					silverBean = new GoodsBean(KindIDs.SILVERTYPE, moneyReq);
					list.put(KindIDs.SILVERTYPE, silverBean);
				}
				GoodsBean goodsBean = refineData.getItemReq();
				GoodsBean tempBean  = new GoodsBean(goodsBean.getPid(),goodsBean.getNum());
				int pid = tempBean.getPid();
				if(pid == 9999){
					pid = templateID;
				}
				int num = tempBean.getNum();
				if(list.containsKey(pid)){
					list.get(pid).setNum(list.get(pid).getNum() + num);
				}else{
					list.put(pid, new GoodsBean(pid,num));
				}
			}
		}
		talisman = talismanModule.getInitTalisman(templateID, targetID);
		talismanMap.put(targetID + "", talisman);
		GoodsBean goldBean = new GoodsBean(KindIDs.GOLDTYPE,50);
		removeList.add(goldBean);
		List<TalismanEntity> talismanList = new ArrayList<TalismanEntity>();
		talismanList.add(talisman);
		itemMap.put("addTalismanList", talismanList);
		return list;
	}
	
//	private int getTalismanRebirthSilverRate(int quality){
//		int num = 0;
//		switch(quality){
//		case QualityType.epic:
//			num = 10;
//			break;
//		case QualityType.legend:
//			num = 15;
//			break;
//		}
//		return num;
//	}
}
