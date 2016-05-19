package com.mi.game.module.legion.pojo;

import java.util.ArrayList;
import java.util.List;

public class LegionPlayer {

	// 军团长
	private String first;
	// 副军团长
	private String second;
	// 成员列表
	private List<String> players;
	// 最大人数
	private int maxNum;

	/**
	 * 增加军团成员
	 * 
	 * @param playerID
	 */
	public void addPlayer(String playerID) {
		if (players == null) {
			players = new ArrayList<String>();
		}
		players.add(playerID);
	}

	/**
	 * 检查是否有更改军团宣言,公告权限
	 * 
	 * @param playerID
	 * @return
	 */
	public boolean isUpdateManage(String playerID) {
		if (playerID.equals(first)) {
			return true;
		}
		if (playerID.equals(second)) {
			return true;
		}
		return false;
	}

	/**
	 * 删除军团成员
	 * 
	 * @param playerID
	 */
	public void removePlayer(String playerID) {
		players.remove(playerID);
	}

	public String getFirst() {
		return first;
	}

	public void setFirst(String first) {
		this.first = first;
	}

	public String getSecond() {
		return second;
	}

	public void setSecond(String second) {
		this.second = second;
	}

	public List<String> getPlayers() {
		return players;
	}

	public void setPlayers(List<String> players) {
		this.players = players;
	}

	public int getMaxNum() {
		return maxNum;
	}

	public void setMaxNum(int maxNum) {
		this.maxNum = maxNum;
	}

}
