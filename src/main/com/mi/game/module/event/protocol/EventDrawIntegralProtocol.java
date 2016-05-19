package com.mi.game.module.event.protocol;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mi.core.protocol.BaseProtocol;
import com.mi.game.module.event.define.EventConstans;
import com.mi.game.module.event.pojo.EventDrawIntegralEntity;
import com.mi.game.module.reward.data.GoodsBean;

public class EventDrawIntegralProtocol extends BaseProtocol {

	private EventDrawIntegralEntity drawIntegralEntity;
	private Map<String, Object> itemMap;
	private List<GoodsBean> showMap;
	private List<GoodsBean> showGoodsBeanList; // 显示的列表

	@Override
	public Map<String, Object> responseMap() {
		Map<String, Object> response = new HashMap<String, Object>();
		if (drawIntegralEntity != null) {
			response.put(EventConstans.EVENT_TYPE_DRAW_INTEGRAL + "", drawIntegralEntity.responseMap());
		}
		if (itemMap != null) {
			response.put("itemMap", itemMap);
		}
		if (showMap != null) {
			response.put("showMap", showMap);
		}
		if (showGoodsBeanList != null) {
			response.put("showGoodsBeanList", showGoodsBeanList);
		}
		return response;
	}

	public EventDrawIntegralEntity getDrawIntegralEntity() {
		return drawIntegralEntity;
	}

	public void setDrawIntegralEntity(EventDrawIntegralEntity drawIntegralEntity) {
		this.drawIntegralEntity = drawIntegralEntity;
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

	public List<GoodsBean> getShowGoodsBeanList() {
		return showGoodsBeanList;
	}

	public void setShowGoodsBeanList(List<GoodsBean> showGoodsBeanList) {
		this.showGoodsBeanList = showGoodsBeanList;
	}

}
