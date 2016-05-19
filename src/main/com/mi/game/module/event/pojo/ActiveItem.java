package com.mi.game.module.event.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class ActiveItem {
	// 商品id
	private int itemID;
	// 个数
	private int amount;
	// 权重
	private int weight;
	// 价格
	private Map<Integer, Integer> price;

	public int getItemID() {
		return itemID;
	}

	public void setItemID(int itemID) {
		this.itemID = itemID;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public Map<Integer, Integer> getPrice() {
		return price;
	}

	public void setPrice(String str) {
		this.price = this.setMultiple(str);
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public List<Integer> getKeys(Map<Integer, Integer> map) {
		List<Integer> list = new ArrayList<Integer>();
		Set<Entry<Integer, Integer>> set = map.entrySet();
		for (Entry<Integer, Integer> entry : set) {
			list.add(entry.getKey());
		}
		return list;
	}

	public Integer getPriceValue(int key) {
		return price.get(key);
	}

	/**
	 * 解析多重含义的xml节点内容
	 * 
	 * @param str
	 * @return
	 */
	public Map<Integer, Integer> setMultiple(String str) {
		Map<Integer, Integer> multiple = null;
		if (str != null && !str.isEmpty()) {
			multiple = new HashMap<Integer, Integer>();
			String[] temps = str.split(",");
			for (String temp : temps) {
				String[] costArr = temp.split("=");
				if (costArr != null) {
					multiple.put(Integer.parseInt(costArr[0]), Integer.parseInt(costArr[1]));
				}
			}
		}
		return multiple;
	}

	@Override
	public String toString() {
		return "ActiveItem [itemID=" + itemID + ", amount=" + amount + ", weight=" + weight + ", price=" + price + "]";
	}

}
