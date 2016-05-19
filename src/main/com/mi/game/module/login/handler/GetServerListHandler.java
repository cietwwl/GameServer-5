package com.mi.game.module.login.handler;

import java.util.List;

import com.mi.game.defines.ErrorIds;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.clearHero.ClearHeroModule;
import com.mi.game.module.login.LoginModule;
import com.mi.game.module.login.dao.LoginInfoDAO;
import com.mi.game.module.login.pojo.LoginInfoEntity;
import com.mi.game.module.login.pojo.ServerInfoEntity;
import com.mi.game.module.login.protocol.LoginInfoProtocol;
import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;

@HandlerType(type = HandlerIds.GetServerList)
public class GetServerListHandler extends BaseHandler {
	@Override
	public void execute(IOMessage ioMessage) {
		String PlatFormID = null;
		if (ioMessage.getInputParse("PlatFormID") != null) {
			PlatFormID = ioMessage.getInputParse("PlatFormID").toString();
			PlatFormID = PlatFormID.toLowerCase();
		}
		LoginInfoProtocol protocol = new LoginInfoProtocol();
		if (PlatFormID == null) {
			logger.error("平台ID参数错误");
			throw new IllegalArgumentException(ErrorIds.ParamWrong + "");
		}
		LoginInfoEntity loginInfoEntity = LoginInfoDAO.getInstance().getEntityByName(PlatFormID);
		if (loginInfoEntity == null) {
			logger.error("用户未注册");
			protocol.setCode(ErrorIds.UserNoRegister);
		} else {
			LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule, LoginModule.class);
			List<ServerInfoEntity> serverList = loginModule.getServerList();
			int status = loginModule.getStatus();
			if(status == 1){
				boolean isOpen = false;
				for(String name : LoginModule.openPlayerIDList){
					if(name.equals(PlatFormID)){
						status = 0;
						isOpen = true;
						logger.error("在白名单中");
						break;
					}
				}
				if(!isOpen){
					String stopServerMessage = loginModule.getStopServerMessage();
					protocol.setStopServerMessage(stopServerMessage);
				}
			}
			
			for(String name : ClearHeroModule.getClearList()){
				String[] nameArr = name.split("-");
				String stopName = "";
				if(nameArr.length > 1){
					stopName = nameArr[1];
				}else{
					stopName = nameArr[0];
				}	
				if(stopName.equals(loginInfoEntity.getKey().toString())){
					status = 1; //停服状态	
					protocol.setStopServerMessage("帐号已封停，无法登陆。");
				}
			}
			
			protocol.setStatus(status);
			protocol.setServerList(serverList);
			protocol.setPlayerID(loginInfoEntity.getKey().toString());
		}
		ioMessage.setProtocol(protocol);
	}
}
