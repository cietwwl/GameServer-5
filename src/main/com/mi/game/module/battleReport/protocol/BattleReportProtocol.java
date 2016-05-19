package com.mi.game.module.battleReport.protocol;

import com.mi.core.protocol.BaseProtocol;

public class BattleReportProtocol extends BaseProtocol{
	private String battleString;

	public String getBattleString() {
		return battleString;
	}

	public void setBattleString(String battleString) {
		this.battleString = battleString;
	}
	
}
