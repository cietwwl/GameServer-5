package com.mi.game.module.event.data;


import java.util.Map;

import com.mi.core.engine.annotation.XmlTemplate;

@XmlTemplate(template = { "com/mi/template/ActiveFortunaPrototype.xml" })
public class ActiveFortunaData extends EventBaseData {		
	private String lowPrice;
	private String highPrice;
	private Map<Integer, Integer> lowPriceMap;
	private Map<Integer, Integer> highPriceMap;
	
	
	public String getLowPrice() {
		return lowPrice;
	}
	public Map<Integer, Integer> getLowPriceMap() {
		return lowPriceMap;
	}
	public Map<Integer, Integer> getHighPriceMap() {
		return highPriceMap;
	}
	public void setLowPrice(String lowPrice) {
		this.lowPrice = lowPrice;
		lowPriceMap=this.setMultiple(lowPrice);
	}
	public String getHighPrice() {
		return highPrice;
	}
	public void setHighPrice(String highPrice) {
		this.highPrice = highPrice;
		highPriceMap=this.setMultiple(highPrice);
	}
	
}
