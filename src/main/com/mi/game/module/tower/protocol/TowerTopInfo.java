package com.mi.game.module.tower.protocol;

public class TowerTopInfo {
	private int towerLevel;
	private int level;
	private String name;
	private int photoID;
	private String playerID;
	private int vipLevel;

	
	public int getVipLevel() {
		return vipLevel;
	}
	public void setVipLevel(int vipLevel) {
		this.vipLevel = vipLevel;
	}
	public String getPlayerID() {
		return playerID;
	}
	public void setPlayerID(String playerID) {
		this.playerID = playerID;
	}
	public int getPhotoID() {
		return photoID;
	}
	public void setPhotoID(int photoID) {
		this.photoID = photoID;
	}
	public int getTowerLevel() {
		return towerLevel;
	}
	public void setTowerLevel(int towerLevel) {
		this.towerLevel = towerLevel;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	
}
