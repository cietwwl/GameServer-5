package com.mi.game.module.cdk.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.code.morphia.annotations.Indexed;
import com.mi.core.pojo.BaseEntity;
import com.mi.game.module.reward.data.GoodsBean;

public class CDKRewardEntity extends BaseEntity {

	private static final long serialVersionUID = 183266897494048548L;

	@Indexed
	private String rewardID;

	// 描述
	private String desc;

	// cdk可兑换物品
	private List<GoodsBean> items = new ArrayList<GoodsBean>();

	public String getRewardID() {
		return rewardID;
	}

	public void setRewardID(String rewardID) {
		this.rewardID = rewardID;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public List<GoodsBean> getItems() {
		return items;
	}

	public void setItems(List<GoodsBean> items) {
		this.items = items;
	}

	@Override
	public Object getKey() {
		return rewardID;
	}

	@Override
	public String getKeyName() {
		return "rewardID";
	}

	@Override
	public void setKey(Object key) {
		rewardID = key.toString();
	}

	@Override
	public Map<String, Object> responseMap(int t) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("rewardID", rewardID);
		result.put("desc", desc);
		result.put("itemsSize", items.size());
		return result;
	}

}
