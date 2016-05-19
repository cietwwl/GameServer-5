package com.mi.game.module.cdk.protocol;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mi.core.protocol.BaseProtocol;
import com.mi.game.module.reward.data.GoodsBean;

public class CDKProtocol extends BaseProtocol {
	private List<GoodsBean> rewards;
	private int pNum;
	private String typeID;
	private Map<String, Object> itemMap;
	private List<GoodsBean> showMap;

	@Override
	public Map<String, Object> responseMap() {
		Map<String, Object> response = new HashMap<String, Object>();
		if (itemMap != null) {
			response.put("itemMap", itemMap);
		}
		if (showMap != null) {
			response.put("showMap", showMap);
		}
		response.put("pNum", pNum);
		response.put("typeID", typeID);
		response.put("rewards", rewards);
		return response;
	}

	public int getpNum() {
		return pNum;
	}

	public void setpNum(int pNum) {
		this.pNum = pNum;
	}

	public List<GoodsBean> getRewards() {
		return rewards;
	}

	public void setRewards(List<GoodsBean> rewards) {
		this.rewards = rewards;
	}

	public Map<String, Object> getItemMap() {
		return itemMap;
	}

	public void setItemMap(Map<String, Object> itemMap) {
		this.itemMap = itemMap;
	}

	public List<GoodsBean> getShowMap() {
		return showMap;
	}

	public void setShowMap(List<GoodsBean> showMap) {
		this.showMap = showMap;
	}

	public String getTypeID() {
		return typeID;
	}

	public void setTypeID(String typeID) {
		this.typeID = typeID;
	}

}
