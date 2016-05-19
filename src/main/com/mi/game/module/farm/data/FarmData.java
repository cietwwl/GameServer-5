package com.mi.game.module.farm.data;

import com.mi.core.engine.annotation.XmlTemplate;
import com.mi.core.template.BaseTemplate;
@XmlTemplate(template = {"com/mi/template/FarmPrototype.xml"})
public class FarmData extends BaseTemplate{
	private int stageID;
	private int coinReward;
	private int soulReward;
	private int dropList;
	private int doublePrice;
	private int doubleVipLimit;
	private int triplePrice;
	private int tripleVipLimit;
	
	public int getDoublePrice() {
		return doublePrice;
	}
	public void setDoublePrice(String doublePrice) {
		if(doublePrice != null && !doublePrice.isEmpty()){
			String[] arr = doublePrice.split("=");
			this.doublePrice = Integer.parseInt(arr[1]);
		}
	
	}
	public int getDoubleVipLimit() {
		return doubleVipLimit;
	}
	public void setDoubleVipLimit(int doubleVipLimit) {
		this.doubleVipLimit = doubleVipLimit;
	}
	public int getTriplePrice() {
		return triplePrice;
	}
	public void setTriplePrice(String triplePrice) {
		if(triplePrice != null && !triplePrice.isEmpty()){
			String[] arr = triplePrice.split("=");
			this.triplePrice = Integer.parseInt(arr[1]);
		}
		
	}
	public int getTripleVipLimit() {
		return tripleVipLimit;
	}
	public void setTripleVipLimit(int tripleVipLimit) {
		this.tripleVipLimit = tripleVipLimit;
	}
	public int getDropList() {
		return dropList;
	}
	public void setDropList(String dropList) {
		if(dropList != null && !dropList.isEmpty()){
			String[] arr = dropList.split("=");
			this.dropList = Integer.parseInt(arr[0]);
		}
	}
	public int getStageID() {
		return stageID;
	}
	public void setStageID(int stageID) {
		this.stageID = stageID;
	}
	public int getCoinReward() {
		return coinReward;
	}
	public void setCoinReward(int coinReward) {
		this.coinReward = coinReward;
	}
	public int getSoulReward() {
		return soulReward;
	}
	public void setSoulReward(int soulReward) {
		this.soulReward = soulReward;
	}
	
	
}
