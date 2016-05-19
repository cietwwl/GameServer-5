package com.mi.game.module.battleReport.dao;

import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.battleReport.pojo.BattleReportEntity;

public class BattleReportEntityDAO extends AbstractBaseDAO<BattleReportEntity>{
	private final static BattleReportEntityDAO BATTLE_REPORT_ENTITY_DAO = new BattleReportEntityDAO();
	private BattleReportEntityDAO(){}
	public static BattleReportEntityDAO getInstance(){
		return BATTLE_REPORT_ENTITY_DAO;
	}
}
