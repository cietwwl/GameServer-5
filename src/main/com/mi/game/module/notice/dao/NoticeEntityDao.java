package com.mi.game.module.notice.dao;

import java.util.List;

import com.mi.core.cache.QueryInfo;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.notice.pojo.NoticeEntity;

public class NoticeEntityDao extends AbstractBaseDAO<NoticeEntity> {

	private static NoticeEntityDao noticeEntityDao = new NoticeEntityDao();

	private NoticeEntityDao() {

	}

	public static NoticeEntityDao getInstance() {
		return noticeEntityDao;
	}

	public List<NoticeEntity> getNoticeList() {
		QueryInfo query = new QueryInfo("-index");
		query.addQueryCondition("stop", false);
		return this.cache.queryList(query, NoticeEntity.class);
	}

	public boolean hasNotice() {
		QueryInfo query = new QueryInfo("-index");
		return this.cache.queryList(query, NoticeEntity.class).size() == 0 ? false : true;
	}

}
