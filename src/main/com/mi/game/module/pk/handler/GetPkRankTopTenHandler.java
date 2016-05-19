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
 * 查询积分排行榜单(积分排行前十名)
 * 
 * @author 赵鹏翔
 * @time Mar 27, 2015 12:30:58 PM
 */
@HandlerType(type = HandlerIds.PK_RANK_TOP_TEN)
public class GetPkRankTopTenHandler extends BaseHandler {
	private PkEntityDao pkEntityDao = PkEntityDao.getInstance();

	@Override
	public void execute(IOMessage ioMessage){
		PkProtocol protocol = new PkProtocol();
		PkModule pkModule = ModuleManager.getModule(ModuleNames.PkModule,
				PkModule.class);
		pkModule.getPkPointTopTen(protocol);
		protocol.setCode(0);
		ioMessage.setProtocol(protocol);
	}
}
