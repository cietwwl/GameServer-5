package com.mi.game.module.pay.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.pay.PayModule;
import com.mi.game.module.pay.protocol.PayProtocol;

/**
 * 获取订单数据
 * 
 * @author 李强 <br/>
 *
 *         创建时间：2013-9-18 下午4:53:22
 */
@HandlerType(type = HandlerIds.PAY_GET_ORDER, order = 2)
public class PayOrderInfoHandler extends BaseHandler {

	@Override
	public void execute(IOMessage ioMessage) {
		String playerID = ioMessage.getPlayerId();
		if (ioMessage.getInputParse("orderID") != null) {
			String orderID = ioMessage.getInputParse("orderID").toString();
			PayModule payModule = ModuleManager.getModule(ModuleNames.PayModule, PayModule.class);
			PayProtocol protocol = new PayProtocol();
			payModule.payOrderInfo(playerID, orderID, protocol);
			ioMessage.setOutputResult(protocol);
		}
	}
}
