package com.mi.game.module.friend.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.utils.IndexDirection;
import com.mi.core.pojo.BaseEntity;

@Entity(noClassnameStored = true)
public class FriendEntity extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6367054749737578952L;
	@Indexed(value = IndexDirection.ASC, unique = true)
	private String playerID;
	private Map<String,FriendState> friendList;
	private List<PresentInfo> presentList;
	private int presentNum;
	private long updateTime;
	private long index;
	private List<String> sendPresent;
	
	public List<String> getSendPresent() {
		if(sendPresent == null){
			sendPresent = new ArrayList<String>();
		}
		return sendPresent;
	}

	public void setSendPresent(List<String> sendPresent) {
		this.sendPresent = sendPresent;
	}

	public long addIndex(){
		index += 1;
		return index;
	}
	
	public long getIndex() {
		return index;
	}
	public void setIndex(long index) {
		this.index = index;
	}
	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public int getPresentNum() {
		return presentNum;
	}

	public void setPresentNum(int presentNum) {
		this.presentNum = presentNum;
	}

	public List<PresentInfo> getPresentList() {
		if(presentList == null){
			presentList = new ArrayList<>();
		}
		return presentList;
	}

	public void setPresentList(List<PresentInfo> presentList) {
		this.presentList = presentList;
	}

	public Map<String,FriendState> getFriendList() {
		if(friendList == null){
			friendList = new HashMap<>();
		}
		return friendList;
	}

	public void setFriendList(Map<String,FriendState> friendList) {
		this.friendList = friendList;
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
