package com.mi.game.module.worldBoss.data;

import com.mi.core.engine.annotation.XmlTemplate;
import com.mi.core.template.BaseTemplate;
@XmlTemplate(template = {"com/mi/template/WorldBossJob.xml"})
public class WorldBossJobData extends BaseTemplate{
	private String cls;
	private String startTime;
	/**
	 * 间隔
	 */
	private int interval = 0;
	/**
	 * 数量
	 */
	private int count = 0;
	
	
	
	
	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getCls() {
		return cls;
	}

	public void setCls(String cls) {
		this.cls = cls;
	}
	
}
