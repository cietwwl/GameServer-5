package com.mi.game.module.farm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.TemplateManager;
import com.mi.core.engine.annotation.Module;
import com.mi.core.util.DateTimeUtil;
import com.mi.game.defines.ActionType;
import com.mi.game.defines.ErrorIds;
import com.mi.game.defines.FarmGetType;
import com.mi.game.defines.KindIDs;
import com.mi.game.defines.ModuleNames;
import com.mi.game.defines.SysConstants;
import com.mi.game.module.base.BaseModule;
import com.mi.game.module.drop.DropModule;
import com.mi.game.module.dungeon.DungeonModule;
import com.mi.game.module.dungeon.pojo.DungeonEliteEntity;
import com.mi.game.module.farm.dao.FarmEntityDAO;
import com.mi.game.module.farm.data.FarmData;
import com.mi.game.module.farm.pojo.FarmEntity;
import com.mi.game.module.farm.protocol.FarmProtocol;
import com.mi.game.module.login.LoginModule;
import com.mi.game.module.login.pojo.PlayerEntity;
import com.mi.game.module.mainTask.MainTaskModule;
import com.mi.game.module.reward.RewardModule;
import com.mi.game.module.reward.data.GoodsBean;
import com.mi.game.util.Utilities;
@Module(name = ModuleNames.FarmModule,clazz = FarmModule.class)
public class FarmModule extends BaseModule{
	private final Map<Integer,Map<String,Integer>> farmDataMap = new HashMap<Integer, Map<String,Integer>>();
	private FarmEntityDAO farmEntityDAO = FarmEntityDAO.getInstance();
	@Override
	public void init (){
		initFarmData();
	}
	
	private  void initFarmData(){
		List<FarmData> dataList = TemplateManager.getTemplateList(FarmData.class);
		for(FarmData data : dataList){
			int stageID = data.getStageID();
			int coinReward = data.getCoinReward();
			int soulReward = data.getSoulReward();
			Map<String,Integer> map = new HashMap<>();
			map.put("coin", coinReward);
			map.put("soul",soulReward);
			map.put("dropID", data.getDropList());
			map.put("doublePrice",data.getDoublePrice());
			map.put("doubleVipLimit", data.getDoubleVipLimit());
			map.put("triplePrice",data.getTriplePrice());
			map.put("tripleVipLimit",data.getTripleVipLimit());
			farmDataMap.put(stageID,map);
		}
	}
	
	public FarmEntity getUpdateEntity(String playerID){
		FarmEntity farmEntity = this.getEntity(playerID);
		this.settleFarm(farmEntity);
		return farmEntity;
	}
	
	public FarmEntity getEntity(String playerID){
		FarmEntity farmEntity = farmEntityDAO.getEntity(playerID);
		if(farmEntity == null){
			farmEntity = new FarmEntity();
			farmEntity.setKey(playerID);
		}
		return farmEntity;
	}
	
	public void saveEntity(FarmEntity farmEntity){
		farmEntityDAO.save(farmEntity);
	}
	
	/**
	 * 更新挂机状态
	 * */
	
	public  void settleFarm(FarmEntity farmEntity){
		long nowTime = System.currentTimeMillis();
		long endTime = farmEntity.getEndTime();
		if(endTime <= nowTime && endTime != 0){
			int farmMinute = (int)(farmEntity.getFarmTime() / DateTimeUtil.ONE_MINUTE_TIME_MS);
			this.addFarmReward(farmEntity,farmMinute);
			long farmedTime = farmEntity.getFarmTime();
			farmEntity.setEndTime(0);
			farmEntity.setFarmTime(0);
			farmEntity.setFarmedTime(farmedTime);
			this.saveEntity(farmEntity);
		}
	}
	
	/**
	 * 增加挂机奖励
	 * */
	
	private void addFarmReward(FarmEntity farmEntity,int farmMinute){
		int stageID = farmEntity.getStageID();
		Map<String,Integer> map = farmDataMap.get(stageID);
		Map<String,Integer> dataMap = new HashMap<>();
		if(map != null && !map.isEmpty()){
			dataMap.putAll(map);
			int coin = dataMap.get("coin");
			int soul = dataMap.get("soul");
			int dropID = dataMap.get("dropID");
			int hour = farmMinute / 60 ;
			Map<Integer,GoodsBean> baseList = farmEntity.getBaseList();
			Map<Integer,GoodsBean> splList = farmEntity.getSplList();
			List<GoodsBean> baseDropList = new ArrayList<>();
			List<GoodsBean> splDropList = new ArrayList<>();
			if(hour > 0){
				for(int i = 1; i <= hour ; i ++){
					GoodsBean goodsBean = DropModule.doDrop(dropID);
					splDropList.add(goodsBean);
				}
			}
			baseDropList.add(new GoodsBean(KindIDs.SILVERTYPE,coin * farmMinute));
			baseDropList.add(new GoodsBean(KindIDs.HEROSOUL,soul * farmMinute));
			for(GoodsBean temp : splDropList){
				int pid = temp.getPid();
				if(pid != SysConstants.emptyPid){
					GoodsBean saveBean = splList.get(pid);
					if(saveBean != null){
						saveBean.setNum(temp.getNum() + saveBean.getNum());
					}else{
						splList.put(pid,temp);
					}
				}
			}
			for(GoodsBean temp : baseDropList){
				int pid = temp.getPid();
				if(pid != SysConstants.emptyPid){
					GoodsBean saveBean = baseList.get(pid);
					if(saveBean != null){
						saveBean.setNum(temp.getNum() + saveBean.getNum());
					}else{
						baseList.put(pid,temp);
					}
				}
			}
			
		}
	}
	
	/**
	 * 根据vip等级得到挂机时间
	 * */
	private long  getFramTime(int vipLevel){
		long farmTime = 0;
		if(vipLevel < 6){
			farmTime = 8 * DateTimeUtil.ONE_HOUR_TIME_MS;
			//farmTime = 10 * DateTimeUtil.ONE_MINUTE_TIME_MS;
		}else
		if(vipLevel > 5 && vipLevel < 9){
			farmTime = 12 * DateTimeUtil.ONE_HOUR_TIME_MS;
		}else{
			farmTime = 24 * DateTimeUtil.ONE_HOUR_TIME_MS;
		}
		return farmTime;
	}
	
	/**
	 * 开始挂机
	 * */
	
	public void startFarm(String playerID, int stageID,FarmProtocol protocol){
		FarmEntity farmEntity = this.getEntity(playerID);
		Map<Integer,GoodsBean> goodsList = farmEntity.getBaseList();
		if(!goodsList.isEmpty()){
			logger.error("奖励未领取,不可挂机");
			throw new IllegalArgumentException(ErrorIds.rewardNotGet+"");
		}
		this.settleFarm(farmEntity);
		long endTime = farmEntity.getEndTime();
		if(endTime == 0){
			DungeonModule dungeonModule = ModuleManager.getModule(ModuleNames.DungeonModule,DungeonModule.class);
			DungeonEliteEntity dungeonEliteEntity = dungeonModule.getDungeonEliteEntity(playerID);
			List<Integer> eliteList = dungeonEliteEntity.getEliteList();
			if(! eliteList.contains(stageID)){
				logger.error("关卡未开启");
				throw new IllegalArgumentException(ErrorIds.DungeonLocked + "");
			}
			LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule,LoginModule.class);
			PlayerEntity playerEntity = loginModule.getPlayerEntity(playerID);
			int vipLevel = playerEntity.getVipLevel();
			long farmTime = this.getFramTime(vipLevel);
			endTime =  System.currentTimeMillis() + farmTime;
			farmEntity.setEndTime(endTime);
			farmEntity.setFarmTime(farmTime);
			farmEntity.setStageID(stageID);
			this.saveEntity(farmEntity);
			protocol.setFarmEntity(farmEntity);
		}else{
			logger.error("已经在挂机中");
			throw new IllegalArgumentException(ErrorIds.NoStartFarm + "");
		}
	}
	
	/**
	 * 取消挂机
	 * */
	public void cancelFarm(String playerID,FarmProtocol protocol){
		FarmEntity farmEntity = this.getEntity(playerID);
		this.settleFarm(farmEntity);
		long endTime = farmEntity.getEndTime();
		long nowTime = System.currentTimeMillis();
		if(endTime > nowTime){
			long farmTime = nowTime - endTime + farmEntity.getFarmTime();
			int farmMinute = (int) (farmTime/DateTimeUtil.ONE_MINUTE_TIME_MS);
			if(farmMinute >= 1){
				this.addFarmReward(farmEntity, farmMinute);
			}
			farmEntity.setFarmedTime(farmTime);
			farmEntity.setEndTime(0);
			this.saveEntity(farmEntity);
			protocol.setFarmEntity(farmEntity);
		}else{
			logger.error("不在挂机状态");
			throw new IllegalArgumentException(ErrorIds.NoInFarmState +"");
		}
	}
	
	/**
	 * 领取挂机奖励
	 * */
	public void getFarmRewad(String playerID,int getType,IOMessage ioMessage,FarmProtocol protocol){
		FarmEntity farmEntity = this.getEntity(playerID);
		this.settleFarm(farmEntity);
		int multiple = 1;
		int hour = 0;
		Map<String,Object> itemMap = new HashMap<>();
		if(getType > 0){
			int stageID = farmEntity.getStageID();
			Map<String,Integer> map = farmDataMap.get(stageID);
			LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule,LoginModule.class);
			PlayerEntity playerEntity = loginModule.getPlayerEntity(playerID);
			int vipLevel = playerEntity.getVipLevel();
			int vipLimit = 0;
			int hourGold = 0;
			switch(getType){
				case FarmGetType.DOUBLEPRICE :
					vipLimit = map.get("doubleVipLimit");
					hourGold = map.get("doublePrice");
					multiple = 2;
					break;
				case FarmGetType.TRIPLEPRICE:
					vipLimit = map.get("tripleVipLimit");
					hourGold = map.get("triplePrice");
					multiple = 3;
					break;
				default:
					logger.error("翻倍类型错误");
					throw new IllegalArgumentException(ErrorIds.ParamWrong + "");
			}
			if(vipLimit > vipLevel){
				logger.error("挂机翻倍vip等级不足");
				throw new IllegalArgumentException(ErrorIds.FARMVIPLEVELNOENOUGH + "");
			}
			long farmedTime = farmEntity.getFarmedTime();
			hour = (int)Math.ceil((double)farmedTime/DateTimeUtil.ONE_HOUR_TIME_MS);
			int gold = hour * hourGold;
			RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule,RewardModule.class);
			int code = rewardModule.useGoods(playerID, KindIDs.GOLDTYPE, gold, 0, true, null, itemMap, ioMessage);
			if(code != 0){
				protocol.setCode(code);
				return;
			}
		}
	
		Map<Integer,GoodsBean> baseMap = farmEntity.getBaseList();
		Map<Integer,GoodsBean> splMap = farmEntity.getSplList();
		if(multiple > 1){
			for(Entry<Integer,GoodsBean> entry : baseMap.entrySet()){
				GoodsBean goodsBean = entry.getValue();
				goodsBean.setNum(goodsBean.getNum() * multiple);
			}
		}
		if(!baseMap.isEmpty()){
			List<GoodsBean> baseList = Utilities.getGoodList(baseMap);
			List<GoodsBean> splList = Utilities.getGoodList(splMap);
			RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule,RewardModule.class);
			Map<String,GoodsBean> showMap = new HashMap<>();
			rewardModule.addGoods(playerID, baseList, true, showMap, itemMap, ioMessage);
			rewardModule.addGoods(playerID, splList, true, showMap, itemMap, ioMessage);
			farmEntity.setBaseList(new HashMap<Integer,GoodsBean>());
			farmEntity.setSplList(new HashMap<Integer,GoodsBean>());
			farmEntity.setFarmedTime(0);
			farmEntity.setStageID(0);
			farmEntity.setFarmTime(0);
			protocol.setItemMap(itemMap);
			protocol.setShowMap(showMap);
			protocol.setFarmEntity(farmEntity);
			this.saveEntity(farmEntity);
			MainTaskModule mainTaskModule = ModuleManager.getModule(ModuleNames.MainTaskModule,MainTaskModule.class);
			mainTaskModule.updateTaskByActionType(playerID, ActionType.GETFARMTIMES, 0,ioMessage);
		}else{
			logger.error("没有挂机奖励可以领取");
			throw new IllegalArgumentException(ErrorIds.NoFarmReward + "");
		}
	}
	
}
