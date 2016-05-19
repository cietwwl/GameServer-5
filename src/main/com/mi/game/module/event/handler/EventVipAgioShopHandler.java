package com.mi.game.module.event.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.event.EventModule;

/**
 * vip折扣商店
 * 
 * @author 赵鹏翔
 * @time Apr 7, 2015 10:20:58 AM
 */
@HandlerType(type = HandlerIds.EVENT_VIP_DIGO_SHOP)
public class EventVipAgioShopHandler extends BaseHandler {

	@Override
	public void execute(IOMessage ioMessage) {
		String playerID = ioMessage.getPlayerId();
		String shopType = (String) ioMessage.getInputParse("shopType");
		EventModule eventModule = ModuleManager.getModule(ModuleNames.EventModule, EventModule.class);
		eventModule.vipAgioShop(playerID, shopType, ioMessage);
	}
}
