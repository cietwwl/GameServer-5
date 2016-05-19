package com.mi.game.module.pay.dao;

import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.pay.pojo.AppstoreReceiptEntity;

public class AppstoreReceiptEntityDao extends AbstractBaseDAO<AppstoreReceiptEntity> {
	private static final AppstoreReceiptEntityDao entityDao = new AppstoreReceiptEntityDao();

	private AppstoreReceiptEntityDao() {
	}

	public static AppstoreReceiptEntityDao getInstance() {
		return entityDao;
	}

}
