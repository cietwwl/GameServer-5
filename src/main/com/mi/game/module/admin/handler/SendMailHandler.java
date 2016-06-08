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

@HandlerType(type = HandlerIds.sendMail, order = 2)
public class SendMailHandler extends BaseHandler {

	@Override
	public void execute(IOMessage ioMessage) {

		String idList = (String) ioMessage.getInputParse("idList");
		String mailTitle = (String) ioMessage.getInputParse("mailTitle");
		String mailMessage = (String) ioMessage.getInputParse("mailMessage");
		
		QueryInfo queryInfo = new QueryInfo();
		BaseEntityManager<?> entityManager = EntityManagerFoctory.getEntityManager("SendMailEntity");
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
