package com.mi.game.module.legion.data;

import java.util.ArrayList;
import java.util.List;

import com.mi.core.engine.annotation.XmlTemplate;
import com.mi.core.template.BaseTemplate;
import com.mi.game.module.legion.pojo.DropItem;

@XmlTemplate(template = { "com/mi/template/LegionDropPrototype.xml" })
public class LegionDropData extends BaseTemplate {

	private int max; // 随机上限
	private List<DropItem> itemList = new ArrayList<DropItem>();

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public List<DropItem> getDropItems() {
		return itemList;
	}

	public void setPackageItem(List<DropItem> itemList) {
		this.itemList = itemList;
	}

}
