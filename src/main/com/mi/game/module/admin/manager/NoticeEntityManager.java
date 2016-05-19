package com.mi.game.module.admin.manager;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.mi.core.cache.QueryInfo;
import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.pojo.BaseEntity;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.abstrac.BaseEntityManager;
import com.mi.game.module.abstrac.ResponseResult;
import com.mi.game.module.admin.protocol.BaseAdminProtocol;
import com.mi.game.module.notice.NoticeModule;
import com.mi.game.module.notice.dao.NoticeEntityDao;
import com.mi.game.module.notice.pojo.NoticeEntity;

public class NoticeEntityManager extends BaseEntityManager<NoticeEntity> {

	public NoticeEntityManager() {
		this.dao = NoticeEntityDao.getInstance();
	}

	@Override
	public void updateEntity(IOMessage ioMessage) {
		BaseAdminProtocol protocol = new BaseAdminProtocol();
		NoticeModule noticeModule = ModuleManager.getModule(ModuleNames.NoticeModule, NoticeModule.class);
		String nid = (String) ioMessage.getInputParse("nid");
		NoticeEntity noticeEntity = dao.getEntity(nid);
		if (noticeEntity == null) {
			noticeEntity = new NoticeEntity();
			noticeEntity.setNid(noticeModule.getNoticeID() + "");
		}
		String title = (String) ioMessage.getInputParse("title");
		String index = (String) ioMessage.getInputParse("index");
		String content = (String) ioMessage.getInputParse("content");
		String stop = (String) ioMessage.getInputParse("stop");
		if (StringUtils.isNotBlank(title)) {
			noticeEntity.setTitle(title);
		}
		if (StringUtils.isNotBlank(index)) {
			noticeEntity.setIndex(Integer.parseInt(index));
		}
		if (StringUtils.isNotBlank(content)) {
			noticeEntity.setContent(content);
		}
		if (StringUtils.isNotBlank(stop)) {
			noticeEntity.setStop(Boolean.parseBoolean(stop));
		}
		noticeEntity.setDateTime(System.currentTimeMillis());
		dao.save(noticeEntity);
		protocol.put("result", ResponseResult.OK);
		protocol.put("code", 1);
		ioMessage.setOutputResult(protocol);
	}

	@Override
	public List<? extends BaseEntity> doQueryList(QueryInfo queryInfo, IOMessage ioMessage) {
		queryInfo.setOrder("-index");
		String nid = (String) ioMessage.getInputParse("nid");
		if (StringUtils.isNotEmpty(nid)) {
			queryInfo.addQueryCondition("nid", nid);
		}
		return dao.queryPage(queryInfo);
	}
}
