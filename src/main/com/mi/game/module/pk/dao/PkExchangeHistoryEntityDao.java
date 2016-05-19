package com.mi.game.module.pk.dao;

import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.pk.pojo.PkExchangeHistoryEntity;

public class PkExchangeHistoryEntityDao extends
		AbstractBaseDAO<PkExchangeHistoryEntity> {
	private static PkExchangeHistoryEntityDao historyEntityDao = new PkExchangeHistoryEntityDao();

	private PkExchangeHistoryEntityDao() {

	}

	public static PkExchangeHistoryEntityDao getInstance() {
		return historyEntityDao;
	}

}
