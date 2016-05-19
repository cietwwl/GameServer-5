package com.mi.game.module.event.data;

import java.util.HashMap;
import java.util.Map;

import com.mi.core.engine.annotation.XmlTemplate;

@XmlTemplate(template = { "com/mi/template/ActiveLuckyDrawPrototype.xml" })
public class ActiveLuckyDrawData extends EventBaseData {
	
	private int  freeDraw;
	private int  payDraw;
	private String showItem;
	private String price;
	private Map<Integer,Integer> priceMap;	
	private int maxDraw;
	
	public int getFreeDraw() {
		return freeDraw;
	}
	public void setFreeDraw(int freeDraw) {
		this.freeDraw = freeDraw;
	}
	public int getPayDraw() {
		return payDraw;
	}
	public void setPayDraw(int payDraw) {
		this.payDraw = payDraw;
	}
	public String getShowItem() {
		return showItem;
	}
	public void setShowItem(String showItem) {
		this.showItem = showItem;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;				
		if (price != null && !price.isEmpty()) {
			String[] arr = price.split(",");
			this.priceMap = new HashMap<Integer, Integer>();
			for (int i = 0; i < arr.length; i++) {				
				String[] tempArr = arr[i].split("=");
				priceMap.put(Integer.parseInt(tempArr[0]),Integer.parseInt(tempArr[1]));
			}
		}
	}
	public int getMaxDraw() {
		return maxDraw;
	}
	public void setMaxDraw(int maxDraw) {
		this.maxDraw = maxDraw;
	}
	public Map<Integer, Integer> getPriceMap() {
		return priceMap;
	}
	
}
