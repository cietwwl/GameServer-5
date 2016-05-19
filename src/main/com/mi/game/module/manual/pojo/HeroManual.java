package com.mi.game.module.manual.pojo;

import java.util.Map;

public class HeroManual {
	private int templateID;
	private int exp;
	private int level;
	private int order;
	private double rate;
	private Map<Integer,Integer> property;
	
	public Map<Integer, Integer> getProperty() {
		return property;
	}
	public void setProperty(Map<Integer, Integer> property) {
		this.property = property;
	}
	public double getRate() {
		return rate;
	}
	public void setRate(double rate) {
		this.rate = rate;
	}
	public int getTemplateID() {
		return templateID;
	}
	public void setTemplateID(int templateID) {
		this.templateID = templateID;
	}
	public int getExp() {
		return exp;
	}
	public void setExp(int exp) {
		this.exp = exp;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	
	
}
