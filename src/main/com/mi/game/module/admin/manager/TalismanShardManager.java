package com.mi.game.module.admin.manager;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.mi.core.cache.QueryInfo;
import com.mi.core.engine.IOMessage;
import com.mi.core.pojo.BaseEntity;
import com.mi.game.module.abstrac.BaseEntityManager;
import com.mi.game.module.abstrac.ResponseResult;
import com.mi.game.module.admin.protocol.BaseAdminProtocol;
import com.mi.game.module.talisman.dao.TalismanShardDAO;
import com.mi.game.module.talisman.pojo.TalismanShard;

public class TalismanShardManager extends BaseEntityManager<TalismanShard> {
	public TalismanShardManager() {
		this.dao = TalismanShardDAO.getInstance();
	}

	public List<? extends BaseEntity> doQueryList(QueryInfo queryInfo, IOMessage ioMessage) {
		String playerID = (String) ioMessage.getInputParse("playerID");
		if (StringUtils.isNotBlank(playerID)) {
			queryInfo.addQueryCondition("playerID", playerID);
		}
		return dao.queryPage(queryInfo);
	}

	@Override
	public void updateEntity(IOMessage ioMessage) {
		BaseAdminProtocol protocol = new BaseAdminProtocol();
		String unique = (String) ioMessage.getInputParse("unique");
		TalismanShard entity = dao.getEntity(unique);
		if (entity == null) {
			protocol.put("code", 0);
			protocol.put("result", ResponseResult.NULL);
			ioMessage.setOutputResult(protocol);
			return;
		}
		String playerID = (String) ioMessage.getInputParse("playerID");
		if (StringUtils.isNotEmpty(playerID)) {
			entity.setPlayerID(playerID);
		}
		String shardID = (String) ioMessage.getInputParse("shardID");
		if (StringUtils.isNotEmpty(shardID)) {
			entity.setShardID(Integer.parseInt(shardID));
		}
		String level = (String) ioMessage.getInputParse("level");
		if (StringUtils.isNotEmpty(level)) {
			entity.setLevel(Integer.parseInt(level));
		}
		String num = (String) ioMessage.getInputParse("num");
		if (StringUtils.isNotEmpty(num)) {
			entity.setNum(Integer.parseInt(num));
		}
		dao.save(entity);
		protocol.put("result", ResponseResult.OK);
		protocol.put("code", 1);
		ioMessage.setOutputResult(protocol);
	}

}
