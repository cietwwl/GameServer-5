package com.mi.game.module.login.pojo;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.utils.IndexDirection;
import com.mi.core.pojo.BaseEntity;
/**
 * @author Administrator
 *
 */
@Entity(noClassnameStored = true)
public class PlayerEntity extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = -9175307280260623752L;
	@Indexed(value = IndexDirection.ASC, unique = true)
	private String playerID;
	private long loginTime;     // 最后一次登陆时间
	@Indexed(value = IndexDirection.ASC, unique = true)
	private String nickName;
	private int sex ;              //0男1女
	private int chatPhotoID;
	private String groupID;
	private int vipLevel;
	private int level = 1;        
	private int fightValue;      //战斗力
	private long offLineTime;
	private int attackFriendNum = 20;
	private int beFriendAttackNum = 20;
	private long updateTime;
	private int photoID;
	private String uniqueKey;   // 唯一key
	private String platform;
	
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getUniqueKey() {
		return uniqueKey;
	}
	public void setUniqueKey(String uniqueKey) {
		this.uniqueKey = uniqueKey;
	}
	public int getPhotoID() {
		return photoID;
	}
	public void setPhotoID(int photoID) {
		this.photoID = photoID;
	}
	public long getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}
	public int getBeFriendAttackNum() {
		return beFriendAttackNum;
	}
	public void setBeFriendAttackNum(int beFriendAttackNum) {
		this.beFriendAttackNum = beFriendAttackNum;
	}
	public int getAttackFriendNum() {
		return attackFriendNum;
	}
	public void setAttackFriendNum(int attackFriendNum) {
		this.attackFriendNum = attackFriendNum;
	}
	public long getOffLineTime() {
		return offLineTime;
	}
	public void setOffLineTime(long offLineTime) {
		this.offLineTime = offLineTime;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getFightValue() {
		return fightValue;
	}
	public void setFightValue(int fightValue) {
		this.fightValue = fightValue;
	}
	public int getVipLevel() {
		return vipLevel;
	}
	public void setVipLevel(int vipLevel) {
		this.vipLevel = vipLevel;
	}
	public int getChatPhotoID() {
		return chatPhotoID;
	}
	public void setChatPhotoID(int chatPhotoID) {
		this.chatPhotoID = chatPhotoID;
	}
	public String getGroupID() {
		if(StringUtils.isBlank(groupID))
			groupID = "0";
		return groupID;
	}
	public void setGroupID(String groupID) {
		this.groupID = groupID;
	}
	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public long getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(long loginTime) {
		this.loginTime = loginTime;
	}
	@Override
	public Map<String, Object> responseMap() {
		Map<String, Object> data = new HashMap<String, Object>();
		//data.put("loginTime", loginTime);
		data.put("nickName", nickName);
		data.put("sex", sex);
		data.put("chatPhotoID", chatPhotoID);
		if(groupID == null || groupID.isEmpty())
			groupID = "0";
		data.put("groupID", groupID);
		data.put("vipLevel", vipLevel);
		data.put("attackFriendNum", attackFriendNum);
		data.put("photoID", photoID);
		data.put("level",level);
		data.put("fightValue", fightValue);
		return data;
	}
	
	
	@Override
	public Map<String, Object> responseMap(int t) {
		Map<String, Object> data = new HashMap<String, Object>();
		switch(t){
			case 11111:{
				data.put("playerID", playerID);
				data.put("nickName", nickName);
				data.put("sex", sex);
				data.put("level", level);
				data.put("fightValue", fightValue);
				data.put("vipLevel", vipLevel);
				break;
			}
		}
		return data;
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
		this.playerID = key.toString();
	}

}
