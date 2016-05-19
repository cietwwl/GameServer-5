package com.mi.game.module.login.dao;

import com.mi.game.module.login.pojo.LoginInfoEntity;
import com.mi.core.cache.QueryInfo;
import com.mi.core.cache.QueryType;
import com.mi.core.cache.bean.QueryBean;
import com.mi.core.dao.AbstractBaseDAO;

/**
 * 玩家登陆信息dao
 * 
 * */

public class LoginInfoDAO extends AbstractBaseDAO<LoginInfoEntity>{
	public static final LoginInfoDAO loginInfoDAO = new LoginInfoDAO();
	
	private LoginInfoDAO(){}
	
	public static LoginInfoDAO getInstance(){
		return loginInfoDAO;
	}
	
	public LoginInfoEntity getEntityByName(String playerName){
		QueryInfo queryInfo = new QueryInfo();
		QueryBean queryBean = new QueryBean("playerName", QueryType.EQUAL,playerName);
		queryInfo.addQueryBean(queryBean);
		LoginInfoEntity entity= this.query(queryInfo);
		return entity;
	}
	
	public LoginInfoEntity getEntityByVisitorID(String visitorID){
		QueryInfo queryInfo = new QueryInfo();
		QueryBean queryBean = new QueryBean("visitorPlayerName", QueryType.EQUAL,visitorID);
		queryInfo.addQueryBean(queryBean);
		LoginInfoEntity entity= this.query(queryInfo);
		return entity;
	}
	
	public LoginInfoEntity getEntityByEmail(String email){
		QueryInfo queryInfo = new QueryInfo();
		QueryBean queryBean = new QueryBean("email", QueryType.EQUAL, email);
		queryInfo.addQueryBean(queryBean);
		LoginInfoEntity entity= this.query(queryInfo);
		return entity;
	}
	
}
