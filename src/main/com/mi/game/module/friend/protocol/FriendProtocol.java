package com.mi.game.module.friend.protocol;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mi.core.protocol.BaseProtocol;
import com.mi.game.defines.HandlerIds;
import com.mi.game.module.friend.pojo.FriendState;
import com.mi.game.module.friend.pojo.PresentInfo;
import com.mi.game.module.mailBox.pojo.MailInfo;
import com.mi.game.module.reward.data.GoodsBean;

public class FriendProtocol extends BaseProtocol{
	private List<FriendInfo> friendList;
	private MailInfo mailInfo;
	private FriendState friendState;
	private List<PresentInfo> presentList;
	private Map<String,Object> itemMap;
	private Map<String,GoodsBean> showMap;
	private int presentNum;
	private FriendInfo friendInfo;
	private List<Long> myPresentRemoveList;
	private int present;
	private int myAllAtkNum;
	private int friendAllAtkNum;
	private int friendAtkNum;
	private String friendID;

	@Override
	public Map<String,Object> responseMap(int y){
		Map<String,Object> data = new HashMap<String, Object>();
		switch(y){
		case HandlerIds.getFriendList:
			data.put("presentList", presentList);
			data.put("presentNum", presentNum);
			data.put("friendList", friendList);
			break;
		case HandlerIds.getRecommendList:
			data.put("friendList", friendList);
			break;
		case HandlerIds.addFriend:
			break;
		case HandlerIds.friendHandler:
			data.put("mailInfo",mailInfo);
			break;
		case HandlerIds.getAllFriendEnergy:
			data.put("itemMap", itemMap);
			data.put("presentList", presentList);
			data.put("presentNum", presentNum);
			if(showMap != null){
				data.put("showMap", showMap.values());
			}
			break;
		case HandlerIds.getFriendEnergy:
			data.put("itemMap", itemMap);
			data.put("presentNum", presentNum);
			if(showMap != null){
				data.put("showMap", showMap.values());
			}
			break;
		case HandlerIds.leaveFriendMessage:
			break;
		case HandlerIds.friendFight:
			data.put("friendAtkNum", friendAtkNum);
			data.put("myAllAtkNum", myAllAtkNum);
			data.put("friendAllAtkNum", friendAllAtkNum);
			data.put("friendID", friendID);
			break;
		case HandlerIds.breakFriendShip:
			data.put("myPresentRemoveList", myPresentRemoveList);
			break;
		case HandlerIds.findPlayerByName:
			data.put("friendList",friendList);
			break;
		}
		return data;
	}
	public Map<String, GoodsBean> getShowMap() {
		return showMap;
	}

	public void setShowMap(Map<String, GoodsBean> showMap) {
		this.showMap = showMap;
	}

	public String getFriendID() {
		return friendID;
	}

	public void setFriendID(String friendID) {
		this.friendID = friendID;
	}

	public int getMyAllAtkNum() {
		return myAllAtkNum;
	}

	public void setMyAllAtkNum(int myAllAtkNum) {
		this.myAllAtkNum = myAllAtkNum;
	}

	public int getFriendAllAtkNum() {
		return friendAllAtkNum;
	}

	public void setFriendAllAtkNum(int friendAllAtkNum) {
		this.friendAllAtkNum = friendAllAtkNum;
	}

	public int getFriendAtkNum() {
		return friendAtkNum;
	}

	public void setFriendAtkNum(int friendAtkNum) {
		this.friendAtkNum = friendAtkNum;
	}

	public int getPresent() {
		return present;
	}

	public void setPresent(int present) {
		this.present = present;
	}

	public List<Long> getMyPresentRemoveList() {
		return myPresentRemoveList;
	}

	public void setMyPresentRemoveList(List<Long> myPresentRemoveList) {
		this.myPresentRemoveList = myPresentRemoveList;
	}

	public FriendInfo getFriendInfo() {
		return friendInfo;
	}

	public void setFriendInfo(FriendInfo friendInfo) {
		this.friendInfo = friendInfo;
	}

	public int getPresentNum() {
		return presentNum;
	}

	public void setPresentNum(int presentNum) {
		this.presentNum = presentNum;
	}

	public Map<String, Object> getItemMap() {
		return itemMap;
	}

	public void setItemMap(Map<String, Object> itemMap) {
		this.itemMap = itemMap;
	}

	public List<PresentInfo> getPresentList() {
		return presentList;
	}

	public void setPresentList(List<PresentInfo> presentList) {
		this.presentList = presentList;
	}

	public FriendState getFriendState() {
		return friendState;
	}

	public void setFriendState(FriendState friendState) {
		this.friendState = friendState;
	}

	public MailInfo getMailInfo() {
		return mailInfo;
	}

	public void setMailInfo(MailInfo mailInfo) {
		this.mailInfo = mailInfo;
	}

	public List<FriendInfo> getFriendList() {
		return friendList;
	}

	public void setFriendList(List<FriendInfo> friendList) {
		this.friendList = friendList;
	}
	
}
