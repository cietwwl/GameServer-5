package com.mi.game.module.pk.protocol;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mi.core.protocol.BaseProtocol;
import com.mi.game.module.pk.pojo.PkPlayer;
import com.mi.game.module.reward.data.GoodsBean;

public class PkProtocol extends BaseProtocol {

	private Map<String, PkPlayer> pkMap; // 找到的比武玩家

	private List<PkPlayer> pkLostList; // 复仇对象

	private PkPlayer playerInfo; // 玩家比武信息

	private List<PkPlayer> topTen; // 积分排行榜前十名

	private Map<String, Integer> historyMap; // 物品兑换记录

	private Map<String, Object> itemMap;

	private List<GoodsBean> showMap;

	public List<GoodsBean> getShowMap() {
		return showMap;
	}

	public void setShowMap(List<GoodsBean> showMap) {
		this.showMap = showMap;
	}

	private int nowReward; // 当前荣誉值

	public int getNowReward() {
		return nowReward;
	}

	public void setNowReward(int nowReward) {
		this.nowReward = nowReward;
	}

	public Map<String, Object> getItemMap() {
		return itemMap;
	}

	public void setItemMap(Map<String, Object> itemMap) {
		this.itemMap = itemMap;
	}

	public Map<String, Integer> getHistoryMap() {
		return historyMap;
	}

	public void setHistoryMap(Map<String, Integer> historyMap) {
		this.historyMap = historyMap;
	}

	public List<PkPlayer> getTopTen() {
		return topTen;
	}

	public void setTopTen(List<PkPlayer> topTen) {
		this.topTen = topTen;
	}

	public PkPlayer getPlayerInfo() {
		return playerInfo;
	}

	public void setPlayerInfo(PkPlayer playerInfo) {
		this.playerInfo = playerInfo;
	}

	public List<PkPlayer> getPkLostList() {
		return pkLostList;
	}

	public void setPkLostList(List<PkPlayer> pkLostList) {
		this.pkLostList = pkLostList;
	}


	public Map<String, PkPlayer> getPkMap() {
		return pkMap;
	}

	public void setPkMap(Map<String, PkPlayer> pkMap) {
		this.pkMap = pkMap;
	}

	@Override
	public Map<String, Object> responseMap(int type) {
		Map<String, Object> data = new HashMap<String, Object>();
		if (playerInfo != null) {
			data.put("playerInfo", playerInfo);
		}
		data.put("nowReward", nowReward);
		if (pkMap != null) {
			data.put("pkMap", pkMap);
		}
		if (pkLostList != null) {
			data.put("pkLostList", pkLostList);
		}

		if (topTen != null) {
			data.put("topTen", topTen);
		}

		if (historyMap != null) {
			data.put("historyMap", historyMap);
		}
		if (itemMap != null) {
			data.put("itemMap", itemMap);
		}
		if (showMap != null) {
			data.put("showMap", showMap);
		}
		return data;
	}
}
