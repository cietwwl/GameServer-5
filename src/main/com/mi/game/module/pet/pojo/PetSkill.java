package com.mi.game.module.pet.pojo;

public class PetSkill {
	private int skillID;                                     // 技能ID
	private int level;                                        // 技能等级
	private int postion;                                  // 技能位置
	private boolean sellLock;                     // 技能位置是否解锁true：没有解锁
	private boolean skillLock;                   // 技能是否锁定	
	private int attack;                                     //攻击力
	private int hp;                                   //生命
	private int pdef;                              //物防	
	private int mdef;                                  //魔防
	private int levelNum;                              //开启天赋，增加等级的次数
	
	public int getLevelNum() {
		return levelNum;
	}
	public void setLevelNum(int levelNum) {
		this.levelNum = levelNum;
	}
	public int getSkillID() {
		return skillID;
	}
	public void setSkillID(int skillID) {
		this.skillID = skillID;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getPostion() {
		return postion;
	}
	public void setPostion(int postion) {
		this.postion = postion;
	}
	public boolean isSellLock() {
		return sellLock;
	}
	public void setSellLock(boolean sellLock) {
		this.sellLock = sellLock;
	}
	public boolean isSkillLock() {
		return skillLock;
	}
	public void setSkillLock(boolean skillLock) {
		this.skillLock = skillLock;
	}
	public int getAttack() {
		return attack;
	}
	public void setAttack(int attack) {
		this.attack = attack;
	}
	public int getHp() {
		return hp;
	}
	public void setHp(int hp) {
		this.hp = hp;
	}
	public int getPdef() {
		return pdef;
	}
	public void setPdef(int pdef) {
		this.pdef = pdef;
	}
	public int getMdef() {
		return mdef;
	}
	public void setMdef(int mdef) {
		this.mdef = mdef;
	}
	
	
	
	
}
