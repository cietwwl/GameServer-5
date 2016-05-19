package com.mi.game.module.event.dao;

import java.util.ArrayList;
import java.util.List;

import com.mi.core.cache.QueryInfo;
import com.mi.core.cache.QueryType;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.defines.ErrorIds;
import com.mi.game.module.event.pojo.EventVipDigoShopEntity;


public class EventVipDigoShopEntityDao extends
		AbstractBaseDAO<EventVipDigoShopEntity> {

	private static EventVipDigoShopEntityDao eventVipDigoShopEntityDao = new EventVipDigoShopEntityDao();

	private EventVipDigoShopEntityDao() {

	}

	public static EventVipDigoShopEntityDao getInstance() {
		return eventVipDigoShopEntityDao;
	}

	/**
	 * 查询某天是否有购买记录
	 * 
	 * @param playerID
	 *            玩家ID
	 * @param goodID
	 *            物品ID
	 * @param day
	 *            日期,格式：yyyyMMdd
	 * @return 0：没有；1：有
	 */
	public int checkCanBuy(String playerID, String pid, String day) {
		QueryInfo query = new QueryInfo();
		query.addQueryCondition("playerID", QueryType.EQUAL, playerID);
		query.addQueryCondition("pid", QueryType.EQUAL, Integer.valueOf(pid));
		query.addQueryCondition("day", QueryType.EQUAL, day);
		List<EventVipDigoShopEntity> list = queryList(query);
		if (list != null && list.size() > 0) {
			return ErrorIds.VIP_DIGO_HAD_BUY;
		}
		return 0;
	}

	/**
	 * 查询当天购买历史
	 * 
	 * @param playerID
	 *            玩家ID
	 * @param goodID
	 *            物品ID
	 * @param day
	 *            日期,格式：yyyyMMdd
	 * @return 0：没有；1：有
	 */
	public List<Integer> getBuyHistory(String playerID, String day) {
		QueryInfo query = new QueryInfo();
		query.addQueryCondition("playerID", QueryType.EQUAL, playerID);
		query.addQueryCondition("day", QueryType.EQUAL, day);
		List<EventVipDigoShopEntity> list = queryList(query);
		List<Integer> buyList = new ArrayList<Integer>();
		if (list != null && list.size() > 0) {
			for (EventVipDigoShopEntity shopEntity : list) {
				buyList.add(shopEntity.getPid());
			}
		}
		return buyList;
	}
}
