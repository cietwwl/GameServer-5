package com.mi.game.module.event.handler;

import java.util.HashMap;
import java.util.Map;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.event.EventModule;
import com.mi.game.module.event.pojo.EventMonthCardEntity;
import com.mi.game.module.event.protocol.EventMonthCardProtocol;
import com.mi.game.module.reward.data.GoodsBean;

/**
 * 领取月卡奖励
 * @author Administrator
 *
 */
@HandlerType(type = HandlerIds.EVENT_MONTH_CARD_REWARD)
public class EventMonthCardRewardHandler extends BaseHandler {

	@Override
	public void execute(IOMessage ioMessage) {
		EventMonthCardProtocol protocol = new EventMonthCardProtocol();
		ioMessage.setProtocol(protocol);

		String playerID = ioMessage.getPlayerId();
		EventModule eventModule = ModuleManager.getModule(ModuleNames.EventModule, EventModule.class);

		Map<String, Object> itemMap = new HashMap<String, Object>();
		Map<String, GoodsBean> showMap = new HashMap<String, GoodsBean>();
		
		EventMonthCardEntity eventMonthCardEntity = eventModule.eventMonthCardReward(playerID, ioMessage, showMap, itemMap);
		
		if (protocol.getCode() == 0) {
			protocol.setEventMonthCardEntity(eventMonthCardEntity);
		}
		
		protocol.setItemMap(itemMap);
		protocol.setShowMap(showMap);
		ioMessage.setOutputResult(protocol);
	}

}
