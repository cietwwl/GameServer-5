package com.mi.game.module.robot;

import com.mi.core.dao.KeyGeneratorDAO;
import com.mi.core.engine.annotation.Module;
import com.mi.core.pojo.KeyGenerator;
import com.mi.game.defines.ModuleNames;
import com.mi.game.defines.SysConstants;
import com.mi.game.module.base.BaseModule;

@Module(name = ModuleNames.RobotModule,clazz = RobotModule.class)
public class RobotModule extends BaseModule{
	private final KeyGeneratorDAO keyGeneratorDAO = KeyGeneratorDAO.getInstance();
	
	public void init(){
		
	}
	
	public void initRotbotID(){
		String clsName = SysConstants.robotIDEntity;
		KeyGenerator keyGenerator = keyGeneratorDAO.getEntity(clsName);
		if(keyGenerator == null){
			keyGenerator = new KeyGenerator();
			keyGenerator.setKey(clsName);
			keyGenerator.setNextId(SysConstants.heroStartID);
			keyGeneratorDAO.save(keyGenerator);
		}
	}
	
	public long getRobotID(){
		String clsName = SysConstants.robotIDEntity;
		long robotID = keyGeneratorDAO.updateInc(clsName);
		return robotID;
	}
}
