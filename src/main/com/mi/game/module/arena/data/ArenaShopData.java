package com.mi.game.module.arena.data;

import com.mi.core.engine.annotation.XmlTemplate;
import com.mi.core.template.BaseTemplate;
import com.mi.game.module.reward.data.GoodsBean;
@XmlTemplate(template = {"com/mi/template/ArenaShopPrototype.xml"})
public class ArenaShopData extends BaseTemplate{
	private GoodsBean itemID;
	private int price;
	private GoodsBean amountLimit;
	private int lvLimit;
	
	public GoodsBean getItemID() {
		return itemID;
	}
	public void setItemID(String itemID) {
		if(itemID  != null && !itemID.isEmpty()){
			String[] strArr = itemID.split("=");
			this.itemID = new GoodsBean(Integer.parseInt(strArr[0]),Integer.parseInt(strArr[1]));
		}

	}
	public int getPrice() {
		return price;
	}
	public void setPrice(String price) {
		if(price  != null && !price.isEmpty()){
			String[] strArr =price.split("=");
			this.price = Integer.parseInt(strArr[1]);
		}
		
	}
	public GoodsBean getAmountLimit() {
		return amountLimit;
	}
	public void setAmountLimit(String amountLimit) {
		if(amountLimit  != null && !amountLimit.isEmpty()){
			String[] strArr = amountLimit.split("=");
			this.amountLimit = new GoodsBean(Integer.parseInt(strArr[0]),Integer.parseInt(strArr[1]));
		}
	}
	public int getLvLimit() {
		return lvLimit;
	}
	public void setLvLimit(int lvLimit) {
		this.lvLimit = lvLimit;
	}
	
	
}
