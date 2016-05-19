package com.mi.game.module.relation.protocol;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mi.core.protocol.BaseProtocol;
import com.mi.game.defines.HandlerIds;
import com.mi.game.module.login.pojo.PlayerEntity;

public class PupilProtocol extends BaseProtocol {

	private List<PlayerEntity> pupilList;

	@Override
	public Map<String, Object> responseMap(int y) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		switch (y) {
		case HandlerIds.RELATION_PUPIL_LIST:
			if (pupilList != null) {
				responseMap.put("pupilList", pupilList);
			}
			break;
		}
		return responseMap;
	}

	public List<PlayerEntity> getPupilList() {
		return pupilList;
	}

	public void setPupilList(List<PlayerEntity> pupilList) {
		this.pupilList = pupilList;
	}
}
