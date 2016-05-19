package com.mi.game.module.wallet;

import java.util.List;

import com.mi.core.engine.IOMessage;
import com.mi.core.engine.ModuleManager;
import com.mi.core.engine.TemplateManager;
import com.mi.core.engine.annotation.Module;
import com.mi.game.defines.ErrorIds;
import com.mi.game.defines.KindIDs;
import com.mi.game.defines.ModuleNames;
import com.mi.game.module.base.BaseModule;
import com.mi.game.module.base.bean.init.WalletInitData;
import com.mi.game.module.event.EventModule;
import com.mi.game.module.wallet.dao.WalletDAO;
import com.mi.game.module.wallet.pojo.WalletEntity;

/**
 * @author 刘凯旋
 *
 *         2014年5月26日 下午2:45:32
 */
@Module(name = ModuleNames.WalletModule, clazz = WalletModule.class)
public class WalletModule extends BaseModule {
	/** 钱包DAO */
	private final WalletDAO walletDAO = WalletDAO.getInstance();

	public List<WalletEntity> getWalletRank() {
		return walletDAO.getWalletRank();
	}

	/**
	 * 初始化钱包
	 * **/
	public WalletEntity initWalletEntity(String playerID) {
		WalletEntity walletEntity = new WalletEntity();
		WalletInitData initData = TemplateManager.getTemplateData(KindIDs.WALLETINITDATA, WalletInitData.class);
		walletEntity.setGold(initData.getGold());
		walletEntity.setSilver(initData.getSilver());
		walletEntity.setHeroSoul(initData.getHeroSoul());
		walletEntity.setJewelSoul(initData.getJewelSoul());
		walletEntity.setReputation(initData.getReputation());
		walletEntity.setKey(playerID);
		return walletEntity;
	}

	/**
	 * 获取钱包实体，如果ioMessage里有钱包实体，优先从ioMessage获取
	 * 
	 * @param playerID
	 * @param ioMessage
	 * @return
	 */
	public WalletEntity getWalletEntity(String playerID, IOMessage ioMessage) {
		WalletEntity entity = null;

		if (ioMessage != null) {
			entity = (WalletEntity) ioMessage.getInputParse().get(WalletEntity.class.getName());

			if (entity == null) {
				entity = walletDAO.getEntity(playerID);
				ioMessage.getInputParse().put(WalletEntity.class.getName(), entity);
			}
		} else {
			entity = walletDAO.getEntity(playerID);
		}
		return entity;
	}

	/**
	 * 获取walletEntity
	 * */
	public WalletEntity getwalletEntity(String playerID) {
		WalletEntity walletEntity = walletDAO.getEntity(playerID);
		/** 临时 */
		if (walletEntity == null) {
			logger.error("初始化钱包");
			walletEntity = this.initWalletEntity(playerID);
			this.saveWalletEntity(walletEntity);
		}
		return walletDAO.getEntity(playerID);
	}

	/**
	 * 保存walletEntity
	 * */
	public void saveWalletEntity(WalletEntity walletEntity) {
		walletDAO.save(walletEntity);
	}

	/**
	 * 扣除货币
	 * */
	public WalletEntity consumeCurrencyWalletEntity(String playerID, int type, int currency, boolean isSave, IOMessage ioMessage) {
		WalletEntity walletEntity = null;
		switch (type) {
		case KindIDs.GOLDTYPE:
			walletEntity = this.consumeGoldWalletEntity(playerID, currency, ioMessage);
			// 消费送礼活动
			EventModule eventModule = ModuleManager.getModule(ModuleNames.EventModule, EventModule.class);
			eventModule.intefaceEventExpense(playerID, currency);
			// 每日消费活动
			eventModule.intefaceEventDailyExpense(playerID, currency);
			break;
		case KindIDs.SILVERTYPE:
			walletEntity = this.consumeSilverWalletEntity(playerID, currency, ioMessage);
			break;
		case KindIDs.HEROSOUL:
			walletEntity = this.consumeSoulWalletEntity(playerID, currency, ioMessage);
			break;
		case KindIDs.JEWELSOUL:
			walletEntity = this.consumeJewelSoulWalletEntity(playerID, currency, ioMessage);
			break;
		case KindIDs.REPUTATION:
			walletEntity = this.consumeReputationlWalletEntity(playerID, currency, ioMessage);
			break;
		}
		if (isSave) {
			this.saveWalletEntity(walletEntity);
		}
		return walletEntity;
	}

	/**
	 * 扣除金币
	 * */
	private synchronized WalletEntity consumeGoldWalletEntity(String playerID, int currency, IOMessage ioMessage) {
		WalletEntity walletEntity = this.getWalletEntity(playerID, ioMessage);
		long gold = walletEntity.getGold();
		gold = gold - currency;
		if (gold < 0) {
			throw new IllegalArgumentException(ErrorIds.NotEnoughGold + "");
		}
		walletEntity.setGold(gold);
		return walletEntity;
	}

	/**
	 * 扣除银币
	 * */
	private synchronized WalletEntity consumeSilverWalletEntity(String playerID, int currency, IOMessage ioMessage) {
		WalletEntity walletEntity = this.getWalletEntity(playerID, ioMessage);
		long silver = walletEntity.getSilver();
		silver = silver - currency;
		if (silver < 0) {
			throw new IllegalArgumentException(ErrorIds.NotEnoughSilver + "");
		}
		walletEntity.setSilver(silver);
		return walletEntity;
	}

	/**
	 * 扣除将魂
	 * */
	private synchronized WalletEntity consumeSoulWalletEntity(String playerID, int currency, IOMessage ioMessage) {
		WalletEntity walletEntity = this.getWalletEntity(playerID, ioMessage);
		long soul = walletEntity.getHeroSoul();
		soul = soul - currency;
		if (soul < 0) {
			throw new IllegalArgumentException(ErrorIds.NotEnoughHeroSoul + "");
		}
		walletEntity.setHeroSoul(soul);
		return walletEntity;
	}

	/**
	 * 扣除魂玉
	 * */
	private synchronized WalletEntity consumeJewelSoulWalletEntity(String playerID, int currency, IOMessage ioMessage) {
		WalletEntity walletEntity = this.getWalletEntity(playerID, ioMessage);
		long soul = walletEntity.getJewelSoul();
		soul = soul - currency;
		if (soul < 0) {
			throw new IllegalArgumentException(ErrorIds.NotEnoughJewelSoul + "");
		}
		walletEntity.setJewelSoul(soul);
		return walletEntity;
	}

	/**
	 * 扣除声望
	 * */
	private synchronized WalletEntity consumeReputationlWalletEntity(String playerID, int currency, IOMessage ioMessage) {
		WalletEntity walletEntity = this.getWalletEntity(playerID, ioMessage);
		long reputation = walletEntity.getReputation();
		reputation = reputation - currency;
		if (reputation < 0) {
			throw new IllegalArgumentException(ErrorIds.NotEnoughReputation + "");
		}
		walletEntity.setReputation(reputation);
		return walletEntity;
	}

	/**
	 * 增加货币
	 * */
	public WalletEntity addCurrencyWalletEntity(String playerID, int type, int currency, boolean isSave, IOMessage ioMessage) {
		WalletEntity walletEntity = null;
		switch (type) {
		case KindIDs.GOLDTYPE:
			walletEntity = this.addGoldWalletEntity(playerID, currency, ioMessage);
			break;
		case KindIDs.SILVERTYPE:
			walletEntity = this.addSilverWalletEntity(playerID, currency, ioMessage);
			break;
		case KindIDs.HEROSOUL:
			walletEntity = this.addSoulWalletEntity(playerID, currency, ioMessage);
			break;
		case KindIDs.JEWELSOUL:
			walletEntity = this.addJewelSoulWalletEntity(playerID, currency, ioMessage);
			break;
		case KindIDs.REPUTATION:
			walletEntity = this.addReputationWalletEntity(playerID, currency, ioMessage);
			break;
		}
		if (isSave) {
			saveWalletEntity(walletEntity);
		}
		return walletEntity;
	}

	/**
	 * 增加金币
	 * */
	private WalletEntity addGoldWalletEntity(String playerID, int currency, IOMessage ioMessage) {
		WalletEntity walletEntity = this.getWalletEntity(playerID, ioMessage);
		long gold = walletEntity.getGold();
		gold = gold + currency;
		if (gold < 0) {
			gold = 0;
		}
		if (gold > Long.MAX_VALUE) {
			gold = Long.MAX_VALUE;
		}
		walletEntity.setGold(gold);
		return walletEntity;
	}

	/**
	 * 增加银币
	 * */
	private WalletEntity addSilverWalletEntity(String playerID, int currency, IOMessage ioMessage) {
		WalletEntity walletEntity = this.getWalletEntity(playerID, ioMessage);
		long silver = walletEntity.getSilver();
		silver = silver + currency;
		if (silver < 0) {
			silver = 0;
		}
		if (silver > Long.MAX_VALUE) {
			silver = Long.MAX_VALUE;
		}
		walletEntity.setSilver(silver);
		return walletEntity;
	}

	/**
	 * 增加将魂
	 * */
	private WalletEntity addSoulWalletEntity(String playerID, int currency, IOMessage ioMessage) {
		WalletEntity walletEntity = this.getWalletEntity(playerID, ioMessage);
		long soul = walletEntity.getHeroSoul();
		if (soul < 0) {
			soul = 0;
		}
		if (soul > Long.MAX_VALUE) {
			soul = Long.MAX_VALUE;
		}
		soul = soul + currency;
		walletEntity.setHeroSoul(soul);
		return walletEntity;
	}

	/**
	 * 增加魂玉
	 * */
	private WalletEntity addJewelSoulWalletEntity(String playerID, int currency, IOMessage ioMessage) {
		WalletEntity walletEntity = this.getWalletEntity(playerID, ioMessage);
		long soul = walletEntity.getJewelSoul();
		soul = soul + currency;
		if (soul < 0) {
			soul = 0;
		}
		if (soul > Long.MAX_VALUE) {
			soul = Long.MAX_VALUE;
		}
		walletEntity.setJewelSoul(soul);
		return walletEntity;
	}

	/**
	 * 增加声望
	 * */
	private WalletEntity addReputationWalletEntity(String playerID, int currency, IOMessage ioMessage) {
		WalletEntity walletEntity = this.getWalletEntity(playerID, ioMessage);
		long reputation = walletEntity.getReputation();
		if (reputation < 0) {
			reputation = 0;
		}
		if (reputation > Long.MAX_VALUE) {
			reputation = Long.MAX_VALUE;
		}
		reputation = reputation + currency;
		walletEntity.setReputation(reputation);
		return walletEntity;
	}

	/***
	 * 传入参数的验证货币是否充足
	 * 
	 * @param playerID
	 * @param type
	 *            货币类型
	 * @param currency
	 *            所需货币数量
	 * @param IoMessage
	 *            ioMessage 消息传输集合
	 * @return 如果货币充足，则返回0。否则，返回错误代码
	 * */

	public int verifyCurrency(String playerID, int type, int currency, IOMessage ioMessage) {
		WalletEntity entity = this.getWalletEntity(playerID, ioMessage);
		long money = 0;
		int code = 0;
		switch (type) {
		case KindIDs.GOLDTYPE:
			money = entity.getGold();
			code = ErrorIds.NotEnoughGold;
			break;
		case KindIDs.SILVERTYPE:
			money = entity.getSilver();
			code = ErrorIds.NotEnoughSilver;
			break;
		case KindIDs.JEWELSOUL:
			money = entity.getJewelSoul();
			code = ErrorIds.NotEnoughJewelSoul;
			break;
		case KindIDs.HEROSOUL:
			money = entity.getHeroSoul();
			code = ErrorIds.NotEnoughHeroSoul;
			break;
		case KindIDs.REPUTATION:
			money = entity.getReputation();
			code = ErrorIds.NotEnoughReputation;
			break;
		default:
			money = 0;
			code = ErrorIds.NotEnoughGold;
			break;
		}
		if (money < currency) {
			return code;
		}
		return 0;
	}
}
