package com.mi.game.module.dungeon.protocol;

public class DungeonTopInfo {
	private String playerID;
	private String name;
	private int starNum;
	private int actID;
	private int photoID;
	private int level;
	private int vipLevel;
	private int fightValue;
	
	public int getFightValue() {
		return fightValue;
	}
	public void setFightValue(int fightValue) {
		this.fightValue = fightValue;
	}
	public int getVipLevel() {
		return vipLevel;
	}
	public void setVipLevel(int vipLevel) {
		this.vipLevel = vipLevel;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	
//	private int rank;
//	
//	public int getRank() {
//		return rank;
//	}
//	public void setRank(int rank) {
//		this.rank = rank;
//	}
	public String getPlayerID() {
		return playerID;
	}
	public void setPlayerID(String playerID) {
		this.playerID = playerID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getStarNum() {
		return starNum;
	}
	public void setStarNum(int starNum) {
		this.starNum = starNum;
	}
	public int getActID() {
		return actID;
	}
	public void setActID(int actID) {
		this.actID = actID;
	}
	public int getPhotoID() {
		return photoID;
	}
	public void setPhotoID(int photoID) {
		this.photoID = photoID;
	}
	
}
