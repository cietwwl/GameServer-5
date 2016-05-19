package com.mi.game.module.notice.pojo;

import java.util.HashMap;
import java.util.Map;

import com.mi.core.pojo.BaseEntity;

public class NoticeEntity extends BaseEntity {

	private static final long serialVersionUID = -4386009705799463909L;

	private String nid;

	private int index;

	private String title;

	private String content;

	private boolean stop;

	private long dateTime;

	public String getNid() {
		return nid;
	}

	public void setNid(String nid) {
		this.nid = nid;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isStop() {
		return stop;
	}

	public void setStop(boolean stop) {
		this.stop = stop;
	}

	public long getDateTime() {
		return dateTime;
	}

	public void setDateTime(long dateTime) {
		this.dateTime = dateTime;
	}

	@Override
	public Map<String, Object> responseMap() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("nid", nid);
		result.put("index", index);
		result.put("title", title);
		result.put("content", content);
		result.put("stop", stop);
		return result;
	}

	@Override
	public Map<String, Object> responseMap(int t) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("nid", nid);
		result.put("index", index);
		result.put("title", title);
		result.put("stop", stop);
		return result;
	}

	@Override
	public Object getKey() {
		return nid;
	}

	@Override
	public String getKeyName() {
		return "nid";
	}

	@Override
	public void setKey(Object key) {
		nid = key.toString();
	}

}
