package com.mi.game.module.admin.handler;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.mi.core.cache.QueryInfo;
import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.core.pojo.BaseEntity;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.MailType;
import com.mi.game.defines.ModuleNames;
import com.mi.game.defines.SystemMsgType;
import com.mi.game.module.abstrac.BaseEntityManager;
import com.mi.game.module.abstrac.EntityManagerFoctory;
import com.mi.game.module.admin.protocol.BaseAdminProtocol;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.mailBox.MailBoxModule;

@HandlerType(type = HandlerIds.sendMail, order = 2)
public class SendMailHandler extends BaseHandler {

	@Override
	public void execute(IOMessage ioMessage) {

		BaseAdminProtocol protocol = new BaseAdminProtocol();
		String idList = (String) ioMessage.getInputParse("idList");
		String mailTitle = (String) ioMessage.getInputParse("mailTitle");
		String mailMessage = (String) ioMessage.getInputParse("mailMessage");
		MailBoxModule mailBoxModule = ModuleManager.getModule(ModuleNames.MailBoxModule,MailBoxModule.class);
		
		String[] idArray = idList.split(",");
		for(int i = 0; i < idArray.length; i++){
			String playerID = idArray[i];
			mailBoxModule.addMail(playerID, mailTitle, null, mailMessage, MailType.SYSTEMTYPE, SystemMsgType.WelcomeInfo, null);
		}
		// 发送成功
		protocol.put("result", 1);
		
		ioMessage.setOutputResult(protocol);
	}

}
