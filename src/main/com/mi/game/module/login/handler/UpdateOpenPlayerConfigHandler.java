package com.mi.game.module.login.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.core.protocol.BaseProtocol;
import com.mi.core.util.ConfigUtil;
import com.mi.game.defines.HandlerIds;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.login.LoginModule;
@HandlerType(type = HandlerIds.updateOpenPlayerList)
public class UpdateOpenPlayerConfigHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		ConfigUtil.init();
		LoginModule.openPlayerIDList = ConfigUtil.getStringArray("openPlayerID");
		String[] list = LoginModule.openPlayerIDList;
		for(String str : list){
			System.out.println(str);
		}
		ioMessage.setProtocol(new BaseProtocol());
	}
}
