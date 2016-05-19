package com.mi.game.module.tower.protocol;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mi.core.protocol.BaseProtocol;
import com.mi.game.defines.HandlerIds;
import com.mi.game.module.login.pojo.PlayerStatusEntity;
import com.mi.game.module.reward.data.GoodsBean;
import com.mi.game.module.tower.pojo.TowerEntity;

public class TowerProtocol extends BaseProtocol{
	private TowerEntity entity;
	private Map<String,Object> itemMap;
	private PlayerStatusEntity playerStatusEntity;
	private List<TowerTopInfo> topList;
	private List<GoodsBean> showMap;
	
	@Override
	public Map<String,Object> responseMap(int y){
		Map<String, Object> data = new HashMap<String, Object>();
		switch(y){
		case HandlerIds.getTowerEntity:
			if(entity != null)
				data.put("towerEntity",entity.responseMap());
			if(playerStatusEntity != null)
				data.put("playerStatusEntity", playerStatusEntity.responseMap());
			break;
		case HandlerIds.challenegeTower:
			if(entity != null)
				data.put("towerEntity",entity.responseMap());
			if(itemMap != null)
				data.put("itemMap", itemMap);
			if(showMap != null)
				data.put("showMap", showMap);
			break;
		case HandlerIds.resetTower:
			if(entity != null)
				data.put("towerEntity",entity.responseMap());
			if(itemMap != null)
				data.put("itemMap", itemMap);
			break;
		case HandlerIds.clearTower:
			if(entity != null)
				data.put("towerEntity", entity.responseMap());
			break;
		case HandlerIds.cancelClear:
			if(entity != null)
				data.put("towerEntity", entity.responseMap());
			if(playerStatusEntity != null)
				data.put("playerStatusEntity", playerStatusEntity.responseMap());
			break;
		case HandlerIds.buyHeartNum:
			if(entity != null)
				data.put("towerEntity",entity.responseMap());
			if(itemMap != null)
				data.put("itemMap", itemMap);
			break;
		case HandlerIds.getTowerTopList:
				data.put("topList", topList);
			break;
		case HandlerIds.challenegeHideTower:
			if(itemMap != null)
				data.put("itemMap", itemMap);
			data.put("showMap",showMap);
			break;
		case HandlerIds.towerQuicklyComplete:
			if(itemMap != null )
				data.put("itemMap", itemMap);
			if(entity != null)
				data.put("towerEntity",entity.responseMap());
			if(playerStatusEntity != null)
				data.put("playerStatusEntity", playerStatusEntity.responseMap());
			break;
		}
		return data;
	}
	
	
	public List<GoodsBean> getShowMap() {
		return showMap;
	}


	public void setShowMap(List<GoodsBean> showMap) {
		this.showMap = showMap;
	}


	public List<TowerTopInfo> getTopList() {
		return topList;
	}


	public void setTopList(List<TowerTopInfo> topList) {
		this.topList = topList;
	}


	public PlayerStatusEntity getPlayerStatusEntity() {
		return playerStatusEntity;
	}


	public void setPlayerStatusEntity(PlayerStatusEntity playerStatusEntity) {
		this.playerStatusEntity = playerStatusEntity;
	}


	public TowerEntity getEntity() {
		return entity;
	}

	public void setEntity(TowerEntity entity) {
		this.entity = entity;
	}
	public Map<String, Object> getItemMap() {
		return itemMap;
	}
	public void setItemMap(Map<String, Object> itemMap) {
		this.itemMap = itemMap;
	}
	

}
