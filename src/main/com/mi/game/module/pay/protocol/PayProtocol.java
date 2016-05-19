package com.mi.game.module.pay.protocol;

import java.util.HashMap;
import java.util.Map;

import com.mi.core.protocol.BaseProtocol;
import com.mi.game.defines.HandlerIds;
import com.mi.game.module.event.define.EventConstans;
import com.mi.game.module.event.pojo.EventDrawPayEntity;
import com.mi.game.module.event.pojo.EventMonthCardEntity;
import com.mi.game.module.pay.pojo.CoolPadOrder;
import com.mi.game.module.pay.pojo.JinliOrder;
import com.mi.game.module.pay.pojo.PayEntity;
import com.mi.game.module.pay.pojo.PayOrderEntity;
import com.mi.game.module.vip.pojo.VipEntity;

public class PayProtocol extends BaseProtocol {

	private PayEntity payEntity;
	private VipEntity vipEntity;
	private PayOrderEntity orderEntity;
	private EventDrawPayEntity drawPayEntity;
	private EventMonthCardEntity monthCardEntity;
	private JinliOrder jinliOrder;
	private CoolPadOrder coolPadOrder;
	private Map<String, Object> itemMap;
	private Map<String, Object> vivoOrder;

	@Override
	public Map<String, Object> responseMap(int y) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		switch (y) {
		case HandlerIds.PAY_GOLD:
			if (payEntity != null) {
				responseMap.put("pay", payEntity.responseMap());
			}
			if (vipEntity != null) {
				responseMap.put("vipInfo", vipEntity.responseMap());
			}
			if (itemMap != null) {
				responseMap.put("itemMap", itemMap);
			}
			if (drawPayEntity != null) {
				responseMap.put("drawPay", drawPayEntity.responseMap());
			}
			break;
		case HandlerIds.PAY_INFO:
			if (payEntity != null) {
				responseMap.put("pay", payEntity.responseMap());
			}
			break;
		case HandlerIds.PAY_CREATE_ORDER:
			if (orderEntity != null) {
				responseMap.put("orderInfo", orderEntity.responseMap());
			}
			break;
		case HandlerIds.JINLI_CREATE_ORDER:
			if (jinliOrder != null) {
				responseMap.put("jinliOrder", jinliOrder.responseMap());
			}
			break;
		case HandlerIds.VIVO_CREATE_ORDER:
			if (jinliOrder != null) {
				responseMap.put("vivoOrder", vivoOrder);
			}
			break;
		case HandlerIds.COOLPAD_CREATE_ORDER:
			if (coolPadOrder != null) {
				responseMap.put("coolPadOrder", coolPadOrder.responseMap());
			}
			break;
		case HandlerIds.PAY_GET_ORDER:
			if (payEntity != null) {
				responseMap.put("pay", payEntity.responseMap());
			}
			if (vipEntity != null) {
				responseMap.put("vipInfo", vipEntity.responseMap());
			}
			if (orderEntity != null) {
				responseMap.put("orderInfo", orderEntity.responseMap());
			}
			if (drawPayEntity != null) {
				responseMap.put("drawPay", drawPayEntity.responseMap());
			}
			if (monthCardEntity != null) {
				responseMap.put(EventConstans.EVENT_TYPE_MONETH_CARD + "", monthCardEntity.responseMap());
			}
			if (itemMap != null) {
				responseMap.put("itemMap", itemMap);
			}
			break;
		}
		return responseMap;
	}

	public PayEntity getPayEntity() {
		return payEntity;
	}

	public void setPayEntity(PayEntity payEntity) {
		this.payEntity = payEntity;
	}

	public VipEntity getVipEntity() {
		return vipEntity;
	}

	public void setVipEntity(VipEntity vipEntity) {
		this.vipEntity = vipEntity;
	}

	public PayOrderEntity getOrderEntity() {
		return orderEntity;
	}

	public void setOrderEntity(PayOrderEntity orderEntity) {
		this.orderEntity = orderEntity;
	}

	public Map<String, Object> getItemMap() {
		return itemMap;
	}

	public void setItemMap(Map<String, Object> itemMap) {
		this.itemMap = itemMap;
	}

	public EventDrawPayEntity getDrawPayEntity() {
		return drawPayEntity;
	}

	public void setDrawPayEntity(EventDrawPayEntity drawPayEntity) {
		this.drawPayEntity = drawPayEntity;
	}

	public EventMonthCardEntity getMonthCardEntity() {
		return monthCardEntity;
	}

	public void setMonthCardEntity(EventMonthCardEntity monthCardEntity) {
		this.monthCardEntity = monthCardEntity;
	}

	public JinliOrder getJinliOrder() {
		return jinliOrder;
	}

	public void setJinliOrder(JinliOrder jinliOrder) {
		this.jinliOrder = jinliOrder;
	}

	public Map<String, Object> getVivoOrder() {
		return vivoOrder;
	}

	public void setVivoOrder(Map<String, Object> vivoOrder) {
		this.vivoOrder = vivoOrder;
	}

	public CoolPadOrder getCoolPadOrder() {
		return coolPadOrder;
	}

	public void setCoolPadOrder(CoolPadOrder coolPadOrder) {
		this.coolPadOrder = coolPadOrder;
	}

}
