package com.mi.game.module.limitShop.dao;

import java.util.ArrayList;
import java.util.List;

import com.mi.core.cache.QueryInfo;
import com.mi.core.cache.QueryType;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.limitShop.pojo.LimitShopEntity;

public class LimitShopEntityDao extends
 AbstractBaseDAO<LimitShopEntity> {

	private static LimitShopEntityDao limitShopEntityDao = new LimitShopEntityDao();

	private LimitShopEntityDao() {
	}

	public static LimitShopEntityDao getInstance() {
		return limitShopEntityDao;
	}

	/**
	 * 根据日期、物品ID、玩家ID查询购买记录
	 * 
	 * @param playerID
	 *            玩家id
	 * @param goodsID
	 *            物品id
	 * @param day
	 *            日期,格式:yyyyMMdd
	 * @return
	 */
	public LimitShopEntity getShopHistory(String playerID, Integer goodsID,
			String day) {
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.addQueryCondition("playerID", QueryType.EQUAL, playerID);
		queryInfo.addQueryCondition("goodsID", QueryType.EQUAL, goodsID);
		queryInfo.addQueryCondition("day", QueryType.EQUAL, day);
		return this.query(queryInfo);
	}

	/**
	 * 根据日期获得购买商品的pid
	 * 
	 * @param playerID
	 *            玩家ID
	 * @param day
	 *            格式：yyyyMMdd
	 * @return
	 */
	public List<Integer> getBuyHistoryByDay(String playerID, String day) {
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.addQueryCondition("playerID", QueryType.EQUAL, playerID);
		queryInfo.addQueryCondition("day", QueryType.EQUAL, day);

		List<LimitShopEntity> shopList = this.queryList(queryInfo);
		List<Integer> idList = null;
		if (shopList != null && shopList.size() > 0) {
			idList = new ArrayList();
			for (LimitShopEntity shopEntity : shopList) {
				idList.add(shopEntity.getPid());
			}
		}
		return idList;
	}

}
