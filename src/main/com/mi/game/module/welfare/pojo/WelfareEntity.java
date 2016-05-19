package com.mi.game.module.welfare.pojo;

import java.util.ArrayList;
import java.util.List;

import com.google.code.morphia.annotations.Indexed;
import com.mi.core.pojo.BaseEntity;

public class WelfareEntity extends BaseEntity {

	private static final long serialVersionUID = -7958033211868466522L;

	// 玩家id
	@Indexed
	private String playerID;
	// 正在进行的福利活动
	private List<String> welfareList = new ArrayList<String>();
	// 时间
	private String dateTime;

	public String getPlayerID() {
		return playerID;
	}

	public void setPlayerID(String playerID) {
		this.playerID = playerID;
	}

	public List<String> getWelfareList() {
		return welfareList;
	}

	public void setWelfareList(List<String> welfareList) {
		this.welfareList = welfareList;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	/**
	 * 删除福利
	 * 
	 * @param key
	 */
	public void reduceWelfare(String key) {
		if (welfareList.contains(key)) {
			welfareList.remove(key);
		}
	}

	@Override
	public Object getKey() {
		return playerID;
	}

	@Override
	public String getKeyName() {
		return "playerID";
	}

	@Override
	public void setKey(Object key) {
		this.playerID = key.toString();
	}

}
