package com.mi.game.module.limitShop.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.limitShop.LimitShopModule;

/**
 * 购买商品
 * 
 * @author 赵鹏翔
 *
 */
@HandlerType(type = HandlerIds.EVENT_LIMIT_SHOP_BUY)
public class LimitShopBuyHandler extends BaseHandler {

	@Override
	public void execute(IOMessage ioMessage){
		LimitShopModule limitShopModule = ModuleManager.getModule(
				ModuleNames.LimitShopModule, LimitShopModule.class);
		String playerID = ioMessage.getPlayerId();
		int pid = 0;
		if (ioMessage.getInputParse("pid") != null) {
			pid = Integer.valueOf(ioMessage.getInputParse("pid").toString());
		}
		int num = 1;
		if (ioMessage.getInputParse("num") != null) {
			num = Integer.valueOf(ioMessage.getInputParse("num").toString());
		}
		// 购买商品
		limitShopModule.buyShop(playerID, pid, num, ioMessage);
	}
}
