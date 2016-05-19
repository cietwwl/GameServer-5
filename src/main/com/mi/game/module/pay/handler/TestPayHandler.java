package com.mi.game.module.pay.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.module.base.handler.BaseHandler;

@HandlerType(type = HandlerIds.PAY_GOLD)
public class TestPayHandler extends BaseHandler {

	@Override
	public void execute(IOMessage ioMessage) {
//		String playerID = ioMessage.getPlayerId();
//		String payID = (String) ioMessage.getInputParse("payID");
//		PayModule payModule = ModuleManager.getModule(ModuleNames.PayModule, PayModule.class);
//
//		String orderID = payModule.orderID();
//		PayOrderEntity orderEntity = new PayOrderEntity();
//		orderEntity.setOrderID(orderID);
//		orderEntity.setPlayerID(playerID);
//		orderEntity.setPayType(Integer.parseInt(payID));
//		orderEntity.setPayMoney(PayModule.payDataMap.get(Integer.parseInt(payID)).getRmb());
//		orderEntity.setState(1);
//		orderEntity.setParse("test pay");
//		orderEntity.setCallbackTime(-1);
//		orderEntity.setPayPlatForm("test");
//		orderEntity.setCreateTime(-1);
//
//		payModule.payGold(playerID, orderEntity, "test", "test", ioMessage);
//
//		PayProtocol protocol = new PayProtocol();
//		payModule.payOrderInfo(playerID, orderID, protocol);
//		ioMessage.setOutputResult(protocol);

	}

}
