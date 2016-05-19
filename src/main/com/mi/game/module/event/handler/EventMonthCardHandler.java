package com.mi.game.module.event.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.TemplateManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.core.util.DateTimeUtil;
import com.mi.game.defines.ErrorIds;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.event.pojo.EventMonthCardEntity;
import com.mi.game.module.event.protocol.EventMonthCardProtocol;
import com.mi.game.module.pay.PayModule;
import com.mi.game.module.pay.data.PayData;
import com.mi.game.module.pay.pojo.PayOrderEntity;
import com.mi.game.util.CommonMethod;

@HandlerType(type = HandlerIds.EVENT_MONTH_CARD_BUY)
public class EventMonthCardHandler extends BaseHandler {

	@Override
	public void execute(IOMessage ioMessage) {
		EventMonthCardProtocol protocol = new EventMonthCardProtocol();
		ioMessage.setProtocol(protocol);
		String playerID = ioMessage.getPlayerId();

		EventMonthCardEntity eventMonthCardEntity = CommonMethod.getEntity(playerID, true, ioMessage, EventMonthCardEntity.class);

		if (eventMonthCardEntity.getExpireTime() > System.currentTimeMillis()) { // 如果当前月卡有效
			int days = DateTimeUtil.getDiffDay(eventMonthCardEntity.getExpireTime(), System.currentTimeMillis()) + 1;
			// 只有最后5天可以购买
			if (days >= 5) {
				protocol.setCode(ErrorIds.MONTH_CARD_CANNOT_BUY);
			}
		}

		if (protocol.getCode() == 0) {
			PayModule payModule = ModuleManager.getModule(ModuleNames.PayModule, PayModule.class);
			// 月卡购买type
			int type = 10808;
			PayData payData = TemplateManager.getTemplateData(type, PayData.class);
			if (payData == null) {
				protocol.setCode(ErrorIds.PAY_TYPE_ERROR);
				return;
			}
			// 创建月卡订单
			PayOrderEntity orderEntity = payModule.createOrder(playerID, type, payData.getRmb(), "monthCard");
			protocol.setOrderEntity(orderEntity);
		}

		ioMessage.setOutputResult(protocol);
	}
}
