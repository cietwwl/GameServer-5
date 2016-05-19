package com.mi.game.module.event.pojo;

/**
 * 抽奖掉落物品
 * 
 * @author Administrator
 *
 */
public class DrawItem {

	private int weight;
	private int amount;
	private int itemId;

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
}
