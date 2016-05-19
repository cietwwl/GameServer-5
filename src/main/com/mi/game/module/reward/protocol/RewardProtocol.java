package com.mi.game.module.reward.protocol;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mi.core.protocol.BaseProtocol;
import com.mi.game.defines.HandlerIds;
import com.mi.game.module.reward.data.GoodsBean;
import com.mi.game.module.reward.pojo.RewardCenterEntity;

public class RewardProtocol extends BaseProtocol{
	private RewardCenterEntity entity;
	private Map<String,Object> itemMap;
	private List<GoodsBean> showMap;
	@Override
	public Map<String,Object> responseMap(int y){
		Map<String,Object> data = new HashMap<String, Object>();
		switch(y){
			case HandlerIds.getRewardCenterInfo:
				if(entity != null)
					data.put("rewardCenter", entity.responseMap());
				break;
			case HandlerIds.getRewardCenterRewrard:
//				if(entity != null)
//					data.put("rewardCenter", entity.responseMap());
				if(itemMap != null)
					data.put("itemMap", itemMap);
				if(showMap != null)
					data.put("showMap",showMap);
				break;
		}
		return data;
 	}
	
	public List<GoodsBean> getShowMap() {
		return showMap;
	}

	public void setShowMap(List<GoodsBean> showMap) {
		this.showMap = showMap;
	}

	public RewardCenterEntity getEntity() {
		return entity;
	}
	public void setEntity(RewardCenterEntity entity) {
		this.entity = entity;
	}
	public Map<String, Object> getItemMap() {
		return itemMap;
	}
	public void setItemMap(Map<String, Object> itemMap) {
		this.itemMap = itemMap;
	}
	
}
