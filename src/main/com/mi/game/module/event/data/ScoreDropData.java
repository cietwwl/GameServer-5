package com.mi.game.module.event.data;

import java.util.ArrayList;
import java.util.List;

import com.mi.core.engine.annotation.XmlTemplate;
import com.mi.game.module.event.pojo.DrawItem;

@XmlTemplate(template = { "com/mi/template/ScoreDropPrototype.xml" })
public class ScoreDropData extends EventBaseData {

	private int max;
	private int cost;
	private List<DrawItem> itemList = new ArrayList<DrawItem>();
	public int getMax() {
		return max;
	}
	public void setMax(int max) {
		this.max = max;
	}
	public int getCost() {
		return cost;
	}
	public void setCost(int cost) {
		this.cost = cost;
	}
	public List<DrawItem> getItemList() {
		return itemList;
	}
	public void setPackageItem(List<DrawItem> itemList) {
		this.itemList = itemList;
	}
	
}
