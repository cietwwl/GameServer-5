package com.mi.game.module.event.data;

import java.util.ArrayList;
import java.util.List;

import com.mi.core.engine.annotation.XmlTemplate;
import com.mi.game.module.event.pojo.ExploreItem;

@XmlTemplate(template = { "com/mi/template/GraveDropPrototype.xml" })
public class ExploreData extends EventBaseData {

	private int max; // 随机上限
	private List<ExploreItem> itemList = new ArrayList<ExploreItem>();

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public List<ExploreItem> getItemList() {
		return itemList;
	}

	public void setPackageItem(List<ExploreItem> itemList) {
		this.itemList = itemList;
	}

}
