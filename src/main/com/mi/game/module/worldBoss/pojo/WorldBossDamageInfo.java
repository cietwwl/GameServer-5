package com.mi.game.module.worldBoss.pojo;

public class WorldBossDamageInfo {
	private String name;
	private long damage;
	private long attackTime;
	
	public WorldBossDamageInfo(){}
	
	public WorldBossDamageInfo(String name,long damage){
		this.name = name;
		this.damage = damage;
		this.attackTime = System.currentTimeMillis();
	}
	
	public long getAttackTime() {
		return attackTime;
	}

	public void setAttackTime(long attackTime) {
		this.attackTime = attackTime;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public long getDamage() {
		return damage;
	}
	
	public void setDamage(long damage) {
		this.damage = damage;
	}
	
	
}
