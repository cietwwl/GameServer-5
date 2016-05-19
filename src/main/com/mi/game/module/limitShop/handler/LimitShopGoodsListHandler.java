package com.mi.game.module.limitShop.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.limitShop.LimitShopModule;
import com.mi.game.module.limitShop.protocol.LimitShopProtocol;

/**
 * 获取商店商品列表
 * 
 * @author 赵鹏翔
 *
 */
@HandlerType(type = HandlerIds.EVENT_LIMIT_SHOP_GOODS_LIST)
public class LimitShopGoodsListHandler extends BaseHandler {

	@Override
	public void execute(IOMessage ioMessage){
		LimitShopProtocol protocol = new LimitShopProtocol();
		String playerID = ioMessage.getPlayerId();
		LimitShopModule limitShopModule = ModuleManager.getModule(
				ModuleNames.LimitShopModule, LimitShopModule.class);
		limitShopModule.getShopGoodsList(playerID, protocol);
		ioMessage.setProtocol(protocol);
	}
}
