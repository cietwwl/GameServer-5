package com.mi.game.module.gameserver;

import com.mi.game.defines.ModuleNames;
import com.mi.game.module.gameserver.impl.GameServerModuleImpl;
import com.mi.core.engine.IModule;
import com.mi.core.engine.annotation.Module;

@Module(name=ModuleNames.GameServerModule,clazz=GameServerModuleImpl.class)
public interface GameServerModule extends IModule {

}
