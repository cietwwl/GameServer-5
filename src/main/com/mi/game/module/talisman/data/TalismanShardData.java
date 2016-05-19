package com.mi.game.module.talisman.data;

import com.mi.core.engine.annotation.XmlTemplate;
import com.mi.core.template.BaseTemplate;
@XmlTemplate(template = {"com/mi/template/TalismanComposePrototype.xml"})
public class TalismanShardData extends BaseTemplate{
	private int PlayerRate;
	private int NPCRate;
	private int talismanID;
	private int type;
	private int postion;
	private int snatchPlayerRate;
	private int snatchNPCRate;
	private int isDefult;
	private int quality;
	
	public int getQuality() {
		return quality;
	}
	public void setQuality(int quality) {
		this.quality = quality;
	}
	public int getIsDefult() {
		return isDefult;
	}
	public void setIsDefult(int isDefult) {
		this.isDefult = isDefult;
	}
	public int getSnatchPlayerRate() {
		return snatchPlayerRate;
	}
	public void setSnatchPlayerRate(int snatchPlayerRate) {
		this.snatchPlayerRate = snatchPlayerRate;
	}
	public int getSnatchNPCRate() {
		return snatchNPCRate;
	}
	public void setSnatchNPCRate(int snatchNPCRate) {
		this.snatchNPCRate = snatchNPCRate;
	}
	public int getPostion() {
		return postion;
	}
	public void setPostion(int postion) {
		this.postion = postion;
	}
	public int getPlayerRate() {
		return PlayerRate;
	}
	public void setPlayerRate(int playerRate) {
		PlayerRate = playerRate;
	}
	public int getNPCRate() {
		return NPCRate;
	}
	public void setNPCRate(int nPCRate) {
		NPCRate = nPCRate;
	}
	public int getTalismanID() {
		return talismanID;
	}
	public void setTalismanID(int talismanID) {
		this.talismanID = talismanID;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	
}
