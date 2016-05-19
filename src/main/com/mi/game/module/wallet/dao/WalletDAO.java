package com.mi.game.module.wallet.dao;

import java.util.List;

import com.mi.core.cache.QueryInfo;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.wallet.pojo.WalletEntity;

public class WalletDAO extends AbstractBaseDAO<WalletEntity> {

	private final static WalletDAO walletDAO = new WalletDAO();

	public static WalletDAO getInstance() {
		return walletDAO;
	}

	public List<WalletEntity> getWalletRank() {
		QueryInfo info = new QueryInfo(1, 1000, "-gold");
		return queryPage(info);
	}

}
