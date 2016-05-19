package com.mi.game.module.festival.protocol;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mi.core.protocol.BaseProtocol;
import com.mi.game.module.festival.pojo.LaborDayNiceEntity;
import com.mi.game.module.reward.data.GoodsBean;

public class FestivalLaborDayNiceProtocol extends BaseProtocol {

	private LaborDayNiceEntity niceEntity;
	private Map<String, Object> itemMap;
	private List<GoodsBean> showMap;

	@Override
	public Map<String, Object> responseMap() {
		Map<String, Object> response = new HashMap<String, Object>();
		if (niceEntity != null) {
			response.put("1106010", niceEntity.responseMap());
		}
		if (itemMap != null) {
			response.put("itemMap", itemMap);
		}
		if (showMap != null) {
			response.put("showMap", showMap);
		}
		return response;
	}

	public LaborDayNiceEntity getNiceEntity() {
		return niceEntity;
	}

	public void setNiceEntity(LaborDayNiceEntity niceEntity) {
		this.niceEntity = niceEntity;
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
