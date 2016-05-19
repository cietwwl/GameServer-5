package com.mi.game.module.playerservice.pojo;

import java.util.HashMap;
import java.util.Map;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.utils.IndexDirection;
import com.mi.core.pojo.BaseEntity;
import com.mi.core.util.DateTimeUtil;
import com.mi.game.defines.SuggestStatus;
import com.mi.game.defines.SuggestType;

/**
 * 玩家信息
 */
@Entity(noClassnameStored = true)
public class SuggestEntity extends BaseEntity{
	private static final long serialVersionUID = 3947651820886552410L;
	@Indexed(value=IndexDirection.ASC, unique=true)
	private String suggestID;
	private int type;
	private long time;
	private String message;
	private int status;
	private String playerID;
	private int showStatus;
	@Override
	public Map<String,Object> responseMap(){
		Map<String,Object> data = new HashMap<String, Object>();
		String suggestType = "";
		switch(type){
			case SuggestType.bug:
				suggestType = "bug";
			break;
			case SuggestType.complain:
				suggestType = "投诉";
			break;
			case SuggestType.consult:
				suggestType = "咨询";
			break;
			case SuggestType.suggest:
				suggestType = "建议";
			break;
			default:
				suggestType = type+ "";
			break;		
		}
		data.put("type",suggestType);
		data.put("time",DateTimeUtil.getStringDate(time));
		data.put("message",message);
		data.put("suggestID", suggestID);
		data.put("playerID",playerID);
		data.put("showStatus", showStatus);
		String statusType = "";
		switch(status){
			case SuggestStatus.unread:
				statusType = "未读";
			break;
			case SuggestStatus.solve:
				statusType = "解决";
			break;
			case SuggestStatus.inHand:
				statusType = "未解决";
			break;
			case SuggestStatus.del:
				statusType = "删除";
			break;
			default:
				statusType = status + "";
			break;
		}
		data.put("status",statusType);
		return data;
	}

	@Override
	public Map<String,Object> responseMap(int type){
		return responseMap();
	}
	
	public int getShowStatus() {
		return showStatus;
	}

	public void setShowStatus(int showStatus) {
		this.showStatus = showStatus;
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

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public Object getKey() {
		// TODO 自动生成的方法存根
		return suggestID;
	}

	@Override
	public String getKeyName() {
		// TODO 自动生成的方法存根
		return "suggestID";
	}

	@Override
	public void setKey(Object key) {
		// TODO 自动生成的方法存根
		suggestID = key.toString();
	}
	
}
