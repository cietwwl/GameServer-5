package com.mi.game.module.manual.data;

import com.mi.core.engine.annotation.XmlTemplate;
import com.mi.core.template.BaseTemplate;
@XmlTemplate(template = {"com/mi/template/LikeabilitySuccessPrototype.xml"})
public class ManualAchievementData extends BaseTemplate{
	private int needLikeability;
	private int stamina;
	
	public int getNeedLikeability() {
		return needLikeability;
	}
	public void setNeedLikeability(int needLikeability) {
		this.needLikeability = needLikeability;
	}
	public int getStamina() {
		return stamina;
	}
	public void setStamina(String stamina) {
		if(stamina != null){
			String[] strArr= stamina.split("=");
			this.stamina = Integer.parseInt(strArr[1]);
		}	
	}
	
	
}
