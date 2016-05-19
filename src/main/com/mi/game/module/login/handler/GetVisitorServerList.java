package com.mi.game.module.login.handler;

import java.util.List;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.ErrorIds;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.login.LoginModule;
import com.mi.game.module.login.dao.LoginInfoDAO;
import com.mi.game.module.login.pojo.LoginInfoEntity;
import com.mi.game.module.login.pojo.ServerInfoEntity;
import com.mi.game.module.login.protocol.LoginInfoProtocol;
@HandlerType(type = HandlerIds.getVisitorServerList)
public class GetVisitorServerList extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String phoneID = null;
		if (ioMessage.getInputParse("phoneID") != null) {
			phoneID = ioMessage.getInputParse("phoneID").toString();
		}
		LoginInfoProtocol protocol = new LoginInfoProtocol();
		if (phoneID == null || phoneID.isEmpty()) {
			throw new IllegalArgumentException(ErrorIds.ParamWrong + "");
		}
		LoginInfoEntity loginInfoEntity = LoginInfoDAO.getInstance().getEntityByVisitorID(phoneID);
		if (loginInfoEntity == null) {
			protocol.setCode(ErrorIds.UserNoRegister);
		} else {
			LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule, LoginModule.class);
			List<ServerInfoEntity> serverList = loginModule.getServerList();
			protocol.setStatus(loginModule.getStatus());
			protocol.setServerList(serverList);
			protocol.setBind(loginInfoEntity.isBind());
			protocol.setPlayerID(loginInfoEntity.getKey().toString());
		}
		ioMessage.setProtocol(protocol);
	}
}
