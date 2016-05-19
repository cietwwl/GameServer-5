package com.mi.game.module.playerservice.dao;

import java.util.List;

import com.mi.core.cache.QueryInfo;
import com.mi.core.cache.QueryType;
import com.mi.core.cache.bean.QueryBean;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.defines.SuggestStatus;
import com.mi.game.module.playerservice.pojo.SuggestEntity;

public class SuggestEntityDAO extends AbstractBaseDAO<SuggestEntity>{
	
	private final static SuggestEntityDAO suggestEntityDAO = new SuggestEntityDAO();
	
	private SuggestEntityDAO(){}
	
	public static SuggestEntityDAO getInstance(){
		return suggestEntityDAO;
	}
	
	/**
	 * 获取未读的建议
	 * */
	public List<SuggestEntity> getUnreadSuggestList(int page){
		QueryInfo queryInfo = new QueryInfo(1,10,"time");
		QueryBean queryBean = new QueryBean("status", QueryType.EQUAL, SuggestStatus.unread);
		queryInfo.addQueryBean(queryBean);
		List<SuggestEntity> suggestList = this.queryPage(queryInfo);
		return suggestList;
	}
}
