package com.mi.game.module.pay.data;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.mi.core.engine.annotation.XmlTemplate;

@XmlTemplate(template = {
	"com/mi/template/LimitDungeonVipPackagePrototype.xml"
})
public class PayTimeRewardData extends PayBaseData {

	private int dungeonID;

	private long time;

	private int payID;

	private Map<Integer, Map<Integer,Integer>> reward = new HashMap<>();
	
	private Map<Integer,Integer> price = new HashMap<>();
	
	public Map<Integer, Integer> getPrice() {
		return price;
	}

	public void setPrice(String price) {
		if(StringUtils.isNotBlank(price)){
			String[] arr = price.split(";");
			for(String temp : arr){
				String[] valueArr = temp.split("\\|");
				String[] priceArr = valueArr[1].split("=");
				this.price.put(Integer.parseInt(valueArr[0]), Integer.parseInt(priceArr[1]));
			}
		}
	}

	public int getDungeonID() {
		return dungeonID;
	}

	public void setDungeonID(int dungeonID) {
		this.dungeonID = dungeonID;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getPayID() {
		return payID;
	}

	public void setPayID(int payID) {
		this.payID = payID;
	}

	public Map<Integer, Map<Integer, Integer>> getReward() {
		return reward;
	}

	public void setReward(String reward) {
		if(StringUtils.isNotBlank(reward)){
			String[] arr = reward.split(";");
			for(String temp : arr){
				String[] valueArr = temp.split("\\|");
				Map<Integer,Integer> map = setMultiple(valueArr[1]);
				this.reward.put(Integer.parseInt(valueArr[0]), map);
			}
		}
	}



}
