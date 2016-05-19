package com.mi.game.module.worldBoss.data;

import com.mi.core.engine.annotation.XmlTemplate;
import com.mi.core.template.BaseTemplate;
@XmlTemplate(template = {"com/mi/template/WorldBossPrototype.xml"})
public class WorldBossData extends BaseTemplate{
	private String startTime;
	private long baseHP;
	private long levelHP;
	private long activeTime;
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public long getBaseHP() {
		return baseHP;
	}
	public void setBaseHP(long baseHP) {
		this.baseHP = baseHP;
	}
	public long getLevelHP() {
		return levelHP;
	}
	public void setLevelHP(long levelHP) {
		this.levelHP = levelHP;
	}
	public long getActiveTime() {
		return activeTime;
	}
	public void setActiveTime(long activeTime) {
		this.activeTime = activeTime;
	}
	
	
}
