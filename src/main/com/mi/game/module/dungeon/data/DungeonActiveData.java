package com.mi.game.module.dungeon.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mi.core.engine.annotation.XmlTemplate;
import com.mi.core.template.BaseTemplate;
@XmlTemplate(template = {"com/mi/template/ActiveDungeonPrototype.xml"})
public class DungeonActiveData extends BaseTemplate{
	private int payNum;
	private int freeNum;
	private int payGold;
	private String openTime;
	private String endTime;
	private List<Integer> openDate;
	private Map<Integer, Integer> dropList;
	private Map<Integer,Integer> payItem;
	private int maxStage;
	
	public int getMaxStage() {
		return maxStage;
	}
	public void setMaxStage(int maxStage) {
		this.maxStage = maxStage;
	}
	public Map<Integer, Integer> getDropList() {
		return dropList;
	}
	public void setDropList(String dropList) {
		if(dropList != null && !dropList.isEmpty()){
				this.dropList = new HashMap<Integer, Integer>();
				String[] tempArr = dropList.split(",");
				for(String temp : tempArr){
					String[] costArr = temp.split("=");
					if(costArr != null){
						this.dropList.put(Integer.parseInt(costArr[0]),Integer.parseInt(costArr[1]));
					}
				}
		}
	}
	
	
	public Map<Integer, Integer> getPayItem() {
		return payItem;
	}
	public void setPayItem(String payItem) {
		if(payItem != null && !payItem.isEmpty()){
			this.payItem = new HashMap<Integer, Integer>();
			String[] tempArr = payItem.split(",");
			for(String temp : tempArr){
				String[] costArr = temp.split("=");
				if(costArr != null){
					this.payItem.put(Integer.parseInt(costArr[0]),Integer.parseInt(costArr[1]));
				}
			}
		}
	}
	public int getPayNum() {
		return payNum;
	}
	public void setPayNum(int payNum) {
		this.payNum = payNum;
	}
	public int getFreeNum() {
		return freeNum;
	}
	public void setFreeNum(int freeNum) {
		this.freeNum = freeNum;
	}
	public int getPayGold() {
		return payGold;
	}
	public void setPayGold(int payGold) {
		this.payGold = payGold;
	}
	public String getOpenTime() {
		return openTime;
	}
	public void setOpenTime(String openTime) {
		this.openTime = openTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public List<Integer> getOpenDate() {
		return openDate;
	}
	public void setOpenDate(String openDate) {
		if(openDate != null && !openDate.isEmpty()){
			this.openDate = new ArrayList<>();
			String[] arr = openDate.split(",");
			for(String str : arr){
				this.openDate.add(Integer.parseInt(str));
			}
		}
	}
	
	
	
}
