package com.mi.game.module.login.dao;

import java.util.List;

import com.mi.game.module.login.pojo.ServerInfoEntity;
import com.mi.core.cache.QueryInfo;
import com.mi.core.dao.AbstractBaseDAO;

/**
 * @author 刘凯旋	
 *
 * 2014年5月23日 下午5:09:04
 */

public class ServerInfoDAO extends AbstractBaseDAO<ServerInfoEntity>{
	
	private final static ServerInfoDAO serverInfoDAO = new ServerInfoDAO();
	
	private ServerInfoDAO(){}
	
	public static ServerInfoDAO getInstance(){
		return serverInfoDAO;
	}
	
	/**
	 * 获取所有的服务器列表
	 * */
	public List<ServerInfoEntity> getAllserverInfo(){
		return serverInfoDAO.queryList(new QueryInfo());
	}

}
