package com.mi.game.module.legion.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.legion.LegionModule;
import com.mi.game.module.legion.protocol.LegionProtocol;

@HandlerType(type = HandlerIds.LEGION_SHOP)
public class LegionShopHandler extends BaseHandler {

	@Override
	public void execute(IOMessage ioMessage) {
		// 玩家id
		String playerID = ioMessage.getPlayerId();
		// 军团Id
		String legionID = (String) ioMessage.getInputParse("legionID");
		// 操作类型
		String legionType = (String) ioMessage.getInputParse("legionType");
		// 物品pid
		String itemID = (String) ioMessage.getInputParse("itemID");
		LegionModule legionModule = ModuleManager.getModule(ModuleNames.LegionModule, LegionModule.class);
		LegionProtocol protocol = new LegionProtocol();
		protocol.setPlayerID(ioMessage.getPlayerId());
		legionModule.legionShop(playerID, legionID, legionType, itemID, protocol);
		ioMessage.setOutputResult(protocol);
	}

}
