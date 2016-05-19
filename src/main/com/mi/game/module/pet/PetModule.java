package com.mi.game.module.pet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.mi.core.dao.KeyGeneratorDAO;
import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.TemplateManager;
import com.mi.core.engine.annotation.Module;
import com.mi.core.pojo.KeyGenerator;
import com.mi.game.defines.ErrorIds;
import com.mi.game.defines.KindIDs;
import com.mi.game.defines.ModuleNames;
import com.mi.game.defines.PetSkillLearnCode;
import com.mi.game.defines.SysConstants;
import com.mi.game.module.analyse.AnalyseModule;
import com.mi.game.module.bag.data.BagData;
import com.mi.game.module.base.BaseModule;
import com.mi.game.module.pet.dao.PetDAO;
import com.mi.game.module.pet.data.PetData;
import com.mi.game.module.pet.data.PetLevelupData;
import com.mi.game.module.pet.data.PetLockSkillData;
import com.mi.game.module.pet.data.PetRankupData;
import com.mi.game.module.pet.data.PetShardData;
import com.mi.game.module.pet.data.PetSkillData;
import com.mi.game.module.pet.data.PetSpecialSkillData;
import com.mi.game.module.pet.data.PetTalentData;
import com.mi.game.module.pet.pojo.Pet;
import com.mi.game.module.pet.pojo.PetEntity;
import com.mi.game.module.pet.pojo.PetShard;
import com.mi.game.module.pet.pojo.PetSkill;
import com.mi.game.module.pet.protocol.PetInfoProtocol;
import com.mi.game.module.reward.RewardModule;
import com.mi.game.module.reward.data.GoodsBean;
import com.mi.game.util.Utilities;

/**
 * @author 宋雷 宠物模块 2015年3月27日 下午4:46:07
 */
@Module(name = ModuleNames.PetModule, clazz = PetModule.class)
public class PetModule extends BaseModule {
	private PetDAO petDAO = PetDAO.getInstance();
	private KeyGeneratorDAO keyGeneratorDAO = KeyGeneratorDAO.getInstance();
	private final Map<Integer, Integer> expListData = new HashMap<Integer, Integer>();
	private final Map<Integer, PetRankupData> rankupListData = new HashMap<Integer, PetRankupData>();
	private final Map<Integer, Map<Integer, Integer>> petLockSkillData = new HashMap<Integer, Map<Integer, Integer>>();
	@Override
	public void init() {
		initPetID();
		initExpData();
		initRankupData();
		initPetLockSkillData();
	}

	private void initExpData() {
		List<PetLevelupData> expList = TemplateManager.getTemplateList(PetLevelupData.class);
		for (PetLevelupData data : expList) {
			int exp = data.getExp();
			int level = data.getLevel();
			expListData.put(level, exp);
		}
	}
	
	private void initRankupData() {
		List<PetRankupData> expList = TemplateManager.getTemplateList(PetRankupData.class);
		for (PetRankupData data : expList) {			
			int level = data.getLevel();
			rankupListData.put(level, data);
		}
	}
	
	private void initPetLockSkillData(){
		List<PetLockSkillData> petLockSkillList = TemplateManager.getTemplateList(PetLockSkillData.class);
		for (PetLockSkillData data : petLockSkillList) {
			int lock = data.getLocks();
			Map<Integer, Integer> priceMap = data.getPrice();
			petLockSkillData.put(lock, priceMap);
		}
	}
	
	/**
	 * 保存宠物实体
	 * */
	public void saveEntity(PetEntity petEntity) {
		this.petDAO.save(petEntity);
	}

	/**
	 * 获取宠物实体
	 * */
	public PetEntity getEntity(String playerID) {
		PetEntity petEntity = this.petDAO.getEntity(playerID);
		if (petEntity == null) {
			petEntity = this.initEntity(playerID);
			this.saveEntity(petEntity);
		}		
		return petEntity;
	}
	
	public PetEntity getEntity(String playerID,IOMessage ioMessage) {
		PetEntity petEntity = null;
		if(ioMessage != null){
			String key = PetEntity.class.getName();
			petEntity = (PetEntity) ioMessage.getInputParse().get(key);
			if(petEntity == null){
				petEntity = this.getEntity(playerID);
				ioMessage.getInputParse().put(key, petEntity);
			}
		}else{
			petEntity = this.getEntity(playerID);
		}
		return petEntity;
	}

	/**
	 * 初始化宠物ID
	 * */
	private void initPetID() {
		String clsName = SysConstants.petIDEntity;
		KeyGenerator keyGenerator = keyGeneratorDAO.getEntity(clsName);
		if (keyGenerator == null) {
			keyGenerator = new KeyGenerator();
			keyGenerator.setKey(clsName);
			keyGenerator.setNextId(SysConstants.petStatID);
			keyGeneratorDAO.save(keyGenerator);
		}
	}

	/**
	 * 获取宠物唯一ID
	 * */
	private long getPetID() {
		String clsName = SysConstants.petIDEntity;
		long petID = keyGeneratorDAO.updateInc(clsName);
		return petID;
	}

	/**
	 * 初始化宠物实体
	 * */
	public PetEntity initEntity(String playerID) {
		Map<Long, Pet> petMap = new HashMap<Long, Pet>();
		Map<Integer, PetShard> shardMap = this.initPetShard();
		PetEntity petEntity = new PetEntity();
		petEntity.setPetMap(petMap);
		petEntity.setShardMap(shardMap);
		petEntity.setKey(playerID);
		return petEntity;
	}

	/**
	 * 初始化宠物碎片
	 * */
	private Map<Integer, PetShard> initPetShard() {
		List<PetShardData> shardDataList = TemplateManager.getTemplateList(PetShardData.class);
		Map<Integer, PetShard> shardList = new HashMap<Integer, PetShard>();
		for (PetShardData data : shardDataList) {
			PetShard shard = new PetShard();
			shard.setShardID(data.getPid());
			shard.setNum(0);
			shardList.put(data.getPid(), shard);
		}
		return shardList;
	}


	/**
	 * 初始化宠物
	 * */
	private Pet initPet(PetData petData) {
		Pet pet = new Pet();
		int skillSlots = petData.getSkillSlots();
		int pid = petData.getPid();
		List<PetSkill> skillList = this.initSkillList(skillSlots);
		int skillPoint = petData.getInitialPrint();
		int specialSkillID = petData.getPetSpecialSkillID();
		pet.setSkillList(skillList);
		pet.setSkillPoint(skillPoint);
		pet.setSpecialSkillID(specialSkillID);
		pet.setTemplateID(pid);
		pet.setPetID(this.getPetID());
		pet.setResetPoint(skillPoint);
		return pet;
	}

	/**
	 * 初始化宠物技能列表
	 * */
	private List<PetSkill> initSkillList(int skillLimit) {
		List<PetSkill> skillList = new ArrayList<PetSkill>();
		for (int i = 1; i <= skillLimit; i++) {
			PetSkill petSkill = new PetSkill();
			petSkill.setPostion(i);
			petSkill.setSellLock(true);
			skillList.add(petSkill);
		}
		return skillList;
	}
	
	/**
	 * 保存宠物碎片
	 * @param playerID
	 * @param pid
	 * @param num
	 * @param isSave
	 * @param ioMessage
	 * @return
	 */
	public void addPetShard(String playerID,List<PetShard> petShardList, int pid, int num, boolean isSave, IOMessage ioMessage) {		
		PetEntity entity = this.getEntity(playerID, ioMessage);
		Map<Integer, PetShard> list = entity.getShardMap();
		PetShard shard = list.get(pid);
		if (shard == null) {
			shard = new PetShard();
			shard.setShardID(pid);
			shard.setNum(num);
			list.put(pid, shard);
		} else {
			shard.setNum(shard.getNum() + num);
		}
		
		if (isSave) {
			this.saveEntity(entity);
		}
		
		boolean flag=false;
		for(PetShard petShard:petShardList){
			if(petShard.getShardID()==pid){
				petShard.setNum(petShard.getNum()+num);
				flag=true;
			}
		}

        if(!flag){
        	petShardList.add(shard);
        }		
	}
	
	/**
	 * 宠物进阶
	 * */
	public void petAdvanced(String playerID, long petID,int shardID, PetInfoProtocol protocol) {
		PetEntity petEntity = this.getEntity(playerID);
		Map<Integer, PetShard> shardMap = petEntity.getShardMap();
		PetShard petShard = shardMap.get(shardID);
		if (petShard == null) {
			logger.error("碎片不存在");
			protocol.setCode(ErrorIds.PetShardNotFound);
			return;
		}
		PetShardData data = TemplateManager.getTemplateData(shardID, PetShardData.class);
		
		Map<Long, Pet> petMap = petEntity.getPetMap();
		Pet pet = petMap.get(petID);//需要进阶的宠物
		
		int targetID = data.getTargetID();		
		if (targetID!=pet.getTemplateID()) {
			logger.error("进阶宠物与宠物碎片不是同一种");
			protocol.setCode(ErrorIds.PET_IS_SHARD);
			return;				
		}
		
        if(pet.getRank()==rankupListData.size()){
        	logger.error("已达到最高阶");
			protocol.setCode(ErrorIds.PET_IS_MAXADV);
			return;
		}
        PetRankupData petRankupData=rankupListData.get(pet.getRank()+1);
		int composeNum=petRankupData.getExp();
		int nowNum = petShard.getNum() - composeNum;
		if (nowNum < 0) {
			logger.error("碎片不足");
			protocol.setCode(ErrorIds.PetSHardNotEnough);
			return;
		}
		

		petShard.setNum(nowNum);
		pet.setSkillPoint(pet.getSkillPoint()+petRankupData.getEarnSkill());
		pet.setResetPoint(pet.getResetPoint()+petRankupData.getEarnSkill());
		pet.setRank(pet.getRank()+1);
		openPetTalent(pet);//开启宠物天赋技能
		protocol.setPet(pet);
		protocol.setPetShard(petShard);
		this.saveEntity(petEntity);
	}

	
	/**
	 * 宠物进阶、采用递归、只要碎片足够，可多次进阶
	 * @param pet
	 * @param petShard
	 */
//	private void petAdvanced(Pet pet,PetShard petShard){
//		int composeNum=rankupListData.get(pet.getRank()+1);
//		int nowNum = petShard.getNum() - composeNum;
//		if(nowNum>=0){
//			petShard.setNum(nowNum);
//			pet.setRank(pet.getRank()+1);
//			openPetTalent(pet);//开启宠物天赋技能
//			petAdvanced(pet,petShard);
//		}
//	}

	/**
	 * 宠物碎片兑换
	 * */
	public void compoundPet(String playerID, int shardID, IOMessage ioMessage, PetInfoProtocol protocol) {
		PetEntity petEntity = this.getEntity(playerID);
		Map<Integer, PetShard> shardMap = petEntity.getShardMap();
		PetShard petShard = shardMap.get(shardID);
		if (petShard == null) {
			logger.error("碎片不存在");
			protocol.setCode(ErrorIds.PetShardNotFound);
			return;
		}
		PetShardData data = TemplateManager.getTemplateData(shardID, PetShardData.class);
		int composeNum = data.getComposeNum();
		Map<Long, Pet> petMap = petEntity.getPetMap();		

		int nowNum = petShard.getNum() - composeNum;
		if (nowNum < 0) {
			logger.error("碎片不足");
			protocol.setCode(ErrorIds.PetSHardNotEnough);
			return;
		}
		
		int targetID = data.getTargetID();
		for (Entry<Long, Pet> entry : petMap.entrySet()) {
			Pet tempPet = entry.getValue();
			if (targetID==tempPet.getTemplateID()) {
				logger.error("宠物已存在");
				protocol.setCode(ErrorIds.PETEXIST);
				return;				
			}
		}
		
		petShard.setNum(nowNum);
		
		PetData petData = TemplateManager.getTemplateData(targetID, PetData.class);
		Pet pet = this.initPet(petData);
		long petID = pet.getPetID();
		petMap.put(petID, pet);
		protocol.setPet(pet);
		protocol.setPetShard(petShard);
		this.saveEntity(petEntity);
	}
	
	
	

	/**
	 * 宠物出战
	 * */
	public void petWork(String playerID, long petID, PetInfoProtocol protocol) {
		PetEntity petEntity = this.getEntity(playerID);
		Map<Long, Pet> petMap = petEntity.getPetMap();
		Pet pet = petMap.get(petID);
		if (pet == null) {
			logger.error("宠物未找到");
			protocol.setCode(ErrorIds.PetNotFound);
			return;
		}
		if (pet.isWorked()) {
			logger.error("宠物已出战");
			protocol.setCode(ErrorIds.PetWorked);
			return;
		}
		long nowTime = System.currentTimeMillis();
		for (Entry<Long, Pet> entry : petMap.entrySet()) {
			Pet tempPet = entry.getValue();
			if (tempPet.isWorked()) {
				tempPet.setWorked(false);
				protocol.setUnWorkPet(tempPet.getPetID());
			}
		}
		
		if(pet.getSpecialSkillTime()==0){
			pet.setSpecialSkillTime(nowTime);
		}
		pet.setWorked(true);
		this.saveEntity(petEntity);
		protocol.setSpecialSkillTime(pet.getSpecialSkillTime());
		protocol.setWorkPet(pet.getPetID());
	}

	/***
	 * 出战的宠物的定时经验计算、每10秒获得一次经验
	 */
	public Pet petTimeExp(String playerID) {
		PetEntity petEntity=this.getEntity(playerID);
		Map<Long, Pet> petMap = petEntity.getPetMap();		
		for (Entry<Long, Pet> entry : petMap.entrySet()) {
			Pet tempPet = entry.getValue();
			if (tempPet.isWorked()) {				
				if(!petIsMaxExp(tempPet)){
					long expTime=tempPet.getExpTime();
					if(expTime==0){
						addPetExp(tempPet,1);
						tempPet.setExpTime(System.currentTimeMillis()/1000);
					}else{						
						if((System.currentTimeMillis()/1000-expTime)>=10){
							int exp=(int) ((System.currentTimeMillis()/1000-expTime)/10);
							addPetExp(tempPet,exp);
							tempPet.setExpTime(System.currentTimeMillis()/1000);
						}else{
							return null;
						}
					}					
					this.saveEntity(petEntity);
					return tempPet;
				}								
			}
		}				
		return null;
	}			
	
	/**
	 * 宠物喂养
	 * @param playerID
	 * @param useType
	 * @param petID
	 * @param goodsList
	 * @param protocol
	 * @param ioMessage
	 */
	public void feedPet(String playerID,int useType,long petID,List<GoodsBean> goodsList,PetInfoProtocol protocol,IOMessage ioMessage){
		PetEntity petEntity = this.getEntity(playerID);
		Map<Long, Pet> petMap = petEntity.getPetMap();
		Pet pet = petMap.get(petID);
		if (pet == null) {
			logger.error("宠物未找到");
			protocol.setCode(ErrorIds.PetNotFound);
			return;
		}
		if(petIsMaxExp(pet)){
			logger.error("宠物已满级");	
			protocol.setCode(ErrorIds.PET_EXP_EMP);
			return;
		}
		
		Map<String,GoodsBean> showMap = new HashMap<>();
		Map<String, Object> itemMap = new HashMap<String, Object>();
		RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule,RewardModule.class);
		int exp=0;
		if(useType==0){			
			rewardModule.useGoods(playerID, goodsList, 0, true, showMap, itemMap, ioMessage);
			for(GoodsBean goodsBean:goodsList){
				BagData bagData = (BagData) TemplateManager.getTemplateData(goodsBean.getPid());
				Map<Integer, Integer> useFunc = bagData.getUseFunc();
				for (Entry<Integer, Integer> entry : useFunc.entrySet()) {					
					int value = entry.getValue();
					exp+=value*goodsBean.getNum();
				}
			}			
		}else{
			//如果宠物没有满级，判断满级需要的经验
			long needExp=petNeedExpMax(pet);
			for(GoodsBean goodsBean:goodsList){
				int maxExp=0;
				BagData bagData = (BagData) TemplateManager.getTemplateData(goodsBean.getPid());
				Map<Integer, Integer> useFunc = bagData.getUseFunc();
				int value=0;				
				for (Entry<Integer, Integer> entry : useFunc.entrySet()) {					
					value = entry.getValue();					
					maxExp+=value*goodsBean.getNum();
				}
				
				List<GoodsBean> newGoodsList=new ArrayList<GoodsBean>();								
				if(maxExp>=needExp){
					int needNum=0;
					if((needExp%value)==0){
						needNum=(int) (needExp/value);//需要的数量
					}else{
						needNum=(int) (needExp/value+1);//需要的数量
					}
					
					GoodsBean needGoodsBean=new GoodsBean(goodsBean.getPid(),needNum);
					newGoodsList.add(needGoodsBean);
					rewardModule.useGoods(playerID, newGoodsList, 0, true, showMap, itemMap, ioMessage);
					exp+=needNum*value;
					break;
				}else{					
					newGoodsList.add(goodsBean);
					rewardModule.useGoods(playerID, newGoodsList, 0, true, showMap, itemMap, ioMessage);
					needExp-=maxExp;
					exp+=maxExp;
				}
			}															
		}
		
		
        this.addPetExp(pet, exp);								
		this.saveEntity(petEntity);	
		protocol.setItemMap(itemMap);
		protocol.setPet(pet);
		
	}
	
	public boolean isPetMaxExp(String playerID,long petID,PetInfoProtocol protocol){
		PetEntity petEntity = this.getEntity(playerID);
		Map<Long, Pet> petMap = petEntity.getPetMap();
		Pet pet = petMap.get(petID);
		if (pet == null) {
			logger.error("宠物未找到");
			protocol.setCode(ErrorIds.PetNotFound);
			throw new IllegalArgumentException(ErrorIds.PetNotFound + "");
		}
		if(petIsMaxExp(pet)){
			protocol.setCode(ErrorIds.PET_EXP_EMP);
			return true;
		}
		return false;
	}
	
	/**
	 * 宠物满级需要的经验
	 * @param pet
	 * @return
	 */
	private long petNeedExpMax(Pet pet){
		long exp=0;
		long nowExp = pet.getExp();		
		int allExp=exp(pet);
		long needExp=allExp-nowExp;//升级到下一级需要用到的经验
		int max=expListData.size();//最大级数
		exp+=needExp;
		for(int i=pet.getLevel()+1;i<=max;i++){
			exp+=expListData.get(i);
		}
		return exp;
	}

	/**
	 * 增加宠物经验
	 * 
	 * */
	private int addPetExp(Pet pet, long exp){	
												
		long nowExp = pet.getExp();		
		int allExp=exp(pet);
		long needExp=allExp-nowExp;//升级到下一级需要用到的经验
		int max=expListData.size();//最大级数
		if(exp>=needExp){
			if(pet.getLevel()<max){
				pet.setLevel(pet.getLevel()+1);
				pet.setExp(allExp);			
				pet.setSkillPoint(pet.getSkillPoint()+1);
				pet.setResetPoint(pet.getResetPoint()+1);
				addPetExp(pet,exp-needExp);
			}else{
				pet.setExp(allExp);
				return 0;
			}			
		}else{
			pet.setExp(nowExp+exp);
		}
		return allExp;
	}
	
	/**
	 * 计算宠物从一级到现在级数的所有经验值
	 * @param pet
	 * @return
	 */
	private int exp(Pet pet){
		int exp=0;		
		for(int i=1;i<=pet.getLevel();i++){			
			int needExp=expListData.get(i);//升级到下一级需要用到的经验
			exp+=needExp;
		}		
		return exp;
	}

	/**
	 * 开启宠物的天赋属性，领悟技能时调用该方法
	 * @param pet
	 */
	private void openPetTalentToSkill(Pet pet,PetSkill petSkill,List<PetSkill> petSkillList){
		PetData petData = TemplateManager.getTemplateData(pet.getTemplateID(), PetData.class);
		Map<Integer,Integer> map=petData.getPetTalent();
		boolean flag=false;
		for (Entry<Integer, Integer> entry : map.entrySet()) {	
			PetTalentData petTalentData = TemplateManager.getTemplateData(entry.getKey(), PetTalentData.class);
			if(petTalentData.getSkillReq()==petSkill.getSkillID()){//判断该技能是否是开启该宠物天赋技能的普通技能				
				int index=map.get(entry.getKey());//开启宠物的天赋属性需要的阶数
				if(pet.getRank()>=index){//判断是否到开启天赋技能的级数										
					List<PetSkill> skillList=pet.getSkillList();
					pet.setTalentNum(pet.getTalentNum()+petTalentData.getAddition());
					flag=true;
					for(PetSkill skill:skillList){						
						if(skill.getSkillID()!=0 && skill.getSkillID() != petSkill.getSkillID()){	
							PetSkillData petSkillData = TemplateManager.getTemplateData(skill.getSkillID(), PetSkillData.class);
							Map<Integer,Integer> petSkillDataMap=petSkillData.getIntensify();
							skill.setLevel(skill.getLevel()+petTalentData.getAddition());//技能等级+1
							skill.setLevelNum(skill.getLevelNum()+petTalentData.getAddition());
							addPrototype(skill,petSkillDataMap);//技能属性增加	
							petSkillList.add(skill);
						}else if(skill.getSkillID()!=0 && skill.getSkillID() == petSkill.getSkillID()){
							PetSkillData petSkillData = TemplateManager.getTemplateData(skill.getSkillID(), PetSkillData.class);
							Map<Integer,Integer> petSkillDataMap=petSkillData.getIntensify();
							for(int i=1;i<=pet.getTalentNum();i++){
								skill.setLevel(skill.getLevel()+1);//技能等级+1
								skill.setLevelNum(skill.getLevelNum()+1);
								addPrototype(skill,petSkillDataMap);//技能属性增加									
							}
							petSkillList.add(skill);
						}				
					}
				}
			}
		}	
		
		if(!flag){//如果不是开启该宠物天赋技能的普通技能，那么将该技能升级到已经开启了天赋技能的次数
			if(pet.getTalentNum() > 0){
				PetSkillData petSkillData = TemplateManager.getTemplateData(petSkill.getSkillID(), PetSkillData.class);
				Map<Integer,Integer> petSkillDataMap=petSkillData.getIntensify();
				for(int i=1;i<=pet.getTalentNum();i++){						
					petSkill.setLevel(petSkill.getLevel()+1);//技能等级+1
					petSkill.setLevelNum(petSkill.getLevelNum()+1);
					addPrototype(petSkill,petSkillDataMap);//技能属性增加							
				}				
				petSkillList.add(petSkill);
			}
		}
	}
	
	/**
	 * 开启宠物的天赋属性，宠物进阶时调用该方法
	 * @param pet
	 */
	private void openPetTalent(Pet pet){
		PetData petData = TemplateManager.getTemplateData(pet.getTemplateID(), PetData.class);
		Map<Integer,Integer> map=petData.getPetTalent();//获取宠物的所有天赋技能
		for (Entry<Integer, Integer> entry : map.entrySet()) {				
			if(pet.getRank()==entry.getValue()){//判断是否达到开启天赋技能的阶段
				PetTalentData petTalentData = TemplateManager.getTemplateData(entry.getKey(), PetTalentData.class);
				for(PetSkill skill:pet.getSkillList()){
					if(skill.getSkillID()!=0&&skill.getSkillID()==petTalentData.getSkillReq()){	
						pet.setTalentNum(pet.getTalentNum()+petTalentData.getAddition());
						for(PetSkill skill1:pet.getSkillList()){
							if(skill1.getSkillID()!=0){
								PetSkillData petSkillData = TemplateManager.getTemplateData(skill1.getSkillID(), PetSkillData.class);
								Map<Integer,Integer> petSkillDataMap=petSkillData.getIntensify();
								skill1.setLevel(skill1.getLevel()+petTalentData.getAddition());//技能等级+1
								addPrototype(skill1,petSkillDataMap);//技能属性增加							
							}				
						}
					}				
				}
			}			
		}
	}

	/**
	 * 传承
	 * */
	public void eatPet(String playerID, long petID, long eatID, PetInfoProtocol protocol) {
		PetEntity petEntity = this.getEntity(playerID);
		Map<Long, Pet> petMap = petEntity.getPetMap();
		if (petID == eatID) {
			logger.error("不可吞噬自身");
			protocol.setCode(ErrorIds.NoEatSelf);
			return;
		}				
		Pet pet = petMap.get(petID);
		if (pet == null) {
			logger.error("宠物未找到");
			protocol.setCode(ErrorIds.PetNotFound);
			return;
		}
		Pet eatPet = petMap.get(eatID);
		if (eatPet == null) {
			logger.error("宠物未找到");
			protocol.setCode(ErrorIds.PetNotFound);
			return;
		}		
		
		if(pet.getLevel()>eatPet.getLevel()){
			logger.error("被传承宠物不能比传承宠物等级高");
			protocol.setCode(ErrorIds.PET_EAT_EMP);
			return;
		}
		
		if(petIsMaxExp(pet)){
			logger.error("宠物已满级");
			protocol.setCode(ErrorIds.PET_EXP_EMP);
			return;
		}
		
		Map<String, Object> itemMap = new HashMap<String, Object>();
		RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
		int gold=eatPet.getLevel()*5;
		int code=rewardModule.useGoods(playerID, KindIDs.GOLDTYPE, gold, 0, true, null, itemMap, null);
        if(code!=0){
        	protocol.setCode(code);
        	return;
        }
		// ////
		// // 元宝消耗记录
		// ///
		AnalyseModule analyseModule = ModuleManager.getModule(ModuleNames.AnalyseModule, AnalyseModule.class);
		analyseModule.goldCostLog(playerID, gold, 1, gold, "eatPet", "pet");
		
		
		
		
		PetData petData= TemplateManager.getTemplateData(eatPet.getTemplateID(),PetData.class);
		long eatExp = eatPet.getExp();									
		this.addPetExp(pet, eatExp);			
								
		Pet initPet=this.initPet(petData);
		initPet.setPetID(eatID);
		initPet.setWorked(eatPet.isWorked());
		petMap.put(eatID, initPet);//将吞吃的宠物初始化状态
		this.saveEntity(petEntity);
		
		Map<String,Pet> petHashMap=new HashMap<String,Pet>();
		petHashMap.put(String.valueOf(petID), petMap.get(petID));
		petHashMap.put(String.valueOf(eatID), petMap.get(eatID));	
		protocol.setItemMap(itemMap);
		protocol.setPetMap(petHashMap);
	}
	
	private boolean petIsMaxExp(Pet pet){
		//1.判断宠物是否满级
		int max=expListData.size();//最大级数
		if(pet.getLevel()==max){
			int allExp=exp(pet);
			//2.判断宠物经验是否满
			if(pet.getExp()==allExp){
				return true;
			}
		}		
		return false;
	}
    
	/**
	 * 获取可激活的技能
	 * @param pet
	 * @return
	 */
	private List<PetSkillData> canLearnPetSkill(Pet pet){
		List<PetSkillData> skillDataList = TemplateManager.getTemplateList(PetSkillData.class);
		List<PetSkillData> canLearnSkillDataList=new ArrayList<PetSkillData>();
		List<PetSkill>  skillList=pet.getSkillList();
		if(skillDataList!=null&&skillDataList.size()>0){
			for(int i=0;i<skillDataList.size();i++){
				PetSkillData petSkillData=skillDataList.get(i);
				boolean flag=false;
				PetData petData=TemplateManager.getTemplateData(pet.getTemplateID(), PetData.class);
				if(petSkillData.getQuality()<=petData.getSkillQuality()){
					if(skillList!=null){
						for(int j=0;j<skillList.size();j++){
							PetSkill petSkill=skillList.get(j);						
							if(petSkillData.getPid()==petSkill.getSkillID()){
								flag=true;
								break;
							}
						}
					}
					
					if(!flag){
						canLearnSkillDataList.add(petSkillData);
					}
				}				
			}
		}
		return canLearnSkillDataList;
	}			
	
	public int getRandomList(List<Integer> list) {
		int random = Utilities.getRandomInt(list.size());
		int index=list.get(random);//随机获取技能		
		return index;
	}		
	
	/**
	 * 领悟技能
	 * @param playerID
	 * @param petID
	 * @param protocol
	 */
	public void learnSkill(String playerID, long petID, PetInfoProtocol protocol) {
		PetEntity petEntity = this.getEntity(playerID);
		Map<Long, Pet> petMap = petEntity.getPetMap();
		Pet pet = petMap.get(petID);
		if (pet == null) {
			logger.error("宠物未找到");
			protocol.setCode(ErrorIds.PetNotFound);
			return;
		}
		int skillPoint = pet.getSkillPoint();
		if (skillPoint < 1) {
			logger.error("技能点数不够");
			protocol.setCode(ErrorIds.PetSkillPointNotEnough);
			return;
		}
		int templateID = pet.getTemplateID();
		PetData petData = TemplateManager.getTemplateData(templateID, PetData.class);
		int skillLevelLimit = petData.getSkillLimit()+pet.getTalentNum();//技能最大等级
		List<PetSkill> skillList = pet.getSkillList();//技能集合
		int state = PetSkillLearnCode.failed;	
		int isLock=isLockPetSkill(skillList);//判断解锁几个技能栏
		int isLearnSkill=isLearnSkill(skillList);//判断学了几个技能	
		int isCanlevelUp=countMaxSkillLev(skillList,skillLevelLimit);//可升级的个数
		//1:解锁技能栏
		//2：学习技能
								
		int random=0;
		List<PetSkillData> PetSkillDataList=canLearnPetSkill(pet);
		List<Integer> list=new ArrayList<Integer>();
		if(isLock>0){//技能栏已经解锁
			//判断是否已经学习了技能
			if(isLearnSkill>0){
				//3：技能栏满之后不用解锁
				if(isLock>=skillList.size()){//技能栏全部解锁
					if(isLearnSkill>=skillList.size()){//技能学满不用学习						
						//判断技能是否全部满级
						if(isCanlevelUp==0){
							protocol.setCode(ErrorIds.PET_KILL_EMP);
							return;
						}else{							
							list.add(2);
							list.add(3);							
						}
					}else{
						//判断学到的技能是否已经学满
						if(isCanlevelUp!=0){							
							list.add(2);
						}
						list.add(1);
						list.add(3);						
					}
					random=getRandomList(list);
				}else{
					if(isLock>isLearnSkill){//技能栏有空余						
						if(isCanlevelUp==0){							
							list.add(0);
							list.add(1);
							list.add(3);
							random=getRandomList(list);
						}else{
							random = Utilities.getRandomInt(4);
						}
					}else{
						if(isCanlevelUp!=0){							
							list.add(2);
							
						}
						list.add(0);							
						list.add(3);
						random=getRandomList(list);
					}					
				}				
			}else{
				random = 1;
			}						
		}
		
		switch (random) {
		case 0://解锁技能栏						
			state=unlockPetSkill(skillList,protocol);			    		
			break;
        case 1://学习新的技能       	
        	if(PetSkillDataList!=null&&PetSkillDataList.size()>0){
        		for (PetSkill tempSkill : skillList) {
    				if (tempSkill.getSkillID() == 0 && !tempSkill.isSellLock()) {
		        		int random1 = Utilities.getRandomInt(PetSkillDataList.size());
		        		PetSkillData petSkillData=PetSkillDataList.get(random1);//随机获取技能
		        		Map<Integer,Integer> petSkillDataMap=petSkillData.getProperty();
						addPrototype(tempSkill,petSkillDataMap);//学习新的技能时，将新学到的技能的血、攻、防等属性值保存到数据库
						tempSkill.setSkillID(petSkillData.getPid());
						List<PetSkill> petSkillList=new ArrayList<PetSkill>();
						openPetTalentToSkill(pet,tempSkill,petSkillList);//开启天赋技能
						if(petSkillList.isEmpty()){
							petSkillList.add(tempSkill);
						}
						protocol.setPetSkillList(petSkillList);
						state = PetSkillLearnCode.newSkill;
						break;
    				}
    		    }
        	}       	        	        	        	        	        	        	
			break;
		case 2://技能升级
			List<PetSkill> petSkillList=new ArrayList<PetSkill>();
			for (PetSkill tempSkill : skillList) {
				if (tempSkill.getSkillID() != 0 && !tempSkill.isSellLock()&&tempSkill.getLevel()<skillLevelLimit) {
					petSkillList.add(tempSkill);//哪些技能可以升级
				}
		    }
			if(petSkillList.size()>0){
				int random2 = Utilities.getRandomInt(petSkillList.size());//随机获取技能
				PetSkill petSkill=petSkillList.get(random2);//得到可升级的技能
				petSkill.setLevel(petSkill.getLevel() + 1);
				state = PetSkillLearnCode.levelUp;
				/*技能升级时，攻、血、防等属性等重新赋值*/
				PetSkillData petSkillData = TemplateManager.getTemplateData(petSkill.getSkillID(), PetSkillData.class);
				Map<Integer,Integer> petSkillDataMap=petSkillData.getIntensify();
				addPrototype(petSkill,petSkillDataMap);
				//结束	
				List<PetSkill> newSkillList=new ArrayList<PetSkill>();
				newSkillList.add(petSkill);
				protocol.setPetSkillList(newSkillList);
				break;
			}
			break;		
		}					
		protocol.setState(state);
		skillPoint -= 1;
		pet.setSkillPoint(skillPoint);
		protocol.setSkillPoint(skillPoint);
		protocol.setPetID(petID);
		this.saveEntity(petEntity);
	}	
	
	/**
	 * 解锁技能栏
	 * @param skillList
	 * @param protocol
	 * @return
	 */
	private int unlockPetSkill(List<PetSkill> skillList, PetInfoProtocol protocol){
		int state=PetSkillLearnCode.failed;
		List<PetSkill> petSkillList=new ArrayList<PetSkill>();
		for (PetSkill tempSkill : skillList) {
			if (tempSkill.isSellLock()) {
				tempSkill.setSellLock(false);
				petSkillList.add(tempSkill);
				protocol.setPetSkillList(petSkillList);
				state = PetSkillLearnCode.openSell;
				return state;
			}
		}
		return state;
	}
	
	/**
	 * 判断技能栏是否解锁
	 * @param skillList
	 * @return
	 */
	private int isLockPetSkill(List<PetSkill> skillList){
		int num=0;
		for (PetSkill tempSkill : skillList) {
			if (!tempSkill.isSellLock()) {								
				num++;
			}
		}
		return num;
	}
	
	/**
	 * 学习了多少个技能
	 * @param skillList
	 * @return
	 */
	private int isLearnSkill(List<PetSkill> skillList){
		int num=0;
		for (PetSkill tempSkill : skillList) {
			if (tempSkill.getSkillID() != 0) {
				num++;				
			}
	    }
		return num;
	}
	
	private int countMaxSkillLev(List<PetSkill> skillList,int skillLevelLimit){
		int num=0;		
		for (PetSkill tempSkill : skillList) {
			if (tempSkill.getSkillID() != 0 && !tempSkill.isSellLock()&&tempSkill.getLevel()<skillLevelLimit) {
				num++;
			}
	    }
		return num;
	}
	
	/**
	 * 计算宠物的战斗力
	 * @param pet
	 */
	public double countCombat(String playerID){
		PetEntity petEntity=getEntity(playerID);		
		double combat=0;
		if(petEntity!=null){
			Map<Long,Pet> petMap=petEntity.getPetMap();
			for (Entry<Long, Pet> entry : petMap.entrySet()) {
				Pet tempPet = entry.getValue();
				if (tempPet.isWorked()) {
					List<PetSkill> skillList=tempPet.getSkillList();
					for(PetSkill petSkill:skillList){
						if(petSkill.getSkillID()!=0){
							combat+=petSkill.getAttack()+petSkill.getMdef()+petSkill.getPdef()+petSkill.getHp()*0.2;
						}
					}
				}
			}						
		}						
		return combat;
	}
	
	/*学习新技能时或升级技能时，增加攻、防、血等属性的值*/
	private void addPrototype(PetSkill tempSkill,Map<Integer,Integer> petSkillDataMap){						
		for (Entry<Integer, Integer> entry : petSkillDataMap.entrySet()) {
			int key=entry.getKey();
			int value = entry.getValue();
			switch (key) {
				case SysConstants.Hp://生命
					tempSkill.setHp(tempSkill.getHp()+value);
					break;
                case SysConstants.Attack://攻击
                	tempSkill.setAttack(tempSkill.getAttack()+value);
					break;
			    case SysConstants.PDef://物防
			    	tempSkill.setPdef(tempSkill.getPdef()+value);
					break;
				case SysConstants.MDef://魔防
					tempSkill.setMdef(tempSkill.getMdef()+value);
					break;	
			}
		}
	}		

	/**
	 * 锁定技能
	 * */
	public void lockSkill(String playerID, long petID, int skillID, PetInfoProtocol protocol) {
		PetEntity petEntity = this.getEntity(playerID);
		Map<Long, Pet> petMap = petEntity.getPetMap();
		Pet pet = petMap.get(petID);
		if (pet == null) {
			logger.error("宠物未找到");
			protocol.setCode(ErrorIds.PetNotFound);
			return;
		}
		boolean isFind = false;
		int lockNum = 0;
		List<PetSkill> petSkills = pet.getSkillList();
		PetSkill petSkill = null;
		/*判断技能锁定数量*/
		for (PetSkill tempSkill : petSkills) {
			if (tempSkill.isSkillLock()) {
				lockNum += 1;
			}			
		}
				
		/*锁定或解锁*/
		for (PetSkill tempSkill : petSkills) {			
			if (tempSkill.getSkillID() == skillID) {
				if (tempSkill.isSkillLock()) {
					tempSkill.setSkillLock(false);
				}else{
					tempSkill.setSkillLock(true);	
					PetData petData = TemplateManager.getTemplateData(pet.getTemplateID(), PetData.class);
					int maxLockNum=petData.getLockNum();
					if(lockNum<maxLockNum){												
						Map<Integer,Integer> map=petLockSkillData.get(lockNum+1);
						int gold =map.get(KindIDs.GOLDTYPE);
						Map<String, Object> itemMap = new HashMap<String, Object>();
						RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
						int code=rewardModule.useGoods(playerID, KindIDs.GOLDTYPE, gold, 0, true, null, itemMap, null);
						
						if(code!=0){
				        	protocol.setCode(code);
				        	return;
				        }
						
						/*元宝消耗记录*/					
						AnalyseModule analyseModule = ModuleManager.getModule(ModuleNames.AnalyseModule, AnalyseModule.class);
						analyseModule.goldCostLog(playerID, gold, 1, gold, "lockSkill", "pet");
						protocol.setItemMap(itemMap);
					}else{
						logger.error("宠物技能锁定数量已满");
						protocol.setCode(ErrorIds.PET_SKILL_LOCK_EMP);
						return;
					}					
				}
				petSkill = tempSkill;
				isFind = true;
			}

		}
		if (!isFind) {
			logger.error("未找到宠物技能");
			protocol.setCode(ErrorIds.PetSkillNotFound);
			return;
		}		
		protocol.setPetSkill(petSkill);
		protocol.setPetID(petID);
		this.saveEntity(petEntity);
	}

	/**
	 * 重置技能
	 * */
	public void resetSkill(String playerID, long petID, PetInfoProtocol protocol) {
		PetEntity petEntity = this.getEntity(playerID);
		Map<Long, Pet> petMap = petEntity.getPetMap();
		Pet pet = petMap.get(petID);

		if (pet == null) {
			logger.error("宠物未找到");
			protocol.setCode(ErrorIds.PetNotFound);
			return;
		}
		int skillPoint = pet.getSkillPoint();
		int resetPoint = pet.getResetPoint();
		if (resetPoint == 0) {
			int templateID = pet.getTemplateID();
			PetData petData = TemplateManager.getTemplateData(templateID, PetData.class);
			resetPoint = petData.getInitialPrint();
		}
		if (resetPoint == skillPoint) {
			logger.error("不需要重置");
			protocol.setCode(ErrorIds.NotResetPetSkill);
			return;
		}
		List<PetSkill> skillList = pet.getSkillList();		
		List<PetSkill> lockSkillList=new ArrayList<PetSkill>();
		boolean flag=false;
		for (PetSkill petSkill : skillList) {
			if (!petSkill.isSkillLock()) {
				if (!petSkill.isSellLock()) {
					petSkill.setSellLock(true);
					petSkill.setSkillID(0);
					petSkill.setLevel(0);
					/*重置之后将攻、防、血等属性设置为0*/
					petSkill.setPdef(0);
					petSkill.setAttack(0);
					petSkill.setHp(0);
					petSkill.setMdef(0);
					petSkill.setLevelNum(0);
					flag=true;
				}
			} else {
				if(petSkill.getSkillID()!=0){
					lockSkillList.add(petSkill);
				}				            
			}
		}
		
		if(!lockSkillList.isEmpty()){
			int num=countPatent(pet,lockSkillList);			
			for(PetSkill petSkill : lockSkillList){
				int skillLevel = petSkill.getLevel();
				int count=petSkill.getLevelNum()-num;
				resetPoint -= skillLevel + 2 - petSkill.getLevelNum();					
				if(count>0){
					PetSkillData petSkillData = TemplateManager.getTemplateData(petSkill.getSkillID(), PetSkillData.class);
					Map<Integer,Integer> petSkillDataMap=petSkillData.getIntensify();
					deletePrototype(petSkill,petSkillDataMap,count);
				}
				petSkill.setLevelNum(num);
				petSkill.setLevel(petSkill.getLevel()-count);
			}			
			pet.setTalentNum(num);
		}else{
			pet.setTalentNum(0);
		}
		
		if(!flag){
			logger.error("不需要重置");
			protocol.setCode(ErrorIds.NotResetPetSkill);
			return;
		}
		
		if (resetPoint < 0) {
			logger.error("数据异常");
			protocol.setCode(ErrorIds.ResetPointWrong);
			return;
		}
		Map<String, Object> itemMap = new HashMap<String, Object>();
		RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
		int code=rewardModule.useGoods(playerID, KindIDs.GOLDTYPE, SysConstants.resetPetSkillGold, 0, true, null, itemMap, null);
        if(code!=0){
        	protocol.setCode(code);
        	return;
        }
		// ////
		// // 元宝消耗记录
		// ///
		AnalyseModule analyseModule = ModuleManager.getModule(ModuleNames.AnalyseModule, AnalyseModule.class);
		analyseModule.goldCostLog(playerID, SysConstants.resetPetSkillGold, 1, SysConstants.resetPetSkillGold, "resetSkill", "pet");

		pet.setSkillPoint(resetPoint);
		protocol.setPet(pet);
		protocol.setItemMap(itemMap);
		protocol.setPetID(petID);
		this.saveEntity(petEntity);
	}
		
	private void deletePrototype(PetSkill tempSkill,Map<Integer,Integer> petSkillDataMap,int num){						
		for (Entry<Integer, Integer> entry : petSkillDataMap.entrySet()) {
			int key=entry.getKey();
			int value = entry.getValue()*num;
			switch (key) {
				case SysConstants.Hp://生命
					tempSkill.setHp(tempSkill.getHp()-value);
					break;
                case SysConstants.Attack://攻击
                	tempSkill.setAttack(tempSkill.getAttack()-value);
					break;
			    case SysConstants.PDef://物防
			    	tempSkill.setPdef(tempSkill.getPdef()-value);
					break;
				case SysConstants.MDef://魔防
					tempSkill.setMdef(tempSkill.getMdef()-value);
					break;	
			}
		}
	}
	
	/**
	 * 判断开启天赋技能的数量
	 * @param pet
	 * @param lockSkillList
	 * @return
	 */
	private int countPatent(Pet pet,List<PetSkill> lockSkillList){
		int num=0;
		PetData petData = TemplateManager.getTemplateData(pet.getTemplateID(), PetData.class);
		Map<Integer,Integer> map=petData.getPetTalent();
		for (Entry<Integer, Integer> entry : map.entrySet()) {	
			PetTalentData petTalentData = TemplateManager.getTemplateData(entry.getKey(), PetTalentData.class);
			for(PetSkill petSkill:lockSkillList){
				if(petTalentData.getSkillReq()==petSkill.getSkillID()){//判断该技能是否是开启该宠物天赋技能的普通技能
					int index=map.get(entry.getKey());//开启宠物的天赋属性需要的阶数
					if(pet.getRank()>=index){//判断是否到开启天赋技能的级数
						num+=petTalentData.getAddition();
					}
				}
			}
		}
		return num;
	}	
	

	/**
	 * 领取特殊奖励
	 * */
	public void getSpecialReward(String playerID, long petID, PetInfoProtocol protocol, IOMessage ioMessage) {
		PetEntity petEntity = this.getEntity(playerID);
		Map<Long, Pet> petMap = petEntity.getPetMap();
		Pet pet = petMap.get(petID);
		if (pet == null) {
			logger.error("宠物未找到");
			protocol.setCode(ErrorIds.PetNotFound);
			return;
		}
		int templateID = pet.getTemplateID();
		PetData petData = TemplateManager.getTemplateData(templateID, PetData.class);
		long lastGetTime = pet.getSpecialSkillTime();
		long nowTime = System.currentTimeMillis();
		int specialID = petData.getPetSpecialSkillID();
		PetSpecialSkillData specialSkillData = TemplateManager.getTemplateData(specialID, PetSpecialSkillData.class);
		if (specialSkillData == null) {
			logger.error("不存在该技能");
			protocol.setCode(ErrorIds.NoPetSpecialSkill);
			return;
		}
		List<GoodsBean> goodsList = specialSkillData.getRewardItem();
		long rewardTime = specialSkillData.getRewardTime() * 1000;
		if (lastGetTime + rewardTime <= nowTime) {
			Map<String, Object> itemMap = new HashMap<String, Object>();			
			RewardModule rewardModule = ModuleManager.getModule(ModuleNames.RewardModule, RewardModule.class);
			rewardModule.addGoods(playerID, goodsList, true, null, itemMap, ioMessage);
			protocol.setItemMap(itemMap);
			protocol.setShowMap(goodsList);
		} else {
			logger.error("未到领取时间");
			protocol.setCode(ErrorIds.ArrivalGetItemTime);
			return;
		}
		pet.setSpecialSkillTime(nowTime);
		protocol.setSpecialSkillTime(nowTime);
		protocol.setPetID(petID);
		this.saveEntity(petEntity);
	}				
}
