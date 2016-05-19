package com.mi.game.module.dungeon.pojo;

import java.util.ArrayList;
import java.util.List;

public class ActLimitReward {
	private int actID;  //大关ID
	private List<Integer> buyPackage = new ArrayList<>();

	public List<Integer> getBuyPackage() {
		return buyPackage;
	}
	public void setBuyPackage(List<Integer> buyPackage) {
		this.buyPackage = buyPackage;
	}
	public int getActID() {
		return actID;
	}
	public void setActID(int actID) {
		this.actID = actID;
	}
	
	
}
