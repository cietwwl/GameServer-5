package com.mi.game.module.limitShop.data;

import com.mi.core.engine.annotation.XmlTemplate;
import com.mi.game.module.event.data.EventBaseData;

/**
 * 限时抢购商品信息
 * 
 * @author 赵鹏翔
 * @time Apr 8, 2015 2:07:29 PM
 */
@XmlTemplate(template = { "com/mi/template/ActiveLimitShopPrototy.xml" })
public class LimitShopData extends EventBaseData {

	private int pid; // 模版id
	private String itemID; // 物品id
	private String price; // 折扣价格

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

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}
}
