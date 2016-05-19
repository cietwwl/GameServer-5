package com.mi.game.module.arena.protocol;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mi.core.protocol.BaseProtocol;
import com.mi.game.defines.HandlerIds;
import com.mi.game.module.arena.pojo.ArenaShopInfo;
import com.mi.game.module.hero.pojo.HeroSkinEntity;
import com.mi.game.module.reward.data.GoodsBean;
import com.mi.game.module.talisman.protocol.DrawInfo;

public class ArenaProtocol extends BaseProtocol{
	private Map<String,ArenaInfo> arenaInfoList;
	private PKInfo pkInfo;
	private long rank;
	private Map<String,Object> itemMap;
	private List<GoodsBean> showMap;
	private List<DrawInfo> drawList;
	private ArenaShopInfo arenaShopInfo;
	private HeroSkinEntity heroSkinEntity;
	
	
	public HeroSkinEntity getHeroSkinEntity() {
		return heroSkinEntity;
	}

	public void setHeroSkinEntity(HeroSkinEntity heroSkinEntity) {
		this.heroSkinEntity = heroSkinEntity;
	}

	public ArenaShopInfo getArenaShopInfo() {
		return arenaShopInfo;
	}

	public void setArenaShopInfo(ArenaShopInfo arenaShopInfo) {
		this.arenaShopInfo = arenaShopInfo;
	}

	public List<DrawInfo> getDrawList() {
		return drawList;
	}

	public void setDrawList(List<DrawInfo> drawList) {
		this.drawList = drawList;
	}

	public Map<String, Object> getItemMap() {
		return itemMap;
	}

	public void setItemMap(Map<String, Object> itemMap) {
		this.itemMap = itemMap;
	}

	public List<GoodsBean> getShowMap() {
		return showMap;
	}

	public void setShowMap(List<GoodsBean> showMap) {
		this.showMap = showMap;
	}

	public long getRank() {
		return rank;
	}

	public void setRank(long rank) {
		this.rank = rank;
	}

	public PKInfo getPkInfo() {
		return pkInfo;
	}

	public void setPkInfo(PKInfo pkInfo) {
		this.pkInfo = pkInfo;
	}

	public Map<String, ArenaInfo> getArenaInfoList() {
		return arenaInfoList;
	}

	public void setArenaInfoList(Map<String, ArenaInfo> arenaInfoList) {
		this.arenaInfoList = arenaInfoList;
	}

	@Override
	public Map<String , Object> responseMap(int t){
		Map<String,Object> data = new HashMap<String, Object>();
		switch(t){
			case HandlerIds.getPkList:
				if(arenaInfoList != null)
				data.put("arenaList", arenaInfoList.values());
				data.put("rank", rank);
				break;
			case HandlerIds.getPkInfo:
				data.put("pkInfo", pkInfo);
				if(heroSkinEntity != null)
					data.put("heroSkinEntity", heroSkinEntity.responseMap());
				break;
			case HandlerIds.pkBattle:
				data.put("itemMap",itemMap);
				data.put("showMap",showMap);
				data.put("drawList", drawList);
		
				if(rank != 0)
					data.put("rank", rank);
				break;
			case HandlerIds.pkTopList:
				if(arenaInfoList != null)
					data.put("arenaList", arenaInfoList.values());
				break;
			case HandlerIds.exchangeArenaGoods:
				data.put("itemMap",itemMap);
				data.put("showMap",showMap);
				data.put("arenaShopInfo", arenaShopInfo);
				break;
		}
		return data;
	}



	
	
	
}
