package com.mi.game.module.hero.data;

import java.util.HashMap;
import java.util.Map;

import com.mi.core.engine.annotation.XmlTemplate;
import com.mi.core.template.BaseTemplate;
@XmlTemplate(template = {"com/mi/template/AdvancedRequestPrototype.xml"})
public class AdvanceHeroData extends BaseTemplate{
	private int quality;
	private int levelReq;
	private Map<Integer,Integer> itemReq ;
	private int rank;
	
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public int getQuality() {
		return quality;
	}
	public void setQuality(int quality) {
		this.quality = quality;
	}
	public int getLevelReq() {
		return levelReq;
	}
	public void setLevelReq(int levelReq) {
		this.levelReq = levelReq;
	}
	public Map<Integer, Integer> getItemReq() {
		return itemReq;
	}
	public void setItemReq(String itemReq) {
		if(itemReq != null && !itemReq.isEmpty()){
			this.itemReq = new HashMap<Integer, Integer>();
			String[] tempArr = itemReq.split(",");
			for(String temp : tempArr){
				String[] costArr = temp.split("=");
				if(costArr != null){
					this.itemReq.put(Integer.parseInt(costArr[0]),Integer.parseInt(costArr[1]));
				}
			}
		}
	}
	
	
	
}
