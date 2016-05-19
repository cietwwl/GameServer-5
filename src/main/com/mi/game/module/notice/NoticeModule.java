package com.mi.game.module.notice;

import java.util.List;

import com.mi.core.dao.KeyGeneratorDAO;
import com.mi.core.engine.annotation.Module;
import com.mi.game.defines.ModuleNames;
import com.mi.game.defines.SysConstants;
import com.mi.game.module.base.BaseModule;
import com.mi.game.module.notice.dao.NoticeEntityDao;
import com.mi.game.module.notice.pojo.NoticeEntity;
import com.mi.game.module.notice.protocol.NoticeProtocol;

@Module(name = ModuleNames.NoticeModule, clazz = NoticeModule.class)
public class NoticeModule extends BaseModule {

	private static NoticeEntityDao noticeEntityDao = NoticeEntityDao.getInstance();
	private final KeyGeneratorDAO keyGeneratorDAO = KeyGeneratorDAO.getInstance();

	private static List<NoticeEntity> noticeList;

	@Override
	public void init() {
		noticeList = getNoticeList();
		if (!hasNotice()) {
			NoticeEntity noticeEntity = new NoticeEntity();
			noticeEntity.setNid(getNoticeID() + "");
			noticeEntity.setStop(true);
			noticeEntity.setIndex(0);
			noticeEntity.setTitle("默认公告");
			noticeEntity.setContent("默认公告,stop属性为true,不会出现在公告列表中,请勿删除!");
			noticeEntity.setDateTime(System.currentTimeMillis());
			saveNoticeEntity(noticeEntity);
			noticeList = getNoticeList();
		}
	}

	public long getNoticeID() {
		String clsName = SysConstants.NoticeIDEntity;
		long noticeID = keyGeneratorDAO.updateInc(clsName);
		return noticeID;
	}

	public void refreshNotice() {
		noticeList = getNoticeList();
	}

	public void noticeInfo(NoticeProtocol protocol) {
		protocol.setNoticeList(noticeList);
	}

	public void saveNoticeEntity(NoticeEntity entity) {
		noticeEntityDao.save(entity);
	}

	public List<NoticeEntity> getNoticeList() {
		return noticeEntityDao.getNoticeList();
	}

	public boolean hasNotice() {
		return noticeEntityDao.hasNotice();
	}

}
