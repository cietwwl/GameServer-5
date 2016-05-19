package com.mi.game.module.arena.dao;

import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.arena.pojo.ArenaShopEntity;

public class ArenaShopEntityDAO extends AbstractBaseDAO<ArenaShopEntity>{
	private final static ArenaShopEntityDAO ARENA_SHOP_ENTITY_DAO= new ArenaShopEntityDAO();
	private ArenaShopEntityDAO(){}
	public static ArenaShopEntityDAO getInstance(){
		return ARENA_SHOP_ENTITY_DAO;
	}
}
