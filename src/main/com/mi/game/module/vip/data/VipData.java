package com.mi.game.module.vip.data;

import java.util.Map;

import com.mi.core.engine.annotation.XmlTemplate;

@XmlTemplate(template = { "com/mi/template/VIPPrototype.xml" })
public class VipData extends VipBaseData {
	private int level;
	private int price;
	private Map<String, Integer> permission;
	private int maxVitatly;

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public Map<String, Integer> getPermission() {
		return permission;
	}

	public int getMaxVitatly() {
		return maxVitatly;
	}

	public void setMaxVitatly(int maxVitatly) {
		this.maxVitatly = maxVitatly;
	}

	public void setPermission(String str) {
		this.permission = this.setMultiple(str);
	}

	public Integer getValue(int key) {
		return permission.get(key);
	}

}
