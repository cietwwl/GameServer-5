package com.mi.game.module.hero.protocol;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mi.core.protocol.BaseProtocol;
import com.mi.game.defines.HandlerIds;
import com.mi.game.module.equipment.pojo.Equipment;
import com.mi.game.module.hero.pojo.Hero;
import com.mi.game.module.hero.pojo.HeroShard;
import com.mi.game.module.hero.pojo.HeroSkinEntity;
import com.mi.game.module.heroDraw.pojo.HeroDrawEntity;
import com.mi.game.module.reward.data.GoodsBean;
import com.mi.game.module.talisman.pojo.TalismanEntity;
import com.mi.game.util.CommonMethod;

/**
 * @author 刘凯旋	
 *
 * 2014年6月26日 下午4:52:43
 */
public class HeroInfoProtocol extends BaseProtocol{
	private List<Long> teamList;
	private List<Long> troops;
	private List<Hero> heros;
	private Map<String,Object> itemMap;
	private Hero hero;
	private int maxSellNum;
	private HeroShard heroShard;
	private Map<String,GoodsBean> showMap;
	private Hero changeHero;
	private List<Equipment> equipmentList;
	private List<TalismanEntity> talismanList; 
	private HeroDrawEntity heroDrawEntity;
	private HeroSkinEntity heroSkinEntity;
	private int valentineGold;

	public HeroSkinEntity getHeroSkinEntity() {
		return heroSkinEntity;
	}
	public void setHeroSkinEntity(HeroSkinEntity heroSkinEntity) {
		this.heroSkinEntity = heroSkinEntity;
	}
	public HeroDrawEntity getHeroDrawEntity() {
		return heroDrawEntity;
	}
	public void setHeroDrawEntity(HeroDrawEntity heroDrawEntity) {
		this.heroDrawEntity = heroDrawEntity;
	}
	public List<Equipment> getEquipmentList() {
		return equipmentList;
	}
	public void setEquipmentList(List<Equipment> equipmentList) {
		this.equipmentList = equipmentList;
	}
	public List<TalismanEntity> getTalismanList() {
		return talismanList;
	}
	public void setTalismanList(List<TalismanEntity> talismanList) {
		this.talismanList = talismanList;
	}
	public Hero getChangeHero() {
		return changeHero;
	}
	public void setChangeHero(Hero changeHero) {
		this.changeHero = changeHero;
	}
	public Map<String, GoodsBean> getShowMap() {
		return showMap;
	}
	public void setShowMap(Map<String, GoodsBean> showMap) {
		this.showMap = showMap;
	}
	public Hero getHero() {
		return hero;
	}
	public void setHero(Hero hero) {
		this.hero = hero;
	}
	public List<Long> getTroops() {
		return troops;
	}
	public void setTroops(List<Long> troops) {
		this.troops = troops;
	}
	public List<Long> getTeamList() {
		return teamList;
	}
	public void setTeamList(List<Long> teamList) {
		this.teamList = teamList;
	}
	public List<Hero> getHeros() {
		return heros;
	}
	public void setHeros(List<Hero> heros) {
		this.heros = heros;
	}
	public Map<String, Object> getItemMap() {
		return itemMap;
	}
	public void setItemMap(Map<String, Object> itemMap) {
		this.itemMap = itemMap;
	}
	
	public int getMaxSellNum() {
		return maxSellNum;
	}
	public void setMaxSellNum(int maxSellNum) {
		this.maxSellNum = maxSellNum;
	}
	public HeroShard getHeroShard() {
		return heroShard;
	}
	public void setHeroShard(HeroShard heroShard) {
		this.heroShard = heroShard;
	}
	public int getValentineGold() {
		return valentineGold;
	}
	public void setValentineGold(int valentineGold) {
		this.valentineGold = valentineGold;
	}
	@Override
	public Map<String, Object> responseMap(int y) {
		Map<String, Object> response = new HashMap<String, Object>();
		switch(y){
			case HandlerIds.HeroAddToTeam :
				response.put("teamList",teamList);
				response.put("troops",troops);
				if(hero != null)
					response.put("hero", hero.responseMap());
			    if(changeHero != null)
			    	response.put("changeHero", changeHero.responseMap());
			    response.put("equipmentList", equipmentList);
			    response.put("talismanList", talismanList);
		    	break;
			case HandlerIds.ChangeTroops:
				response.put("troops", troops);
				break;
			case HandlerIds.DrawHero:
				if(heros != null)
				 response.put("heros", CommonMethod.getResponseListMap(heros));
				if(itemMap != null)
					response.put("itemMap",itemMap);
				if(showMap != null)
					response.put("showMap",showMap.values());
				if(heroDrawEntity != null)
					response.put("heroDrawEntity",heroDrawEntity.responseMap());
				if(valentineGold != 0)
					response.put("valentineGold", valentineGold);
				break;
			case HandlerIds.StrengHero:
				if(hero != null)
					 response.put("hero", hero.responseMap());
				if(itemMap != null)
						response.put("itemMap",itemMap);
				break;
			case HandlerIds.AdvanceHero:
				if(hero != null)
					 response.put("hero", hero.responseMap());
				if(itemMap != null)
					response.put("itemMap",itemMap);
				break;
			case HandlerIds.SellHero:
				if(itemMap != null)
					response.put("itemMap",itemMap);
				if(showMap != null)
					response.put("showMap", showMap.values());
				break;
			case HandlerIds.ExpandHeroBag:
				if(itemMap != null){
					response.put("itemMap",itemMap);
				}
				response.put("maxSellNum", maxSellNum);
				break;
			case HandlerIds.CompoundHero:
				if(heroShard != null)
					response.put("heroShard", heroShard);
				if(itemMap != null)
					response.put("itemMap", itemMap);
				break;
			case HandlerIds.ChangeHeroSkin:
				if(heroSkinEntity != null)
					response.put("heroSkinEntity", heroSkinEntity.responseMap());
				break;
			case HandlerIds.getHeroSkin:
				if(heroSkinEntity != null)
					response.put("heroSkinEntity",heroSkinEntity.responseMap());
				break;
		}
		return response;
	}
}
