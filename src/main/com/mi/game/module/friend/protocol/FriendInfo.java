package com.mi.game.module.friend.protocol;

public class FriendInfo {
	private int online;    //0不在线 1在线
	private String name;
	private int fightValue;
	private String friendID;
	private int level;
	private int present; //0未赠送 1 增送
	private int attackNum;
	private int beAttackNum;
	
	public int getAttackNum() {
		return attackNum;
	}
	public void setAttackNum(int attackNum) {
		this.attackNum = attackNum;
	}
	public int getBeAttackNum() {
		return beAttackNum;
	}
	public void setBeAttackNum(int beAttackNum) {
		this.beAttackNum = beAttackNum;
	}
	public int getOnline() {
		return online;
	}
	public void setOnline(int online) {
		this.online = online;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getFightValue() {
		return fightValue;
	}
	public void setFightValue(int fightValue) {
		this.fightValue = fightValue;
	}
	public String getFriendID() {
		return friendID;
	}
	public void setFriendID(String friendID) {
		this.friendID = friendID;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getPresent() {
		return present;
	}
	public void setPresent(int present) {
		this.present = present;
	}
	
	
	
}
