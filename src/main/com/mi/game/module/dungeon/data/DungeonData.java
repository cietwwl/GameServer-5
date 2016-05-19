package com.mi.game.module.dungeon.data;

import java.util.HashMap;
import java.util.Map;

import com.mi.core.engine.annotation.XmlTemplate;
import com.mi.core.template.BaseTemplate;
@XmlTemplate(template = {"com/mi/template/DungeonPrototype.xml"})
public class DungeonData extends BaseTemplate{
	private int openLevel;                       //解锁等级
	private int nextOpenID;                  //开启的下一个关卡ID
	private int maxStarLevel;              //最大的级别
	private int maxAttackNum;         //每天最大攻击次数上限
	private int vitality;                         //消耗的体力
	private int actID;                            //所属大关ID
	private int maxStage;                   //最大波数
	private Map<Integer,Integer> dropList; //掉落列表
	private Map<Integer,Integer> specialDrop;
	private int soulReward;
	private int coinReward;
	public Map<Integer, Integer> getDropList() {
		if(dropList == null ){
			dropList = new HashMap<>();
		}
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
	
	public Map<Integer, Integer> getSpecialDrop() {
		if(specialDrop == null){
			specialDrop = new HashMap<>();
		}
		return specialDrop;
	}
	public void setSpecialDrop(String specialDrop) {
		if(specialDrop != null && !specialDrop.isEmpty()){
			this.specialDrop = new HashMap<Integer, Integer>();
			String[] tempArr = specialDrop.split(",");
			for(String temp : tempArr){
				String[] costArr = temp.split("=");
				if(costArr != null){
					this.specialDrop.put(Integer.parseInt(costArr[0]),Integer.parseInt(costArr[1]));
				}
			}
		}
	}
	
	public int getSoulReward() {
		return soulReward;
	}
	public void setSoulReward(int soulReward) {
		this.soulReward = soulReward;
	}
	public int getCoinReward() {
		return coinReward;
	}
	public void setCoinReward(int coinReward) {
		this.coinReward = coinReward;
	}
	public int getMaxStage() {
		return maxStage;
	}
	public void setMaxStage(int maxStage) {
		this.maxStage = maxStage;
	}
	public int getActID() {
		return actID;
	}
	public void setActID(int actID) {
		this.actID = actID;
	}
	public int getVitality() {
		return vitality;
	}
	public void setVitality(int vitality) {
		this.vitality = vitality;
	}
	public int getMaxAttackNum() {
		return maxAttackNum;
	}
	public void setMaxAttackNum(int maxAttackNum) {
		this.maxAttackNum = maxAttackNum;
	}
	public int getMaxStarLevel() {
		return maxStarLevel;
	}
	public void setMaxStarLevel(int maxStarLevel) {
		this.maxStarLevel = maxStarLevel;
	}
	public int getOpenLevel() {
		return openLevel;
	}
	public void setOpenLevel(int openLevel) {
		this.openLevel = openLevel;
	}
	public int getNextOpenID() {
		return nextOpenID;
	}
	public void setNextOpenID(int nextOpenID) {
		this.nextOpenID = nextOpenID;
	}
}
