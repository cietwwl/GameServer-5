package com.mi.game.module.legion.data;

import java.util.Map;

import com.mi.core.engine.annotation.XmlTemplate;

@XmlTemplate(template = { "com/mi/template/LegionBuildPrototype.xml" })
public class LegionBuildData extends LegionBaseData {

	// 建筑类型,1为军团大厅,2为军团商店,3为军团关公殿
	private int type;
	// 建筑等级
	private int buildLevel;
	// 升级条件,0表示没有要求
	private int upgradeLimit;
	// 升级需求
	private Map<Integer, Integer> upgradeRequire;
	// 下一级id
	private int nextID;
	// 当前等级最大人数
	private int maxMember;

	public int getMaxMember() {
		return maxMember;
	}

	public void setMaxMember(int maxMember) {
		this.maxMember = maxMember;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getBuildLevel() {
		return buildLevel;
	}

	public void setBuildLevel(int buildLevel) {
		this.buildLevel = buildLevel;
	}

	public int getUpgradeLimit() {
		return upgradeLimit;
	}

	public void setUpgradeLimit(int upgradeLimit) {
		this.upgradeLimit = upgradeLimit;
	}

	public Map<Integer, Integer> getUpgradeRequire() {
		return upgradeRequire;
	}

	public void setUpgradeRequire(String str) {
		this.upgradeRequire = setMultiple(str);
	}

	public int getNextID() {
		return nextID;
	}

	public void setNextID(int nextID) {
		this.nextID = nextID;
	}

	public Integer getValue(Integer key) {
		return upgradeRequire.get(key);
	}

	@Override
	public String toString() {
		return "LegionBuildData [type=" + type + ", buildLevel=" + buildLevel + ", upgradeLimit=" + upgradeLimit + ", upgradeRequire=" + upgradeRequire + ", nextID=" + nextID + "]";
	}

}
