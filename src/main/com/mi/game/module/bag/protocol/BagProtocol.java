package com.mi.game.module.bag.protocol;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.mi.core.protocol.BaseProtocol;
import com.mi.game.defines.HandlerIds;
import com.mi.game.module.bag.pojo.BagItem;
import com.mi.game.module.reward.data.GoodsBean;

public class BagProtocol extends BaseProtocol{
	private Map<String,Object> itemMap;
	private int maxNum;
	private Collection<BagItem>  bagList;
	private Map<String,GoodsBean> showMap;
	@Override
	public Map<String, Object> responseMap(int y) {
		Map<String,Object> data = new HashMap<String, Object>();
		switch(y){
			case HandlerIds.ExpandBag:
				data.put("maxNum", maxNum);
				data.put("itemMap",itemMap);
				break;
			case HandlerIds.ItemSell:
				data.put("itemMap",itemMap);
				data.put("bagList",bagList);
				if(showMap != null)
					data.put("showMap",showMap.values());	
				break;
			case HandlerIds.UseItem:
				data.put("itemMap",itemMap);
				if(showMap != null)
					data.put("showMap",showMap.values());
				break;
		} 
		return data;
	}
	
	
	public Map<String, GoodsBean> getShowMap() {
		return showMap;
	}


	public void setShowMap(Map<String, GoodsBean> showMap) {
		this.showMap = showMap;
	}


	public Collection<BagItem> getBagList() {
		return bagList;
	}


	public void setBagList(Collection<BagItem> bagList) {
		this.bagList = bagList;
	}


	public int getMaxNum() {
		return maxNum;
	}


	public void setMaxNum(int maxNum) {
		this.maxNum = maxNum;
	}


	public Map<String, Object> getItemMap() {
		return itemMap;
	}

	public void setItemMap(Map<String, Object> itemMap) {
		this.itemMap = itemMap;
	}
	
}
