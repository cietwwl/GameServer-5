package com.mi.game.module.pk.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.core.protocol.BaseProtocol;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.pk.PkModule;
import com.mi.game.module.pk.dao.PkEntityDao;

/**
 * 发送比武申请,检查是否可以进行比武
 * 
 * @author 赵鹏翔
 * @time Mar 27, 2015 5:52:27 PM
 */
@HandlerType(type = HandlerIds.PK_SENK_PK_ASK)
public class SendPkAskHandler extends BaseHandler {
	private PkEntityDao pkEntityDao = PkEntityDao.getInstance();

	@Override
	public void execute(IOMessage ioMessage){
		String playerID = "";
		String pkPlayerID = "";
		if (ioMessage.getInputParse("playerID") != null) {
			playerID = ioMessage.getInputParse("playerID").toString();
		}
		if (ioMessage.getInputParse("pkPlayerID") != null) {
			pkPlayerID = ioMessage.getInputParse("pkPlayerID").toString();
		}

		BaseProtocol protocol = new BaseProtocol();
		PkModule pkModule = ModuleManager.getModule(ModuleNames.PkModule,
				PkModule.class);
		boolean flag = pkModule.checkCanPlayPk(playerID, pkPlayerID, protocol);
		ioMessage.setProtocol(protocol);
	}
}
