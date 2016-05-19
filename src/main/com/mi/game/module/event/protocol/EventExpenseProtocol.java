package com.mi.game.module.event.protocol;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mi.core.protocol.BaseProtocol;
import com.mi.game.module.event.data.ConsumeGiftData;
import com.mi.game.module.event.define.EventConstans;
import com.mi.game.module.event.pojo.EventExpenseEntity;
import com.mi.game.module.reward.data.GoodsBean;

public class EventExpenseProtocol extends BaseProtocol {

	private EventExpenseEntity expenseEntity;
	private Map<String, Object> itemMap;
	private List<GoodsBean> showMap;
	private List<ConsumeGiftData> consumeGiftList;		

	@Override
	public Map<String, Object> responseMap() {
		Map<String, Object> response = new HashMap<String, Object>();
		if (expenseEntity != null) {
			response.put(EventConstans.EVENT_TYPE_EXPENSE + "", expenseEntity.responseMap());
		}
		if (itemMap != null) {
			response.put("itemMap", itemMap);
		}
		if (showMap != null) {
			response.put("showMap", showMap);
		}
		return response;
	}

	public EventExpenseEntity getExpenseEntity() {
		return expenseEntity;
	}

	public void setExpenseEntity(EventExpenseEntity expenseEntity) {
		this.expenseEntity = expenseEntity;
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
	public List<ConsumeGiftData> getConsumeGiftList() {
		return consumeGiftList;
	}

	public void setConsumeGiftList(List<ConsumeGiftData> consumeGiftList) {
		this.consumeGiftList = consumeGiftList;
	}
}
