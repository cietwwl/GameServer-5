package com.mi.game.module.talisman.handler;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.talisman.TalismanModule;
import com.mi.game.module.talisman.protocol.TalismanProtocol;

@HandlerType(type = HandlerIds.StrengTalisman)
public class StrengTalismanHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String playerID = ioMessage.getPlayerId();
		TalismanModule talismanModule = ModuleManager.getModule(ModuleNames.TalismanModule,TalismanModule.class);
		String talismanID = "-1";
		List<Object> talismanList = new ArrayList<>();
		if(ioMessage.getInputParse("talismanID") != null)
			talismanID = ioMessage.getInputParse("talismanID").toString();
		if(ioMessage.getInputParse("talismanList") != null)
			talismanList = JSON.parseArray(ioMessage.getInputParse("talismanList").toString());
		TalismanProtocol protocol = new TalismanProtocol();
		talismanModule.strengTalisman(playerID, talismanID, talismanList, protocol);
		ioMessage.setProtocol(protocol);
	}
}
