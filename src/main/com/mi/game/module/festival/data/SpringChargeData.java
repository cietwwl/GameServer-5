package com.mi.game.module.festival.data;

import com.mi.core.engine.annotation.XmlTemplate;
import com.mi.core.template.BaseTemplate;

@XmlTemplate(template = {
	"com/mi/template/SpringChargePrototype.xml"
})
public class SpringChargeData extends BaseTemplate {

	// 充值元宝数
	private int charge;
	// 赠送元宝数
	private int gold;

	public int getCharge() {
		return charge;
	}

	public void setCharge(int charge) {
		this.charge = charge;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

}
