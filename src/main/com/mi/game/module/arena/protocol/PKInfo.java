package com.mi.game.module.arena.protocol;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.mi.game.module.equipment.pojo.Equipment;
import com.mi.game.module.lead.pojo.HeroPrototype;
import com.mi.game.module.manual.pojo.HeroManual;
import com.mi.game.module.talisman.pojo.TalismanEntity;

/**
 * @author 刘凯旋	
 *
 * 2014年12月3日 上午11:41:51
 */
public class PKInfo {
	private List<Map<String,Object>> heroList;
	private List<TalismanEntity> talismanList;
	private List<Equipment> equipmentList;
	private List<Long> teamList;
	private List<Long> troops;
	private int fightValue;
	private Collection< HeroPrototype> DestinyPrototype;
	private List< HeroManual> manualList;
	private int vipLevel;
	private String groupName;
	
	
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public int getVipLevel() {
		return vipLevel;
	}
	public void setVipLevel(int vipLevel) {
		this.vipLevel = vipLevel;
	}
	public List<HeroManual> getManualList() {
		return manualList;
	}
	public void setManualList(List<HeroManual> manualList) {
		this.manualList = manualList;
	}
	public Collection<HeroPrototype> getDestinyPrototype() {
		return DestinyPrototype;
	}
	public void setDestinyPrototype(Collection<HeroPrototype> destinyPrototype) {
		DestinyPrototype = destinyPrototype;
	}
	public int getFightValue() {
		return fightValue;
	}
	public void setFightValue(int fightValue) {
		this.fightValue = fightValue;
	}
	public List<Map<String,Object>> getHeroList() {
		return heroList;
	}
	public void setHeroList(List<Map<String,Object>> heroList) {
		this.heroList = heroList;
	}
	public List<TalismanEntity> getTalismanList() {
		return talismanList;
	}
	public void setTalismanList(List<TalismanEntity> talismanList) {
		this.talismanList = talismanList;
	}
	public List<Equipment> getEquipmentList() {
		return equipmentList;
	}
	public void setEquipmentList(List<Equipment> equipmentList) {
		this.equipmentList = equipmentList;
	}
	public List<Long> getTeamList() {
		return teamList;
	}
	public void setTeamList(List<Long> teamList) {
		this.teamList = teamList;
	}
	public List<Long> getTroops() {
		return troops;
	}
	public void setTroops(List<Long> troops) {
		this.troops = troops;
	}
	
	
}
