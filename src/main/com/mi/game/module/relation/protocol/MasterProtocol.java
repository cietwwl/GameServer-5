package com.mi.game.module.relation.protocol;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mi.core.protocol.BaseProtocol;
import com.mi.game.defines.HandlerIds;
import com.mi.game.module.login.pojo.PlayerEntity;
import com.mi.game.module.relation.pojo.MasterEntity;

public class MasterProtocol extends BaseProtocol {

	private MasterEntity masterEntity;
	private List<PlayerEntity> masterList;

	@Override
	public Map<String, Object> responseMap(int y) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		switch (y) {
		case HandlerIds.RELATION_MASTER_LIST:
			if (masterList != null) {
				responseMap.put("masterList", masterList);
			}
			break;
		}

		return responseMap;
	}

	public MasterEntity getMasterEntity() {
		return masterEntity;
	}

	public void setMasterEntity(MasterEntity masterEntity) {
		this.masterEntity = masterEntity;
	}

	public List<PlayerEntity> getMasterList() {
		return masterList;
	}

	public void setMasterList(List<PlayerEntity> masterList) {
		this.masterList = masterList;
	}

}
