package com.mi.game.module.admin.manager;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.mi.core.cache.QueryInfo;
import com.mi.core.engine.IOMessage;
import com.mi.core.pojo.BaseEntity;
import com.mi.game.module.abstrac.BaseEntityManager;
import com.mi.game.module.abstrac.ResponseResult;
import com.mi.game.module.admin.protocol.BaseAdminProtocol;
import com.mi.game.module.login.dao.NewPlayerEntityDAO;
import com.mi.game.module.login.pojo.NewPlayerEntity;

public class NewPlayerEntityManager extends BaseEntityManager<NewPlayerEntity> {
	public NewPlayerEntityManager() {
		this.dao = NewPlayerEntityDAO.getInstance();
	}

	public List<? extends BaseEntity> doQueryList(QueryInfo queryInfo, IOMessage ioMessage) {
		String playerID = (String) ioMessage.getInputParse("playerID");
		String gameLevelID = (String) ioMessage.getInputParse("gameLevelID");
		if (StringUtils.isNotBlank(playerID)) {
			queryInfo.addQueryCondition("playerID", playerID);
		}
		if (StringUtils.isNotBlank(gameLevelID)) {
			queryInfo.addQueryCondition("gameLevelID", Integer.parseInt(gameLevelID));
		}
		return dao.queryPage(queryInfo);
	}

	@Override
	public void updateEntity(IOMessage ioMessage) {
		BaseAdminProtocol protocol = new BaseAdminProtocol();
		String playerID = (String) ioMessage.getInputParse("playerID");
		NewPlayerEntity newPlayerEntity = dao.getEntity(playerID);
		if (newPlayerEntity == null) {
			protocol.put("code", 0);
			protocol.put("result", ResponseResult.NULL);
			ioMessage.setOutputResult(protocol);
			return;
		}
		String properId = (String) ioMessage.getInputParse("properId");
		String isFinality = (String) ioMessage.getInputParse("finality");
		String triggerId = (String) ioMessage.getInputParse("triggerId");
		String gameLevelID = (String) ioMessage.getInputParse("gameLevelID");
		if (StringUtils.isNotBlank(properId)) {
			newPlayerEntity.setProperId(Integer.parseInt(properId));
		}
		if (StringUtils.isNotBlank(isFinality)) {
			newPlayerEntity.setFinality(Boolean.parseBoolean(isFinality));
		}
		if (StringUtils.isNotBlank(triggerId)) {
			newPlayerEntity.setTriggerId(triggerId);
		}
		if (StringUtils.isNotBlank(gameLevelID)) {
			newPlayerEntity.setGameLevelID(Integer.parseInt(gameLevelID));
		}
		dao.save(newPlayerEntity);
		protocol.put("result", ResponseResult.OK);
		protocol.put("code", 1);
		ioMessage.setOutputResult(protocol);
	}

}
