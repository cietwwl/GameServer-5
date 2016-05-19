package com.mi.game.module.talisman.dao;

import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.talisman.pojo.TalismanMapEntity;

public class TalismanMapDAO extends AbstractBaseDAO<TalismanMapEntity>{
	private static final TalismanMapDAO talismanMapDAO = new TalismanMapDAO();
	
	private TalismanMapDAO(){}
	
	public static TalismanMapDAO getInstance(){
		return talismanMapDAO;
	}
}
