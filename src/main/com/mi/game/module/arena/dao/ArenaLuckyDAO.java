package com.mi.game.module.arena.dao;

import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.arena.pojo.LuckRankMapEntity;

public class ArenaLuckyDAO extends AbstractBaseDAO<LuckRankMapEntity>{
	private static final ArenaLuckyDAO arenaLuckyDAO = new ArenaLuckyDAO();
	private ArenaLuckyDAO(){}
	public static  ArenaLuckyDAO getInstance(){
		return arenaLuckyDAO;
	}
}
