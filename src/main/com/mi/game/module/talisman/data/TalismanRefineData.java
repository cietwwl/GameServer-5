package com.mi.game.module.talisman.data;

import com.mi.core.engine.annotation.XmlTemplate;
import com.mi.core.template.BaseTemplate;
import com.mi.game.module.reward.data.GoodsBean;
@XmlTemplate(template = "com/mi/template/IntensifyPrototype.xml")
public class TalismanRefineData extends BaseTemplate{
	private int quality;
	private int intensify;
	private int moneyReq;
	private GoodsBean itemReq;
	public int getQuality() {
		return quality;
	}
	public void setQuality(int quality) {
		this.quality = quality;
	}
	public int getIntensify() {
		return intensify;
	}
	public void setIntensify(int intensify) {
		this.intensify = intensify;
	}
	public int getMoneyReq() {
		return moneyReq;
	}
	public void setMoneyReq(int moneyReq) {
		this.moneyReq = moneyReq;
	}
	public GoodsBean getItemReq() {
		return itemReq;
	}
	public void setItemReq(String itemReq) {
		if(itemReq != null && !itemReq.isEmpty()){
				String[] strArr= itemReq.split("=");
				this.itemReq = new GoodsBean(Integer.parseInt(strArr[0]) ,Integer.parseInt(strArr[1]));
		}
	}
	
	
	
}
