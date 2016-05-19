package com.mi.game.module.equipment.data;

import java.util.HashMap;
import java.util.Map;

import com.mi.core.engine.annotation.XmlTemplate;
import com.mi.core.template.BaseTemplate;

/**
 * @author 刘凯旋	
 * 装备实体
 * 2014年6月12日 下午4:01:05
 */
@XmlTemplate(template = {"com/mi/template/EquipmentPrototype.xml"})
public class EquipmentData extends BaseTemplate{
	private Map<String,Double> refine;              //精炼属性
	private int part;                                //装备部位
	private Map<String,Double> property;            //初始属性
	private int quality;                             //稀有度
	private int price;                               //出售价格
	private Map<String,Double> intensify;           //强化增加属性
	private int setID;                               //所属的套装ID
	private int aptitude;                            //品质
	private Map<Integer,Integer> resolveItem; 
	private int canResolve;
	private int canRebirth;
	
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
	public int getQuality() {
		return quality;
	}
	public void setQuality(int rare) {
		this.quality = rare;
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
	public Map<String,Double> getIntensify() {
		return intensify;
	}
	public void setIntensify(String intensify) {
		if(intensify != null){
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
	public int getAptitude() {
		return aptitude;
	}
	public void setAptitude(int aptitude) {
		this.aptitude = aptitude;
	}
	public int getSetID() {
		return setID;
	}
	public void setSetID(int setID) {
		this.setID = setID;
	}
	public int getPart() {
		return part;
	}
	public void setPart(int part) {
		this.part = part;
	}
	public Map<String, Double> getRefine() {
		return refine;
	}
	public void setRefine(String refine) {
		if(refine != null){
			this.refine = new HashMap<String,Double>();
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
		return property;
	}
	
	public void setProperty(String property) {
		if(property != null){
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
	
}
