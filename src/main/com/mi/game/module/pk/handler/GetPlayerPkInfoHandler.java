package com.mi.game.module.pk.handler;

import java.util.Date;

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
import com.mi.game.util.Utilities;

/**
 * 查询玩家比武积分排行信息
 * 
 * @author 赵鹏翔
 *
 */
@HandlerType(type = HandlerIds.PK_GET_PK_INFO)
public class GetPlayerPkInfoHandler extends BaseHandler {
	private PlayerEntitiyDAO playerEntitiyDAO = PlayerEntitiyDAO.getInstance();
	private PkEntityDao pkEntityDao = PkEntityDao.getInstance();

	@Override
	public void execute(IOMessage ioMessage){
		PkProtocol protocol = new PkProtocol();
		String playerID = "";
		if (ioMessage.getInputParse("playerID") != null) {
			playerID = ioMessage.getInputParse("playerID").toString();
		}
		String today = Utilities.getDateTime("yyyyMMdd");
		String weekNum = Utilities.getWeekNumOfYear(new Date());
		PkModule pkModule = ModuleManager.getModule(ModuleNames.PkModule,
				PkModule.class);
		pkModule.initPkPlayers(today);
		pkModule.getPlayerPkRankInfo(playerID, protocol, today);
		ioMessage.setProtocol(protocol);
	}
}
