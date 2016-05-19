package com.mi.game.module.analyse.task;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimerTask;

import com.alibaba.fastjson.JSON;
import com.mi.core.engine.IOMessage;
import com.mi.core.protocol.BaseProtocol;
import com.mi.game.module.analyse.util.MongoUtils;
import com.mi.game.util.Logs;
import com.mongodb.BasicDBObject;

public class AnalyseTask extends TimerTask {

	private IOMessage ioMessage;

	private Map<String, Object> payMap;

	public AnalyseTask(IOMessage ioMessage) {
		this.ioMessage = ioMessage;
	}

	public AnalyseTask(Map<String, Object> payMap) {
		this.payMap = payMap;
	}

	private Map<String, Object> filterRequest(Map<String, Object> map) {
		Map<String, Object> temp = new HashMap<String, Object>();
		Set<Entry<String, Object>> set = map.entrySet();
		for (Entry<String, Object> entry : set) {
			String key = entry.getKey();
			Object value = entry.getValue();
			if (key.equals("lastSendTime") || key.equals("uniqueKey") || key.equals("sendNum") || key.indexOf("Entity") != -1) {
				continue;
			}
			temp.put(key, JSON.toJSON(value));
		}
		return temp;
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> filterResponse(BaseProtocol protocol, int type) {
		Map<String, Object> temp = new HashMap<String, Object>();
		Map<String, Object> map = (Map<String, Object>) JSON.toJSON(protocol.responseMap(type));
		Set<Entry<String, Object>> set = map.entrySet();
		for (Entry<String, Object> entry : set) {
			String key = entry.getKey();
			Object value = entry.getValue();
			if (key.equals("channelType") || key.equals("msgList") || value == null) {
				continue;
			}
			temp.put(key, JSON.toJSON(value));
		}
		return temp;
	}

	@Override
	public void run() {
		int type = 0;
		Map<String, Object> request = new HashMap<String, Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			BasicDBObject basic = new BasicDBObject();
			if (payMap != null) {
				// 支付type 固定为8888
				basic.put("type", 8888);
				basic.put("code", 0);
				basic.put("playerID", payMap.get("playerID"));
				basic.put("request", payMap);
				basic.put("response", response);
				basic.put("dateTime", System.currentTimeMillis());
			} else {
				String playerID = ioMessage.getPlayerId();
				int code = ioMessage.getProtocol().getCode();
				type = ioMessage.getType();
				// 心跳不记录
				if (type == 1301 || type == 1302) {
					return;
				}
				request = filterRequest(ioMessage.getInputParse());
				response = filterResponse(ioMessage.getOutputResult(), type);
				basic.put("type", type);
				basic.put("code", code);
				basic.put("playerID", playerID);
				basic.put("request", request);
				basic.put("response", response);
				basic.put("dateTime", System.currentTimeMillis());
			}
			MongoUtils.insert(basic);
		} catch (Exception e) {
			Logs.logger.error("type=" + type + ",request=" + request + ",response=" + response);
		}

	}
}
