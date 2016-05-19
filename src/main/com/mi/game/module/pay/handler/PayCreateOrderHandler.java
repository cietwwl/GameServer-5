package com.mi.game.module.pay.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.TemplateManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.ErrorIds;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.pay.PayModule;
import com.mi.game.module.pay.data.PayData;
import com.mi.game.module.pay.pojo.PayOrderEntity;
import com.mi.game.module.pay.protocol.PayProtocol;

/**
 * 创建支付订单
 * 
 * @author 李强 <br/>
 *
 */
@HandlerType(type = HandlerIds.PAY_CREATE_ORDER, order = 2)
public class PayCreateOrderHandler extends BaseHandler {

	@Override
	public void execute(IOMessage ioMessage) {
		PayProtocol protocol = new PayProtocol();
		ioMessage.setProtocol(protocol);

		String playerID = ioMessage.getPlayerId();
		// 充值类型
		int type = 0;
		if (ioMessage.getInputParse("t") != null) {
			type = Integer.parseInt(ioMessage.getInputParse("t").toString());
		}
		
		// 其他
		String parse = null;
		if (ioMessage.getInputParse("parse") != null) {
			parse = ioMessage.getInputParse("parse").toString();
		}

		PayData payData = TemplateManager.getTemplateData(type, PayData.class);
		if (payData == null) {
			protocol.setCode(ErrorIds.PAY_TYPE_ERROR);
			return;
		}

		PayModule payModule = ModuleManager.getModule(ModuleNames.PayModule, PayModule.class);

		PayOrderEntity orderEntity = payModule.createOrder(playerID, type, payData.getRmb(), parse);

		protocol.setOrderEntity(orderEntity);

		ioMessage.setOutputResult(protocol);

	}
}
