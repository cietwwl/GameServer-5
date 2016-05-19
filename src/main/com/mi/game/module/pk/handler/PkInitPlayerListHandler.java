package com.mi.game.module.pk.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.core.protocol.BaseProtocol;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.pk.PkModule;
import com.mi.game.util.Utilities;

/**
 * 初始化可以参加比武的人员列表
 * 
 * @author 赵鹏翔
 *
 */
@HandlerType(type = HandlerIds.PK_INIT_PLAYER)
public class PkInitPlayerListHandler extends BaseHandler {
	@Override
	public void execute(IOMessage ioMessage){

		PkModule pkModule = ModuleManager.getModule(ModuleNames.PkModule,
				PkModule.class);
		// 得到今天日期字符串
		String today = Utilities.getDateTime("yyyyMMdd");
		pkModule.initPkPlayers(today);
		BaseProtocol protocol = new BaseProtocol();
		ioMessage.setProtocol(protocol);
	}
}
