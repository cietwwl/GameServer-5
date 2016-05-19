package com.mi.game.module.playerservice.handler;

import java.util.List;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.playerservice.PlayerServiceModule;
import com.mi.game.module.playerservice.pojo.SuggestEntity;
import com.mi.game.module.playerservice.protocol.SuggestProtocol;
@HandlerType(type = HandlerIds.getUnreadInfo)
public class GetUnreadSuggestListHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		int page = 0;
		PlayerServiceModule playerServiceModule = ModuleManager.getModule(ModuleNames.PlayerServiceModule,PlayerServiceModule.class);
		playerServiceModule.getUnreadMessage(1);
		SuggestProtocol protocol = new SuggestProtocol();
		List<SuggestEntity> suggestList = playerServiceModule.getUnreadEntity(page);
		protocol.setSuggestList(suggestList);
		ioMessage.setProtocol(protocol);
	}
}
