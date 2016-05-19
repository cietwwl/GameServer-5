package com.mi.game.module.worldBoss.pojo;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.utils.IndexDirection;
import com.mi.core.pojo.BaseEntity;
@Entity(noClassnameStored = true)
public class PlayerBossEntity extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5781936657767268895L;
	@Indexed(value=IndexDirection.ASC, unique=true)
	private String playerID;
	private long damage;              //造成的伤害
	private int attackNum;           //攻击次数
	private int inspireNum;        // 鼓舞次数
	private int reviveNum;         //复活次数
	private long lastAttackTime;  //下次攻击时间
	private long lastInspireTime; //下次鼓舞时间
	private int bossUniqueID ;       //boss唯一ID

	
	public int getBossUniqueID() {
		return bossUniqueID;
	}

	public void setBossUniqueID(int bossUniqueID) {
		this.bossUniqueID = bossUniqueID;
	}

	public long getLastInspireTime() {
		return lastInspireTime;
	}

	public void setLastInspireTime(long lastInspireTime) {
		this.lastInspireTime = lastInspireTime;
	}

	public long getLastAttackTime() {
		return lastAttackTime;
	}

	public void setLastAttackTime(long lastAttackTime) {
		this.lastAttackTime = lastAttackTime;
	}

	public void addDamage(long damage){
		this.damage += damage;
	}
	
	public void addAttackNum(){
		this.attackNum ++;
	}
	
	public void addInspireNum(){
		this.inspireNum ++;
	}
	
	public void addReviveNum(){
		this.reviveNum ++;
	}
	
	public int getReviveNum() {
		return reviveNum;
	}

	public void setReviveNum(int reviveNum) {
		this.reviveNum = reviveNum;
	}

	public long getDamage() {
		return damage;
	}

	public void setDamage(long damage) {
		this.damage = damage;
	}

	public int getAttackNum() {
		return attackNum;
	}

	public void setAttackNum(int attackNum) {
		this.attackNum = attackNum;
	}

	public int getInspireNum() {
		return inspireNum;
	}

	public void setInspireNum(int inspireNum) {
		this.inspireNum = inspireNum;
	}

	@Override
	public Object getKey() {
		// TODO 自动生成的方法存根
		return playerID;
	}

	@Override
	public String getKeyName() {
		// TODO 自动生成的方法存根
		return "playerID";
	}

	@Override
	public void setKey(Object key) {
		// TODO 自动生成的方法存根
		playerID = key.toString();
	}

}
