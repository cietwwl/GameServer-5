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
 * 添加比武记录
 * 
 * @author 赵鹏翔
 *
 */
@HandlerType(type = HandlerIds.PK_CREATE_INFO)
public class CreatePkInfoHandler extends BaseHandler {
	private PlayerEntitiyDAO playerEntitiyDAO = PlayerEntitiyDAO.getInstance();
	private PkEntityDao pkEntityDao = PkEntityDao.getInstance();

	@Override
	public void execute(IOMessage ioMessage){
		PkProtocol pkProtocol = new PkProtocol();
		String playerID = "";
		String pkPlayerID = "";
		int result = 0;
		if (ioMessage.getInputParse("playerID") != null) {
			playerID = ioMessage.getInputParse("playerID").toString();
		}

		if (ioMessage.getInputParse("pkPlayerID") != null) {
			pkPlayerID = ioMessage.getInputParse("pkPlayerID").toString();
		}

		if (ioMessage.getInputParse("result") != null) {
			result = Integer.valueOf(ioMessage.getInputParse("result")
					.toString());
		}

		String battleString = ""; // 战斗过程
		if (ioMessage.getInputParse("battleString") != null) {
			battleString = ioMessage.getInputParse("battleString").toString();
		}

		PkModule pkModule = ModuleManager.getModule(ModuleNames.PkModule,
				PkModule.class);

		int updatePoint = pkModule.getPkAddPoint(playerID, pkPlayerID); // 得到比武应该添加的分数
		if (result == 1) { // 比武失败
			updatePoint = updatePoint * -1;
		}
		pkModule.creatPkEntity(playerID, pkPlayerID, result, updatePoint,
				pkProtocol, battleString, ioMessage);
		ioMessage.setProtocol(pkProtocol);

	}
}
