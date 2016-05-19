package com.mi.game.module.mailBox.handler;

import java.util.List;

import com.mi.core.cache.QueryInfo;
import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.annotation.HandlerType;
import com.mi.core.protocol.BaseProtocol;
import com.mi.game.defines.HandlerIds;
import com.mi.game.defines.MailType;
import com.mi.game.defines.ModuleNames;
import com.mi.game.defines.SystemMsgType;
import com.mi.game.module.base.handler.BaseHandler;
import com.mi.game.module.login.dao.PlayerEntitiyDAO;
import com.mi.game.module.login.pojo.PlayerEntity;
import com.mi.game.module.mailBox.MailBoxModule;
import com.mi.game.util.Utilities;
@HandlerType(type = HandlerIds.addMailInfo)
public class AddMailHandler extends BaseHandler{
	@Override
	public void execute(IOMessage ioMessage){
		PlayerEntitiyDAO playerEntitiyDAO = PlayerEntitiyDAO.getInstance();
		long count = playerEntitiyDAO.queryCount(new QueryInfo());
		QueryInfo queryInfo = new QueryInfo(1, 100,"playerID");
		queryInfo.setTotal(count);
		queryInfo.initTotalPage();
		List<PlayerEntity> queryList = null;
		long nowTime = System.currentTimeMillis();
		MailBoxModule mailBoxModule = ModuleManager.getModule(ModuleNames.MailBoxModule,MailBoxModule.class);
		// 分页查询数据
		while (queryInfo.getPage() <= queryInfo.getTotalPage()) {
			queryList = playerEntitiyDAO.queryPage(queryInfo);
			if (queryList == null || queryList.isEmpty()) {
				break;
			}
			int size = queryList.size();
			for (int i = 0; i < size; i++) {
				PlayerEntity playerEntity = queryList.get(i);
				String playerID = playerEntity.getKey().toString();
				if (Utilities.isNpc(playerID)) {
					continue;
				}
				mailBoxModule.addMail(playerID, null, null, null, MailType.SYSTEMTYPE, SystemMsgType.WelcomeInfo, null);
			}
			playerEntitiyDAO.save(queryList);
			queryInfo.setPage(queryInfo.getPage() + 1);
		}
		logger.error("发放邮件成功, 用时"+ (System.currentTimeMillis() - nowTime) +"毫秒");
		ioMessage.setProtocol(new BaseProtocol());
	}
	
}
