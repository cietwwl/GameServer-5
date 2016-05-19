package com.mi.game.module.talisman.handler;

import java.util.List;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.talisman.TalismanModule;
import com.mi.game.module.talisman.pojo.TalismanShard;
import com.mi.game.module.talisman.protocol.TalismanProtocol;
@HandlerType(type = HandlerIds.getTalismanShardList)
public class GetTalismanShardHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		String playerID = ioMessage.getPlayerId();
		TalismanModule talismanModule = ModuleManager.getModule(ModuleNames.TalismanModule, TalismanModule.class);
		List<TalismanShard> talismanShardList = talismanModule.getTalismanShardList(playerID);
		TalismanProtocol protocol = new TalismanProtocol();
		protocol.setShardList(talismanShardList);
		ioMessage.setProtocol(protocol);
	}
}
