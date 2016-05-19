package com.mi.game.module.event.protocol;

import java.util.HashMap;
import java.util.Map;

import com.mi.core.protocol.BaseProtocol;
import com.mi.game.defines.HandlerIds;
import com.mi.game.module.event.define.EventConstans;
import com.mi.game.module.event.pojo.EventMonthCardEntity;
import com.mi.game.module.pay.pojo.PayOrderEntity;
import com.mi.game.module.reward.data.GoodsBean;

public class EventMonthCardProtocol extends BaseProtocol {
	private EventMonthCardEntity eventMonthCardEntity;
	private PayOrderEntity orderEntity;
	private Map<String, Object> itemMap;
	private Map<String, GoodsBean> showMap;

	@Override
	public Map<String, Object> responseMap(int y) {
		Map<String, Object> response = new HashMap<String, Object>();

		switch (y) {
		case HandlerIds.EVENT_MONTH_CARD_BUY:
			if (orderEntity != null) {
				response.put("orderInfo", orderEntity.responseMap());
			}
			break;

		case HandlerIds.EVENT_MONTH_CARD_REWARD:
			if (eventMonthCardEntity != null) {
				response.put(EventConstans.EVENT_TYPE_MONETH_CARD + "", eventMonthCardEntity.responseMap());
			}
			if (itemMap != null) {
				response.put("itemMap", itemMap);
			}
			if (showMap != null) {
				response.put("showMap", showMap.values());
			}
			break;
		}
		return response;
	}

	public void setEventMonthCardEntity(EventMonthCardEntity eventMonthCardEntity) {
		this.eventMonthCardEntity = eventMonthCardEntity;
	}

	public void setItemMap(Map<String, Object> itemMap) {
		this.itemMap = itemMap;
	}

	public void setShowMap(Map<String, GoodsBean> showMap) {
		this.showMap = showMap;
	}

	public PayOrderEntity getOrderEntity() {
		return orderEntity;
	}

	public void setOrderEntity(PayOrderEntity orderEntity) {
		this.orderEntity = orderEntity;
	}

}
