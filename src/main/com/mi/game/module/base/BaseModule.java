package com.mi.game.module.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mi.game.defines.ModuleNames;
import com.mi.core.engine.IModule;
import com.mi.core.engine.annotation.Module;

@Module(name=ModuleNames.BaseModule,clazz=BaseModule.class)
public class BaseModule implements IModule {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}
}
