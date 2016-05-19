package com.mi.game.module.pk.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.login.dao.PlayerEntitiyDAO;
import com.mi.game.module.login.pojo.PlayerEntity;
import com.mi.game.module.pk.PkModule;
import com.mi.game.module.pk.dao.PkEntityDao;
import com.mi.game.module.pk.protocol.PkProtocol;
import com.mi.game.util.Utilities;

/**
 * 寻找比武对手
 * 
 * @author 赵鹏翔
 *
 */
@HandlerType(type = HandlerIds.PK_FIND_PLAYER)
public class PkFindPkPlayersHandler extends BaseHandler {
	private PlayerEntitiyDAO playerEntitiyDAO = PlayerEntitiyDAO.getInstance();
	private PkEntityDao pkEntityDao = PkEntityDao.getInstance();

	@Override
	public void execute(IOMessage ioMessage){
		PkProtocol pkProtocol = new PkProtocol();
		String playerID = "";
		if (ioMessage.getInputParse("playerID") != null) {
			playerID = ioMessage.getInputParse("playerID").toString();
		}
		PlayerEntity playerEntity = playerEntitiyDAO.getEntity(playerID);
		if (playerEntity == null) {
			pkProtocol.setCode(0);
			ioMessage.setProtocol(pkProtocol);
			return;
		}
		PkModule pkModule = ModuleManager.getModule(ModuleNames.PkModule,
				PkModule.class);
		String today = Utilities.getDateTime("yyyyMMdd");
		// 寻找对手前,先初始化可参加比武人员数据
		// pkModule.initPkPlayers(today);
		// 寻找比武对手
		pkModule.findPkPlayers(playerID, pkProtocol);
		ioMessage.setProtocol(pkProtocol);


	}
}
