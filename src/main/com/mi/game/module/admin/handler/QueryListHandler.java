package com.mi.game.module.admin.handler;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.mi.core.cache.QueryInfo;
import com.mi.core.engine.IOMessage;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.core.pojo.BaseEntity;
import com.mi.game.defines.HandlerIds;
import com.mi.game.module.abstrac.BaseEntityManager;
import com.mi.game.module.abstrac.EntityManagerFoctory;
import com.mi.game.module.admin.protocol.BaseAdminProtocol;
import com.mi.game.module.base.handler.BaseHandler;

@HandlerType(type = HandlerIds.adminQueryList, order = 2)
public class QueryListHandler extends BaseHandler {

	public void execute(IOMessage ioMessage) {
		String entityName = (String) ioMessage.getInputParse("entity");
		String page = (String) ioMessage.getInputParse("page");
		String pagesize = (String) ioMessage.getInputParse("pagesize");
		QueryInfo queryInfo = new QueryInfo();
		if (StringUtils.isNotBlank(page)) {
			queryInfo.setPage(Integer.parseInt(page));
		}
		if (StringUtils.isNotBlank(pagesize)) {
			queryInfo.setSize(Integer.parseInt(pagesize));
		}
		BaseEntityManager<?> entityManager = EntityManagerFoctory.getEntityManager(entityName);
		List<? extends BaseEntity> entityList = entityManager.doQueryList(queryInfo, ioMessage);
		BaseAdminProtocol protocol = new BaseAdminProtocol();
		if (entityList != null) {
			protocol.put("entityList", getResponseEntityArray(entityList, 11111));
			ioMessage.setOutputResult(protocol);
		}
		protocol.put("page", queryInfo.getPage());
		protocol.put("totalpage", queryInfo.getTotalPage());
		protocol.put("pagesize", queryInfo.getSize());
		protocol.put("total", queryInfo.getTotal());
		ioMessage.setOutputResult(protocol);
	}

}
