package com.mi.game.module.pk.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.login.dao.PlayerEntitiyDAO;
import com.mi.game.module.pk.PkModule;
import com.mi.game.module.pk.dao.PkEntityDao;
import com.mi.game.module.pk.protocol.PkProtocol;

/**
 * 兑换奖品
 * 
 * @author 赵鹏翔
 *
 */
@HandlerType(type = HandlerIds.PK_EXCHANGE_PRIZE)
public class PkExchangePrizeHandler extends BaseHandler {
	private PlayerEntitiyDAO playerEntitiyDAO = PlayerEntitiyDAO.getInstance();
	private PkEntityDao pkEntityDao = PkEntityDao.getInstance();

	@Override
	public void execute(IOMessage ioMessage){
		PkProtocol protocol = new PkProtocol();
		String playerID = "";
		int pid = 0; // 兑换的商品id
		int num = 0; // 兑换的商品数量
		if (ioMessage.getInputParse("playerID") != null) {
			playerID = ioMessage.getInputParse("playerID").toString();
		}

		if (ioMessage.getInputParse("pid") != null) {
			pid = Integer.valueOf(ioMessage.getInputParse("pid").toString());
		}

		if (ioMessage.getInputParse("num") != null) {
			num = Integer.valueOf(ioMessage.getInputParse("num")
					.toString());
		}

		PkModule pkModule = ModuleManager.getModule(ModuleNames.PkModule,
				PkModule.class);
		boolean flag = pkModule.exchangePrize(playerID, pid, num, protocol,
				ioMessage);
		if (!flag) {
			ioMessage.setProtocol(protocol);
			return;
		}
		ioMessage.setProtocol(protocol);
	}
}
