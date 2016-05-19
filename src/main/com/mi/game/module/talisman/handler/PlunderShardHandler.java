package com.mi.game.module.talisman.handler;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.talisman.TalismanModule;
import com.mi.game.module.talisman.protocol.TalismanProtocol;
@HandlerType(type = HandlerIds.DoPlunderTalisman)
public class PlunderShardHandler extends BaseHandler{
		@Override
		public void execute(IOMessage ioMessage){
			String playerID = ioMessage.getPlayerId();
			int shardID = 0;
			String plunderID = "";
			boolean win = false;
			String battleString = "";
			if(ioMessage.getInputParse("shardID") != null){
				shardID = Integer.parseInt(ioMessage.getInputParse("shardID").toString());
			}
			if(ioMessage.getInputParse("plunderID") != null){
				plunderID = ioMessage.getInputParse("plunderID").toString();
			}
			if(ioMessage.getInputParse("win") != null){
				win  = Boolean.parseBoolean(ioMessage.getInputParse("win").toString());
			}
			if(ioMessage.getInputParse("battleString") != null){
				battleString = ioMessage.getInputParse("battleString").toString();
			}
			TalismanProtocol protocol = new TalismanProtocol(); 
			TalismanModule module = ModuleManager.getModule(ModuleNames.TalismanModule,TalismanModule.class);
			try{
				module.plunderShard(playerID, shardID, plunderID, win,battleString ,ioMessage, protocol);
			}catch(IllegalArgumentException illegalEx){
				protocol.setCode(Integer.parseInt(illegalEx.getMessage()));
			}
			
			ioMessage.setProtocol(protocol);
		}
}
