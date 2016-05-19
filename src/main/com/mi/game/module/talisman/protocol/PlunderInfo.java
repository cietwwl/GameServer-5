package com.mi.game.module.talisman.protocol;

import java.util.List;

public class PlunderInfo {
	private String name;
	private int level;
	private int probability;              //1极低 2低 3一般 4 较高 5 高
	private  List<Integer> heroList; 
	private String plunderID;
	private int quality;
	
	public int getQuality() {
		return quality;
	}
	public void setQuality(int quality) {
		this.quality = quality;
	}
	public String getPlunderID() {
		return plunderID;
	}
	public void setPlunderID(String plunderID) {
		this.plunderID = plunderID;
	}
	public List<Integer> getHeroList() {
		return heroList;
	}
	public void setHeroList(List<Integer> heroList) {
		this.heroList = heroList;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getProbability() {
		return probability;
	}
	public void setProbability(int probability) {
		this.probability = probability;
	}

}
