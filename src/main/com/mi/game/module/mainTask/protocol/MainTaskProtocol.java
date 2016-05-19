package com.mi.game.module.mainTask.protocol;

import java.util.HashMap;
import java.util.Map;

import com.mi.core.protocol.BaseProtocol;
import com.mi.game.defines.HandlerIds;
import com.mi.game.module.mainTask.pojo.MainTaskEntity;
import com.mi.game.module.reward.data.GoodsBean;

public class MainTaskProtocol extends BaseProtocol{
	private Map<String, GoodsBean> showMap;
	private Map<String, Object> itemMap;
	private MainTaskEntity mainTaskEntity;
	@Override
	public Map<String, Object> responseMap(int y) {
		Map<String, Object> response = new HashMap<String, Object>();
		switch (y) {
			case HandlerIds.getTaskEntity:
				response.put("mainTask", mainTaskEntity.responseMap());
				break;
			case HandlerIds.getTaskReward:
				if(showMap != null)
					response.put("showMap", showMap.values());
				if(itemMap != null)
					response.put("itemMap", itemMap);
				break;
		}
		return response;
	}
	
	
	public MainTaskEntity getMainTaskEntity() {
		return mainTaskEntity;
	}
	public void setMainTaskEntity(MainTaskEntity mainTaskEntity) {
		this.mainTaskEntity = mainTaskEntity;
	}
	public Map<String, GoodsBean> getShowMap() {
		return showMap;
	}
	public void setShowMap(Map<String, GoodsBean> showMap) {
		this.showMap = showMap;
	}
	public Map<String, Object> getItemMap() {
		return itemMap;
	}
	public void setItemMap(Map<String, Object> itemMap) {
		this.itemMap = itemMap;
	}
	
	
	
}
