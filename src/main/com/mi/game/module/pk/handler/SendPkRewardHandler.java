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
 * 计算比武排名,发放奖励
 * 
 * @author 赵鹏翔
 * @time Mar 27, 2015 12:30:58 PM
 */
@HandlerType(type = HandlerIds.PK_SEND_REWARD)
public class SendPkRewardHandler extends BaseHandler {
	private PkEntityDao pkEntityDao = PkEntityDao.getInstance();

	@Override
	public void execute(IOMessage ioMessage){
		BaseProtocol protocol = new BaseProtocol();
		PkModule pkModule = ModuleManager.getModule(ModuleNames.PkModule,
				PkModule.class);
		pkModule.sendPkRewardNew();
		protocol.setCode(0);
		ioMessage.setProtocol(protocol);
	}
}
