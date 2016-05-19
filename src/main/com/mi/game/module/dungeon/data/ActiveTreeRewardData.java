package com.mi.game.module.dungeon.data;

import com.mi.core.engine.annotation.XmlTemplate;
import com.mi.core.template.BaseTemplate;
@XmlTemplate(template = {"com/mi/template/ActiveTreeRewardPrototype.xml"})
public class ActiveTreeRewardData extends BaseTemplate{
	private int damageRange;
	private int rewardTotal;
	
	public int getDamageRange() {
		return damageRange;
	}
	public void setDamageRange(int damageRange) {
		this.damageRange = damageRange;
	}
	public int getRewardTotal() {
		return rewardTotal;
	}
	public void setRewardTotal(int rewardTotal) {
		this.rewardTotal = rewardTotal;
	}
	
	
}
