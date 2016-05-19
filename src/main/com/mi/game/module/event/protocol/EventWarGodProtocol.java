package com.mi.game.module.event.protocol;

import java.util.HashMap;
import java.util.Map;

import com.mi.core.protocol.BaseProtocol;
import com.mi.game.module.event.pojo.WarGodRankEntity;

public class EventWarGodProtocol extends BaseProtocol{
	private WarGodRankEntity warGodRankEntity;

	@Override
	public Map<String,Object> responseMap(){
		Map<String,Object> data = new HashMap<String, Object>();
		if(warGodRankEntity != null)
			data.put("warGodRank", warGodRankEntity.responseMap());
		return data;
	}
	
	public WarGodRankEntity getWarGodRankEntity() {
		return warGodRankEntity;
	}

	public void setWarGodRankEntity(WarGodRankEntity warGodRankEntity) {
		this.warGodRankEntity = warGodRankEntity;
	}
	
}
