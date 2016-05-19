package com.mi.game.module.pk.data;

import com.mi.core.engine.annotation.XmlTemplate;
import com.mi.core.template.BaseTemplate;

/**
 * 比武添加积分规则
 * 
 * @author 赵鹏翔
 * @time Apr 14, 2015 5:43:10 PM
 */
@XmlTemplate(template = { "com/mi/template/TournamentScorePrototype.xml" })
public class PkScoreData extends BaseTemplate {
	private int pid;
	private int lower;
	private int normal;
	private int higher;

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public int getLower() {
		return lower;
	}

	public void setLower(int lower) {
		this.lower = lower;
	}

	public int getNormal() {
		return normal;
	}

	public void setNormal(int normal) {
		this.normal = normal;
	}

	public int getHigher() {
		return higher;
	}

	public void setHigher(int higher) {
		this.higher = higher;
	}

}
