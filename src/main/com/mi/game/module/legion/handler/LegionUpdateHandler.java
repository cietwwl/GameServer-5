package com.mi.game.module.legion.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.legion.LegionModule;
import com.mi.game.module.legion.protocol.LegionProtocol;

@HandlerType(type = HandlerIds.LEGION_UPDATE)
public class LegionUpdateHandler extends BaseHandler {

	@Override
	public void execute(IOMessage ioMessage) {
		String playerID = ioMessage.getPlayerId();
		// 军团Id
		String legionID = (String) ioMessage.getInputParse("legionID");
		String legionType = (String) ioMessage.getInputParse("legionType");
		// 军团公告
		String notice = (String) ioMessage.getInputParse("notice");
		// 军团宣言
		String declaration = (String) ioMessage.getInputParse("declaration");
		// 军团密码
		String password = (String) ioMessage.getInputParse("password");
		// 新军团密码
		String newPassword = (String) ioMessage.getInputParse("newPassword");
		LegionModule legionModule = ModuleManager.getModule(ModuleNames.LegionModule, LegionModule.class);
		LegionProtocol protocol = new LegionProtocol();
		protocol.setPlayerID(ioMessage.getPlayerId());
		legionModule.updateLegion(playerID, legionID, legionType, notice, declaration, password, newPassword, protocol);
		ioMessage.setOutputResult(protocol);
	}
}
