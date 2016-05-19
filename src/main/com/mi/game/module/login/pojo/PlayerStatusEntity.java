package com.mi.game.module.login.pojo;

import java.util.HashMap;
import java.util.Map;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.utils.IndexDirection;
import com.mi.core.pojo.BaseEntity;

@Entity(noClassnameStored = true)
public class PlayerStatusEntity extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4950004790970574563L;
	@Indexed(value = IndexDirection.ASC, unique = true)
	private String playerID;
	private int reward;
	private int friend;
	private int invite;
	private int news;
	private int task;
	private int festivalID;
	private int expStatus = 1;

	@Override
	public Map<String, Object> responseMap() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("reward", reward);
		data.put("news", news);
		data.put("invite", invite);
		data.put("friend", friend);
		data.put("task", task);
		data.put("festivalID", festivalID);
		data.put("expStatus", expStatus);
		return data;
	}

	public int getTask() {
		return task;
	}

	public void setTask(int task) {
		this.task = task;
	}

	public int getFriend() {
		return friend;
	}

	public void setFriend(int friend) {
		this.friend = friend;
	}

	public int getInvite() {
		return invite;
	}

	public void setInvite(int invite) {
		this.invite = invite;
	}

	public int getReward() {
		return reward;
	}

	public void setReward(int reward) {
		this.reward = reward;
	}

	public int getNews() {
		return news;
	}

	public void setNews(int news) {
		this.news = news;
	}

	public int getFestivalID() {
		return festivalID;
	}

	public void setFestivalID(int festivalID) {
		this.festivalID = festivalID;
	}

	public int getExpStatus() {
		return expStatus;
	}

	public void setExpStatus(int expStatus) {
		this.expStatus = expStatus;
	}

	@Override
	public Object getKey() {
		// TODO 自动生成的方法存根
		return playerID;
	}

	@Override
	public String getKeyName() {
		// TODO 自动生成的方法存根
		return "playerID";
	}

	@Override
	public void setKey(Object key) {
		// TODO 自动生成的方法存根
		playerID = key.toString();
	}

}
