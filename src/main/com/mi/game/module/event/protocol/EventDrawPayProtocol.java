package com.mi.game.module.event.protocol;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mi.core.protocol.BaseProtocol;
import com.mi.game.module.event.data.ChargeActiveData;
import com.mi.game.module.event.data.ChargeDropData;
import com.mi.game.module.event.define.EventConstans;
import com.mi.game.module.event.pojo.EventDrawPayEntity;
import com.mi.game.module.reward.data.GoodsBean;

public class EventDrawPayProtocol extends BaseProtocol {

	private EventDrawPayEntity drawPayEntity;
	private Map<String, Object> itemMap;
	private List<GoodsBean> showMap;
	private List<ChargeActiveData> drawPayList;	

	@Override
	public Map<String, Object> responseMap() {
		Map<String, Object> response = new HashMap<String, Object>();
		if (drawPayEntity != null) {
			response.put(EventConstans.EVENT_TYPE_DRAW_PAY + "", drawPayEntity.responseMap());
		}
		if (itemMap != null) {
			response.put("itemMap", itemMap);
		}
		if (showMap != null) {
			response.put("showMap", showMap);
		}
		return response;
	}

	public EventDrawPayEntity getDrawPayEntity() {
		return drawPayEntity;
	}

	public void setDrawPayEntity(EventDrawPayEntity drawPayEntity) {
		this.drawPayEntity = drawPayEntity;
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
	
	public List<ChargeActiveData> getDrawPayList() {
		return drawPayList;
	}

	public void setDrawPayList(List<ChargeActiveData> drawPayList) {
		this.drawPayList = drawPayList;
	}

}
