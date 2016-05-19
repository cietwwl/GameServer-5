package com.mi.game.module.event.data;

import com.mi.core.engine.annotation.XmlTemplate;

/**
 * vip折扣店
 * 
 * @author 赵鹏翔
 * @time Apr 7, 2015 10:44:35 AM
 */
@XmlTemplate(template = { "com/mi/template/VIPLimitShopPrototype.xml" })
public class VipDigoShopData extends EventBaseData {

	private int pid; // 模版id
	private String itemID; // 物品id
	private String cost; // 折扣价格
	private String recost; // 原价
	private int vipLimit; // vip限定等级

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public String getItemID() {
		return itemID;
	}

	public void setItemID(String itemID) {
		this.itemID = itemID;
	}

	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

	public String getRecost() {
		return recost;
	}

	public void setRecost(String recost) {
		this.recost = recost;
	}

	public int getVipLimit() {
		return vipLimit;
	}

	public void setVipLimit(int vipLimit) {
		this.vipLimit = vipLimit;
	}
}
