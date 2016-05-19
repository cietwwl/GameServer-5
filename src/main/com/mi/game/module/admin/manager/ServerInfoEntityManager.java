package com.mi.game.module.admin.manager;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.mi.core.cache.QueryInfo;
import com.mi.core.engine.IOMessage;
import com.mi.core.pojo.BaseEntity;
import com.mi.game.defines.ErrorIds;
import com.mi.game.module.abstrac.BaseEntityManager;
import com.mi.game.module.abstrac.ResponseResult;
import com.mi.game.module.admin.protocol.BaseAdminProtocol;
import com.mi.game.module.login.dao.ServerInfoDAO;
import com.mi.game.module.login.pojo.ServerInfoEntity;

public class ServerInfoEntityManager extends BaseEntityManager<ServerInfoEntity> {
	public ServerInfoEntityManager() {
		super();
		this.dao = ServerInfoDAO.getInstance();
	}

	public List<? extends BaseEntity> doQueryList(QueryInfo queryInfo, IOMessage ioMessage) {

		queryInfo.setOrder("-serverID");
		String serverID = (String) ioMessage.getInputParse("serverID");

		String serverName = (String) ioMessage.getInputParse("serverName");

		String url = (String) ioMessage.getInputParse("url");
		if (StringUtils.isNotBlank(serverID)) {
			queryInfo.addQueryCondition("serverID", serverID);
		}
		if (StringUtils.isNotBlank(serverName)) {
			queryInfo.addQueryCondition("serverName", serverName);
		}
		if (StringUtils.isNotBlank(url)) {
			queryInfo.addQueryCondition("url", url);
		}

		return dao.queryPage(queryInfo);

	}

	@Override
	public void updateEntity(IOMessage ioMessage) {
		String serverID = (String) ioMessage.getInputParse("serverID");
		if (StringUtils.isNotBlank(serverID)) {
			ServerInfoEntity entity = dao.getEntity(Long.parseLong(serverID));
			if (entity == null) {
				entity = new ServerInfoEntity();
			}

			String serverName = (String) ioMessage.getInputParse("serverName");
			String url = (String) ioMessage.getInputParse("url");
			String status = (String) ioMessage.getInputParse("status");

			if (StringUtils.isNotBlank(serverName))
				entity.setServerName(serverName);
			if (StringUtils.isNotBlank(url))
				entity.setUrl(url);
			if (StringUtils.isNotBlank(status))
				entity.setStatus(Integer.parseInt(status));

			dao.save(entity);

			BaseAdminProtocol protocol = new BaseAdminProtocol();
			protocol.put("result", ResponseResult.OK);
			protocol.put("code", 1);
			ioMessage.setOutputResult(protocol);
		} else {
			this.writeErrorResult(ioMessage, ErrorIds.update_entity_error);
			return;
		}

	}

}
