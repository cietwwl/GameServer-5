package com.mi.game.module.equipment.protocol;

import java.util.HashMap;
import java.util.List;
import java.util.Map;




import com.mi.core.protocol.BaseProtocol;
import com.mi.game.defines.HandlerIds;
import com.mi.game.module.equipment.pojo.Equipment;
import com.mi.game.module.equipment.pojo.EquipmentShard;
import com.mi.game.module.hero.pojo.Hero;
import com.mi.game.module.reward.data.GoodsBean;
import com.mi.game.module.talisman.pojo.TalismanEntity;

public class EquipmentProtocol extends BaseProtocol{
	private Hero equipHero;                
	private Hero unEquipHero;
	private Equipment equipment;
	private Equipment unEquipment;
	private Map<String, GoodsBean> showMap;
	private Map<String, Object> itemMap;
	private List<Integer> strengList;
	private int maxEquipNum;
	private EquipmentShard shard;
	private List<Object> removeList;
	private Map<String,Double> refineShowMap; 
	private int maxShardNum;
	private Map<Long,Equipment> equipList;
	private List<Equipment> autoEquipList ;
	private Map<Long,TalismanEntity> talismanList;
	private Map<Long,Map<String,Object>> upList ;
 	@Override
	public Map<String, Object> responseMap(int y) {
		Map<String, Object> response = new HashMap<String, Object>();
		switch(y){
			case HandlerIds.DoEquip :
				if(equipHero != null)
					response.put("equipHero",equipHero.responseMap());
				if(unEquipHero != null)
					response.put("unEquipHero",unEquipHero.responseMap());
				if(equipment != null)
					response.put("equipment", equipment);
				if(unEquipment != null)
					response.put("unEquipment", unEquipment);
			break;
			case HandlerIds.UnEquip:
				if(equipHero != null)
					response.put("equipHero",equipHero.responseMap());
				if(equipment != null)
					response.put("equipment", equipment);
			break;
			case HandlerIds.StrengEquip:
				if(itemMap != null)
					response.put("itemMap", itemMap);
				if(equipment != null)
					response.put("equipment", equipment);
				if(strengList != null)
					response.put("strengList", strengList);
			break;
			case HandlerIds.SellEquip:
				if(itemMap != null)
					response.put("itemMap", itemMap);
				if(showMap != null )
					response.put("showMap", showMap.values());
				break;
			case HandlerIds.AtuoEquip:
				if(equipHero != null)
					response.put("equipHero",equipHero.responseMap());
				if(equipList != null)
					response.put("equipList",equipList.values());
				if(talismanList !=  null)
					response.put("talismanList",talismanList.values());
			break;
			case HandlerIds.ExpandEquipBag:
				if(itemMap != null)
					response.put("itemMap", itemMap);
				response.put("maxEquipNum", maxEquipNum);
				break;
			case HandlerIds.EquipShardCompose:
				if(itemMap != null)
					response.put("itemMap", itemMap);
				if(shard != null)
					response.put("shard", shard);
				break;
			case HandlerIds.EquipmentRefine:
				if(itemMap != null)
					response.put("itemMap", itemMap);
				if(refineShowMap != null)
					response.put("refineShowMap", refineShowMap);
				break;
			case HandlerIds.DoRefine:
				if(equipment != null)
					response.put("equipment", equipment);
				break;
			case HandlerIds.ExpandEquipShardBag:
				if(itemMap != null)
					response.put("itemMap", itemMap);
				response.put("maxShardNum", maxShardNum);
				break;
			case HandlerIds.SellEquipmentShard:
				if(itemMap != null)
					response.put("itemMap", itemMap);
				if(showMap != null )
					response.put("showMap", showMap.values());	
					break;
			case HandlerIds.AutoStrengEquipment:
				if(itemMap != null)
					response.put("itemMap", itemMap);
				if(upList != null)
					response.put("upList", upList.values());
				if(autoEquipList != null)
					response.put("equipList",autoEquipList);
				break;
		}
		return response;
	}
	
	
	public List<Equipment> getAutoEquipList() {
		return autoEquipList;
	}


	public void setAutoEquipList(List<Equipment> autoEquipList) {
		this.autoEquipList = autoEquipList;
	}


	public Map<Long, Map<String, Object>> getUpList() {
		return upList;
	}


	public void setUpList(Map<Long, Map<String, Object>> upList) {
		this.upList = upList;
	}

	public Map<Long, Equipment> getEquipList() {
		return equipList;
	}


	public void setEquipList(Map<Long, Equipment> equipList) {
		this.equipList = equipList;
	}



	public int getMaxShardNum() {
		return maxShardNum;
	}


	public void setMaxShardNum(int maxShardNum) {
		this.maxShardNum = maxShardNum;
	}


	public Map<String, Double> getRefineShowMap() {
		return refineShowMap;
	}


	public void setRefineShowMap(Map<String, Double> refineShowMap) {
		this.refineShowMap = refineShowMap;
	}


	public List<Object> getRemoveList() {
		return removeList;
	}

	public void setRemoveList(List<Object> removeList) {
		this.removeList = removeList;
	}

	public Map<Long, TalismanEntity> getTalismanList() {
		return talismanList;
	}

	public void setTalismanList(Map<Long, TalismanEntity> talismanList) {
		this.talismanList = talismanList;
	}

	public EquipmentShard getShard() {
		return shard;
	}
	public void setShard(EquipmentShard shard) {
		this.shard = shard;
	}
	public int getMaxEquipNum() {
		return maxEquipNum;
	}
	public void setMaxEquipNum(int maxEquipNum) {
		this.maxEquipNum = maxEquipNum;
	}
	public List<Integer> getStrengList() {
		return strengList;
	}
	public void setStrengList(List<Integer> strengList) {
		this.strengList = strengList;
	}
	public Map<String, GoodsBean> getShowMap() {
		return showMap;
	}
	public void setShowMap(Map<String, GoodsBean> showMap) {
		this.showMap = showMap;
	}
	public Map<String, Object> getItemMap() {
		return itemMap;
	}
	public void setItemMap(Map<String, Object> itemMap) {
		this.itemMap = itemMap;
	}
	public Equipment getUnEquipment() {
		return unEquipment;
	}
	public void setUnEquipment(Equipment unEquipment) {
		this.unEquipment = unEquipment;
	}
	public Equipment getEquipment() {
		return equipment;
	}
	public void setEquipment(Equipment equipment) {
		this.equipment = equipment;
	}
	public Hero getEquipHero() {
		return equipHero;
	}
	public void setEquipHero(Hero equipHero) {
		this.equipHero = equipHero;
	}
	public Hero getUnEquipHero() {
		return unEquipHero;
	}
	public void setUnEquipHero(Hero unEquipHero) {
		this.unEquipHero = unEquipHero;
	}
	
}
