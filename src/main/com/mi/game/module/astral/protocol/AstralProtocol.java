package com.mi.game.module.astral.protocol;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mi.core.protocol.BaseProtocol;
import com.mi.game.defines.HandlerIds;
import com.mi.game.module.astral.pojo.AstralEntity;
import com.mi.game.module.astral.pojo.AstralReward;

public class AstralProtocol extends BaseProtocol{
	private AstralEntity astralEntity;
	private Map<String,Object> itemMap;
	private int freeNum;
	private List<Integer> drawList; 
	private int itemDelNum;
	private AstralReward astralReward;
	private List<AstralReward> astralRewardList;
	
	@Override
	public Map<String,Object> responseMap(int type){
		Map<String,Object> data = new HashMap<String, Object>();
		switch(type){
			case HandlerIds.getAstralInfo:
				data.put("astralEntity", astralEntity);
				break;
			case HandlerIds.DoAstral:
				data.put("astralEntity", astralEntity);
				break;
			case HandlerIds.RefreshAstralList:
				data.put("freeNum",freeNum);
				data.put("drawList",drawList);
				data.put("itemDelNum", itemDelNum);
				if(itemMap != null)
					data.put("itemMap", itemMap);
				break;
			case HandlerIds.GetAstralReward:
				if(astralReward != null){
					data.put("astralReward", astralReward);
				if(itemMap != null)
					data.put("itemMap", itemMap);
				}
				break;
		}
		return data;
	}
	
	
	public List<AstralReward> getAstralRewardList() {
		return astralRewardList;
	}

	public void setAstralRewardList(List<AstralReward> astralRewardList) {
		this.astralRewardList = astralRewardList;
	}

	public AstralReward getAstralReward() {
		return astralReward;
	}

	public void setAstralReward(AstralReward astralReward) {
		this.astralReward = astralReward;
	}

	public int getFreeNum() {
		return freeNum;
	}

	public void setFreeNum(int freeNum) {
		this.freeNum = freeNum;
	}

	public List<Integer> getDrawList() {
		return drawList;
	}

	public void setDrawList(List<Integer> drawList) {
		this.drawList = drawList;
	}

	public int getItemDelNum() {
		return itemDelNum;
	}

	public void setItemDelNum(int itemDelNum) {
		this.itemDelNum = itemDelNum;
	}

	public AstralEntity getAstralEntity() {
		return astralEntity;
	}

	public void setAstralEntity(AstralEntity astralEntity) {
		this.astralEntity = astralEntity;
	}

	public Map<String, Object> getItemMap() {
		return itemMap;
	}

	public void setItemMap(Map<String, Object> itemMap) {
		this.itemMap = itemMap;
	}
	
	
}
