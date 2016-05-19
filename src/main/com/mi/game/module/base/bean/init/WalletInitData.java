package com.mi.game.module.base.bean.init;

import com.mi.core.engine.annotation.XmlTemplate;
import com.mi.core.template.BaseTemplate;

@XmlTemplate(template = {"com/mi/template/init/walletInit.xml"})
public class WalletInitData extends BaseTemplate{
	private long gold;
	private long silver;
	private long heroSoul;
	private long jewelSoul;
	private long reputation;
	
	public long getReputation() {
		return reputation;
	}
	public void setReputation(long reputation) {
		this.reputation = reputation;
	}
	public long getHeroSoul() {
		return heroSoul;
	}
	public void setHeroSoul(long heroSoul) {
		this.heroSoul = heroSoul;
	}
	public long getJewelSoul() {
		return jewelSoul;
	}
	public void setJewelSoul(long jewelSoul) {
		this.jewelSoul = jewelSoul;
	}
	public long getGold() {
		return gold;
	}
	public void setGold(long gold) {
		this.gold = gold;
	}
	public long getSilver() {
		return silver;
	}
	public void setSilver(long silver) {
		this.silver = silver;
	}
	
}
