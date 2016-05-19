package com.mi.game.module.playerservice.protocol;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mi.core.protocol.BaseProtocol;
import com.mi.game.defines.HandlerIds;
import com.mi.game.module.playerservice.pojo.SuggestEntity;

public class SuggestProtocol extends BaseProtocol{
	private List<SuggestEntity> suggestList;

	
	public List<SuggestEntity> getSuggestList() {
		return suggestList;
	}

	public void setSuggestList(List<SuggestEntity> suggestList) {
		this.suggestList = suggestList;
	}
	@Override
	public Map<String,Object> responseMap (int y){
		Map<String,Object> map = new HashMap<String, Object>();
		switch(y){
			case HandlerIds.getUnreadInfo:
				
				break;
		}
		return map;
	}
	
}
