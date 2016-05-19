package com.mi.game.module.pk.pojo;

import java.util.List;

/**
 * 比武对手
 * 
 * @author Administrator
 *
 */
public class PkPlayer {
	private String playerID; // 玩家ID

	private int type; // 类型 1:弱势;2:平等;3:强势

	private String playerName; // 昵称

	private int level; // 等级

	private int vipLevel; // vip等级

	private String groupName; // 军团名称

	private String groupID; // 军团ID

	private long rankNum; // 积分排名

	private int points; // 积分

	private int num; // pk次数

	private int energy; // 耐力值

	private List<Integer> teamList; // 默认阵容

	public String getGroupID() {
		return groupID;
	}

	public void setGroupID(String groupID) {
		this.groupID = groupID;
	}

	public int getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(int vipLevel) {
		this.vipLevel = vipLevel;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public List<Integer> getTeamList() {
		return teamList;
	}

	public void setTeamList(List<Integer> teamList) {
		this.teamList = teamList;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getEnergy() {
		return energy;
	}

	public void setEnergy(int energy) {
		this.energy = energy;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public int getFightValue() {
		return fightValue;
	}

	public void setFightValue(int fightValue) {
		this.fightValue = fightValue;
	}

	private int fightValue; // 战斗力

	public long getRankNum() {
		return rankNum;
	}

	public void setRankNum(long rankNum) {
		this.rankNum = rankNum;
	}

	public String getPlayerID() {
		return playerID;
	}

	public void setPlayerID(String playerID) {
		this.playerID = playerID;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
}
