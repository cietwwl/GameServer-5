package com.mi.game.module.pet.data;

import java.util.HashMap;
import java.util.Map;

import com.mi.core.engine.annotation.XmlTemplate;
import com.mi.core.template.BaseTemplate;
@XmlTemplate(template = {"com/mi/template/PetPrototype.xml"})
public class PetData extends BaseTemplate{
	private int initialPrint;               //初始的技能点数
	private int skillLimit;                 //技能最大等级
	private int skillSlots;                 //技能孔数
	private int petSpecialSkillID;          //特殊技能
	private Map<Integer,Integer> petTalent;        //天赋	
	private int quality;                    //资质
	private int baseExp;                    //基础经验值
	private int skillQuality;
	private int lockNum;                    //技能锁定数量
	
	public int getLockNum() {
		return lockNum;
	}
	public void setLockNum(int lockNum) {
		this.lockNum = lockNum;
	}
	public int getSkillQuality() {
		return skillQuality;
	}
	public void setSkillQuality(int skillQuality) {
		this.skillQuality = skillQuality;
	}	
	public int getBaseExp() {
		return baseExp;
	}
	public void setBaseExp(int baseExp) {
		this.baseExp = baseExp;
	}
	public int getQuality() {
		return quality;
	}
	public void setQuality(int quality) {
		this.quality = quality;
	}
	public int getInitialPrint() {
		return initialPrint;
	}
	public void setInitialPrint(int initialPrint) {
		this.initialPrint = initialPrint;
	}
	public int getSkillLimit() {
		return skillLimit;
	}
	public void setSkillLimit(int skillLimit) {
		this.skillLimit = skillLimit;
	}
	public int getSkillSlots() {
		return skillSlots;
	}
	public void setSkillSlots(int skillSlots) {
		this.skillSlots = skillSlots;
	}
	public int getPetSpecialSkillID() {
		return petSpecialSkillID;
	}
	public void setPetSpecialSkillID(int petSpecialSkillID) {
		this.petSpecialSkillID = petSpecialSkillID;
	}	
	public Map<Integer, Integer> getPetTalent() {
		return petTalent;
	}
	public void setPetTalent(String petTalent) {
		if(petTalent != null){
			this.petTalent = new HashMap<Integer, Integer>();
			String[] tempArr = petTalent.split(",");
			for(String temp : tempArr){
				String[] costArr = temp.split("=");
				if(costArr != null){
					this.petTalent.put(Integer.parseInt(costArr[0]),Integer.parseInt(costArr[1]));
				}
			}
		}		
	}
	
}
