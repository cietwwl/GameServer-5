package com.mi.game.module.talisman.protocol;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mi.core.protocol.BaseProtocol;
import com.mi.game.defines.HandlerIds;
import com.mi.game.module.hero.pojo.Hero;
import com.mi.game.module.reward.data.GoodsBean;
import com.mi.game.module.talisman.pojo.TalismanEntity;
import com.mi.game.module.talisman.pojo.TalismanShard;
import com.mi.game.util.CommonMethod;

public class TalismanProtocol extends BaseProtocol{
	private TalismanEntity unEquipTalisman;
	private TalismanEntity equipTalisman;
	private Hero unEquipHero;
	private Hero equipHero;
	private Map<String, Object> itemMap;
	private List<Object> removeList;
	private List<PlunderInfo> plunderInfoList;
	private List<GoodsBean> showMap;
	private List<TalismanShard> shardList;
	private List<DrawInfo> drawList;
	private int maxNum;
	private long effectEndTime ;
	private int num;
	
	@Override
	public Map<String,Object>responseMap(int y){
		Map<String,Object> data = new HashMap<String, Object>();
		switch (y) {
		case HandlerIds.TalismanShardCompose:
			data.put("itemMap", itemMap);
			data.put("shardList", CommonMethod.getResponseListMap(shardList));
			break;
		case HandlerIds.PlunderTalismanList:
			data.put("plunderList", plunderInfoList);
			break;
		case HandlerIds.EquipTalisman:
			if(equipHero != null)
				data.put("equipHero", equipHero.responseMap());
			if(unEquipHero != null)
				data.put("unEquipHero", unEquipHero.responseMap());
			data.put("equipTalisman", equipTalisman);
			data.put("unEquipTalisman", unEquipTalisman);
			break;
		case HandlerIds.UnEquipTalisman:
			if(unEquipHero != null)
				data.put("unEquipHero", unEquipHero.responseMap());
			data.put("unEquipTalisman", unEquipTalisman);
			break;
		case HandlerIds.NewPlayerPlunderTalismanList:
			data.put("plunderList", plunderInfoList);
			break;
		case HandlerIds.getTalismanShardList:
			if (shardList != null)
				data.put("talismanShardMapEntity", CommonMethod.getResponseListMap(shardList));
			break;
		case HandlerIds.ContinuesPlunder :
			data.put("itemMap", itemMap);
			data.put("showMap", showMap);
			data.put("drawList", drawList);
			data.put("num",num);
			break;
		case HandlerIds.SellTalisman:
			data.put("itemMap", itemMap);
			data.put("showMap", showMap);
			break;
		default:
			break;
		}
		return data;
	}
	
	
	
	
	public int getNum() {
		return num;
	}




	public void setNum(int num) {
		this.num = num;
	}




	public long getEffectEndTime() {
		return effectEndTime;
	}

	public void setEffectEndTime(long effectEndTime) {
		this.effectEndTime = effectEndTime;
	}

	public int getMaxNum() {
		return maxNum;
	}

	public void setMaxNum(int maxNum) {
		this.maxNum = maxNum;
	}

	public List<DrawInfo> getDrawList() {
		return drawList;
	}

	public void setDrawList(List<DrawInfo> drawList) {
		this.drawList = drawList;
	}

	public List<TalismanShard> getShardList() {
		return shardList;
	}

	public void setShardList(List<TalismanShard> shardList) {
		this.shardList = shardList;
	}

	public List<GoodsBean> getShowMap() {
		return showMap;
	}

	public void setShowMap(List<GoodsBean> showMap) {
		this.showMap = showMap;
	}

	public List<PlunderInfo> getPlunderInfoList() {
		return plunderInfoList;
	}
	public void setPlunderInfoList(List<PlunderInfo> plunderInfoList) {
		this.plunderInfoList = plunderInfoList;
	}

	public List<Object> getRemoveList() {
		return removeList;
	}
	public void setRemoveList(List<Object> removeList) {
		this.removeList = removeList;
	}
	public Map<String, Object> getItemMap() {
		return itemMap;
	}
	public void setItemMap(Map<String, Object> itemMap) {
		this.itemMap = itemMap;
	}
	public Hero getUnEquipHero() {
		return unEquipHero;
	}
	public void setUnEquipHero(Hero unEquipHero) {
		this.unEquipHero = unEquipHero;
	}
	public Hero getEquipHero() {
		return equipHero;
	}
	public void setEquipHero(Hero equipHero) {
		this.equipHero = equipHero;
	}
	public TalismanEntity getEquipTalisman() {
		return equipTalisman;
	}
	public void setEquipTalisman(TalismanEntity equipTalisman) {
		this.equipTalisman = equipTalisman;
	}
	public TalismanEntity getUnEquipTalisman() {
		return unEquipTalisman;
	}
	public void setUnEquipTalisman(TalismanEntity unEquipTalisman) {
		this.unEquipTalisman = unEquipTalisman;
	}

	
	
}
