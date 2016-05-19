package com.mi.game.module.admin.manager;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.mi.core.cache.QueryInfo;
import com.mi.core.cache.QueryType;
import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.pojo.BaseEntity;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.abstrac.BaseEntityManager;
import com.mi.game.module.abstrac.ResponseResult;
import com.mi.game.module.admin.protocol.BaseAdminProtocol;
import com.mi.game.module.event.EventModule;
import com.mi.game.module.event.dao.EventConfigDao;
import com.mi.game.module.event.pojo.EventConfigEntity;

public class EventConfigEntityManager extends BaseEntityManager<EventConfigEntity> {

	public EventConfigEntityManager() {
		this.dao = EventConfigDao.getInstance();
	}

	@Override
	public void updateEntity(IOMessage ioMessage) {
		BaseAdminProtocol protocol = new BaseAdminProtocol();
		String eventID = (String) ioMessage.getInputParse("eventID");
		EventConfigEntity eventConfigEntity = dao.getEntity(eventID);
		if (eventConfigEntity == null) {
			eventID = ModuleManager.getModule(ModuleNames.EventModule, EventModule.class).getEventID() + "";
			eventConfigEntity = new EventConfigEntity();
			eventConfigEntity.setEventID(eventID);
		}
		String pid = (String) ioMessage.getInputParse("pid");
		String infoPid = (String) ioMessage.getInputParse("infoPid");
		String index = (String) ioMessage.getInputParse("index");
		String status = (String) ioMessage.getInputParse("status");
		String desc = (String) ioMessage.getInputParse("desc");
		String version = (String) ioMessage.getInputParse("version");
		String startTime = (String) ioMessage.getInputParse("startTime");
		String endTime = (String) ioMessage.getInputParse("endTime");
		if (StringUtils.isNotBlank(index)) {
			eventConfigEntity.setIndex(Integer.parseInt(index));
		}
		if (StringUtils.isNotBlank(pid)) {
			eventConfigEntity.setPid(Integer.parseInt(pid));
		}
		if (StringUtils.isNotBlank(infoPid)) {
			eventConfigEntity.setInfoPid(Integer.parseInt(infoPid));
		}
		if (StringUtils.isNotBlank(status)) {
			eventConfigEntity.setStatus(Integer.parseInt(status));
		}
		if (StringUtils.isNotBlank(desc)) {
			eventConfigEntity.setDesc(desc);
		}
		if (StringUtils.isNotBlank(version)) {
			eventConfigEntity.setVersion(Integer.parseInt(version));
		}
		if (StringUtils.isNotBlank(startTime)) {
			eventConfigEntity.setStartTime(startTime);
		}
		if (StringUtils.isNotBlank(endTime)) {
			eventConfigEntity.setEndTime(endTime);
		}
		dao.save(eventConfigEntity);
		protocol.put("result", ResponseResult.OK);
		protocol.put("code", 1);
		ioMessage.setOutputResult(protocol);
	}

	@Override
	public List<? extends BaseEntity> doQueryList(QueryInfo queryInfo, IOMessage ioMessage) {
		String pid = (String) ioMessage.getInputParse("pid");
		String index = (String) ioMessage.getInputParse("index");
		String status = (String) ioMessage.getInputParse("status");
		String desc = (String) ioMessage.getInputParse("desc");
		String version = (String) ioMessage.getInputParse("version");
		String startTime = (String) ioMessage.getInputParse("startTime");
		String endTime = (String) ioMessage.getInputParse("endTime");
		queryInfo.setOrder("index");
		if (StringUtils.isNotBlank(index)) {
			queryInfo.addQueryCondition("index", Integer.parseInt(index));
		}
		if (StringUtils.isNotBlank(pid)) {
			queryInfo.addQueryCondition("pid", Integer.parseInt(pid));
		}
		if (StringUtils.isNotBlank(status)) {
			queryInfo.addQueryCondition("status", Integer.parseInt(status));
		}
		if (StringUtils.isNotBlank(desc)) {
			queryInfo.addQueryCondition("desc", QueryType.LIKE, desc);
		}
		if (StringUtils.isNotBlank(version)) {
			queryInfo.addQueryCondition("version", Integer.parseInt(version));
		}
		if (StringUtils.isNotBlank(startTime)) {
			queryInfo.addQueryCondition("startTime", startTime);
		}
		if (StringUtils.isNotBlank(endTime)) {
			queryInfo.addQueryCondition("endTime", endTime);
		}
		return dao.queryPage(queryInfo);
	}
}
