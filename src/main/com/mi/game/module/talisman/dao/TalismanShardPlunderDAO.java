package com.mi.game.module.talisman.dao;

import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.talisman.pojo.TalismanShardPlunderEntity;

public class TalismanShardPlunderDAO extends AbstractBaseDAO<TalismanShardPlunderEntity>{
	private static final TalismanShardPlunderDAO talismanShardPlunderDAO = new TalismanShardPlunderDAO();
	private TalismanShardPlunderDAO(){}
	public static TalismanShardPlunderDAO getInstance(){
		return talismanShardPlunderDAO;
	}
}
