package com.mi.game.module.talisman.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mi.core.engine.annotation.XmlTemplate;
import com.mi.core.template.BaseTemplate;
@XmlTemplate(template = {"com/mi/template/TalismanPrototype.xml"})
public class TalismanData extends BaseTemplate{
	private Map<String,Double> refine;        //精炼属性
	private int part;                                               //装备部位
	private Map<String,Double> property; //初始属性
	private int price;                                            //出售价格
	private Map<String,Double> intensify;            //强化增加属性
	private int aptitude;                                    //品质
	private int quality;                                      //稀有度
	private int canSwallow;                           //能否作为强化材料
	private int baseEXP;                                 //基础经验
	private int sell;
	private int canRefine;
	private int canResolve;
	private Map<Integer,Integer> resolveItem; 
	private int canRebirth;
	private Map<String,Double> unlockProperty1;
	private Map<String,Double> unlockProperty2;
	private List<Integer> composeID;
	private int canEquip;         //是否可装备 1可/0否
	private int refineMax;       //最大精炼等级
	
	public int getRefineMax() {
		return refineMax;
	}
	public void setRefineMax(int refineMax) {
		this.refineMax = refineMax;
	}
	public int getCanEquip() {
		return canEquip;
	}
	public void setCanEquip(int canEquip) {
		this.canEquip = canEquip;
	}
	public Map<String, Double> getUnlockProperty1() {
		return unlockProperty1;
	}
	public void setUnlockProperty1(String unlockProperty1) {
		if(unlockProperty1 != null && !unlockProperty1.isEmpty()){
			this.unlockProperty1 = new HashMap<String, Double>();
			String[] tempArr = unlockProperty1.split(",");
			for(String temp : tempArr){
				String[] costArr = temp.split("=");
				if(costArr != null){
					this.unlockProperty1.put(costArr[0],Double.parseDouble(costArr[1]));
				}
			}
		}
	}
	public Map<String, Double> getUnlockProperty2() {
		return unlockProperty2;
	}
	public void setUnlockProperty2(String unlockProperty2) {
		if(unlockProperty2 != null && !unlockProperty2.isEmpty()){
			this.unlockProperty2 = new HashMap<String, Double>();
			String[] tempArr = unlockProperty2.split(",");
			for(String temp : tempArr){
				String[] costArr = temp.split("=");
				if(costArr != null){
					this.unlockProperty2.put(costArr[0],Double.parseDouble(costArr[1]));
				}
			}
		}
	}
	public int getCanRebirth() {
		return canRebirth;
	}
	public void setCanRebirth(int canRebirth) {
		this.canRebirth = canRebirth;
	}
	public int getCanResolve() {
		return canResolve;
	}
	public void setCanResolve(int canResolve) {
		this.canResolve = canResolve;
	}
	public Map<Integer, Integer> getResolveItem() {
		return resolveItem;
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
	public int getCanRefine() {
		return canRefine;
	}
	public void setCanRefine(int canRefine) {
		this.canRefine = canRefine;
	}
	public int getSell() {
		return sell;
	}
	public void setSell(int sell) {
		this.sell = sell;
	}
	public Map<String, Double> getRefine() {
		return refine;
	}
	public void setRefine(String refine) {
		if(refine != null && !refine.isEmpty()){
			this.refine = new HashMap<String, Double>();
			String[] tempArr = refine.split(",");
			for(String temp : tempArr){
				String[] costArr = temp.split("=");
				if(costArr != null){
					this.refine.put(costArr[0],Double.parseDouble(costArr[1]));
				}
			}
		}
	}
	public Map<String, Double> getProperty() {
		if(property == null){
			property = new HashMap<>();
		}
		return property;
	}
	public void setProperty(String property) {
		if(property != null && !property.isEmpty()){
			this.property = new HashMap<String, Double>();
			String[] tempArr = property.split(",");
			for(String temp : tempArr){
				String[] costArr = temp.split("=");
				if(costArr != null){
					this.property.put(costArr[0],Double.parseDouble(costArr[1]));
				}
			}
		}
	}
	public Map<String, Double> getIntensify() {
		return intensify;
	}
	public void setIntensify(String intensify) {
		if(intensify != null && !intensify.isEmpty()){
			this.intensify = new HashMap<String, Double>();
			String[] tempArr = intensify.split(",");
			for(String temp : tempArr){
				String[] costArr = temp.split("=");
				if(costArr != null){
					this.intensify.put(costArr[0],Double.parseDouble(costArr[1]));
				}
			}
		}
	}
	public int getPart() {
		return part;
	}
	public void setPart(int part) {
		this.part = part;
	}
	
	public int getPrice() {
		return price;
	}
	public void setPrice(String price) {
		if(price != null && !price.isEmpty()){
			String[] strArr= price.split("=");
			this.price = Integer.parseInt(strArr[1]);
		}
	}
	public int getAptitude() {
		return aptitude;
	}
	public void setAptitude(int aptitude) {
		this.aptitude = aptitude;
	}
	public int getQuality() {
		return quality;
	}
	public void setQuality(int quality) {
		this.quality = quality;
	}
	public int getCanSwallow() {
		return canSwallow;
	}
	public void setCanSwallow(int canSwallow) {
		this.canSwallow = canSwallow;
	}
	public int getBaseEXP() {
		return baseEXP;
	}
	public void setBaseEXP(String baseEXP) {
		if(baseEXP != null && !baseEXP.isEmpty() ){
			String[] strArr= baseEXP.split("=");
			this.baseEXP = Integer.parseInt(strArr[1]);
		}
	}
	
	public List<Integer> getComposeID() {
		return composeID;
	}
	public void setComposeID(String composeID) {
		if(composeID != null && !composeID.isEmpty()){
			this.composeID = new ArrayList<Integer>();
			String[] arr = composeID.split(",");
			for(String str : arr){
				this.composeID.add(Integer.parseInt(str));
			}
		}
		
	}
	
	
	
	
}
