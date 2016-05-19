package com.mi.game.module.battleReport.pojo;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.utils.IndexDirection;
import com.mi.core.pojo.BaseEntity;
@Entity(noClassnameStored = true)
public class BattleReportEntity extends BaseEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4061297367334163667L;
	@Indexed(unique=true,value=IndexDirection.ASC)
	private String reportID;
	private String battleString ;
	
	
	public String getBattleString() {
		return battleString;
	}

	public void setBattleString(String battleString) {
		this.battleString = battleString;
	}

	@Override
	public Object getKey() {
		// TODO 自动生成的方法存根
		return reportID;
	}

	@Override
	public String getKeyName() {
		// TODO 自动生成的方法存根
		return "reportID";
	}

	@Override
	public void setKey(Object key) {
		// TODO 自动生成的方法存根
		reportID = key.toString();
	}
	
}
