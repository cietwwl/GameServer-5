package com.mi.game.module.friend.dao;

import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.friend.pojo.FriendEntity;

public class FriendEntityDAO extends AbstractBaseDAO<FriendEntity>{
	private final static FriendEntityDAO FRIEND_ENTITY_DAO = new FriendEntityDAO();
	private FriendEntityDAO(){}
	public static FriendEntityDAO getInstance(){
		return FRIEND_ENTITY_DAO;
	}
}
