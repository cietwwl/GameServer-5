package com.mi.game.module.dungeon.protocol;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mi.core.protocol.BaseProtocol;
import com.mi.game.defines.HandlerIds;
import com.mi.game.module.dungeon.pojo.ActLimitRewardMapEntity;
import com.mi.game.module.dungeon.pojo.Dungeon;
import com.mi.game.module.dungeon.pojo.DungeonAct;
import com.mi.game.module.dungeon.pojo.DungeonActMapEntity;
import com.mi.game.module.dungeon.pojo.DungeonActive;
import com.mi.game.module.dungeon.pojo.DungeonActiveEntity;
import com.mi.game.module.dungeon.pojo.DungeonEliteEntity;
import com.mi.game.module.dungeon.pojo.DungeonMapEntity;
import com.mi.game.module.pay.pojo.PayOrderEntity;
import com.mi.game.module.reward.data.GoodsBean;
import com.mi.game.module.reward.pojo.RewardCenterEntity;


/**
 * @author 刘凯旋	
 *
 * 2014年10月24日 下午2:11:25
 */
/**
 * @author Administrator
 *
 */
public class DungeonProtocol extends BaseProtocol{
	private Map<Integer,GoodsBean> showMap ;
	private Map<String,Object> itemMap;
	private Dungeon nextDungeon;
	private Dungeon nowDungeon;
	private List<GoodsBean> dropList;
	private Map<Integer,GoodsBean> specialDropList;
	private DungeonAct nowDungeonAct;
	private DungeonAct nextDungeonAct;
	private int openEliteID;
	private int attackNum;
	private int payNum;
	private DungeonActive active ;
	private RewardCenterEntity rewardCenterEntity;
	private int maxGameLevelID;
	private boolean mystery;
	private long lastContinuousTime;
	private List<DungeonTopInfo> topList;
	private int starNum;
	private int continuousPayNum;
	private DungeonActiveEntity dungeonActiveEntity;
	private DungeonEliteEntity dungeonEliteEntity;
	private DungeonMapEntity dungeonMapEntity;
	private DungeonActMapEntity dungeonActMapEntity;
	private ActLimitRewardMapEntity actLimitRewardMapEntity;
	private PayOrderEntity payOrderEntity;
	private int maxEliteID;
	
	public int getMaxEliteID() {
		return maxEliteID;
	}
	public void setMaxEliteID(int maxEliteID) {
		this.maxEliteID = maxEliteID;
	}
	public PayOrderEntity getPayOrderEntity() {
		return payOrderEntity;
	}
	public void setPayOrderEntity(PayOrderEntity payOrderEntity) {
		this.payOrderEntity = payOrderEntity;
	}
	public ActLimitRewardMapEntity getActLimitRewardMapEntity() {
		return actLimitRewardMapEntity;
	}
	public void setActLimitRewardMapEntity(
			ActLimitRewardMapEntity actLimitRewardMapEntity) {
		this.actLimitRewardMapEntity = actLimitRewardMapEntity;
	}
	public DungeonActMapEntity getDungeonActMapEntity() {
		return dungeonActMapEntity;
	}
	public void setDungeonActMapEntity(DungeonActMapEntity dungeonActMapEntity) {
		this.dungeonActMapEntity = dungeonActMapEntity;
	}
	public DungeonEliteEntity getDungeonEliteEntity() {
		return dungeonEliteEntity;
	}
	public void setDungeonEliteEntity(DungeonEliteEntity dungeonEliteEntity) {
		this.dungeonEliteEntity = dungeonEliteEntity;
	}
	public DungeonActiveEntity getDungeonActiveEntity() {
		return dungeonActiveEntity;
	}
	public void setDungeonActiveEntity(DungeonActiveEntity dungeonActiveEntity) {
		this.dungeonActiveEntity = dungeonActiveEntity;
	}
	public int getContinuousPayNum() {
		return continuousPayNum;
	}
	public void setContinuousPayNum(int continuousPayNum) {
		this.continuousPayNum = continuousPayNum;
	}
	public int getStarNum() {
		return starNum;
	}
	public void setStarNum(int starNum) {
		this.starNum = starNum;
	}
	public List<DungeonTopInfo> getTopList() {
		return topList;
	}
	public void setTopList(List<DungeonTopInfo> topList) {
		this.topList = topList;
	}
	public long getLastContinuousTime() {
		return lastContinuousTime;
	}
	public void setLastContinuousTime(long lastContinuousTime) {
		this.lastContinuousTime = lastContinuousTime;
	}
	public int getMaxGameLevelID() {
		return maxGameLevelID;
	}
	public void setMaxGameLevelID(int maxGameLevelID) {
		this.maxGameLevelID = maxGameLevelID;
	}
	public RewardCenterEntity getRewardCenterEntity() {
		return rewardCenterEntity;
	}
	public void setRewardCenterEntity(RewardCenterEntity rewardCenterEntity) {
		this.rewardCenterEntity = rewardCenterEntity;
	}
	public DungeonActive getActive() {
		return active;
	}
	public void setActive(DungeonActive active) {
		this.active = active;
	}
	public int getPayNum() {
		return payNum;
	}
	public void setPayNum(int payNum) {
		this.payNum = payNum;
	}
	public int getAttackNum() {
		return attackNum;
	}
	public void setAttackNum(int attackNum) {
		this.attackNum = attackNum;
	}
	public int getOpenEliteID() {
		return openEliteID;
	}
	public void setOpenEliteID(int openEliteID) {
		this.openEliteID = openEliteID;
	}
	public List<GoodsBean> getDropList() {
		return dropList;
	}
	public void setDropList(List<GoodsBean> dropList) {
		this.dropList = dropList;
	}
	public Map<Integer, GoodsBean> getShowMap() {
		return showMap;
	}
	public void setShowMap(Map<Integer, GoodsBean> showMap) {
		this.showMap = showMap;
	}
	public Map<Integer, GoodsBean> getSpecialDropList() {
		return specialDropList;
	}
	public void setSpecialDropList(Map<Integer, GoodsBean> specialDropList) {
		this.specialDropList = specialDropList;
	}
	public Map<String, Object> getItemMap() {
		return itemMap;
	}
	public void setItemMap(Map<String, Object> itemMap) {
		this.itemMap = itemMap;
	}
	public Dungeon getNextDungeon() {
		return nextDungeon;
	}
	public void setNextDungeon(Dungeon nextDungeon) {
		this.nextDungeon = nextDungeon;
	}
	public Dungeon getNowDungeon() {
		return nowDungeon;
	}
	public void setNowDungeon(Dungeon nowDungeon) {
		this.nowDungeon = nowDungeon;
	}
	public DungeonAct getNowDungeonAct() {
		return nowDungeonAct;
	}
	public void setNowDungeonAct(DungeonAct nowDungeonAct) {
		this.nowDungeonAct = nowDungeonAct;
	}
	public DungeonAct getNextDungeonAct() {
		return nextDungeonAct;
	}
	public void setNextDungeonAct(DungeonAct nextDungeonAct) {
		this.nextDungeonAct = nextDungeonAct;
	}
	public boolean isMystery() {
		return mystery;
	}
	public void setMystery(boolean mystery) {
		this.mystery = mystery;
	}
	public DungeonMapEntity getDungeonMapEntity() {
		return dungeonMapEntity;
	}
	public void setDungeonMapEntity(DungeonMapEntity dungeonMapEntity) {
		this.dungeonMapEntity = dungeonMapEntity;
	}
	@Override
	public Map<String, Object> responseMap(int y) {
		Map<String, Object> response = new HashMap<String, Object>();
		switch(y){
			case HandlerIds.dungeonStart :
				if(nextDungeon != null)
					response.put("nextDungeon",nextDungeon.responseMap());
				if(nowDungeon != null)
					response.put("nowDungeon",nowDungeon.responseMap());
				if(itemMap != null)
					response.put("itemMap", itemMap);
				if(specialDropList != null)
					response.put("specialShowMap", specialDropList.values());
				if(showMap != null)
					response.put("showMap", showMap.values());
				if(nowDungeonAct != null)
					response.put("nowDungeonAct", nowDungeonAct.responseMap());
				if(nextDungeonAct != null)
					response.put("nextDungeonAct", nextDungeonAct.responseMap());
				if(maxGameLevelID != 0)
					response.put("maxGameLevelID", maxGameLevelID);
				response.put("mystery", mystery);
				response.put("starNum", starNum);
				if(actLimitRewardMapEntity != null)
					response.put("actLimitRewardMapEntity", actLimitRewardMapEntity.responseMap());
			break;
			case HandlerIds.recoverDungeonNum:
				if(itemMap != null)
					response.put("itemMap", itemMap);
				if(nowDungeon != null)
					response.put("nowDungeon",nowDungeon.responseMap());
				break;
			case HandlerIds.getActReward:
				if(nowDungeonAct != null)
					response.put("nowDungeonAct",nowDungeonAct.responseMap());
				if(itemMap != null)
					response.put("itemMap", itemMap);
				break;
			case HandlerIds.dungeonEliteStart:
				if(itemMap != null)
					response.put("itemMap", itemMap);
				if(showMap != null)
					response.put("showMap", showMap.values());
				if(openEliteID != 0)
					response.put("openEliteID", openEliteID);
				response.put("attackNum", attackNum);
				response.put("maxEliteID", maxEliteID);
				break;
			case HandlerIds.recoverDungeonEliteNum:
				if(itemMap != null)
					response.put("itemMap", itemMap);
				response.put("attackNum", attackNum);
				response.put("payNum", payNum);
				break;
			case HandlerIds.dungeonActiveStart:
				if(itemMap != null)
					response.put("itemMap", itemMap);
				if(active != null)
					response.put("active", active);
				if(showMap != null)
					response.put("showMap", showMap.values());
				break;
			case HandlerIds.ContinuousFight:
				if(itemMap != null)
					response.put("itemMap", itemMap);
				if(showMap != null)
					response.put("showMap", showMap.values());
				response.put("mystery", mystery);
				if(specialDropList != null)
					response.put("specialShowMap", specialDropList.values());
				if(nowDungeon != null)
					response.put("nowDungeon",nowDungeon.responseMap());
				response.put("lastContinuousTime", lastContinuousTime);
			 break;
			case HandlerIds.getDungeonTopList:
				response.put("topList", topList);
			break;
			case HandlerIds.clearContinuousCooldown:
				response.put("lastContinuousTime", lastContinuousTime);
				response.put("itemMap", itemMap);
				response.put("continuousPayNum", continuousPayNum);
			break;
			case HandlerIds.GetActiveDungeonInfoHandler:
				if(dungeonActiveEntity != null){
					response.put("dungeonActiveEntity", dungeonActiveEntity.responseMap());
				}
				break;
			case HandlerIds.GetEliteDungeonInfo:
				if(dungeonEliteEntity != null){
					response.put("dungeonEliteEntity",dungeonEliteEntity.responseMap());
				}
				break;
			case HandlerIds.getNormalDungeonInfo:
				if(dungeonMapEntity != null){
					if(dungeonMapEntity != null)
						response.put("dungeonMapEntity", dungeonMapEntity.responseMap());
					if(dungeonActMapEntity != null )
						response.put("dungeonActMapEntity", dungeonActMapEntity.responseMap());
					if(dungeonEliteEntity != null)
						response.put("dungeonEliteEntity", dungeonEliteEntity.responseMap());
				}
				break;
			case HandlerIds.ResurgenceHero :
				if(itemMap != null)
					response.put("itemMap", itemMap);
				break;
			case HandlerIds.BuyActLimitReward :
				if(itemMap != null)
					response.put("itemMap", itemMap);
				if(actLimitRewardMapEntity != null)
					response.put("actLimitRewardMapEntity", actLimitRewardMapEntity.responseMap());
				break;
		}
		return response;
	}
	
	
	
	
}
