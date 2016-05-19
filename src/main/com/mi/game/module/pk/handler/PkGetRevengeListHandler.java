package com.mi.game.module.pk.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.pk.PkModule;
import com.mi.game.module.pk.dao.PkEntityDao;
import com.mi.game.module.pk.protocol.PkProtocol;

/**
 * 比武查询复仇列表
 * 
 * @author 赵鹏翔
 *
 */
@HandlerType(type = HandlerIds.PK_GET_REVENGE_INFO)
public class PkGetRevengeListHandler extends BaseHandler {
	private PkEntityDao pkEntityDao = PkEntityDao.getInstance();

	@Override
	public void execute(IOMessage ioMessage){
		PkProtocol protocol = new PkProtocol();
		String playerID = "";

		if (ioMessage.getInputParse("playerID") != null) {
			playerID = ioMessage.getInputParse("playerID").toString();
		}

		PkModule pkModule = ModuleManager.getModule(ModuleNames.PkModule,
				PkModule.class);
		pkModule.getRevengeInfo(playerID, protocol);
		protocol.setCode(0);
		ioMessage.setProtocol(protocol);


	}
}
