package com.mi.game.module.manual;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.TemplateManager;
import com.mi.core.engine.annotation.Module;
import com.mi.core.util.MathUtil;
import com.mi.game.defines.ActionType;
import com.mi.game.defines.ErrorIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.defines.SysConstants;
import com.mi.game.module.achievement.AchievementModule;
import com.mi.game.module.base.BaseModule;
import com.mi.game.module.hero.data.HeroData;
import com.mi.game.module.mainTask.MainTaskModule;
import com.mi.game.module.manual.dao.HeroManualDAO;
import com.mi.game.module.manual.data.ManualAchievementData;
import com.mi.game.module.manual.data.ManualExpData;
import com.mi.game.module.manual.data.ManualPrototypeData;
import com.mi.game.module.manual.pojo.HeroManual;
import com.mi.game.module.manual.pojo.HeroManualsEntity;
import com.mi.game.module.manual.protocol.ManualAddExp;
import com.mi.game.module.vitatly.VitatlyModule;
import com.mi.game.module.vitatly.pojo.VitatlyEntity;
import com.mi.game.util.Utilities;

/**
 * @author 刘凯旋 图鉴模块 2014年7月3日 上午11:49:18
 */
@Module(name = ModuleNames.ManualModule,clazz= ManualModule.class)
public class ManualModule extends BaseModule{
	private final HeroManualDAO manualDao = HeroManualDAO.getInstance();
	private final Map<Integer,ManualExpData> manualExpData = new HashMap<Integer, ManualExpData>();
	private final Map<Integer,Integer> achieveData = new HashMap<Integer,Integer>();
	private final int maxManualLevel = 50;
	private final int blueHeroType = 4;
	private final int purpleHeroType = 5;
	
	/**
	 * 初始化
	 * */
	@Override
	public void init() {
		initExpData();
		initManualAchievement();
	}
	
	private void initExpData(){
		List<ManualExpData> dataList = TemplateManager.getTemplateList(ManualExpData.class);
		for(ManualExpData data : dataList){
			manualExpData.put(data.getLevel() , data);
		}
	}
	
	private void initManualAchievement(){
		List<ManualAchievementData> dataList = TemplateManager.getTemplateList(ManualAchievementData.class);
		for(ManualAchievementData data : dataList){
			achieveData.put(data.getNeedLikeability(), data.getStamina());
		}
	}
	public HeroManualsEntity initEntity(String playerID){
		HeroManualsEntity entity = new HeroManualsEntity();
		entity.setKey(playerID);
		return entity;
	}
	
	
	/**
	 * 获取实体
	 * */
	public HeroManualsEntity getEntity(String playerID){
		HeroManualsEntity entity =manualDao.getEntity(playerID);
		if(entity == null){
			logger.error("仙班实体为空");
			throw new IllegalArgumentException(ErrorIds.NoEntity + "");
		}
		return entity;
	}
	
	/**
	 * 获取实体(缓存)
	 * */
	public HeroManualsEntity getEntity(String playerID,IOMessage ioMessage){
		HeroManualsEntity entity = null;
		if(ioMessage != null){
			entity = (HeroManualsEntity)ioMessage.getInputParse().get(HeroManualsEntity.class.getName() + playerID);
			if(entity == null){
				entity = this.getEntity(playerID);
				ioMessage.getInputParse().put(HeroManualsEntity.class.getName() + playerID, entity);
			}
		}else{
			entity = this.getEntity(playerID);
		}
		return entity;
	}
	
	
	/**
	 * 保存实体
	 * */
	public void saveEntity(HeroManualsEntity heroManualsEntity){
		manualDao.save(heroManualsEntity);
	}
	
	
	/**
	 * 添加新的图鉴
	 * */
	
	public HeroManual addNewManual(String playerID,int templateID,boolean isSave,IOMessage ioMessage){
		HeroManualsEntity entity = this.getEntity(playerID,ioMessage);
		Map<Integer,HeroManual> map =  entity.getHeroList();
		HeroData heroData = TemplateManager.getTemplateData(templateID,HeroData.class);
		int charID = heroData.getCharactorID();
		HeroManual manual = map.get(charID);
		if(manual == null){
			manual = initManual(charID, map.size());
			map.put(charID, manual);
			HeroData data = TemplateManager.getTemplateData(charID,HeroData.class);
			if(data != null){
				int quailty = data.getQuality();
				int actionType = 0;
				if(quailty == purpleHeroType){
					actionType = ActionType.GETDIFFBLUEHERO;
				}else
				if(quailty == blueHeroType){
					actionType = ActionType.GETDIFFPURPLE;
				}
				AchievementModule acModule = ModuleManager.getModule(ModuleNames.AchievementModule,AchievementModule.class);
				acModule.refreshAchievement(playerID, actionType, 1);
			}
			if(isSave){
				this.saveEntity(entity);
			}
			return manual;
		}
		return null;
	}

	
	
	/**
	 * 初始化图鉴
	 * */
	
	public HeroManual initManual(int templateID, int size){
		HeroManual manual = new HeroManual();
		manual.setTemplateID(templateID);
		manual.setOrder(size + 1);
		manual.setRate(this.getRate(0, 0));
		return manual;
	}
	
	
	private double getRate (int level, int exp){
		ManualExpData data = manualExpData.get(level);
		int nextExp = 0;
		double  rate = 0;
		if(level > 0){
			ManualExpData levelUpData = manualExpData.get(level - 1);
			nextExp =data.getExp() - levelUpData.getExp() ;
			rate =  (double) (nextExp + (exp - data.getExp()) + 140)/ (data.getExp() +100 ) * data.getRate();
			BigDecimal bg = new BigDecimal(rate);
			rate  = bg.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
		}else{
			rate = data.getRate();
		}

		return rate;
	}
	

	
	
	/**
	 * 名将加经验
	 * */
	public ManualAddExp addManualExp(String playerID, int addExp, long manualID,Map<String,Object> itemMap,IOMessage ioMessage){
		ManualAddExp manualAddExp = new ManualAddExp();
		HeroManual heroManual; 
		heroManual = this.getManual(playerID, (int)manualID,ioMessage);
		int exp = heroManual.getExp();
		int level = heroManual.getLevel();
		HeroManualsEntity entity = this.getEntity(playerID, ioMessage);
		int point = entity.getPoint();
		int oldPoint = point ;
		if(level >= maxManualLevel){
			logger.error("已达到好感度的最高等级");
			throw new IllegalArgumentException(ErrorIds.ManualMaxLevel + "");
		}
		if(level < 0){
			logger.error("错误的名将等级");
			throw new IllegalArgumentException(ErrorIds.ManualWrongLevel + "");
		}
		int nowExp = exp + addExp;
		if(nowExp >= manualExpData.get(level).getExp()){
			for(int i = level + 1; i<= maxManualLevel ;i++ ){
				point += 1;
				if (nowExp < manualExpData.get(i).getExp()) {
					heroManual.setLevel(i);
					heroManual.setExp(nowExp);
					break;
				} else {
					if (i == 50) {
						if (nowExp >= manualExpData.get(49).getExp()) {
							heroManual.setLevel(i);
							heroManual.setExp(nowExp);
						}
					}
				}

			}
		}else{
			double rate = heroManual.getRate();
			if(rate == 0){
				rate = this.getRate(level, exp);
			}
			double random = Utilities.getrandomDouble();
			if(random <rate){
				heroManual.setExp(manualExpData.get(level).getExp());
				heroManual.setLevel(level + 1);
				manualAddExp.setCrit(true);
				point += 1;
			}else{
				heroManual.setExp(nowExp);
			}
		}
		entity.setPoint(point);
		if(point > oldPoint){
			MainTaskModule mainTaskModule = ModuleManager.getModule(ModuleNames.MainTaskModule,MainTaskModule.class);
			mainTaskModule.updateTaskByActionType(playerID, ActionType.MANUALLEVEL,point, ioMessage);
		}
		this.reachPoint(playerID, point, oldPoint,manualAddExp,itemMap);
		heroManual.setRate(this.getRate(heroManual.getLevel(), heroManual.getExp()));
		manualAddExp.setExp(heroManual.getExp());
		manualAddExp.setLevel(heroManual.getLevel());
		manualAddExp.setRate(heroManual.getRate());
		manualAddExp.setTemplateID(heroManual.getTemplateID());

		return manualAddExp;
	} 
	
	private ManualAddExp reachPoint(String playerID,int point,int oldPoint , ManualAddExp manualAddExp,Map<String,Object> itemMap){
		for(Entry<Integer,Integer> entry : achieveData.entrySet()){
			int key = entry.getKey();
			int value = entry.getValue();
			if(point >= key && oldPoint < key){
				VitatlyModule vitatlyModule = ModuleManager.getModule(ModuleNames.VitatlyModule,VitatlyModule.class);
				VitatlyEntity entity = vitatlyModule.getVitatlyEntity(playerID);
				entity.setMaxEnergy(entity.getMaxEnergy() + value);
				vitatlyModule.saveVitatlyEntity(entity);
				manualAddExp.setAchieve(1);
				manualAddExp.setMaxEnergy(entity.getMaxEnergy());
				itemMap.put("vitatlyEntity", entity.responseMap());
				break;
			}
		}
		return manualAddExp;
	}
	
	private HeroManual getManual(String playerID,int manualID,IOMessage ioMessage){
		HeroManualsEntity entity  = this.getEntity(playerID,ioMessage);
		Map<Integer,HeroManual> heroList = entity.getHeroList();
		HeroManual manual = heroList.get(manualID);
		if(manual == null){
			logger.error("未找到名将");
			throw new IllegalArgumentException(ErrorIds.ManualNotFound + "");
		}
		return manual;
	}
	
	public Map<Integer,Double> getPrototype(String playerID, int manualID,int atype,IOMessage ioMessage){
		HeroManual manual = this.getManual(playerID, manualID, ioMessage);
		int level = manual.getLevel();
		return this.calculatePrototype(atype, level);
	}
	
	public 	Map<Integer,Double> calculatePrototype(int templateID,int level){
		ManualPrototypeData data = TemplateManager.getTemplateData(templateID,ManualPrototypeData.class);
		if(data == null){
			throw new IllegalArgumentException(ErrorIds.NOTEMPLATE + "");
		}
		List<Integer> tempList = data.getProperty();
		Map<Integer,Double> propMap = new HashMap<>();
		for(int k =1 ;k <= level; k++){
			int key = 0;
			int base = 0;
			if(k > 10){
				int temp = 0;
				temp = k %10;
				if(temp == 0){
					temp  = 10;
				}
				key = tempList.get(temp -1);
			}else{
				key = tempList.get(k -1);
			}
			
			if(key == SysConstants.Hp){
				base = 50;
			}else{
				base = 5;
			}
			int b = (int) MathUtil.getDoubleCeilInt((double) k / 5);
			Double value = (double) (base  * b);
			if(propMap.get(key) != null){
				propMap.put(key, propMap.get(key) + value);
			}else{
				propMap.put(key, value);
			}
		}
		return propMap;
	}
	

}
