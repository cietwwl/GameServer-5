package com.mi.game.module.legion.pojo;

import java.util.HashMap;
import java.util.Map;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;
import com.mi.core.engine.ModuleManager;
import com.mi.core.pojo.BaseEntity;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.login.LoginModule;
import com.mi.game.module.login.pojo.PlayerEntity;

@Entity(noClassnameStored = true)
public class LegionHistoryEntity extends BaseEntity {

	private static final long serialVersionUID = 4356506661681122058L;
	// 记录id
	private String historyID;
	// 军团id
	@Indexed
	private String legionID;
	// 操作类型
	private String type;
	// 操作内容
	private String content;
	// 用户id
	@Indexed
	private String playerID;
	// 操作时间
	@Indexed
	private long createTime;

	public String getHistoryID() {
		return historyID;
	}

	public void setHistoryID(String historyID) {
		this.historyID = historyID;
	}

	public String getLegionID() {
		return legionID;
	}

	public void setLegionID(String legionID) {
		this.legionID = legionID;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPlayerID() {
		return playerID;
	}

	public void setPlayerID(String playerID) {
		this.playerID = playerID;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public Map<String, Object> respsoneMap() {
		LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule, LoginModule.class);
		PlayerEntity temp = loginModule.getPlayerEntity(playerID);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("historyID", historyID);
		result.put("playerID", playerID);
		result.put("nickName", temp.getNickName());
		result.put("level", temp.getLevel());
		result.put("photoID", temp.getPhotoID());
		result.put("sex", temp.getSex());
		result.put("chatPhotoID", temp.getPhotoID());
		result.put("type", type);
		result.put("content", content);
		result.put("time", createTime);
		return result;
	}

	@Override
	public Object getKey() {
		return historyID;
	}

	@Override
	public String getKeyName() {
		return "historyID";
	}

	@Override
	public void setKey(Object key) {
		historyID = key.toString();

	}

}
