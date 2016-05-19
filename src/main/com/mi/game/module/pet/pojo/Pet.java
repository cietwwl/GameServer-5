package com.mi.game.module.pet.pojo;

import java.util.ArrayList;
import java.util.List;

public class Pet {
	private long petID;
	private int level = 1;                               //级数
	private long exp;                                     //经验值	
	private int skillPoint;                              //技能点
	private List<PetSkill> skillList;                    //技能集合
	private int specialSkillID;                          //特殊技能
	private int templateID;
	private boolean trained;                             //  是否驯养
	private boolean worked;                              //  是否出战
	private long specialSkillTime;
	private int fieldID;                                 //驯养位置
	private int resetPoint;	
	private int rank=0;                                  //当前几阶
	private long expTime;                                //记录上次上阵宠物获取经验的时间
	private int talentNum;                               //开启天赋技能的次数
				
	public int getTalentNum() {
		return talentNum;
	}
	public void setTalentNum(int talentNum) {
		this.talentNum = talentNum;
	}
	public long getExpTime() {
		return expTime;
	}
	public void setExpTime(long expTime) {
		this.expTime = expTime;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public int getResetPoint() {
		return resetPoint;
	}
	public void setResetPoint(int resetPoint) {
		this.resetPoint = resetPoint;
	}
	public int getFieldID() {
		return fieldID;
	}
	public void setFieldID(int fieldID) {
		this.fieldID = fieldID;
	}
	public long getSpecialSkillTime() {
		return specialSkillTime;
	}
	public void setSpecialSkillTime(long specialSkillTime) {
		this.specialSkillTime = specialSkillTime;
	}

	public boolean isTrained() {
		return trained;
	}
	public void setTrained(boolean trained) {
		this.trained = trained;
	}
	public boolean isWorked() {
		return worked;
	}
	public void setWorked(boolean worked) {
		this.worked = worked;
	}
	public long getPetID() {
		return petID;
	}
	public void setPetID(long petID) {
		this.petID = petID;
	}
	public int getTemplateID() {
		return templateID;
	}
	public void setTemplateID(int templateID) {
		this.templateID = templateID;
	}
	public int getSpecialSkillID() {
		return specialSkillID;
	}
	public void setSpecialSkillID(int specialSkillID) {
		this.specialSkillID = specialSkillID;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public long getExp() {
		return exp;
	}
	public void setExp(long exp) {
		this.exp = exp;
	}
	public int getSkillPoint() {
		return skillPoint;
	}
	public void setSkillPoint(int skillPoint) {
		this.skillPoint = skillPoint;
	}
	public List<PetSkill> getSkillList() {
		if(skillList == null){
			skillList = new ArrayList<PetSkill>();
		}
		return skillList;
	}
	public void setSkillList(List<PetSkill> skillList) {
		this.skillList = skillList;
	}		
	
}
