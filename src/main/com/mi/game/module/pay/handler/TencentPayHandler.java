package com.mi.game.module.pay.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.core.protocol.BaseProtocol;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.pay.PayModule;

/**
 * 腾讯平台支付成功后,客户端调用接口,比对订单数据。比对成功,修改订单状态
 * 
 */
@HandlerType(type = HandlerIds.PAY_TENCENT)
public class TencentPayHandler extends BaseHandler {

	@Override
	public void execute(IOMessage ioMessage) {
		String playerID = ioMessage.getPlayerId();
		if (ioMessage.getInputParse("orderID") != null) {
			String orderID = ioMessage.getInputParse("orderID").toString();
			String infoMD5 = ioMessage.getInputParse("infoMD5").toString();
			PayModule payModule = ModuleManager.getModule(ModuleNames.PayModule, PayModule.class);
			BaseProtocol protocol = new BaseProtocol();
			payModule.tencentPay(infoMD5, orderID, ioMessage);
		}
	}
}
