package com.mi.game.module.chat.protocol;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mi.core.protocol.BaseProtocol;
import com.mi.game.module.chat.classes.Message;
import com.mi.game.module.chat.define.EnumChannelType;
import com.mi.game.util.CommonMethod;

/**
 * @author 刘凯旋	
 *
 * 2014年12月1日 下午4:07:01
 */
public class ChatRoomResponse extends BaseProtocol {

	public String starTime;
	public List<Message> msgList;
	public EnumChannelType channelType;
	private long systemTime;
	
	public long getSystemTime() {
		return systemTime;
	}


	public void setSystemTime(long systemTime) {
		this.systemTime = systemTime;
	}


	@Override
	public Map<String, Object> responseMap() {
		Map<String, Object> data = new HashMap<String, Object>();
		
		data.put("starTime", starTime);
		data.put("msgList", CommonMethod.getResponseListMap(msgList));
		data.put("channelType", channelType.value());
		if(systemTime != 0)
			data.put("systemTime", systemTime);
		return data;
	}
	
	
}
