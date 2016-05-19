package com.mi.game.module.legion.dao;

import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.legion.pojo.LegionMemberEntity;

public class LegionMemberEntityDAO extends AbstractBaseDAO<LegionMemberEntity> {

	private static final LegionMemberEntityDAO memberEntityDAO = new LegionMemberEntityDAO();

	private LegionMemberEntityDAO() {
	}

	public static LegionMemberEntityDAO getInstance() {
		return memberEntityDAO;
	}

}
