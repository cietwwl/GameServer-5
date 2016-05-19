package com.mi.game.module.chat.classes;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.mi.core.engine.IResponseMap;
import com.mi.core.util.DateTimeUtil;
import com.mi.game.module.chat.define.EnumMessageAddressType;
import com.mi.game.module.chat.define.EnumMessageType;
import com.mi.game.module.login.pojo.PlayerEntity;

/**
 * 用于聊天频道的message类
 * @author Administrator
 *
 */
public class Message implements IResponseMap{
	private Object body;							//发送消息体
	private long receiverDateTime;					//接收时间
	private EnumMessageAddressType receiverType;	//接收类型
	private String receiverID;						//接收者ID
	private String senderID;						//发送者ID
	private String senderName;						//发送者姓名
	private int senderSex;						    //发送者性别
	private int senderPhotoID;						//发送者头像
	private String senderGroupID;						//发送者军团ID
	private int senderVipLevel;						//发送者vip等级
	private int level;						//发送者等级
	private int fightValue;						//发送者战斗力
	
	private EnumMessageAddressType senderType;		//发送类型(源头)
	private String strParams;						//其他信息
	private EnumMessageType type;					//消息类型
	private Object parse;                           // 添加参数
	
	/**
	 * 构造
	 * @param senderType
	 * @param senderID
	 * @param senderName
	 * @param receiverType
	 * @param receiverID
	 * @param body
	 * @param type
	 * @param params
	 */
	public Message(EnumMessageAddressType senderType,PlayerEntity senderPlayer,
			EnumMessageAddressType receiverType,String receiverID,Object body,
			EnumMessageType type,String params){
		this.senderType = senderType;
		this.senderID = senderPlayer.getKey().toString();
		this.senderName = senderPlayer.getNickName();
		this.senderSex = senderPlayer.getSex();
		this.senderPhotoID = senderPlayer.getChatPhotoID();
		this.senderGroupID = senderPlayer.getGroupID();
		this.senderVipLevel = senderPlayer.getVipLevel();
		this.level = senderPlayer.getLevel();
		this.fightValue = senderPlayer.getFightValue();
		this.receiverType = receiverType;
		this.receiverID = receiverID;
		this.receiverDateTime=DateTimeUtil.getMillTime();
		this.body = body;
		this.type = type;
		this.strParams = params;
	}
	
	/**
	 * 构造
	 * @param senderType
	 * @param senderID
	 * @param senderName
	 * @param receiverType
	 * @param receiverID
	 * @param body
	 * @param type
	 * @param params
	 */
	public Message(EnumMessageAddressType senderType,String senderID,String senderName,
			EnumMessageAddressType receiverType,String receiverID,Object body,
			EnumMessageType type,String params){
		this.senderType = senderType;
		this.senderID = senderID;
		this.senderName = senderName;
		this.receiverType = receiverType;
		this.receiverID = receiverID;
		this.receiverDateTime=DateTimeUtil.getMillTime();
		this.body = body;
		this.type = type;
		this.strParams = params;
	}

	
	
	@Override
	public Map<String, Object> responseMap() {
		Map<String, Object> data = new HashMap<String, Object>();
		
		data.put("body", body);
		data.put("receiverDateTime", receiverDateTime);
		data.put("receiverType", receiverType.value());
		data.put("receiverID", receiverID);
		data.put("senderID", senderID);
		data.put("senderName", senderName);
		data.put("senderSex", senderSex);
		data.put("senderPhotoID", senderPhotoID);
		if(StringUtils.isBlank(senderGroupID))
			senderGroupID = "0";
		data.put("senderGroupID", senderGroupID);
		data.put("senderVipLevel", senderVipLevel);
		data.put("fightValue", fightValue);
		data.put("level", level);
		data.put("senderType", senderType.value());
		data.put("strParams", strParams);
		data.put("type", type.value());
		data.put("parse", parse);
		
		return data;
	}



	@Override
	public Map<String, Object> responseMap(int arg0) {
		Map<String, Object> data = new HashMap<String, Object>();
		
		data.put("body", body);
		data.put("receiverDateTime", receiverDateTime);
		data.put("receiverType", receiverType.value());
		data.put("receiverID", receiverID);
		data.put("senderID", senderID);
		data.put("senderName", senderName);
		data.put("senderSex", senderSex);
		data.put("senderPhotoID", senderPhotoID);
		if(StringUtils.isBlank(senderGroupID))
			senderGroupID = "0";
		data.put("senderGroupID", senderGroupID);
		data.put("senderVipLevel", senderVipLevel);
		data.put("fightValue", fightValue);
		data.put("level", level);
		data.put("senderType", senderType.value());
		data.put("strParams", strParams);
		data.put("type", type.value());
		data.put("parse", parse);
		
		return data;
	}



	public Object getBody() {
		return body;
	}

	public void setBody(Object body) {
		this.body = body;
	}

	public long getReceiverDateTime() {
		return receiverDateTime;
	}

	public void setReceiverDateTime(long receiverDateTime) {
		this.receiverDateTime = receiverDateTime;
	}

	public EnumMessageAddressType getReceiverType() {
		return receiverType;
	}

	public void setReceiverType(EnumMessageAddressType receiverType) {
		this.receiverType = receiverType;
	}

	public String getReceiverID() {
		return receiverID;
	}

	public void setReceiverID(String receiverID) {
		this.receiverID = receiverID;
	}

	public String getSenderID() {
		return senderID;
	}

	public void setSenderID(String senderID) {
		this.senderID = senderID;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public EnumMessageAddressType getSenderType() {
		return senderType;
	}

	public void setSenderType(EnumMessageAddressType senderType) {
		this.senderType = senderType;
	}

	public String getStrParams() {
		return strParams;
	}

	public void setStrParams(String strParams) {
		this.strParams = strParams;
	}

	public EnumMessageType getType() {
		return type;
	}

	public void setType(EnumMessageType type) {
		this.type = type;
	}

	/**
	 * @return the parse
	 */
	public Object getParse() {
		return parse;
	}

	/**
	 * @param parse the parse to set
	 */
	public void setParse(Object parse) {
		this.parse = parse;
	}



	public int getSenderSex() {
		return senderSex;
	}



	public void setSenderSex(int senderSex) {
		this.senderSex = senderSex;
	}



	public int getSenderPhotoID() {
		return senderPhotoID;
	}



	public void setSenderPhotoID(int senderPhotoID) {
		this.senderPhotoID = senderPhotoID;
	}



	public String getSenderGroupID() {
		return senderGroupID;
	}



	public void setSenderGroupID(String senderGroupID) {
		this.senderGroupID = senderGroupID;
	}



	public int getSenderVipLevel() {
		return senderVipLevel;
	}



	public void setSenderVipLevel(int senderVipLevel) {
		this.senderVipLevel = senderVipLevel;
	}
	
	
}
