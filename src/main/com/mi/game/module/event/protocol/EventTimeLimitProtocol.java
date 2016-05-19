package com.mi.game.module.event.protocol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mi.core.engine.ModuleManager;
import com.mi.core.protocol.BaseProtocol;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.event.define.EventConstans;
import com.mi.game.module.event.pojo.EventTimeLimitEntity;
import com.mi.game.module.login.LoginModule;
import com.mi.game.module.login.pojo.PlayerEntity;
import com.mi.game.module.reward.data.GoodsBean;

public class EventTimeLimitProtocol extends BaseProtocol {
	private EventTimeLimitEntity timeLimitEntity;
	private List<EventTimeLimitEntity> rankList;;
	private Map<String, Object> itemMap;
	private List<GoodsBean> showMap;

	@Override
	public Map<String, Object> responseMap(int y) {
		Map<String, Object> response = new HashMap<String, Object>();
		switch (y) {
		case HandlerIds.TimeLimitScoreList:
			if (rankList != null) {
				response.put("timeLimitList", entityListToMap(rankList));
			}
			if (timeLimitEntity != null) {
				response.put(EventConstans.EVENT_TYPE_TIME_LIMIT + "", timeLimitEntity.responseMap());
			}
			break;
		case HandlerIds.TimeLimitDrawHero:
			if (timeLimitEntity != null) {
				response.put(EventConstans.EVENT_TYPE_TIME_LIMIT + "", timeLimitEntity.responseMap());
			}
			if (rankList != null) {
				response.put("timeLimitList", entityListToMap(rankList));
			}
			if (itemMap != null) {
				response.put("itemMap", itemMap);
			}
			if (showMap != null) {
				response.put("showMap", showMap);
			}
			break;
		}
		return response;
	}

	public EventTimeLimitEntity getTimeLimitEntity() {
		return timeLimitEntity;
	}

	public void setTimeLimitEntity(EventTimeLimitEntity timeLimitEntity) {
		this.timeLimitEntity = timeLimitEntity;
	}

	public List<EventTimeLimitEntity> getRankList() {
		return rankList;
	}

	public void setRankList(List<EventTimeLimitEntity> rankList) {
		this.rankList = rankList;
	}

	public Map<String, Object> getItemMap() {
		return itemMap;
	}

	public void setItemMap(Map<String, Object> itemMap) {
		this.itemMap = itemMap;
	}

	public List<GoodsBean> getShowMap() {
		return showMap;
	}

	public void setShowMap(List<GoodsBean> showMap) {
		this.showMap = showMap;
	}

	private List<Map<String, Object>> entityListToMap(List<EventTimeLimitEntity> entityList) {
		LoginModule loginModule = ModuleManager.getModule(ModuleNames.LoginModule, LoginModule.class);
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		for (EventTimeLimitEntity entity : entityList) {
			Map<String, Object> response = new HashMap<String, Object>();
			PlayerEntity playerEntity = loginModule.getPlayerEntity(entity.getPlayerID());
			response.put("nick", playerEntity.getNickName());
			response.put("score", entity.getScore());
			result.add(response);
		}
		return result;
	}
}
