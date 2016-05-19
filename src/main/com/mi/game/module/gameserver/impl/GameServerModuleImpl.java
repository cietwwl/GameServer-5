package com.mi.game.module.gameserver.impl;

import com.mi.game.module.gameserver.GameServerModule;
import com.mi.game.module.gameserver.service.GameServer;



public class GameServerModuleImpl implements GameServerModule{
	@Override
	public void init() {
		GameServer server = new GameServer();
		server.start();
	}
}
