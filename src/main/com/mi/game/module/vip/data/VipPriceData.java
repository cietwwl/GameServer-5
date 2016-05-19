package com.mi.game.module.vip.data;

import com.mi.core.engine.annotation.XmlTemplate;

@XmlTemplate(template = { "com/mi/template/VIPPricePrototype.xml" })
public class VipPriceData extends VipBaseData {
	// 行为id
	private int methodID;
	// 价格
	private int price;
	// 递增价格
	private int pricePlus;

	public int getMethodID() {
		return methodID;
	}

	public void setMethodID(int methodID) {
		this.methodID = methodID;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getPricePlus() {
		return pricePlus;
	}

	public void setPricePlus(int pricePlus) {
		this.pricePlus = pricePlus;
	}

}
