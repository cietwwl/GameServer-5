package com.mi.game.module.festival.protocol;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mi.core.protocol.BaseProtocol;
import com.mi.game.module.festival.pojo.NewYearFirecrackerEntity;
import com.mi.game.module.reward.data.GoodsBean;

public class FestivalFirecrackerProtocol extends BaseProtocol {

	private NewYearFirecrackerEntity firecrackerEntity;
	private Map<String, Object> itemMap;
	private List<GoodsBean> showMap;

	@Override
	public Map<String, Object> responseMap() {
		Map<String, Object> response = new HashMap<String, Object>();
		if (firecrackerEntity != null) {
			response.put("1106003", firecrackerEntity.responseMap());
		}
		if (itemMap != null) {
			response.put("itemMap", itemMap);
		}
		if (showMap != null) {
			response.put("showMap", showMap);
		}
		return response;
	}

	public NewYearFirecrackerEntity getFirecrackerEntity() {
		return firecrackerEntity;
	}

	public void setFirecrackerEntity(NewYearFirecrackerEntity firecrackerEntity) {
		this.firecrackerEntity = firecrackerEntity;
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

}
