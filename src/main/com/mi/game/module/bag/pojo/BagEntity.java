package com.mi.game.module.bag.pojo;

import java.util.HashMap;
import java.util.Map;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.utils.IndexDirection;
import com.mi.core.pojo.BaseEntity;
@Entity(noClassnameStored = true)
public class BagEntity extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1199431505803553205L;
	@Indexed(unique=true,value=IndexDirection.ASC)
	private String playerID;
	private Map<Integer, BagItem> bagList = new HashMap<Integer, BagItem>();
	private int maxBagSellNum;
	private int useSilverBoxNum;
	private int useGoldBoxNum;
	private int useCopperNum;
	public int getMaxBagSellNum() {
		return maxBagSellNum;
	}

	@Override
	public Map<String, Object> responseMap() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("bagList", bagList.values());
		data.put("maxBagSellNum", maxBagSellNum);
		return data;
	}
	
	public int getUseSilverBoxNum() {
		return useSilverBoxNum;
	}

	public void setUseSilverBoxNum(int useSilverBoxNum) {
		this.useSilverBoxNum = useSilverBoxNum;
	}

	public int getUseGoldBoxNum() {
		return useGoldBoxNum;
	}

	public void setUseGoldBoxNum(int useGoldBoxNum) {
		this.useGoldBoxNum = useGoldBoxNum;
	}

	public int getUseCopperNum() {
		return useCopperNum;
	}

	public void setUseCopperNum(int useCopperNum) {
		this.useCopperNum = useCopperNum;
	}

	public void setMaxBagSellNum(int maxBagSellNum) {
		this.maxBagSellNum = maxBagSellNum;
	}

	public Map<Integer, BagItem> getBagList() {
		if (bagList == null) {
			bagList = new HashMap<Integer, BagItem>();
		}
		return bagList;
	}

	public void setBagList(Map<Integer, BagItem> bagList) {
		this.bagList = bagList;
	}

	@Override
	public Map<String, Object> responseMap(int t) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("playerID", playerID);
		result.put("maxBagSellNum", maxBagSellNum);
		return result;
	}

	@Override
	public Object getKey() {
		return playerID;
	}

	@Override
	public String getKeyName() {
		return "playerID";
	}

	@Override
	public void setKey(Object key) {
		this.playerID = key.toString();
	}

}
