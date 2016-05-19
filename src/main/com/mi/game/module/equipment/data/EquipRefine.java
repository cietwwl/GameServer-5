package com.mi.game.module.equipment.data;

public class EquipRefine {
	private double percent;
	private int min;
	private int max;
	
	public EquipRefine(double percent,int min,int max){
		this.percent = percent;
		this.min = min;
		this.max = max;
	}
	
	public double getPercent() {
		return percent;
	}
	public void setPercent(double percent) {
		this.percent = percent;
	}
	public int getMin() {
		return min;
	}
	public void setMin(int min) {
		this.min = min;
	}
	public int getMax() {
		return max;
	}
	public void setMax(int max) {
		this.max = max;
	}
	
	
}
