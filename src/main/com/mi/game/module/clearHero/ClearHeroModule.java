package com.mi.game.module.clearHero;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.mi.core.cache.QueryInfo;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.Module;
import com.mi.core.util.ConfigUtil;
import com.mi.game.defines.ModuleNames;
import com.mi.game.defines.SysConstants;
import com.mi.game.module.bag.BagModule;
import com.mi.game.module.bag.dao.BagEntityDAO;
import com.mi.game.module.bag.pojo.BagEntity;
import com.mi.game.module.bag.pojo.BagItem;
import com.mi.game.module.base.BaseModule;
import com.mi.game.module.equipment.EquipmentModule;
import com.mi.game.module.equipment.pojo.Equipment;
import com.mi.game.module.equipment.pojo.EquipmentMapEntity;
import com.mi.game.module.hero.HeroModule;
import com.mi.game.module.hero.pojo.Hero;
import com.mi.game.module.hero.pojo.HeroEntity;
import com.mi.game.module.hero.pojo.HeroTroopsEntity;
import com.mi.game.module.talisman.TalismanModule;
import com.mi.game.module.talisman.pojo.TalismanEntity;
import com.mi.game.module.talisman.pojo.TalismanMapEntity;
@Module(name = ModuleNames.ClearHeroModule,clazz = ClearHeroModule.class)
public class ClearHeroModule extends BaseModule{
	private static String[] clearList = ConfigUtil.getStringArray("clearList");
	private static final BagEntityDAO bagDao = BagEntityDAO.getInstance();
	
	public static String[] getClearList() {
		return clearList;
	}


	public static void setClearList(String[] clearList) {
		ClearHeroModule.clearList = clearList;
	}


	public void clearHero(){
		HeroModule heroModule = ModuleManager.getModule(ModuleNames.HeroModule,HeroModule.class);
		EquipmentModule equipmentModule = ModuleManager.getModule(ModuleNames.EquipmentModule, EquipmentModule.class);
		TalismanModule talismanModule = ModuleManager.getModule(ModuleNames.TalismanModule,TalismanModule.class);
		for(String playerID : clearList){
			HeroEntity heroEntity =  heroModule.getHeroEntity(playerID);
			HeroTroopsEntity heroTroopsEntity = heroModule.getHeroTroopsEntity(playerID);
			TalismanMapEntity talismanMapEntity = talismanModule.getEntity(playerID);
			Map<String,TalismanEntity> talismanMap =  new HashMap<>();
			List<Long> teamList = new ArrayList<>();
			for (int i = 0; i < 12; i++) {
				teamList.add(0l);
			}
			List<Long> troops = new ArrayList<>();
			for (int i = 0; i < 6; i++) {
				troops.add(0l);
			}
			if(heroEntity != null){
				Map<String, Hero> heroMap = heroEntity.getHeroMap();
				Set<Entry<String,Hero>> set = heroMap.entrySet();
				Map<String, Hero> nowHeroMap = new HashMap<String, Hero>();
				for (Entry<String,Hero> entry : set) {
					Hero hero = entry.getValue();
					String key = entry.getKey();
					long heroID = hero.getHeroID();
					if(heroID == SysConstants.maleHero || heroID == SysConstants.femaleHero){
						hero.setEquipMap(new HashMap<String, Long>());
						nowHeroMap.put(key, hero);
						teamList.set(0,heroID);
						troops.set(0,heroID);
					}
				}
				heroEntity.setTeamList(teamList);
				heroEntity.setHeroMap(nowHeroMap);
				heroModule.saveHeroEntity(heroEntity);
				heroTroopsEntity.setTroops(troops);
				heroModule.saveHeroTroops(heroTroopsEntity);
				
				talismanMapEntity.setTalismanMap(talismanMap);
				talismanModule.saveEntity(talismanMapEntity);
			}
			EquipmentMapEntity equipmentMapEntity = equipmentModule.getEquipmentMapEntity(playerID);
			if(equipmentMapEntity != null){
				equipmentMapEntity.setEquipMap(new HashMap<String, Equipment>());
				equipmentModule.saveEquipmentEntity(equipmentMapEntity);
				logger.error("清除玩家"+playerID);
			}
			
		}
	}
	
	public void searchItemNum(){
		BagModule bagModule = ModuleManager.getModule(ModuleNames.BagModule,BagModule.class);
		long total = bagModule.getBagCount();
		QueryInfo queryInfo = new QueryInfo(1, 100, "playerID");
		queryInfo.setTotal(total);
		queryInfo.initTotalPage();
		List<BagEntity> bagList = null;
		List<String> playerList = new ArrayList<>();
		// 分页查询数据
		while (queryInfo.getPage() <= queryInfo.getTotalPage()) {
			bagList = bagDao.queryPage(queryInfo);
			if (bagList == null || bagList.isEmpty()) {
				break;
			}
			int size = bagList.size();
			for (int i = 0; i < size; i++) {
				BagEntity bagEntity = bagList.get(i);
				Map<Integer,BagItem> bagMap = bagEntity.getBagList();
				Set<Entry<Integer,BagItem>> entrySet  = bagMap.entrySet();
				for(Entry<Integer,BagItem> entry : entrySet){
					BagItem bagItem = entry.getValue();
					int templateID = bagItem.getTemplateID();
					if(templateID == 10176 || templateID == 10174){
						continue;
					}
					int num = bagItem.getNum();
					if(num > 200){
						playerList.add(bagEntity.getKey().toString());
					}
				}
			}
			queryInfo.setPage(queryInfo.getPage() + 1);
		}
		try{
			FileOutputStream out = new FileOutputStream(new File("itemPlayerIDList.properties"));
			for(String playerID : playerList){
				out.write((playerID + "\r\n").getBytes());
			}
			out.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	
	}
	
	
}
