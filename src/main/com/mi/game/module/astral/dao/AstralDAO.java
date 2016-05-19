package com.mi.game.module.astral.dao;

import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.astral.pojo.AstralEntity;

public class AstralDAO extends AbstractBaseDAO<AstralEntity>{
	private static final AstralDAO  astralDAO = new AstralDAO();
	private AstralDAO(){}
	public static AstralDAO getInstance(){
		return astralDAO;
	}
}
