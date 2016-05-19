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
 * 每周日定时重置比武积分等信息
 * 
 * @author 赵鹏翔
 *
 */
@HandlerType(type = HandlerIds.PK_CLEAR_POINTS)
public class ClearPkPointsHandler extends BaseHandler {
	private PkEntityDao pkEntityDao = PkEntityDao.getInstance();

	@Override
	public void execute(IOMessage ioMessage){
		BaseProtocol protocol = new BaseProtocol();
		// pkEntityDao.clearPoints();
		PkModule pkModule = ModuleManager.getModule(ModuleNames.PkModule,
				PkModule.class);
		pkModule.clearPoints();
		protocol.setCode(0);
		ioMessage.setProtocol(protocol);
	}
}
