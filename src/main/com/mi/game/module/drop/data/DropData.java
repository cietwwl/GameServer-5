package com.mi.game.module.drop.data;

import java.util.ArrayList;
import java.util.List;

import com.mi.core.engine.annotation.XmlTemplate;
import com.mi.core.template.BaseTemplate;

@XmlTemplate(template = {"com/mi/template/DropPrototype.xml"})
public class DropData extends BaseTemplate{
	private int max; //随机上限
	private List<PackageItem> packageItem = new ArrayList<PackageItem>();
	
	public int getMax() {
		return max;
	}
	public void setMax(int max) {
		this.max = max;
	}
	public List<PackageItem> getPackageItem() {
		return packageItem;
	}
	public void setPackageItem(List<PackageItem> packageItem) {
		this.packageItem = packageItem;
	}
	
	
	
}
