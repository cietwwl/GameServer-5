package com.mi.game.module.notice.protocol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mi.core.protocol.BaseProtocol;
import com.mi.game.module.notice.pojo.NoticeEntity;

public class NoticeProtocol extends BaseProtocol {

	private List<NoticeEntity> noticeList;

	@Override
	public Map<String, Object> responseMap() {
		Map<String, Object> result = new HashMap<String, Object>();
		if (noticeList != null) {
			result.put("noticeInfo", responseList());
		}
		return result;
	}

	public List<NoticeEntity> getNoticeList() {
		return noticeList;
	}

	public void setNoticeList(List<NoticeEntity> noticeList) {
		this.noticeList = noticeList;
	}

	private List<Map<String, Object>> responseList() {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		for (NoticeEntity notice : noticeList) {
			result.add(notice.responseMap());
		}
		return result;
	}

}
