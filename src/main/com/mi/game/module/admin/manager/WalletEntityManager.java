package com.mi.game.module.admin.manager;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.mi.core.cache.QueryInfo;
import com.mi.core.engine.IOMessage;
import com.mi.core.pojo.BaseEntity;
import com.mi.game.module.abstrac.BaseEntityManager;
import com.mi.game.module.abstrac.ResponseResult;
import com.mi.game.module.admin.protocol.BaseAdminProtocol;
import com.mi.game.module.wallet.dao.WalletDAO;
import com.mi.game.module.wallet.pojo.WalletEntity;

public class WalletEntityManager extends BaseEntityManager<WalletEntity> {

	public WalletEntityManager() {
		this.dao = WalletDAO.getInstance();
	}

	@Override
	public void updateEntity(IOMessage ioMessage) {
		BaseAdminProtocol protocol = new BaseAdminProtocol();
		String playerID = (String) ioMessage.getInputParse("playerID");
		WalletEntity walletEntity = dao.getEntity(playerID);
		if (walletEntity == null) {
			protocol.put("code", 0);
			protocol.put("result", ResponseResult.NULL);
			ioMessage.setOutputResult(protocol);
			return;
		}
		String gold = (String) ioMessage.getInputParse("gold");
		String silver = (String) ioMessage.getInputParse("silver");
		String heroSoul = (String) ioMessage.getInputParse("heroSoul");
		String jewelSoul = (String) ioMessage.getInputParse("jewelSoul");
		String reputation = (String) ioMessage.getInputParse("reputation");
		if (StringUtils.isNotBlank(gold)) {
			walletEntity.setGold(Long.parseLong(gold));
		}
		if (StringUtils.isNotBlank(silver)) {
			walletEntity.setSilver(Long.parseLong(silver));
		}
		if (StringUtils.isNotBlank(heroSoul)) {
			walletEntity.setHeroSoul(Long.parseLong(heroSoul));
		}
		if (StringUtils.isNotBlank(jewelSoul)) {
			walletEntity.setJewelSoul(Long.parseLong(jewelSoul));
		}
		if (StringUtils.isNotBlank(reputation)) {
			walletEntity.setReputation(Long.parseLong(reputation));
		}
		dao.save(walletEntity);
		protocol.put("result", ResponseResult.OK);
		protocol.put("code", 1);
		ioMessage.setOutputResult(protocol);
	}

	@Override
	public List<? extends BaseEntity> doQueryList(QueryInfo queryInfo, IOMessage ioMessage) {
		String playerID = (String) ioMessage.getInputParse("playerID");
		if (StringUtils.isNotBlank(playerID)) {
			queryInfo.addQueryCondition("playerID", playerID);
		}
		return dao.queryPage(queryInfo);
	}
}
