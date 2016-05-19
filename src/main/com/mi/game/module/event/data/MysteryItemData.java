package com.mi.game.module.event.data;

import java.util.ArrayList;
import java.util.List;

import com.mi.core.engine.annotation.XmlTemplate;
import com.mi.game.module.event.pojo.ActiveItem;

@XmlTemplate(template = { "com/mi/template/ActiveShopPrototype.xml" })
public class MysteryItemData extends EventBaseData {

	private int max; // 随机上限
	private List<ActiveItem> itemList = new ArrayList<ActiveItem>();

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public List<ActiveItem> getItemList() {
		return itemList;
	}

	public void setPackageItem(List<ActiveItem> itemList) {
		this.itemList = itemList;
	}

}
