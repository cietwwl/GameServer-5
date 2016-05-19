package com.mi.game.module.pet.protocol;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mi.core.protocol.BaseProtocol;
import com.mi.game.defines.HandlerIds;
import com.mi.game.module.pet.pojo.Pet;
import com.mi.game.module.pet.pojo.PetShard;
import com.mi.game.module.pet.pojo.PetSkill;
import com.mi.game.module.reward.data.GoodsBean;

public class PetInfoProtocol extends BaseProtocol{
	private Pet pet;
	private PetShard petShard ;
	private Pet oldPet;
	private long workPet;
	private long unWorkPet;
	private int state;
	private PetSkill petSkill;
	private List<PetSkill> petSkillList;	
	private int skillPoint;
	private Map<String,Object> itemMap;
	private long petID;
	private int maxSellNum;
	private long SpecialSkillTime;
	private long removePetID;
	private Map<String,Pet> petMap;		
	private List<GoodsBean> showMap;


	@Override
	public Map<String,Object> responseMap(int type){
		Map<String,Object> data = new HashMap<String, Object>();
		switch(type){
		case HandlerIds.CompoundPet:
			if(pet != null)
				data.put("pet",pet);
			if(petShard != null)
				data.put("petShard",petShard);
			break;
		case HandlerIds.PetTrain:
			if(pet != null)
				data.put("pet",pet);
			if(oldPet != null)
				data.put("oldPet",oldPet);
			break;
		case HandlerIds.PetWork:
			data.put("SpecialSkillTime",SpecialSkillTime);
			data.put("workPet", workPet);
			data.put("unWorkPet", unWorkPet);
			break;
		case HandlerIds.LearnPetSkill:
			data.put("petSkillList",petSkillList);
			data.put("skillPoint",skillPoint);
			data.put("state", state);
			data.put("petID", petID);
			break;
		case HandlerIds.LockPetSkill:
			if(itemMap != null)
				data.put("itemMap",itemMap);
			if(petSkill != null)
				data.put("petSkill",petSkill);
			data.put("petID", petID);
			break;
		case HandlerIds.ResetPetSkill:
			if(itemMap != null)
				data.put("itemMap",itemMap);
			if(pet != null)
				data.put("pet",pet);
			break;
		case HandlerIds.ExpandPetBag:
			data.put("maxSellNum", maxSellNum);
			if(itemMap != null)
				data.put("itemMap",itemMap);		
			break;
		case HandlerIds.GetSpecialSkillItem:
			data.put("SpecialSkillTime",SpecialSkillTime);
			if(itemMap != null)
				data.put("itemMap",itemMap);	
			if(showMap !=null)
				data.put("showMap",showMap);
			data.put("petID",petID);
			break;
		case HandlerIds.EatPet:	
			data.put("itemMap",itemMap);
			data.put("petMap", petMap);
			break;
		case HandlerIds.FeedPet:
			data.put("itemMap",itemMap);
			data.put("petInfo", pet);
			break;
		}
		return data;
	}

	
	
	public long getRemovePetID() {
		return removePetID;
	}



	public void setRemovePetID(long removePetID) {
		this.removePetID = removePetID;
	}



	public long getSpecialSkillTime() {
		return SpecialSkillTime;
	}

	public void setSpecialSkillTime(long specialSkillTime) {
		SpecialSkillTime = specialSkillTime;
	}

	public int getMaxSellNum() {
		return maxSellNum;
	}

	public void setMaxSellNum(int maxSellNum) {
		this.maxSellNum = maxSellNum;
	}

	public long getPetID() {
		return petID;
	}

	public void setPetID(long petID) {
		this.petID = petID;
	}

	public Map<String, Object> getItemMap() {
		return itemMap;
	}

	public void setItemMap(Map<String, Object> itemMap) {
		this.itemMap = itemMap;
	}

	public int getSkillPoint() {
		return skillPoint;
	}

	public void setSkillPoint(int skillPoint) {
		this.skillPoint = skillPoint;
	}

	public PetSkill getPetSkill() {
		return petSkill;
	}

	public void setPetSkill(PetSkill petSkill) {
		this.petSkill = petSkill;
	}

	public int getState() {
		return state;
	}
	
	public void setState(int state) {
		this.state = state;
	}

	public long getWorkPet() {
		return workPet;
	}

	public void setWorkPet(long workPet) {
		this.workPet = workPet;
	}

	public long getUnWorkPet() {
		return unWorkPet;
	}

	public void setUnWorkPet(long unWorkPet) {
		this.unWorkPet = unWorkPet;
	}

	public Pet getPet() {
		return pet;
	}
	public void setPet(Pet pet) {
		this.pet = pet;
	}
	public PetShard getPetShard() {
		return petShard;
	}
	public void setPetShard(PetShard petShard) {
		this.petShard = petShard;
	}
	public Pet getOldPet() {
		return oldPet;
	}
	public void setOldPet(Pet oldPet) {
		this.oldPet = oldPet;
	}
	public Map<String, Pet> getPetMap() {
		return petMap;
	}
	public void setPetMap(Map<String, Pet> petMap) {
		this.petMap = petMap;
	}
	public List<GoodsBean> getShowMap() {
		return showMap;
	}
	public void setShowMap(List<GoodsBean> showMap) {
		this.showMap = showMap;
	}
	public List<PetSkill> getPetSkillList() {
		return petSkillList;
	}
	public void setPetSkillList(List<PetSkill> petSkillList) {
		this.petSkillList = petSkillList;
	}
	
}
