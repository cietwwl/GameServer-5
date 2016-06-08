package com.mi.game.module.login.dao;

import java.util.ArrayList;
import java.util.List;

import com.mi.core.cache.QueryInfo;
import com.mi.core.cache.QueryType;
import com.mi.core.cache.bean.QueryBean;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.login.pojo.PlayerEntity;

public class PlayerEntitiyDAO extends AbstractBaseDAO<PlayerEntity> {
	private final static PlayerEntitiyDAO playerEntityDAO = new PlayerEntitiyDAO();

	private PlayerEntitiyDAO() {
	}

	public static PlayerEntitiyDAO getInstance() {
		return playerEntityDAO;
	}

	public long getCount(String serverID){
		QueryInfo queryInfo = new QueryInfo();
		return this.queryCount(queryInfo, serverID);
	}
	
	public PlayerEntity getEntityByName(String playerName) {
		QueryInfo queryInfo = new QueryInfo();
		QueryBean queryBean = new QueryBean("nickName", QueryType.EQUAL, playerName);
		queryInfo.addQueryBean(queryBean);
		PlayerEntity entity = this.query(queryInfo);
		return entity;
	}

	public List<PlayerEntity> getEntityInList(List<Object> list) {
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.addMultipleQueryCondition("playerID", QueryType.IN, list);
		List<PlayerEntity> heroList = cache.queryList(queryInfo, PlayerEntity.class);
		return heroList;
	}

	public List<PlayerEntity> getRecommendList(List<Object> list) {
		QueryInfo queryInfo = new QueryInfo();
		if (!list.isEmpty()) {
			queryInfo.addMultipleQueryCondition("playerID", QueryType.NOT_IN, list);
		}
		queryInfo.addQueryCondition("level", QueryType.LESSTHAN, 10);
		queryInfo.addQueryCondition("level", QueryType.GREATERTHAN_OR_EQUAL, 1);
		queryInfo.setSize(50);
		List<PlayerEntity> heroList = cache.queryPage(queryInfo, PlayerEntity.class);
		return heroList;
	}

	public List<PlayerEntity> getLikeNameList(String name) {
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.addQueryCondition("nickName", QueryType.LIKE, name);
		queryInfo.setSize(50);
		List<PlayerEntity> heroList = cache.queryPage(queryInfo, PlayerEntity.class);
		return heroList;
	}

	/**
	 * 获取day天内 level级以上登录用户
	 * 
	 * @return
	 */
	public List<PlayerEntity> getaActivePlayerList(int day, int level, boolean type) {
		long time = System.currentTimeMillis() - (day * 24 * 3600 * 1000);
		QueryInfo queryInfo = new QueryInfo(1, 50, "-playerID");
		queryInfo.addQueryCondition("loginTime", QueryType.GREATERTHAN_OR_EQUAL, time);
		if (type) {
			queryInfo.addQueryCondition("level", QueryType.GREATERTHAN_OR_EQUAL, level);
		} else {
			queryInfo.addQueryCondition("level", QueryType.LESSTHAN_OR_EQUAL, level);
		}
		List<PlayerEntity> playerList = cache.queryPage(queryInfo, PlayerEntity.class);
		return playerList;
	}

	/**
	 * 获取达到level的用户id列表
	 * 
	 * @param level
	 * @return
	 */
	public List<String> getPlayerListByLevel(int level) {
		List<String> players = new ArrayList<String>();
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.addQueryCondition("level", QueryType.GREATERTHAN_OR_EQUAL, level);
		List<PlayerEntity> playerList = queryList(queryInfo);
		for (PlayerEntity player : playerList) {
			players.add(player.getKey().toString());
		}
		return players;
	}
	
	/**
	 * 获取达到VipLevel的用户列表
	 * @param vipLevel
	 * @return
	 */
	public List<PlayerEntity> getPlayerListByVIPLevel(int vipLevel) {
		List<String> players = new ArrayList<String>();
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.addQueryCondition("vipLevel", QueryType.GREATERTHAN_OR_EQUAL, vipLevel);
		List<PlayerEntity> playerList = queryList(queryInfo);
		return playerList;
	}

	/**
	 * 获取达到战斗力排行
	 * 
	 * @param size
	 * @return
	 */
	public List<String> getPlayerListByfightValue(int size) {
		List<String> players = new ArrayList<String>();
		QueryInfo queryInfo = new QueryInfo(1, size, "-fightValue");
		List<PlayerEntity> playerList = cache.queryPage(queryInfo, PlayerEntity.class);
		for (PlayerEntity player : playerList) {
			players.add(player.getKey().toString());
		}
		return players;
	}
	
	/**
	 * 获取达到战斗力排行
	 * 
	 * @param size
	 * @return
	 *
	 */
	public List<PlayerEntity> getPlayerEntityListByFightValue(int size){
		QueryInfo queryInfo = new QueryInfo(1, size, "-fightValue");
		List<PlayerEntity> playerList = cache.queryPage(queryInfo, PlayerEntity.class);
		return playerList;
	}
	
	/**
	 * 根据平台查询用户数量
	 * */
	public long getPlatformCount(String platform){
		QueryInfo queryInfo = new QueryInfo();
		QueryBean queryBean = new QueryBean("platform", QueryType.EQUAL, platform);
		queryInfo.addQueryBean(queryBean);
		long count = this.queryCount(queryInfo);
		return count;
	}
}
