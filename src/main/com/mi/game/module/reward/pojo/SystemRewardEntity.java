package com.mi.game.module.reward.pojo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.utils.IndexDirection;
import com.mi.core.pojo.BaseEntity;
import com.mi.core.util.DateTimeUtil;
import com.mi.game.module.reward.data.GoodsBean;

public class SystemRewardEntity extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4542510678649933909L;
	@Indexed(value=IndexDirection.ASC, unique=true )
	private String rewardID;
	private List<GoodsBean> goodsList;
	@Indexed(value=IndexDirection.ASC, unique=true )
	private String rewardKey;
	private long createTime;
	private String desc;
	@Override
	public Map<String,Object> responseMap(){
		Map<String,Object> data = new HashMap<String, Object>();
		data.put("goodsList", goodsList);
		data.put("rewardKey", rewardKey);
		data.put("createTime", DateTimeUtil.getStringDate(createTime));
		data.put("desc", desc);
		data.put("rewardID", rewardID);
		return data;
	}
	
	public Map<String,Object> responseMap(int y){
		return responseMap();
	}
	
	
	public String getRewardID() {
		return rewardID;
	}

	public void setRewardID(String rewardID) {
		this.rewardID = rewardID;
	}

	public String getRewardKey() {
		return rewardKey;
	}

	public void setRewardKey(String rewardKey) {
		this.rewardKey = rewardKey;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public List<GoodsBean> getGoodsList() {
		return goodsList;
	}

	public void setGoodsList(List<GoodsBean> goodsList) {
		this.goodsList = goodsList;
	}

	@Override
	public Object getKey() {
		// TODO 自动生成的方法存根
		return rewardID;
	}

	@Override
	public String getKeyName() {
		// TODO 自动生成的方法存根
		return "rewardID";
	}

	@Override
	public void setKey(Object key) {
		// TODO 自动生成的方法存根
		rewardID = key.toString();
	}
	
}
