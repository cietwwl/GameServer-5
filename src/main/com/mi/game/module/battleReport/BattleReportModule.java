package com.mi.game.module.battleReport;

import com.mi.core.dao.KeyGeneratorDAO;
import com.mi.core.engine.annotation.Module;
import com.mi.core.pojo.KeyGenerator;
import com.mi.game.defines.ErrorIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.defines.SysConstants;
import com.mi.game.module.base.BaseModule;
import com.mi.game.module.battleReport.dao.BattleReportEntityDAO;
import com.mi.game.module.battleReport.pojo.BattleReportEntity;
@Module(name = ModuleNames.BattleReportModule,clazz = BattleReportModule.class)
public class BattleReportModule extends BaseModule{
	private final KeyGeneratorDAO keyGeneratorDAO = KeyGeneratorDAO.getInstance();
	private final BattleReportEntityDAO battleReportEntityDAO = BattleReportEntityDAO.getInstance();
	@Override
	public void init(){
		String clsName = SysConstants.reportIDEntity;
		KeyGenerator keyGenerator = keyGeneratorDAO.getEntity(clsName);
		if (keyGenerator == null) {
			keyGenerator = new KeyGenerator();
			keyGenerator.setKey(clsName);
			keyGenerator.setNextId(SysConstants.reportStartID);
			keyGeneratorDAO.save(keyGenerator);
		}
	}
	
	public long getReportID(){
		String clsName = SysConstants.reportIDEntity;
		long reportID = keyGeneratorDAO.updateInc(clsName);
		return reportID;
	}
	
	public void saveReportEntity(BattleReportEntity entity){
		battleReportEntityDAO.save(entity);
	}
	
	public BattleReportEntity getReportEntity(String reportID){
		BattleReportEntity entity = battleReportEntityDAO.getEntity(reportID);
		if(entity == null){
			logger.error("战报实体为空");
			throw new IllegalArgumentException(ErrorIds.NoEntity + "");
		}
		return entity;
	}
	
	public long saveReport(String report){
		long reportID = this.getReportID();
		BattleReportEntity entity = new BattleReportEntity();
		entity.setKey(reportID + "");
		entity.setBattleString(report);
		this.saveReportEntity(entity);
		return reportID;
	}
}
