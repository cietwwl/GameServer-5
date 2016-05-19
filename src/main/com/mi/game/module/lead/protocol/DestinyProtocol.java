package com.mi.game.module.lead.protocol;

import java.util.HashMap;
import java.util.Map;

import com.mi.core.protocol.BaseProtocol;
import com.mi.game.defines.HandlerIds;
import com.mi.game.module.hero.pojo.Hero;
import com.mi.game.module.lead.pojo.LeadDesitnyEntity;

public class DestinyProtocol extends BaseProtocol{
	private LeadDesitnyEntity entity;
	private Map<String,Object> itemMap;
	private ExpResponse expResponse;
	private int num;
	private Hero lead;
	@Override
	public Map<String,Object> responseMap(int y){
		Map<String, Object> data = new HashMap<String, Object>();
		switch (y) {
		case HandlerIds.destinyAdd:
			if(entity != null)
				data.put("destinyEntity", entity.responseMap());
			if(itemMap != null)
				data.put("itemMap", itemMap);
			if(lead != null)
				data.put("lead", lead.responseMap());
		case HandlerIds.AddHeroExp:
			if(expResponse != null)
				data.put("expResponse", expResponse);
			if(itemMap != null)
				data.put("itemMap",itemMap);
			break;
		case HandlerIds.getDestinyNum:
			data.put("num",num);
			break;
		default:
			break;
		}
		return data;
	}
	
	public Hero getLead() {
		return lead;
	}

	public void setLead(Hero lead) {
		this.lead = lead;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public ExpResponse getExpResponse() {
		return expResponse;
	}

	public void setExpResponse(ExpResponse expResponse) {
		this.expResponse = expResponse;
	}

	public Map<String, Object> getItemMap() {
		return itemMap;
	}

	public void setItemMap(Map<String, Object> itemMap) {
		this.itemMap = itemMap;
	}

	public LeadDesitnyEntity getEntity() {
		return entity;
	}

	public void setEntity(LeadDesitnyEntity entity) {
		this.entity = entity;
	}
	
}
