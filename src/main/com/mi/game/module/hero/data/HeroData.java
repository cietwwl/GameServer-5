package com.mi.game.module.hero.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mi.core.engine.annotation.XmlTemplate;
import com.mi.core.template.BaseTemplate;
@XmlTemplate(template = {"com/mi/template/HeroPrototype.xml"})
public class HeroData extends BaseTemplate{
	private int starLevel;
	private int aptitude;
	private int swallowExp;
	private int canSwallow;
	private int quality;
	private int canSell;
	private int salePrice;
	private Map<Integer,Integer> resolveItem; 
	private int canResolve;
	private int nextID;
	private int rank;
	private int charactorID;         //英雄类型
	private int Atype;                    // 名将属性ID
	private int nextReq;
	private double baseHP;
	private double baseATK;
	private double baseDEF;
	private double baseRES;
	private int spirit;
	private int force;
	private int godhood;
	private Map<Integer,Integer> talent;
	private List<Integer> friendship;
	private int originalID;
	
	public int getOriginalID() {
		return originalID;
	}
	public void setOriginalID(int originalID) {
		this.originalID = originalID;
	}
	public List<Integer> getFriendship() {
		if(friendship == null)
			friendship = new ArrayList<>();
		return friendship;
	}
	public void setFriendship(String friendship) {
		if(friendship != null && !friendship.isEmpty()){
			this.friendship = new ArrayList<>();
			String[] temp = friendship.split(",");
			for(String str : temp){
				this.friendship.add(Integer.parseInt(str));
			}
		}
	
	}
	public Map<Integer, Integer> getTalent() {
		if(talent == null){
			talent = new HashMap<>();
		}
		return talent;
	}
	public void setTalent(String talent) {
		if(talent != null && !talent.isEmpty()){
			this.talent = new HashMap<>();
			String[] temp = talent.split(",");
			for(String str : temp){
				String[] prop  = str.split("=");
				this.talent.put(Integer.parseInt(prop[0]), Integer.parseInt(prop[1]));
			}
		}
		
	}
	public double getBaseHP() {
		return baseHP;
	}
	public void setBaseHP(double baseHP) {
		this.baseHP = baseHP;
	}
	public double getBaseATK() {
		return baseATK;
	}
	public void setBaseATK(double baseATK) {
		this.baseATK = baseATK;
	}
	public double getBaseDEF() {
		return baseDEF;
	}
	public void setBaseDEF(double baseDEF) {
		this.baseDEF = baseDEF;
	}
	public double getBaseRES() {
		return baseRES;
	}
	public void setBaseRES(double baseRES) {
		this.baseRES = baseRES;
	}
	public int getSpirit() {
		return spirit;
	}
	public void setSpirit(int spirit) {
		this.spirit = spirit;
	}
	public int getForce() {
		return force;
	}
	public void setForce(int force) {
		this.force = force;
	}
	public int getGodhood() {
		return godhood;
	}
	public void setGodhood(int godhood) {
		this.godhood = godhood;
	}

	public int getNextReq() {
		return nextReq;
	}
	
	public void setNextReq(int nextReq) {
		this.nextReq = nextReq;
	}
	
	public int getRank() {
		return rank;
	}
	
	public int getCharactorID() {
		return charactorID;
	}
	public void setCharactorID(int charactorID) {
		this.charactorID = charactorID;
	}
	public int getAtype() {
		return Atype;
	}
	public void setAtype(int atype) {
		Atype = atype;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public int getNextID() {
		return nextID;
	}
	public void setNextID(int nextID) {
		this.nextID = nextID;
	}
	public int getCanResolve() {
		return canResolve;
	}
	public void setCanResolve(int canResolve) {
		this.canResolve = canResolve;
	}
	public int getQuality() {
		return quality;
	}
	public void setQuality(int quality) {
		this.quality = quality;
	}
	public int getSwallowExp() {
		return swallowExp;
	}
	public void setSwallowExp(String swallowExp) {
		if(swallowExp != null && !swallowExp.isEmpty()){
			String[] strArr= swallowExp.split("=");
			this.swallowExp = Integer.parseInt(strArr[1]);
		}
	}
	public int getCanSwallow() {
		return canSwallow;
	}
	public void setCanSwallow(int canSwallow) {
		this.canSwallow = canSwallow;
	}
	public int getStarLevel() {
		return starLevel;
	}
	public void setStarLevel(int starLevel) {
		this.starLevel = starLevel;
	}
	public int getAptitude() {
		return aptitude;
	}
	public void setAptitude(int aptitude) {
		this.aptitude = aptitude;
	}
	public int getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(String salePrice) {
		if(salePrice != null && !salePrice.isEmpty()) {
			String[] strArr= salePrice.split("=");
			this.salePrice = Integer.parseInt(strArr[1]);
		}
	}
	
	public void setResolveItem(String resolveItem) {
		if(resolveItem != null && !resolveItem.isEmpty()){
			this.resolveItem = new HashMap<Integer, Integer>();
			String[] tempArr = resolveItem.split(",");
			for(String temp : tempArr){
				String[] costArr = temp.split("=");
				if(costArr != null){
					this.resolveItem.put(Integer.parseInt(costArr[0]),Integer.parseInt(costArr[1]));
				}
			}
		}
	}
	public Map<Integer, Integer> getResolveItem() {
		return resolveItem;
	}
	public int getCanSell() {
		return canSell;
	}
	public void setCanSell(int canSell) {
		this.canSell = canSell;
	}
	
	
	
}
