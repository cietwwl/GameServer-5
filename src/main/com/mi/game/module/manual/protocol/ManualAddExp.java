package com.mi.game.module.manual.protocol;

public class ManualAddExp {
	private int level;
	private boolean crit;
	private int exp;
	private double rate;
	private int templateID;
	private long maxEnergy;
	private int achieve;

	public long getMaxEnergy() {
		return maxEnergy;
	}
	public void setMaxEnergy(long maxEnergy) {
		this.maxEnergy = maxEnergy;
	}
	public int getAchieve() {
		return achieve;
	}
	public void setAchieve(int achieve) {
		this.achieve = achieve;
	}
	public int getTemplateID() {
		return templateID;
	}
	public void setTemplateID(int templateID) {
		this.templateID = templateID;
	}
	public double getRate() {
		return rate;
	}
	public void setRate(double rate) {
		this.rate = rate;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public boolean isCrit() {
		return crit;
	}
	public void setCrit(boolean crit) {
		this.crit = crit;
	}
	public int getExp() {
		return exp;
	}
	public void setExp(int exp) {
		this.exp = exp;
	}
	
	
	
}
